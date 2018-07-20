package ua.fvadevand.testchat.ui.message;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ua.fvadevand.testchat.R;

public class MessageActivity extends AppCompatActivity
        implements MessageFragment.OnChatCloseListener {

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.message_fragment_container, new MessageFragment())
                .commit();
    }

    @Override
    public void OnChatClose() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(getString(R.string.message_connection_closed));
            }
        });
        finish();
    }

    private void showToast(String text) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
