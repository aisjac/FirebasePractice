package com.example.aisjac.firebasepractice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Student> lastStudentList;

    public Adapter(@NonNull Context context, ArrayList<Student> lastStudentList) {
        super(context, R.layout.sample, lastStudentList);
        this.context = context;
        this.lastStudentList = lastStudentList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.sample,parent,false);

        TextView dateTextView = convertView.findViewById(R.id.DateTVId);
        TextView name_initial = convertView.findViewById(R.id.NameInitialTVId);
        TextView breakfastTextView = convertView.findViewById(R.id.BreakfastTVId);
        TextView dinneTextView = convertView.findViewById(R.id.DinnerTVId);
        TextView dailyShopTextView = convertView.findViewById(R.id.DailyShopTVId);

        dateTextView.setText(lastStudentList.get(position).getDate());
        name_initial.setText(lastStudentList.get(position).getName_initial());
        breakfastTextView.setText(lastStudentList.get(position).getBreakfast());
        dinneTextView.setText(lastStudentList.get(position).getDinner());
        dailyShopTextView.setText(lastStudentList.get(position).getDaily_shop());

        return convertView;
    }
}
