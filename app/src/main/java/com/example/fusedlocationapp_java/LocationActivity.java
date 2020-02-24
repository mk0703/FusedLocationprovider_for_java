package com.example.fusedlocationapp_java;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class LocationActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationProviderClient;
    protected Location location;

    private StringBuilder stringBuilder = new StringBuilder();
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("debug","error1");

        setContentView(R.layout.activity_main);
        textView = findViewById((R.id.text_view));

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest();

        //Priorityの設定．今後はボタン設定などでPriorityを切り替えられるようにしたい．
        //現在は一番精度の良くなる設定．
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Button button = findViewById((R.id.button_start));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    @SuppressWarnings("MissingPermission")
    private void getLocation()
    {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(
                        this,
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if(task.isSuccessful() && task.getResult() != null)
                                {
                                    location = task.getResult();
                                    stringBuilder.append((String.format(Locale.ENGLISH, "%s: %f, ","緯度", location.getLatitude())));
                                    stringBuilder.append((String.format(Locale.ENGLISH, "%s: %f, ","経度", location.getLongitude())));
                                    textView.setText(stringBuilder);
                                }
                                else
                                {
                                    textView.setText("測定不能");
                                }
                            }
                        });
    }
}
