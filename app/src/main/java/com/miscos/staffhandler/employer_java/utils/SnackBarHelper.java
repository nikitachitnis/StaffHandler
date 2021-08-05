package com.miscos.staffhandler.employer_java.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miscos.staffhandler.R;

public class SnackBarHelper {

    public static void snackBarMessage(Context activity, String message) {
        try {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(
                        R.layout.layout_toast,
                        null
                );
                TextView text = layout.findViewById(R.id.text);
                text.setText(message);

                Toast toast = new Toast(activity);
                toast.setGravity(Gravity.FILL_HORIZONTAL & Gravity.BOTTOM, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
