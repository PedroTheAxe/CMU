package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Response;

public class PublicCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_creation);
    }

    public void createPublicChat(View view) {
        EditText chatName = (EditText)findViewById(R.id.publicChatNameEditText);
        Log.e("chatname", chatName.getText().toString());
        new Thread(() -> {
            try {
                Response response = RequestHandler.createPublicChatRequest(chatName.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        super.onBackPressed();
    }
}