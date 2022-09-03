package com.codetek.lottaryapp.Controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.lottaryapp.Models.DB.User;
import com.codetek.lottaryapp.Models.Utils;
import com.codetek.lottaryapp.Views.Dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class AuthController {
    private ProgressDialog progress;
    private String url;
    public Context context;
    RequestQueue queue;

    public AuthController(Context context, String url){
        this.url=url;
        this.context=context;
        queue = Volley.newRequestQueue(this.context);
        progress=new ProgressDialog(context);
    }

    public void doRegister(Map<String,String> data) {
        progress.setMessage("Please wait");
        progress.show();
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();
                try {
                    JSONObject responseObject=new JSONObject(response);
                    Toast.makeText(context, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                progress.hide();
                Toast.makeText(context, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return data;
            }
        };
        queue.add(sr);
    }

    public void doLogin(Map<String,String> data) {
        progress.setMessage("Please wait");
        progress.show();
        System.out.println(url);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                progress.hide();
                try {
                    JSONObject responseObject=new JSONObject(response);
                    Utils.setUser(new User(responseObject.getInt("id"),responseObject.getString("name"),responseObject.getString("email"), responseObject.getInt("usertype")));
                    System.out.println("Logged user - "+Utils.getUser().getEmail());
                    context.startActivity(new Intent(context, Dashboard.class));
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
                        System.out.println(body);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                progress.hide();
                if(error.networkResponse!=null && error.networkResponse.statusCode!=422){
                    System.out.println(error.getMessage());
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return data;
            }
        };
        queue.add(sr);
    }
}
