package com.fitapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.security.Key;

public class MainActivityTemp extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    private ActionBarDrawerToggle mToggle;

    private TextView emailTextView;
    SharedPreferences sharedPreferences;
    private static final String Shared_prefence_name="mypref";
    private static final String Key_Email="email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_temp);
        LayoutInflater inflater = LayoutInflater.from(this);
        View headerView = inflater.inflate(R.layout.nav_header_temp, null);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_drawer2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        replaceFragment(new HomeFragment()); //ilk home fragmenti gelicek

        navigationView=findViewById(R.id.navigate_bar);
        navigationView.addHeaderView(headerView);
        toolbar=findViewById(R.id.toolbar2);
        drawerLayout=findViewById(R.id.main_drawer2);
        setSupportActionBar(toolbar);
        bottomNavigationView=findViewById(R.id.bottomNavigationView2);
        emailTextView=headerView.findViewById(R.id.text_Email);
        sharedPreferences=getSharedPreferences(Shared_prefence_name,MODE_PRIVATE);
        String email=sharedPreferences.getString(Key_Email,null);
        if(email!=null)
        {
            emailTextView.setText(email);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Email bilgisi alınamadı",Toast.LENGTH_SHORT).show();
            emailTextView.setText(" ");
        }







//        if (email != null) {
//            emailTextView.setText(email);
//            System.out.println(email);
//
//
//            Toast.makeText(getApplicationContext(),"Başarılı",Toast.LENGTH_LONG).show();
//
//        } else {
//            // Eğer email değeri alınamazsa, varsayılan bir değer belirleyebilir veya hata mesajı gösterebilirsiniz.
//            emailTextView.setText("No email found");
//            // veya
//            Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show();
//        }











        mToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.nav_count)
                {
                    replaceFragment(new CountingCalorieFragment());
                }
                else if(menuItem.getItemId()==R.id.nav_timer)
                {
                    replaceFragment(new TimerFragment());

                }
                else if(menuItem.getItemId()==R.id.nav_step_counter)
                {
                    replaceFragment(new SubscriptionsFragment());
                }
                else if(menuItem.getItemId()==R.id.logout)
                {
                    Intent intent=new Intent(MainActivityTemp.this,LoginActivity.class);
                    startActivity(intent);

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if(itemId == R.id.home){
                replaceFragment(new HomeFragment());

            }else if(itemId == R.id.maps){
                replaceFragment(new MapsFragment());
            }else if (itemId == R.id.header_music){
                replaceFragment(new MusicMainFragment());
            }else if (itemId == R.id.weather_forecast){
                replaceFragment(new WeatherMainActivity());
            }

            return true;
        });

    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout2, fragment);
        fragmentTransaction.commit();
    }

}
