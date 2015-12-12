package com.example.myweatherapplication.launch;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myweatherapplication.R;
import com.example.myweatherapplication.dataBase.WorkWithDataBase;
import com.example.myweatherapplication.utils.API;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private TextView cityV;
    private TextView tempV;
    private TextView weatherV;
    private ListView listWeather;
    private ImageView imageOfWeather;
    SharedPreferences mSettings;

    final static int CITY_COL_INDEX = 1;
    final static int WEATHER_COL_INDEX = 3;
    final static int ICO_COL_INDEX = 4;
    final static int TEMP_COL_INDEX = 1;

    final static String DATES_TIME = "date and time";
    final static String ICON = "icon";
    final static String TEMP = "temp";
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_CITY = "City";
    public static final String appPreferencesCelcs = "appPreferencesCelcs";
    public String city = "Ростов-на-Дону";
    boolean typeOfTemp = true;

    public interface OnItemPressed {
        void itemPressed(int position, WorkWithDataBase dBase);
    }
    public OnItemPressed listener;

    public void setOnItemClickListener(OnItemPressed listener) {
        this.listener = listener;
    }

    public void removeOnItemClickListener() {
        this.listener = null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyAsyncTask myATask = new MyAsyncTask();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        cityV = (TextView) rootView.findViewById(R.id.city);
        tempV = (TextView) rootView.findViewById(R.id.temp);
        weatherV = (TextView) rootView.findViewById(R.id.weather);
        imageOfWeather = (ImageView) rootView.findViewById(R.id.imageOfWeather);
        listWeather = (ListView) rootView.findViewById(R.id.listWeather);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        city = mSettings.getString(APP_PREFERENCES_CITY, "Ростов-на-Дону");
        if (!city.isEmpty())
            myATask.execute(city);
        typeOfTemp = mSettings.getBoolean(appPreferencesCelcs, true);
        return rootView;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            API.ApiResponse ap = API.execute("forecast", API.HttpMethod.GET, "q", params[0], "lang", "ru", "type", "like", "units",
                    "metric", "APPID", "bb1c655f7a4998d7f39918e3058d0699");
            JSONObject result;
            result = ap.getJson();
            return result;
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            super.onPostExecute(result);
            final WorkWithDataBase workWithDataBase = new WorkWithDataBase(result, getActivity() );
            String[] datesAndTime = workWithDataBase.getDates();
            double[] tempForPeriod;
            int[] ico = workWithDataBase.getIcons();
            tempForPeriod = workWithDataBase.getTempForDates();
            cityV.setText(workWithDataBase.getStringValues(CITY_COL_INDEX));
            if (typeOfTemp)
                tempV.setText(Math.round(workWithDataBase.getDoubleValues(TEMP_COL_INDEX)) + "°");
            else
                tempV.setText(Math.round((workWithDataBase.getDoubleValues(TEMP_COL_INDEX) - 32) * 5 / 9) + "°");
            weatherV.setText(workWithDataBase.getStringValues(WEATHER_COL_INDEX));
            imageOfWeather.setImageResource(getIcon(workWithDataBase.getStringValues(ICO_COL_INDEX)));

            final ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(datesAndTime.length);
            Map<String, Object> m;
            for (int i = 0; i < datesAndTime.length; i++) {
                m = new HashMap<>();
                m.put(DATES_TIME, datesAndTime[i]);
                m.put(ICON, ico[i]);
                if (typeOfTemp)
                    m.put(TEMP, Math.round(tempForPeriod[i]) + "°");
                else
                    m.put(TEMP, Math.round((tempForPeriod[i] - 32) * 5 / 9) + "°");
                data.add(m);
            }

            String[] from = {ICON, DATES_TIME, TEMP};
            int[] to = {R.id.iconForList, R.id.dateTextView, R.id.tempTextView};

            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity().getApplicationContext(), data, R.layout.item, from, to);
            listWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listener.itemPressed(position, workWithDataBase);
                }
            });
            listWeather.setAdapter(simpleAdapter);
        }
    }
    private int getIcon(String icon) {
        if (icon.equals("01d"))
            return R.drawable._01d;
        else if (icon.equals("01n"))
            return R.drawable._01n;
        else if (icon.equals("02d"))
            return R.drawable._02d;
        else if (icon.equals("02n"))
            return R.drawable._02n;
        else if (icon.equals("03d"))
            return R.drawable._03d;
        else if (icon.equals("03n"))
            return R.drawable._03n;
        else if (icon.equals("04d"))
            return R.drawable._04d;
        else if (icon.equals("04n"))
            return R.drawable._04n;
        else if (icon.equals("09d"))
            return R.drawable._09d;
        else if (icon.equals("09n"))
            return R.drawable._09n;
        else if (icon.equals("10d"))
            return R.drawable._10d;
        else if (icon.equals("10n"))
            return R.drawable._10n;
        else if (icon.equals("11d"))
            return R.drawable._11d;
        else if (icon.equals("11n"))
            return R.drawable._11n;
        else if (icon.equals("13d"))
            return R.drawable._13d;
        else if (icon.equals("13n"))
            return R.drawable._13n;
        else if (icon.equals("50d"))
            return R.drawable._50d;
        else return R.drawable._50n;
    }

}
