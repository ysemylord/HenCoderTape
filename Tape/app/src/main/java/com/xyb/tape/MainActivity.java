package com.xyb.tape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xyb.tape.ui.MyTap;

public class MainActivity extends AppCompatActivity {

    private TextView kgTV;
    private MyTap myTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kgTV = (TextView) findViewById(R.id.kg_textview);
        myTap = (MyTap) findViewById(R.id.my_tap);
        myTap.setStartKg(30f);
        myTap.setEndKg(40f);
        myTap.setmOuterInterface(new MyTap.OuterInterface() {
            @Override
            public void nowKg(String nowKG) {
                kgTV.setText(nowKG + "kg");
            }
        });

    }
}
