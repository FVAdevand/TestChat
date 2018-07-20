package ua.fvadevand.testchat.chat;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import ua.fvadevand.testchat.Const;

public class Server extends Thread {

    private String mHostName;
    private ServerSocket mServerSocket;
    private OnServerStartListener mListener;

    public Server(String hostName, OnServerStartListener listener) {
        mHostName = hostName;
        mListener = listener;
    }

    private void runServer() {
        try {
            InetAddress inetAddress = InetAddress.getByName(mHostName);
            mServerSocket = new ServerSocket(Const.SERVER_PORT, 0, inetAddress);
            if (mListener != null) {
                mListener.onServerStart(getHost());
            }

            Socket client = mServerSocket.accept();
            if (mListener != null) {
                mListener.onJoinClient(client);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        runServer();
    }

    private String getHost() {
        return mServerSocket.getInetAddress().getHostName();
    }

    public void close() {
        if (mServerSocket != null && !mServerSocket.isClosed()) {
            try {
                mServerSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mServerSocket = null;
    }

    public interface OnServerStartListener {
        void onServerStart(String host);

        void onJoinClient(Socket client);
    }
}
