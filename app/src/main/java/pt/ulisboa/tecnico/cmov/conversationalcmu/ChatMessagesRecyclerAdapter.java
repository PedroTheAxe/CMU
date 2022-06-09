package pt.ulisboa.tecnico.cmov.conversationalcmu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatMessagesRecyclerAdapter extends RecyclerView.Adapter<ChatMessagesRecyclerAdapter.MyViewHolder> {
    private ArrayList<JSONObject> chatMessageList;

    public ChatMessagesRecyclerAdapter(ArrayList<JSONObject> chatList)  {
        this.chatMessageList = chatList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView chatUserText;
        private TextView chatMessageText;
        private TextView chatTimestampText;

        public MyViewHolder(final View view) {
            super(view);
            chatUserText = view.findViewById(R.id.textViewUserName);
            chatMessageText = view.findViewById(R.id.textViewChatMessage);
            chatTimestampText = view.findViewById(R.id.textViewChatTimestamp);
        }
    }

    @NonNull
    @Override
    public ChatMessagesRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messages,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessagesRecyclerAdapter.MyViewHolder holder, int position) {
        String chatUser = null;
        String chatMessage = null;
        String chatTimestamp = null;
        try {
            chatUser = chatMessageList.get(position).getString("chatMessageSender");
            chatMessage = chatMessageList.get(position).getString("chatMessageContent");
            chatTimestamp = chatMessageList.get(position).getString("chatMessageTimestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.chatUserText.setText(chatUser);
        holder.chatMessageText.setText(chatMessage);
        holder.chatTimestampText.setText(chatTimestamp);
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

}
