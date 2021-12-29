package com.example.cyclesafejava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment  extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settingsfragment_layout, container, false);
    }

    public void Connect(View view){
            BluetoothHandler handler = new BluetoothHandler(getContext(), this.getActivity());
            try{
                handler.Initialize();
            }
            catch(Exception e){

        }

    }
}
