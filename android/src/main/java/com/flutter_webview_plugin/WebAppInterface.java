package com.flutter_webview_plugin;

import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

class WebAppInterface {

    private Handler uiThreadHandler = new Handler(Looper.getMainLooper());

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void showToast(String toast) {
        //Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void getExternalAuth(final String payload) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(payload);
                    if (json != null) {
                        boolean force = false;
                        if (json.has("force")) {
                            force = json.getBoolean("force");
                        }
                        FlutterWebviewPlugin.channel.invokeMethod("getExternalAuth", force);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(mContext, "Error handling external auth method.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}