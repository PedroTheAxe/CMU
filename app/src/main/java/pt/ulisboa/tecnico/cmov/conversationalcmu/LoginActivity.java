package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    String myUrl = "http://192.168.56.1:8080";
    private final OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) throws Exception {
        EditText user = (EditText)findViewById(R.id.userEditText);
        EditText pass = (EditText)findViewById(R.id.passwordEditText);

        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(myUrl + "/login?user=" + user.getText().toString() + "&pass=" + pass.getText().toString())
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("headers",responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                Log.d("body",response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
        Intent i = new Intent(getApplicationContext(), ChatListActivity.class);
        startActivity(i);
    }
}
