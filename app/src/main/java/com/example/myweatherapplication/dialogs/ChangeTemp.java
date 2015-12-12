package com.example.myweatherapplication.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myweatherapplication.R;
import com.example.myweatherapplication.launch.MainFragment;

/**
 * Created by Влад on 11.12.2015.
 */
public class ChangeTemp extends DialogFragment {
    private EditText inputText;
    Context mainActivity;
    SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String appPreferencesCelcs = "appPreferencesCelcs";
    public static final String isAppPreferencesFar = "isAppPreferencesFar";
    protected MainFragment mainFragment;

    public ChangeTemp(Context context){
        mainActivity = context;



    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String[] tempNamesArray = {"Цельсиях", "Фаренгейтах"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Отобразить температуру в :")
                // добавляем переключатели
                .setSingleChoiceItems(tempNamesArray, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                SharedPreferences.Editor editor = mSettings.edit();
                                boolean celsClick = mSettings.getBoolean(appPreferencesCelcs, true);
                                if (tempNamesArray[item] == "Цельсиях") {
                                    if (celsClick == false) {
                                        editor.putBoolean(appPreferencesCelcs, true);
                                        celsClick = true;
                                    }
                                }
                                else{
                                    if(celsClick == true){
                                        editor.putBoolean(appPreferencesCelcs, false);
                                        celsClick = false;
                                    }
                                }

                                editor.apply();
                            }
                        }
                )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        boolean m = mSettings.getBoolean(appPreferencesCelcs, true);
                        mainFragment = new MainFragment();
                        setCurrentFragment(mainFragment, false);
                        mainFragment.removeOnItemClickListener();
                        mainFragment.setOnItemClickListener((MainFragment.OnItemPressed) mainActivity);

                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
    public void setCurrentFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }
}