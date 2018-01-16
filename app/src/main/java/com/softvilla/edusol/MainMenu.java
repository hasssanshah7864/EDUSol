package com.softvilla.edusol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.softvilla.edusol.Adapter.SchoolAdapter;
import com.softvilla.edusol.Model.SchoolInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {


    TextView profileText, galleryText, attendanceText, resultText, feeText, circularText, academicText, progressText
            , rhymesText, wallpapersText, questionsText, quizText, taskText, complaintText, strategicText, newsText;

    Intent intent;
    String id, promId, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            id = extras.getString("id");
            promId = extras.getString("promId");
            url = extras.getString("url");
        }

        profileText = findViewById(R.id.profileText);
        galleryText = findViewById(R.id.galleryText);
        attendanceText = findViewById(R.id.attendanceText);
        resultText = findViewById(R.id.resultText);
        feeText = findViewById(R.id.feeText);
        circularText = findViewById(R.id.circularText);
        academicText = findViewById(R.id.academicText);
        rhymesText = findViewById(R.id.rhymesText);
        wallpapersText = findViewById(R.id.wallpapersText);
        questionsText = findViewById(R.id.questionsText);
        quizText = findViewById(R.id.quizText);
        taskText = findViewById(R.id.taskText);
        complaintText = findViewById(R.id.complaintText);
        strategicText = findViewById(R.id.strategicPartnerText);
        newsText = findViewById(R.id.newsText);
        progressText = findViewById(R.id.progressText);

        int[] color = {Color.parseColor("#f3be00"), Color.parseColor("#ff8d00")};
        float[] position = {0, 1};
        Shader.TileMode tile_mode0= Shader.TileMode.REPEAT; // or TileMode.REPEAT;
        LinearGradient lin_grad0 = new LinearGradient(0, 0, 0, 200,color,position, tile_mode0);
        Shader shader_gradient0 = lin_grad0;
        profileText.getPaint().setShader(shader_gradient0);
        galleryText.getPaint().setShader(shader_gradient0);
        attendanceText.getPaint().setShader(shader_gradient0);
        resultText.getPaint().setShader(shader_gradient0);
        feeText.getPaint().setShader(shader_gradient0);
        circularText.getPaint().setShader(shader_gradient0);
        academicText.getPaint().setShader(shader_gradient0);
        rhymesText.getPaint().setShader(shader_gradient0);
        wallpapersText.getPaint().setShader(shader_gradient0);
        questionsText.getPaint().setShader(shader_gradient0);
        quizText.getPaint().setShader(shader_gradient0);
        taskText.getPaint().setShader(shader_gradient0);
        complaintText.getPaint().setShader(shader_gradient0);
        strategicText.getPaint().setShader(shader_gradient0);
        newsText.getPaint().setShader(shader_gradient0);
        progressText.getPaint().setShader(shader_gradient0);
    }

    public void attendanceClick(View view) {
        intent = new Intent(this, ChildList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("which","attendance");
        startActivity(intent);
    }

    public void feeClick(View view) {
        intent = new Intent(this, ChildList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("id",promId);
        intent.putExtra("url",url);
        intent.putExtra("which","fee");
        startActivity(intent);
    }

    public void resultClick(View view) {
        intent = new Intent(this, ChildList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("id",promId);
        intent.putExtra("url",url);
        intent.putExtra("which","result");
        startActivity(intent);
    }

    public void circularClick(View view) {
        intent = new Intent(this, ChildList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("which","notice");
        startActivity(intent);
    }
}