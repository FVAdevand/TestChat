package ua.fvadevand.testchat.models;

public class ChatMessage {

    public static final int ID_USER = 0;
    public static final int ID_COMPANION = 1;

    private int mPersonId;
    private String mText;

    public ChatMessage() {
    }

    public ChatMessage(int personId, String text) {
        mPersonId = personId;
        mText = text;
    }

    public int getPersonId() {
        return mPersonId;
    }

    public void setPersonId(int personId) {
        mPersonId = personId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
