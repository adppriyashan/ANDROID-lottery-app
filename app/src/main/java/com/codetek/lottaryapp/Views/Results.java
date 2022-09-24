package com.codetek.lottaryapp.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.codetek.lottaryapp.Models.ZodanSymbols;
import com.codetek.lottaryapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Results extends AppCompatActivity {

    private ImageView results_image,results_back,results_letter_or_symbol_image,results_actual_letter_symbol_image;
    private TextView results_ticket_name,price_won,results_drawer,results_seriel,actual_results_info,results_date,results_number1,results_number2,results_number3,results_number4,results_letter_or_symbol,results_actual_number1,results_actual_number2,results_actual_number3,results_actual_number4,results_actual_letter_symbol,results_price;

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
        results_letter_or_symbol_image=findViewById(R.id.results_letter_or_symbol_image);
        results_actual_number1=findViewById(R.id.results_actual_number1);
        results_actual_number2=findViewById(R.id.results_actual_number2);
        results_actual_number3=findViewById(R.id.results_actual_number3);
        results_actual_number4=findViewById(R.id.results_actual_number4);
        results_actual_letter_symbol=findViewById(R.id.results_actual_letter_symbol);
        results_actual_letter_symbol_image=findViewById(R.id.results_actual_letter_symbol_image);
        results_price=findViewById(R.id.results_price);
        price_won=findViewById(R.id.price_won);

        actual_results_info=findViewById(R.id.actual_results_info);

        results_ticket_name.setText(Utils.scanned.getName());
        results_drawer.setText(Utils.scanned.getDraw_no());
        results_seriel.setText(Utils.scanned.getSerial());
        results_date.setText(Utils.scanned.getDate());
        results_number1.setText(Utils.scanned.getNumber1());
        results_number2.setText(Utils.scanned.getNumber2());
        results_number3.setText(Utils.scanned.getNumber3());
        results_number4.setText(Utils.scanned.getNumber4());

        results_number1.setTextColor(getColor(R.color.primary));
        results_number2.setTextColor(getColor(R.color.primary));
        results_number3.setTextColor(getColor(R.color.primary));
        results_number4.setTextColor(getColor(R.color.primary));
        results_letter_or_symbol.setTextColor(getColor(R.color.primary));

        results_actual_number1.setTextColor(getColor(R.color.primary));
        results_actual_number2.setTextColor(getColor(R.color.primary));
        results_actual_number3.setTextColor(getColor(R.color.primary));
        results_actual_number4.setTextColor(getColor(R.color.primary));
        results_actual_letter_symbol.setTextColor(getColor(R.color.primary));

        if(Utils.scanned.getLetter()==null){
            results_letter_or_symbol.setText(Utils.scanned.getSymbol());
        }else{
            results_letter_or_symbol.setText(Utils.scanned.getLetter());
        }

        Drawable drawable= ZodanSymbols.getSymbol(results_letter_or_symbol.getText().toString(),this);

        if(drawable!=null){
            results_letter_or_symbol.setVisibility(View.INVISIBLE);
            results_letter_or_symbol_image.setImageDrawable(drawable);
        }else{
            results_letter_or_symbol_image.setVisibility(View.INVISIBLE);
            results_letter_or_symbol.setVisibility(View.VISIBLE);
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

                    if(jsonObject.getBoolean("has_ticket")==true){
                        JSONObject winningLottery=jsonObject.getJSONObject("ticket");

                        actual_results_info.setText("Actual Result");
                        actual_results_info.setTextColor(getColor(R.color.secondaryDark));
                        results_actual_number1.setText(winningLottery.getString("number1"));
                        results_actual_number2.setText(winningLottery.getString("number2"));
                        results_actual_number3.setText(winningLottery.getString("number3"));
                        results_actual_number4.setText(winningLottery.getString("number4"));
                        results_actual_letter_symbol.setText(winningLottery.getString("letter"));

                        Drawable drawable= ZodanSymbols.getSymbol(winningLottery.getString("letter"),Results.this);

                        if(drawable!=null){
                            results_actual_letter_symbol.setVisibility(View.INVISIBLE);
                            results_actual_letter_symbol_image.setImageDrawable(drawable);
                        }else{
                            results_actual_letter_symbol_image.setVisibility(View.INVISIBLE);
                            results_actual_letter_symbol.setVisibility(View.VISIBLE);
                        }

                        if(Utils.scanned.getNumber1().equals(winningLottery.getString("number1"))){
                            results_actual_number1.setTextColor(getColor(R.color.purple_200));
                            results_number1.setTextColor(getColor(R.color.purple_200));
                        }

                        if(Utils.scanned.getNumber2().equals(winningLottery.getString("number2"))){
                            results_actual_number2.setTextColor(getColor(R.color.purple_200));
                            results_number2.setTextColor(getColor(R.color.purple_200));
                        }

                        if(Utils.scanned.getNumber3().equals(winningLottery.getString("number3"))){
                            results_actual_number3.setTextColor(getColor(R.color.purple_200));
                            results_number3.setTextColor(getColor(R.color.purple_200));
                        }

                        if(Utils.scanned.getNumber4().equals(winningLottery.getString("number4"))){
                            results_actual_number4.setTextColor(getColor(R.color.purple_200));
                            results_number4.setTextColor(getColor(R.color.purple_200));
                        }

                        if(Utils.scanned.getLetter().equals(winningLottery.getString("letter"))){
                            results_actual_letter_symbol.setTextColor(getColor(R.color.purple_200));
                            results_letter_or_symbol.setTextColor(getColor(R.color.purple_200));
                        }

                    }else{
                        price_won.setVisibility(View.INVISIBLE);
                        results_actual_letter_symbol_image.setVisibility(View.INVISIBLE);
                        results_price.setVisibility(View.INVISIBLE);
                        actual_results_info.setText("No Results Found");
                        actual_results_info.setTextColor(getColor(R.color.purple_200));
                        results_actual_number1.setVisibility(View.INVISIBLE);
                        results_actual_number2.setVisibility(View.INVISIBLE);
                        results_actual_number3.setVisibility(View.INVISIBLE);
                        results_actual_number4.setVisibility(View.INVISIBLE);
                        results_actual_letter_symbol.setVisibility(View.INVISIBLE);
                    }

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
                        Toast.makeText(Results.this, body, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if(error.networkResponse!=null && error.networkResponse.statusCode!=422){
                    Toast.makeText(Results.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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