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
import com.softvilla.edusol.Adapter.SubjectResultAdapter;
import com.softvilla.edusol.Model.SubjectResultnfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubjectResults extends AppCompatActivity {

    RecyclerView recyclerView;
    SubjectResultAdapter adapter;
    ProgressDialog dialog;
    ArrayList<SubjectResultnfo> data;
    String resultId, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_results);

        AndroidNetworking.initialize(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            resultId = extras.getString("id");
            url = extras.getString("url");
        }

        data = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        AndroidNetworking.post(preferences.getString("url","") + "getSubjectsResult")
                .addBodyParameter("id", resultId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                SubjectResultnfo obj = new SubjectResultnfo();
                                obj.name = jsonObject1.getString("subName" + i);
                                obj.obtainMarks = jsonObject1.getString("obtain_marks" + i);
                                obj.totalMarks = jsonObject1.getString("total_marks" + i);
                                obj.date = jsonObject1.getString("paper_date" + i);



                                data.add(obj);

                            }
                            recyclerView = findViewById(R.id.subjectResultRecycler);
                            adapter = new SubjectResultAdapter(data, SubjectResults.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(SubjectResults.this));
                            recyclerView.setAdapter(adapter);

                            dialog.dismiss();
                        } catch (JSONException e) {
                            dialog.dismiss();
                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                            Toast.makeText(SubjectResults.this,e.toString(),Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Toast.makeText(SubjectResults.this, anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
