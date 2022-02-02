package com.example.hospital20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hospital20.Models.Doctor;
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
import java.util.Locale;

public class ActivityDoctor extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> listData;
    private List <User> userList;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference rf;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuthD;
    private FirebaseDatabase dbD;
    private DatabaseReference rfD;
    private final String EMAIL_DOCTOR = "email_doctor";
    private final String UID = "UID";
    private final String DISEASE = "disease";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        init();
        getDataFromDB();
        setOnClickListiner();
    }
    private void init(){
        listView = findViewById(R.id.idHistory);
        listData = new ArrayList<>();
        userList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
        listView.setAdapter(arrayAdapter);
        //Для пациентов
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        rf = db.getReference("User");
        // Врач
        mAuthD = FirebaseAuth.getInstance();
        dbD = FirebaseDatabase.getInstance();
        rfD = dbD.getReference("Doctor");
        firebaseUser = mAuthD.getCurrentUser();

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
                startActivity( new Intent(ActivityDoctor.this,MainActivity.class));
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getDataFromDB(){
        System.out.println("asdasd");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listData.size() >0) listData.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Doctor doctor = dataSnapshot1.getValue(Doctor.class);
                    assert doctor != null;
                    if(firebaseUser.getEmail().equals(doctor.email.toLowerCase(Locale.ROOT))){
                        if(doctor.userArrayList != null) {
                            for (User user : doctor.userArrayList) {
                                user.setUid(dataSnapshot1.getKey());
                                listData.add(user.firstname + "  " + user.lastname);
                                userList.add(user);
                            }
                        }
                    }

                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        rfD.addValueEventListener(valueEventListener);
        System.out.println(listData);
    }

    public void setOnClickListiner(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userList.get(i);
                Intent t = new Intent(ActivityDoctor.this,AnserDoctorActivity.class);
                t.putExtra(EMAIL_DOCTOR,user.email);
                t.putExtra(UID,user.uid);
                t.putExtra(DISEASE,user.disease);
                startActivity(t);
                finish();
            }
        });
    }
}