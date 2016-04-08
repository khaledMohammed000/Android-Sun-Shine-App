package app.com.example.com.sun_shine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    ArrayAdapter<String> adapter;
    List<String> weekForecast;
    String ActualData[] = {
            "Today-Sunny-83/63",
            "Tommorow-Foggy-70/46",
            "Weds-Cloudy-72/63",
            "Thurs-Rainy-64/51",
            "Fri-Foggy-70/46",
            "Sat-Sunny-76/68"};
    View rootView;
    ListView ls;
    ArrayList<WeatherModel> weatherModelArrayList;
    String forecastJsonStr = null;
    String location;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container);
        setHasOptionsMenu(true);//required compulsory for adding the new menu item using the below two methods
        //onCreateOptionsMenu()  and   onOptionsItemSelected()

        weekForecast = new ArrayList<String>(Arrays.asList(ActualData));
        adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_text_view,
                weekForecast);

        ls = (ListView) rootView.findViewById(R.id.listView_fragment_main);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                //Toast.makeText(getContext(),tv.getText().toString() +" pos : "+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),DetailActivity.class);
                intent.putExtra(intent.EXTRA_TEXT,tv.getText().toString());
                startActivity(intent);
            }
        });

        ls.setAdapter(adapter);

        ////http://api.openweathermap.org/data/2.5/forecast/daily?q=500009,in&APPID=76ffb28f9ba7a7314b3714cb7cb46d43
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("app.com.example.com.sun_shine_preferences", context.MODE_PRIVATE);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        location = sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default_value));
        Log.v("DEKHOOOO",location);
        new WeatherDataFetchTask().execute(location);


        return rootView;
    }//end of onCreateView

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to  of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        Context context = getActivity();
        Log.v("onResume","YO hoooooooo");
        SharedPreferences settings = context.getSharedPreferences("app.com.example.com.sun_shine_preferences",context.MODE_PRIVATE);
        location = settings.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default_value));
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        new WeatherDataFetchTask().execute(location);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forcastfragment, menu);
    }//end of onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Context context = getActivity();
            SharedPreferences settings = context.getSharedPreferences("app.com.example.com.sun_shine_preferences", context.MODE_PRIVATE);
            location = settings.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default_value));
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            new WeatherDataFetchTask().execute(location);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }//end of onOptionsItemSelected

    class WeatherDataFetchTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            InputStream inputStream = null;
            BufferedReader bufferedReader = null;
            forecastJsonStr = null;
            HttpURLConnection httpURLConnection = null;
            try {

                Uri.Builder finalURL = new Uri.Builder();
                finalURL.scheme("http");
                finalURL.authority("api.openweathermap.org");
                finalURL.appendPath("data");
                finalURL.appendPath("2.5");
                finalURL.appendPath("forecast");
                finalURL.appendPath("daily");
                finalURL.appendQueryParameter("q", params[0]);
                finalURL.appendQueryParameter("cnt", "13");
                finalURL.appendQueryParameter("APPID", "76ffb28f9ba7a7314b3714cb7cb46d43");

                URL url = new URL(finalURL.build().toString());
                Log.v("URL is  : ", finalURL.build().toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                Log.d("RESPONSE", "The response is : " + httpURLConnection.getResponseCode());

                inputStream = httpURLConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }

                //convert the input data to string
                StringBuffer buffer = new StringBuffer();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                forecastJsonStr = buffer.toString();
                Log.v("Json RESULT", forecastJsonStr);
                //end of try block
            } catch (IOException e) {
                Log.e("IOException", "Error", e);
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e("Finally Block", "Error", e);
                    }
                }
            }//end of finally
            return forecastJsonStr;
        }


        @Override
        protected void onPostExecute(String s) {
            Helper helper = new Helper();
            try {
                ActualData = helper.getWeatherDataFromJson(s, 13);
                adapter.clear();
                for(String we : ActualData){
                    adapter.add(we);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }//end of AsyncTask

}//end of class
