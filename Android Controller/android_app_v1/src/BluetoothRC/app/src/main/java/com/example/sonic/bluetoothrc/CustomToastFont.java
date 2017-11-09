package com.example.sonic.bluetoothrc;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToastFont extends Activity {
    public void setCustomToastFont(Toast toast, Typeface myFont){
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTypeface(myFont);
        toast.show();
    }
}
