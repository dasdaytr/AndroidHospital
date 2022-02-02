package com.example.hospital20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

public class HistoryActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference rf;
    private FirebaseUser firebaseUser;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> listData;
    private List <History> historyList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        init();
        getDataFromDB();
        setOnClickListiner();
    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        rf = db.getReference("User");
        firebaseUser = mAuth.getCurrentUser();

        listView = findViewById(R.id.idHistory);
        listData = new ArrayList<>();
        historyList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
        listView.setAdapter(arrayAdapter);
    }
    private void getDataFromDB(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listData.size() >0) listData.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    User user = dataSnapshot1.getValue(User.class);
                    assert user != null;
                    if(Objects.equals(firebaseUser.getEmail(), user.email) && user.historyUser != null){
                        for(History h:user.historyUser){
                            listData.add(h.nameDisease);
                            historyList.add(h);
                        }
                    }
                }
                arrayAdapter.notifyDataSetChanged();
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
                History history = historyList.get(i);
                Intent t = new Intent(HistoryActivity.this,FulInformationUser.class);
                t.putExtra("name_disease",history.nameDisease);
                t.putExtra("des_of_disease",history.desOfDisease);
                t.putExtra("verdict",history.verdictDoctor);
                startActivity(t);
                finish();
            }
        });
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
                startActivity(new Intent(HistoryActivity.this, UserMainActivity.class));
                this.finish();
                return true;
            case R.id.action_settings:
                FirebaseAuth.getInstance().signOut();
                startActivity( new Intent(HistoryActivity.this,MainActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}