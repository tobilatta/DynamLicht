package com.sample.hrv;

import android.app.IntentService;
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
import static android.content.ContentValues.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ModbusService extends IntentService {


    public ModbusService() {
        super("ModbusService");
    }
    private BleService bleService;
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private String deviceAddress;
    int heartValue;

/*
    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bleService = ((BleService.LocalBinder) service).getService();
            if (!bleService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                }
            // Automatically connects to the device upon successful start-up initialization.
            bleService.connect(deviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bleService = null;
        }
    };
*/
    public void onCreate(){
        super.onCreate();
        registerReceiver(gattUpdateReceiver,makeGattUpdateIntentFilter());
    }

    @Override
    protected void onHandleIntent(Intent intent) {


/*        final Intent gattServiceIntent = new Intent(this, BleService.class);
        bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);*/

       // registerReceiver(gattUpdateReceiver,makeGattUpdateIntentFilter());

        if (intent != null) {
          //  final String action = intent.getAction();


            int lightValue;
            if(heartValue >= 100 && heartValue < 200){
             //   lightValue = R.integer.GREEN;
                lightValue = 4;
            }
            else if(heartValue < 100 && heartValue > 0){
             //   lightValue = R.integer.RED;
                lightValue = 2;
            }

            else{
             //   lightValue = R.integer.WHITE;
                lightValue = 1;
            }

            SimpleProcessImage spi = null;
            spi = new SimpleProcessImage();
            spi.addRegister(new SimpleRegister(lightValue));
            ModbusCoupler.getReference().setProcessImage(spi);



        }
    }

    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BleService.ACTION_DATA_AVAILABLE.equals(action)) {

                heartValue = Integer.valueOf(intent.getStringExtra(BleService.EXTRA_TEXT));

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

    public void onDestroy() {
        unregisterReceiver(gattUpdateReceiver);
    }
}
