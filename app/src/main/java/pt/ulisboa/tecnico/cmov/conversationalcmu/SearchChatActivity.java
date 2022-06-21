package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

public class SearchChatActivity extends AppCompatActivity {
    private ArrayList<JSONObject> chatroomsList;
    private RecyclerView recyclerView;
    private ChatSearchRecyclerAdapter.RecyclerViewClickListener listener;
    private ChatSearchRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chat);
        recyclerView = findViewById(R.id.recyclerView);
        chatroomsList = new ArrayList<>();
        setChatInfo();
        setAdapter();

        EditText searchEditText = findViewById(R.id.searchNameEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    filter(s.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void filter(String text) throws JSONException {
        ArrayList<JSONObject> filtered = new ArrayList<>();
        for (JSONObject obj: chatroomsList) {
            if (obj.getString("chatName").toLowerCase().contains(text.toLowerCase())) {
                filtered.add(obj);
            }
        }

        adapter.filterList(filtered);
     }

    private void setAdapter() {
        setOnClickListener();
        adapter = new ChatSearchRecyclerAdapter(chatroomsList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            new Thread(() -> {
                try {
                    Response response = RequestHandler.buildJoinChatRequest(chatroomsList.get(position).getString("chatName"),getIntent().getExtras().getString("userName"));
                    Intent i = new Intent(getApplicationContext(), ChatListActivity.class);
                    i.putExtra("userName", getIntent().getExtras().getString("userName"));
                    startActivity(i);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }).start();
        };
    }

    private void setChatInfo() {
        new Thread(() -> {
            try {
                Response response = RequestHandler.buildAvailableChatsRequest(getIntent().getExtras().getString("userName"));
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