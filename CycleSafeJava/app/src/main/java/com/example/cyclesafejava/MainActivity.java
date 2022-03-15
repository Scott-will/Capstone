package com.example.cyclesafejava;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.cyclesafejava.Bluetooth.BluetoothHandler;
import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cyclesafejava.ui.main.SectionsPagerAdapter;
import com.example.cyclesafejava.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextInputLayout textInputLayout;
    private BluetoothHandler handler;
    ListView statisticsList;
    private Settings settings;
    private Statistics statistics;
    private static final int PERMISSIONS_REQUEST_BLUETOOTH = 2;
    private boolean bluetoothPermissionsGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        this.getBluetoothPermissions();
        this.handler = new BluetoothHandler(this.getApplicationContext(), this, bluetoothPermissionsGranted);
    }



    public void Connect(View view){
        try{

            this.handler.Initialize();
        }
        catch(Exception e){

        }

    }

    public void ChangeDeviceID(View view){
        this.textInputLayout = findViewById(R.id.DeviceID);
        String ID = this.textInputLayout.getEditText().getText().toString().trim();
        this.handler.SetDeviceID(ID);
    }

    public void LoadStoredData(){
        Logger.debug("Loading Data");
        String directory = getApplicationInfo().dataDir;
        this.statistics = JsonFileHandler.readStatistics(directory);
        this.settings = JsonFileHandler.readSettings(directory);
    }

    private void getBluetoothPermissions() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.BLUETOOTH)
                == PackageManager.PERMISSION_GRANTED) {
            bluetoothPermissionsGranted = true;
            Logger.debug("permission granted for location");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    PERMISSIONS_REQUEST_BLUETOOTH);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        bluetoothPermissionsGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_BLUETOOTH) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bluetoothPermissionsGranted = true;
                Logger.debug("permission granted for location");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}