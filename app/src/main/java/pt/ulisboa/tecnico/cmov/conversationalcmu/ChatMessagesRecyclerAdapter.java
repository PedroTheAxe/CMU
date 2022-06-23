package pt.ulisboa.tecnico.cmov.conversationalcmu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        private ImageView chatImage;

        public MyViewHolder(final View view) {
            super(view);
            chatUserText = view.findViewById(R.id.textViewUserName);
            chatMessageText = view.findViewById(R.id.textViewChatMessage);
            chatTimestampText = view.findViewById(R.id.textViewChatTimestamp);
            chatImage = view.findViewById(R.id.imagePicture);
        }
    }

    @NonNull
    @Override
    public ChatMessagesRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messages,parent, false);
        return new MyViewHolder(itemView);
    }

    public Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessagesRecyclerAdapter.MyViewHolder holder, int position) {
        String chatUser = null;
        String chatMessage = null;
        String chatTimestamp = null;
        String chatImage = null;
        try {
            if (chatMessageList.get(position).has("chatMessageType") && chatMessageList.get(position).getString("chatMessageType").equals("image")) {
                Log.e("TAG","TEXTO");
                chatUser = chatMessageList.get(position).getString("chatMessageSender");
                chatTimestamp = chatMessageList.get(position).getString("chatMessageTimestamp");
                chatImage = chatMessageList.get(position).getString("chatImageBitmap");

                holder.chatUserText.setText(chatUser);
                holder.chatTimestampText.setText(chatTimestamp);
                holder.chatImage.setImageBitmap(getBitmapFromString(chatImage));
            } else {
                chatUser = chatMessageList.get(position).getString("chatMessageSender");
                chatTimestamp = chatMessageList.get(position).getString("chatMessageTimestamp");
                chatMessage = chatMessageList.get(position).getString("chatMessageContent");
                holder.chatUserText.setText(chatUser);
                holder.chatTimestampText.setText(chatTimestamp);
                holder.chatMessageText.setText(chatMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

}
