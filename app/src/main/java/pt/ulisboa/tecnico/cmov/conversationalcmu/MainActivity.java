package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    TextView resultsTextView;
    Button displayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultsTextView = (TextView) findViewById(R.id.results);
        displayData = (Button) findViewById(R.id.displayData);
        Uri uri = getIntent().getData();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        if (uri != null) {
            String[] splitUri = uri.toString().split("/");
            i.putExtra("URI",splitUri[3]);
            Log.e("URI",splitUri[3]);
        }
        startActivity(i);
    }

    public void iftest(View view) throws Exception {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(myUrl + "/users")
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
    }

}