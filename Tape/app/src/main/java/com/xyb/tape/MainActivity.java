package com.xyb.tape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xyb.tape.ui.ScrollDetection;

public class MainActivity extends AppCompatActivity {

    private TextView kgTV;
    private ScrollDetection myTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kgTV = (TextView) findViewById(R.id.kg_textview);
        myTap = (ScrollDetection) findViewById(R.id.my_tap);
        myTap.setmOuterInterface(new ScrollDetection.OuterInterface() {
            @Override
            public void nowKg(String nowKG) {
                kgTV.setText("scrollX is "+nowKG );
            }
        });

    }
}
