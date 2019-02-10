package com.example.weighsafe;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Change extends AppCompatActivity {

    EditText t1;
    Button b;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change);
        t1= (EditText)findViewById(R.id.text);
        b=(Button) findViewById(R.id.button4);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(Change.this,MainActivity.class);
                intent.putExtra("Data",t1.getText().toString());
                startActivity(intent);
            }
        });

    }
}
