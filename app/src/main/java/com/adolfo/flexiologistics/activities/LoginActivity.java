package com.adolfo.flexiologistics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adolfo.flexiologistics.R;
import com.adolfo.flexiologistics.apiServices.APIServices;
import com.adolfo.flexiologistics.apiServices.LoginAPIService;
import com.adolfo.flexiologistics.models.Company;
import com.adolfo.flexiologistics.utils.AppConstants;
import com.adolfo.flexiologistics.utils.CoreApp;
import com.adolfo.flexiologistics.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText edt_user, edt_password;
    private ImageView img_server;
    private TextView tv_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        edt_user = findViewById(R.id.edt_user);
        edt_password = findViewById(R.id.edt_password);
        img_server = findViewById(R.id.img_server);
        tv_error = findViewById(R.id.tv_error);
        TextView tv_login = findViewById(R.id.tv_login);

        edt_user.setText("rrosas@pensanomica.com");
        edt_password.setText("12345");
        tv_login.setOnClickListener(this);
        img_server.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login :
                if (edt_user.getText().toString().equals("")) {
                    Toast.makeText(this, "Please enter the User ID", Toast.LENGTH_LONG).show();
                    return;
                }
                if (edt_password.getText().toString().equals("")) {
                    Toast.makeText(this, "Please enter the Password", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Preferences.getValue_String(this, Preferences.SERVER_URL).equals("")) {
                    tv_error.setVisibility(View.VISIBLE);
                    return;
                }

                AppConstants.BaseURL = Preferences.getValue_String(this, Preferences.SERVER_URL);

                if (CoreApp.isNetworkConnection(this)) {
                    new LoginAPIService(this, edt_user.getText().toString(), edt_password.getText().toString(), result -> {
                        if (!result.equals(APIServices.RESPONSE_UNWANTED)) {
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("success").equals("true")) {
                                    Log.d(TAG, "Log In");
                                    String dataStr = object.getString("data");
                                    JSONObject dataObject = new JSONObject(dataStr);
                                    AppConstants.token = dataObject.getString("api_token");
                                    JSONObject userObject = dataObject.getJSONObject("user");
                                    AppConstants.userID = userObject.getString("id");
                                    AppConstants.currentUserName = userObject.getString("nombre") + " " + userObject.getString("apellido");
                                    JSONArray jsonArray = dataObject.getJSONArray("empresas");
                                    AppConstants.companyArr.clear();
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject companyObject = jsonArray.getJSONObject(i);
                                            AppConstants.companyArr.add(new Company(companyObject.getString("empresa_id"), companyObject.getString("nombre_empresa")));
                                        }
                                    }
                                    startActivity(new Intent(this, DashboardActivity.class));
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
                    Toast.makeText(this, "No internet connection available", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.img_server:
                startActivity(new Intent(this, ServerConfigActivity.class));
                break;
        }
    }
}
