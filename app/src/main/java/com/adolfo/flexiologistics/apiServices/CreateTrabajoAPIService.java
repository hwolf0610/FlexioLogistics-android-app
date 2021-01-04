package com.adolfo.flexiologistics.apiServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.LoadingDialog;

public class CreateTrabajoAPIService extends AsyncTask<Void, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private String currentDateStr, currentTimeStr, serverName, code;
    private boolean flagProgress;
    private LoadingDialog loadingDialog;
    private OnResultReceived mListner;

    public interface OnResultReceived {
        void onResult(String result);
    }

    public CreateTrabajoAPIService(Context mContext, String serverName, String code, String currentDateStr, String currentTimeStr, OnResultReceived mListner, boolean flagProgress) {
        this.mContext = mContext;;
        this.serverName = serverName;
        this.code = code;
        this.currentTimeStr = currentTimeStr;
        this.currentDateStr = currentDateStr;
        this.mListner = mListner;
        this.flagProgress = flagProgress;
    }

    public void show() {
        if (flagProgress) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
    }

    private void hide() {
        if (flagProgress) {
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
            String url = AppConstants.BaseURL + APIServices.CREATE_TRABAJO;
            Log.e("URL", url);
            response = APIServices.POSTWithTokenCreateTrabajo(url, serverName, code, currentDateStr, currentTimeStr);
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
