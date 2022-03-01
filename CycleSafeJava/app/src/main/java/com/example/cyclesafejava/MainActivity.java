package com.example.cyclesafejava;

import android.content.Intent;
import android.os.Bundle;

import com.example.cyclesafejava.Bluetooth.BluetoothHandler;
import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private ActivityMainBinding binding;
    private TextInputLayout textInputLayout;
    private BluetoothHandler handler;
    ListView statisticsList;
    private Settings settings;
    private Statistics statistics;

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
        this.handler = new BluetoothHandler(this.getApplicationContext(), this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.println(Log.INFO,"Request Granted", "Request Granted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.println(Log.ERROR,"Request Denied", "Request Denied");
    }

    public void LoadStoredData(){
        Logger.debug("Loading Data");
        String directory = getApplicationInfo().dataDir;
        this.statistics = JsonFileHandler.readStatistics(directory);
        this.settings = JsonFileHandler.readSettings(directory);
    }



}