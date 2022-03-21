package com.example.cyclesafejava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.ViewModels.SettingsViewModel;
import com.example.cyclesafejava.data.Settings;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SettingsFragment  extends Fragment {
    private Settings settings;
    private SettingsViewModel settingsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.settingsfragment_layout, container, false);

        this.LoadStoredData();
        TextInputLayout textEdit = (TextInputLayout)ll.findViewById(R.id.DeviceID);
        textEdit.setHint(settings.DeviceID);
        return ll;
    }

    public void LoadStoredData(){
        String directory = getActivity().getApplicationInfo().dataDir;
        this.settings = JsonFileHandler.readSettings(directory);
    }

}
