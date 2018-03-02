package com.tagproject.tags;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Switch;

public class FirstActivity extends AppCompatActivity implements SensorEventListener {

    BluetoothService btService = MainActivity.btService;
    private static final String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final String EXTRA_DEVICE_NAME = "device_name";

    private static final int REQUEST_STORAGE = 1;

    Bundle extras = new Bundle();
    Intent intent = null;

    Switch smsSwich;
    Switch callSwich;

//    private boolean smsState = true;
//    private boolean callState = true;

    SensorManager mSensorManager;
    Sensor mAccelerometer;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //broadcast
//        mReceiver = new MyBroadcastReceiver();
//        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//        filter.addAction(TelephonyManager.EXTRA_STATE_RINGING);
//        registerReceiver(mReceiver, filter);
        MyApplication myApplication = (MyApplication) this.getApplicationContext();
        myApplication.firstActivity = this;

        //센서 사용
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //인텐트
        extras = getIntent().getExtras();
        if(extras ==null){
            return;
        }
        String device_address = extras.getString(EXTRA_DEVICE_ADDRESS);
        String device_name = extras.getString(EXTRA_DEVICE_NAME);
        Log.d("FirstActivity", device_address);

        smsSwich = (Switch)findViewById(R.id.smsSwitch);
        callSwich = (Switch)findViewById(R.id.callSwitch);
//        smsState = smsSwich.isChecked();
//        callState = callSwich.isChecked();

        //권한 설정
        if(PermissionUtil.checkPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                (PermissionUtil.checkPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE))){
        }else {
            PermissionUtil.requestExternalPermissions(this);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction(TelephonyManager.EXTRA_STATE_RINGING);
        registerReceiver(new MyBroadcastReceiver(), filter);
    }
//    @Override
//    protected void onPause(){
//        super.onPause();
//        unregisterReceiver(mReceiver);
//    }

    public boolean getsmsState(){return  smsSwich.isChecked();}
    public boolean getcallState(){return callSwich.isChecked();}

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if(requestCode==REQUEST_STORAGE){
            if(PermissionUtil.verifyPermission(grantResults)){
                //권한yes
            }else {
//                showRequestAgainDialog();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    //sensor control
    @Override
    public void onStart() {
        super.onStart();
        if (mAccelerometer != null)
            mSensorManager.registerListener(this, mAccelerometer,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    private static final int SHAKE_THRESHOLD = 700;
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private long beforeT = 0;

    private boolean sensorState = true;
    public void setSensorState(boolean state){ this.sensorState = state; }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() ==Sensor.TYPE_ACCELEROMETER && sensorState){
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if(gabOfTime > 100){
                lastTime = currentTime;
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                speed = Math.abs(axisX + axisY + axisZ - lastX - lastY - lastZ) / gabOfTime * 10000;

                if(speed > SHAKE_THRESHOLD){
                    long gabOfTime2 = (currentTime - beforeT);
                    if(gabOfTime2 >500){
                        beforeT = currentTime;
                        if(btService.getState()==3){
                            //bluetooth send message
                            String sendData = "#^1&";
                            byte[] getText = sendData.getBytes();
                            btService.write(getText);
                        }//bt가 연결되었을 때만
                    }
                }else{
                    long gabOfTime2 = (currentTime - beforeT);
                    if(gabOfTime2 >5 * 1000){
                        beforeT = currentTime;
                        if(btService.getState()==3){
                            //bluetooth send message
                            String sendData = "#^0&";
                            byte[] getText = sendData.getBytes();
                            btService.write(getText);
                        }//bt가 연결되었을 때만
                    }
                }
                lastX = axisX;
                lastY = axisY;
                lastZ = axisZ;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
