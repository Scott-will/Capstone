package com.example.cyclesafejava;

import android.os.Bundle;

import com.example.cyclesafejava.Bluetooth.BluetoothHandler;
import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.data.RideInformation;
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

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private ActivityMainBinding binding;
    private TextInputLayout textInputLayout;
    private BluetoothHandler blutoothHandler;
    private ListView statistics_ListView;
    private Settings settings;
    private Statistics statistics;
    private RideInformation rideInfo;

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
        LoadStoredData();
        CreateStatisticsList();
        this.blutoothHandler = new BluetoothHandler(this.getApplicationContext(), this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void Connect(View view){
        try{
            this.blutoothHandler.Initialize();
        }
        catch(Exception e){

        }
    }

    public void ChangeDeviceID(View view){
        this.textInputLayout = findViewById(R.id.DeviceID);
        String ID = this.textInputLayout.getEditText().getText().toString().trim();
        this.blutoothHandler.SetDeviceID(ID);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.println(Log.INFO,"Request Granted", "Request Granted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.println(Log.ERROR,"Request Denied", "Request Denied");
    }

    private void LoadStoredData(){
        this.statistics = JsonFileHandler.readStatistics();
        this.settings = JsonFileHandler.readSettings();
    }

    private void CreateStatisticsList(){
        statistics_ListView = (ListView)findViewById(R.id.Statistics_List);
        ArrayList<Double> statisticsArray = new ArrayList<Double>();
        statisticsArray.add(this.statistics.LongestRide);
        statisticsArray.add(this.statistics.TotalDistance);
        statisticsArray.add(this.statistics.FastestSpeed);
        ArrayAdapter statisticsArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, statisticsArray);
        statistics_ListView.setAdapter(statisticsArrayAdapter);
    }
}