package com.tagproject.tags;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE =5;//activity
    private static final int REQUEST_CONNECT_DEVICE = 1;//BT
    private static final int REQUEST_ENABLE_BT = 2;

    private static final String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final String EXTRA_DEVICE_NAME = "device_name";

    private String device_name;
    private String device_address;

    private String TAG = "MainActivity";

    static BluetoothService btService = null;
    private final Handler mHandler = new Handler(){
        //        @Override
        public void hadleMessage(Message msg){
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(btService ==null){
            btService = new BluetoothService(this, mHandler);
        }
    }

    Intent intent = null;
    public void onStartButtonClicked(View view){
        if(btService.getState()==3){
            if(device_name.equals("2.7_OLED")){
                Intent btdata = new Intent(this, FirstActivity.class);
                btdata.putExtras(intent);
                startActivityForResult(btdata, REQUEST_CODE);
            }
            if(device_name.equals("1.5_OLED")){
                Intent btdata = new Intent(this, SecondActivity.class);
                btdata.putExtras(intent);
                startActivityForResult(btdata, REQUEST_CODE);
            }
            if(device_name.equals("OLED_BAG")){
                Intent btdata = new Intent(this, ThiredActivity.class);
                btdata.putExtras(intent);
                startActivityForResult(btdata, REQUEST_CODE);
            }
        }else if(btService.getState()==0 || btService.getState()==1){
            Toast.makeText(MainActivity.this, "블루투스가 연결되지 않았습니다.",Toast.LENGTH_SHORT).show();
        }else if( btService.getState()==2){
            Toast.makeText(MainActivity.this, "블루투스에 연결중입니다.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "알맞지 않은 블루투스 입니다.",Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        TextView textView = (TextView)findViewById(R.id.textView);
        switch (requestCode){
            case REQUEST_CODE:
                if(resultCode== RESULT_OK){
                    String returnString = data.getExtras().getString("returnData");
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if(resultCode== Activity.RESULT_OK){
                    btService.getDeviceInfo(data);
                    device_address= data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
                    device_name = data.getExtras().getString(EXTRA_DEVICE_NAME);
                    textView.setText("선택된 기종 : "+device_name);
                    Button start = (Button)findViewById(R.id.StartButton);
                    start.setTextColor(Color.WHITE);
                    this.intent = data;

                }else {
                    textView.setText("선택된 기종 : 연결되지 않음");
                }
                break;
            case REQUEST_ENABLE_BT:
                if(resultCode== Activity.RESULT_OK){//확인
                    btService.scanDevice();
                }else{//취소
                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
    }

    public void onBTConnectButtonClicked(View v){
        if(btService.getDeviceState()){
            btService.enableBluetooth();
            TextView textView = (TextView)findViewById(R.id.textView);
            textView.setText("선택된 기종 : 연결중..");
        }else{
            finish();
        }
    }
}
