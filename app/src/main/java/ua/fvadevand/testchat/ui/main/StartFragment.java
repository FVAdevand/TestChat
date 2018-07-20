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
import android.widget.TextView;

import ua.fvadevand.testchat.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStartInteractionListener} interface
 * to handle interaction events.
 */
public class StartFragment extends Fragment {

    private OnStartInteractionListener mListener;
    private Button mCreateChatBtn;
    private Button mJoinChatBtn;
    private TextView mDescriptionView;

    public StartFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCreateChatBtn = view.findViewById(R.id.btn_create_chat);
        mCreateChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCreateChat();
                }
            }
        });

        mJoinChatBtn = view.findViewById(R.id.btn_join_chat);
        mJoinChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onJoinChat();
                }
            }
        });

        mDescriptionView = view.findViewById(R.id.tv_wifi_message);
        mDescriptionView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStartInteractionListener) {
            mListener = (OnStartInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void displayBtns(boolean isDisplay) {
        if (isDisplay) {
            mCreateChatBtn.setVisibility(View.VISIBLE);
            mJoinChatBtn.setVisibility(View.VISIBLE);
            mDescriptionView.setVisibility(View.INVISIBLE);
        } else {
            mCreateChatBtn.setVisibility(View.INVISIBLE);
            mJoinChatBtn.setVisibility(View.INVISIBLE);
            mDescriptionView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnStartInteractionListener {
        void onCreateChat();

        void onJoinChat();
    }
}
