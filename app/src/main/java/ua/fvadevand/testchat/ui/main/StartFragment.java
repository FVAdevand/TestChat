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

import ua.fvadevand.testchat.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStartInteractionListener} interface
 * to handle interaction events.
 */
public class StartFragment extends Fragment {

    private OnStartInteractionListener mListener;

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

        Button createChatBtn = view.findViewById(R.id.btn_create_chat);
        createChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCreateChat();
                }
            }
        });

        Button joinChatBtn = view.findViewById(R.id.btn_join_chat);
        joinChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onJoinChat();
                }
            }
        });
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

    public interface OnStartInteractionListener {
        void onCreateChat();

        void onJoinChat();
    }
}
