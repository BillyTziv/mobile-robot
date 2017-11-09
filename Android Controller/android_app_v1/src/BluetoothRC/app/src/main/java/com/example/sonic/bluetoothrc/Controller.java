package com.example.sonic.bluetoothrc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Controller extends Activity{
    private ViewGroup lrLayout, udLayout;
    private ImageView lrImage, udImage, udImageBackgound, lrImageBackground;
    private TextView forwardText, backwardText, leftText, rightText, connectedToText;
    private Button backButton, bBrakeButton, aBrakeButton;
    // Text font
    private Typeface customFont;
    private int finalPositionFlag=0;
    private int dx, dy, xStartPos, yStartPos, xCurrentPos = 0, yCurrentPos = 0;
    private String deviceName, upDownData, leftRightData;
    RelativeLayout.LayoutParams lrParms,udParms;
    private ConnectingTo t;

    private CustomToastFont myCustomToast = new CustomToastFont();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        getMacAddressAndStartConnection();
        // Setting the application to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Getting the left right relative layouts, image and background
        lrLayout = (RelativeLayout)findViewById(R.id.leftrightlayout);
        lrImage = (ImageView)findViewById(R.id.lr_image);
        lrImageBackground = (ImageView)findViewById(R.id.lrButtonPlace);
        //Getting up down relative layout, image and background
        udLayout = (RelativeLayout)findViewById(R.id.udlayout);
        udImage = (ImageView)findViewById(R.id.ud_image);
        udImageBackgound = (ImageView)findViewById(R.id.udButtonPlace);

        getTextViewsAndSetFonts();
        connectedToText.setText("Connected to:\n" + deviceName);
        checkConnection();

        // Back button listener
        backButton.setOnTouchListener(backButtonListener());
        // Setting listener to left right button
        lrImage.setOnTouchListener(leftRightTouchListener());
        // Setting listener to up down button
        udImage.setOnTouchListener(upDownTouchListener());
    }

    private void checkConnection(){
        // If connection cannot establishes than finish the controller
        if(t.connectionStatus() == 2){
            finish();
        }
    }

    private void setSpeedToZero(){
        xCurrentPos = 0;
        yCurrentPos = 0;
        t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        setSpeedToZero();
        try{
            t.getBluetoothSocket().close();
        }catch (IOException e) {}
    }

    public void beforeBrakeButtonPressed(View view){
        if(finalPositionFlag != 0) {
            bBrakeButton.setVisibility(View.INVISIBLE);
            aBrakeButton.setVisibility(View.VISIBLE);
            // Setting the up/down button to start position
            udParms.topMargin = yStartPos;
            udImage.setLayoutParams(udParms);
            forwardText.setVisibility(View.INVISIBLE);
            backwardText.setVisibility(View.INVISIBLE);
            setSpeedToZero();
            finalPositionFlag = 0;
        }
    }

    public void afterBrakeButtonPressed(View view){
        bBrakeButton.setVisibility(View.VISIBLE);
        aBrakeButton.setVisibility(View.INVISIBLE);
    }


    private View.OnTouchListener backButtonListener(){
        return new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        backButton.setVisibility(View.INVISIBLE);
                        ((Button)findViewById(R.id.backButtonAfterClick)).setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        backButton.setVisibility(View.VISIBLE);
                        ((Button)findViewById(R.id.backButtonAfterClick)).setVisibility(View.INVISIBLE);
                        // Try to close the bluetooth connection
                        try{
                            setSpeedToZero();
                            (t.getBluetoothSocket()).close();
                            Toast disconnected = Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT);
                            (myCustomToast).setCustomToastFont(disconnected, customFont);
                        }catch (IOException e) { }
                        finish();
                        break;
                }
                udLayout.invalidate();
                return true;
            }
        };
    }

    private void getMacAddressAndStartConnection(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String mac = ((Bundle)getIntent().getExtras()).getString("MAC");

        BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(mac);
        deviceName = bluetoothDevice.getName();

        t = new ConnectingTo(bluetoothDevice, mBluetoothAdapter, getApplicationContext());
        t.start();
        while(t.getConnectedTo() == null){
            // Wait here until the connection establishes
        }
    }

    // Getting textviews and setting my own font
    private void getTextViewsAndSetFonts(){
        customFont = Typeface.createFromAsset(getAssets(), "fonts/coolvetica.ttf");
        forwardText = (TextView)findViewById(R.id.udForwardText);
        forwardText.setTypeface(customFont);
        backwardText = (TextView)findViewById(R.id.udBackwardText);
        backwardText.setTypeface(customFont);
        leftText = (TextView)findViewById(R.id.lrLeftText);
        leftText.setTypeface(customFont);
        rightText = (TextView)findViewById(R.id.lrRightText);
        rightText.setTypeface(customFont);
        connectedToText = (TextView)findViewById(R.id.connectToText);
        connectedToText.setTypeface(customFont);
        backButton = (Button)findViewById(R.id.backButtonBeforeClick);
        bBrakeButton = (Button)findViewById(R.id.beforeBrakeButton);
        aBrakeButton = (Button)findViewById(R.id.afterBrakeButton);
        // All text invisible
        forwardText.setVisibility(View.INVISIBLE);
        backwardText.setVisibility(View.INVISIBLE);
        leftText.setVisibility(View.INVISIBLE);
        rightText.setVisibility(View.INVISIBLE);
    }

    private View.OnTouchListener leftRightTouchListener(){
        return new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        //This part of code will be executed only once when the button is touched
                        lrParms = (RelativeLayout.LayoutParams) lrImage.getLayoutParams();
                        dx = (int)(event.getRawX() - lrParms.leftMargin);
                        // Getting the start position of the button
                        xStartPos = (int)event.getRawX() - dx;
                        // xCurrentPos is the current position in X Axis
                        xCurrentPos = xStartPos;
                        break;
                    case MotionEvent.ACTION_UP:
                        // This part of code will be executed only once when the finger is removed from the button
                        // Set the button to the start position
                        lrParms.leftMargin = xStartPos;
                        lrImage.setLayoutParams(lrParms);
                        // When remove the button from the button set all the texts to invisible
                        leftText.setVisibility(View.INVISIBLE);
                        rightText.setVisibility(View.INVISIBLE);
                        // Set L to 0
                        t.getConnectedTo().write("0," + yCurrentPos + "\n");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // This part of code will be executed while the button is touched
                        // Checking if the current button position is bigger than background image width / 2
                        // e.g.: Current position: 230, background image width: 400.  So if 230 > (400/2)
                        if(Math.abs(xStartPos - (event.getRawX() - dx)) > ((lrImageBackground.getWidth()/ 2) - (lrImage.getWidth()/2))){
                            // If the above statement is true do not get new position.
                            // Checking if button pushed left or right
                            // If the button pushed left
                            // xCurrentPos will take the final position
                            xCurrentPos = (lrImageBackground.getWidth()/ 2) - (lrImage.getWidth()/2);
                            if((event.getRawX() - dx) < xStartPos){
                                // This is the final left position
                                lrParms.leftMargin = (xStartPos - ((lrImageBackground.getWidth()/ 2) - (lrImage.getWidth()/2)));
                                // Sending to bluetooth the final poslition
                                xCurrentPos = 0 - xCurrentPos;
                                t.getConnectedTo().write( xCurrentPos + "," + yCurrentPos + "\n");
                            }
                            // If the button pushed right
                            else{
                                // This is the final right position
                                lrParms.leftMargin = (xStartPos + ((lrImageBackground.getWidth()/ 2) - (lrImage.getWidth()/2)));
                                t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                            }
                        }else{
                            // Sending to bluetooth the current position
                            xCurrentPos = 0 - (int)(xStartPos - (event.getRawX() - dx));
                            if((event.getRawX() - dx) < xStartPos){
                                t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                            }else if((event.getRawX() - dx) == xStartPos){
                                xCurrentPos = 0;
                                t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                            }else{
                                t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                            }

                            // Getting new putton position
                            lrParms.leftMargin = (int)(event.getRawX() - dx);
                        }
                        // Set the right or left text to visible or invisible
                        if((event.getRawX() - dx) < xStartPos){
                            rightText.setVisibility(View.INVISIBLE);
                            leftText.setVisibility(View.VISIBLE);
                        }else if((event.getRawX() - dx) == xStartPos){
                            leftText.setVisibility(View.INVISIBLE);
                            rightText.setVisibility(View.INVISIBLE);
                        }else{
                            leftText.setVisibility(View.INVISIBLE);
                            rightText.setVisibility(View.VISIBLE);
                        }
                        // Setting the new positions
                        lrImage.setLayoutParams(lrParms);
                        break;
                }
                // ReDraw the relative layout with the new button positions
                lrLayout.invalidate();
                return true;
            }
        };
    }

    private View.OnTouchListener upDownTouchListener(){
        return new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        udParms = (RelativeLayout.LayoutParams) udImage.getLayoutParams();
                        if(finalPositionFlag == 0) {
                            dy = (int) (event.getRawY() - udParms.topMargin);
                            yStartPos = (int) event.getRawY() - dy;
                            // yCurrentPos is the current position in Y Axis
                            yCurrentPos = yStartPos;
                            finalPositionFlag = 1;

                            bBrakeButton.setVisibility(View.VISIBLE);
                            aBrakeButton.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //udParms.topMargin = yStartPos;
                        //udImage.setLayoutParams(udParms);
                        //forwardText.setVisibility(View.INVISIBLE);
                        //backwardText.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(Math.abs(yStartPos - (event.getRawY() - dy)) > ((udImageBackgound.getHeight()/ 2) - (udImage.getHeight()/2))){
                            yCurrentPos = (udImageBackgound.getHeight() / 2) - (udImage.getHeight() / 2);
                            if((event.getRawY() - dy) < yStartPos){
                                udParms.topMargin = (yStartPos - ((udImageBackgound.getHeight()/ 2) - (udImage.getHeight()/2)));
                                t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                            }
                            else{
                                udParms.topMargin = (yStartPos + ((udImageBackgound.getHeight()/ 2) - (udImage.getHeight()/2)));
                                yCurrentPos = -yCurrentPos;
                                t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                            }
                        }else{
                            yCurrentPos = (int)(yStartPos - (event.getRawY() - dy));
                            if(event.getRawY() - dy < yStartPos){
                                t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                            }else if(event.getRawY() - dy == yStartPos){
                                yCurrentPos = 0;
                                t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                            }else{
                                xCurrentPos = -xCurrentPos;
                                t.getConnectedTo().write(xCurrentPos + "," + yCurrentPos + "\n");
                            }

                            udParms.topMargin = (int)(event.getRawY() - dy);
                        }
                        if(event.getRawY() - dy < yStartPos){
                            backwardText.setVisibility(View.INVISIBLE);
                            forwardText.setVisibility(View.VISIBLE);
                        }else if(event.getRawY() - dy == yStartPos){
                            forwardText.setVisibility(View.INVISIBLE);
                            backwardText.setVisibility(View.INVISIBLE);
                        }else{
                            forwardText.setVisibility(View.INVISIBLE);
                            backwardText.setVisibility(View.VISIBLE);
                        }
                        udImage.setLayoutParams(udParms);
                        break;
                }
                udLayout.invalidate();
                return true;
            }
        };
    }
}

