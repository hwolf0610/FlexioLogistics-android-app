package com.adolfo.flexiologistics.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.adapters.DashboardAdapter;
import com.adolfo.flexiologistics.adapters.SaleAdapter;
import com.adolfo.flexiologistics.apiServices.APIServices;
import com.adolfo.flexiologistics.apiServices.GetInventoriesAPIService;
import com.adolfo.flexiologistics.apiServices.SalesAPIService;
import com.adolfo.flexiologistics.models.Inventory;
import com.adolfo.flexiologistics.models.Sale;
import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.CompanySelectClass;
import com.adolfo.flexiologistics.utils.CoreApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private LinearLayout lin_menu, lin_menu_view, lin_actualizar;
    private TextView tv_title, tv_productos, tv_definir, tv_orden, tv_actualizar, tv_company;
    private RecyclerView rv_inventory;
    private ImageView img_Add;
    private Boolean isShowedMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupDashboardView();
    }

    private void initView() {
        lin_menu = findViewById(R.id.lin_menu);
        lin_menu_view = findViewById(R.id.lin_menu_view);
        lin_actualizar = findViewById(R.id.lin_actualizar);
        tv_title = findViewById(R.id.tv_title);
        tv_productos = findViewById(R.id.tv_productos);
        tv_definir = findViewById(R.id.tv_definir);
        tv_orden = findViewById(R.id.tv_orden);
        tv_actualizar = findViewById(R.id.tv_actualizar);
        tv_company = findViewById(R.id.tv_company);
        rv_inventory = findViewById(R.id.rv_inventory);
        img_Add = findViewById(R.id.img_Add);
    }

    @SuppressLint("SetTextI18n")
    public void setupDashboardView() {
        tv_company.setText("Company : " + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyName());

        @SuppressLint("WrongConstant") LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        mLayoutManager.setRecycleChildrenOnDetach(true);
        rv_inventory.setLayoutManager(mLayoutManager);

        showDashboard();

        lin_menu.setOnClickListener(this);
        lin_menu_view.setOnClickListener(this);
        tv_productos.setOnClickListener(this);
        tv_definir.setOnClickListener(this);
        tv_orden.setOnClickListener(this);
        tv_actualizar.setOnClickListener(this);
        tv_company.setOnClickListener(this);
        img_Add.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void showDashboard() {
        Log.d(TAG, AppConstants.currentState);
        if (AppConstants.currentState.equals("Orden")) {
            tv_title.setText("Ordenes de Venta");
            tv_orden.setText("Despachos/entradas");
            tv_actualizar.setText("Actualizar Datos");
            tv_company.setVisibility(View.GONE);
            img_Add.setVisibility(View.VISIBLE);
            lin_actualizar.setVisibility(View.GONE);
            rv_inventory.setVisibility(View.VISIBLE);

            getSales();
        } else if (AppConstants.currentState.equals("Actualizar")) {
            tv_title.setText("Actualizar Datos");
            tv_orden.setText("Orden de Venta");
            tv_actualizar.setText("Despachos/entradas");
            tv_company.setVisibility(View.GONE);
            img_Add.setVisibility(View.GONE);
            lin_actualizar.setVisibility(View.VISIBLE);
            rv_inventory.setVisibility(View.GONE);
        } else {
            tv_title.setText("Despachos/entradas");
            tv_orden.setText("Orden de Venta");
            tv_actualizar.setText("Actualizar Datos");
            tv_company.setVisibility(View.VISIBLE);
            img_Add.setVisibility(View.GONE);
            lin_actualizar.setVisibility(View.GONE);
            rv_inventory.setVisibility(View.VISIBLE);

            getInventories();
        }
    }

    private void getInventories() {
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
                                        String typeStr = jsonObject.getString("type");
                                        String provideName = jsonObject.getString("nombre_proveedor");
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
                                            mins = (int) (mills % (1000*60*60)) / (1000 * 60);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (typeStr.equals("D")) {
                                            if (hours != 0) {
                                                AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_1, productID, code, reference, provideName, typeStr, dateString, hours + " hours ago"));
                                            } else {
                                                if (mins != 0) {
                                                    AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_1, productID, code, reference, provideName, typeStr, dateString, mins + " mins ago"));
                                                } else {
                                                    AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_1, productID, code, reference, provideName, typeStr, dateString, "Just"));
                                                }
                                            }
                                        } else {
                                            if (hours != 0) {
                                                AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_2, productID, code, reference, provideName, typeStr, dateString, hours + " hours ago"));
                                            } else {
                                                if (mins != 0) {
                                                    AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_2, productID, code, reference, provideName, typeStr, dateString, mins + " mins ago"));
                                                } else {
                                                    AppConstants.inventories.add(new Inventory(R.drawable.ic_inventory_2, productID, code, reference, provideName, typeStr, dateString, "Just"));
                                                }
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
                        rv_inventory.setVisibility(View.VISIBLE);
                        DashboardAdapter dashboardAdapter = new DashboardAdapter(this, AppConstants.inventories);
                        rv_inventory.setAdapter(dashboardAdapter);
                        dashboardAdapter.notifyDataSetChanged();
                    } else {
                        rv_inventory.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }, true).execute();
        } else {
            Toast.makeText(this,"No internet connection available", Toast.LENGTH_LONG).show();
        }
    }

    private void getSales() {
        if (CoreApp.isNetworkConnection(this)) {
            new SalesAPIService(this, result -> {
                if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                    AppConstants.sales.clear();
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getString("success").equals("true")) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int itemID = jsonObject.getInt("id");
                                    String code = jsonObject.getString("codigo");
                                    String clientName = jsonObject.getString("cliente_nombre");

                                    AppConstants.sales.add(new Sale(itemID, code, clientName));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (AppConstants.sales.size() > 0) {
                        Collections.reverse(AppConstants.sales);
                        rv_inventory.setVisibility(View.VISIBLE);
                        SaleAdapter saleAdapter = new SaleAdapter(this, AppConstants.sales);
                        rv_inventory.setAdapter(saleAdapter);
                        saleAdapter.notifyDataSetChanged();
                    } else {
                        rv_inventory.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(this, "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }, true).execute();
        } else {
            Toast.makeText(this,"No internet connection available", Toast.LENGTH_LONG).show();
        }
    }

    private void showMenu() {
        isShowedMenu = true;
        lin_menu_view.setVisibility(View.VISIBLE);
    }

    private void hideMenu() {
        isShowedMenu = false;
        lin_menu_view.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_menu: {
                if (isShowedMenu) {
                    hideMenu();
                } else {
                    showMenu();
                }
                break;
            }
            case R.id.lin_menu_view: {
                Log.d(TAG, "Dismiss menu view");
                hideMenu();
                break;
            }
            case R.id.tv_productos: {
                Log.d(TAG, "Productos");
                startActivity(new Intent(this, ProductsActivity.class));
                finish();
                break;
            }
            case R.id.tv_definir: {
                Toast.makeText(DashboardActivity.this, "This button is not used for now.", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.tv_orden: {
                hideMenu();
                if (AppConstants.currentState.equals("Orden")) {
                    AppConstants.currentState = " Despachos/entradas";
                } else {
                    AppConstants.currentState = "Orden";
                }

                showDashboard();
                break;
            }
            case R.id.tv_actualizar: {
                hideMenu();
                if (AppConstants.currentState.equals("Actualizar")) {
                    AppConstants.currentState = " Despachos/entradas";
                } else {
                    AppConstants.currentState = "Actualizar";
                }

                showDashboard();

                break;
            }
            case R.id.tv_company: {
                if (AppConstants.companyArr.size() > 0) {
                    CompanySelectClass cdd = new CompanySelectClass(DashboardActivity.this, tv_company);
                    cdd.show();
                } else {
                    Toast.makeText(DashboardActivity.this, "There is no company.", Toast.LENGTH_LONG).show();
                }
                break;
            }

            case R.id.img_Add: {
                AppConstants.isNewSale = true;
                startActivity(new Intent(this, ConsumptionActivity.class));
                break;
            }
        }
    }
}
