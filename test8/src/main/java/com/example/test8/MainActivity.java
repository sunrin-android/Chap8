package com.example.test8;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText genre, title, content, search;
    Button btn1, save_btn, btn2;
    TextView result_jenre, result_content;
    Integer index;
    AlertDialog alertDialog, listDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        genre = findViewById(R.id.genre);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        save_btn = findViewById(R.id.save_btn);
        search = findViewById(R.id.search);
        result_content = findViewById(R.id.result_content);

        // 버튼 이벤트 연결
        genre.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        save_btn.setOnClickListener(this);

        // 탭화면 만들기
        TabHost host = findViewById(R.id.host);
        host.setup();// 초기화
        // 탭 spec 만들어서 설정
        TabHost.TabSpec spec;

        spec = host.newTabSpec("tab1");
        spec.setIndicator("노래 입력");
        spec.setContent(R.id.tab1);
        host.addTab(spec);

        spec = host.newTabSpec("tab2");
        spec.setIndicator("노래 조회");
        spec.setContent(R.id.tab2);
        host.addTab(spec);
    }

    @Override
    public void onClick(View v) {
        if (v == btn1 || v == genre) { // TAB1 검색
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("노래 장르 선택");
            builder.setSingleChoiceItems(R.array.genre, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    index = which;
                }
            });
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String[] datas = getResources().getStringArray(R.array.genre);
                    String s = datas[index];
                    showToast(s + "(이)가 선택되었습니다.");
                    genre.setText(s.toString());
                }
            });
            builder.setNegativeButton("취소", null);
            listDialog = builder.create();
            listDialog.show();
        } else if (v == save_btn) { // 저장
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            String sql = "INSERT INTO song (genre, title, content) VALUES (" +
                    "'" + genre.getText().toString() + "', " +
                    "'" + title.getText().toString() + "', " +
                    "'" + content.getText().toString() + "'" +
                    ")";
            try {
                db.execSQL(sql);
                showToast("레코드 1개가 입력되었습니다.");
            } catch (Exception e) {
                showToast("입력에 실패하였습니다. 다음 오류를 확인하십시오 : " + e);
            }
        } else if (v == btn2) { // TAB2 검색
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            String selectSQL = "SELECT genre,content FROM song WHERE title = '" + search.getText().toString() + "'";
            try {
                Cursor cursor = db.rawQuery(selectSQL, null);
                result_content.setText("");
                while (cursor.moveToNext()) {
                    String genre = cursor.getString(0);
                    String content = cursor.getString(1);
                    result_content.append(genre + "\n\n" + content + "\n");
                }
                db.close();
                showToast("레코드 1개가 조회되었습니다.");
            } catch (Exception e) {
                showToast("검색 결과가 없습니다 : " + e);
            }
        }
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
