package app.com.example.com.sun_shine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user-1 on 4/3/2016.
 */
public class JParser {
    String data;
    ArrayList<WeatherModel> weatherModels;

    JParser(String data){
        this.data = data;
        weatherModels = new ArrayList<WeatherModel>();
    }

    ArrayList<WeatherModel> parser (){

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            for(int i=0;i<jsonArray.length();i++){
                WeatherModel weatherModel= new WeatherModel();
                WeatherArrayModel weatherArrayModel = new WeatherArrayModel();
                TempModel tempModel = new TempModel();
                JSONObject jsonObjectREAL = jsonArray.getJSONObject(i);

                weatherModel.setDt(jsonObjectREAL.getLong("dt"));
                weatherModel.setSpeed(jsonObjectREAL.getDouble("speed"));
                weatherModel.setDeg(jsonObjectREAL.getInt("deg"));
                weatherModel.setClouds(jsonObjectREAL.getInt("clouds"));
                weatherModel.setHumidity(jsonObjectREAL.getInt("humidity"));
                weatherModel.setPressure(jsonObjectREAL.getDouble("pressure"));

                JSONArray jsonArrayWEATHER = jsonObjectREAL.getJSONArray("weather");
                JSONObject jsonObjectWeather = jsonArrayWEATHER.getJSONObject(0);

                weatherArrayModel.setId(jsonObjectWeather.getInt("id"));
                weatherArrayModel.setMain(jsonObjectWeather.getString("main"));
                weatherArrayModel.setDescription(jsonObjectWeather.getString("description"));
                weatherArrayModel.setIcon(jsonObjectWeather.getString("icon"));
                weatherModel.setWeather(weatherArrayModel);

                JSONObject jsonObjectTEMP = jsonObjectREAL.getJSONObject("temp");
                tempModel.setDay(jsonObjectTEMP.getDouble("day"));
                tempModel.setMin(jsonObjectTEMP.getDouble("min"));
                tempModel.setMax(jsonObjectTEMP.getDouble("max"));
                tempModel.setNight(jsonObjectTEMP.getDouble("night"));
                tempModel.setEve(jsonObjectTEMP.getDouble("eve"));
                tempModel.setMorn(jsonObjectTEMP.getDouble("morn"));
                weatherModel.setTemp(tempModel);


                weatherModels.add(weatherModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return weatherModels;
    }
}
