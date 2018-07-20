package ua.fvadevand.testchat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.fvadevand.testchat.R;
import ua.fvadevand.testchat.models.ChatMessage;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private List<ChatMessage> mMessageList;

    public MessageListAdapter(List<ChatMessage> messageList) {
        mMessageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;

        switch (viewType) {
            case ChatMessage.ID_USER:
                layoutId = R.layout.message_item_user;
                break;
            case ChatMessage.ID_COMPANION:
                layoutId = R.layout.message_item_companion;
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = mMessageList.get(position);
        holder.mTextView.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessageList.get(position).getPersonId();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        MessageViewHolder(View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.tv_message);
        }
    }
}
