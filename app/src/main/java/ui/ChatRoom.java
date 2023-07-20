package ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.mezh0013.R;
import algonquin.cst2335.mezh0013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.mezh0013.databinding.ReceiveMessageBinding;
import algonquin.cst2335.mezh0013.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    ChatViewModel chatModel;

    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private  ChatMessage removeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "android-database").build();
        ChatMessageDAO mDAO = db.cmDAO();

        chatModel = new ViewModelProvider(this).get(ChatViewModel.class);
        ArrayList<ChatMessage> chatMessages = chatModel.messages.getValue();
        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.theRecycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.theRecycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.sendButton.setOnClickListener(click->{


            String messageText=binding.textInput.getText().toString();
            if (!messageText.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
                String currentDatEndTime = sdf.format(new Date());

                ChatMessage sent = new ChatMessage(messageText, currentDatEndTime, true);
                messages.add(sent);


                myAdapter.notifyItemInserted(messages.size() - 1);


                Executor thread = Executors.newSingleThreadExecutor();

                binding.textInput.setText("");
                thread.execute(() -> {

                    mDAO.insertMessage(sent);
                });
            }
        });




        binding.button.setOnClickListener(click->{


            String messageText=binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a",Locale.getDefault());
            String currentDatEndTime = sdf.format(new Date());

            ChatMessage recieve = new ChatMessage(messageText, currentDatEndTime, false);
            messages.add(recieve);

            myAdapter.notifyItemInserted(messages.size()-1);

            Executor thread = Executors.newSingleThreadExecutor();

            binding.textInput.setText("");
            thread.execute(() -> {
                mDAO.insertMessage(recieve);
            });


        binding.theRecycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                if (viewType == 0) {
                    View view = inflater.inflate(R.layout.sent_message, parent, false);
                    return new MyRowHolder(view, mDAO);
                } else {
                    View view = inflater.inflate(R.layout.receive_message, parent, false);
                    return new MyRowHolder(view, mDAO);
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.bind(chatMessage);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position){
                ChatMessage chatMessage = messages.get(position);
                if (chatMessage.getIsSentButton()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });










        class MyRowHolder extends RecyclerView.ViewHolder{

        public TextView message;
        public TextView timeSent;
        ChatMessageDAO mDAO;



        public MyRowHolder(@NonNull View itemView, ChatMessageDAO dao) {
            super(itemView);
            mDAO = dao;

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setTitle("delete");
                builder.setMessage("Do you want to delete this message: " + messages.getMessage());
                builder.setNegativeButton("No", (dialog, cl) -> {
                });
                builder.setPositiveButton("Yes", (dialog, cl) -> {
                    Executor thread = Executor.newSingleThreadExecutor();
                    thread.execute(() -> {
                        ChatMessage m = messages.get(position);
                        mDAO.deleteMessage(m);
                        runOnUiThread(() ->
                                messages.remove(position);
                        myAdapter.notifyItemRemoved(position);

                        Snackbar.make(messageText, "removed message # "+position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", undoClk -> {
                                    messages.add(position, removedMessage);
                                    myAdapter.notifyItemInserted(position);
                                })
                                .show();
                    });

                });
            });

                builder.create().show();
        });
            messageText = itemView.findViewById(R.id.m);
            timeText = itemView.findViewById(R.id.t);
    }


    public void bind(ChatMessage messages) {
                m.setText(messages.getMessage());
                t.setText(messages.getTimeSent());
            }
    public void removeMessage(int position) {
        messages.remove(position);
        myAdapter.notifyItemRemoved(position);
    }
    public void updateMessages(ArrayList<ChatMessage> newMessages) {
        messages.clear();
        messages.addAll(newMessages);
        myAdapter.notifyDataSetChanged();

}
