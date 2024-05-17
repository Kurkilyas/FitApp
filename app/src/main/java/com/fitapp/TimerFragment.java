package com.fitapp;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.fragment.app.Fragment;

public class TimerFragment extends Fragment {

    long fark;
    Chronometer kronometre;
    Button basladur, restart;

    int deger = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        kronometre = view.findViewById(R.id.kronometre);
        basladur = view.findViewById(R.id.basladur);
        restart = view.findViewById(R.id.restart);


        basladur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deger == 0) {
                  kronometre.setBase(SystemClock.elapsedRealtime());
                    kronometre.start();
                    kronometre.setText("Stop");
                    deger = 1;
                } else if (deger == 1) {
                    fark = SystemClock.elapsedRealtime();
                    kronometre.stop();
                    kronometre.setText("Resume");
                    deger = 2;
                } else {
                    kronometre.setBase(kronometre.getBase() + SystemClock.elapsedRealtime() - fark);
                    basladur.setText("Stop");
                    kronometre.start();
                    deger= 1;
                }
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kronometre.setBase(SystemClock.elapsedRealtime());
                kronometre.start();
                basladur.setText("Stop");
                deger = 1;
            }
        });

        return view;
    }
}
