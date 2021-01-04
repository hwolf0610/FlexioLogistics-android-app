package com.adolfo.flexiologistics.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.models.Inventory;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CustomViewHolder> {

    private static final String TAG = ProductAdapter.class.getSimpleName();

    private Context mContext;
    private List<Inventory> arrayList;

    public ProductAdapter(Context mContext, List<Inventory> list) {
        this.mContext = mContext;
        this.arrayList = list;
    }

    @NonNull
    @Override
    public ProductAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_dashboard, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductAdapter.CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.CustomViewHolder customViewHolder, final int pos) {
        final Inventory list = arrayList.get(pos);
        customViewHolder.img_inventory.setImageResource(list.getImgType());
        customViewHolder.tv_name.setText(list.getName());
        customViewHolder.tv_subname.setText(list.getSubname());
        customViewHolder.tv_providerName.setText(list.getProviderName());
        customViewHolder.tv_date.setText(list.getDateStr());
        customViewHolder.tv_time.setText(list.getTimeStr());

        customViewHolder.lin_item.setOnClickListener(view -> {
            Log.d(TAG, "Item Clicked");
            Toast.makeText(mContext, "This option is not used for now", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lin_item;
        ImageView img_inventory;
        TextView tv_name, tv_subname, tv_providerName, tv_date, tv_time;

        CustomViewHolder(View view) {
            super(view);
            lin_item = view.findViewById(R.id.lin_item);
            img_inventory = view.findViewById(R.id.img_inventory);
            tv_name = view.findViewById(R.id.tv_name);
            tv_subname = view.findViewById(R.id.tv_subname);
            tv_providerName = view.findViewById(R.id.tv_providerName);
            tv_date = view.findViewById(R.id.tv_date);
            tv_time= view.findViewById(R.id.tv_time);
        }
    }
}
