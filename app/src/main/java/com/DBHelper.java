package com;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    // 생성자
    public DBHelper(Context context) {
        super(context, "school_db", null, DATABASE_VERSION);
    }

    // 추상메소드
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성
        String createSQL = "CREATE TABLE student (" +
                "num INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "phone TEXT, " +
                "depart TEXT " +
                ")";
        try {
            db.execSQL(createSQL);
        } catch (Exception e) {
            Log.e("onCreate", "테이블 생성 중 오류 발생");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블 변경될 경우 테이블 제거 후 다시 생성
        String dropSQL = "DROP TABLE student";
        db.execSQL(dropSQL);
        onCreate(db);
    }
}
