package com.softvilla.edusol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softvilla.edusol.Attendance;
import com.softvilla.edusol.ChildMenu;
import com.softvilla.edusol.Fee;
import com.softvilla.edusol.MainMenu;
import com.softvilla.edusol.Model.AttendanceInfo;
import com.softvilla.edusol.Model.ChildInfo;
import com.softvilla.edusol.Notice;
import com.softvilla.edusol.R;
import com.softvilla.edusol.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hassan on 1/4/2018.
 */

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    ArrayList<ChildInfo> data;
    Context context;
    String which;
    
    public ChildAdapter(ArrayList<ChildInfo> data, Context context, String which){
        this.data = data;
        this.context = context;
        this.which = which;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(context).load(data.get(position).image).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(holder.childImage);
        holder.name.setText(data.get(position).name);

        holder.card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        AttendanceInfo.id = data.get(position).identity;
                        AttendanceInfo.promId = data.get(position).promId;
                        if(which.equalsIgnoreCase("attendance")){
                            intent = new Intent(context, Attendance.class);
                        }else if (which.equalsIgnoreCase("result")){
                            intent = new Intent(context, Result.class);
                            
                        }else if (which.equalsIgnoreCase("notice")){
                            intent = new Intent(context, Notice.class);
                        }else if (which.equalsIgnoreCase("fee")){
                            intent = new Intent(context, Fee.class);
                        }
                        
                        intent.putExtra("id",data.get(position).identity);
                        intent.putExtra("promId",data.get(position).promId);
                        intent.putExtra("url",AttendanceInfo.url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView childImage;
        TextView name;
        RelativeLayout card;
        public ViewHolder(View itemView) {
            super(itemView);
            childImage = itemView.findViewById(R.id.circleView);
            name = itemView.findViewById(R.id.title);
            card = itemView.findViewById(R.id.childCard);
        }
    }
}
