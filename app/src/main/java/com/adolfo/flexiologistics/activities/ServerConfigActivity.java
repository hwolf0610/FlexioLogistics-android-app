package com.adolfo.flexiologistics.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.apiServices.APIServices;
import com.adolfo.flexiologistics.apiServices.CheckAPIService;
import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.CoreApp;
import com.adolfo.flexiologistics.utils.Preferences;

import org.json.JSONException;
import org.json.JSONObject;


public class ServerConfigActivity extends Activity implements View.OnClickListener {

    private LinearLayout lin_back;
    private EditText edt_server;
    private TextView tv_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_config);

        initView();
    }

    private void initView() {
        lin_back = findViewById(R.id.lin_back);
        edt_server = findViewById(R.id.edt_server);
        tv_save = findViewById(R.id.tv_save);

        lin_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save :
                if (edt_server.getText().toString().equals("")) {
                    Toast.makeText(this, "Introduzca el número o nombre del servidor", Toast.LENGTH_LONG).show();
                    return;
                }

                String serverUrl = AppConstants.httpURL + edt_server.getText().toString() + AppConstants.APIURL + "/login_master";
                if (CoreApp.isNetworkConnection(this)) {
                    new CheckAPIService(this, serverUrl, "Test", "Test", result -> {
                        if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("success").equals("true")) {
                                    Preferences.setValue(this, Preferences.SERVER_URL, AppConstants.httpURL + edt_server.getText().toString() + AppConstants.APIURL);
                                    finish();
                                } else {
                                    Preferences.setValue(this, Preferences.SERVER_URL, AppConstants.httpURL + edt_server.getText().toString() + AppConstants.APIURL);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(this, "Por favor introduzca el servidor correcto", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "Por favor introduzca el servidor correcto", Toast.LENGTH_LONG).show();
                        }
                    }, true).execute();
                } else {
                    Toast.makeText(this, "No internet connection available", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.lin_back:
                finish();
                break;
        }
    }
}
