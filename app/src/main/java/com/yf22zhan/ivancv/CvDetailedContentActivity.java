package com.yf22zhan.ivancv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzhang on 2/10/2016.
 */
public class CvDetailedContentActivity extends Activity {

    private MyListView listViewMaintask;
    private MyListView listViewPerformance;
    private MyDatabaseHelper dbHelper;

    public static void actionStart(Context context, int position) {
        Intent intent = new Intent(context, CvDetailedContentActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detailed_content);

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (size.x * 0.88);
        lp.height = (int) (size.y * 0.7);
        lp.y = 100;
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setAttributes(lp);

        dbHelper = new MyDatabaseHelper(this, "FootStep.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final int selectPage = getIntent().getIntExtra("position", 0);

        String mainTask = "";
        String performance = "";
        Cursor cursorCurrent = db.query("footstep", null, "id=" + selectPage, null, null, null, null);
        if (cursorCurrent.moveToFirst()) {
            mainTask = cursorCurrent.getString(cursorCurrent.getColumnIndex("maintask"));
            performance = cursorCurrent.getString(cursorCurrent.getColumnIndex("performance"));
        }
        cursorCurrent.close();

        String[] mainTaskArray = mainTask.split("\\|");
        List<String> mainTaskList = new ArrayList<String>();
        for (String maintask : mainTaskArray) {
            mainTaskList.add(maintask);
        }

        String[] performanceArray = performance.split("\\|");
        List<String> performanceList = new ArrayList<String>();
        for (String perFormance : performanceArray) {
            performanceList.add(perFormance);
        }

        listViewMaintask = (MyListView) findViewById(R.id.listview_maintask);
        MainTaskAdapter mainTaskAdapter = new MainTaskAdapter(CvDetailedContentActivity.this, R.layout.maintask_display, mainTaskList);
        listViewMaintask.setAdapter(mainTaskAdapter);


        listViewPerformance = (MyListView) findViewById(R.id.listview_performance);
        PerformanceAdapter performanceAdapter = new PerformanceAdapter(CvDetailedContentActivity.this, R.layout.performance_display, performanceList);
        listViewPerformance.setAdapter(performanceAdapter);
    }

}
