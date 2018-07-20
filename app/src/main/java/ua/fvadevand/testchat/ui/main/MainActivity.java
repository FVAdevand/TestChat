package ua.fvadevand.testchat.ui.main;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.SocketException;

import ua.fvadevand.testchat.Const;
import ua.fvadevand.testchat.R;
import ua.fvadevand.testchat.services.ServerService;
import ua.fvadevand.testchat.ui.message.MessageActivity;
import ua.fvadevand.testchat.utilities.Utils;

public class MainActivity extends AppCompatActivity
        implements StartFragment.OnStartInteractionListener,
        JoinChatFragment.OnEnterHostNameListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String TAG_CREATE_CHAT_FRAGMENT = "CREATE_CHAT_FRAGMENT";
    private static final String TAG_JOIN_CHAT_FRAGMENT = "JOIN_CHAT_FRAGMENT";
    private static final int REQUEST_CODE_SERVER_SERVICE = 217;
    private static final int REQUEST_CODE_MESSAGE_ACTIVITY = 789;
    private Intent mServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: check
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, new StartFragment())
                    .commit();
        }
    }

    @Override
    public void onCreateChat() {
        try {
            String hostName = Utils.getLocalIpAddress();
            startChatService(true, hostName);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onJoinChat() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, new JoinChatFragment())
                .addToBackStack(TAG_JOIN_CHAT_FRAGMENT)
                .commit();
    }

    @Override
    public void onEnterHostName(String hostName) {
        startChatService(false, hostName);
    }

    private void startChatService(boolean isServer, String hostName) {
        PendingIntent pendingIntent = createPendingResult(REQUEST_CODE_SERVER_SERVICE, new Intent(), 0);
        mServiceIntent = new Intent(this, ServerService.class);
        mServiceIntent.putExtra(Const.KEY_PENDING_INTENT, pendingIntent);
        mServiceIntent.putExtra(Const.KEY_IS_SERVER, isServer);
        mServiceIntent.putExtra(Const.KEY_HOST_NAME, hostName);
        startService(mServiceIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SERVER_SERVICE) {
            switch (resultCode) {
                case Const.CODE_START_SERVER:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment_container, CreateChatFragment.newInstance(data.getStringExtra(Const.KEY_HOST_NAME)))
                            .addToBackStack(TAG_CREATE_CHAT_FRAGMENT)
                            .commit();
                    break;
                case Const.CODE_CONNECT_CLIENT:
                    Log.i(LOG_TAG, "onActivityResult: " + Thread.currentThread().getName());
                    Intent activityIntent = new Intent(this, MessageActivity.class);
                    activityIntent.putExtra(Const.KEY_IS_SERVER, true);
                    startActivityForResult(activityIntent, REQUEST_CODE_MESSAGE_ACTIVITY);
                    break;
            }
        } else if (requestCode == REQUEST_CODE_MESSAGE_ACTIVITY) {
            getSupportFragmentManager().popBackStack();
            stopServerService();
        }
    }

    private void stopServerService() {
        stopService(mServiceIntent);
    }

    @Override
    public void onBackPressed() {
        stopServerService();
        super.onBackPressed();
    }
}
