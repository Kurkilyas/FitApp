package com.fitapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiActivity extends AppCompatActivity {

    private Button btnGetTime;
    private TextView txtView;

    private Retrofit retrofit;
    private TimeApi timeApi;
    private String baseUrl = "http://worldtimeapi.org/api/timezone/";
    private Call<TimeTurkey> timeTurkeyCall;
    private TimeTurkey timeTurkey;


    private void init(){
       // btnGetTime=findViewById(R.id.main_activity_btnGetTime);
        txtView=findViewById(R.id.main_activit_txtTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        init();
setRetrofitSettings();

        /*btnGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setRetrofitSettings();
            }
        });*/
    }

    private void setRetrofitSettings(){
retrofit=new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

  timeApi=retrofit.create(TimeApi.class);
  timeTurkeyCall=timeApi.getTime();

  timeTurkeyCall.enqueue(new Callback<TimeTurkey>() {
      @Override
      public void onResponse(Call<TimeTurkey> call, Response<TimeTurkey> response) {
          if(response.isSuccessful()){
              timeTurkey=response.body();

              if(timeTurkey!=null){
                 // txtView.setText(timeTurkey.getDateTime().split("T")[0]);
                  //txtView.setText(String.valueOf(timeTurkey.getDateDay()));
                  //txtView.setText(timeTurkey.getDateTimeZone());
                  String dateTime = timeTurkey.getDateTime();

                  String dateTimeZone = timeTurkey.getDateTimeZone();

                  String combinedText = "Date: " + dateTime   + "\nCity: " + dateTimeZone;
                  txtView.setText(combinedText);
              }
          }
      }

      @Override
      public void onFailure(Call<TimeTurkey> call, Throwable t) {

          System.out.println(t.toString());
      }
  });
    }

}
