package ua.fvadevand.testchat.ui.message;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ua.fvadevand.testchat.R;

public class MessageActivity extends AppCompatActivity
        implements ServerMessageFragment.OnChatCloseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.message_fragment_container, new ServerMessageFragment())
                .commit();
    }

    @Override
    public void OnChatClose() {
        finish();
    }
}
