package com.example.sonic.bluetoothrc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ConnectingTo extends Thread{
    private final static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final BluetoothAdapter mBluetoothAdapter;
    private int connectionStatusFlag = 0;
    private Controller myController;
    private CustomToastFont myCustomToast = new CustomToastFont();
    private Context appContext;
    private Typeface customFont;
    private ConnectedTo connected;

    public ConnectingTo(BluetoothDevice device, BluetoothAdapter adapter, Context context) {

        BluetoothSocket temp = null;
        bluetoothDevice = device;
        mBluetoothAdapter = adapter;
        customFont = Typeface.createFromAsset(context.getAssets(), "fonts/coolvetica.ttf");
        appContext = context;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            temp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothSocket = temp;
    }

    public BluetoothSocket getBluetoothSocket(){
        return bluetoothSocket;
    }

    public void run() {
        // Cancel discovery as it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // This will block until it succeeds in connecting to the device
            // through the bluetoothSocket or throws an exception
            bluetoothSocket.connect();
            connectionStatusFlag = 1;
        } catch (IOException connectException) {
            connectionStatusFlag = 2;
            connectException.printStackTrace();
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
        }

        connected = new ConnectedTo(bluetoothSocket);
        connected.start();
    }

    public int connectionStatus(){
        while(true)
        {
            if(connectionStatusFlag == 1){
                Toast connected = Toast.makeText(appContext, "Connected", Toast.LENGTH_SHORT);
                (myCustomToast).setCustomToastFont(connected, customFont);
                return connectionStatusFlag;
            } else if(connectionStatusFlag == 2){
                Toast noConnect = Toast.makeText(appContext, "Can not connect to selected device", Toast.LENGTH_SHORT);
                (myCustomToast).setCustomToastFont(noConnect,customFont);
                return connectionStatusFlag;
            }
        }
    }

    // Cancel an open connection and terminate the thread
    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConnectedTo getConnectedTo(){
        return connected;
    }
}
