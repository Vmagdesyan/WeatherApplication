package com.example.myweatherapplication.dialogs;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.myweatherapplication.R;
import com.example.myweatherapplication.launch.MainActivity;
import com.example.myweatherapplication.launch.MainFragment;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeCity extends DialogFragment implements OnClickListener{
    private EditText inputText;
    Context mainActivity;
    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_CITY = "City";
    private MainFragment mainFragment;

    public ChangeCity(Context context){
        mainActivity = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Введите город");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.splash_background)));
        View v = inflater.inflate(R.layout.fragment_change_city, null);
        v.findViewById(R.id.btnInput).setOnClickListener(this);
        v.findViewById(R.id.btnCancel).setOnClickListener(this);
        v.findViewById(R.id.inputTextField).setOnClickListener(this);
        inputText = (EditText)v.findViewById(R.id.inputTextField);
        return v;

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancel)
            dismiss();
        else
            if(v.getId() == R.id.btnInput)
            {
                mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSettings.edit();
                String s = inputText.getText().toString();
                if(!s.isEmpty())
                    editor.putString(APP_PREFERENCES_CITY, inputText.getText().toString());

                else
                    editor.putString(APP_PREFERENCES_CITY, "Ростов-на-Дону");
                editor.commit();
                dismiss();
                mainFragment = new MainFragment();
                setCurrentFragment(mainFragment, false);
                mainFragment.removeOnItemClickListener();
                mainFragment.setOnItemClickListener((MainFragment.OnItemPressed) mainActivity);
            }

    }
    public void setCurrentFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }
}
