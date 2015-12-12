package com.example.myweatherapplication.launch;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myweatherapplication.R;
import com.example.myweatherapplication.dataBase.WorkWithDataBase;
import com.example.myweatherapplication.dialogs.About;
import com.example.myweatherapplication.dialogs.ChangeCity;
import com.example.myweatherapplication.dialogs.ChangeTemp;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements MainFragment.OnItemPressed {
    private MainFragment mainFragment;
    private int close = 0;
    private boolean isWeatherForecastFragmentActive;
    private boolean exitNow = false;
    private DialogFragment dg1;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] toolbarTitles;
    private CharSequence drawerTitle;
    private CharSequence title;
    private DrawerLayout drawerLayout;
    private ListView toolbarList;
    private ItemFromAdapter itemFromAdapter;
    private WorkWithDataBase workWithDataBase;
    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_CITY = "City";
    public static final String appPreferencesCelcs = "appPreferencesCelcs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        setCurrentFragment(mainFragment, false);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(appPreferencesCelcs, true);
        editor.commit();
        title = drawerTitle = getTitle();
        toolbarTitles = getResources().getStringArray(R.array.toolbar_list);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        toolbarList = (ListView)findViewById(R.id.left_drawer);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        toolbarList.setAdapter(new ArrayAdapter<String>(
                this, R.layout.drawer_list_item, toolbarTitles
        ));
        toolbarList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.mipmap.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(toolbarList);
        return super.onPrepareOptionsMenu(menu);
    }

    public void setCurrentFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            onFragmentBackPressed();
        }
    }

    private void onFragmentBackPressed() {
        if (!isWeatherForecastFragmentActive) {
            setWeatherForecastFragment();
        } else {
            if (exitNow) {
                super.onBackPressed();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Приложение закроется", Toast.LENGTH_SHORT);
                toast.show();
                exitNow = true;
            }
        }
    }

    private void setWeatherForecastFragment() {
        mainFragment.removeOnItemClickListener();
        mainFragment = new MainFragment();
        mainFragment.setOnItemClickListener(this);
        setCurrentFragment(mainFragment, true);
        isWeatherForecastFragmentActive = true;
    }

    private void setFragment(Fragment fragment) {
        setCurrentFragment(fragment, true);
        isWeatherForecastFragmentActive = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
      int id = item.getItemId();
        switch (id){
            case R.id.change_city:
                dg1 = new ChangeCity(MainActivity.this);
                dg1.show(getFragmentManager(), "dg1");

                break;
            case R.id.update:
                setWeatherForecastFragment();
                break;
            case R.id.about_app:
                About aboutDialog = new About(MainActivity.this);
                aboutDialog.setTitle("О нас");
                aboutDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemPressed(int position, WorkWithDataBase workWithDataBase) {
            Bundle bundle = new Bundle();
            itemFromAdapter = new ItemFromAdapter();
            setCurrentFragment(itemFromAdapter, false);

            bundle.putDouble("pressure", workWithDataBase.getPressureForPeriod(position));
            bundle.putDouble("windSpeed", workWithDataBase.getWindSpeed(position));
            bundle.putDouble("temp", workWithDataBase.getTemp(position));
            bundle.putString("ico", workWithDataBase.getIconForItem(position));
            itemFromAdapter.setArguments(bundle);
            isWeatherForecastFragmentActive = false;
    }

    @Override
    protected void onPause() {
        mainFragment.removeOnItemClickListener();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainFragment.setOnItemClickListener(this);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        switch(position){
            case 0:
                dg1 = new ChangeCity(MainActivity.this);
                dg1.show(getFragmentManager(), "dg1");
                close++;
                break;

            case 1:
                About aboutDialog = new About(MainActivity.this);
                aboutDialog.setTitle("О нас");
                aboutDialog.show();
                break;
            case 2:
                ChangeTemp changeTemp = new ChangeTemp(MainActivity.this);
                changeTemp.show(getFragmentManager(), "changeTemp");
        }
    }

    @Override
    public void setTitle(CharSequence titleTool) {
        title = titleTool;
        getActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
