package com.miscos.staffhandler.ssa.policynruledocumentation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityPolicyAndRuleDocumentBinding;

import java.util.ArrayList;
import java.util.List;

public class PolicyAndRuleDocumentActivity extends AppCompatActivity {

    ActivityPolicyAndRuleDocumentBinding binding;
    PolicyFilesAdapter policyFilesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =DataBindingUtil.setContentView(this, R.layout.activity_policy_and_rule_document);
        binding.recyclerFiles.setAdapter(policyFilesAdapter = new PolicyFilesAdapter());
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            s.add("Notice No. "+i+1+".pdf");
        }
        policyFilesAdapter.submitList(s);
    }
}