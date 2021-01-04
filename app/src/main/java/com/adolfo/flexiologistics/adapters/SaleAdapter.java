package com.adolfo.flexiologistics.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.activities.ConsumptionActivity;
import com.adolfo.flexiologistics.models.Sale;
import com.adolfo.flexiologistics.utils.AppConstants;

import java.util.List;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.CustomViewHolder> {

    private static final String TAG = SaleAdapter.class.getSimpleName();

    private Context mContext;
    private List<Sale> arrayList;

    public SaleAdapter(Context mContext, List<Sale> list) {
        this.mContext = mContext;
        this.arrayList = list;
    }

    @NonNull
    @Override
    public SaleAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_sale, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new SaleAdapter.CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final SaleAdapter.CustomViewHolder customViewHolder, final int pos) {
        final Sale list = arrayList.get(pos);
        customViewHolder.tv_name.setText(list.getCode());
        customViewHolder.tv_providerName.setText(list.getClientName());

        customViewHolder.lin_item.setOnClickListener(view -> {
            Log.d(TAG, "Item Clicked");
            AppConstants.selectedSale = pos;
            AppConstants.scanType = null;
            AppConstants.isNewSale = false;
            mContext.startActivity(new Intent(mContext, ConsumptionActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lin_item;
        TextView tv_name, tv_providerName;

        CustomViewHolder(View view) {
            super(view);
            lin_item = view.findViewById(R.id.lin_item);
            tv_name = view.findViewById(R.id.tv_name);
            tv_providerName = view.findViewById(R.id.tv_providerName);
        }
    }
}
