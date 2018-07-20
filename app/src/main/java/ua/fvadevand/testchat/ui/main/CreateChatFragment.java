package ua.fvadevand.testchat.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.fvadevand.testchat.R;


public class CreateChatFragment extends Fragment {

    private static final String LOG_TAG = CreateChatFragment.class.getSimpleName();

    private static final String ARG_HOST_NAME = "HOST_NAME";

    private String mHostName;

    public CreateChatFragment() {
    }

    public static CreateChatFragment newInstance(String hostName) {
        CreateChatFragment fragment = new CreateChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HOST_NAME, hostName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHostName = getArguments().getString(ARG_HOST_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView hostView = view.findViewById(R.id.tv_host);
        hostView.setText(mHostName);
    }
}
