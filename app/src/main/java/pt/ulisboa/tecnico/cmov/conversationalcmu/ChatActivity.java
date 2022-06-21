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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONArray jsonArray = new JSONArray(s);
                            for ( int i = 0; i < jsonArray.length(); i++) {
                                JSONObject entry = jsonArray.getJSONObject(i);
                                chatMessageList.add(entry);
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
            } //fechar sessao com erro qd houver bug
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        //webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();

    }

    private void setAdapter() {
        adapter = new ChatMessagesRecyclerAdapter(chatMessageList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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
            new ActivityResultCallback<ActivityResult>() { //ver content type ou por tudo em cima uns dos outros e popular apenas de acordo com os fields
                @Override
                public void onActivityResult(ActivityResult result) {
                    setContentView(R.layout.chat_images);
                    ImageView imageView = (ImageView) findViewById(R.id.imagePicture);
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData(); //local password stored in phone guest account -- on bind chama o item e fico a ver
                        imageView.setImageURI(uri); //avisar opr websocket de imagem e ir buscar atraves de rest
                    }
                }
            });

}
