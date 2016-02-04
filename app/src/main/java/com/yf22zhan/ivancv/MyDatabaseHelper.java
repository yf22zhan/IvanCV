package com.yf22zhan.ivancv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yzhang on 2/4/2016.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_FOOTSTEP = "create table footstep ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "maintask text, "
            + "performance text, "
            + "lat real, "
            + "lng real)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_FOOTSTEP);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
