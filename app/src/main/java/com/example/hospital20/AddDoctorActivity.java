package com.example.hospital20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.hospital20.Models.Doctor;
import com.example.hospital20.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddDoctorActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText fieldName, fieldLastName, fieldSp, fieldEmail, fieldPassword;
    private Button btnAddDoctor, btnComeBackMainActivity;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference rf;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        relativeLayout = findViewById(R.id.DoctorA);
        init();
    }
    private void init(){
        fieldName = findViewById(R.id.DoctorName);
        fieldLastName = findViewById(R.id.DoctorLastName);
        fieldSp = findViewById(R.id.SpecialD);
        fieldEmail = findViewById(R.id.AddDoctorEmail);
        fieldPassword = findViewById(R.id.addPasswordDoctor);

        btnAddDoctor = findViewById(R.id.btnAddDoctor);
        btnComeBackMainActivity = findViewById(R.id.btnComeBackActivity);
        btnAddDoctor.setOnClickListener(this);
        btnComeBackMainActivity.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        rf = db.getReference("Doctor");

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddDoctor:
                addDoctor();
                break;
            case R.id.btnComeBackActivity:
                a();

        }
    }
    private void a(){
        startActivity(new Intent(AddDoctorActivity.this, AllDoctorInHospital.class));
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                mAuth.signOut();
                startActivity( new Intent(AddDoctorActivity.this,MainActivity.class));
                this.finish();
                break;
            case android.R.id.home:
                a();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void addDoctor(){
        if (checkEmpty(fieldName)) {
            Snackbar.make(relativeLayout, "Введите  имя", Snackbar.LENGTH_LONG).show();
            return;
        }else if(checkEmpty(fieldLastName)){
            Snackbar.make(relativeLayout, "Введите  Фамилию", Snackbar.LENGTH_LONG).show();
            return;
        }else if (checkEmpty(fieldEmail)){
            Snackbar.make(relativeLayout, "Введите  Email", Snackbar.LENGTH_LONG).show();
            return;
        }else if(fieldPassword.getText().toString().length() < 5){
            Snackbar.make(relativeLayout, "Пароль должен содержать больше 5 символов", Snackbar.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(fieldEmail.getText().toString(),fieldPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Doctor doctor = new Doctor(fieldName.getText().toString(),
                        fieldLastName.getText().toString(),
                        fieldEmail.getText().toString(),
                        fieldSp.getText().toString());
                doctor.setUserArrayList(new ArrayList<>());
                rf.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        a();
                        //Snackbar.make(mainActivity, "Регистрация прошла успешно", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    public boolean checkEmpty(EditText field){
        return TextUtils.isEmpty(field.getText().toString());
    }
}