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

    public RequestHandler() {
    }

    public static Response buildLoginRequest(EditText user, EditText pass) throws IOException{
        Request request = new Request.Builder()
                .url(myUrl + "/login?user=" + user.getText().toString() + "&pass=" + pass.getText().toString())
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response buildChatListRequest(String userName) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/chats?username=" + userName)
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

    public static Response fetchChatMessagesRequest(String chatRoomName, int totalCount) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/fetchmessages?chatroomname=" + chatRoomName + "&from=" + String.valueOf(totalCount))
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response createPublicChatRequest(String chatRoomName) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/createchat?chatname=" + chatRoomName + "&chattype=public")
                .build();
        Log.e("URL","/createchat?chatname=" + chatRoomName + "&chattype=public");
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response createPrivateChatRequest(String chatRoomName, String inviteLink) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/createchat?chatname=" + chatRoomName + "&chattype=private&invitelink=" + inviteLink)
                .build();
        Log.e("URL",myUrl + "/createchat?chatname=" + chatRoomName + "&chattype=private&invitelink=" + inviteLink);
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response createGeoFencedChatRequest(String chatRoomName, String chatRadius, String lat, String lon) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/creategeo?chatname=" + chatRoomName + "&chatradius=" + chatRadius + "&lat=" + lat + "&lon=" + lon)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response joinWithInviteRequest(String chatRoomName, String inviteLink) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/joinprivatechat?username=" + chatRoomName + "&invitelink=" + inviteLink)
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

    public static Response buildJoinChatRequest(String chatRoom, String userName) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/joinchat?chatroom=" + chatRoom + "&username=" + userName)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response buildImageMessageRequest(String messageId) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/getimage?id=" + messageId)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response buildAvailableChatsRequest(String userName) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/joinablechats?username=" + userName)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }

    public static Response buildLeaveChatsRequest(String userName, String chatRoom) throws IOException {
        Request request = new Request.Builder()
                .url(myUrl + "/leavechat?chatroom=" + chatRoom + "&username=" + userName)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response;
    }
}
