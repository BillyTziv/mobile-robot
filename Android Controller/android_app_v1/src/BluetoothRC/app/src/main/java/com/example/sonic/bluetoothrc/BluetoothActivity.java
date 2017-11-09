package com.example.sonic.bluetoothrc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class BluetoothActivity extends Activity {
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2;
    private static final int DISCOVERABLE_DURATION = 300;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter mArrayAdapter ;
    private ListView lstView;
    private Typeface customFont;
    private ProgressDialog connectingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        // Getting the custom text font
        customFont = Typeface.createFromAsset(getAssets(), "fonts/coolvetica.ttf");
        // Set custom font to available devices text
        ((TextView)findViewById(R.id.availableDevicesText)).setTypeface(customFont);
        // Creating pop up window
        createPopUpWindow();
        /*Make the memory allocation here because i need
         *only one memory allocation for my mArrayAdapter*/
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        getListViewAndSetListener();
        checkIfSupportsBluetooth();
        discoverDevices();
    }

    private void createPopUpWindow(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .8));
    }

    /* Get the list view and set a listener
     * If a device is selected from the available devices
     * Connect to selected device*/
    private void getListViewAndSetListener(){
        lstView = (ListView) findViewById(R.id.listAvailableDevices);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked item position
                String itemValue = (String) lstView.getItemAtPosition(position);
                // Get the clicked items MAC address
                String MAC = itemValue.substring(itemValue.length() - 17);
                // Creating Intent to start new activity
                Intent selectController = new Intent(getBaseContext(), SelectController.class);
                // Passing the mac address to new activity
                selectController.putExtra("MAC", MAC);
                // Start the new activity
                startActivity(selectController);
                // Finisth the current activity
                finish();
            }
        });
        lstView.setAdapter(mArrayAdapter);
    }

    // Set the custom font to Toast text
    private void customToastFont(Toast toast){
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTypeface(customFont);
        toast.show();
    }

    // Checking if device supports Bluetooth
    private void checkIfSupportsBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            Toast toast = Toast.makeText(getApplicationContext(), "Bluetooth not supported",
                    Toast.LENGTH_LONG);
            customToastFont(toast);

            //If Bluetooth is not supported finish the current activity
            super.finish();
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // If Bluetooth activation is successfull the resultCode will be RESULT_OK
        // Else if the user press No the resultCode will be RESULT_CANCEL
        if(requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK){
            discoverDevices();
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "You have to activate Bluetooth \n" +
                    "Or let me do it by typing yes", Toast.LENGTH_LONG);
            customToastFont(toast);
            // Finish the current activity and return to previous
            finish();
        }
    }

    protected void enableBluetooth(){
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //REQUEST_ENABLE_BT is an int that i have been defined
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        //After the startActivityForResult method the system
        //will call the onActivityResult method automaticly
    }

    private void discoverDevices(){
        if(!mBluetoothAdapter.isEnabled()){ enableBluetooth(); }
        if (mBluetoothAdapter != null && mBluetoothAdapter.startDiscovery() != false) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start discovering devices
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mBluetoothAdapter.cancelDiscovery();
        mArrayAdapter.clear();
    }
}

