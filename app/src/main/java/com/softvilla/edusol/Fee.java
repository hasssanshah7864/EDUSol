package com.softvilla.edusol;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.softvilla.edusol.Adapter.FeeAdapter;
import com.softvilla.edusol.Adapter.ResultAdapter;
import com.softvilla.edusol.Model.AttendanceInfo;
import com.softvilla.edusol.Model.FeeInfo;
import com.softvilla.edusol.Model.ResultInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Fee extends AppCompatActivity {

    RecyclerView recyclerView;
    FeeAdapter adapter;
    ProgressDialog dialog;
    ArrayList<FeeInfo> data;
    String childId;
    String url;
    TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);

        AndroidNetworking.initialize(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            childId = extras.getString("id");
            url = extras.getString("url");
        }

        data = new ArrayList<>();
        status = (TextView) findViewById(R.id.CurrentStatus);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AndroidNetworking.post(preferences.getString("url","") + "getFee")
                .addBodyParameter("id",AttendanceInfo.id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isFee = false;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                FeeInfo obj = new FeeInfo();
                                obj.date = jsonObject1.getString("submitted_at" + i);
                                obj.amount = jsonObject1.getString("feeAmount" + i);
                                obj.received = jsonObject1.getString("recieved" + i);
                                obj.remaining = jsonObject1.getString("remaining" + i);
                                obj.advance = jsonObject1.getString("advance" + i);
                                obj.issueDate = jsonObject1.getString("date" + i);
                                obj.rem = jsonObject1.getString("rem" + i);
                                obj.expireDate = jsonObject1.getString("date_expire" + i);
                                obj.Total = jsonObject1.getString("rem"+i);
                                try {
                                    obj.month = getMonth(obj.issueDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                isFee = true;
                                data.add(obj);

                                if(i == jsonArray.length() - 1){
                                    Double rem = Double.parseDouble(jsonObject1.getString("rem" + i)) - Double.parseDouble(jsonObject1.getString("recieved" + i));
                                    status.setText("Remaining Balance: " + String.valueOf(rem) + "PKR");
                                }

                            }
                            recyclerView = findViewById(R.id.feerecycler);
                            adapter = new FeeAdapter(data, Fee.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Fee.this));

                            dialog.dismiss();
                            if(!isFee){
                                Toast.makeText(Fee.this,"No Data Available",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                            Toast.makeText(Fee.this,e.toString(),Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Toast.makeText(Fee.this, anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("dd-MMM-yyyy").format(cal.getTime());
        return monthName;
    }
}
