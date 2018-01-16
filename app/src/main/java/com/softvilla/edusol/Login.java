package com.softvilla.edusol;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.softvilla.edusol.Adapter.SchoolAdapter;
import com.softvilla.edusol.Model.SchoolInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends AppCompatActivity {


    Button dropDownBtn, submit;
    EditText cnic;
    RecyclerView recyclerView;
    SchoolAdapter adapter;
    ArrayList<SchoolInfo> data;
    ProgressDialog progressDialog;
    Dialog dialog;
    String url, schoolName;
    boolean isSchoolSelected;
    LocalBroadcastManager broadcastManager;

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent != null) {
                schoolName = intent.getStringExtra("name");
                url = intent.getStringExtra("url");
                dropDownBtn.setText(schoolName);
                dialog.dismiss();
                isSchoolSelected = true;
                // get all your data from intent and do what you want
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AndroidNetworking.initialize(this);


        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(receiver, new IntentFilter("broadcast"));

        dropDownBtn = findViewById(R.id.dropDownBtn);
        submit = findViewById(R.id.submit);
        cnic = findViewById(R.id.cnicEditText);
        data = new ArrayList<>();


        int[] color = {Color.parseColor("#f3be00"), Color.parseColor("#ff8d00")};
        float[] position = {0, 1};
        Shader.TileMode tile_mode0= Shader.TileMode.REPEAT; // or TileMode.REPEAT;
        LinearGradient lin_grad0 = new LinearGradient(0, 0, 0, 200,color,position, tile_mode0);
        Shader shader_gradient0 = lin_grad0;
        dropDownBtn.getPaint().setShader(shader_gradient0);
        submit.getPaint().setShader(shader_gradient0);
        cnic.getPaint().setShader(shader_gradient0);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post("http://softvilla.com.pk/MultiApp/API/getSchools")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray(response).getJSONObject(0).getJSONArray("info");
                            for(int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                SchoolInfo obj = new SchoolInfo();
                                obj.schoolImageUrl = jsonObject.getString("Logo");
                                obj.accountType = jsonObject.getString("Account_Type");
                                obj.schoolName = jsonObject.getString("Name");
                                obj.url = jsonObject.getString("Url");

                                data.add(obj);
                            }


                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
                        // handle error
                    }
                });


        dropDownBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new Dialog(Login.this);
                        dialog.setContentView(R.layout.select_school_dialog);

                        recyclerView = dialog.findViewById(R.id.schollListRecycler);
                        adapter = new SchoolAdapter(Login.this, data);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Login.this));
                        recyclerView.setAdapter(adapter);

                        dialog.show();
                    }
                }
        );

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cnic.getText().toString().length() < 13 || cnic.getText().toString().length() > 13){
                            cnic.setError("Enter Valid CNIC");
                        }
                        else {
                            progressDialog.show();
                            AndroidNetworking.post(url + "login").
                                    addBodyParameter("cnic", cnic.getText().toString())
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response) {
                                            try{
                                                JSONArray jsonArray = new JSONArray(response);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                String message = jsonObject.getString("msg");

                                                if(message.equalsIgnoreCase("false")){
                                                    Toast.makeText(Login.this,"Incorect CNIC.",Toast.LENGTH_SHORT).show();
                                                }

                                                else if(message.equalsIgnoreCase("true")) {
                                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("cnic",cnic.getText().toString());
                                                    editor.putString("isLogin","1");
                                                    editor.putString("url",url);
                                                    editor.apply();
                                                    finish();
                                                    Intent intent = new Intent(Login.this,MainMenu.class);
                                                    intent.putExtra("cnic",cnic.getText().toString());
                                                    intent.putExtra("url",url);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                }
                                                progressDialog.dismiss();
                                            }catch (JSONException e){
                                                progressDialog.dismiss();
                                                // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                                                //Toast.makeText(LogIn.this,e.toString(),Toast.LENGTH_SHORT).show();

                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, anError.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }
        );
    }


}
