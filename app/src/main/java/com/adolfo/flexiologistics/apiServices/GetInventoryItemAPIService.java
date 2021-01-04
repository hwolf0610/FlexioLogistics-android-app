package com.adolfo.flexiologistics.apiServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.LoadingDialog;

public class GetInventoryItemAPIService extends AsyncTask<Void, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private String orderID, typeStr;
    private boolean flagProgress;
    private LoadingDialog loadingDialog;
    private OnResultReceived mListner;

    public interface OnResultReceived {
        void onResult(String result);
    }

    public GetInventoryItemAPIService(Context mContext, String orderID, String typeStr, OnResultReceived mListner, boolean flagProgress) {
        this.mContext = mContext;
        this.orderID = orderID;
        this.typeStr = typeStr;
        this.mListner = mListner;
        this.flagProgress = flagProgress;
    }

//    public void show() {
//        if (flagProgress) {
//            loadingDialog = new LoadingDialog(mContext, false);
//        }
//    }
//
//    private void hide() {
//        if (flagProgress) {
//            loadingDialog.hide();
//        }
//    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        show();
    }

    @Override
    protected String doInBackground(Void... unsued) {
        String response;
        try {
            String url;
            if (typeStr.equals("D")) {
                url = AppConstants.BaseURL + APIServices.GETINVENTORIYITEM_D;
            } else {
                url = AppConstants.BaseURL + APIServices.GETINVENTORIYITEM_P;
            }
            Log.e("URL", url);
            response = APIServices.POSTWithTokenINTENTORYITEM(url, orderID, typeStr);
            Log.e(TAG, " Response: " + response);
        } catch (Exception e) {
            Log.e(TAG, "Error:... " + e.getMessage());
            response = APIServices.RESPONSE_UNWANTED;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
//        hide();
        if (mListner != null) mListner.onResult(result);
    }
}
