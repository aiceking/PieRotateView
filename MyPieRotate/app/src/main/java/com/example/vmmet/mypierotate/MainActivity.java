package com.example.vmmet.mypierotate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.vmmet.mypierotate.test.Test1Activity;
import com.example.vmmet.mypierotate.test.Test2Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
private Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                startActivity(new Intent(this, Test1Activity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this, Test2Activity.class));
                break;
        }
    }
}
