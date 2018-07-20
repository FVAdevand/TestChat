package ua.fvadevand.testchat.ui.message;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import ua.fvadevand.testchat.R;
import ua.fvadevand.testchat.adapters.MessageListAdapter;
import ua.fvadevand.testchat.chat.ConnectedClient;
import ua.fvadevand.testchat.models.ChatMessage;
import ua.fvadevand.testchat.services.ServerService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnChatCloseListener} interface
 * to handle interaction events.
 */
public class ServerMessageFragment extends Fragment
        implements ConnectedClient.OnConnectedClientListener {

    private static final String LOG_TAG = ServerMessageFragment.class.getSimpleName();

    private ConnectedClient mClient;
    private Handler mHandler;
    private List<ChatMessage> mMessageList;

    private OnChatCloseListener mListener;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService service1 = ((ServerService.ServiceBinder) service).getService();
            mClient = new ConnectedClient(service1.getSocket(), ServerMessageFragment.this);
            mClient.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private MessageListAdapter mAdapter;
    private RecyclerView mMessageListView;
    private EditText mMessageTextView;

    public ServerMessageFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHandler = new Handler();
        mMessageList = new ArrayList<>();

        mMessageTextView = view.findViewById(R.id.et_message);
        ImageButton sendMessageBtn = view.findViewById(R.id.ibtn_send_message);
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mMessageListView = view.findViewById(R.id.message_list);
        mMessageListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MessageListAdapter(mMessageList);
        mMessageListView.setAdapter(mAdapter);

        if (getActivity() != null) {
            Intent intent = new Intent(getContext(), ServerService.class);
            getActivity().bindService(intent, mConnection, 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatCloseListener) {
            mListener = (OnChatCloseListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChatCloseListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (getActivity() != null) {
            mClient.close();
            getActivity().unbindService(mConnection);
            Log.i(LOG_TAG, "onDetach: unBindService");
        }
    }

    @Override
    public void onReceiveMessage(final String messageText) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ChatMessage message = new ChatMessage(ChatMessage.ID_COMPANION, messageText);
                displayMessage(message);
            }
        });
    }

    @Override
    public void onConnectionClosed() {
        if (mListener != null) {
            mListener.OnChatClose();
        }
    }

    private void sendMessage() {
        String messageText = mMessageTextView.getText().toString().trim();
        mMessageTextView.getText().clear();
        mClient.sendMessage(messageText);
        ChatMessage message = new ChatMessage(ChatMessage.ID_USER, messageText);
        displayMessage(message);
    }

    private void displayMessage(ChatMessage message) {
        mMessageList.add(message);
        int lastPosition = mMessageList.size() - 1;
        mAdapter.notifyItemInserted(lastPosition);
        mMessageListView.smoothScrollToPosition(lastPosition);
    }

    public interface OnChatCloseListener {
        void OnChatClose();
    }
}
