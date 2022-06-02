package pt.ulisboa.tecnico.cmov.conversationalcmu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListRecyclerAdapter extends RecyclerView.Adapter<ChatListRecyclerAdapter.MyViewHolder> {
    private ArrayList<Chat> chatList;

    public ChatListRecyclerAdapter(ArrayList<Chat> chatList)  {
        this.chatList = chatList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView chatListText;

        public MyViewHolder(final View view) {
            super(view);
            chatListText = view.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public ChatListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_items,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListRecyclerAdapter.MyViewHolder holder, int position) {
        String chatName = chatList.get(position).getChatName();
        holder.chatListText.setText(chatName);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
