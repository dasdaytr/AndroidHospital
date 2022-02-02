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
import com.example.hospital20.Models.History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllDoctorInHospital extends AppCompatActivity {
     private ListView listView;
     private FirebaseAuth auth;
     private FirebaseDatabase database;
     private ArrayAdapter<String> arrayAdapter;
     private List<String> listData;
     private List <Doctor> historyList;
     private String FIRSTNAME = "FirstName";
     private String LASTNAME = "LastName";
     private String AMOUNT = "amount";
     private String EMAIL = "Email";
    private String DOCTOR_TABLE = "Doctor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctor_in_hospital);
        init();
        getAllDoctors();
        setOnClickListiner();
    }
    private void init(){
        listView = findViewById(R.id.IdAmountOfDoctor);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        listData = new ArrayList<>();
        historyList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
        listView.setAdapter(arrayAdapter);
    }

    private void getAllDoctors(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d1: dataSnapshot.getChildren()){
                    Doctor doctor = d1.getValue(Doctor.class);
                    assert  doctor != null;
                    listData.add(doctor.getName() + " " + doctor.getLastname() + " (" + doctor.special+ ")");
                    historyList.add(doctor);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        database.getReference(DOCTOR_TABLE).addListenerForSingleValueEvent(valueEventListener);

    }
    public void setOnClickListiner(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Doctor doctor = historyList.get(i);
                Intent t = new Intent(AllDoctorInHospital.this,InformationADoctor.class);
                System.out.println(doctor.name + " " + doctor.lastname + " " + doctor.userArrayList.size() + " " + doctor.email);
                t.putExtra(FIRSTNAME,doctor.name);
                t.putExtra(LASTNAME,doctor.lastname);
                t.putExtra(AMOUNT,String.valueOf(doctor.userArrayList.size()));
                t.putExtra(EMAIL,doctor.email);
                startActivity(t);
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AllDoctorInHospital.this,MainActivity.class));
                finish();
                return true;
            case R.id.addDoctor:
                startActivity(new Intent(AllDoctorInHospital.this,AddDoctorActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_doctor, menu);
        return true;
    }
}
