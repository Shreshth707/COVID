package com.example.ceasepandemic.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ceasepandemic.GPS_SERVICE;
import com.example.ceasepandemic.HomeActivity;
import com.example.ceasepandemic.MainActivity;
import com.example.ceasepandemic.R;
import com.google.android.material.chip.ChipGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    View SettingsFragment;
    View mUpdateInfo;
    private SwitchCompat location_switch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SettingsFragment = inflater.inflate(R.layout.fragment_settings, container, false);
        mUpdateInfo = SettingsFragment.findViewById(R.id.update_info);
        location_switch = SettingsFragment.findViewById(R.id.location_switch);

        mUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        location_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location_switch.isChecked()){
                    //start service
                    Intent service = new Intent (getContext(), GPS_SERVICE.class);
                    getActivity().startService(service);
                }else {
                    //end service
                    Intent service = new Intent (getContext(),GPS_SERVICE.class);
                    getActivity().stopService(service);
                }
            }
        });

        return SettingsFragment;
    }

}
