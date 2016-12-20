package com.sample.hrv;

import android.app.IntentService;
import android.app.Service;
import android.os.CountDownTimer;
import android.os.HandlerThread;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;
import android.app.ListActivity;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.sample.hrv.sensor.BleHeartRateSensor;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.TCPConnectionHandler;
import net.wimpi.modbus.net.TCPSlaveConnection;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.ModbusCoupler;

import static android.R.attr.action;
import static android.R.attr.delay;
import static android.content.ContentValues.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ModbusService extends Service {


    /*public ModbusService() {
        super("ModbusService");
    }*/
    private BleService bleService;
    private static final String TAG = "ModbusService";
    private boolean isRunning = false;
    private String deviceAddress;
    int heartValue;
    int old_heartValue;
    int tmp_heartValue;
    int lightValue;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStartCommand");


       new Thread(){

            public void run() {
                workRoutine();
            }

       }.start();

        return Service.START_STICKY;
    }

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    public void onDestroy(){

        unregisterReceiver(gattUpdateReceiver);
        isRunning = false;
        Log.i(TAG, "Service onDestroy");

    }

    public void onCreate() {

        Log.i(TAG, "Service onCreate");
        isRunning = true;

        //Connection to BleService
        registerReceiver(gattUpdateReceiver,makeGattUpdateIntentFilter());

    }

    public void workRoutine() {
        while(true){
            tmp_heartValue = heartValue;

            if (tmp_heartValue != old_heartValue) {


                if (tmp_heartValue >= 100 && tmp_heartValue < 200) {
                    //   GREEN
                    lightValue = 4;
                } else if (tmp_heartValue < 100 && tmp_heartValue > 0) {
                    //   RED
                    lightValue = 2;
                } else {
                    //   WHITE
                    lightValue = 1;
                }
////////////////////////////////////WIEDER REINNEHMEN/////////////////////////////
            }
            SimpleProcessImage spi = null;
            spi = new SimpleProcessImage();
            spi.addRegister(new SimpleRegister(lightValue));
            ModbusCoupler.getReference().setProcessImage(spi);
            old_heartValue = tmp_heartValue;

            if (isRunning) {
               // Log.i(TAG, "Service running");
            }


        }
    }


//////////////////////////////////////////////////////ALTER TEIL////////////////////////////////
/*
    public void onCreate(){
        super.onCreate();
        registerReceiver(gattUpdateReceiver,makeGattUpdateIntentFilter());
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        final Intent gattServiceIntent = new Intent(this, BleService.class);
        bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);

       // registerReceiver(gattUpdateReceiver,makeGattUpdateIntentFilter());

      */
/*  android.os.Debug.waitForDebugger();

        if (intent != null) {

            tmp_heartValue = heartValue;

            if (tmp_heartValue != old_heartValue) {

                int lightValue;
                if (tmp_heartValue >= 100 && tmp_heartValue < 200) {
                    //   GREEN
                    lightValue = 4;
                } else if (tmp_heartValue < 100 && tmp_heartValue > 0) {
                    //   RED
                    lightValue = 2;
                } else {
                    //   WHITE
                    lightValue = 1;
                }
////////////////////////////////////WIEDER REINNEHMEN/////////////////////////////

                SimpleProcessImage spi = null;
                spi = new SimpleProcessImage();
                spi.addRegister(new SimpleRegister(lightValue));
                ModbusCoupler.getReference().setProcessImage(spi);
                old_heartValue = tmp_heartValue;

////////////////////////////////////WIEDER REINNEHMEN/////////////////////////////
            }
        }
    }





    public void onDestroy() {
        unregisterReceiver(gattUpdateReceiver);
    }



*/

    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BleService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BleService.EXTRA_SERVICE_UUID), intent.getStringExtra(BleService.EXTRA_TEXT));

            }

        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BleService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    public void displayData(String uuid, String data) {
        if (data != null) {
            if (uuid.equals(BleHeartRateSensor.getServiceUUIDString())) {

                heartValue = BLEStringToInt(data) ;
            } else {

                heartValue = BLEStringToInt(data);
            }
        }
    }

    public int BLEStringToInt(String str){

        return Integer.parseInt(str.replaceAll("\\D+","")) / 10;

    }
}


