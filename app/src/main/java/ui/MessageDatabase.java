package ui;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import ui.ChatMessage;
import ui.ChatMessageDAO      ;


@Database(entities = {ChatMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {
    public abstract ChatMessageDAO cmDAO();
}