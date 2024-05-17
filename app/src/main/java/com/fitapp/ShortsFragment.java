package com.fitapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShortsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShortsFragment extends Fragment {

    private EditText editTextBoy, editTextKilo, editTextYas;
    private RadioGroup radioGroupCinsiyet;
    private RadioButton radioButtonErkek, radioButtonKadin;
    private TextView textViewKalori;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ShortsFragment() {
        // Required empty public constructor
    }

    public static ShortsFragment newInstance(String param1, String param2) {
        ShortsFragment fragment = new ShortsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referansları al
        editTextKilo = view.findViewById(R.id.editTextKilo);
        editTextYas = view.findViewById(R.id.editTextYas);
        editTextBoy = view.findViewById(R.id.editTextBoy);
        radioGroupCinsiyet = view.findViewById(R.id.radioGroupCinsiyet);
        radioButtonErkek = view.findViewById(R.id.radioButtonErkek);
        radioButtonKadin = view.findViewById(R.id.radioButtonKadin);
        textViewKalori = view.findViewById(R.id.textViewKalori);

        // Butonun tıklanma olayını dinle
        Button buttonHesapla = view.findViewById(R.id.buttonHesapla);
        buttonHesapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verileri al
                double kilo = Double.parseDouble(editTextKilo.getText().toString());
                int yas = Integer.parseInt(editTextYas.getText().toString());
                double boy = Double.parseDouble(editTextBoy.getText().toString()); // Boy değeri alındı
                boolean cinsiyet = radioButtonErkek.isChecked();

                // Kalori hesapla
                double kalori = hesaplaKalori(kilo, yas, cinsiyet, boy);

                // Sonucu göster
                textViewKalori.setText(String.format("%.0f", kalori));
            }
        });
    }
    private double hesaplaKalori(double kilo, int yas, boolean cinsiyet, double boy) {
        double kalori;
        if (cinsiyet) {
            kalori = 66.47 + (13.75 * kilo) + (5.0 * boy) - (6.76 * yas);
        } else {
            kalori = 655.1 + (9.56 * kilo) + (1.85 * boy) - (4.68 * yas);
        }
        return kalori;
    }
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shorts, container, false);
    }




}

//arris-Benedict Denklemleri olarak bilinen ve yaygın olarak kullanılan formüllerdir. Bu denklemler, ilk olarak 1919 yılında J. Arthur Harris ve Francis G. Benedict tarafından geliştirilmiştir.
//
//Harris-Benedict Denklemleri:
//
//Erkekler için: BMR = 66.47 + (13.75 × ağırlık (kg)) + (5.0 × boy (cm)) - (6.76 × yaş (yıl))
//
//Kadınlar için: BMR = 655.1 + (9.56 × ağırlık (kg)) + (1.85 × boy (cm)) - (4.68 × yaş (yıl))