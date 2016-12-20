package com.sample.hrv;

        import android.app.Activity;
        import android.content.BroadcastReceiver;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.ServiceConnection;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.IBinder;
        import android.support.v7.app.ActionBarActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import com.sample.hrv.DeviceScanActivity;
        import com.sample.hrv.adapters.BleDevicesAdapter;
        import com.sample.hrv.sensor.BleHeartRateSensor;
        import com.sample.hrv.DeviceServicesActivity;
        import static android.R.attr.data;

        import static android.content.ContentValues.TAG;
        import static com.sample.hrv.BleService.EXTRA_DATA;
        import static com.sample.hrv.R.id.heartrate_value;
        import static com.sample.hrv.R.id.uuid;
        import static com.sample.hrv.R.string.no_data;

        import net.wimpi.modbus.Modbus;
        import net.wimpi.modbus.net.TCPConnectionHandler;
        import net.wimpi.modbus.net.TCPSlaveConnection;
        import net.wimpi.modbus.net.ModbusTCPListener;
        import net.wimpi.modbus.procimg.*;
        import net.wimpi.modbus.ModbusCoupler;


public class setupActivity extends /*ActionBar*/Activity {

    public TextView heartRateField;
    public TextView dataField;
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private String deviceAddress;
    private BleService bleService;
    private ModbusService modbusService;
    private String hrTransfer;
    public int heartValue;
    public int lightValue;

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bleService = ((BleService.LocalBinder) service).getService();
            if (!bleService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            bleService.connect(deviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bleService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       // getSupportActionBar().setTitle(R.string.title_devices);

        final Intent intent = getIntent();
       // final Intent modbusServiceIntent = getIntent();
        setContentView(R.layout.activity_setup);


        deviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        dataField = (TextView) findViewById(R.id.data_value);


        final Intent gattServiceIntent = new Intent(this, BleService.class);
        bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);


        //Declaration of BLESetup-Call with buttons and activity switch
        final Button blesetup = (Button) findViewById(R.id.but_menu_ble);
        blesetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(setupActivity.this, DeviceScanActivity.class);
                startActivity(intent);
            }
        } );

        //Opening the modbussetup-Activity when clicking on the Modbus-Button
        final Button modbussetup = (Button) findViewById(R.id.but_menu_modbus);
        modbussetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(setupActivity.this, setupModbus.class);
                startActivity(intent);
            }
        }
        );

        //Start Button
        final Button startBut = (Button) findViewById(R.id.but_menu_Start);
        startBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(setupActivity.this, ModbusService.class);
                startService(intent);

                }

        }
        );

        final Button nightModeOn = (Button) findViewById(R.id.but_menu_nightOn);
        nightModeOn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {

                                               SimpleProcessImage spi = null;
                                               spi = new SimpleProcessImage();
                                               spi.addRegister(new SimpleRegister(256));
                                               ModbusCoupler.getReference().setProcessImage(spi);


                                           }
                                       }
        );

        final Button nightModeOff = (Button) findViewById(R.id.but_menu_nightOff);
        nightModeOff.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {

                                               SimpleProcessImage spi = null;
                                               spi = new SimpleProcessImage();
                                               spi.addRegister(new SimpleRegister(512));
                                               ModbusCoupler.getReference().setProcessImage(spi);


                                           }
                                       }
        );



    }

    protected void onResume() {
        super.onResume();

        heartRateField = (TextView) findViewById(R.id.heartrate_value);
       ////////////////////////////////////FÜR BETRIEB WIEDER REINNEHMEN///////////////////////////////////////
        registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
        //final Intent intent = new Intent(this, ModbusService.class);
        //this.startService(intent);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////








    }

    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
          //  final String cleared;

            if (BleService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BleService.EXTRA_SERVICE_UUID), intent.getStringExtra(BleService.EXTRA_TEXT));

            }
        }
    };

    public void displayData(String uuid, String data) {
        if (data != null) {
            if (uuid.equals(BleHeartRateSensor.getServiceUUIDString())) {
                heartRateField.setText(data);
                heartValue = BLEStringToInt(data);
            } else {
                dataField.setText(data);
                heartValue = BLEStringToInt(data);
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BleService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    protected void onPause() {
        super.onPause();
      //////////////////////777WIEDER REINNEHMEN/////////////////////////////////
        unregisterReceiver(gattUpdateReceiver);
        /////////////////////////////////////////////////////////////////////////
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        bleService = null;
    }


    public int BLEStringToInt(String str){

        return Integer.parseInt(str.replaceAll("\\D+","")) / 10;

    }

}



