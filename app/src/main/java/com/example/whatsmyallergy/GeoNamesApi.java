package com.example.whatsmyallergy;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
            String postalCode = "92612";//globalState.getPostalCode();
            int radius = 10;
            String uri = "http://api.geonames.org/findNearbyPostalCodesJSON?postalcode="+postalCode+"&country=US&radius="+radius+"&username=stam3";
            URL url = new URL(uri);
            String response = getRequestResponse(url);
            ArrayList<String[]> nearbyPostalCodes = processPostalCodes(response);
            globalState.nearbyPostalCodes = nearbyPostalCodes;

            // find their key from accuweather
//            String[] keys = new String[nearbyPostalCodes.size()];
//            for (int i = 0; i < nearbyPostalCodes.size(); ++i) {
//                URL keyRequest = new URL("http://dataservice.accuweather.com/locations/v1/postalcodes/search?apikey=HMoCMG0Z9rnwGcuwnVzfOVTbhK77TexE&q="+nearbyPostalCodes.get(0)[0]);
//                response = getRequestResponse(keyRequest);
//                JSONObject keyObject = new JSONObject(response);
//                keys[i] = keyObject.getString("key");
//            }

            // get the forecasts
//            for (int i = 0; i < keys.length; ++i) {
//                URL keyRequest = new URL(" http://dataservice.accuweather.com/forecasts/v1/daily/1day/"+keys[i]+"?apikey=HMoCMG0Z9rnwGcuwnVzfOVTbhK77TexE&details=true");
//                response = getRequestResponse(keyRequest);
//                JSONObject forecastObject = new JSONObject(response);
//                JSONArray dailyForecasts = forecastObject.getJSONArray("DailyForecasts");
//                JSONArray pollens = dailyForecasts.getJSONObject(i).getJSONArray("AirAndPollen");
//
//                int maxPollenCount = 0;
//                for (int j = 0; j < pollens.length(); ++j) {
//                    JSONObject pollen = pollens.getJSONObject(j);
//                    maxPollenCount = Math.max(Integer.parseInt(pollen.getString("Value")),maxPollenCount);
//                }
//                nearbyPostalCodes.get(i)[4] = maxPollenCount+"";
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        ArrayList<String[]> nearbyPostalCodes = globalState.nearbyPostalCodes;
        for (int i = 0; i < nearbyPostalCodes.size(); ++i ) {
            double lat = Double.valueOf(nearbyPostalCodes.get(i)[1]);
            double lng = Double.valueOf(nearbyPostalCodes.get(i)[2]);
            String count = nearbyPostalCodes.get(i)[3];
            LatLng location = new LatLng(lat, lng);
            googleMap.addMarker(new MarkerOptions().position(location).title(count)); // marker for current location
        }

    }

    private String getRequestResponse(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("Print", "GeoNames GET Success");
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

    private ArrayList<String[]> processPostalCodes(String response) {
        ArrayList<String[]> postalCodes = new ArrayList<>();
        // get all the postal codes
        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray("postalCodes");
            for (int i = 0; i < jsonArray.length(); ++i) {
                String[] codes = {"0", "0", "0", "0"}; // [postal code, lat, lng, pollen count]
                String ps = jsonArray.getJSONObject(i).getString("postalCode");
                if (ps != globalState.getPostalCode()) {
                    codes[0] = ps;
                    codes[1] = jsonArray.getJSONObject(i).getString("lat");
                    codes[2] = jsonArray.getJSONObject(i).getString("lng");
                    postalCodes.add(codes);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postalCodes;
    }
}
