package com.softvilla.edusol;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.softvilla.edusol.Adapter.ResultAdapter;
import com.softvilla.edusol.Model.AttendanceInfo;
import com.softvilla.edusol.Model.ResultInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    RecyclerView recyclerView;
    ResultAdapter adapter;
    ProgressDialog dialog;
    ArrayList<ResultInfo> data;
    String childId;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        AndroidNetworking.initialize(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            childId = extras.getString("id");
            url = extras.getString("url");
        }

        data = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AndroidNetworking.post(preferences.getString("url","") + "getResult")
                .addBodyParameter("id", AttendanceInfo.id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isFee = false;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = jsonArray.length() - 1; i >= 0 ; i--) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ResultInfo obj = new ResultInfo();
                                obj.name = jsonObject1.getString("examType" + i);
                                obj.obtainMarkes = jsonObject1.getString("obtain_marks" + i);
                                obj.totalMarks = jsonObject1.getString("total_marks" + i);
                                obj.position = jsonObject1.getString("position" + i);
                                obj.resultId = jsonObject1.getString("id" + i);
                                data.add(obj);
                                isFee = true;

                            }
                            recyclerView = findViewById(R.id.resultRecycler);
                            adapter = new ResultAdapter(data, Result.this, url);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Result.this));
                            recyclerView.setAdapter(adapter);
                            dialog.dismiss();
                            if(!isFee){
                                Toast.makeText(Result.this,"No Data Available",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                            //Toast.makeText(LogIn.this,e.toString(),Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Toast.makeText(Result.this, anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
