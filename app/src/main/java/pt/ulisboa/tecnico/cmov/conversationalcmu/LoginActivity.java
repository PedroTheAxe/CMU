package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences shp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        shp = getSharedPreferences("myPreferences", MODE_PRIVATE);
    }

    public void login(View view) throws Exception {
        EditText user = (EditText)findViewById(R.id.userEditText);
        EditText pass = (EditText)findViewById(R.id.passwordEditText);
        new Thread(() -> {
            try {
                Response response = RequestHandler.buildLoginRequest(user.getText().toString(), pass.getText().toString());
                String responsePlaceholder = response.body().string();
                if (responsePlaceholder.equals("Incorrect password") || responsePlaceholder.equals("No such user, register new account"))  {
                    return;
                }
                shp.edit().putString("user",user.getText().toString()).commit();
                shp.edit().putString("pass",pass.getText().toString()).commit();
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

    public void register(View view) {
        EditText user = (EditText)findViewById(R.id.userEditText);
        EditText pass = (EditText)findViewById(R.id.passwordEditText);
        new Thread(() -> {
            try {

                Response response = RequestHandler.buildRegisterRequest(user.getText().toString(), pass.getText().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void guestAccountLogin(View view) {
        EditText user = (EditText)findViewById(R.id.userEditText);
        EditText pass = (EditText)findViewById(R.id.passwordEditText);
        new Thread(() -> {
            try {
                Random ran = new Random();
                Integer randomPass = ran.nextInt(90000)+10000;
                pass.setText(String.valueOf(randomPass));
                Response response = RequestHandler.buildLoginRequest(user.getText().toString(), pass.getText().toString());
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
