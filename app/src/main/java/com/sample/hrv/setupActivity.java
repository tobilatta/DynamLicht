package com.sample.hrv;

        import android.app.Activity;
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
        import static com.sample.hrv.R.id.heartrate_value;
        import static com.sample.hrv.R.id.uuid;
        import static com.sample.hrv.R.string.no_data;


public class setupActivity extends /*ActionBar*/Activity {

    public TextView heartRateField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
       // getSupportActionBar().setTitle(R.string.title_devices);
        setContentView(R.layout.activity_setup);


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

        heartRateField.setText(com.sample.hrv.DeviceServicesActivity.HEARTRATEV);

    }

    public void displayData(String data) {
        if (data != null) {

                heartRateField.setText(data);

        }
        else {
                heartRateField.setText(no_data);
        }
    }
}

