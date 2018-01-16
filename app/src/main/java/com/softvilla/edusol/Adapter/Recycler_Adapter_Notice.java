package com.softvilla.edusol.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.softvilla.edusol.Model.NoticeInfo;
import com.softvilla.edusol.R;

import java.util.ArrayList;

/**
 * Created by shah on 9/9/2017.
 */

public class Recycler_Adapter_Notice extends RecyclerView.Adapter<Recycler_Adapter_Notice.View_Holder_Notice> {
    ArrayList<NoticeInfo> list;
    Context context;


    public Recycler_Adapter_Notice(ArrayList<NoticeInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder_Notice onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_row, parent, false);
        View_Holder_Notice holder = new View_Holder_Notice(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(View_Holder_Notice holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView

        holder.date.setText(list.get(position).date);
        holder.notice.setText(list.get(position).notice);

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class View_Holder_Notice extends RecyclerView.ViewHolder {

        CardView cv;
        TextView date, notice;
        ExpandableRelativeLayout expandLayout;
        //ImageView imageView;



        View_Holder_Notice(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardViewfee);
            date = (TextView) itemView.findViewById(R.id.noticeDate);
            notice = (TextView) itemView.findViewById(R.id.noticeContent);

            //imageView = (ImageView) itemView.findViewById(R.id.circleView);

        }
    }
}
