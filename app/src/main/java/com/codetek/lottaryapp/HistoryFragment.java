package com.codetek.lottaryapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryFragment extends Fragment {

    ArrayList<Lottery> dataList;
    RecyclerView history_recycler_view;

    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_library, container, false);

        queue = Volley.newRequestQueue(view.getContext());
        dataList=new ArrayList<>();

        history_recycler_view=view.findViewById(R.id.history_recycler_view);

        getData();

        return view;
    }

    private void getData() {
        StringRequest sr = new StringRequest(Request.Method.POST, Utils.getApiUrl()+"history/get", new Response.Listener<String>() {
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