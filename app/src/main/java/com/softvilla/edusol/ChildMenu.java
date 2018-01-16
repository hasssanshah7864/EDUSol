package com.softvilla.edusol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChildMenu extends AppCompatActivity {

    Intent intent;
    String id, promId, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_menu);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            id = extras.getString("id");
            promId = extras.getString("promId");
            url = extras.getString("url");
        }
    }

    public void attendanceClick(View view) {
        intent = new Intent(this, Attendance.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void feeClick(View view) {
        intent = new Intent(this, Fee.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("id",promId);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    public void resultClick(View view) {
        intent = new Intent(this, Result.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("id",promId);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    public void noticeClick(View view) {
        intent = new Intent(this, Notice.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
