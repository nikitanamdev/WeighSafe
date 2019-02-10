package com.example.weighsafe;

import android.app.AlarmManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView t1, t2;
    Button b1,b2,b3;
    String old="0";
    AlarmManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //t2=(TextView)findViewById(R.id.textView3);
        //t2.setText("Hello World");
        b1=(Button) findViewById(R.id.button);
        b2=(Button) findViewById(R.id.button2);
        b3=(Button) findViewById(R.id.button3);
        
        startService(new Intent(this, YourService.class));

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String posted_by = "111-333-222-4";

                String uri = "tel:" + posted_by.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Change.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        t1=(TextView)findViewById(R.id.textView2);
        String str = intent.getStringExtra("Data");
        /*Intent intent1=new Intent(MainActivity.this,YourService.class);
        intent1.putExtra("value",str);
        startActivity(intent1);*/
       /* Bundle extras=getIntent().getExtras();

        if(extras!=null)
        {

            t1.setText(extras.getString("Data"));
        }
*/

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                    /*Intent intent=new Intent(MainActivity.this,YourService.class);
                    intent.putExtra("value1","0");
                    startActivity(intent);*/

                Toast.makeText(MainActivity.this, "The Value gets disabled",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
