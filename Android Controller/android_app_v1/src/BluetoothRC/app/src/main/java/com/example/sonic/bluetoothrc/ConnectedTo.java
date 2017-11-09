package com.example.sonic.bluetoothrc;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedTo extends Thread {
    private final BluetoothSocket blueSocket;
    private final InputStream blueInStream;
    private final OutputStream blueOutStream;
    private static final String TAG = "ConnectedThread";
    private static final boolean D = true;

    public ConnectedTo(BluetoothSocket socket){
        blueSocket = socket;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try{
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e){  }

        blueInStream = tmpIn;
        blueOutStream = tmpOut;
    }

    public void run(){
        byte[] buffer = new byte[1024]; //buffer store for the stream
        int bytes; // bytes returned by read
        try {
            while ((bytes = blueInStream.read(buffer)) != -1) {
                blueOutStream.write(bytes);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String msg) {
        byte[] msgBuffer = msg.getBytes();

        try {
            blueOutStream.write(msgBuffer);
        } catch (IOException e) { }
        try{
            blueOutStream.flush();
        }catch (IOException e){}
    }

    // Call this from the main activity to shutdown the connection
    public void cancel() {
        try {
            blueSocket.close();
        } catch (IOException e) { }
    }
}
