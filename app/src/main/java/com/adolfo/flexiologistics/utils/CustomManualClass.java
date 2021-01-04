package com.adolfo.flexiologistics.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.activities.DeliveryActivity;
import com.adolfo.flexiologistics.models.DeliveryItem;

import java.util.ArrayList;
import java.util.List;

public class CustomManualClass extends Dialog implements View.OnClickListener {

    private DeliveryActivity activity;
    private String itemID;
    private EditText edt_packed;

    public CustomManualClass(DeliveryActivity activity, String itemID) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.itemID = itemID;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_manual);
        TextView tv_title = findViewById(R.id.tv_title);
        edt_packed = findViewById(R.id.edt_packed);
        TextView tv_continue = findViewById(R.id.tv_continue);

        tv_title.setText("Enter amount packed for <" + itemID + "> :");
        tv_continue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_continue) {
            if (edt_packed.getText().toString().equals("")) {
                Toast.makeText(activity, "Please enter the packed amount.", Toast.LENGTH_LONG).show();
                return;
            }

            List<DeliveryItem> deliveryItems = new ArrayList<>(AppConstants.deliveryItems);
            AppConstants.deliveryItems.clear();
            for (int i = 0; i < deliveryItems.size(); i++) {
                DeliveryItem deliveryItem = deliveryItems.get(i);
                if (deliveryItem.getItemBarcode().equals(itemID)) {
                    AppConstants.deliveryItems.add(new DeliveryItem(deliveryItem.getItemID(), deliveryItem.getServerName(), deliveryItem.getCount(),
                            deliveryItem.getPacked() + Float.valueOf(edt_packed.getText().toString()), deliveryItem.getWarehouseID(),
                           deliveryItem.getLineItemID(), deliveryItem.getUnidadID(), deliveryItem.getId(), deliveryItem.getItemBarcode()));
                } else {
                    AppConstants.deliveryItems.add(deliveryItem);
                }
            }
//            AppConstants.scanType = ScanType.MANUALDELIVERY;
            activity.setupDeliveryView();
        }
        dismiss();
    }
}
