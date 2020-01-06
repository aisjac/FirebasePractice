package com.example.aisjac.firebasepractice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class StatusActivity extends AppCompatActivity {

//    boolean doubleBackToExitPressedOnce = false;

    ListView listView;
    TextView textView,totalMeal,totalShop,mealRate;

    ArrayList<String> totalBreakfastList = new ArrayList<>();
    ArrayList<String> toalDinnerList = new ArrayList<>();
    ArrayList<String> totalShopList = new ArrayList<>();

    ArrayList<Student> lastStudentList;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference rootReference;
    DatabaseReference userReference;
    DatabaseReference studentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        this.setTitle("Status");

        listView = findViewById(R.id.ListViewId2);
        textView = findViewById(R.id.TotalUserShowTextViewId);
        totalMeal = findViewById(R.id.TotalMealTextViewId);
        totalShop = findViewById(R.id.TotalShopTextViewId);
        mealRate = findViewById(R.id.MealRateTextViewId);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        rootReference = FirebaseDatabase.getInstance().getReference();

        if (firebaseUser != null){
            userReference = rootReference.child(firebaseUser.getUid());
            studentReference = userReference.child("Student");
        }

        rootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lastStudentList = new ArrayList<>();

                int totalUser=0;
                for(DataSnapshot user: dataSnapshot.getChildren()){
                    //in every iteration it will give every user
                    totalUser++;
                    textView.setText("Total Number of Users :" +totalUser);


                    for(DataSnapshot student: user.getChildren()){
                        //in every iteration it will give student(child of every user)
                        //create a List to keep the data of every student



                        Stack<Student> studentStack = new Stack<>();

                        for(DataSnapshot studentId: student.getChildren()){
                            //Every iteration it will collect all the data given by every user
                            Student aStudent = studentId.getValue(Student.class);

                            totalBreakfastList.add(aStudent.getBreakfast());
                            toalDinnerList.add(aStudent.getDinner());
                            totalShopList.add(aStudent.getDaily_shop());


                            double[] doubleList = new double[totalBreakfastList.size()];
                            double sum = 0;
                            for (int i = 0; i < totalBreakfastList.size(); ++i) {
                                doubleList[i] = Double.parseDouble(totalBreakfastList.get(i));
                                sum += doubleList[i];
                            }




                            double[] doubleList2 = new double[toalDinnerList.size()];
                            double sum2 = 0;
                            for (int i = 0; i < toalDinnerList.size(); ++i) {
                                doubleList2[i] = Double.parseDouble(toalDinnerList.get(i));
                                sum += doubleList2[i];
                            }

                            double totalSum = sum+sum2;

                            totalMeal.setText(""+ totalSum);



                            double[] doubleList3 = new double[totalShopList.size()];
                            double sum3 = 0;
                            for (int i = 0; i < totalShopList.size(); ++i) {
                                doubleList3[i] = Double.parseDouble(totalShopList.get(i));
                                sum3 += doubleList3[i];
                            }

                            totalShop.setText(""+ sum3);

                            double mRate = sum3/totalSum;

                            mealRate.setText(new DecimalFormat("##.##").format(mRate));


                            studentStack.add(aStudent);
                        }

                        //After terminating this loop we have to collect only the last data. But how? ok. just craete a stack instead of arrayList
                         //it will return the last data given by the particular user. Yup! it is. Let me check it out
                       // Log.d("Last Data", studentStack.pop().getDate()); //it will print the last data of every user
                        //it is time to show it in the view

                        Student lastStudent = studentStack.pop();
                        lastStudentList.add(lastStudent);





                    }


                }

                Adapter adapter = new Adapter(StatusActivity.this, lastStudentList);
                listView.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




      /* rootReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               for (DataSnapshot ds: dataSnapshot.getChildren()){
                    userList.add(ds.getKey());
                     allDataList.add(ds.child("Student").getValue().toString());
                     filterDataList.add (allDataList.get (allDataList.size ()-1));



               }
               Toast.makeText (StatusActivity.this, "Data :"+filterDataList, Toast.LENGTH_SHORT).show ();




               ArrayAdapter<String> adapter1 = new ArrayAdapter<>(StatusActivity.this,android.R.layout.simple_list_item_1,userList);
               listView1.setAdapter(adapter1);

//               Adapter adapter2 = new Adapter(StatusActivity.this, dateList,breakfastList,dinnerList,dailyShopList);
//               Toast.makeText(StatusActivity.this, "Adapter :"+ dateList, Toast.LENGTH_SHORT).show();
//
//               listView2.setAdapter(adapter2);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });*/

       /* studentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Student student = ds.getValue(Student.class);
                    dateList.add(student.getDate());
                    breakfastList.add(student.getBreakfast());
                    dinnerList.add(student.getDinner());
                    dailyShopList.add(student.getDaily_shop());

//                    Adapter adapter2 = new Adapter(StatusActivity.this, dateList,breakfastList,dinnerList,dailyShopList);

                }

                if (!dateList.isEmpty () && !breakfastList.isEmpty () && !dinnerList.isEmpty () && !dailyShopList.isEmpty ()){

                    dtList.add(dateList.get(dateList.size() - 1));
                    bList.add(breakfastList.get(breakfastList.size() - 1));
                    dList.add(dinnerList.get(dinnerList.size() - 1));
                    dsList.add(dailyShopList.get(dailyShopList.size() - 1));
                    Adapter adapter2 = new Adapter(StatusActivity.this, dtList,bList,dList,dsList);
                    listView2.setAdapter(adapter2);

                }else {
                    Toast.makeText (StatusActivity.this, "List is Empty !", Toast.LENGTH_SHORT).show ();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.UserId){
            Intent intent = new Intent(StatusActivity.this,ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }




    public void addNew(View view) {
        Intent intent = new Intent(StatusActivity.this,DatabaseActivity.class);
        startActivity(intent);
    }



//    @Override
//    public void onBackPressed() {
//
//        if (doubleBackToExitPressedOnce) {
//
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
//    }




    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StatusActivity.this);
        alertDialog.setTitle("Exit !!!");
        alertDialog.setMessage("Are you sure ?");
        alertDialog.setIcon(R.drawable.warning_icon);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();

    }



    public void goToProfile(View view) {
        startActivity(new Intent(this,ProfileActivity.class));
    }
}
