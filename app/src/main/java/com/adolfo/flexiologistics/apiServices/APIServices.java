package com.adolfo.flexiologistics.apiServices;

import android.annotation.SuppressLint;

import com.adolfo.flexiologistics.models.ConsumptionItem;
import com.adolfo.flexiologistics.models.DeliveryItem;
import com.adolfo.flexiologistics.utils.AppConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public class APIServices {

    public static final String RESPONSE_UNWANTED = "UNWANTED";
    public static final String LOGIN = "/login_master";
    public static final String GETINVENTORIES = "/dashboard_oc_ov";
    public static final String GETINVENTORIYITEM_D = "/listar_despacho_detalle";
    public static final String GETINVENTORIYITEM_P = "/listar_entrada_detalle";
    public static final String SENDINVENTORY_D = "/guardar_despacho";
    public static final String SENDINVENTORY_P = "/guardar_entrada";
    public static final String SALES = "/obtener_listado_orden_venta";
    public static final String SALEDETAIL = "/obtener_detalle_orden_venta";
    public static final String SALES_ITEMS = "/obtener_listado_items";
    public static final String SEND_SALE = "/guardar_orden_trabajo";
    public static final String CREATE_TRABAJO = "/crear_orden_trabajo";

    private static final String LINE_FEED = "\r\n";
    private static final String twoHyphens = "--";

    public static String POSTWithTokenSGETInventories(String url, int page, int size) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("token", AppConstants.token);
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"company_id\"" + LINE_FEED + LINE_FEED
                + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyID() + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"page\"" + LINE_FEED + LINE_FEED
                + page + LINE_FEED);


        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"page_item_size\"" + LINE_FEED + LINE_FEED
                + size + LINE_FEED);

        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTWithTokenGETSales(String url) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("token", AppConstants.token);
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"empresa_id\"" + LINE_FEED + LINE_FEED
                + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyID() + LINE_FEED);

        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTWithTokenGETSaleItmes(String url, int page) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("token", AppConstants.token);
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"empresa_id\"" + LINE_FEED + LINE_FEED
                + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyID() + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"paginate\"" + LINE_FEED + LINE_FEED
                + 100 + LINE_FEED);


        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"current_page\"" + LINE_FEED + LINE_FEED
                + page + LINE_FEED);

        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTWithTokenSENDINVENTORY(String url, String productID, String typeStr, String currentDateStr) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("token", AppConstants.token);
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"id\"" + LINE_FEED + LINE_FEED
                + productID + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"type\"" + LINE_FEED + LINE_FEED
                + typeStr + LINE_FEED);

        if (typeStr.equals("D")) {
            writer.write(twoHyphens + boundary + LINE_FEED);
            writer.write("Content-Disposition: form-data; name=\"fecha_despacho_pendiente\"" + LINE_FEED + LINE_FEED
                    + currentDateStr + LINE_FEED);

            writer.write(twoHyphens + boundary + LINE_FEED);
            writer.write("Content-Disposition: form-data; name=\"estado\"" + LINE_FEED + LINE_FEED
                    + "por_empacar" + LINE_FEED);
        } else {
            writer.write(twoHyphens + boundary + LINE_FEED);
            writer.write("Content-Disposition: form-data; name=\"estado\"" + LINE_FEED + LINE_FEED
                    + "por_recibir" + LINE_FEED);
        }

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"empresa_id\"" + LINE_FEED + LINE_FEED
                + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyID() + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"updated_by\"" + LINE_FEED + LINE_FEED
                + AppConstants.userID + LINE_FEED);

        List<DeliveryItem> deliveryItems = new ArrayList<>();
        if (AppConstants.deliveryItems.size() > 0) {
            for (DeliveryItem deliveryItem : AppConstants.deliveryItems) {
                if (deliveryItem.getCount() > 0) {
                    deliveryItems.add(deliveryItem);
                }
            }
        }

        if (deliveryItems.size() > 0) {
            for (int i =0; i<deliveryItems.size(); i++) {
                DeliveryItem deliveryItem = deliveryItems.get(i);
                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][id]\""+ LINE_FEED + LINE_FEED
                        + deliveryItem.getId() + LINE_FEED);
                if (typeStr.equals("D")) {
                    writer.write(twoHyphens + boundary + LINE_FEED);
                    writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][cantidad_empacar]\"" + LINE_FEED + LINE_FEED
                            + deliveryItem.getCount() + LINE_FEED);
                    writer.write(twoHyphens + boundary + LINE_FEED);
                    writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][cantidad_empacada]\"" + LINE_FEED + LINE_FEED
                            + deliveryItem.getPacked() + LINE_FEED);
                } else {
                    writer.write(twoHyphens + boundary + LINE_FEED);
                    writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][cantidad_recibir]\"" + LINE_FEED + LINE_FEED
                            + deliveryItem.getCount() + LINE_FEED);
                    writer.write(twoHyphens + boundary + LINE_FEED);
                    writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][cantidad_recibida]\"" + LINE_FEED + LINE_FEED
                            + deliveryItem.getPacked() + LINE_FEED);
                }
                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][bodega_id]\"" + LINE_FEED + LINE_FEED
                        + deliveryItem.getWarehouseID() + LINE_FEED);

                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][line_item_id]\"" + LINE_FEED + LINE_FEED
                        + deliveryItem.getLineItemID() + LINE_FEED);

                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][item_id]\"" + LINE_FEED + LINE_FEED
                        + Integer.valueOf(deliveryItem.getItemID()) + LINE_FEED);

                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][unidad_id]\"" + LINE_FEED + LINE_FEED
                        + deliveryItem.getUnidadID() + LINE_FEED);

                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"entrada_items[" + i + "][cantidad]\"" + LINE_FEED + LINE_FEED
                        + deliveryItem.getCount() + LINE_FEED);
            }
        }

        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }


    public static String POSTWithTokenSendSale(String url, int productID, String serverName, String code, String currentDateStr, String currentTimeStr) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("token", AppConstants.token);
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"empresa_id\"" + LINE_FEED + LINE_FEED
                + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyID() + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"fecha_inicio\"" + LINE_FEED + LINE_FEED
                + currentDateStr + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"fecha_planificada_fin\"" + LINE_FEED + LINE_FEED
                + currentDateStr + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"hora_inicio\"" + LINE_FEED + LINE_FEED
                + currentTimeStr + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"hora_planificada_fin\"" + LINE_FEED + LINE_FEED
                + currentTimeStr + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"usuario_id\"" + LINE_FEED + LINE_FEED
                + AppConstants.userID + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"empezable_type\"" + LINE_FEED + LINE_FEED
                + "orden_venta" + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"empezable_id\"" + LINE_FEED + LINE_FEED
                + productID + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"tipo_orden_id\"" + LINE_FEED + LINE_FEED
                + 1 + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"asignado\"" + LINE_FEED + LINE_FEED
                + serverName + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"asignable_id\"" + LINE_FEED + LINE_FEED
                + productID + LINE_FEED);

        if (AppConstants.consumptionItems.size() > 0) {
            for (int i =0; i<AppConstants.consumptionItems.size(); i++) {
                ConsumptionItem consumptionItem = AppConstants.consumptionItems.get(i);
                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"items[" + i + "][categoria_id]\""+ LINE_FEED + LINE_FEED
                        + consumptionItem.getCategoryID() + LINE_FEED);

                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"items[" + i + "][item_id]\"" + LINE_FEED + LINE_FEED
                        + consumptionItem.getItemID() + LINE_FEED);
                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"items[" + i + "][atributo_text]\"" + LINE_FEED + LINE_FEED
                        + consumptionItem.getAtributo() + LINE_FEED);

                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"filas[" + i + "][cantidad]\"" + LINE_FEED + LINE_FEED
                        + consumptionItem.getCantidad() + LINE_FEED);

                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"filas[" + i + "][unidad_id]\"" + LINE_FEED + LINE_FEED
                        + consumptionItem.getUnidadID() + LINE_FEED);

                writer.write(twoHyphens + boundary + LINE_FEED);
                writer.write("Content-Disposition: form-data; name=\"filas[" + i + "][bodega_id]\"" + LINE_FEED + LINE_FEED
                        + consumptionItem.getBodegaID() + LINE_FEED);
            }
        }

        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTWithTokenDetailSale(String url, int saleID) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("token", AppConstants.token);
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"empresa_id\"" + LINE_FEED + LINE_FEED
                + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyID() + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"orden_venta_id\"" + LINE_FEED + LINE_FEED
                + saleID + LINE_FEED);

        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTWithTokenCreateTrabajo(String url, String serverName, String code, String currentDateStr, String currentTimeStr) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("token", AppConstants.token);
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"empresa_id\"" + LINE_FEED + LINE_FEED
                + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyID() + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"nombre_cliente\"" + LINE_FEED + LINE_FEED
                + serverName + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"centro_contable\"" + LINE_FEED + LINE_FEED
                + serverName + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"fecha_inicio\"" + LINE_FEED + LINE_FEED
                + currentDateStr + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"fecha_planificada_inicio\"" + LINE_FEED + LINE_FEED
                + currentDateStr + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"hora_inicio\"" + LINE_FEED + LINE_FEED
                + currentTimeStr + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"hora_planificada_inicio\"" + LINE_FEED + LINE_FEED
                + currentTimeStr + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"usuario_id\"" + LINE_FEED + LINE_FEED
                + AppConstants.userID + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"nombre_equipo_trabajo\"" + LINE_FEED + LINE_FEED
                + serverName + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"serie\"" + LINE_FEED + LINE_FEED
                + code + LINE_FEED);

        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"archivo_nombre\"" + LINE_FEED + LINE_FEED
                + "XLS-1" + LINE_FEED);

        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTWithTokenINTENTORYITEM(String url, String orderID, String typeStr) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("token", AppConstants.token);
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"order_id\"" + LINE_FEED + LINE_FEED
                + orderID + LINE_FEED);

        if (typeStr.equals("D")) {
            writer.write(twoHyphens + boundary + LINE_FEED);
            writer.write("Content-Disposition: form-data; name=\"empresa_id\"" + LINE_FEED + LINE_FEED
                    + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyID() + LINE_FEED);
        } else {
            writer.write(twoHyphens + boundary + LINE_FEED);
            writer.write("Content-Disposition: form-data; name=\"company_id\"" + LINE_FEED + LINE_FEED
                    + AppConstants.companyArr.get(AppConstants.selectedCompany).getCompanyID() + LINE_FEED);
        }

        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTWithURL(String url, String emailStr, String passwordStr) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.addRequestProperty("Accept", "application/json");
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        //first parameter - email
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"email\"" + LINE_FEED + LINE_FEED
                + emailStr + LINE_FEED);

        //second parameter - passwordStr
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"password\"" + LINE_FEED + LINE_FEED
                + passwordStr + LINE_FEED);
        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    private static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {

                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {

                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
