package com.example.fusedlocationapp_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if(Build.VERSION.SDK_INT >= 24)
        {
            checkPermission();
        }
        else
        {
            locationActivity();
        }*/
        locationActivity();

    }

    //位置情報permission許可確認
    public void checkPermission()
    {
        //すでに許可している場合
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)
        {
            locationActivity();
        }
        //拒否していた場合(初回起動含む)
        else
        {
            requestLocationPermission();
        }
    }

    //permission許可を求める
    private void requestLocationPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    R.string.message, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(Color.rgb(32, 125, 98));

            snackbar.setAction(R.string.sbar_button1, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSION);
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION);
        }
    }

    //許可申請結果の受け取り
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        if(requestCode == REQUEST_PERMISSION)
        {
            //許可された場合
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                locationActivity();
            }
            //拒否された場合
            else
            {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        R.string.massage2, Snackbar.LENGTH_SHORT);
                snackbar.setDuration(10000);
                snackbar.getView().setBackgroundColor(Color.rgb(255, 64, 0));

                snackbar.setAction(R.string.sbar_button2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intentによるアプリ権限の設定画面遷移
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                        //時間がかかってエラーになる場合があるため，待機状態にする．
                        Uri uri = Uri.fromParts("Package", BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        }
    }

    //IntentによるLocationActivity.javaでの測位
    private void locationActivity()
    {
        Intent intent = new Intent(getApplication(), LocationActivity.class);
        startActivity(intent);
    }
}
