package com.example.chap8;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.DBHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText numView, nameView, phoneView, departView;
    Button addBtn, searchBtn;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numView = findViewById(R.id.num);
        nameView = findViewById(R.id.name);
        phoneView = findViewById(R.id.phone);
        departView = findViewById(R.id.depart);
        addBtn = findViewById(R.id.add_btn);
        searchBtn = findViewById(R.id.search_btn);
        result = findViewById(R.id.result);

        addBtn.setOnClickListener(this); // 레코드 추가
        searchBtn.setOnClickListener(this); // 레코드 조회
    }

    @Override
    public void onClick(View v) {
        if (v == addBtn) { // 추가버튼
            // 데이터베이스 생성 열기
            // 테이블 생성
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            // 테이블에 레코드 저장
            String num = numView.getText().toString();
            String name = nameView.getText().toString();
            String phone = phoneView.getText().toString();
            String depart = departView.getText().toString();
            String sql = "INSERT INTO student VALUES(" +
                    num + ", " +
                    "'" + name + "', " +
                    "'" + phone + "', " +
                    "'" + depart + "'" +
                    ")";
            try {
                db.execSQL(sql);
                Toast.makeText(this, "레코드 1개가 입력되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(this, "레코드 입력에 실패하였습니다. 학번이 중복되었을 수 있습니다.", Toast.LENGTH_SHORT).show();
                Log.e("INSERT SQL=============", sql);
            }
            db.close();
        } else if (v == searchBtn) { // 조회버튼
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();

            String selectSQL = "SELECT * FROM student";

            Cursor cursor = db.rawQuery(selectSQL, null);
            result.setText("");
            while(cursor.moveToNext()){
                String depart = cursor.getString(3);
                int num = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                result.append(depart + "\t" + num + "\t" + name + "\t" + phone + "\n");
            }
            db.close();
        }
    }
}
