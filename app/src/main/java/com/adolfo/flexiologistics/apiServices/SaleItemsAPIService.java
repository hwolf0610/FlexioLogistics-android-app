package com.adolfo.flexiologistics.apiServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.LoadingDialog;

public class SaleItemsAPIService extends AsyncTask<Void, Void, String>  {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private int page;
    private boolean flagProgress;
    private LoadingDialog loadingDialog;
    private OnResultReceived mListner;

    public interface OnResultReceived {
        void onResult(String result);
    }

    public SaleItemsAPIService(Context mContext, int page, OnResultReceived mListner, boolean flagProgress) {
        this.mContext = mContext;
        this.mListner = mListner;
        this.flagProgress = flagProgress;
        this.page = page;
    }

//    public void show() {
//        if (flagProgress) {
//            loadingDialog = new LoadingDialog(mContext, false);
//        }
//    }
//
//    private void hide() {
//        if (flagProgress  && loadingDialog != null) {
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
            String url = AppConstants.BaseURL + APIServices.SALES_ITEMS;
            Log.e("URL", url);
            response = APIServices.POSTWithTokenGETSaleItmes(url, page);
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
