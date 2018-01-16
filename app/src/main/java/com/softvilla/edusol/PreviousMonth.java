package com.softvilla.edusol;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.softvilla.edusol.Model.AttendanceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Malik on 07/08/2017.
 */

public class PreviousMonth extends Fragment implements OnChartValueSelectedListener {

    public static final String LASTMONTH_URL = "http://ccsanghar.edu.pk/edusolutions/Api/getLastMonthAttendance";
    ArrayList<AttendanceInfo> lastMonthData;
    int total, present, absent, leave, shortLeave;

    Dialog dialog;
    ListView listView;
    TextView Title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.previousmonth, container, false);

        lastMonthData = new ArrayList<AttendanceInfo>();
        total = present = absent = leave = shortLeave = 0;
        final ProgressDialog dialog1 = new ProgressDialog(view.getContext());
        dialog1.setMessage("Loading");
        dialog1.setCancelable(false);

        dialog1.show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());

        AndroidNetworking.post(preferences.getString("url","") + "getLastMonthAttendance")
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
                                AttendanceInfo obj = new AttendanceInfo();
                                obj.date = jsonObject1.getString("date"+i);
                                obj.status = jsonObject1.getString("status"+i);
                                String status = jsonObject1.getString("status"+i);
                                if(status.equalsIgnoreCase("1")){
                                    present++;
                                }
                                else if(status.equalsIgnoreCase("2")){
                                    absent++;
                                }
                                else if(status.equalsIgnoreCase("3")){
                                    leave++;
                                }
                                else if(status.equalsIgnoreCase("4")){
                                    shortLeave++;
                                }
                                lastMonthData.add(obj);
                                total++;

                            }

                            dialog1.dismiss();

                            PieChart pieChart = (PieChart) view.findViewById(R.id.previousemonth);
                            pieChart.setUsePercentValues(true);
                            pieChart.setTransparentCircleRadius(30f);
                            dialog = new Dialog(view.getContext());
                            dialog.setContentView(R.layout.attendancereport);
                            dialog.setTitle("Previous Month Attendance");
                            Title = (TextView) dialog.findViewById(R.id.DialogTitle);


                            // IMPORTANT: In a PieChart, no values (Entry) should have the same
                            // xIndex (even if from different DataSets), since no values can be
                            // drawn above each other.
                            ArrayList<Entry> yvalues = new ArrayList<Entry>();

                            if(total == 0) {
                                yvalues.add(new Entry(0f, 0));
                                yvalues.add(new Entry(0f, 1));
                                yvalues.add(new Entry(0f, 2));
                                yvalues.add(new Entry(0f, 3));
                                Toast.makeText(view.getContext(),"No Data For Previous Month",Toast.LENGTH_LONG).show();

                            }

                            else {
                                yvalues.add(new Entry(((present*100)/total), 0));
                                yvalues.add(new Entry((absent*100)/total, 1));
                                yvalues.add(new Entry((leave*100)/total, 2));
                                yvalues.add(new Entry((shortLeave*100)/total, 3));

                            }

                            PieDataSet dataSet = new PieDataSet(yvalues, "Attendance");

                            ArrayList<String> xVals = new ArrayList<String>();
                            String p= "Present",a ="Absent",l = "Leave",sl = "Short Leave";
                            if(present == 0.0){
                                p = "";
                                //Toast.makeText(view.getContext(),"Presenet",Toast.LENGTH_SHORT).show();
                            }
                            if(absent == 0.0){
                                a = "";
                                //Toast.makeText(view.getContext(),"Absent",Toast.LENGTH_SHORT).show();
                            }
                            if(leave == 0.0){
                                l = "";
                                //Toast.makeText(view.getContext(),"Leave",Toast.LENGTH_SHORT).show();
                            }
                            if(shortLeave == 0.0){
                                //Toast.makeText(view.getContext(),"Short Leave",Toast.LENGTH_SHORT).show();
                                sl = "";
                            }
                            xVals.add(p);
                            xVals.add(a);
                            xVals.add(l);
                            xVals.add(sl);


                            PieData data = new PieData(xVals, dataSet);
                            // In Percentage term
                            data.setValueFormatter(new PercentFormatter());
                            // Default value
                            //data.setValueFormatter(new DefaultValueFormatter(0));
                            pieChart.setData(data);
                            pieChart.setDescription("Previous Month Attendance Report");

                            pieChart.setDrawHoleEnabled(true);
                            pieChart.setTransparentCircleRadius(25f);
                            pieChart.setHoleRadius(25f);

                            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                            data.setValueTextSize(13f);
                            data.setValueTextColor(Color.DKGRAY);
                            pieChart.setOnChartValueSelectedListener(PreviousMonth.this);

                            pieChart.animateXY(1400, 1400);
//                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
//                            Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, context);
//                            recyclerView.setAdapter(adapter);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(context));

                        }catch (JSONException e){
                            dialog.dismiss();
                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                            //Toast.makeText(LogIn.this,e.toString(),Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Toast.makeText(view.getContext(), anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


        return view;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if(e.getXIndex()==0){
            ArrayList<String> data = new ArrayList<>();


            for(int i = lastMonthData.size() - 1 ; i >=0 ; i--){
                if(lastMonthData.get(i).status.equalsIgnoreCase("1")){
                    try {
                        data.add(getMonth(lastMonthData.get(i).date));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            listView = (ListView) dialog.findViewById(R.id.att);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.name_list,data);
            listView.setAdapter(adapter);
            Title.setText("Present");
            dialog.show();
            return;
        }
        else if(e.getXIndex()==1){
            ArrayList<String> data = new ArrayList<>();


            for(int i = lastMonthData.size() - 1 ; i >=0 ; i--){
                if(lastMonthData.get(i).status.equalsIgnoreCase("2")){
                    try {
                        data.add(getMonth(lastMonthData.get(i).date));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            listView = (ListView) dialog.findViewById(R.id.att);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.name_list,data);
            listView.setAdapter(adapter);
            Title.setText("Absent");
            dialog.show();
            return;
        }
        else if(e.getXIndex()==2){
            ArrayList<String> data = new ArrayList<>();


            for(int i = lastMonthData.size() - 1 ; i >=0 ; i--){
                if(lastMonthData.get(i).status.equalsIgnoreCase("3")){
                    try {
                        data.add(getMonth(lastMonthData.get(i).date));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            listView = (ListView) dialog.findViewById(R.id.att);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.name_list,data);
            listView.setAdapter(adapter);
            Title.setText("Leave");
            dialog.show();
            return;
        }
        else if(e.getXIndex()==3){
            ArrayList<String> data = new ArrayList<>();


            for(int i = lastMonthData.size() - 1 ; i >=0 ; i--){
                if(lastMonthData.get(i).status.equalsIgnoreCase("4")){
                    try {
                        data.add(getMonth(lastMonthData.get(i).date));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            listView = (ListView) dialog.findViewById(R.id.att);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.name_list,data);
            listView.setAdapter(adapter);
            Title.setText("Short Leave");
            dialog.show();

            return;
        }
    }




    @Override
    public void onNothingSelected() {
        //Log.i("PieChart", "nothing selected");
    }

    void getCurrentMonthAtt(final Context context){


    }

    private static String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("dd-MMM-yyyy").format(cal.getTime());
        return monthName;
    }
}
