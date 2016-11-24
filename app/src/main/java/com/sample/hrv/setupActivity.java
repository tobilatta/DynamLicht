package com.sample.hrv;

        import android.app.Activity;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import com.sample.hrv.DeviceScanActivity;
        import com.sample.hrv.adapters.BleDevicesAdapter;
        import com.sample.hrv.sensor.BleHeartRateSensor;
        import com.sample.hrv.DeviceServicesActivity;
        import static android.R.attr.data;

        import static com.sample.hrv.BleService.EXTRA_DATA;
//        import static com.sample.hrv.DeviceServicesActivity.HEARTRATEV;
        import static com.sample.hrv.DeviceServicesActivity.HEARTRATEV;
        import static com.sample.hrv.R.id.heartrate_value;
        import static com.sample.hrv.R.id.uuid;
        import static com.sample.hrv.R.string.no_data;


public class setupActivity extends /*ActionBar*/Activity {

    public TextView heartRateField;
    public TextView dataField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
       // getSupportActionBar().setTitle(R.string.title_devices);
        setContentView(R.layout.activity_setup);

        dataField = (TextView) findViewById(R.id.data_value);


        //displayData(intent.getStringExtra(BleService.EXTRA_TEXT));

/*
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newHR= null;
            } else {
                newHR= extras.getString("STRING_I_NEED");
            }
        } else {
            newHR= (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }

*/
        //Declaration of BLESetup-Call with buttons and activity switch
        final Button blesetup = (Button) findViewById(R.id.but_menu_ble);
        blesetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(setupActivity.this, DeviceScanActivity.class);
                startActivity(intent);
            }
        } );


    }

    /*
    public void displayData(String uuid, String data) {
        if (data != null) {
            if (uuid.equals(BleHeartRateSensor.getServiceUUIDString())) {
                heartRateField.setText(data);
            }
        }
    }
*/
    protected void onResume() {
        super.onResume();

        heartRateField = (TextView) findViewById(R.id.heartrate_value);


    }

    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
/*            if (BleService.ACTION_GATT_CONNECTED.equals(action)) {
                isConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                isConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(bleService.getSupportedGattServices());
                enableHeartRateSensor();
            } else*/
            if (BleService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BleService.EXTRA_SERVICE_UUID), intent.getStringExtra(BleService.EXTRA_TEXT));
//                HEARTRATEV = intent.getStringExtra(BleService.EXTRA_TEXT);

            }
        }
    };

    public void displayData(String uuid, String data) {
        if (data != null) {
            if (uuid.equals(BleHeartRateSensor.getServiceUUIDString())) {
                heartRateField.setText(data);
//                HEARTRATEV = data;
            } else {
                dataField.setText(data);
            }
        }
    }


}

