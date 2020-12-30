package com.flutter_webview_plugin;

import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import java.util.HashMap;

import io.flutter.plugin.common.MethodChannel;

class WebAppInterface {

    private final String javaScriptChannelName;
    private final MethodChannel methodChannel;
    private final Handler platformThreadHandler;

    /**
     * @param channel the Flutter WebView method channel to which JS messages are sent
     * @param channelName the name of the JavaScript channel, this is sent over the method
     *     channel with each message to let the Dart code know which JavaScript channel the message
     *     was sent through
     */
    WebAppInterface(MethodChannel channel, String channelName, Handler handler) {
        platformThreadHandler = handler;
        methodChannel = channel;
        javaScriptChannelName = channelName;
    }

    /**
     * Interface method called from the loaded page
     * @param payload the payload sent from the javascript method on the loaded page
     */
    @JavascriptInterface
    public void getExternalAuth(final String payload) {
        Runnable postMessageRunnable =
                new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> arguments = new HashMap<>();
                        arguments.put("channel", javaScriptChannelName);
                        arguments.put("message", payload);
                        methodChannel.invokeMethod("javascriptChannelMessage", arguments);
                    }
                };
        if (platformThreadHandler.getLooper() == Looper.myLooper()) {
            postMessageRunnable.run();
        } else {
            platformThreadHandler.post(postMessageRunnable);
        }
    }
}