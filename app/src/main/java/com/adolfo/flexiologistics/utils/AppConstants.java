package com.adolfo.flexiologistics.utils;

import android.device.ScanDevice;

import com.adolfo.flexiologistics.models.Company;
import com.adolfo.flexiologistics.models.ConsumptionItem;
import com.adolfo.flexiologistics.models.DeliveryItem;
import com.adolfo.flexiologistics.models.Inventory;
import com.adolfo.flexiologistics.models.Sale;

import java.util.ArrayList;
import java.util.List;

public class AppConstants {

    public static String httpURL = "http://";
    public static String APIURL = "/clientes/flexio/API_Flexio";
    public static String BaseURL = ""; // http://192.237.163.118/clientes/flexio/API_Flexio/
    public static String token = "";
    public static String currentUserName = "";
    public static String userID = "";
    public static List<Company> companyArr = new ArrayList<>();

    public static ScanType scanType;
    public static int pos;
    public static boolean isNewSale = false;
    public static int selectedSale;
    public static int selectedCompany;
    public static int barcodePos;
    public static String currentState = "Despachos/entradas";

    public static List<Inventory> inventories = new ArrayList<>();
    public static List<Sale> sales = new ArrayList<>();
    public static List<DeliveryItem> deliveryItems = new ArrayList<>();
    public static List<ConsumptionItem> consumptionItems = new ArrayList<>();
    public static ArrayList<Sale> saleItmes = new ArrayList<>();
    public static ScanDevice sm;
    public final static String SCAN_ACTION = "scan.rcv.message";
}
