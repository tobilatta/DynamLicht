package com.sample.hrv;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.TCPConnectionHandler;
import net.wimpi.modbus.net.TCPSlaveConnection;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.ModbusCoupler;


public class setupModbus extends Activity {


    @Override
    //Mainentry
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_modbus);

        //Modbus initialization
        ModbusTCPListener listener = null ;
        SimpleProcessImage spi = null;
        int port = Modbus.DEFAULT_PORT;
        byte inRegLowByte = 1;
        byte inRegHighByte = 0;

        try {


            //prepare process image
            spi = new SimpleProcessImage();
           /* spi.addDigitalOut(new SimpleDigitalOut(true));

*/
            spi.addRegister(new SimpleRegister(0));
            spi.addRegister(new SimpleRegister(1));
            spi.addRegister(new SimpleRegister(0));
           // spi.addInputRegister(new SimpleInputRegister(45));

            //set image on coupler
            ModbusCoupler.getReference().setProcessImage(spi);
            ModbusCoupler.getReference().setMaster(false);
            ModbusCoupler.getReference().setUnitID(0);

            //3. create a listener with 3 threads in pool
            /*if (Modbus.debug) */System.out.println("Listening...");
            listener = new ModbusTCPListener(3);
            listener.setPort(port);
            listener.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }







}
