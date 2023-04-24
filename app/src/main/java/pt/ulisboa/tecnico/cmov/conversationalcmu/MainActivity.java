package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pt.ulisboa.tecnico.cmov.conversationalcmu.LoginActivity;

public class MainActivity extends AppCompatActivity {

    String myUrl = "http://192.168.1.80:8080";
    private final OkHttpClient client = new OkHttpClient();
    SharedPreferences shp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shp = getSharedPreferences("myPreferences", MODE_PRIVATE);
        Uri uri = getIntent().getData();


        new Thread(() -> {
            try {
                String user = shp.getString("user","");
                String pass = shp.getString("pass","");
                String[] splitUri = null;
                Response response = RequestHandler.buildLoginRequest(user, pass);
                String responsePlaceholder = response.body().string();
                if (responsePlaceholder.equals("Incorrect password") || responsePlaceholder.equals("No such user, register new account"))  {
                    Intent e = new Intent(getApplicationContext(), LoginActivity.class);
                    if (uri != null) {
                        splitUri = uri.toString().split("/");
                        e.putExtra("URI",splitUri[3]);
                    }
                    startActivity(e);
                    return;
                }
                Intent i = new Intent(getApplicationContext(), ChatListActivity.class);
                Bundle extras = getIntent().getExtras();
                String invite = null;
                if (uri != null) {
                    splitUri = uri.toString().split("/");
                    Response response2 = RequestHandler.joinWithInviteRequest(user, splitUri[3]);
                }
                i.putExtra("userName", user);
                startActivity(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();



    }

}