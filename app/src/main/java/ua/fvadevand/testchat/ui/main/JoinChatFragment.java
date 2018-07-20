package ua.fvadevand.testchat.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ua.fvadevand.testchat.R;
import ua.fvadevand.testchat.utilities.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEnterHostNameListener} interface
 * to handle interaction events.
 */
public class JoinChatFragment extends Fragment {

    private OnEnterHostNameListener mListener;
    private Toast mToast;

    public JoinChatFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_join_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText hostNameView = view.findViewById(R.id.et_host_name);
        Button enterHostNameBtn = view.findViewById(R.id.btn_enter_host_name);
        enterHostNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hostName = hostNameView.getText().toString().trim();
                if (Utils.isValidIp(hostName)) {
                    if (mListener != null) {
                        mListener.onEnterHostName(hostName);
                    }
                } else {
                    showToast(getString(R.string.error_invalid_host));
                }
            }
        });
    }

    private void showToast(String text) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEnterHostNameListener) {
            mListener = (OnEnterHostNameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEnterHostNameListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEnterHostNameListener {
        void onEnterHostName(String hostName);
    }
}
