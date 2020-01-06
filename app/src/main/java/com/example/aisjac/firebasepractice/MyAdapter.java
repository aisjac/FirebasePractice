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

public class MyAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Student> StudentList;

    public MyAdapter(@NonNull Context context, ArrayList<Student> StudentList) {
        super(context, R.layout.sample, StudentList);
        this.context = context;
        this.StudentList = StudentList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.sample,parent,false);

        TextView dateTextView = convertView.findViewById(R.id.DateTVId);
        TextView breakfastTextView = convertView.findViewById(R.id.BreakfastTVId);
        TextView dinneTextView = convertView.findViewById(R.id.DinnerTVId);
        TextView dailyShopTextView = convertView.findViewById(R.id.DailyShopTVId);

        dateTextView.setText(StudentList.get(position).getDate());
        breakfastTextView.setText(StudentList.get(position).getBreakfast());
        dinneTextView.setText(StudentList.get(position).getDinner());
        dailyShopTextView.setText(StudentList.get(position).getDaily_shop());

        return convertView;
    }
}
