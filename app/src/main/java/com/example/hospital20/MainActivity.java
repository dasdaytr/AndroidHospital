package com.example.hospital20;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hospital20.Models.Doctor;
import com.example.hospital20.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText fieldEmail, fieldPassword;
    private Button signIn, signUp;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference rf;

    private FirebaseAuth mAuthD;
    private FirebaseDatabase dbD;
    private DatabaseReference rfD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        rf = db.getReference("User");

        mAuthD = FirebaseAuth.getInstance();
        dbD = FirebaseDatabase.getInstance();
        rfD = dbD.getReference("Doctor");

        fieldEmail = findViewById(R.id.fieldEmail);
        fieldPassword = findViewById(R.id.fieldPassword);

        signIn = findViewById(R.id.btnSignIn);
        signUp = findViewById(R.id.btnSignUp);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignIn:
                SignIn();
                break;
            case R.id.btnSignUp:
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                finish();
                break;

        }
    }

    public void SignIn(){

        mAuth.signInWithEmailAndPassword(fieldEmail.getText().toString(),fieldPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if(fieldEmail.getText().toString().equals("dandoctor@mail.ru")){
                        startActivity(new Intent(MainActivity.this,AllDoctorInHospital.class));
                        finish();
                    }
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                User user = dataSnapshot1.getValue(User.class);
                                if(fieldEmail.getText().toString().equals(user.email)){
                                    startActivity(new Intent(MainActivity.this, UserMainActivity.class));
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    rf.addValueEventListener(valueEventListener);
                    ValueEventListener val = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                Doctor user = dataSnapshot1.getValue(Doctor.class);
                                if(fieldEmail.getText().toString().equals(user.email)){
                                    startActivity(new Intent(MainActivity.this, ActivityDoctor.class));
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    };
                    rfD.addValueEventListener(val);
                }else{
                    System.out.println("Провал");
                }
            }
        });
    }

}
