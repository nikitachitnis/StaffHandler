package com.miscos.staffhandler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.miscos.staffhandler.Adapters.MultiAdapter;
import com.miscos.staffhandler.LocalDatabase.SqLiteHelper;
import com.miscos.staffhandler.MainActivity;
import com.miscos.staffhandler.Model.Employee;
import com.miscos.staffhandler.Model.EmployeeListModel;
import com.miscos.staffhandler.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MultipleSelectionActivity extends AppCompatActivity {
    private ArrayList<String> employeeNameList;
    private String[] items;
    private ArrayList<String> selected_employeeIDList;
    private ArrayList<String> selected_employeeNameList;
    private ArrayList<String> employeeIDList;
    private RecyclerView recyclerView;
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<Employee> filteredEmployees = new ArrayList<>();
    private ArrayList<EmployeeListModel> employeeListModels = new ArrayList<>();
    private MultiAdapter adapter;
    private Button btnGetSelected,btnOkay;
    private SqLiteHelper db;
    String operation_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiselection_activity);
        db = new SqLiteHelper(this);
        this.btnGetSelected = findViewById(R.id.btnGetSelected);
        this.btnOkay = findViewById(R.id.btnSelectedokay);


        EditText edtsearchview=findViewById(R.id.search_editText);
edtsearchview.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void afterTextChanged(Editable editable)
    {
        filterList(edtsearchview.getText().toString());

    }
});
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        for (int i=0;i<employees.size();i++)
        {
           employees.get(i).setChecked(false);
        }
        adapter = new MultiAdapter(this, employees);
        recyclerView.setAdapter(adapter);


        employeeNameList = new ArrayList<>();
        employeeIDList = new ArrayList<>();

        createList();
        btnGetSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAll();
            }
        });
        btnOkay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               // selectAll();

                if (adapter.getSelected().size() > 0)
                {
                    String stringBuilder="";
                    for (int i = 0; i < adapter.getSelected().size(); i++)
                    {
                        if (i==0)
                        {
                            stringBuilder = adapter.getSelected().get(i).getId();
                        }else
                            {
                            stringBuilder = stringBuilder+"," + adapter.getSelected().get(i).getId();
                        }
                    }
                    showToast(stringBuilder);
                } else {
                    showToast("Select Employee");
                }
            }
        });

    }
  public void filterList(String s)
  {
      filteredEmployees.clear();
      if(s.equalsIgnoreCase(""))
          filteredEmployees.addAll(employees);

      for (int i = 0; i < employees.size(); i++)
      {
       if(employees.get(i).getName().contains(s))
           filteredEmployees.add(employees.get(i));
      }
      adapter.setEmployees(filteredEmployees);
  }
    private void createList()
    {
        employees = new ArrayList<>();
        for (int i = 0; i < employeeNameList.size(); i++)
        {
            Employee employee = new Employee();
            employee.setName(employeeNameList.get(i));
            employee.setId(employeeIDList.get(i));
                employee.setChecked(false);
            employees.add(employee);
        }
        adapter.setEmployees(employees);
    }


    private void selectAll()
    {
        for (int i=0;i<employees.size();i++)
        {
            employees.get(i).setChecked(true);
        }
        adapter = new MultiAdapter(this, employees);
        recyclerView.setAdapter(adapter);
    }



    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MultipleSelectionActivity.this, MainActivity.class));
        finish();
    }
}