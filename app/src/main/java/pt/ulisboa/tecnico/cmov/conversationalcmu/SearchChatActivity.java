package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
                    if (entry.getString("chatType").equals("Geo-Fenced")) {
                        if (checkIfInGeoFence(entry)) {
                            chatroomsList.add(entry);
                        } else {
                            continue;
                        }
                    } else {
                        chatroomsList.add(entry);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean checkIfInGeoFence(JSONObject entry) throws JSONException {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Location locationB = new Location("Chat center");
        locationB.setLatitude(Double.parseDouble(entry.getString("chatLat")));
        locationB.setLongitude(Double.parseDouble(entry.getString("chatLon")));
        Log.e("fixe", String.valueOf(location.distanceTo(locationB)));
        Log.e("fixe", String.valueOf((Double.parseDouble(entry.getString("chatRadius")) * 1000)));
        if ((Double.parseDouble(entry.getString("chatRadius")) * 1000) >= location.distanceTo(locationB)) {
            return true;
        } else {
            return false;
        }
    }

}