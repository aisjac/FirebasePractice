package com.example.aisjac.firebasepractice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    EditText emailEditText,passwordEditText;
    ProgressBar progressBar;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        progressBar = findViewById(R.id.progressBarId);
        emailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        if(firebaseUser != null){
            startActivity(new Intent(this,StatusActivity.class));
        }



    }


    public void logIn(final View view) {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!email.equals("") && !password.equals("")){
            progressBar.setVisibility(view.VISIBLE);


            Task<AuthResult> authResultTask = firebaseAuth.signInWithEmailAndPassword(email,password);

            authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Log in Succesfull !", Toast.LENGTH_SHORT).show();
                        firebaseUser = firebaseAuth.getCurrentUser();

                        // String value = firebaseUser.getEmail();
                        Intent intent = new Intent(MainActivity.this,StatusActivity.class);
                        // intent.putExtra("value",value);
                        startActivity(intent);
                        progressBar.setVisibility(view.GONE);

                    }
                }
            });
            authResultTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressBar.setVisibility(view.GONE);
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            if(email.equals("")){
                Toast.makeText(this, "Email field can not be empty !", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Password field can not be empty !", Toast.LENGTH_SHORT).show();
            }

        }




    }

    public void signUp(final View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!email.equals("") && !password.equals("")){
            progressBar.setVisibility(view.VISIBLE);

            Task<AuthResult> authResultTask = firebaseAuth.createUserWithEmailAndPassword(email,password);

            authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(view.GONE);
                        Toast.makeText(MainActivity.this, "New User Created !\nPlease Login Now !", Toast.LENGTH_LONG).show();

                    }

                }
            });

            authResultTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(view.GONE);
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            if(email.equals("")){
                Toast.makeText(this, "Email field can not be empty !", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Password field can not be empty !", Toast.LENGTH_SHORT).show();
            }
        }




    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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
