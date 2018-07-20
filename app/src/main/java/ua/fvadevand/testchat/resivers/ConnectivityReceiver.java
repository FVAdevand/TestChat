package ua.fvadevand.testchat.resivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ua.fvadevand.testchat.utilities.Utils;

public class ConnectivityReceiver extends BroadcastReceiver {

    private OnConnectedWIFIListener mListener;

    public ConnectivityReceiver(OnConnectedWIFIListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mListener != null) {
            mListener.onConnectedWIFI(Utils.isWifiConnected(context.getApplicationContext()));
        }
    }

    public interface OnConnectedWIFIListener {
        void onConnectedWIFI(boolean isConnected);
    }
}
