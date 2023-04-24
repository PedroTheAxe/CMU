package pt.ulisboa.tecnico.cmov.conversationalcmu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

public class ChatActivity extends AppCompatActivity {
    private ArrayList<JSONObject> chatMessageList;
    private RecyclerView recyclerView;
    private TextView chatRoomTxt;
    private String chatRoomName;
    private String userName;
    private String messageContent;
    private WebSocketClient webSocketClient;
    private ChatMessagesRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ChatMessagesRecyclerAdapter.RecyclerViewClickListener listener;
    int totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_resource);
        recyclerView = findViewById(R.id.recyclerView);
        chatMessageList = new ArrayList<>();
        this.chatRoomTxt = findViewById(R.id.chatRoomName);
        this.chatRoomName = "chatRoomName not set";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chatRoomName = extras.getString("chatName");
            userName = extras.getString("userName");
        }
        chatRoomTxt.setText(chatRoomName);
        setChatMessageInfo();
        setAdapter();
        EditText submitMessage = findViewById(R.id.userEditText);
        submitMessage.setFocusableInTouchMode(true);
        submitMessage.requestFocus();
        createWebSocketClient();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    totalItemCount = layoutManager.getItemCount();
                    if (!recyclerView.canScrollVertically(1)) {
                        fetchData(totalItemCount);
                    }
                }
            }
        });
        submitMessage.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    messageContent = submitMessage.getText().toString();
                    submitMessage.getText().clear();
                    sendChatMessage();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(submitMessage.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    public void fetchData(int totalItems) {

        new Thread(() -> {
            try {
                Response response = RequestHandler.fetchChatMessagesRequest(chatRoomName, totalItems);
                JSONArray jsonArray = new JSONArray(response.body().string());
                for ( int i = 0; i < jsonArray.length(); i++) {
                    JSONObject entry = jsonArray.getJSONObject(i);
                    Log.e("LENGTH",String.valueOf(jsonArray.length()));
                    chatMessageList.add(entry);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void callForImageRequest(String s) {
        String[] params = s.split("/");
        new Thread(() -> {
            try {
                Response response = RequestHandler.buildImageMessageRequest(params[1]);
                JSONObject entry = new JSONObject(response.body().string());
                chatMessageList.add(entry);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://192.168.1.80:8080/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                webSocketClient.send("Session start");
                Log.e("WebSocket","Websocket is open.");
            }

            @Override
            public void onTextReceived(String s) {
                Log.e("Recebido", s);
                if (s.contains("imgfileidreq")) {
                    callForImageRequest(s);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if (s.contains("imgfileidreq")) {

                            } else {
                                JSONArray jsonArray = new JSONArray(s);
                                for ( int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject entry = jsonArray.getJSONObject(i);
                                    Log.e("TAGENTRY",entry.toString());
                                    chatMessageList.add(entry);
                                }
                            }
                            adapter.notifyItemInserted(chatMessageList.size());
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
            }

            @Override
            public void onCloseReceived() {
                webSocketClient.close();
            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.connect();

    }

    private void setAdapter() {
        setOnClickListener();
        adapter = new ChatMessagesRecyclerAdapter(chatMessageList, listener);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            Intent socialIntent = new Intent();
            socialIntent.setAction(Intent.ACTION_SEND);
            socialIntent.putExtra(Intent.EXTRA_TEXT, chatMessageList.get(position).getString("chatMessageContent"));
            socialIntent.setType("text/plain");

            Intent shareSocialIntent = Intent.createChooser(socialIntent, null);
            startActivity(shareSocialIntent);
        };
    }

    private void sendChatMessage() {
        new Thread(() -> {
            Log.e("user", this.userName);
            Log.e("user", this.chatRoomName);
            Log.e("user", this.messageContent);
            webSocketClient.send(this.chatRoomName + "/" + this.userName + "/" + this.messageContent);
        }).start();
    }

    private void setChatMessageInfo() {
        new Thread(() -> {
            try {
                Response response = RequestHandler.buildChatMessagesRequest(this.chatRoomName);
                JSONArray jsonArray = new JSONArray(response.body().string());
                for ( int i = 0; i < jsonArray.length(); i++) {
                    JSONObject entry = jsonArray.getJSONObject(i);
                    chatMessageList.add(entry);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void leaveChat(View view) {
        new Thread(() -> {
            try {
                Response response = RequestHandler.buildLeaveChatsRequest(this.userName, this.chatRoomName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        super.onBackPressed();
    }

    public void sendAttachment(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() { 
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                        final int COMPRESSION_QUALITY = 0;
                        String encodedImage;
                        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                                byteArrayBitmapStream);
                        byte[] b = byteArrayBitmapStream.toByteArray();
                        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        webSocketClient.send("image/" + chatRoomName + "/" + userName + "/" + encodedImage);
                    }
                }
            });

}
