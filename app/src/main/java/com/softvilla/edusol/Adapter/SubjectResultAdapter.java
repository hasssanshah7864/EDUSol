package com.softvilla.edusol.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softvilla.edusol.Model.SubjectResultnfo;
import com.softvilla.edusol.R;

import java.util.ArrayList;

/**
 * Created by Hassan on 1/5/2018.
 */

public class SubjectResultAdapter extends RecyclerView.Adapter<SubjectResultAdapter.ViewHolder> {

    ArrayList<SubjectResultnfo> data;
    Context context;

    public SubjectResultAdapter(ArrayList<SubjectResultnfo> data, Context context){
        this.data = data;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(data.get(position).name);
        holder.date.setText(data.get(position).date);
        holder.obtainMarks.setText(data.get(position).obtainMarks);
        holder.totalMarks.setText(data.get(position).totalMarks);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView  name, totalMarks, obtainMarks, date;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.position);
            obtainMarks = itemView.findViewById(R.id.obtMarks);
            totalMarks = itemView.findViewById(R.id.totlaMarks);
        }
    }
}
