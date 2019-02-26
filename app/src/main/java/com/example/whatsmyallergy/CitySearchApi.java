package com.example.whatsmyallergy;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CitySearchApi extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        try {
//            for (String urlString: urls) {
//                URL url = new URL(urlString);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    Log.d("Print", "CitySearch GET Success");
//                }
//
//                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder sb = new StringBuilder();
//
//                String line;
//                while ((line = rd.readLine()) != null)
//                {
//                    sb.append(line);
//                }
//                rd.close();
//                String result = sb.toString();
//
//                JSONArray jsonArray = new JSONArray((result));
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                String locKey = jsonObject.getString("Key");
//                return locKey;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String locationKey) {
        MainActivity.processLocationKey(locationKey);
    }

}
