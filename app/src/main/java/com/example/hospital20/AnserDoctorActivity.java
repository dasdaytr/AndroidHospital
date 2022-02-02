package com.example.hospital20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hospital20.Models.Doctor;
import com.example.hospital20.Models.History;
import com.example.hospital20.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnserDoctorActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText v1, v2, v3;
    private FirebaseAuth mAuthD;
    private FirebaseDatabase dbD;
    private DatabaseReference rfD;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference rf;
    private Button btn;
    private final String EMAIL_DOCTOR = "email_doctor";
    private final String UID = "UID";
    private final String DISEASE = "disease";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anser_doctor);
        ActionBar actionBar =getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        init();
    }
    public void init(){

        mAuthD = FirebaseAuth.getInstance();
        dbD = FirebaseDatabase.getInstance();
        rfD = dbD.getReference("Doctor");
        firebaseUser = mAuthD.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        rf = db.getReference("User");


        v1 = findViewById(R.id.textView5);
        v2 = findViewById(R.id.NameB);
        v3 = findViewById(R.id.Lechenie);

        btn = findViewById(R.id.sentVerdict);
        btn.setOnClickListener(this);

        Intent i = getIntent();
        v1.setText(i.getStringExtra(DISEASE));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void getInformation(){
        Intent i = getIntent();
        ValueEventListener valueEventListener  = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Doctor doctor = d.getValue(Doctor.class);
                    if (doctor.email.toLowerCase().equals(firebaseUser.getEmail())){
                        for(User user: doctor.userArrayList){
                            if(user.email.equals(i.getStringExtra("email_doctor"))){
                                if(user.historyUser == null) user.historyUser = new ArrayList<>();
                                doctor.userArrayList.remove(user);
                                ValueEventListener val = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot s: dataSnapshot.getChildren()){
                                            User user1 = s.getValue(User.class);
                                            assert user1 != null;
                                            if(user1.email.toLowerCase().equals(i.getStringExtra("email_doctor"))){
                                                if(user1.historyUser == null)
                                                    user1.historyUser = new ArrayList<>();
                                                user1.historyUser.add(new History(v2.getText().toString(),v1.getText().toString(),v3.getText().toString()));
                                                rf.child(Objects.requireNonNull(s.getKey())).child("historyUser").setValue(user1.historyUser);
                                                rfD.child(Objects.requireNonNull(d.getKey())).child("userArrayList").setValue(doctor.userArrayList);
                                                startActivity(new Intent(AnserDoctorActivity.this,ActivityDoctor.class));
                                                finish();
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                rf.addListenerForSingleValueEvent(val);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        rfD.addListenerForSingleValueEvent(valueEventListener);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sentVerdict:
                getInformation();
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(AnserDoctorActivity.this, ActivityDoctor.class));
                this.finish();
                return true;
            case R.id.action_settings:
                mAuth.signOut();
                startActivity( new Intent(AnserDoctorActivity.this,MainActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}