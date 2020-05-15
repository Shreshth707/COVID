package com.example.ceasepandemic.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ceasepandemic.GPS_SERVICE;
import com.example.ceasepandemic.HomeActivity;
import com.example.ceasepandemic.MainActivity;
import com.example.ceasepandemic.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    View SettingsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SettingsFragment = inflater.inflate(R.layout.fragment_settings, container, false);
        Button mUpdateInfo = SettingsFragment.findViewById(R.id.update_info);
        mUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return SettingsFragment;
    }

    private Button mStop;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Code

    }
}
