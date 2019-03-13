package com.example.whatsmyallergy;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.whatsmyallergy.MainActivity.globalState;

public class GeoNamesApi extends AsyncTask<Void, Void, Void> {
    GoogleMap googleMap;

    public GeoNamesApi(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // Get nearby zip codes
            String postalCode = globalState.getPostalCode();

            if (globalState.getLastRun() != "" && globalState.getLastRun() == postalCode) { // not empty or last run is the same postal code
                Log.d("Print", "Last map run is " + globalState.getLastRun());
                return null;
            }

            globalState.lastMapRun = postalCode;
            int radius = 30;
            String uri = "http://api.geonames.org/findNearbyPostalCodesJSON?postalcode="+postalCode+"&country=US&radius="+radius+"&username=stam3";
            URL url = new URL(uri);
            String response = getRequestResponse(url);
            HashMap<String, String[]> nearbyPostalCodes = processPostalCodes(response);
            globalState.nearbyPostalCodes = nearbyPostalCodes;

            // get the nearby forecasts
            ArrayList<Marker> markers = new ArrayList<>();
            for (String key: globalState.nearbyPostalCodes.keySet()) {
                String lat = globalState.nearbyPostalCodes.get(key)[0];
                String lon = globalState.nearbyPostalCodes.get(key)[1];
                URL keyRequest = new URL("https://api.breezometer.com/pollen/v2/forecast/daily?lat="+lat+"&lon="+lon+"&key=54ed27411f414e63ad61001563d05b31&days=1");
                response = getRequestResponse(keyRequest);

                JSONObject forecastObject = new JSONObject(response);
                JSONObject data = forecastObject.getJSONArray("data").getJSONObject(0).getJSONObject("types");
                JSONObject[] types = {data.getJSONObject("grass"), data.getJSONObject("tree")};
                for (JSONObject type: types) {
                    if (type.getBoolean("in_season") && type.getBoolean("data_available")) {
                        globalState.nearbyPostalCodes.get(key)[2] = type.getJSONObject("index").getString("category") + " (" + type.getString("display_name") + ")\n";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                ArrayList<Marker> markers = new ArrayList<>();
                for (String key: globalState.nearbyPostalCodes.keySet()) {
                    String label = globalState.nearbyPostalCodes.get(key)[2];
                    LatLng location = new LatLng(Double.valueOf(globalState.nearbyPostalCodes.get(key)[0]),
                            Double.valueOf(globalState.nearbyPostalCodes.get(key)[1]));
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title(label));
                    markers.add(marker);
                }

                if (markers.size()>0) {
                    markers.get(0).showInfoWindow();
                }
            }
        });
    }

    private String getRequestResponse(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("Print", "GeoNames/BreezeMeter GET Success");
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = rd.readLine()) != null)
            {
                sb.append(line);
            }
            rd.close();
            String response = sb.toString();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HashMap<String, String[]> processPostalCodes(String response) {
        HashMap<String, String[]> postalCodes = new HashMap<>();
        // get all the postal codes
        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray("postalCodes");
            for (int i = 0; i < jsonArray.length(); ++i) {
                String[] codes = {"0", "0", "NULL"}; // [lat, lng, pollen count]
                String ps = jsonArray.getJSONObject(i).getString("postalCode");

                codes[0] = jsonArray.getJSONObject(i).getString("lat");
                codes[1] = jsonArray.getJSONObject(i).getString("lng");
                postalCodes.put(ps, codes);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postalCodes;
    }
}
