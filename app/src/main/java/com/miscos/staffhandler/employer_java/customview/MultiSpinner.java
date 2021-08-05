package com.miscos.staffhandler.employer_java.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;


import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;


import java.util.Arrays;
import java.util.List;

import static java.security.AccessController.getContext;

public class MultiSpinner extends androidx.appcompat.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private List<Integer> disabled;
    private String defaultText;
    public MultiSpinnerListener listener;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (which==0)
        {
            selected[0] = isChecked;
            if (isChecked) {
                for (int i = 1; i < selected.length; i++) {
                    selected[i] = false;
                    ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                }
            }
        } else {
            if (!selected[0]) {
                if (disabled.contains(which)) {
                    selected[which] = false;
                    ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                    SnackBarHelper.snackBarMessage(getContext(), "Can't select this shift");
                } else {
                    selected[which] = isChecked;
                }
            } else {
                selected[which] = false;
                ((AlertDialog) dialog).getListView().setItemChecked(which, false);
               // SnackBarHelper.snackBarMessage(getContext(), "Please deselect All to select another shift");
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someSelected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
                someSelected = true;
            }
        }
        String spinnerText;
        if (someSelected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        listener.onItemsSelected(selected);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<String> items,boolean[] selectedShift,List<Integer> disabled, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
        this.disabled = disabled;
        this.defaultText = allText;
        this.listener = listener;

        // all selected by default
        if (selectedShift!=null){
            selected=selectedShift;
            StringBuffer spinnerBuffer = new StringBuffer();
            boolean someSelected = false;
            for (int i = 0; i < items.size(); i++) {
                if (selected[i] == true) {
                    spinnerBuffer.append(items.get(i));
                    spinnerBuffer.append(", ");
                    someSelected = true;
                }
            }
            String spinnerText;
            if (someSelected) {
                spinnerText = spinnerBuffer.toString();
                if (spinnerText.length() > 2)
                    defaultText = spinnerText.substring(0, spinnerText.length() - 2);
            }
        } else {
            selected = new boolean[items.size()];
            Arrays.fill(selected, false);
        }

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { defaultText });
        setAdapter(adapter);
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }
}