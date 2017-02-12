package com.sample.hrv;

import android.app.Activity;
import android.app.ListActivity;
import android.os.CountDownTimer;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.TCPConnectionHandler;
import net.wimpi.modbus.net.TCPSlaveConnection;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.ModbusCoupler;

import com.sample.hrv.DeviceScanActivity;
import com.sample.hrv.adapters.BleDevicesAdapter;
import com.sample.hrv.sensor.BleHeartRateSensor;
import com.sample.hrv.DeviceServicesActivity;
import static android.R.attr.data;

import static java.util.logging.Logger.global;


public class setupModbus extends ListActivity {


    @Override
    //Mainentry
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_modbus);
        getActionBar().setTitle(R.string.title_devices);
        getActionBar().setDisplayHomeAsUpEnabled (true);

        //Modbus initialization
        ModbusTCPListener listener = null ;
        SimpleProcessImage spi = null;
        //COM port is set ( needs to be unlocked on network router)
        //can be different from typical Modbus port 502
        int port = Modbus.DEFAULT_PORT;


        try {


            //prepare initial process image for DALI reset
            spi = new SimpleProcessImage();
            spi.addRegister(new SimpleRegister(15));

            //set image on coupler
            ModbusCoupler.getReference().setProcessImage(spi);
            ModbusCoupler.getReference().setMaster(false);
            ModbusCoupler.getReference().setUnitID(0);

            //3. create a listener with 3 threads in pool
            /*if (Modbus.debug) */System.out.println("Listening...");
            listener = new ModbusTCPListener(3);
            listener.setPort(port);
            listener.start();

            //after 500ms a new process Image is sended which contains only 0 (meaning deactivation
            //of earlier image)
            new CountDownTimer(500, 100) {
                public void onFinish() {

                    SimpleProcessImage spi = null;
                    spi = new SimpleProcessImage();
                    spi.addRegister(new SimpleRegister(0));
                    ModbusCoupler.getReference().setProcessImage(spi);

                }
                public void onTick(long millisUntilFinished) {

                }
            }.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
