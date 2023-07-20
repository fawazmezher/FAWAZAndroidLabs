package ui;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ChatMessage extends ViewModel {

    private String message;
    private String timeSent;
    private boolean isSentButton;



    public ChatMessage(String m, String t, boolean sent) {

        this.message = m;
        this.timeSent = t;
        this.isSentButton = sent;

    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}