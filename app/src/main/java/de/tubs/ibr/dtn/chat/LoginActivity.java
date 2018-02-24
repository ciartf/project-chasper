package de.tubs.ibr.dtn.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import constant.GeneralData;
import constant.GlobalData;
import dao.MessageDestination;
import dao.User;
import dataaccess.MessageDestinationDataAccess;
import dataaccess.UserDataAccess;
import http.HttpClient;
import tool.Tool;
import requestresponse.GsonHelper;
import requestresponse.TokenRequest;
import requestresponse.TokenResponse;
import tool.WiFiConnection;

import static constant.GlobalData.token;
import static constant.GlobalData.url;

/**
 * Created by danu on 6/2/17.
 */

public class LoginActivity extends FragmentActivity {
    Button btnLogin;
    EditText edtUserId;
    EditText edtPassword;
    CheckBox checkShowPassword;
    Context context;
    public static WiFiConnection wiFiConnection = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        try{
            //adding from me
            wiFiConnection = new WiFiConnection(getApplicationContext(), GlobalData.wifiSSID, GlobalData.wifiPass);
            if(!wiFiConnection.isConnected()){
                wiFiConnection.connect();
                Thread.sleep(2000);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        context = getApplicationContext();
        edtUserId = (EditText) findViewById(R.id.editUserId);
        edtPassword = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        checkShowPassword = (CheckBox) findViewById(R.id.checkShowPassword);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, Boolean>() {
                    private ProgressDialog progressDialog;

                    @Override
                    protected void onPreExecute() {

                        progressDialog = ProgressDialog.show(LoginActivity.this,
                                "", getString(R.string.progressWait), true);

                    }

                    @Override
                    protected Boolean doInBackground(String... params) {
                        if(params[0].equalsIgnoreCase(GlobalData.mqttUser)){
                            saveAuthenticatedUser(params[0], params[0], params[0]);
                            return true;
                        }
                        else if(params[0] != null && params[1] != null){
                            try{
                                if(wiFiConnection.isConnected()){
                                    wiFiConnection.disconnect();
                                    Thread.sleep(500);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }


                            //TODO: request response token
                            //marshall request now
                            TokenRequest tokenRequest = new TokenRequest();
                            tokenRequest.setUser(params[0]);
                            tokenRequest.setLabel(GlobalData.defaultLoginLabel);
                            tokenRequest.setSecretkey(params[1]);
                            TokenResponse tokenResponse;
                            String jsonRequest = GsonHelper.toJson(tokenRequest);
                            String jsonResponse = null;
                            HttpClient httpClient = new HttpClient();
                            if(Tool.isInternetConnected(context)){
                                try {
                                    jsonResponse = httpClient.postMessage(jsonRequest, url+token);
                                    if(jsonResponse != null){
                                        //unmarshall now
                                        tokenResponse = GsonHelper.fromJson(jsonResponse, TokenResponse.class);
                                        saveAuthenticatedUser(params[0], params[1], tokenResponse.getToken());

                                        return true;
                                    }else{
                                        return false;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return false;
                                }
                            }else{
                                return false;
                            }
                        }
                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean continued) {
                        super.onPostExecute(continued);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e) {
                            }
                        }
                        if(continued) {
//                            Toast.makeText(context, "Request Token Success", Toast.LENGTH_LONG).show();


                            goToMain();



                        }else{
                            Toast.makeText(context, "Request Token Error", Toast.LENGTH_LONG).show();
                        }
                    }

                }.execute(edtUserId.getText().toString(), edtPassword.getText().toString());
            }
        });

        checkShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtPassword.setTransformationMethod(null);
                } else {
                    edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isHaveAccess()){
            goToMain();
        }
    }

    private Boolean isHaveAccess(){
        User authenticatedUser;
        authenticatedUser = UserDataAccess.get(context);
        if(authenticatedUser != null){
            GeneralData.setUuidUser(authenticatedUser.getUuid_user());
            GeneralData.setToken(authenticatedUser.getToken());
            GeneralData.setUserId(authenticatedUser.getUser_id());
            GeneralData.setPassword(authenticatedUser.getPassword());
            GeneralData.setPresenceHackFlag(true);
//            List<MessageDestination> messageDestinationList = MessageDestinationDataAccess.getAll(context);
//            GeneralData.setBuddyEndpointMap(new HashMap<String, Long>());
//            if(messageDestinationList != null || messageDestinationList.size() != 0){
//                for(MessageDestination messageDestination : messageDestinationList){
//                    if(messageDestination.getBuddy_id() != null && !messageDestination.getBuddy_id().equalsIgnoreCase("")){
//                        GeneralData.getBuddyEndpointMap().put(messageDestination.getDestination(),
//                                Long.parseLong(messageDestination.getBuddy_id()));
//                    }else{
//                        GeneralData.getBuddyEndpointMap().put(messageDestination.getDestination(), null);
//                    }
//                }
//            }
            return true;
        }else{
            return false;
        }
    }

    private void goToMain(){
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    private void saveAuthenticatedUser(String userId, String password, String token){
        User authenticatedUser = new User();
        String uuid = Tool.getUUID();
        authenticatedUser.setUser_id(userId);
        authenticatedUser.setUuid_user(uuid);
        authenticatedUser.setPassword(password);
        authenticatedUser.setToken("JWT "+token);
        GeneralData.setUuidUser(authenticatedUser.getUuid_user());
        GeneralData.setToken(authenticatedUser.getToken());
        GeneralData.setUserId(authenticatedUser.getUser_id());
        GeneralData.setPassword(authenticatedUser.getPassword());
        GeneralData.setPresenceHackFlag(true);
//        List<MessageDestination> messageDestinationList = MessageDestinationDataAccess.getAll(context);
//        GeneralData.setBuddyEndpointMap(new HashMap<String, Long>());
//        if(messageDestinationList != null || messageDestinationList.size() != 0){
//            for(MessageDestination messageDestination : messageDestinationList){
//                if(messageDestination.getBuddy_id() != null && !messageDestination.getBuddy_id().equalsIgnoreCase("")){
//                    GeneralData.getBuddyEndpointMap().put(messageDestination.getDestination(),
//                            Long.parseLong(messageDestination.getBuddy_id()));
//                }else{
//                    GeneralData.getBuddyEndpointMap().put(messageDestination.getDestination(), null);
//                }
//            }
//        }

        UserDataAccess.add(context, authenticatedUser);
    }

}
