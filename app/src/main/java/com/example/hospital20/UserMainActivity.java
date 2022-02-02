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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class UserMainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private ArrayAdapter <String> arrayAdapter;
    private List <String> listData;
    private List <Doctor> doctorList;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference rf;

    private FirebaseAuth mAuthD;
    private FirebaseDatabase dbD;
    private DatabaseReference rfD;

    private Button btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        init();
        getUserName();
        getDataFromDB();
        setOnClickListiner();
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
                FirebaseAuth.getInstance().signOut();
                startActivity( new Intent(UserMainActivity.this,MainActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(){
        listView = findViewById(R.id.ListDoctor);
        listData = new ArrayList<>();
        doctorList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
        listView.setAdapter(arrayAdapter);
        //Для пациентов
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        rf = db.getReference("User");
        // Врач
        dbD = FirebaseDatabase.getInstance();
        rfD = dbD.getReference("Doctor");

        btnHistory = findViewById(R.id.historyDismic);
        btnHistory.setOnClickListener(this);

    }

    private void getDataFromDB(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listData.size() >0) listData.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Doctor doctor = dataSnapshot1.getValue(Doctor.class);
                    assert doctor != null;
                    doctor.setUID(dataSnapshot1.getKey());
                    listData.add("Врач " + doctor.name + " " + doctor.lastname + "(" + doctor.special + ")");
                    doctorList.add(doctor);
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
    public void getUserName(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        TextView textView = findViewById(R.id.Welcom);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listData.size() >0) listData.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    User user = dataSnapshot1.getValue(User.class);
                    assert user != null;
                    if(firebaseUser.getEmail().equals(user.email)){
                        textView.setText("Добро пожаловаться " + user.firstname);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        rf.addValueEventListener(valueEventListener);
    }
    public void setOnClickListiner(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Doctor doctor = doctorList.get(i);
                Intent t = new Intent(UserMainActivity.this,RecordPatientActivity.class);
                t.putExtra("email_doctor",doctor.email);
                t.putExtra("UID",doctor.uid);
                startActivity(t);
                finish();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.historyDismic:
                startActivity(new Intent(UserMainActivity.this,HistoryActivity.class));
                finish();
                break;
        }


    }
}