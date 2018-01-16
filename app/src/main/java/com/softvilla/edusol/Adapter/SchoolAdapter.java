package com.softvilla.edusol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softvilla.edusol.Login;
import com.softvilla.edusol.Model.AttendanceInfo;
import com.softvilla.edusol.Model.SchoolInfo;
import com.softvilla.edusol.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hassan on 1/3/2018.
 */

public class SchoolAdapter extends  RecyclerView.Adapter<SchoolAdapter.ViewHolder> {

    Context context;
    ArrayList<SchoolInfo> data;

    public SchoolAdapter(Context context, ArrayList<SchoolInfo> data){
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_row, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(context).load(data.get(position).schoolImageUrl).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(holder.schoolLogo);
        holder.schoolName.setText(data.get(position).schoolName);

        holder.card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*AttendanceInfo.url = data.get(position).url;
                        Intent intent = new Intent(context, Login.class);
                        intent.putExtra("url", data.get(position).url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);*/
                        Intent intent = new Intent("broadcast");
                        intent.putExtra("url", data.get(position).url);
                        intent.putExtra("name", data.get(position).schoolName);

                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView schoolLogo;
        TextView schoolName;
        CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            schoolLogo = itemView.findViewById(R.id.schoolLogo);
            schoolName = itemView.findViewById(R.id.schoolNameText);
            card = itemView.findViewById(R.id.schoolCard);
        }
    }
}
