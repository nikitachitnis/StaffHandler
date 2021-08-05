package com.miscos.staffhandler.ssa.departdesignation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityDepartAndDesignationBinding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.DesignationArrItem;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.DesignationResponse;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.ListItem;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.employeedesignationwise.DataItem;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.employeedesignationwise.EmployeesDesignationWiseResponse;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;
import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.ssa.dialogs.DialogAdd;
import com.miscos.staffhandler.ssa.dialogs.DialogCallBack;
import com.miscos.staffhandler.ssa.dialogs.DialogRemove;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartAndDesignationActivity extends AppCompatActivity implements DesignationDataAdapter.DesignationCallBack {

    String empId;
    List<ListItem> deptList;
    List<DesignationArrItem> designationList;
    ListItem selectedDept;
    List<DataItem> employeesDesignationData;
    ActivityDepartAndDesignationBinding binding;
    DesignationDataAdapter adapter;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_depart_and_designation);
        preferenceManager=new PreferenceManager(this);
        empId = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        getDesignationList();
        binding.spDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (deptList != null) {
                    binding.lyrEmployeeDetail.setVisibility(View.GONE);
                    if (position==0) {
                        binding.imgAddDesignation.setVisibility(View.INVISIBLE);
                        binding.imgRemoveDesignation.setVisibility(View.INVISIBLE);
                    }
                    else {
                        binding.imgAddDesignation.setVisibility(View.VISIBLE);
                        binding.imgRemoveDesignation.setVisibility(View.VISIBLE);
                    }
                    selectedDept = deptList.get(position);
                    ArrayAdapter<DesignationArrItem> adapter = new ArrayAdapter<DesignationArrItem>(DepartAndDesignationActivity.this,
                            android.R.layout.simple_spinner_item, designationList = deptList.get(position).getDesignationArr());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spDesignation.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.lyrEmployeeDetail.setVisibility(View.GONE);
                if (designationList != null && employeesDesignationData != null)
                {
                    String desId = designationList.get(position).getId();
                    List<DataItem> tempData = new ArrayList<>();
                    for (int i = 0; i < employeesDesignationData.size(); i++)
                    {
                        if ((desId.equals("-1") || desId.equals(employeesDesignationData.get(i).getDesignationId())) && (selectedDept.getDeptId().equals("-1") || selectedDept.getDeptId().equals(employeesDesignationData.get(i).getDepartmentId())))
                            tempData.add(employeesDesignationData.get(i));
                    }
                    adapter.submitList(tempData);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.imgAddDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogAdd(DepartAndDesignationActivity.this, new DialogCallBack() {
                    @Override
                    public void onClickOk(String value) {
                        addDeptAPI(value);
                    }

                    @Override
                    public void onClickCancel() {

                    }
                },null, AppConstant.TYPE_DEPARTMENT).show();
            }
        });

        binding.imgAddDesignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDept != null && !selectedDept.getDeptId().equals("-1")) {
                    new DialogAdd(DepartAndDesignationActivity.this, new DialogCallBack() {
                        @Override
                        public void onClickOk(String value) {
                            if (selectedDept != null)
                                addDesignationAPI(value);
                        }

                        @Override
                        public void onClickCancel() {

                        }
                    },selectedDept.getDeptName(), AppConstant.TYPE_DESIGNATION).show();
                } else
                    SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, "Please select a valid department");

            }
        });

        binding.imgRemoveDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deptList != null && deptList.size() > 1) {
                    List<ListItem> cloneDeptList = new ArrayList<ListItem>(deptList);
                    new DialogRemove(DepartAndDesignationActivity.this, new DialogCallBack() {
                        @Override
                        public void onClickOk(String value) {
                            if (selectedDept != null)
                                removeDeptAPI(value);
                        }

                        @Override
                        public void onClickCancel() {

                        }
                    }, AppConstant.TYPE_DEPARTMENT, cloneDeptList, null).show();
                }
            }
        });

        binding.imgRemoveDesignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (designationList != null && designationList.size() > 1) {
                    List<DesignationArrItem> cloneDesignation = new ArrayList<DesignationArrItem>(designationList);
                    new DialogRemove(DepartAndDesignationActivity.this, new DialogCallBack() {
                        @Override
                        public void onClickOk(String value) {
                            if (selectedDept != null)
                                removeDesignationAPI(value);
                        }

                        @Override
                        public void onClickCancel() {

                        }
                    }, AppConstant.TYPE_DESIGNATION, null, cloneDesignation).show();
                }
            }
        });

    }

    private void addDeptAPI(String value) {
        Helper.showProgress(this);
        NetworkService.getInstance().api().addDepartment("add_department", empId, value)
                .enqueue(new Callback<List<NormalResponse>>() {
                    @Override
                    public void onResponse(Call<List<NormalResponse>> call, Response<List<NormalResponse>> response) {
                        Helper.hideProgress();
                        if (response.body() != null) {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, response.body().get(0).getMessage());
                            if (response.body().get(0).getError_code() == 104) {
                                getDesignationList();
                            }

                        } else {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, "Failed to get reponse from server");

                        }
                    }

                    @Override
                    public void onFailure(Call<List<NormalResponse>> call, Throwable t) {
                        Helper.hideProgress();
                        SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, t.getMessage() + "");
                    }
                });
    }

    private void removeDeptAPI(String value) {
        Helper.showProgress(this);
        NetworkService.getInstance().api().removeDepartment("remove_department", empId, value)
                .enqueue(new Callback<List<NormalResponse>>() {
                    @Override
                    public void onResponse(Call<List<NormalResponse>> call, Response<List<NormalResponse>> response) {
                        Helper.hideProgress();
                        if (response.body() != null) {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, response.body().get(0).getMessage());
                            if (response.body().get(0).getError_code() == 104) {
                                getDesignationList();
                            }

                        } else {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, "Failed to get reponse from server");

                        }
                    }

                    @Override
                    public void onFailure(Call<List<NormalResponse>> call, Throwable t) {
                        Helper.hideProgress();
                        SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, t.getMessage() + "");
                    }
                });
    }

    private void removeDesignationAPI(String value) {
        Helper.showProgress(this);
        NetworkService.getInstance().api().removeDesignation("remove_designation", empId, selectedDept.getDeptId(), value)
                .enqueue(new Callback<List<NormalResponse>>() {
                    @Override
                    public void onResponse(Call<List<NormalResponse>> call, Response<List<NormalResponse>> response) {
                        Helper.hideProgress();
                        if (response.body() != null) {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, response.body().get(0).getMessage());
                            if (response.body().get(0).getError_code() == 104) {
                                getDesignationList();
                            }

                        } else {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, "Failed to get reponse from server");

                        }
                    }

                    @Override
                    public void onFailure(Call<List<NormalResponse>> call, Throwable t) {
                        Helper.hideProgress();
                        SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, t.getMessage() + "");
                    }
                });
    }

    private void addDesignationAPI(String value) {
        Helper.showProgress(this);
        NetworkService.getInstance().api().addDesignation("add_designation", empId, selectedDept.getDeptId(), value)
                .enqueue(new Callback<List<NormalResponse>>() {
                    @Override
                    public void onResponse(Call<List<NormalResponse>> call, Response<List<NormalResponse>> response) {
                        Helper.hideProgress();
                        if (response.body() != null) {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, response.body().get(0).getMessage());
                            if (response.body().get(0).getError_code() == 104) {
                                getDesignationList();
                            }

                        } else {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, "Failed to get reponse from server");

                        }
                    }

                    @Override
                    public void onFailure(Call<List<NormalResponse>> call, Throwable t) {
                        Helper.hideProgress();
                        SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, t.getMessage() + "");
                    }
                });
    }


    void getDesignationList() {
        Helper.showProgress(this);
        NetworkService.getInstance().api().getDepartmentDesignationList("fetch_designation", empId)
                .enqueue(new Callback<List<DesignationResponse>>() {
                    @Override
                    public void onResponse(Call<List<DesignationResponse>> call, Response<List<DesignationResponse>> it) {
                        Helper.hideProgress();
                        if (it.body() != null) {
                            if (it.body().get(0).getErrorCode() == 104) {
                                ListItem allItem = new ListItem();
                                allItem.setDeptId("-1");
                                allItem.setDeptName("All");
                                if (it.body().get(0).getList() == null)
                                    it.body().get(0).setList(new ArrayList<>());
                                it.body().get(0).getList().add(0, allItem);
                                for (int i = 0; i < it.body().get(0).getList().size(); i++) {
                                    DesignationArrItem allDesignation = new DesignationArrItem();
                                    allDesignation.setId("-1");
                                    allDesignation.setName("All");
                                    if (i == 0) {
                                        List<DesignationArrItem> allDesignationItems = new ArrayList<>();
                                        allDesignationItems.add(allDesignation);
                                        for (int j = 1; j < it.body().get(0).getList().size(); j++) {
                                            for (int k = 0; k < it.body().get(0).getList().get(j).getDesignationArr().size(); k++) {
                                                if (!it.body().get(0).getList().get(j).getDesignationArr().get(k).getId().equals("-1"))
                                                    allDesignationItems.add(it.body().get(0).getList().get(j).getDesignationArr().get(k));
                                            }
                                        }
                                        it.body().get(0).getList().get(i).setDesignationArr(allDesignationItems);
                                    } else {
                                        if (it.body().get(0).getList().get(i).getDesignationArr() == null)
                                            it.body().get(0).getList().get(i).setDesignationArr(new ArrayList<>());
                                        it.body().get(0).getList().get(i).getDesignationArr().add(0, allDesignation);
                                    }
                                }
                                ArrayAdapter<ListItem> adapter = new ArrayAdapter<ListItem>(DepartAndDesignationActivity.this,
                                        android.R.layout.simple_spinner_item, deptList = it.body().get(0).getList());
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spDept.setAdapter(adapter);
                                getEmployeesList();
                            }

                        } else {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, "Failed to get reponse from server");

                        }

                    }

                    @Override
                    public void onFailure(Call<List<DesignationResponse>> call, Throwable t) {
                        Log.e("fetch_designation", t.getMessage());
                        Helper.hideProgress();
                        SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, getString(R.string.something_wrong));
                    }
                });
    }

    void getEmployeesList() {
        Helper.showProgress(this);
        NetworkService.getInstance().api().getEmployeesDesignationWise("get_employee_detail_designation_wise", empId)
                .enqueue(new Callback<List<EmployeesDesignationWiseResponse>>() {
                    @Override
                    public void onResponse(Call<List<EmployeesDesignationWiseResponse>> call, Response<List<EmployeesDesignationWiseResponse>> it) {
                        Helper.hideProgress();
                        if (it.body() != null) {
                            if (it.body().get(0).getErrorCode() == 104) {
                                binding.recyclerViewDesignation.setAdapter(adapter = new DesignationDataAdapter(DepartAndDesignationActivity.this));
                                employeesDesignationData = it.body().get(0).getData();
                                if (designationList != null && designationList.size() > 0 && employeesDesignationData != null) {
                                    String desId = designationList.get(0).getId();
                                    List<DataItem> tempData = new ArrayList<>();
                                    for (int i = 0; i < employeesDesignationData.size(); i++) {
                                        if ((desId.equals("-1") || desId.equals(employeesDesignationData.get(i).getDesignationId())) && (selectedDept.getDeptId().equals("-1") || selectedDept.getDeptId().equals(employeesDesignationData.get(i).getDepartmentId())))
                                            tempData.add(employeesDesignationData.get(i));
                                    }
                                    adapter.submitList(tempData);
                                }
                            }

                        } else {
                            SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, "Failed to get reponse from server");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<EmployeesDesignationWiseResponse>> call, Throwable t) {
                        Log.e("fetch_designation", t.getMessage());
                        Helper.hideProgress();
                        SnackBarHelper.snackBarMessage(DepartAndDesignationActivity.this, getString(R.string.something_wrong));
                    }
                });
    }

    @Override
    public void onListClick(int pos, DataItem item) {
        if (item.getEmployeeData().size() > 0) {
            binding.lyrEmployeeDetail.setVisibility(View.VISIBLE);
            EmployeeDetailAdapter employeeDetailAdapter;
            binding.recyclerViewName.setAdapter(employeeDetailAdapter = new EmployeeDetailAdapter());
            employeeDetailAdapter.submitList(item.getEmployeeData());
        } else binding.lyrEmployeeDetail.setVisibility(View.GONE);
    }
}