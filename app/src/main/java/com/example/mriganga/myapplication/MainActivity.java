package com.example.mriganga.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b  = (Button)findViewById(R.id.search_button);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText name = (EditText)findViewById(R.id.name);
                EditText room = (EditText)findViewById(R.id.room_name);
                Intent i = new Intent(MainActivity.this,ChatActivity.class);
                i.putExtra("name",name.getText().toString());
                i.putExtra("room",room.getText().toString());
                startActivity(i);
            }
        });
    }
}
