package com.adolfo.flexiologistics.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.activities.DeliveryActivity;
import com.adolfo.flexiologistics.models.DeliveryItem;
import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.NoManualClass;
import com.adolfo.flexiologistics.utils.ScanType;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.CustomViewHolder> {

    private static final String TAG = DeliveryAdapter.class.getSimpleName();

    private DeliveryActivity mContext;
    private List<DeliveryItem> arrayList;
    private EventListener listener;

    public interface EventListener {
        void onEvent(ScanType scanType, int pos);
    }

    public DeliveryAdapter(DeliveryActivity mContext, EventListener listener, List<DeliveryItem> list) {
        this.mContext = mContext;
        this.arrayList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeliveryAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_delivery_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new DeliveryAdapter.CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final DeliveryAdapter.CustomViewHolder customViewHolder, final int pos) {
        final DeliveryItem list = arrayList.get(pos);
        customViewHolder.tv_name.setText(list.getServerName());
        customViewHolder.tv_count.setText("Total : " + String.valueOf(list.getCount()));
        customViewHolder.tv_packed.setText("Packed : " + String.valueOf(list.getPacked()));

        customViewHolder.rel_item.setOnClickListener(view -> {
            Log.d(TAG, "Item Clicked");
        });

        customViewHolder.lin_nobarcode.setOnClickListener(view -> {
            Log.d(TAG, "No Barcode");
            Log.d("BarcodeStr", list.getItemBarcode());
            AppConstants.barcodePos = pos;
            NoManualClass cdd = new NoManualClass(mContext, list.getItemID());
            cdd.show();
        });

        customViewHolder.img_barcode.setOnClickListener(view -> {
            Log.d(TAG, "Image Barcode");
            Log.d("BarcodeStr", list.getItemBarcode());
            listener.onEvent(ScanType.DELIVERY, pos);
        });
        customViewHolder.img_manual.setOnClickListener(view -> {
            Log.d(TAG, "Image Barcode");
            listener.onEvent(ScanType.MANUALDELIVERY, pos);
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rel_item;
        LinearLayout lin_nobarcode;
        TextView tv_name, tv_count, tv_packed;
        ImageView img_barcode, img_manual;

        CustomViewHolder(View view) {
            super(view);
            rel_item = view.findViewById(R.id.rel_item);
            lin_nobarcode = view.findViewById(R.id.lin_nobarcode);
            tv_name = view.findViewById(R.id.tv_name);
            tv_count= view.findViewById(R.id.tv_count);
            tv_packed= view.findViewById(R.id.tv_packed);
            img_barcode= view.findViewById(R.id.img_barcode);
            img_manual= view.findViewById(R.id.img_manual);
        }
    }
}
