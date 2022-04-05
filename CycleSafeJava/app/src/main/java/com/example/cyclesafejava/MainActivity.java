package com.example.cyclesafejava;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.ViewModels.BluetoothViewModel;
import com.example.cyclesafejava.ViewModels.SettingsViewModel;
import com.example.cyclesafejava.ViewModels.StatisticsViewModel;
import com.example.cyclesafejava.data.Events.Event;
import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.example.cyclesafejava.ui.main.SectionsPagerAdapter;
import com.example.cyclesafejava.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    //Services
    private BluetoothViewModel bluetoothViewModel;
    private SettingsViewModel settingsViewModel;
    private StatisticsViewModel statisticsViewModel;

    //checks
    private boolean bluetoothPermissionsGranted = false;
    private boolean connected = false;

    //Data
    private Settings settings;
    private Statistics statistics;

    //Request Codes
    private final int PERMISSIONS_REQUEST_BLUETOOTH = 0;

    //UI stuff
    private TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
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
        //Ui stuff

        //create stuff
        AppContainer appContainer = ((CycleSafe) getApplication()).appContainer;
        this.getBluetoothPermissions();
        this.bluetoothViewModel = new BluetoothViewModel(appContainer.bluetoothService);
        this.settingsViewModel = new SettingsViewModel(appContainer.settingsService);
        this.statisticsViewModel = new StatisticsViewModel(appContainer.statisticsService);
        this.settings = settingsViewModel.LoadSettings(this.getApplicationContext());
        this.statistics = statisticsViewModel.LoadStatistics(this.getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            EventBus.getDefault().register(this);
        }
       catch(Exception e){

       }
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            EventBus.getDefault().register(this);
        }
        catch(Exception e){

        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event e) {
        //do stuff here
        if(e.getmResultValue().equals(Event.BATTERY)){
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("CycleSafe Battery Low");
            alert.setMessage("Your battery is low for the CycleSafe equipment");
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }

    public void Connect(View view) {
        try {
            this.bluetoothViewModel.SetDeviceId(this.settings.DeviceID);
            this.connected = this.bluetoothViewModel.Initialize();
            Button ConnectButton = (Button) findViewById(R.id.connect_button);
            if (connected) {
                Logger.debug("Connected!");
                ConnectButton.setBackgroundColor(Color.GREEN);
                Button button = (Button) findViewById(R.id.Brake);
                ChangeColourOfButton(button, this.settings.Brake);
                button = (Button) findViewById(R.id.Turn);
                ChangeColourOfButton(button, this.settings.Turn);
                button = (Button) findViewById(R.id.Notify);
                ChangeColourOfButton(button, this.settings.BatteryNotif);
                this.bluetoothViewModel.StartListening();
            }
            else{
                ConnectButton.setBackgroundColor(Color.RED);
            }
        }
        catch (Exception e) {

        }
        return;
    }

    public void BrakeSwitch(View view) {
        this.settings.Brake = this.settingsViewModel.BrakeSwitch(this.settings);
        //save
        JsonFileHandler.writeSettings(this.settings, this.getFilesDir());
        //change colour
        Button brakeButton = (Button) findViewById(R.id.Brake);
        ChangeColourOfButton(brakeButton, this.settings.Brake);
        this.bluetoothViewModel.SendSettings(0);
        this.settingsViewModel.SaveSettings(this.getApplicationContext(), this.settings);
    }

    public void TurnSwitch(View view) {
        this.settings.Turn = this.settingsViewModel.TurnSwitch(this.settings);
        JsonFileHandler.writeSettings(this.settings, this.getFilesDir());
        Button turnButton = (Button) findViewById(R.id.Turn);
        ChangeColourOfButton(turnButton, this.settings.Turn);
        this.bluetoothViewModel.SendSettings(1);
    }

    public void BatteryNotifSwitch(View view) {
        this.settings.BatteryNotif = this.settingsViewModel.BatteryNotifSwitch(this.settings);
        JsonFileHandler.writeSettings(this.settings, this.getFilesDir());
        Button notifyButton = (Button) findViewById(R.id.Notify);
        ChangeColourOfButton(notifyButton, this.settings.BatteryNotif);
        this.bluetoothViewModel.SendSettings(2);
    }

    public void ChangeDeviceID(View view) {
        this.textInputLayout = findViewById(R.id.DeviceID);
        String ID = this.textInputLayout.getEditText().getText().toString().trim();
        this.bluetoothViewModel.SetDeviceId(ID);
        this.settingsViewModel.SetDeviceId(this.getApplicationContext(),this.settings, ID);
    }

    private void getBluetoothPermissions() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.*/

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

    private void ChangeColourOfButton(Button button , boolean value){
        if(value){
            button.setBackgroundColor(Color.GREEN);
        }
        else{
            button.setBackgroundColor(Color.RED);
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
