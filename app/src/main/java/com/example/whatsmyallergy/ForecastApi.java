package com.example.whatsmyallergy;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static com.example.whatsmyallergy.MainActivity.globalState;

public class ForecastApi extends AsyncTask<String, Void, String> {
    Context context;

    public ForecastApi(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        try {
            for (String urlString: urls) {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("Print", "Forecast GET Success");
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = rd.readLine()) != null)
                {
                    sb.append(line);
                }
                rd.close();
                String result = sb.toString();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray dailyForecastsJSON = jsonObject.getJSONArray("DailyForecasts");
                return dailyForecastsJSON.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String json) {

        try {
            JSONArray dailyForecasts = new JSONArray(json);
            ArrayList<Day> fiveDays = new ArrayList<>();

            /// PROCESS JSON into Days and Pollutant Objects
            for (int i = 0; i < dailyForecasts.length(); ++i) {
                JSONArray pollens = dailyForecasts.getJSONObject(i).getJSONArray("AirAndPollen");

                ArrayList<Pollutants> pollenList = new ArrayList<>();
                for (int j = 0; j < pollens.length(); ++j) {
                    JSONObject pollen = pollens.getJSONObject(j);
//                    Log.d("Print",pollen.toString());
                    pollenList.add(new Pollutants(pollen.getString("Name"),
                            Integer.parseInt(pollen.getString("Value")),
                            pollen.getString("Category"),
                            pollen.getString("CategoryValue")));

                }

                String dateString = dailyForecasts.getJSONObject(i).getString("Date").substring(0,10);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(dateString, formatter);
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                Day day = new Day(date, pollenList);
                fiveDays.add(day);
            }

            // Change the Views
            MainActivity.setLocationString("San Diego, CA");
            FiveDayForecast fiveDayForecast = new FiveDayForecast(fiveDays);
            MainActivity.setFiveDayForecast(fiveDayForecast);

            Day today = fiveDayForecast.getDayN(0);
            Pollutants highestPollen = today.getHighestPollutant();

            TextView pollenValue = (TextView) ((MainActivity)context).findViewById(R.id.count_text);
            pollenValue.setText(String.valueOf(highestPollen.getValue()));

            TextView pollenCategory = (TextView) ((MainActivity)context).findViewById(R.id.severity_text);
            pollenCategory.setText(highestPollen.getCategory());

            TextView pollenName = (TextView) ((MainActivity)context).findViewById(R.id.name_text);
            pollenName.setText(highestPollen.getName());

            TextView locationText = (TextView) ((MainActivity)context).findViewById(R.id.location_text);
            locationText.setText(globalState.getLocation());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
