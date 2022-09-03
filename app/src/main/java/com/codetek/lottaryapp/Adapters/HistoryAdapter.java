package com.codetek.lottaryapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codetek.lottaryapp.Models.DB.Lottery;
import com.codetek.lottaryapp.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    ArrayList<Lottery> dataList;
    Context context;

    public HistoryAdapter(Context context, ArrayList<Lottery> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView result_ticket_image;
        public TextView result_ticket_name,result_ticket_draw,result_ticket_date,result_ticket_number1,result_ticket_number2,result_ticket_number3,result_ticket_number4,result_ticket_letter,result_ticket_price;

        public ViewHolder(View view) {
            super(view);
            result_ticket_image=view.findViewById(R.id.result_ticket_image);
            result_ticket_name = view.findViewById(R.id.result_ticket_name);
            result_ticket_draw = view.findViewById(R.id.result_ticket_draw);
            result_ticket_date = view.findViewById(R.id.result_ticket_date);
            result_ticket_number1 = view.findViewById(R.id.result_ticket_number1);
            result_ticket_number2 = view.findViewById(R.id.result_ticket_number2);
            result_ticket_number3 = view.findViewById(R.id.result_ticket_number3);
            result_ticket_number4 = view.findViewById(R.id.result_ticket_number4);
            result_ticket_letter = view.findViewById(R.id.result_ticket_letter);
            result_ticket_price = view.findViewById(R.id.result_ticket_price);
        }
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.activity_history_record, parent, false);
        HistoryAdapter.ViewHolder viewHolder = new HistoryAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        Lottery data = dataList.get(position);
        holder.result_ticket_name.setText(data.getName().toUpperCase());
        holder.result_ticket_draw.setText(data.getDraw_no());
        holder.result_ticket_date.setText(data.getDate());
        holder.result_ticket_number1.setText(data.getNumber1());
        holder.result_ticket_number2.setText(data.getNumber2());
        holder.result_ticket_number3.setText(data.getNumber3());
        holder.result_ticket_number4.setText(data.getNumber4());
        holder.result_ticket_letter.setText(data.getLetter());
        holder.result_ticket_price.setText( (data.getPrice()==0.0)?"No Winnings":"LKR "+String.valueOf(data.getPrice()) );
        holder.result_ticket_image.setImageDrawable(Lottery.getLotteryImage(context, data.getName()));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}