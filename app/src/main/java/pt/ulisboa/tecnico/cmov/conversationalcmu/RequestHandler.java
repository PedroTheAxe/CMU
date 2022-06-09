package pt.ulisboa.tecnico.cmov.conversationalcmu;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestHandler {

    private static final String myUrl = "http://192.168.1.80:8080";
    private static final OkHttpClient client = new OkHttpClient();

    public static Response buildLoginRequest(EditText user, EditText pass) throws IOException{
        Request request = new Request.Builder()
                .url(myUrl + "/login?user=" + user.getText().toString() + "&pass=" + pass.getText().toString())
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response buildChatListRequest() throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/chats")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response buildChatMessagesRequest(String chatRoomName) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/chatrooms?chatroomname=" + chatRoomName)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response buildSendRequest(String chatRoom, String userName, String messageContent) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/sendmessage?chatroom=" + chatRoom + "&username=" + userName + "&content=" + messageContent)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }
}
