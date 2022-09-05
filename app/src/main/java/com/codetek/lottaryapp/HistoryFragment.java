package com.codetek.lottaryapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.lottaryapp.Adapters.HistoryAdapter;
import com.codetek.lottaryapp.Models.DB.Lottery;
import com.codetek.lottaryapp.Models.Utils;
import com.codetek.lottaryapp.Views.NewLottery;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HistoryFragment extends Fragment {

    ArrayList<Lottery> dataList;
    RecyclerView history_recycler_view;
    EditText search_field;
    Button filter_btn;
    private int isLotto=2;
    Switch history_switch;
    public static TextView search_by_date,search_by_date2,income,loss;

    public static String selectedDate;

    private RequestQueue queue;

    public static boolean isFrom=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_library, container, false);

        queue = Volley.newRequestQueue(view.getContext());
        dataList=new ArrayList<>();

        history_recycler_view=view.findViewById(R.id.history_recycler_view);
        income=view.findViewById(R.id.income);
        loss=view.findViewById(R.id.loss);
        history_switch=view.findViewById(R.id.history_switch);
        filter_btn=view.findViewById(R.id.filter_btn);
        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        getData();

        search_field=view.findViewById(R.id.search_field);
        search_field.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>3) getData();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length()>3) getData();

            }
        });


        search_by_date=view.findViewById(R.id.search_by_date);
        search_by_date2=view.findViewById(R.id.search_by_date2);
        search_by_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFrom=true;
                DialogFragment newFragment = new DatePickerFragment1();
                newFragment.show(getActivity().getSupportFragmentManager(), "Date");
            }
        });
        search_by_date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFrom=false;
                DialogFragment newFragment = new DatePickerFragment1();
                newFragment.show(getActivity().getSupportFragmentManager(), "Date");
            }
        });


        return view;
    }

    private void getData() {
        StringRequest sr = new StringRequest(Request.Method.POST, Utils.getApiUrl()+"history/get", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataList.clear();
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    income.setText(jsonObject.getString("profit"));
                    loss.setText(jsonObject.getString("loss"));

                    JSONArray dataArray=jsonObject.getJSONArray("data");
                    for (int x=0;x<dataArray.length();x++){
                        JSONObject data= (JSONObject) dataArray.get(x);
                        dataList.add(new Lottery(data.getInt("id"), data.getString("name"),data.getString("drawno"),data.getString("number1"), data.getString("number2") , data.getString("number3"), data.getString("number4"),data.getString("letter") , data.getString("letter"), data.getString("serial"), data.getString("date"), (data.getInt("type")==2)?true:false , data.getDouble("price")));
                    }
                    HistoryAdapter historyAdapter=new HistoryAdapter(getContext(),dataList);
                    history_recycler_view.setHasFixedSize(true);
                    history_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
                    history_recycler_view.setAdapter(historyAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> searchMap=new HashMap<>();
                searchMap.put("user",String.valueOf(Utils.getUser().getId()));
                searchMap.put("search",search_field.getText().toString());
                searchMap.put("is_lotto",(history_switch.isChecked())?"1":"2");

                if(!search_by_date.getText().toString().isEmpty()){
                    searchMap.put("from",search_by_date.getText().toString());
                }

                if(!search_by_date2.getText().toString().isEmpty()){
                    searchMap.put("to",search_by_date2.getText().toString());
                }

                return searchMap;
            }
        };
        queue.add(sr);
    }

    public static class DatePickerFragment1 extends DialogFragment
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
                if(isFrom==true){
                    search_by_date.setText(new SimpleDateFormat("yyyy/MM/dd").format(new SimpleDateFormat("yyyy/MM/dd").parse(selectedDate)));
                }else{
                    search_by_date2.setText(new SimpleDateFormat("yyyy/MM/dd").format(new SimpleDateFormat("yyyy/MM/dd").parse(selectedDate)));
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
}