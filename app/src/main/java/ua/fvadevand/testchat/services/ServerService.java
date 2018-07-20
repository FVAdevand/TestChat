package ua.fvadevand.testchat.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.net.Socket;

import ua.fvadevand.testchat.Const;
import ua.fvadevand.testchat.chat.Server;

public class ServerService extends Service implements Server.OnServerStartListener {

    private PendingIntent mPendingIntent;
    private boolean mIsServer;
    private Server mServer;
    private IBinder mBinder = new ServiceBinder();
    private Socket mClientSocket;

    public ServerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            mPendingIntent = intent.getParcelableExtra(Const.KEY_PENDING_INTENT);
            mIsServer = intent.getBooleanExtra(Const.KEY_IS_SERVER, false);
            final String hostName = intent.getStringExtra(Const.KEY_HOST_NAME);

            if (mIsServer) {
                mServer = new Server(hostName, this);
                mServer.start();
            } else {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket(hostName, Const.SERVER_PORT);
                            onJoinClient(socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onServerStart(String host) {
        try {
            Intent intent = new Intent();
            intent.putExtra(Const.KEY_HOST_NAME, host);
            mPendingIntent.send(ServerService.this, Const.CODE_START_SERVER, intent);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onJoinClient(Socket client) {
        mClientSocket = client;
        sendRequestConnectClient();
    }

    private void sendRequestConnectClient() {
        try {
            mPendingIntent.send(ServerService.this, Const.CODE_CONNECT_CLIENT, null);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIsServer) {
            mServer.close();
        }
    }

    public Socket getSocket() {
        return mClientSocket;
    }

    public class ServiceBinder extends Binder {
        public ServerService getService() {
            return ServerService.this;
        }
    }
}
