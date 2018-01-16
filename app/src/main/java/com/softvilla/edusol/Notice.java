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
import com.softvilla.edusol.Adapter.Recycler_Adapter_Notice;
import com.softvilla.edusol.Model.AttendanceInfo;
import com.softvilla.edusol.Model.NoticeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notice extends AppCompatActivity {

    RecyclerView recyclerView;
    Recycler_Adapter_Notice adapter;
    ArrayList<NoticeInfo> data;
    ProgressDialog dialog;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        AndroidNetworking.initialize(this);

        data = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        //Toast.makeText(this, AttendanceInfo.url + "getNotice", Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AndroidNetworking.post(preferences.getString("url","") + "getNotice")
                .addBodyParameter("id",AttendanceInfo.id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                NoticeInfo obj = new NoticeInfo();
                                obj.notice = jsonObject1.getString("notice"+i);
                                obj.date = jsonObject1.getString("date"+i);

                                data.add(obj);
                            }

                            recyclerView = findViewById(R.id.noticeRecycler);
                            Recycler_Adapter_Notice adapter = new Recycler_Adapter_Notice(data, Notice.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Notice.this));
                            dialog.dismiss();
                        }catch (JSONException e){
                            dialog.dismiss();
                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                            //Toast.makeText(LogIn.this,e.toString(),Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
