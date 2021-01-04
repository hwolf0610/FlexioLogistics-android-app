package com.adolfo.flexiologistics.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.adapters.DeliveryAdapter;
import com.adolfo.flexiologistics.apiServices.APIServices;
import com.adolfo.flexiologistics.apiServices.GetInventoryItemAPIService;
import com.adolfo.flexiologistics.apiServices.SaleItemsAPIService;
import com.adolfo.flexiologistics.apiServices.SendInventoryAPIService;
import com.adolfo.flexiologistics.models.DeliveryItem;
import com.adolfo.flexiologistics.models.Sale;
import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.CoreApp;
import com.adolfo.flexiologistics.utils.CustomManualClass;
import com.adolfo.flexiologistics.utils.LoadingDialog;
import com.adolfo.flexiologistics.utils.Preferences;
import com.adolfo.flexiologistics.utils.ScanType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.device.ScanDevice;

public class DeliveryActivity extends AppCompatActivity implements View.OnClickListener, DeliveryAdapter.EventListener {

    private static final String TAG = DeliveryActivity.class.getSimpleName();

    private LinearLayout lin_back;
    private ImageView img_inventory, img_barcode, img_manual;
    private TextView tv_title, tv_name, tv_subname, tv_date, tv_save, tv_finish, tv_cancel;
    private RecyclerView rv_delivery_item;

    private LoadingDialog loadingDialog;

    private BroadcastReceiver mDeliveryScanReceiver = new BroadcastReceiver() {

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
        setContentView(R.layout.activity_delivery);

        initView();

        AppConstants.saleItmes.clear();
        getDeliveryItems();
        loadingDialog = new LoadingDialog(this, false);
        getSaleItems(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.SCAN_ACTION);
        registerReceiver(mDeliveryScanReceiver, filter);

        if (AppConstants.scanType != null) {
            setupDeliveryView();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (AppConstants.sm != null) {
            AppConstants.sm.stopScan();
        }
        unregisterReceiver(mDeliveryScanReceiver);
    }

    private void getDeliveryItems() {
        if (CoreApp.isNetworkConnection(this)) {
            new GetInventoryItemAPIService(this, AppConstants.inventories.get(AppConstants.pos).getProductID(), AppConstants.inventories.get(AppConstants.pos).getTypeStr(), result -> {
                if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                    AppConstants.deliveryItems.clear();

                    if (Preferences.getValue_Boolean(this, AppConstants.inventories.get(AppConstants.pos).getSubname(), false)) {
                        String[] itemIDArr = Preferences.loadArray(this, Preferences.DELIVERY_ITEM_ID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                        String[] serverNameArr = Preferences.loadArray(this, Preferences.DELIVERY_ITEM_SERVER + AppConstants.inventories.get(AppConstants.pos).getSubname());
                        String[] countArr = Preferences.loadArray(this, Preferences.DELIVERY_ITEM_COUNT + AppConstants.inventories.get(AppConstants.pos).getSubname());
                        String[] packedArr = Preferences.loadArray(this, Preferences.DELIVERY_ITEM_PACKED + AppConstants.inventories.get(AppConstants.pos).getSubname());
                        String[] warehouseArr = Preferences.loadArray(this, Preferences.DELIVERY_ITEM_WAREHOUSEID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                        String[] lineItemIDArr = Preferences.loadArray(this, Preferences.DELIVERY_ITEM_LINEITEMID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                        String[] unidadIDArr = Preferences.loadArray(this, Preferences.DELIVERY_ITEM_UNIDADID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                        String[] IDArr = Preferences.loadArray(this, Preferences.DELIVERY_ITEM_IDID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                        String[] barcodeArr = Preferences.loadArray(this, Preferences.DELIVERY_ITEM_BARCODE + AppConstants.inventories.get(AppConstants.pos).getSubname());

                        for (int k = 0; k < itemIDArr.length; k++) {
                            AppConstants.deliveryItems.add(new DeliveryItem(itemIDArr[k], serverNameArr[k], Integer.valueOf(countArr[k]), Float.valueOf(packedArr[k]), Integer.valueOf(warehouseArr[k]),
                                    Integer.valueOf(lineItemIDArr[k]), Integer.valueOf(unidadIDArr[k]), Integer.valueOf(IDArr[k]), barcodeArr[k]));
                        }
                    } else {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getString("success").equals("true")) {
                                String dataStr = object.getString("data");
                                Log.d(TAG, dataStr);
                                if (!dataStr.equals("")) {
                                    JSONObject dataObject = new JSONObject(dataStr);
                                    String detailStr = dataObject.getString("details");
                                    JSONObject detailObject = new JSONObject(detailStr);
                                    JSONArray jsonArray = detailObject.getJSONArray("items");
                                    String typeStr = detailObject.getJSONObject("0").getString("type");
                                    int despacho_id = detailObject.getJSONObject("0").getInt("id");

                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String itemID = jsonObject.getString("items_id");
                                            String itemName = jsonObject.getString("items_name");
                                            if (typeStr.equals("D")) {
                                                int cantidad = jsonObject.getInt("total_quantity");
                                                int warehouseID = 0;
                                                int line_item_id = jsonObject.getInt("linea_id");
                                                int unidad_id = jsonObject.getInt("unidad_id");
                                                String barcodeStr = "";
                                                String barcodeArrStr = jsonObject.getString("items_codigo_barra");
                                                if (!barcodeArrStr.equals("")) {
                                                    JSONArray barcodeArr = jsonObject.getJSONObject("items_codigo_barra").getJSONArray("codigo_barra");
                                                    if (barcodeArr != null && barcodeArr.length() > 0) {
                                                        JSONObject barcodeArrObject = barcodeArr.getJSONObject(0);
                                                        barcodeStr = barcodeArrObject.getString("codigo_barra").replace(" ", "");
                                                    }
                                                }

                                                AppConstants.deliveryItems.add(new DeliveryItem(itemID, itemName, cantidad, 0, warehouseID, line_item_id, unidad_id, despacho_id, barcodeStr));
                                            } else {
                                                int cantidad = jsonObject.getInt("cantidad");
                                                int warehouseID = jsonObject.getInt("bodega_id");
                                                int line_item_id = jsonObject.getInt("line_item_id");
                                                int unidad_id = jsonObject.getInt("unidad_id");
                                                int id = jsonObject.getInt("id");
                                                String barcodeStr = "";
                                                String barcodeArrStr = jsonObject.getString("item_codigo_barra");
                                                if (!barcodeArrStr.equals("[]")) {
                                                    JSONArray barcodeArr = jsonObject.getJSONObject("item_codigo_barra").getJSONArray("codigo_barra");
                                                    if (barcodeArr != null && barcodeArr.length() > 0) {
                                                        JSONObject barcodeArrObject = barcodeArr.getJSONObject(0);
                                                        barcodeStr = barcodeArrObject.getString("codigo_barra").replace(" ", "");
                                                    }
                                                }

                                                AppConstants.deliveryItems.add(new DeliveryItem(itemID, itemName, cantidad, 0, warehouseID, line_item_id, unidad_id, id, barcodeStr));
                                            }
                                        }
                                    } else {
                                        Toast.makeText(DeliveryActivity.this, "There are no items", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    setupDeliveryView();
                } else {
                    Toast.makeText(this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }, true).execute();
        } else {
            Toast.makeText(this,"No internet connection available", Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        lin_back = findViewById(R.id.lin_back);
        img_inventory = findViewById(R.id.img_inventory);
        img_barcode = findViewById(R.id.img_barcode);
        img_manual = findViewById(R.id.img_manual);
        tv_title = findViewById(R.id.tv_title);
        tv_name = findViewById(R.id.tv_name);
        tv_subname = findViewById(R.id.tv_subname);
        tv_date = findViewById(R.id.tv_date);
        tv_save = findViewById(R.id.tv_save);
        tv_finish = findViewById(R.id.tv_finish);
        tv_cancel = findViewById(R.id.tv_cancel);
        rv_delivery_item = findViewById(R.id.rv_delivery_item);

        if (AppConstants.inventories.get(AppConstants.pos).getTypeStr().equals("D")) {
            tv_title.setText("Despacho");
        } else {
            tv_title.setText("Entrada");
        }
    }

    public void setupDeliveryView() {
        img_inventory.setImageResource(AppConstants.inventories.get(AppConstants.pos).getImgType());
        tv_name.setText(AppConstants.inventories.get(AppConstants.pos).getName());
        tv_subname.setText(AppConstants.inventories.get(AppConstants.pos).getSubname());
        tv_date.setText(AppConstants.inventories.get(AppConstants.pos).getDateStr());

        @SuppressLint("WrongConstant") LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        mLayoutManager.setRecycleChildrenOnDetach(true);
        rv_delivery_item.setLayoutManager(mLayoutManager);

        if (AppConstants.deliveryItems.size() > 0) {
            rv_delivery_item.setVisibility(View.VISIBLE);
            DeliveryAdapter deliveryAdapter = new DeliveryAdapter(this, this, AppConstants.deliveryItems);
            rv_delivery_item.setAdapter(deliveryAdapter);
            deliveryAdapter.notifyDataSetChanged();
        } else {
            rv_delivery_item.setVisibility(View.INVISIBLE);
        }

        lin_back.setOnClickListener(this);
        img_barcode.setOnClickListener(this);
        img_manual.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    private void getSaleItems(int page) {
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

        boolean isNewItem = false;
        if (contents.length() > 2) {
            isNewItem = contents.substring(0, 2).equals("02");
        }

        String[] separated = contents.split(" ");
        String itemID = "";
        String packedStr = "";
        for (String aSeparated : separated) {
            if (!aSeparated.equals("")) {
                if (itemID.equals("")) {
                    itemID = aSeparated;
                } else {
                    packedStr = aSeparated;
                }
            }
        }

        float packed = 1;
        if (!packedStr.equals("") && CoreApp.isNumeric(packedStr)) {
            packed = Float.valueOf(packedStr);
        }

        switch (AppConstants.scanType) {
            case DELIVERY:
                if (isNewItem) {
                    if (AppConstants.deliveryItems.size() > 0) {
                        AppConstants.deliveryItems.add(new DeliveryItem(itemID, AppConstants.deliveryItems.get(0).getServerName(), 0, packed, AppConstants.deliveryItems.get(0).getWarehouseID(),
                                AppConstants.deliveryItems.get(0).getLineItemID(), AppConstants.deliveryItems.get(0).getUnidadID(), AppConstants.deliveryItems.get(0).getId(), itemID));
                    } else {
                        String serverName = "";
                        if (AppConstants.saleItmes.size() > 0) {
                            for (Sale sale : AppConstants.saleItmes) {
                                if (sale.getCode().replace(" ", "").equals(itemID)) {
                                    serverName = sale.getClientName();
                                }
                            }
                        }
                        AppConstants.deliveryItems.add(new DeliveryItem(itemID, serverName, 0, packed, 4, Integer.valueOf(itemID), 4, 0, itemID));
                    }
                    setupDeliveryView();
                } else {
                    List<DeliveryItem> deliveryItems = new ArrayList<>(AppConstants.deliveryItems);
                    AppConstants.deliveryItems.clear();
                    boolean isAdded = false;
                    for (int i = 0; i < deliveryItems.size(); i++) {
                        DeliveryItem deliveryItem = deliveryItems.get(i);
                        if (deliveryItem.getItemBarcode().equals(itemID)) {
                            isAdded = true;
                            AppConstants.deliveryItems.add(new DeliveryItem(deliveryItem.getItemID(), deliveryItem.getServerName(), deliveryItem.getCount(), deliveryItem.getPacked() + 1, deliveryItem.getWarehouseID(),
                                    deliveryItem.getLineItemID(), deliveryItem.getUnidadID(), deliveryItem.getId(), deliveryItem.getItemBarcode()));
                        } else {
                            AppConstants.deliveryItems.add(deliveryItem);
                        }
                    }

                    if (isAdded) {
                        setupDeliveryView();
                    } else {
                        Toast.makeText(this, "Scanned item not in the packing list.", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            case MANUALDELIVERY:
                if (isNewItem) {
                    if (AppConstants.deliveryItems.size() > 0) {
                        AppConstants.deliveryItems.add(new DeliveryItem(itemID, AppConstants.deliveryItems.get(0).getServerName(), 0, packed, AppConstants.deliveryItems.get(0).getWarehouseID(),
                                AppConstants.deliveryItems.get(0).getLineItemID(), AppConstants.deliveryItems.get(0).getUnidadID(), AppConstants.deliveryItems.get(0).getId(), itemID));
                    } else {
                        String serverName = "";
                        if (AppConstants.saleItmes.size() > 0) {
                            for (Sale sale : AppConstants.saleItmes) {
                                if (sale.getCode().replace(" ", "").equals(itemID)) {
                                    serverName = sale.getClientName();
                                }
                            }
                        }
                        AppConstants.deliveryItems.add(new DeliveryItem(itemID, serverName, 0, packed, 4, Integer.valueOf(itemID), 4, 0, itemID));
                    }
                    setupDeliveryView();
                } else {
                    boolean isExisted = false;
                    for (int i = 0; i < AppConstants.deliveryItems.size(); i++) {
                        DeliveryItem deliveryItem = AppConstants.deliveryItems.get(i);
                        if (deliveryItem.getItemBarcode().equals(itemID)) {
                            isExisted = true;
                        }
                    }

                    if (isExisted) {
                        CustomManualClass cdd = new CustomManualClass(DeliveryActivity.this, itemID);
                        cdd.show();
                    } else {
                        Toast.makeText(this, "Scanned item not in the packing list.", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                Log.d(TAG, "Scan Activity");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back: {
                finish();
                break;
            }
            case R.id.tv_cancel: {
                finish();
                break;
            }
            case R.id.img_barcode: {
                Log.d(TAG, "Scan the barcode");
                AppConstants.scanType = ScanType.DELIVERY;
                scanBarcode();
                break;
            }
            case R.id.img_manual: {
                Log.d(TAG, "Scan the barcode manually");
                AppConstants.scanType = ScanType.MANUALDELIVERY;
                scanBarcode();
                break;
            }
            case R.id.tv_save: {
                Log.d(TAG, "Save");
                if (AppConstants.deliveryItems.size() > 0) {
                    Preferences.setValue(this, AppConstants.inventories.get(AppConstants.pos).getSubname(), true);

                    String[] itemIDArr = new String[AppConstants.deliveryItems.size()];
                    String[] serverNameArr = new String[AppConstants.deliveryItems.size()];
                    String[] countArr = new String[AppConstants.deliveryItems.size()];
                    String[] packedArr = new String[AppConstants.deliveryItems.size()];
                    String[] warehouseArr = new String[AppConstants.deliveryItems.size()];
                    String[] lineItemIDArr = new String[AppConstants.deliveryItems.size()];
                    String[] unidadIDArr = new String[AppConstants.deliveryItems.size()];
                    String[] IDArr = new String[AppConstants.deliveryItems.size()];
                    String[] barcodeArr = new String[AppConstants.deliveryItems.size()];
                    for (int i = 0; i < AppConstants.deliveryItems.size(); i++) {
                        DeliveryItem deliveryItem = AppConstants.deliveryItems.get(i);
                        itemIDArr[i] = deliveryItem.getItemID();
                        serverNameArr[i] = deliveryItem.getServerName();
                        countArr[i] = String.valueOf(deliveryItem.getCount());
                        packedArr[i] = String.valueOf(deliveryItem.getPacked());
                        warehouseArr[i] = String.valueOf(deliveryItem.getWarehouseID());
                        lineItemIDArr[i] = String.valueOf(deliveryItem.getLineItemID());
                        unidadIDArr[i] = String.valueOf(deliveryItem.getUnidadID());
                        IDArr[i] = String.valueOf(deliveryItem.getId());
                        barcodeArr[i] = deliveryItem.getItemBarcode();
                    }

                    Preferences.saveArray(this, itemIDArr, Preferences.DELIVERY_ITEM_ID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                    Preferences.saveArray(this, serverNameArr, Preferences.DELIVERY_ITEM_SERVER + AppConstants.inventories.get(AppConstants.pos).getSubname());
                    Preferences.saveArray(this, countArr, Preferences.DELIVERY_ITEM_COUNT + AppConstants.inventories.get(AppConstants.pos).getSubname());
                    Preferences.saveArray(this, packedArr, Preferences.DELIVERY_ITEM_PACKED + AppConstants.inventories.get(AppConstants.pos).getSubname());
                    Preferences.saveArray(this, warehouseArr, Preferences.DELIVERY_ITEM_WAREHOUSEID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                    Preferences.saveArray(this, lineItemIDArr, Preferences.DELIVERY_ITEM_LINEITEMID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                    Preferences.saveArray(this, unidadIDArr, Preferences.DELIVERY_ITEM_UNIDADID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                    Preferences.saveArray(this, IDArr, Preferences.DELIVERY_ITEM_IDID + AppConstants.inventories.get(AppConstants.pos).getSubname());
                    Preferences.saveArray(this, barcodeArr, Preferences.DELIVERY_ITEM_BARCODE + AppConstants.inventories.get(AppConstants.pos).getSubname());
                }

                finish();
                break;
            }
            case R.id.tv_finish: {
                Log.d(TAG, "Save and Finish");
                Date currentTime = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateStr = sdf.format(currentTime);

                if (CoreApp.isNetworkConnection(this)) {
                    new SendInventoryAPIService(this, AppConstants.inventories.get(AppConstants.pos).getProductID(), AppConstants.inventories.get(AppConstants.pos).getTypeStr(), currentDateStr, result -> {
                        if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("success").equals("true")) {
                                    Log.d(TAG, "Success");
                                    finish();
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
                break;
            }
        }
    }

    @Override
    public void onEvent(ScanType scanType, int pos) {
        AppConstants.barcodePos = pos;
        AppConstants.scanType = scanType;
        scanBarcode();
    }
}
