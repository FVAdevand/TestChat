package ua.fvadevand.testchat.chat;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import ua.fvadevand.testchat.Const;
import ua.fvadevand.testchat.utilities.CryptUtils;

public class ConnectedClient extends Thread {

    private static final String LOG_TAG = ConnectedClient.class.getSimpleName();
    private final CryptUtils mCryptUtils;
    private Socket mSocket;
    private boolean mIsRunning;
    private boolean mIsHasSecret;
    private OnConnectedClientListener mListener;
    private DataInputStream mInput;
    private DataOutputStream mOut;

    public ConnectedClient(Socket socket, OnConnectedClientListener listener) {
        mIsRunning = true;
        mSocket = socket;
        mListener = listener;
        mCryptUtils = new CryptUtils();
    }

    @Override
    public void run() {
        super.run();
        try {

            mInput = new DataInputStream(mSocket.getInputStream());
            mOut = new DataOutputStream(mSocket.getOutputStream());

            sendCommand(Const.COMMAND_PUBLIC_KEY + Const.SEPARATOR + mCryptUtils.getUserPkStr());

            while (mIsRunning) {
                String message = null;
                try {
                    message = mInput.readUTF();
                } catch (IOException e) {
                    Log.i(LOG_TAG, "run: " + e);
                    e.printStackTrace();
                }

                if (message != null) {
                    if (hasCommand(message)) {
                        continue;
                    }
                    if (mListener != null) {
                        Log.i(LOG_TAG, "run: " + message);
                        mListener.onReceiveMessage(mCryptUtils.decript(message));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasCommand(String message) {
        if (message.contains(Const.COMMAND_CONNECTION_CLOSED)) {
            mIsRunning = false;
            if (mListener != null) {
                mListener.onConnectionClosed();
            }
            return true;
        } else if (message.contains(Const.COMMAND_PUBLIC_KEY)) {
            String publicKey = message.substring(message.indexOf(Const.SEPARATOR) + Const.SEPARATOR.length());
            Log.i(LOG_TAG, "hasCommand: " + message);
            Log.i(LOG_TAG, "hasCommand: " + publicKey);
            mCryptUtils.generateSecret(publicKey);
            mIsHasSecret = true;
            return true;
        }
        return false;
    }

    public void close() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mIsRunning) {
                        mIsRunning = false;
                        mOut.writeUTF(Const.COMMAND_CONNECTION_CLOSED);
                    }
                    mOut.close();
                    mInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mSocket = null;
            }
        });
        thread.start();
    }

    public void sendMessage(final String text) {
        if (!mIsHasSecret) return;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mOut.writeUTF(mCryptUtils.encript(text));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void sendCommand(final String text) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mOut.writeUTF(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public interface OnConnectedClientListener {
        void onReceiveMessage(String messageText);

        void onConnectionClosed();
    }
}
