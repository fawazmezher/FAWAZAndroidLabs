package Chatroom.ui;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import Chatroom.Data.ChatMessage;
import Chatroom.Data.ChatMessageDAO;
import Chatroom.Data.ChatViewModel;
import Chatroom.Data.MessageDatabase;
import algonquin.cst2335.mezh0013.R;
import algonquin.cst2335.mezh0013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.mezh0013.databinding.ReceiveMessageBinding;
import algonquin.cst2335.mezh0013.databinding.SentMessageBinding;
import algonquin.cst2335.mezh0013.databinding.DetailsLayoutBinding;

public class ChatRoom extends AppCompatActivity {


    private RecyclerView.Adapter myAdapter;
    private ArrayList<ChatMessage> messages;
    private ChatMessageDAO mDAO;
    private Executor thread;
    private ActivityChatRoomBinding binding;
    private String message;

    MyRowHolder rh;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        if (item.getItemId() == R.id.id_item1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
            builder.setMessage("Do you want to delete this message " + rh.messageText.getText());
            builder.setTitle("Question");
            builder.setNegativeButton("No", (dialog, cl) -> {});

            builder.setPositiveButton("Yes", (dialog, cl) -> {
                ChatMessage toDelete = messages.get(rh.getAbsoluteAdapterPosition());
                Executor thread1 = Executors.newSingleThreadExecutor();
                thread1.execute(() -> {
                    mDAO.deleteMessage(toDelete);
                    messages.remove(rh.getAbsoluteAdapterPosition());
                    runOnUiThread(() -> {
                        myAdapter.notifyDataSetChanged();
                        Snackbar.make(binding.getRoot(), "You deleted message #" + rh.getAbsoluteAdapterPosition(), Snackbar.LENGTH_LONG)
                                .setAction("Undo", c -> {
                                    messages.add(rh.getAbsoluteAdapterPosition(), toDelete);
                                    myAdapter.notifyItemInserted(rh.getAbsoluteAdapterPosition());
                                })
                                .show();
                    });
                });
            });
            builder.create().show();
        } else if (item.getItemId() == R.id.id_item2) {
            message = "Version 1.0, created by Fawaz Mezher";
            runOnUiThread(() -> {
                Toast.makeText(this, "You clicked " + message, Toast.LENGTH_SHORT).show();
            });
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChatRoomBinding binding=ActivityChatRoomBinding.inflate(getLayoutInflater());

        ChatViewModel chatModel = new ViewModelProvider(this).get(ChatViewModel.class);

        messages= chatModel.messages.getValue();
        setContentView(binding.getRoot());


        if (messages==null){
            chatModel.messages.setValue(messages=new  ArrayList<ChatMessage>());
            thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
                mDAO = db.cmDAO();
                messages.addAll( mDAO.getAllMessages() );
                runOnUiThread( () -> {
                    binding.theRecycleView.setAdapter( myAdapter );
                    setContentView(binding.getRoot());
                    if(messages.size()-1>0) {
                        binding.theRecycleView.smoothScrollToPosition(messages.size() - 1);
                    }
                });
            });
        }

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            Log.i("tag", "onCreate: "+newMessageValue.getMessages());
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLocation ,chatFragment).addToBackStack("").commit();
        });

        binding.theRecycleView.setAdapter(myAdapter=new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType==0){

                    SentMessageBinding sentMessageBinding = SentMessageBinding.inflate(getLayoutInflater(),
                            parent, false);
                    View root = sentMessageBinding.getRoot();
                    return new MyRowHolder(root);
                }else {

                    ReceiveMessageBinding receiveMessagesBinding=ReceiveMessageBinding.inflate(getLayoutInflater(),
                            parent,false);
                    View root = receiveMessagesBinding.getRoot();
                    return new MyRowHolder(root);
                }
            }


            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                String strMessage = messages.get(position).getMessages();
                holder.messageText.setText(strMessage);
                holder.timeText.setText(messages.get(position).getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }


            @Override
            public int getItemViewType(int position) {
                if(messages.get(position).isSentButton()) {
                    return 0;
                }else {
                    return 1;
                }
            }
        });

        binding.theRecycleView.setLayoutManager(new LinearLayoutManager(this));




        binding.sendButton.setOnClickListener(click->{


            String text=binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDatEndTime = sdf.format(new Date());

            messages.add(new ChatMessage(text,currentDatEndTime,true));



            thread.execute(() ->
            {

                mDAO.insertMessage(new ChatMessage(text,currentDatEndTime,true));


            });


            myAdapter.notifyItemInserted(messages.size()-1);

            binding.textInput.setText("");//now will be empty
            binding.theRecycleView.smoothScrollToPosition(messages.size()-1);

        });



        binding.button.setOnClickListener(click->{

            String text=binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDatEndTime = sdf.format(new Date());

            messages.add(new ChatMessage(text,currentDatEndTime,false));




            thread.execute(() ->
            {
                mDAO.insertMessage(new ChatMessage(text,currentDatEndTime,false));
            });

            myAdapter.notifyItemInserted(messages.size()-1);

            binding.textInput.setText("");
            binding.theRecycleView.smoothScrollToPosition(messages.size()-1);
        });
    }





    class MyRowHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        public  TextView timeText;

        public MyRowHolder(@NonNull View itemView) {

            super(itemView);


            itemView.setOnClickListener(clk->{
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                ChatViewModel.selectedMessage.postValue(selected);




//                AlertDialog.Builder builder=new AlertDialog.Builder(ChatRoom.this);
//                builder.setMessage("Do you want to Delete this message : "+messageText.getText()).
//                        setTitle("Question").
//                        setNegativeButton("no",(dialog,cl)->{})
//
//                        .setPositiveButton("yes",(dialog,cl)->{
//
//                            int Position=getAbsoluteAdapterPosition();
//                            ChatMessage removedMessage=messages.get(Position);
//
//
//                            thread.execute(() ->
//                            {
//
//
//
//                                mDAO.deleteMessage(removedMessage);
//
//
//
//                            });
//
//                            runOnUiThread( () ->  {
//
//
//                                messages.remove(Position);
//                                myAdapter.notifyItemRemoved(Position);
//
//                            });
//
//                            Snackbar.make(itemView,"You deleted message # "+messageText.getText() ,Snackbar.LENGTH_SHORT)
//                                    .setAction("Undo",c->{
//                                        messages.add(Position,removedMessage);
//                                        myAdapter.notifyItemInserted(Position);
//                                    }).show();
//                        }).create().show();
            });

            messageText= itemView.findViewById(R.id.m);
            timeText= itemView.findViewById(R.id.t);
        }
    }
}