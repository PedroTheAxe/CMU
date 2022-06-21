package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.Random;

import okhttp3.Response;

public class PrivateCreationActivity extends AppCompatActivity {

    private ClipboardManager myClipboard;
    private ClipData myClip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_creation);
    }

    public void createPrivateChat(View view) {
        EditText chatName = (EditText)findViewById(R.id.privateChatNameEditText);
        String inviteLinkString = getInviteLinkString();
        new Thread(() -> {
            try {
                Response response = RequestHandler.createPrivateChatRequest(chatName.getText().toString(), inviteLinkString);
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                myClip = ClipData.newPlainText("URI", "http://www.conversationalist.com/" + inviteLinkString);
                myClipboard.setPrimaryClip(myClip);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        super.onBackPressed();
    }

    protected String getInviteLinkString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}