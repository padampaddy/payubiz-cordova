package com.espranza.cordova.payubiz;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;


public class PayUBizCordova extends CordovaPlugin {

    private static final String TAG = "PayUBizCordovaPlugin";
    private CallbackContext mCallbackContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.i(TAG, "execute(): " + action + ", args = " + args);

        switch (action) {
            case "showPaymentView":
                Log.d(TAG, "to showPaymentView()");
                final JSONObject paymentJsonObject = args.getJSONObject(0);
                cordova.getActivity().runOnUiThread(() -> launchPayUMoneyFlow(paymentJsonObject, callbackContext));
                return true;
        }

        return false;
    }


    private void launchPayUMoneyFlow(final JSONObject jsonObject, final CallbackContext callbackContext) {
        mCallbackContext = callbackContext;
        Log.d(TAG, "launchPayUMoneyFlow(): " + jsonObject);
        Intent i = new Intent(cordova.getActivity(), MainActivity.class);
        Gson gson = new Gson();
        i.putExtra("orderDetails", gson.toJson(jsonObject));
        cordova.startActivityForResult(this,i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            switch (resultCode) {
                case 1:
                    if (mCallbackContext != null) {
                        JSONObject json = new JSONObject();
                        Bundle bundle = intent.getBundleExtra("result");
                        Set<String> keys = bundle.keySet();
                        for (String key : keys) {
                            try {
                                json.put(key, JSONObject.wrap(bundle.get(key)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mCallbackContext.error(e.getMessage());
                            }
                        }
                        mCallbackContext.success(json);
                    }

                    break;
                case 2:
                    if (mCallbackContext != null) {
                        mCallbackContext.error(intent.getStringExtra("result"));
                    }
                    break;
                case 3:
                    if (mCallbackContext != null) {
                        mCallbackContext.error("Transaction Cancelled.");
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);

    }
}



