package pt.ulisboa.tecnico.cmov.conversationalcmu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import okhttp3.Response;

public class GeoCreationActivity extends AppCompatActivity {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mapView;
    private EditText chatName;
    private EditText chatRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_creation);

        chatName = findViewById(R.id.geoChatNameEditText);
        chatRadius = findViewById(R.id.radiusEditText);

    }


    public void createGeoChat(View view) {

        Intent i = new Intent(getApplicationContext(), GeoFencedCreationActivity.class);
        i.putExtra("userName", getIntent().getExtras().getString("userName"));
        Log.e("USER",getIntent().getExtras().getString("userName"));
        i.putExtra("chatName", chatName.getText().toString());
        i.putExtra("chatRadius", chatRadius.getText().toString());
        startActivity(i);

    }
}