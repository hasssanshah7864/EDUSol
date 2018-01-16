package com.softvilla.edusol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softvilla.edusol.Model.ResultInfo;
import com.softvilla.edusol.R;
import com.softvilla.edusol.SubjectResults;

import java.util.ArrayList;

/**
 * Created by Hassan on 1/4/2018.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    ArrayList<ResultInfo> data;
    Context context;
    String url;

    public ResultAdapter(ArrayList<ResultInfo> data, Context context, String url){
        this.data = data;
        this.context = context;
        this.url = url;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(data.get(position).name);
        holder.position.setText(data.get(position).position);
        holder.obtainMarkes.setText(data.get(position).obtainMarkes);
        holder.totalMarks.setText(data.get(position).totalMarks);

        holder.card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SubjectResults.class);
                        intent.putExtra("url",url);
                        intent.putExtra("id",data.get(position).resultId);
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
        TextView name, position, totalMarks, obtainMarkes;
        CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            position = itemView.findViewById(R.id.position);
            obtainMarkes = itemView.findViewById(R.id.obtMarks);
            totalMarks = itemView.findViewById(R.id.totlaMarks);
            card = itemView.findViewById(R.id.resultCard);
        }
    }
}
