package com.example.sonic.bluetoothrc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AccelerometerController extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int xCurrentPos=0, yCurrentPos=0;
    private TextView accelerometerText, plusX, minusX, plusY, minusY, connectedToText;
    private Typeface customFont;
    private ConnectingTo connectingTo;
    private CustomToastFont myCustomToast = new CustomToastFont();
    private String deviceName;
    private Button backButtonBeforeClick, backButtonAfterClick;
    private Button beforeBrakePressed, afterBrakePressed;
    private int brakeFlag=0;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_accelerator_controller);

        getMacAddressAndStartConnection();

        initializeViews();
        setCustomFont();
        checkConnection();
        connectedToText.setText("Connected to:\n" + deviceName);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        //Set listener to back button
        backButtonBeforeClick.setOnTouchListener(backButtonListener());
    }

    private void checkConnection(){
        // If connection cannot establishes than finish the controller
        if(connectingTo.connectionStatus() == 2){
            finish();
        }
    }

    private void getMacAddressAndStartConnection(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String mac = ((Bundle)getIntent().getExtras()).getString("MAC");

        BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(mac);
        deviceName = bluetoothDevice.getName();

        connectingTo = new ConnectingTo(bluetoothDevice, mBluetoothAdapter, getApplicationContext());
        connectingTo.start();
        while(connectingTo.getConnectedTo() == null){
            // Stay here until the connection establishes
            // Doing this will not have null pointer exception
        }
    }

    public void initializeViews(){
        plusX = (TextView)findViewById(R.id.plusX);
        minusX = (TextView)findViewById(R.id.minusX);
        plusY = (TextView)findViewById(R.id.plusY);
        minusY = (TextView)findViewById(R.id.minusY);
        accelerometerText = (TextView)findViewById(R.id.accelerometer);
        connectedToText = (TextView)findViewById(R.id.accelerometerConnectedToText);
        backButtonBeforeClick = (Button)findViewById(R.id.accelerometerBackButtonBeforeClick);
        backButtonAfterClick = (Button)findViewById(R.id.accelerometerBackButtonAfterClick);
        beforeBrakePressed = (Button)findViewById(R.id.handBrakeBeforePressed);
        afterBrakePressed = (Button)findViewById(R.id.handBrakeAfterPressed);
        //get the custom font
        customFont = Typeface.createFromAsset(getAssets(), "fonts/coolvetica.ttf");
    }

    public void setCustomFont(){
        plusX.setTypeface(customFont);
        minusX.setTypeface(customFont);
        plusY.setTypeface(customFont);
        minusY.setTypeface(customFont);
        accelerometerText.setTypeface(customFont);
        connectedToText.setTypeface(customFont);
    }

    public void sendZeroCoordinates(){
        xCurrentPos = 0;
        yCurrentPos = 0;
        connectingTo.getConnectedTo().write(xCurrentPos + "," + yCurrentPos +"\n");
    }

    public void setCoordinateTextsToZero(){
        plusY.setText("0");
        plusX.setText("0");
        minusX.setText("0");
        minusY.setText("0");
    }

    private View.OnTouchListener backButtonListener(){
        return new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        backButtonBeforeClick.setVisibility(View.INVISIBLE);
                        backButtonAfterClick.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        backButtonBeforeClick.setVisibility(View.VISIBLE);
                        backButtonAfterClick.setVisibility(View.INVISIBLE);
                        // Try to close the bluetooth connection
                        try{
                            sendZeroCoordinates();
                            (connectingTo.getBluetoothSocket()).close();
                            Toast disconnected = Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT);
                            (myCustomToast).setCustomToastFont(disconnected, customFont);
                        }catch (IOException e) { }
                        finish();
                        break;
                }
                return true;
            }
        };
    }

    // when the warning button pressed this method will be called
    public void brakeButtonPressed(View view){
        brakeFlag = 1;
        sendZeroCoordinates();
        setCoordinateTextsToZero();
        beforeBrakePressed.setVisibility(View.INVISIBLE);
        afterBrakePressed.setVisibility(View.VISIBLE);
    }

    public void brakeButtonUnpressed(View view){
        brakeFlag = 0;
        beforeBrakePressed.setVisibility(View.VISIBLE);
        afterBrakePressed.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        sendZeroCoordinates();
        try{
            connectingTo.getBluetoothSocket().close();
        }catch (IOException e){}
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        displayCurrentValues();
        // putting the 0 - to reverse the plus and minus in axis Y
        yCurrentPos = (int)(0 -  (event.values[0]*40));
        xCurrentPos =  (int)(event.values[1]*40);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void displayCurrentValues(){
        if(brakeFlag != 1) {
            if (xCurrentPos > 0) {
                plusX.setText(Integer.toString(xCurrentPos));
                connectingTo.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                minusX.setText("0");
            }
            if (xCurrentPos < 0) {
                minusX.setText(Integer.toString(xCurrentPos));
                //xCurrentPos = Math.abs(xCurrentPos);
                connectingTo.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                plusX.setText("0");
            }
            if (yCurrentPos > 0) {
                plusY.setText(Integer.toString(yCurrentPos));
                connectingTo.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                minusY.setText("0");
            }
            if (yCurrentPos < 0) {
                minusY.setText(Integer.toString(yCurrentPos));
                //yCurrentPos = Math.abs(yCurrentPos);
                connectingTo.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                plusY.setText("0");
            }
        }
    }
}
