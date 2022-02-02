package com.example.hospital20;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospital20.Models.History;
import com.example.hospital20.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signUpName, signUpLastName,signUpEmail, signUpPassword, signUpRPassword;
    private Button btnSignUpReg, btnSignUpComeBack;
    private RelativeLayout relativeLayout;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference rf;
    private RelativeLayout mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        relativeLayout = findViewById(R.id.signUpActivity);
        mainActivity = findViewById(R.id.mainActivity);
        init();

    }
    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance( );
        rf = db.getReference("User");

        signUpName = findViewById(R.id.EditTextNameId);
        signUpLastName = findViewById(R.id.EditTextLastNameId);
        signUpEmail = findViewById(R.id.EditTextEmailId);
        signUpPassword = findViewById(R.id.EditTextPasswordId);
        signUpRPassword = findViewById(R.id.EditTextRPasswordId);

        btnSignUpReg = findViewById(R.id.ButtonSignUpR);
        btnSignUpComeBack = findViewById(R.id.ButtonComeBackMainActivity);
        btnSignUpReg.setOnClickListener(this);
        btnSignUpComeBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ButtonSignUpR:
                SignUp();
                break;

        }
    }

    public void SignUp(){
        if (checkEmpty(signUpName)) {
            Snackbar.make(relativeLayout, "Введите ваше имя", Snackbar.LENGTH_LONG).show();
            return;
        }else if(checkEmpty(signUpLastName)){
            Snackbar.make(relativeLayout, "Введите вашу Фамилию", Snackbar.LENGTH_LONG).show();
            return;
        }else if (checkEmpty(signUpEmail)){
            Snackbar.make(relativeLayout, "Введите ваш Email", Snackbar.LENGTH_LONG).show();
            return;
        }else if(checkEmpty(signUpPassword) && signUpPassword.getText().toString().equals(signUpRPassword.getText().toString())){
            Snackbar.make(relativeLayout, "Пароли не совпадают или пустой", Snackbar.LENGTH_LONG).show();
            return;
        }else if(signUpPassword.getText().toString().length() < 5){
            Snackbar.make(relativeLayout, "Пароль должен содержать больше 5 символов", Snackbar.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(signUpEmail.getText().toString(),signUpPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                ArrayList <History> Lin = new ArrayList<>();
                User user = new User(signUpName.getText().toString(),
                        signUpLastName.getText().toString(),
                        signUpEmail.getText().toString(),
                        Lin);
                rf.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
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