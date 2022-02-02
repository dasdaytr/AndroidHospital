package com.example.hospital20;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hospital20.Models.Doctor;
import com.example.hospital20.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecordPatientActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuthD;
    private FirebaseDatabase dbD;
    private DatabaseReference rfD;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference rf;

    private String Uid;
    private FirebaseUser firebaseUser;
    private EditText editText;

    private List<User> userList;
    private Doctor doctor;
    private User useraaa;

    private ArrayAdapter <String> arrayAdapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_patient);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        init();

    }

    public void init(){
        mAuthD = FirebaseAuth.getInstance();
        dbD = FirebaseDatabase.getInstance();
        rfD = dbD.getReference("Doctor");

        db = FirebaseDatabase.getInstance();
        rf = db.getReference("User");
        firebaseUser = mAuthD.getCurrentUser();
        userList = new ArrayList<>();
        editText = findViewById(R.id.editTextTextMultiLine);
        button = findViewById(R.id.bntRecord);
        button.setOnClickListener(this);
    }


    public void getUserList() {
        Intent i = getIntent();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(userList.size() > 0) userList.clear();
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Intent i  = getIntent();
                    Doctor doctor = d.getValue(Doctor.class);
                    assert doctor != null;
                    if(i.getStringExtra("email_doctor").equals(doctor.email)){
                        ValueEventListener val = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot s: dataSnapshot.getChildren()){
                                    User user = s.getValue(User.class);
                                    System.out.println(firebaseUser.getEmail() + "  " + user.email.toLowerCase());
                                    if(Objects.equals(firebaseUser.getEmail(), user.email.toLowerCase())){
                                        if(doctor.userArrayList==null){
                                            doctor.userArrayList = new ArrayList<>();
                                        }
                                        user.disease = editText.getText().toString();
                                        doctor.userArrayList.add(user);
                                        rfD.child(i.getStringExtra("UID")).child("userArrayList").setValue(doctor.userArrayList);
                                        startActivity(new Intent(RecordPatientActivity.this,UserMainActivity.class));
                                        finish();
                                        return;

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        rf.addListenerForSingleValueEvent(val);
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
            case R.id.bntRecord:
                getUserList();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(RecordPatientActivity.this, UserMainActivity.class));
                this.finish();
                return true;
            case R.id.action_settings:
                FirebaseAuth.getInstance().signOut();
                startActivity( new Intent(RecordPatientActivity.this,MainActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
