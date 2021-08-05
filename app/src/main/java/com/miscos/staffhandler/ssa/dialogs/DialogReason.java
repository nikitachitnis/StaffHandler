package com.miscos.staffhandler.ssa.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.DialogOutReasonBinding;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;


public class DialogReason extends Dialog {

    public Activity mActivity;
    private DialogCallBack callBack;
    private DialogOutReasonBinding binding;



    public DialogReason(Activity mActivity, DialogCallBack callBack) {
        super(mActivity);
        this.mActivity = mActivity;
        this.callBack = callBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_out_reason, null, false);
        setContentView(binding.getRoot());

        binding.txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.edt.getText().toString())) {
                    callBack.onClickOk(binding.edt.getText().toString());
                    dismiss();
                } else
                    SnackBarHelper.snackBarMessage(mActivity,"Required Field can't be empty");
            }
        });
        binding.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onClickCancel();
                dismiss();
            }
        });
    }
}
