package com.adolfo.flexiologistics.apiServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.LoadingDialog;

public class GetInventoriesAPIService extends AsyncTask<Void, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private boolean flagProgress;
    private LoadingDialog loadingDialog;
    private OnResultReceived mListner;
    private int page, size;

    public interface OnResultReceived {
        void onResult(String result);
    }

    public GetInventoriesAPIService(Context mContext, int page, int size, OnResultReceived mListner, boolean flagProgress) {
        this.mContext = mContext;
        this.mListner = mListner;
        this.flagProgress = flagProgress;
        this.page = page;
        this.size = size;
    }

    public void show() {
        if (flagProgress) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
    }

    private void hide() {
        if (flagProgress  && loadingDialog != null) {
            loadingDialog.hide();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        show();
    }

    @Override
    protected String doInBackground(Void... unsued) {
        String response;
        try {
            String url = AppConstants.BaseURL + APIServices.GETINVENTORIES;
            Log.e("URL", url);
            response = APIServices.POSTWithTokenSGETInventories(url, page, size);
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
        hide();
        if (mListner != null) mListner.onResult(result);
    }
}
