package ui;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import java.util.ArrayList;

    @Entity
    public class ChatMessage {
        @ColumnInfo(name = "message")
        String message;

        @ColumnInfo(name = "timeSent")
        String timeSent;

        @ColumnInfo(name = "SendOrReceive")
        boolean isSentButton;

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        public int id;

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

}