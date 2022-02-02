package com.example.hospital20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hospital20.Models.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InformationADoctor extends AppCompatActivity {
    private TextView v1,v2,v3,v4;
    private String FIRSTNAME = "FirstName";
    private String LASTNAME = "LastName";
    private String AMOUNT = "amount";
    private String EMAIL = "Email";
    private String DOCTOR_TABLE = "Doctor";
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_adoctor);
        init();
        setInformation();
    }
    private void init(){
        Auth = FirebaseAuth.getInstance();

        v1 = findViewById(R.id.textView1a);
        v2 = findViewById(R.id.textView2a);
        v3 = findViewById(R.id.textView3a);
        v4 = findViewById(R.id.textView4a);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setInformation(){
        Intent i = getIntent();
        v1.setText(i.getStringExtra(FIRSTNAME));
        v2.setText(i.getStringExtra(LASTNAME));
        v3.setText(i.getStringExtra(EMAIL));
        v4.setText(i.getStringExtra(AMOUNT));
    }
    private void delDoctor(){
        ValueEventListener value = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Doctor doctor = d.getValue(Doctor.class);
                    assert doctor != null;
                    if(doctor.email.equals(getIntent().getStringExtra(EMAIL))){
                        d.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference(DOCTOR_TABLE).addListenerForSingleValueEvent(value);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(InformationADoctor.this, AllDoctorInHospital.class));
                this.finish();
                return true;
            case R.id.settings:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(InformationADoctor.this,MainActivity.class));
                finish();
                return true;
            case R.id.del:
                delDoctor();
                startActivity(new Intent(InformationADoctor.this,AllDoctorInHospital.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.del_doctor, menu);
        return true;
    }

}