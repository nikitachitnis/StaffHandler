package com.miscos.staffhandler.ssa.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.DialogRemoveFieldBinding;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.DesignationArrItem;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.ListItem;

import java.util.List;

public class DialogRemove extends Dialog {

    public Activity mActivity;
    private DialogCallBack callBack;
    private int type;
    private final List<ListItem> deptList;
    private final List<DesignationArrItem> designationList;
    private DialogRemoveFieldBinding binding;
    String selectedId;


    public DialogRemove(Activity mActivity, DialogCallBack callBack, int type, List<ListItem> deptList, List<DesignationArrItem> designationList) {
        super(mActivity);
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.type = type;
        this.deptList = deptList;
        this.designationList = designationList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_remove_field, null, false);
        setContentView(binding.getRoot());
        switch (type){
            case AppConstant.TYPE_DEPARTMENT:
                binding.txtHead.setText("Remove Department");
                deptList.remove(0);
                ArrayAdapter<ListItem> dAdapter = new ArrayAdapter<ListItem>(mActivity,
                        android.R.layout.simple_spinner_item, deptList);
                dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinner.setAdapter(dAdapter);
                break;
            case AppConstant.TYPE_DESIGNATION:
                binding.txtHead.setText("Remove Designation");
                designationList.remove(0);
                ArrayAdapter<DesignationArrItem> adapter = new ArrayAdapter<DesignationArrItem>(mActivity,
                        android.R.layout.simple_spinner_item, designationList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinner.setAdapter(adapter);
                break;
        }

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
                    case AppConstant.TYPE_DEPARTMENT:
                        selectedId = deptList.get(position).getDeptId();
                        break;
                    case AppConstant.TYPE_DESIGNATION:
                        selectedId = designationList.get(position).getId();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId!=null) {
                    callBack.onClickOk(selectedId);
                    dismiss();
                }
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
