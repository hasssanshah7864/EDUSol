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
import com.softvilla.edusol.Adapter.ChildAdapter;
import com.softvilla.edusol.Model.ChildInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChildList extends AppCompatActivity {

    ArrayList<ChildInfo> data;
    ChildAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    String url;
    String cnic;
    String which;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);

        AndroidNetworking.initialize(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            /*cnic = extras.getString("cnic");
            url = extras.getString("url");*/
            which = extras.getString("which");

        }

        data = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AndroidNetworking.post(preferences.getString("url","") + "getStudents")
                .addBodyParameter("cnic",preferences.getString("cnic",""))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ChildList.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                ChildInfo obj = new ChildInfo();

                                obj.image = jsonObject.getString("img"+i);
                                obj.name = jsonObject.getString("name"+i);
                                obj.identity = jsonObject.getString("id"+i);
                                obj.promId = jsonObject.getString("promid"+i);

                                data.add(obj);
                            }

                            recyclerView = findViewById(R.id.childListRecycler);
                            adapter = new ChildAdapter(data,ChildList.this, which);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ChildList.this));
                            recyclerView.setAdapter(adapter);
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChildList.this, e.toString(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Toast.makeText(ChildList.this, anError.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
