package com.adolfo.flexiologistics.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.adapters.ProductAdapter;
import com.adolfo.flexiologistics.apiServices.APIServices;
import com.adolfo.flexiologistics.apiServices.GetInventoriesAPIService;
import com.adolfo.flexiologistics.models.Inventory;
import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.CoreApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProductsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private LinearLayout lin_back;
    private RecyclerView rv_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupProductosView();
    }

    private void initView() {
        lin_back = findViewById(R.id.lin_back);
        rv_products = findViewById(R.id.rv_products);
    }

    private void setupProductosView() {
        @SuppressLint("WrongConstant") LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        mLayoutManager.setRecycleChildrenOnDetach(true);
        rv_products.setLayoutManager(mLayoutManager);

        if (CoreApp.isNetworkConnection(this)) {
            new GetInventoriesAPIService(this, 1, 100, result -> {
                if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                    AppConstants.inventories.clear();
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getString("success").equals("true")) {
                            Log.d(TAG, "Log In");
                            String dataStr = object.getString("data");
                            if (!dataStr.equals("")) {
                                JSONObject dataObject = new JSONObject(dataStr);
                                JSONArray jsonArray = dataObject.getJSONArray("jobs");
                                Date currentTime = Calendar.getInstance().getTime();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String productID = jsonObject.getString("id");
                                        String code = jsonObject.getString("code");
                                        String reference = jsonObject.getString("reference_num");
                                        String provideName = jsonObject.getString("nombre_proveedor");
                                        String typeStr = jsonObject.getString("type");
                                        String[] separated = jsonObject.getString("date").split(" ");
                                        String dateString = separated[0];
                                        String currentTimeStr = timeFormat.format(currentTime);
                                        Date current;
                                        int hours = 0;
                                        int mins = 0;
                                        try {
                                            current = timeFormat.parse(currentTimeStr);
                                            Date timeValue = timeFormat.parse(separated[1]);
                                            long mills = current.getTime() - timeValue.getTime();
                                            if (mills < 0) {
                                                mills = 0 - mills;
                                            }
                                            hours = (int) (mills/(1000 * 60 * 60));
                                            mins = (int) (mills % (1000*60*60));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (typeStr.equals("D")) {
                                            if (hours != 0) {
                                                AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_1, productID, code, reference, provideName, typeStr, dateString, hours + " hours ago"));
                                            } else {
                                                AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_1, productID, code, reference, provideName, typeStr, dateString, mins + " mins ago"));
                                            }
                                        } else {
                                            if (hours != 0) {
                                                AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_2, productID, code, reference, provideName, typeStr, dateString, hours + " hours ago"));
                                            } else {
                                                AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_2, productID, code, reference, provideName, typeStr, dateString, mins + " mins ago"));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (AppConstants.inventories.size() > 0) {
                        rv_products.setVisibility(View.VISIBLE);
                        ProductAdapter productAdapter = new ProductAdapter(this, AppConstants.inventories);
                        rv_products.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged();
                    } else {
                        rv_products.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }, true).execute();
        } else {
            Toast.makeText(this,"No internet connection available", Toast.LENGTH_LONG).show();
        }

        lin_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.lin_back) {
            AppConstants.currentState = "Actualizar";
            startActivity(new Intent(this, DashboardActivity.class));
        }
    }
}
