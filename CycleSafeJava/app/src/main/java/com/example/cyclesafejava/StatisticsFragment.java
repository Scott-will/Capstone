package com.example.cyclesafejava;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.data.Statistics;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

    private Statistics statistics;
    private ListView statisticsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.statisticsfragment_layout, container, false);
        this.LoadStoredData();
        this.CreateListView(ll);
        //some code
        return ll;
    }

    public void LoadStoredData(){
        String directory = getActivity().getApplicationInfo().dataDir;
        this.statistics = JsonFileHandler.readStatistics(directory);
    }

    public void CreateListView(LinearLayout ll){
        statisticsList = ll.findViewById(R.id.StatisticsList);

        ArrayList<String> statisticsArray = new ArrayList<>();
        statisticsArray.add("Fastest Speed: " + this.statistics.FastestSpeed);
        statisticsArray.add("Total Distance: " + this.statistics.TotalDistance);
        statisticsArray.add("Longest Ride: " + this.statistics.LongestRide);

        ArrayAdapter statisticsAdapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.custom_listitem, statisticsArray);
        try{
            statisticsList.setAdapter(statisticsAdapter);
        }
        catch (Exception e){

        }
    }
}
