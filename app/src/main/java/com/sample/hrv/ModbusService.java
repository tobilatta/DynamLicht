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

    //equivalent to onCreate method but for services.  workroutine is started and the
    //service is declared as "STICKY" which means:
    //when phone is down on system resources, it shuts down unnecessary applications and services
    //After this service gets shut down and resource are available again, "STICKY" tells the OS to restart
    //the service
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

    //when application gets destroyed, all bits are set to 0
    public void onDestroy(){
        isRunning = false;

        SimpleProcessImage spi = null;
        spi = new SimpleProcessImage();
        spi.addRegister(new SimpleRegister(0));
        ModbusCoupler.getReference().setProcessImage(spi);

        //the ble receiver is shut down
        unregisterReceiver(gattUpdateReceiver);

        Log.i(TAG, "Service onDestroy");

    }

    public void onCreate() {

        Log.i(TAG, "Service onCreate");
        isRunning = true;

        //Connection to BleService
        registerReceiver(gattUpdateReceiver,makeGattUpdateIntentFilter());

    }

    public void workRoutine() {
        while(isRunning){

            //saving current heartValue for explicit work with one value

            tmp_heartValue = heartValue;

            //and unnecessary rework,if the value stays the same
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

            }
            //creating ProcessImage, that can receive/transmit single or multiple bits/values
            SimpleProcessImage spi = null;
            spi = new SimpleProcessImage();
            //here a register needs to be sended, so the fitting method is called
            spi.addRegister(new SimpleRegister(lightValue));
            //via ModbusCoupler-class the ProcessImage is set and sended
            ModbusCoupler.getReference().setProcessImage(spi);
            //current heartValue is switched to old value
            old_heartValue = tmp_heartValue;

            if (isRunning) {
               Log.i(TAG, "Service running");
            }


        }
    }




    //broadcast receiver for bluetooth data that is spread from BleService.class
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


    //handling the received data and transforming it from String to integer value
    public void displayData(String uuid, String data) {
        if (data != null) {
            if (uuid.equals(BleHeartRateSensor.getServiceUUIDString())) {

                heartValue = BLEStringToInt(data) ;
            } else {

                heartValue = BLEStringToInt(data);
            }
        }
    }

    //explicit method is need for correct transformation of string to integer value
    public int BLEStringToInt(String str){

        //from the string all dots are replaced with "", meaning they get removed
        //value would start from 123.0 bpm to 1230 bpm (still string)
        //then it gets parsed to Int and devided by 10 to receiver 123 bpm
        return Integer.parseInt(str.replaceAll("\\D+","")) / 10;

    }
}


