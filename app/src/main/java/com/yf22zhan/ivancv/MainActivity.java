package com.yf22zhan.ivancv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private EditText editTextName;
    private Button buttonSearch;
    private Button buttonStart;

    private MyDatabaseHelper dbHelper;

    private long exitTime = 0;

    private void setUpMap() {

        googleMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(43.6269, -79.9514), 8.1f), 2000, null);

        googleMap.addMarker(new MarkerOptions().position(LocationConstants.WATERLOO));

        googleMap.addPolyline((new PolylineOptions())
                .add(LocationConstants.WATERLOO, LocationConstants.VAUGHAN)
                .width(20).color(Color.parseColor("#448aff")));
        googleMap.addMarker(new MarkerOptions().position(LocationConstants.VAUGHAN));


        googleMap.addPolyline((new PolylineOptions())
                .add(LocationConstants.WATERLOO, LocationConstants.OAKVILLE)
                .width(20).color(Color.parseColor("#448aff")));
        googleMap.addMarker(new MarkerOptions().position(LocationConstants.OAKVILLE));


    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }

    private void exitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public void onBackPressed() {
        exitApp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbHelper = new MyDatabaseHelper(this, "FootStep.db", null, 1);
        //dbHelper.getWritableDatabase();


        editTextName = (EditText) findViewById(R.id.edittext_name);
        buttonStart = (Button) findViewById(R.id.button_start);
        buttonStart.setVisibility(View.INVISIBLE);
        buttonSearch = (Button) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String editTextInput = editTextName.getText().toString();
                final String cvName = editTextInput.substring(0, 1).toUpperCase()
                        + editTextInput.substring(1).toLowerCase();
                if (cvName.equals("Ivan")) {

                    setUpMap();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    buttonStart.setVisibility(View.VISIBLE);
                    buttonStart.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            CvActivity.actionStart(MainActivity.this, cvName);
                        }
                    });

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("footstep", null, null, null, null, null, null);
                    if (cursor.getCount() == 0) {
                        ContentValues values = new ContentValues();
                        //insert first set data
                        values.put("name", "Canada · Ontario · Waterloo");
                        values.put("maintask", "Computer Eng Undergrad Study at UW");
                        values.put("performance", "Academics：Top 10 in Program|Extracurricular：GitHub Projects and Hackathons|Awards：President's Scholarship of Distinction");
                        values.put("lat", LocationConstants.WATERLOO.latitude);
                        values.put("lng", LocationConstants.WATERLOO.longitude);
                        db.insert("footstep", null, values);
                        values.clear();
                        //insert second set data
                        values.put("name", "Canada · Ontario · Vaughan");
                        values.put("maintask", "System Level Test Specialist at Tyco International Ltd.|Participation in Toast Masters|G2 Driver's Licence");
                        values.put("performance", "Evaluated as an excellent contributor to the team|Implemented script engine and test cases for sensor-server wireless communication|；Coded automatic SVN-fetching system in Python");
                        values.put("lat", LocationConstants.VAUGHAN.latitude);
                        values.put("lng", LocationConstants.VAUGHAN.longitude);
                        db.insert("footstep", null, values);
                        values.clear();
                        //insert third set data
                        values.put("name", "China · Ontario · Oakville");
                        values.put("maintask", "Android and iOS Mobile QA Developer at Pelmorex (The Weather Network)|U of T Hackathons");
                        values.put("performance", "Performed regression testing and developed UI automated testing framework|Enjoyed participating in various events with colleagues after work");
                        values.put("lat", LocationConstants.OAKVILLE.latitude);
                        values.put("lng", LocationConstants.OAKVILLE.longitude);
                        db.insert("footstep", null, values);
                        values.clear();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the correct name for search", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
