package com.example.aisjac.firebasepractice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    TextView nametextView,totalMealTextView,totalDebitTextView,currentStatusTextView;
    ListView listView;

    ArrayList<String> breakfastList = new ArrayList<>();
    ArrayList<String> dinnerList = new ArrayList<>();
    ArrayList<String> dailyShopList = new ArrayList<>();

    ArrayList<String> totalBreakfastList = new ArrayList<>();
    ArrayList<String> toalDinnerList = new ArrayList<>();
    ArrayList<String> totalShopList = new ArrayList<>();

    ArrayList<Student> StudentList;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference rootReference;
    DatabaseReference userReference;
    DatabaseReference studentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setTitle("Profile");

        nametextView = findViewById(R.id.ProfileNameTVId);
        totalMealTextView = findViewById(R.id.TotalMealTVId);
        totalDebitTextView = findViewById(R.id.TotalDebitTVId);
        currentStatusTextView = findViewById(R.id.CurrentStatusTVId);

        listView = findViewById(R.id.DailyStatusListViewId);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        rootReference = FirebaseDatabase.getInstance().getReference();

        if (firebaseUser != null){
            userReference = rootReference.child(firebaseUser.getUid());
            studentReference = userReference.child("Student");
        }

        nametextView.setText(firebaseUser.getEmail());



        studentReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                StudentList = new ArrayList<>();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Student student = data.getValue(Student.class);
                    StudentList.add(student);

                    breakfastList.add(student.getBreakfast());
                    dinnerList.add(student.getDinner());
                    dailyShopList.add(student.getDaily_shop());

                    //convert String array to double array and show in textView

                    double[] doubleList = new double[breakfastList.size()];
                    double sum = 0;
                    for (int i = 0; i < breakfastList.size(); ++i) {
                        doubleList[i] = Double.parseDouble(breakfastList.get(i));
                        sum += doubleList[i];
                    }




                    double[] doubleList2 = new double[dinnerList.size()];
                    double sum2 = 0;
                    for (int i = 0; i < dinnerList.size(); ++i) {
                        doubleList2[i] = Double.parseDouble(dinnerList.get(i));
                        sum += doubleList2[i];
                    }

                    double totalSum = sum+sum2;

                    totalMealTextView.setText(""+totalSum);



                    double[] doubleList3 = new double[dailyShopList.size()];
                    double sum3 = 0;
                    for (int i = 0; i < dailyShopList.size(); ++i) {
                        doubleList3[i] = Double.parseDouble(dailyShopList.get(i));
                        sum3 += doubleList3[i];
                    }

                    totalDebitTextView.setText("" +sum3);





                }

//              MyAdapter myadapter = new MyAdapter(ProfileActivity.this, dateList,breakfastList,dinnerList,dailyShopList);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity.this,android.R.layout.simple_list_item_1,dailyShopList);

                MyAdapter myAdapter = new MyAdapter(ProfileActivity.this, StudentList);
                listView.setAdapter(myAdapter);

//                if(myAdapter.getCount() == 0)
//                    nodataTV.setVisibility(View.VISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Error :"+databaseError, Toast.LENGTH_SHORT).show();

            }
        });

        rootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot user: dataSnapshot.getChildren()){
                    //in every iteration it will give every user

                    for(DataSnapshot student: user.getChildren()){
                        //in every iteration it will give student(child of every user)
                        //create a List to keep the data of every student

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


                            double[] doubleList3 = new double[totalShopList.size()];
                            double sum3 = 0;
                            for (int i = 0; i < totalShopList.size(); ++i) {
                                doubleList3[i] = Double.parseDouble(totalShopList.get(i));
                                sum3 += doubleList3[i];
                            }


                            double mRate = sum3/totalSum;

                            String meal = totalMealTextView.getText().toString();
                            double total_meal = Double.parseDouble(meal);

                            String debit = totalDebitTextView.getText().toString();
                            double total_debit = Double.parseDouble(debit);

                            double status = total_debit-(total_meal*mRate);

                            currentStatusTextView.setText(new DecimalFormat("##.##").format(status));




                        }

                        //After terminating this loop we have to collect only the last data. But how? ok. just craete a stack instead of arrayList
                        //it will return the last data given by the particular user. Yup! it is. Let me check it out
                        // Log.d("Last Data", studentStack.pop().getDate()); //it will print the last data of every user
                        //it is time to show it in the view

//                        Student lastStudent = studentStack.pop();
//                        lastStudentList.add(lastStudent);



                    }


                }

//                Adapter adapter = new Adapter(StatusActivity.this, lastStudentList);
//                listView.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.edit_delete_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = menuInfo.position;

        Student student = StudentList.get(position);

        switch (itemId){
            case R.id.editId:
                Intent intent = new Intent(this, UpdateActivity.class);
                intent.putExtra("student", student);
                startActivity(intent);

                break;
            case R.id.deleteId:
//                studentReference.child(student.getId()).removeValue();
                Toast.makeText(this, "Opps ! Delete option deleted :D Hahaha ;)", Toast.LENGTH_SHORT).show();

                break;
        }

        return super.onContextItemSelected(item);
    }



    public void signOut(View view) {

        if (view.getId() == R.id.SignOutButtonId) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Log out Successfully !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
        }
    }


    public void addNew(View view) {
        Intent intent = new Intent(ProfileActivity.this,DatabaseActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
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

}
