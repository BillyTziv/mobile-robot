package com.example.sonic.bluetoothrc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class SelectController extends Activity{

    private Typeface customfont;

    @Override
    protected void onCreate(Bundle savedI){
        super.onCreate(savedI);
        setContentView(R.layout.activity_select_controller);

        initialization();

        createPopUpWindow();
    }

    // Creates a pop up window to select the controller
    public void createPopUpWindow(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .7));
    }

    public void initialization(){
        customfont = Typeface.createFromAsset(getAssets(), "fonts/coolvetica.ttf");
        ((TextView)findViewById(R.id.joystick)).setTypeface(customfont);
        ((TextView)findViewById(R.id.accelerometer)).setTypeface(customfont);
        ((TextView)findViewById(R.id.select_controller)).setTypeface(customfont);
    }

    public void joystickController(View view){
        Intent controller = new Intent(getBaseContext(), Controller.class);
        controller.putExtra("MAC", ((Bundle)getIntent().getExtras()).getString("MAC"));
        startActivity(controller);
    }

    public void accelerometerController(View view){
        Intent controller = new Intent(getBaseContext(), AccelerometerController.class);
        controller.putExtra("MAC", ((Bundle)getIntent().getExtras()).getString("MAC"));
        startActivity(controller);
    }
}
