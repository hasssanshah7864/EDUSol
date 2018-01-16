package com.softvilla.edusol.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.softvilla.edusol.Model.FeeInfo;
import com.softvilla.edusol.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hassan on 1/5/2018.
 */

public class FeeAdapter extends  RecyclerView.Adapter<FeeAdapter.ViewHolder>{

    ArrayList<FeeInfo> list;
    Context context;

    public FeeAdapter(ArrayList<FeeInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fee_row, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        double Aerials = Double.parseDouble(list.get(position).rem) - Double.parseDouble(list.get(position).amount);

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.received.setText(bold("Received: ",list.get(position).received));
        holder.remaining.setText(bold("Remaining: " , list.get(position).remaining));
        holder.advance.setText(bold("Advances: " , list.get(position).advance));
        try {
            holder.issueDate.setText(bold("Issue Date: " , getMonth(list.get(position).issueDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            holder.submittedAt.setText(bold("Submitted Date: " , getMonth(list.get(position).date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.fee.setText(bold("Fee Package: " , list.get(position).amount));
        holder.arrears.setText(bold("Arrears : " , String.valueOf(Aerials)));
        try {
            holder.expireDate.setText(bold("Expire Date: " , getMonth(list.get(position).expireDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // holder.imageView.setImageResource(list.get(position).imageId);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onCardClickListner.OnCardClicked(view, position);
            }
        });

        holder.expBtn.setText(list.get(position).month + "              " + list.get(position).Total);
        holder.expBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.expandLayout.toggle();
                        String tag = (String) holder.expBtn.getTag();

                        if(tag.equals("down")){
                            Resources resources = context.getResources();
                            Drawable img = resources.getDrawable(android.R.drawable.arrow_up_float);
                            holder.expBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,img,null);
                            holder.expBtn.setTag("up");
                        }
                        else {
                            Resources resources = context.getResources();
                            Drawable img = resources.getDrawable(android.R.drawable.arrow_down_float);
                            holder.expBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,img,null);
                            holder.expBtn.setTag("down");
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView received, remaining, advance,issueDate, expireDate, submittedAt, arrears, fee;
        Button expBtn;
        ExpandableRelativeLayout expandLayout;
        //ImageView imageView;



        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardViewfee);
            received = (TextView) itemView.findViewById(R.id.recieved);
            remaining = (TextView) itemView.findViewById(R.id.remaining);
            advance = (TextView) itemView.findViewById(R.id.advance);
            issueDate = (TextView) itemView.findViewById(R.id.issuedate);
            submittedAt = (TextView) itemView.findViewById(R.id.submittedAt);
            arrears = (TextView) itemView.findViewById(R.id.arrears);
            fee = (TextView) itemView.findViewById(R.id.fee);
            expireDate = (TextView) itemView.findViewById(R.id.expiredate);
            expBtn = (Button) itemView.findViewById(R.id.lbs);
            expandLayout = (ExpandableRelativeLayout) itemView.findViewById(R.id.expandableLayout);
            //imageView = (ImageView) itemView.findViewById(R.id.circleView);

        }


    }

    public SpannableString bold(String boldText, String normalText){
//        String boldText = "id";
//        String normalText = "name";
        SpannableString str = new SpannableString(boldText + normalText);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    private static String getMonth(String date) throws ParseException {
        if(date.equalsIgnoreCase("0000-00-00")){
            return "00-00-0000";
        }
        else {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            String monthName = new SimpleDateFormat("dd-MMM-yyyy").format(cal.getTime());
            return monthName;
        }

    }
}
