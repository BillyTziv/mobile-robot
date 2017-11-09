package com.example.sonic.bluetoothrc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends Activity{
    protected BluetoothAdapter btAdapter;
    private TextView notActivatedText;
    private TextView scanText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textFonts();
        // Check if the bluetooth is enable
        if(btAdapter.getDefaultAdapter().isEnabled()){
            setTextToInvisible();
        }
    }

    // Setting custom text font
    private void textFonts(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/coolvetica.ttf");
        scanText = (TextView)findViewById(R.id.scanButton);
        scanText.setTypeface(font);
        notActivatedText = (TextView) findViewById(R.id.bluetoothStatus);
        notActivatedText.setTypeface(font);
    }

    // Set the Bluetooth situation text invisible
    public void setTextToInvisible(){
        notActivatedText.setVisibility(View.INVISIBLE);
    }

    // Set the Bluetooth situation text visible
    public void setTextToVisible() {
        notActivatedText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!btAdapter.getDefaultAdapter().isEnabled()){ setTextToVisible(); }
        else { setTextToInvisible(); }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //btAdapter.getDefaultAdapter().disable();
    }

    public void getBluetoothActivity(View view){
        Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
        startActivity(intent);
    }
}