package com.fitapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherMainActivity extends Fragment implements LocationListener {

    private TextView cityName;
    private Button search;
    private Button buton;
    private TextView show;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;

    private static final String OPEN_WEATHER_MAP_API =
            "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=84949b125d45f84805ee91ef5b2cc380";

    public WeatherMainActivity() {
        // Required empty public constructor
    }

   /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_weather, container, false);

        cityName = view.findViewById(R.id.cityName);
        search = view.findViewById(R.id.search);
        show = view.findViewById(R.id.weather);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button Clicked! ", Toast.LENGTH_SHORT).show();
                String city = cityName.getText().toString();
                try {
                    if (city != null) {
                        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=84949b125d45f84805ee91ef5b2cc380";
                        new GetWeatherTask().execute(url);
                    } else {
                        Toast.makeText(getActivity(), "Enter City", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_weather, container, false);

        cityName = view.findViewById(R.id.cityName);
        search = view.findViewById(R.id.search);
        show = view.findViewById(R.id.weather);

        // buton değişkenini tanımlayalım
        Button buton = view.findViewById(R.id.buton);

        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation(); // Butona tıklandığında konumu almak için bu işlemi gerçekleştiriyoruz
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button Clicked! ", Toast.LENGTH_SHORT).show();
                String city = cityName.getText().toString();
                try {
                    if (city != null) {
                        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=84949b125d45f84805ee91ef5b2cc380";
                        new GetWeatherTask().execute(url);
                    } else {
                        Toast.makeText(getActivity(), "Enter City", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }


    private void getLocation() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String apiURL = String.format(OPEN_WEATHER_MAP_API, latitude, longitude);
        new GetWeatherTask().execute(apiURL);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(requireContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject main = jsonObject.getJSONObject("main");
                    double tempKelvin = main.getDouble("temp");
                    double feelsLikeKelvin = main.getDouble("feels_like");
                    double tempMaxKelvin = main.getDouble("temp_max");
                    double tempMinKelvin = main.getDouble("temp_min");
                    int pressure = main.getInt("pressure");
                    int humidity = main.getInt("humidity");

                    // Kelvin'i Celsius'a dönüştürme
                    double tempCelsius = tempKelvin - 273.15;
                    double feelsLikeCelsius = feelsLikeKelvin - 273.15;
                    double tempMaxCelsius = tempMaxKelvin - 273.15;
                    double tempMinCelsius = tempMinKelvin - 273.15;

                    String weatherInfo = "Temperature: " + String.format("%.2f", tempCelsius) + "°C\n" +
                            "Feels Like: " + String.format("%.2f", feelsLikeCelsius) + "°C\n" +
                            "Max Temperature: " + String.format("%.2f", tempMaxCelsius) + "°C\n" +
                            "Min Temperature: " + String.format("%.2f", tempMinCelsius) + "°C\n" +
                            "Pressure: " + pressure + " hPa\n" +
                            "Humidity: " + humidity + "%";


                    show.setText(weatherInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                show.setText("Cannot retrieve weather data");
            }
        }
    }



}
