package ui;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO {

    @Insert
    void insertMessage(ChatRoom.ChatMessage m);


    @Query("Select * from ChatMessage")
    List<ChatRoom.ChatMessage> getAllMessages();

    @Delete
    void deleteMessage(ChatRoom.ChatMessage m);




}