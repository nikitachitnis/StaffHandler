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

import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.DialogAddDepartmentBinding;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;


public class DialogAdd extends Dialog {

    public Activity mActivity;
    private DialogCallBack callBack;
    private String deptName;
    private int type;
    private DialogAddDepartmentBinding binding;



    public DialogAdd(Activity mActivity, DialogCallBack callBack, String deptName, int type) {
        super(mActivity);
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.deptName = deptName;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_add_department, null, false);
        setContentView(binding.getRoot());
        if (deptName!=null)
            binding.txtName.setText(deptName);
        switch (type){
            case AppConstant.TYPE_DEPARTMENT:
                binding.txtHead.setText("Add New Department");
                break;
            case AppConstant.TYPE_DESIGNATION:
                binding.txtHead.setText("Add New Designation");
                break;
        }
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
