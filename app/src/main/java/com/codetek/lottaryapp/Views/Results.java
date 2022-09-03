package com.codetek.lottaryapp.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.lottaryapp.Models.DB.Lottery;
import com.codetek.lottaryapp.Models.DB.User;
import com.codetek.lottaryapp.Models.Utils;
import com.codetek.lottaryapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Results extends AppCompatActivity {

    private ImageView results_image,results_back;
    private TextView results_ticket_name,results_drawer,results_seriel,results_date,results_number1,results_number2,results_number3,results_number4,results_letter_or_symbol,results_actual_number1,results_actual_number2,results_actual_number3,results_actual_number4,results_actual_letter_symbol,results_price;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        queue = Volley.newRequestQueue(this);

        results_image=findViewById(R.id.results_image);
        results_ticket_name=findViewById(R.id.results_ticket_name);
        results_drawer=findViewById(R.id.results_drawer);
        results_seriel=findViewById(R.id.results_seriel);
        results_date=findViewById(R.id.results_date);
        results_number1=findViewById(R.id.results_number1);
        results_number2=findViewById(R.id.results_number2);
        results_number3=findViewById(R.id.results_number3);
        results_number4=findViewById(R.id.results_number4);
        results_letter_or_symbol=findViewById(R.id.results_letter_or_symbol);
        results_actual_number1=findViewById(R.id.results_actual_number1);
        results_actual_number2=findViewById(R.id.results_actual_number2);
        results_actual_number3=findViewById(R.id.results_actual_number3);
        results_actual_number4=findViewById(R.id.results_actual_number4);
        results_actual_letter_symbol=findViewById(R.id.results_actual_letter_symbol);
        results_price=findViewById(R.id.results_price);

        results_ticket_name.setText(Utils.scanned.getName());
        results_drawer.setText(Utils.scanned.getDraw_no());
        results_seriel.setText(Utils.scanned.getSerial());
        results_date.setText(Utils.scanned.getDate());
        results_number1.setText(Utils.scanned.getNumber1());
        results_number2.setText(Utils.scanned.getNumber2());
        results_number3.setText(Utils.scanned.getNumber3());
        results_number4.setText(Utils.scanned.getNumber4());

        if(Utils.scanned.getLetter()==null){
            results_letter_or_symbol.setText(Utils.scanned.getSymbol());
        }else{
            results_letter_or_symbol.setText(Utils.scanned.getLetter());
        }

        results_image.setImageDrawable(Lottery.getLotteryImage(this,Utils.scanned.getName()));

        results_back=findViewById(R.id.results_back);
        results_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        StringRequest sr = new StringRequest(Request.Method.POST, Utils.getApiUrl()+"history/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    results_price.setText(jsonObject.getString("price"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                String body;
                if(error.networkResponse!=null && error.networkResponse.data!=null) {
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        Log.println(Log.DEBUG,"debug", body);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if(error.networkResponse!=null && error.networkResponse.statusCode!=422){
                    Log.println(Log.DEBUG,"debug", error.getMessage());
                }else{
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> data=new HashMap<>();
                data.put("name",Utils.scanned.getName());
                data.put("serial",Utils.scanned.getSerial());
                data.put("drawno",Utils.scanned.getDraw_no());
                data.put("number1",Utils.scanned.getNumber1());
                data.put("number2",Utils.scanned.getNumber2());
                data.put("number3",Utils.scanned.getNumber3());
                data.put("number4",Utils.scanned.getNumber4());
                data.put("date",Utils.scanned.getDate());
                data.put("user",String.valueOf(Utils.getUser().getId()));
                if(Utils.scanned.getLetter()==null){
                    data.put("letter",Utils.scanned.getSymbol());
                }else{
                    data.put("letter",Utils.scanned.getLetter());
                }
                if(Utils.scanned.getName().toLowerCase().equals("lotto")){
                    data.put("type","2");
                }else{
                    data.put("type","1");
                }
                data.put("status","1");
                data.put("is_result","false");
                return data;
            }
        };
        queue.add(sr);

    }
}