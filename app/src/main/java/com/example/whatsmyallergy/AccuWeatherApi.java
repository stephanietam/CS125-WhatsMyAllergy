package com.example.whatsmyallergy;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
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

public class AccuWeatherApi extends AsyncTask<Void, Void, Void> {
    Context context;

    public AccuWeatherApi(Context context) { this.context = context; }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL postalCodeRequest = new URL(globalState.getPostalCodeRequest()); // PostalCode Api
            String response = getRequestResponse(postalCodeRequest);
            processLocationKey(getLocationJSON(response));

            URL forecastRequest = new URL(globalState.getForecastRequest()); // Forecast Api
            String forecastResponse = getRequestResponse(forecastRequest);
            JSONObject jsonObject = new JSONObject(forecastResponse);
            JSONArray dailyForecastsJSON = jsonObject.getJSONArray("DailyForecasts");
            processForecast(dailyForecastsJSON);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                FiveDayForecast fiveDayForecast = globalState.getFiveDayForecast();
                updateTodayPollenCount();

                TextView[] dayOne = new TextView[] {
                        ((MainActivity)context).findViewById(R.id.day_one_label),
                        ((MainActivity)context).findViewById(R.id.day_one_count)};
                TextView[] dayTwo = new TextView[] {
                        ((MainActivity)context).findViewById(R.id.day_two_label),
                        ((MainActivity)context).findViewById(R.id.day_two_count)};
                TextView[] dayThree = new TextView[] {
                        ((MainActivity)context).findViewById(R.id.day_three_label),
                        ((MainActivity)context).findViewById(R.id.day_three_count)};
                TextView[] dayFour = new TextView[] {
                        ((MainActivity)context).findViewById(R.id.day_four_label),
                        ((MainActivity)context).findViewById(R.id.day_four_count)};
                TextView[] dayFive = new TextView[] {
                        ((MainActivity)context).findViewById(R.id.day_five_label),
                        ((MainActivity)context).findViewById(R.id.day_five_count)};

                TextView[][] week = new TextView[][]{ dayOne, dayTwo, dayThree, dayFour, dayFive };

                for (int i = 0; i < week.length; ++i) {
                    TextView[] day = week[i];
                    day[0].setText(fiveDayForecast.getDaysOfWeek()[i]);

                    int count = fiveDayForecast.getDayN(i).getHighestPollutant().getValue();
                    day[1].setText(count+""); // count
                }
            }
        });
    }

    private void updateTodayPollenCount() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                Pollen highestPollen = globalState.getFiveDayForecast().getDayN(0).getHighestPollutant();

                TextView pollenValue = (TextView) ((MainActivity) context).findViewById(R.id.count_text);
                pollenValue.setText(String.valueOf(highestPollen.getValue()));

                TextView pollenCategory = (TextView) ((MainActivity) context).findViewById(R.id.severity_text);
                pollenCategory.setText(highestPollen.getCategory());

                TextView pollenName = (TextView) ((MainActivity) context).findViewById(R.id.name_text);
                pollenName.setText(highestPollen.getName());

                TextView locationText = (TextView) ((MainActivity) context).findViewById(R.id.location_text);
                locationText.setText(globalState.getLocationName());

                TextView severityText = (TextView) ((MainActivity) context).findViewById(R.id.severity_text);
                severityText.setText(highestPollen.getSeverity());
            }
        });
    }

    private void processLocationKey(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            globalState.setApiLocationKey(jsonObject.getString("key"));
            globalState.setLocationName(jsonObject.getString("city") + ", " + jsonObject.getString("state"));
            globalState.locationSet(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void processForecast(JSONArray dailyForecasts) {
        try {
            ArrayList<Day> fiveDays = new ArrayList<>();

            /// PROCESS JSON into Days and Pollutant Objects
            for (int i = 0; i < dailyForecasts.length(); ++i) {
                JSONArray pollens = dailyForecasts.getJSONObject(i).getJSONArray("AirAndPollen");

                ArrayList<Pollen> pollenList = new ArrayList<>();
                for (int j = 0; j < pollens.length(); ++j) {
                    JSONObject pollen = pollens.getJSONObject(j);
//                    Log.d("Print",pollen.toString());
                    pollenList.add(new Pollen(pollen.getString("Name"),
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

            FiveDayForecast fiveDayForecast = new FiveDayForecast(fiveDays);
            globalState.setFiveDayForecast(fiveDayForecast);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getRequestResponse(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("Print", "GET Success");
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

    private String getLocationJSON(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String locKey = jsonObject.getString("Key");
            String city = jsonObject.getString("EnglishName");
            String state = jsonObject.getJSONObject("AdministrativeArea").getString("ID");

            JSONObject results = new JSONObject();
            results.put("key",locKey);
            results.put("city", city);
            results.put("state", state);

            return results.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
