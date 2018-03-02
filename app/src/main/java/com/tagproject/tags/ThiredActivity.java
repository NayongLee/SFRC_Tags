package com.tagproject.tags;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by 이나영 on 2017-10-23.
 */

public class ThiredActivity extends AppCompatActivity {
    BluetoothService btService = MainActivity.btService;
    String checkState ;

    RadioGroup group1;
    RadioGroup group2;
    RadioButton e01;
    RadioButton e02;
    RadioButton e03;
    RadioButton e04;
    RadioButton e05;
    RadioButton e06;
    RadioButton e07;
    RadioButton e08;
    RadioButton e09;
    RadioButton e10;
    RadioButton e11;
    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i != -1){
                group2.setOnCheckedChangeListener(null);
                group2.clearCheck();
                group2.setOnCheckedChangeListener(listener2);
                switch (i) {
                    case R.id.check01:
                        checkState = "a";
                        break;
                    case R.id.check03:
                        checkState = "c";
                        break;
                    case R.id.check05:
                        checkState = "e";
                        break;
                    case R.id.check07:
                        checkState = "g";
                        break;
                    case R.id.check09:
                        checkState = "i";
                        break;
                    case R.id.check11:
                        checkState = "k";
                        break;
                }
                Log.d("SecondActivity", "onCheckedChange "+checkState);
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i != -1){
                group1.setOnCheckedChangeListener(null);
                group1.clearCheck();
                group1.setOnCheckedChangeListener(listener1);
                switch (i) {
                    case R.id.check02:
                        checkState = "b";
                        break;
                    case R.id.check04:
                        checkState = "d";
                        break;
                    case R.id.check06:
                        checkState = "f";
                        break;
                    case R.id.check08:
                        checkState = "h";
                        break;
                    case R.id.check10:
                        checkState = "j";
                        break;
                }
                Log.d("SecondActivity", "onCheckedChange "+checkState);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thired);

        group1 = (RadioGroup)findViewById(R.id.radioGroup1);
        group2 = (RadioGroup)findViewById(R.id.radioGroup2);
        e01 = (RadioButton)findViewById(R.id.check01);
        e02 = (RadioButton)findViewById(R.id.check02);
        e03 = (RadioButton)findViewById(R.id.check03);
        e04 = (RadioButton)findViewById(R.id.check04);
        e05 = (RadioButton)findViewById(R.id.check05);
        e06 = (RadioButton)findViewById(R.id.check06);
        e07 = (RadioButton)findViewById(R.id.check07);
        e08 = (RadioButton)findViewById(R.id.check08);
        e09 = (RadioButton)findViewById(R.id.check09);
        e10 = (RadioButton)findViewById(R.id.check10);
        e11 = (RadioButton)findViewById(R.id.check11);

        group1.clearCheck();
        group2.clearCheck();
        group1.setOnCheckedChangeListener(listener1);
        group2.setOnCheckedChangeListener(listener2);
    }

    public void changeCheck(String checkState){
        if(!checkState.equals("a")){ e01.setChecked(false); }
        if(!checkState.equals("b")){ e02.setChecked(false); }
        if(!checkState.equals("c")){ e03.setChecked(false); }
        if(!checkState.equals("d")){ e04.setChecked(false); }
        if(!checkState.equals("e")){ e05.setChecked(false); }
        if(!checkState.equals("f")){ e06.setChecked(false); }
        if(!checkState.equals("g")){ e07.setChecked(false); }
        if(!checkState.equals("h")){ e08.setChecked(false); }
        if(!checkState.equals("i")){ e09.setChecked(false); }
        if(!checkState.equals("j")){ e10.setChecked(false); }
        if(!checkState.equals("k")){ e11.setChecked(false); }
    }

    public void onsendButtonClicked(View view){
        //bluetooth send message
        String sendData = "#"+checkState+"&";
        byte[] getText = sendData.getBytes();
        btService.write(getText);
    }
}
