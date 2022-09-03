package com.codetek.lottaryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.codetek.lottaryapp.Views.Results;
import com.codetek.lottaryapp.Views.Scan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    Button scan_button,result_view;

    ArrayList<Lottery> dataList;
    RecyclerView history_recycler_view;

    TextView home_fragment_name,home_fragment_email;

    ConstraintLayout dashboard_new_lottery;

    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        queue = Volley.newRequestQueue(view.getContext());
        dataList=new ArrayList<>();

        history_recycler_view=view.findViewById(R.id.dashboard_results);

        home_fragment_name=view.findViewById(R.id.home_fragment_name);
        home_fragment_email=view.findViewById(R.id.home_fragment_email);

        home_fragment_name.setText(Utils.getUser().getName());
        home_fragment_email.setText(Utils.getUser().getEmail());

        dashboard_new_lottery=view.findViewById(R.id.dashboard_new_lottery);
        dashboard_new_lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext() , NewLottery.class));
            }
        });

        getData();

        return view;
    }

    private void getData() {
        StringRequest sr = new StringRequest(Request.Method.POST, Utils.getApiUrl()+"history/generated/get", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataList.clear();
                try {
                    JSONArray dataArray=new JSONArray(response);
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
                return searchMap;
            }
        };
        queue.add(sr);
    }
}