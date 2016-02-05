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
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        editTextName.setVisibility(View.VISIBLE);
        buttonSearch.setVisibility(View.VISIBLE);

        //TODO
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();


        dbHelper = new MyDatabaseHelper(this, "FootStep.db", null, 1);
        //dbHelper.getWritableDatabase();

        editTextName = (EditText) findViewById(R.id.edittext_name);
        buttonSearch = (Button) findViewById(R.id.button_search);
        buttonStart = (Button) findViewById(R.id.button_start);
        buttonSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final String cvName = editTextName.getText().toString();
                if (cvName.equals("Ivan")) {

                    setUpMap();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    buttonStart.setVisibility(View.VISIBLE);
                    buttonStart.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            //CvActivity.actionStart(MainActivity.this, cvName);
                        }
                    });

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("footstep", null, null, null, null, null, null);
                    if (cursor.getCount() == 0) {
                        ContentValues values = new ContentValues();

                        //insert first set data
                        values.put("name", "Canada · Ontario · Waterloo");
                        values.put("maintask", "Computer Eng Undergrad Study at UW");
                        values.put("performance", "Academics: Top 10 in Program|extracurricular: GitHub projects and Hackathons|Awards: President’s Scholarship of Distinction");
                        values.put("lat", LocationConstants.WATERLOO.latitude);
                        values.put("lng", LocationConstants.WATERLOO.longitude);
                        db.insert("footstep", null, values);
                        values.clear();

                        //insert second set data
                        values.put("name", "Canada · Ontario · Vaughan");
                        values.put("maintask", "以尽量优异的成绩毕业，锻炼个人学习的能力；并尽量多拿奖学金，改善穷学生的日常生活.|提高英语水平，以便了解外面的世界与接触另外一种可能性.|社会实习，提早感受工作氛围，并能自己赚钱出去旅游.");
                        values.put("performance", "学位绩点:3.92/5；专业排名:13/128；二等奖学金:2次；三等奖学金:1次，奖学金总额5000元.|雅思(IELTS):6.5(听力:8.5,阅读:7)；英语六级(CET-6):581(阅读单项:满分)；BEC商务英语:中级.|IBM大连：商务支持实习生。主要工作内容：在SAP系统环境下将IBM销售部门的各种订单制作成不同的正式单据并保证高准确率.完成IBM实习后，手握2000元实习工资完成了第一次长途旅行，线路：大连--天津--沈阳--哈尔滨--漠河，坐着绿皮火车一路向北的体验希望以后还能再经历一次.");
                        values.put("lat", LocationConstants.VAUGHAN.latitude);
                        values.put("lng", LocationConstants.VAUGHAN.longitude);
                        db.insert("footstep", null, values);
                        values.clear();

                        //insert third set data
                        values.put("name", "Canada · Ontario · Oakville");
                        values.put("maintask", "实习期深圳地区直销工作1个月，海能达龙岗工厂产线实习1个月.|印度区域技术支持工程师，负责渠道经销商日常技术支持并且提供具体项目的方案报价.");
                        values.put("performance", "直销工作中陌生拜访客户150家以上，售出4台对讲机，总金额2479元。体验了销售工作的艰辛，也感受到通过和队友合作卖出东西的喜悦.|龙岗工厂实习中体验了电子产品制造的各个环节，从SMT贴片加工到整机组装再到包装出货.|解决4个主要经销商的日常技术问题，主要涉及对讲机终端、基站、系统软件的售前介绍以及售后维护.");
                        values.put("lat", LocationConstants.OAKVILLE.latitude);
                        values.put("lng", LocationConstants.OAKVILLE.longitude);
                        db.insert("footstep", null, values);
                        values.clear();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the correct name for search", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUpMap() {

        googleMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(43.4638889, -80.5258333), 8.1f), 2000, null);

        googleMap.addMarker(new MarkerOptions().position(LocationConstants.WATERLOO)
                .anchor(0.5f, 0.8f));

        googleMap.addPolyline((new PolylineOptions())
                .add(LocationConstants.WATERLOO, LocationConstants.VAUGHAN)
                .width(20).color(Color.parseColor("#448aff")));
        googleMap.addMarker(new MarkerOptions().position(LocationConstants.VAUGHAN)
                .anchor(0.5f, 0.8f));

        googleMap.addPolyline((new PolylineOptions())
                .add(LocationConstants.WATERLOO, LocationConstants.OAKVILLE)
                .width(20).color(Color.parseColor("#448aff")));
        googleMap.addMarker(new MarkerOptions().position(LocationConstants.OAKVILLE)
                .anchor(0.5f, 0.8f));

    }

/*
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
*/

}
