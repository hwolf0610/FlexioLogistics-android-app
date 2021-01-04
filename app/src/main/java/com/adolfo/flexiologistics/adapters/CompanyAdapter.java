package com.adolfo.flexiologistics.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.models.Company;
import com.adolfo.flexiologistics.utils.AppConstants;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CustomViewHolder> {

    private List<Company> arrayList;

    private CompanyListener companyListener;

    public interface CompanyListener {
        void onEvent();
    }

    public CompanyAdapter(List<Company> list, CompanyListener companyListener) {
        this.arrayList = list;
        this.companyListener = companyListener;
    }

    @NonNull
    @Override
    public CompanyAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_company, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CompanyAdapter.CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CompanyAdapter.CustomViewHolder customViewHolder, final int pos) {
        final Company list = arrayList.get(pos);
        customViewHolder.tv_company_id.setText(list.getCompanyName());

        customViewHolder.tv_company_id.setOnClickListener(view -> {
            AppConstants.selectedCompany = pos;
            companyListener.onEvent();
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_company_id;

        CustomViewHolder(View view) {
            super(view);
            tv_company_id = view.findViewById(R.id.tv_company_id);
        }
    }
}
