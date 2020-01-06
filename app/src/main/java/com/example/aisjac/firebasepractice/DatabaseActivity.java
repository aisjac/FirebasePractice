package com.example.aisjac.firebasepractice;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatabaseActivity extends AppCompatActivity {

    EditText dateEditTex,nameInitial;
    DatePickerDialog datePickerDialog;

    Spinner breakfastSpinner,dinnerSpinner;
    EditText shopEditText;
    String[] breakfast_array,dinner_array;

    RequestQueue requestQueue;
    String URL = "https://fcm.googleapis.com/fcm/send";


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference rootReference;
    DatabaseReference userReference;
    DatabaseReference studentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        this.setTitle("Provide your information");

        dateEditTex = findViewById(R.id.DatePickerEditTextId);
        nameInitial = findViewById(R.id.NameInitialEditTextId);
        breakfastSpinner = findViewById(R.id.BreakFastSpinnerId);
        dinnerSpinner = findViewById(R.id.DinnerSpinnerId);
        shopEditText = findViewById(R.id.DailyShopEditTextId);

        requestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");




        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        rootReference = FirebaseDatabase.getInstance().getReference();

        if (firebaseUser != null){
            userReference = rootReference.child(firebaseUser.getUid());
            studentReference = userReference.child("Student");
        }



        breakfast_array = getResources().getStringArray(R.array.breakfast);
        dinner_array = getResources().getStringArray(R.array.dinner);

        ArrayAdapter<String> uniAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,breakfast_array);
        breakfastSpinner.setAdapter(uniAdapter);

        ArrayAdapter<String> depAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,dinner_array);
        dinnerSpinner.setAdapter(depAdapter);

//        Intent intent = getIntent();
//        String value = intent.getStringExtra("value");
//        catchUserIdEditText.setText(value);

        dateEditTex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePicker datePicker = new DatePicker(DatabaseActivity.this);
                int currentDay = datePicker.getDayOfMonth();
                int currentMonth = datePicker.getMonth();
                int currentYear = datePicker.getYear();

                datePickerDialog = new DatePickerDialog(DatabaseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        dateEditTex.setText(i2+"/"+(i1+1)+"/"+i);
                    }
                },currentYear,currentMonth,currentDay);
                datePickerDialog.show();

            }
        });



    }


    //Datepicker Dialog by Button Click

//    public void picDate(View view) {
//        if(view.getId()==R.id.DatePickerButtonId){
//
//            DatePicker datePicker = new DatePicker(this);
//            int currentDay = datePicker.getDayOfMonth();
//            int currentMonth = datePicker.getMonth();
//            int currentYear = datePicker.getYear();
//
//            datePickerDialog = new DatePickerDialog(DatabaseActivity.this, new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//
//                    dateEditTex.setText(i2+"/"+(i1+1)+"/"+i);
//                }
//            },currentYear,currentMonth,currentDay);
//            datePickerDialog.show();
//        }
//
//
//    }

    public void saveInfo(View view) {

        String breakfast = breakfastSpinner.getSelectedItem().toString();
        String dinner = dinnerSpinner.getSelectedItem().toString();
        String daily_shop =  shopEditText.getText().toString();
        String date = dateEditTex.getText().toString();
        String name_initial = nameInitial.getText().toString();
        String id = studentReference.push().getKey();

        if (!date.equals("") && !name_initial.equals("") && !daily_shop.equals("")){

            Student student = new Student(id,name_initial,breakfast,dinner,daily_shop,date);

//            Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

            studentReference.child(id).setValue(student);
            Toast.makeText(this, "Data added Successfully !", Toast.LENGTH_SHORT).show();
            sendNotification();



            startActivity(new Intent(this,ProfileActivity.class));

        }else {
            if(date.equals("")){
                Toast.makeText(this, "Date field can not be empty !", Toast.LENGTH_SHORT).show();
            }else if (name_initial.equals("")){
                Toast.makeText(this, "Initial field can not be empty !", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Daily Shop field can not be empty !", Toast.LENGTH_SHORT).show();
            }
        }




    }

    private void sendNotification(){

        String user = firebaseUser.getEmail();



        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","MessReport Updated by "+user);
            notificationObj.put("body","If you don't update your info.Please update it right now.");
            json.put("notification",notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyAF0rAh0ptZ54fWqfY0WpL71R04mStYlAQ");
                    return header;
                }
            };
            requestQueue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }

    }









//    @Override
//    public void onBackPressed() {
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DatabaseActivity.this);
//        alertDialog.setTitle("Exit !!!");
//        alertDialog.setMessage("Are you sure ?");
//        alertDialog.setIcon(R.drawable.warning_icon);
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finishAffinity();
//            }
//        });
//
//        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        AlertDialog alertDialog1 = alertDialog.create();
//        alertDialog1.show();
//
//    }



}
