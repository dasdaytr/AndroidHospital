package com.example.hospital20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class FulInformationUser extends AppCompatActivity {
    private TextView text1, text2, text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ful_information_user);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        init();
        Intent();
    }

    private void init(){
        text1 = findViewById(R.id.textView);
        text2 = findViewById(R.id.textView3);
        text3 = findViewById(R.id.textView4);
    }

    private void Intent(){
        Intent i = getIntent();
        assert i != null;
        text1.setText("Болезнь "+i.getStringExtra("name_disease"));
        text2.setText("Описание болезни \n" + i.getStringExtra("des_of_disease"));
        text3.setText("Вердикт врача и лечение\n" + i.getStringExtra("verdict"));
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
                startActivity(new Intent(FulInformationUser.this, HistoryActivity.class));
                this.finish();
                return true;
            case R.id.action_settings:
                FirebaseAuth.getInstance().signOut();
                startActivity( new Intent(FulInformationUser.this,MainActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}