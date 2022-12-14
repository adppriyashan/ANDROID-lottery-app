package com.codetek.lottaryapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.lottaryapp.Models.Utils;
import com.codetek.lottaryapp.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewLottery extends AppCompatActivity {

    public static String selectedDate;
    public static SimpleDateFormat simpleDateFormat;

    static TextView generate_date;

    RequestQueue queue;
    private ProgressDialog progress;

    private Button generate_btn,generate_buy_btn;
    private EditText generate_number1,generate_number2,generate_number3,generate_number4,generate_letter;
    private ImageView new_lottery_back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codetek.lottaryapp.R.layout.activity_new_lottery);

        simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");


        progress=new ProgressDialog(this);

        generate_date=findViewById(R.id.generate_date);
        generate_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Date");
            }
        });

        generate_number1=findViewById(R.id.generate_number1);
        generate_number2=findViewById(R.id.generate_number2);
        generate_number3=findViewById(R.id.generate_number3);
        generate_number4=findViewById(R.id.generate_number4);
        generate_letter=findViewById(R.id.generate_letter);

        new_lottery_back_btn=findViewById(R.id.new_lottery_back_btn);
        new_lottery_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        disableEditing();


        generate_btn=findViewById(R.id.generate_btn);
        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queue = Volley.newRequestQueue(NewLottery.this);
                if(selectedDate!=null){

                    progress.setMessage("Please wait while we generating lottery.");
                    progress.show();
                    StringRequest sr = new StringRequest(Request.Method.POST, Utils.getApiUrl()+"lottery/generate", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress.hide();

                            try {
                                JSONObject responseObject=new JSONObject(response);

                                if(responseObject.getInt("code")==200){
                                    generate=true;
                                    JSONObject generatedNumbers=responseObject.getJSONObject("data");
                                    generate_number1.setText(String.valueOf(generatedNumbers.getInt("number1")));
                                    generate_number2.setText(String.valueOf(generatedNumbers.getInt("number2")));
                                    generate_number3.setText(String.valueOf(generatedNumbers.getInt("number3")));
                                    generate_number4.setText(String.valueOf(generatedNumbers.getInt("number4")));
                                    generate_letter.setText(generatedNumbers.getString("letter"));
                                    enableEditing();
                                }else{
                                    Toast.makeText(NewLottery.this, "Please use valid date.", Toast.LENGTH_SHORT).show();
                                }

                                Toast.makeText(NewLottery.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.getMessage());
                            progress.hide();


                            Toast.makeText(NewLottery.this, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> data=new HashMap<>();
                            data.put("date",selectedDate);
                            data.put("user",String.valueOf(Utils.getUser().getId()));
                            return data;
                        }
                    };
                    queue.add(sr);

                }else{
                    disableEditing();
                }
            }
        });

        generate_buy_btn=findViewById(R.id.generate_buy_btn);
        generate_buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DefaultRetryPolicy  retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                queue = Volley.newRequestQueue(NewLottery.this);
                if(generate==false){
                    Toast.makeText(NewLottery.this, "Please generate new lottery after date changed", Toast.LENGTH_SHORT).show();
                }else{
                    if(selectedDate!=null && !generate_number1.getText().toString().isEmpty()){

                        progress.setMessage("Please wait till reserve your ticket.");
                        progress.show();
                        StringRequest sr = new StringRequest(Request.Method.POST, Utils.getApiUrl()+"lottery/save", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progress.hide();
                                try {
                                    JSONObject responseObject=new JSONObject(response);
                                    if(responseObject.getInt("code")==200){
                                        Toast.makeText(getApplicationContext(), "Ticket Reserved", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }else{
                                        Toast.makeText(NewLottery.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.getMessage());
                                progress.hide();
                                Toast.makeText(NewLottery.this, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {



                                Map<String, String> data=new HashMap<>();
                                data.put("number1",generate_number1.getText().toString());
                                data.put("number2",generate_number2.getText().toString());
                                data.put("number3",generate_number3.getText().toString());
                                data.put("number4",generate_number4.getText().toString());
                                data.put("letter",generate_letter.getText().toString());
                                data.put("date",selectedDate);
                                data.put("user",String.valueOf(Utils.getUser().getId()));

                                return data;
                            }
                        };

                        sr.setRetryPolicy(retryPolicy);

                        queue.add(sr);

                    }else{
                        disableEditing();
                    }
                }

            }
        });
    }

    private void disableEditing() {
        generate_number1.setEnabled(false);
        generate_number2.setEnabled(false);
        generate_number3.setEnabled(false);
        generate_number4.setEnabled(false);
        generate_letter.setEnabled(false);
    }

    private void enableEditing() {
        generate_number1.setEnabled(true);
        generate_number2.setEnabled(true);
        generate_number3.setEnabled(true);
        generate_number4.setEnabled(true);
        generate_letter.setEnabled(true);
    }
    
    public static boolean generate=false;
    
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            selectedDate=year+"/"+ StringUtils.leftPad(String.valueOf((month+1)),2).replace(" ","0") +"/"+StringUtils.leftPad(String.valueOf((day)),2).replace(" ","0");
            System.out.println(selectedDate);
            try {
                generate_date.setText(simpleDateFormat.format(simpleDateFormat.parse(selectedDate)));
                generate=false;
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
}