package com.example.myweatherapplication.launch;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myweatherapplication.R;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFromAdapter extends Fragment {
    private TextView pressure;
    private TextView windSpeed;
    private TextView temp;
    private ImageView ico;

    private double pressureForPeriod = 0;
    private double windSpeedForPeriod = 0;
    private double tempForPeriod = 0;
    private String icoForPeriod;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle dataFromDataBase = getArguments();
        pressureForPeriod = dataFromDataBase.getDouble("pressure");
        windSpeedForPeriod = dataFromDataBase.getDouble("windSpeed");
        tempForPeriod = dataFromDataBase.getDouble("temp");
        icoForPeriod = dataFromDataBase.getString("ico");

        View rootView = inflater.inflate(R.layout.fragment_item_from_adapter, container, false);
        pressure = (TextView)rootView.findViewById(R.id.pressure);
        windSpeed = (TextView)rootView.findViewById(R.id.windSpeedOnItem);
        temp = (TextView)rootView.findViewById(R.id.tempInItem);
        ico = (ImageView)rootView.findViewById(R.id.iconOnItemList);

        pressure.setText(pressure.getText() + " " + Math.round(pressureForPeriod) + " мм");
        windSpeed.setText(windSpeed.getText() + " " + Math.round(windSpeedForPeriod) + " м / с");
        temp.setText(temp.getText() + " " + Math.round(tempForPeriod) + "°");
        ico.setImageResource(getIcon(icoForPeriod));

        return rootView;
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
