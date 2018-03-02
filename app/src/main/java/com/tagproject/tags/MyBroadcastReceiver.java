package com.tagproject.tags;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 * Created by 이나영 on 2017-09-05.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    BluetoothService btService = MainActivity.btService;
    private static String mLastState;
    private static final int STATE_CONNECTED = 3;
    FirstActivity t;
    BluetoothAdapter btAdapter = btService.getbtAdapter();

    @Override
    public void onReceive(Context context, Intent intent) {
        t=((MyApplication) context.getApplicationContext()).firstActivity;
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        boolean smsState = t.getsmsState();
        boolean callState = t.getcallState();
        if(btService.getState()==STATE_CONNECTED && btAdapter.isEnabled()){//bt연결되었을때에만 실행
            if(smsState){
                t.setSensorState(false);
                if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                    String sendSMS = "#!&";
                    byte[] getText = sendSMS.getBytes();
                    btService.write(getText);
                    //Toast.makeText(context, "문자수신", Toast.LENGTH_LONG).show();
                }
                t.setSensorState(true);
            }

            if(callState){
//                if (state.equals(mLastState)) { return; }
//                else { mLastState = state; }
                t.setSensorState(false);
                if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                    String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    String outPut = "#@"+incomingNumber+"&";
                    byte[] getText = outPut.getBytes();
                    btService.write(getText);
                }
                t.setSensorState(true);
            }

        }

    }
}
