package com.adolfo.flexiologistics.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.models.ConsumptionItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConsumptionAdapter extends RecyclerView.Adapter<ConsumptionAdapter.CustomViewHolder> {

    private static final String TAG = ConsumptionAdapter.class.getSimpleName();

    private Context mContext;
    private List<ConsumptionItem> arrayList;

    public ConsumptionAdapter(Context mContext, List<ConsumptionItem> list) {
        this.mContext = mContext;
        this.arrayList = list;
    }

    @NonNull
    @Override
    public ConsumptionAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_consumption_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ConsumptionAdapter.CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ConsumptionAdapter.CustomViewHolder customViewHolder, final int pos) {
        final ConsumptionItem list = arrayList.get(pos);
        customViewHolder.tv_name.setText(list.getAtributo());
        customViewHolder.tv_count.setText("Total : " + list.getCantidad());

        customViewHolder.lin_item.setOnClickListener(view -> {
            Log.d(TAG, "Item Clicked");
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lin_item;
        TextView tv_name, tv_count;

        CustomViewHolder(View view) {
            super(view);
            lin_item = view.findViewById(R.id.lin_item);
            tv_name = view.findViewById(R.id.tv_name);
            tv_count= view.findViewById(R.id.tv_count);
        }
    }
}
