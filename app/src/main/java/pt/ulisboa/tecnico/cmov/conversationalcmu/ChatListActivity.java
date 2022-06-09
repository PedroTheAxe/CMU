package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatListActivity extends AppCompatActivity {
    private ArrayList<JSONObject> chatroomsList;
    private RecyclerView recyclerView;
    private ChatListRecyclerAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        recyclerView = findViewById(R.id.recyclerView);
        chatroomsList = new ArrayList<>();
        setChatInfo();
        setAdapter();
    }

    private void setAdapter() {
        setOnClickListener();
        ChatListRecyclerAdapter adapter = new ChatListRecyclerAdapter(chatroomsList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra("chatName", chatroomsList.get(position).getString("chatName"));
            Bundle extras = getIntent().getExtras();
            String userName = null;
            if (extras != null) {
                userName = extras.getString("userName");
            }
            intent.putExtra("userName",userName);
            startActivity(intent);
        };
    }

    private void setChatInfo() {
        new Thread(() -> {
            try {
                Response response = RequestHandler.buildChatListRequest();
                JSONArray jsonArray = new JSONArray(response.body().string());
                for ( int i = 0; i < jsonArray.length(); i++) {
                    JSONObject entry = jsonArray.getJSONObject(i);
                    chatroomsList.add(entry);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
}