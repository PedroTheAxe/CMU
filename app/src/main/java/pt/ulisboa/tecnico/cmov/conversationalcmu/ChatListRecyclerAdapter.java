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

public class ChatListRecyclerAdapter extends RecyclerView.Adapter<ChatListRecyclerAdapter.MyViewHolder> {
    private ArrayList<JSONObject> chatList;
    private RecyclerViewClickListener listener;

    public ChatListRecyclerAdapter(ArrayList<JSONObject> chatList, RecyclerViewClickListener listener)  {
        this.chatList = chatList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView chatListText;
        private TextView chatListType;

        public MyViewHolder(final View view) {
            super(view);
            chatListText = view.findViewById(R.id.textViewUserName);
            chatListType = view.findViewById(R.id.textViewChatMessage);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                listener.onClick(v, getAdapterPosition());
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        String chatName = null;
        String chatType = null;
        try {
            chatName = chatList.get(position).getString("chatName");
            chatType = chatList.get(position).getString("chatType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.chatListText.setText(chatName);
        holder.chatListType.setText(chatType);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position) throws JSONException;
    }
}
