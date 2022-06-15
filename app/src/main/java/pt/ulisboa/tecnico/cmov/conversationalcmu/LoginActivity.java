package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) throws Exception {
        EditText user = (EditText)findViewById(R.id.userEditText);
        EditText pass = (EditText)findViewById(R.id.passwordEditText);
        new Thread(() -> {
            try {
                Response response = RequestHandler.buildLoginRequest(user, pass);
                Intent i = new Intent(getApplicationContext(), ChatListActivity.class);
                Bundle extras = getIntent().getExtras();
                String invite = null;
                if (extras != null) {
                    invite = extras.getString("URI");
                    Response response2 = RequestHandler.joinWithInviteRequest(user.getText().toString(), invite);
                }
                i.putExtra("userName", user.getText().toString());
                startActivity(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
