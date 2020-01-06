package com.example.aisjac.firebasepractice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class UpdateActivity extends AppCompatActivity {
    EditText dateEditText,initialEditText,breakfastEditText,launchEditText,dailyshopEditText;
    String id;

    RequestQueue requestQueue;
    String URL = "https://fcm.googleapis.com/fcm/send";

    DatePickerDialog datePickerDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference rootReference;
    DatabaseReference userReference;
    DatabaseReference studentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        this.setTitle("Update your information");

        dateEditText = findViewById(R.id.UpdateDatePickerEditTextId);
        initialEditText = findViewById(R.id.UpdateInitialEditTextId);
        breakfastEditText = findViewById(R.id.UpdateBreakfastEditTextId);
        launchEditText = findViewById(R.id.UpdateLaunchEditTextId);
        dailyshopEditText = findViewById(R.id.UpdateDailyShopEditTextId);

        requestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        rootReference = FirebaseDatabase.getInstance().getReference();

        if (firebaseUser != null){
            userReference = rootReference.child(firebaseUser.getUid());
            studentReference = userReference.child("Student");
        }

        Intent intent = getIntent();
        Student student = (Student) intent.getSerializableExtra("student");

        dateEditText.setText(student.getDate());
        initialEditText.setText(student.getName_initial());
        breakfastEditText.setText(student.getBreakfast());
        launchEditText.setText(student.getDinner());
        dailyshopEditText.setText(student.getDaily_shop());
        id = student.getId();

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePicker datePicker = new DatePicker(UpdateActivity.this);
                int currentDay = datePicker.getDayOfMonth();
                int currentMonth = datePicker.getMonth();
                int currentYear = datePicker.getYear();

                datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        dateEditText.setText(i2+"/"+(i1+1)+"/"+i);
                    }
                },currentYear,currentMonth,currentDay);
                datePickerDialog.show();

            }
        });
    }

    public void UpdateInfo(View view) {
        String date = dateEditText.getText().toString();
        String initial = initialEditText.getText().toString();
        String breakfast = breakfastEditText.getText().toString();
        String launch = launchEditText.getText().toString();
        String dailyShop = dailyshopEditText.getText().toString();


        if (!date.equals("") && !initial.equals("") && !breakfast.equals("")&& !launch.equals("")&& !dailyShop.equals("")){


            Student student = new Student(id,initial,breakfast,launch,dailyShop,date);


            studentReference.child(id).setValue(student);
            Toast.makeText(this, "Data Updated Successfully !", Toast.LENGTH_SHORT).show();
            sendNotification();

            startActivity(new Intent(this,ProfileActivity.class));

        }else {
            if(date.equals("")){
                Toast.makeText(this, "Date field can not be empty !", Toast.LENGTH_SHORT).show();
            }else if (initial.equals("")){
                Toast.makeText(this, "Name Initial field can not be empty !", Toast.LENGTH_SHORT).show();
            }else if (breakfast.equals("")){
                Toast.makeText(this, "Lunch field can not be empty !", Toast.LENGTH_SHORT).show();
            }else if (launch.equals("")){
                Toast.makeText(this, "Dinner field can not be empty !", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Daily Shop field can not be empty !", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void sendNotification(){

        Intent intent = getIntent();
        Student student = (Student) intent.getSerializableExtra("student");

        String dt = student.getDate();
        String bf = student.getBreakfast();
        String lnch = student.getDinner();
        String ds = student.getDaily_shop();

        String user = firebaseUser.getEmail();
        String breakfast = breakfastEditText.getText().toString();
        String launch = launchEditText.getText().toString();
        String dailyShop = dailyshopEditText.getText().toString();
        String date = dateEditText.getText().toString();



        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","MessReport Updated by "+user);
            notificationObj.put("body","Previous Date : "+dt+"\t\t"+"Current Date : "+date +"\n"+"Previous Launch : "+bf+"\t\t"+"Current Launch : "+breakfast +"\n"+"Previous Dinner : "+lnch+"\t\t"+"Current Dinner : "+launch +"\n"+"Previous Shop : "+ds+"\t\t"+"Current Shop : "+dailyShop);
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

}
