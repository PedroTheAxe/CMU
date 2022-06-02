package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ChatListActivity extends AppCompatActivity {
    String myUrl = "http://192.168.56.1:8080";
    private final OkHttpClient client = new OkHttpClient();
    private ArrayList<Chat> chatroomsList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        recyclerView = findViewById(R.id.recyclerView);
        chatroomsList = new ArrayList<>();
        // change to get info from rest api
        setChatInfo();
        setAdapter();
    }

    private void setAdapter() {
        ChatListRecyclerAdapter adapter = new ChatListRecyclerAdapter(chatroomsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setChatInfo() {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(myUrl + "/users")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("headers",responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                Gson gson = new Gson();
                //Chat newChat = gson.fromJson(response.body().string(), Chat.class);
                //Log.d("response",newChat.getChatType());
                //String jsonString = response.body().string();
                //ObjectMapper mapper = new ObjectMapper();
                //chatroomsList = mapper.readValue(jsonString, ArrayList.class);
                Log.d("body",response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}