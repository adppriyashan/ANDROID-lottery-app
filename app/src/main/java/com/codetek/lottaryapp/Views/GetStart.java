package com.codetek.lottaryapp.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codetek.lottaryapp.R;

public class GetStart extends AppCompatActivity {

    Button get_start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codetek.lottaryapp.R.layout.activity_get_start);

        get_start_button=findViewById(R.id.get_start_button);
        get_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetStart.this,Login.class));
            }
        });
    }
}