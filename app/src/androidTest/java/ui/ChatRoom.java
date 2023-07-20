package ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.mezh0013.R;
import algonquin.cst2335.mezh0013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.mezh0013.databinding.ReceiveMessageBinding;
import algonquin.cst2335.mezh0013.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    ChatViewModel chatModel;

    private RecyclerView.Adapter<MyRowHolder> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatViewModel.class);
        ArrayList<ChatMessage> chatMessages = chatModel.messages.getValue();
        if (chatMessages == null) {
            chatModel.messages.postValue(messages);
        } else {
            messages.addAll(chatMessages);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

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



                String strMessage = messages.get(position).getMessage();
                holder.message.setText(strMessage);

                holder.timeSent.setText(messages.get(position).getTimeSent());


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




            myAdapter.notifyItemInserted(messages.size()-1);

            binding.textInput.setText("");
            binding.theRecycleView.smoothScrollToPosition(messages.size()-1);

        });






        binding.button.setOnClickListener(click->{


            String text=binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDatEndTime = sdf.format(new Date());

            messages.add(new ChatMessage(text,currentDatEndTime,false));




            myAdapter.notifyItemInserted(messages.size()-1);

            binding.textInput.setText("");
            binding.theRecycleView.smoothScrollToPosition(messages.size()-1);




        });













    }


    class MyRowHolder extends RecyclerView.ViewHolder{

        public TextView message;
        public TextView timeSent;


        public MyRowHolder(@NonNull View itemView) {

            super(itemView);
            message= itemView.findViewById(R.id.m);
            timeSent= itemView.findViewById(R.id.t);


        }
    }

}