package com.adolfo.flexiologistics.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.adapters.ConsumptionAdapter;
import com.adolfo.flexiologistics.apiServices.APIServices;
import com.adolfo.flexiologistics.apiServices.CreateTrabajoAPIService;
import com.adolfo.flexiologistics.apiServices.GetSaleDetailAPIService;
import com.adolfo.flexiologistics.apiServices.SaleItemsAPIService;
import com.adolfo.flexiologistics.apiServices.SendSaleAPIService;
import com.adolfo.flexiologistics.models.ConsumptionItem;
import com.adolfo.flexiologistics.models.Sale;
import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.CoreApp;
import com.adolfo.flexiologistics.utils.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConsumptionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ConsumptionActivity.class.getSimpleName();

    private LinearLayout lin_back;
    private EditText edt_name, edt_subname;
    private TextView tv_add_item, tv_finish, tv_cancel;
    private RecyclerView rv_consumption_item;
    private int saleID;

    private LoadingDialog loadingDialog;

    private BroadcastReceiver mEntraScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            String barcodeStr = new String(barocode, 0, barocodelen);
            processBarcode(barcodeStr);
            AppConstants.sm.stopScan();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);

        initView();

        AppConstants.saleItmes.clear();
        loadingDialog = new LoadingDialog(this, false);
        AppConstants.consumptionItems.clear();
        getSaleDetail("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.SCAN_ACTION);
        registerReceiver(mEntraScanReceiver, filter);

        setupConsumptionView();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (AppConstants.sm != null) {
            AppConstants.sm.stopScan();
        }
        unregisterReceiver(mEntraScanReceiver);
    }

    private void initView() {
        lin_back = findViewById(R.id.lin_back);
        edt_name = findViewById(R.id.edt_name);
        edt_subname = findViewById(R.id.edt_subname);
        tv_add_item = findViewById(R.id.tv_add_item);
        tv_finish = findViewById(R.id.tv_finish);
        tv_cancel = findViewById(R.id.tv_cancel);
        rv_consumption_item = findViewById(R.id.rv_consumption_item);

        if (AppConstants.isNewSale) {
            saleID = 0;
            edt_name.setText("");
            edt_subname.setText("");
        } else {
            saleID = AppConstants.sales.get(AppConstants.selectedSale).getItemID();
            edt_name.setText(AppConstants.sales.get(AppConstants.selectedSale).getClientName());
            edt_subname.setText(AppConstants.sales.get(AppConstants.selectedSale).getCode());
        }
    }

    private void setupConsumptionView() {
        @SuppressLint("WrongConstant") LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        mLayoutManager.setRecycleChildrenOnDetach(true);
        rv_consumption_item.setLayoutManager(mLayoutManager);

        if (AppConstants.consumptionItems.size() > 0) {
            rv_consumption_item.setVisibility(View.VISIBLE);
            ConsumptionAdapter consumptionAdapter = new ConsumptionAdapter(this, AppConstants.consumptionItems);
            rv_consumption_item.setAdapter(consumptionAdapter);
            consumptionAdapter.notifyDataSetChanged();
        } else {
            rv_consumption_item.setVisibility(View.INVISIBLE);
        }

        lin_back.setOnClickListener(this);
        tv_add_item.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    private void getSaleDetail(String serverName) {
        if (CoreApp.isNetworkConnection(this)) {
            AppConstants.consumptionItems.clear();
            new GetSaleDetailAPIService(this, saleID, result -> {
                if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getString("success").equals("true")) {
                            JSONObject dataObject = object.getJSONObject("data");
                            JSONArray jsonArray = dataObject.getJSONArray("lines_items");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String categoryID = jsonObject.getString("categoria_id");
                                    String itemID = jsonObject.getString("item_id");
                                    String cantidad = jsonObject.getString("cantidad");
                                    String unidad_id = jsonObject.getString("unidad_id");
                                    String bodega_id = jsonObject.getString("bodega_id");

                                    AppConstants.consumptionItems.add(new ConsumptionItem(categoryID, itemID, serverName, cantidad, unidad_id, bodega_id));
                                }
                            }
                        }

                        setupConsumptionView();
                        getSaleItems(1);

                    } catch (JSONException e) {
                        loadingDialog.hide();
                        e.printStackTrace();
                    }
                } else {
                    loadingDialog.hide();
                    Toast.makeText(this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }, true).execute();
        } else {
            loadingDialog.hide();
            Toast.makeText(this,"No internet connection available", Toast.LENGTH_LONG).show();
        }
    }

    private void getSaleItems(int page) {
        AppConstants.saleItmes.clear();
        if (CoreApp.isNetworkConnection(this)) {
            new SaleItemsAPIService(this, page, result -> {
                if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                    boolean isRefresh = false;
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getString("success").equals("true")) {
                            JSONObject dataObject = object.getJSONObject("data");
                            JSONArray jsonArray = dataObject.getJSONArray("data");
                            int current_page = dataObject.getInt("current_page");
                            int lastPage = dataObject.getInt("last_page");
                            isRefresh = current_page < lastPage;
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int itemID = jsonObject.getInt("id");
                                    String code = jsonObject.getString("codigo");
                                    String serverName = jsonObject.getString("nombre");

                                    AppConstants.saleItmes.add(new Sale(itemID, code, serverName));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        loadingDialog.hide();
                        e.printStackTrace();
                    }

                    Log.d(TAG, String.valueOf(AppConstants.saleItmes.size()));

                    if (isRefresh) {
                        getSaleItems(page + 1);
                    } else {
                        loadingDialog.hide();
                    }

                } else {
                    loadingDialog.hide();
                    Toast.makeText(this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }, true).execute();
        } else {
            loadingDialog.hide();
            Toast.makeText(this,"No internet connection available", Toast.LENGTH_LONG).show();
        }
    }

    private void scanBarcode() {
        if (AppConstants.sm == null) {
            AppConstants.sm = new ScanDevice();
            AppConstants.sm.setOutScanMode(0);
        }

        AppConstants.sm.openScan();
        AppConstants.sm.startScan();
    }

    private void processBarcode(String contents) {
        Toast.makeText(this, "Scaned Barcode : " + contents, Toast.LENGTH_LONG).show();

        String[] separated = contents.split(" ");
        String itemID = "";
        for (String aSeparated : separated) {
            if (!aSeparated.equals("")) {
                if (itemID.equals("")) {
                    itemID = aSeparated;
                }
            }
        }

        String serverName = "";
        if (AppConstants.saleItmes.size() > 0) {
            for (Sale sale : AppConstants.saleItmes) {
                if (sale.getCode().replace(" ", "").equals(itemID)) {
                    serverName = sale.getClientName();
                }
            }
        }

//        saleID = Integer.valueOf(itemID);
        getSaleDetail(serverName);

        setupConsumptionView();
    }

    private boolean isValidInfo(String nameStr, String subnameStr) {
        boolean isValid = true;
        if (nameStr.equals("")) {
            isValid = false;
            Toast.makeText(this, "Por favor ingrese el nombre del cliente", Toast.LENGTH_LONG).show();
            return isValid;
        }

        if (subnameStr.equals("")) {
            isValid = false;
            Toast.makeText(this, "Por favor, introduzca el número de pedido de ventas", Toast.LENGTH_LONG).show();
            return isValid;
        }

        return isValid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back: {
                AppConstants.currentState = "Orden";
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            }
            case R.id.tv_cancel: {
                AppConstants.currentState = "Orden";
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            }
            case R.id.tv_add_item: {
                Log.d(TAG, "Add the item");
                scanBarcode();
                break;
            }
            case R.id.tv_finish: {
                Log.d(TAG, "Save and Finish");
                if (isValidInfo(edt_name.getText().toString(), edt_subname.getText().toString())) {
                    Date currentTime = Calendar.getInstance().getTime();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDateStr = dateFormat.format(currentTime);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM:SS");
                    String currentTimeStr = timeFormat.format(currentTime);

                    Log.d(TAG, currentDateStr);
                    Log.d(TAG, currentTimeStr);

                    int id = AppConstants.sales.get(AppConstants.selectedSale).getItemID();
                    if (AppConstants.isNewSale) {
                        id = AppConstants.sales.size() + 1;
                    }

                    String code = AppConstants.sales.get(AppConstants.selectedSale).getCode();
                    if (AppConstants.isNewSale) {
                        code = edt_subname.getText().toString();
                    }

                    String name = AppConstants.sales.get(AppConstants.selectedSale).getClientName();
                    if (AppConstants.isNewSale) {
                        name = edt_name.getText().toString();
                    }

                    if (CoreApp.isNetworkConnection(this)) {
                        int finalId = id;
                        String finalName = name;
                        String finalCode = code;
                        new CreateTrabajoAPIService(this, finalName, finalCode, currentDateStr, currentTimeStr, result -> {
                            if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                                try {
                                    JSONObject object = new JSONObject(result);
                                    if (object.getString("success").equals("true")) {
                                        Log.d(TAG, "Success");

                                        new SendSaleAPIService(this, finalId, finalName, finalCode, currentDateStr, currentTimeStr, sendResult -> {
                                            if (!sendResult.equals(APIServices.RESPONSE_UNWANTED)) {
                                                try {
                                                    JSONObject sendObject = new JSONObject(sendResult);
                                                    if (sendObject.getString("success").equals("true")) {
                                                        Log.d(TAG, "Success");
                                                        finish();
                                                    } else {
                                                        Toast.makeText(this, sendObject.getString("message"), Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                                            }
                                        }, true).execute();
                                    } else {
                                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                            }
                        }, true).execute();
                    } else {
                        Toast.makeText(this,"No internet connection available", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }
        }
    }
}
