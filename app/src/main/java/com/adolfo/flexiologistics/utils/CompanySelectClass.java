package com.adolfo.flexiologistics.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.activities.DashboardActivity;
import com.adolfo.flexiologistics.adapters.CompanyAdapter;

import java.util.Objects;

public class CompanySelectClass extends Dialog implements CompanyAdapter.CompanyListener {

    private DashboardActivity dashboardActivity;
    private RecyclerView rv_company;
    private TextView tv_company;

    public CompanySelectClass(DashboardActivity dashboardActivity, TextView tv_company) {
        super(dashboardActivity);
        // TODO Auto-generated constructor stub
        this.dashboardActivity = dashboardActivity;
        this.tv_company = tv_company;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.company_select);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rv_company = findViewById(R.id.rv_company);

        showCompanies();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onEvent() {
        tv_company.setText("Company : " + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyName());
        dashboardActivity.setupDashboardView();
        dismiss();
    }

    private void showCompanies() {
        @SuppressLint("WrongConstant") LinearLayoutManager mLayoutManager = new LinearLayoutManager(dashboardActivity, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        mLayoutManager.setRecycleChildrenOnDetach(true);
        rv_company.setLayoutManager(mLayoutManager);

        CompanyAdapter companyAdapter = new CompanyAdapter(AppConstants.companyArr, this);
        rv_company.setAdapter(companyAdapter);
        companyAdapter.notifyDataSetChanged();
    }
}
