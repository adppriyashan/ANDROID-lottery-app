package com.codetek.lottaryapp.Views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.lottaryapp.Controllers.AuthController;
import com.codetek.lottaryapp.Models.Utils;
import com.codetek.lottaryapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity  implements Validator.ValidationListener {

    private TextView register_back_login;
    @NotEmpty
    private EditText register_name;
    @NotEmpty
    private EditText register_mobile;
    @NotEmpty
    @Email
    private EditText register_email;
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText register_password;
    @ConfirmPassword
    private EditText register_retype_password;
    Button register_button;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        validator = new Validator(this);
        validator.setValidationListener(Register.this);
        initState();
    }

    private void initState() {
//        dataList1=Utils.getBloodTypeNameList();
//        dataList2=Utils.getDistrictNameList();

        register_name=findViewById(R.id.register_name);
        register_mobile=findViewById(R.id.register_mobile);
        register_email=findViewById(R.id.register_email);
        register_password=findViewById(R.id.register_password);
        register_retype_password=findViewById(R.id.register_retype_password);

        register_button=findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        register_back_login=findViewById(R.id.register_back_login);
        register_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    RequestQueue queue;
    private ProgressDialog progress;

    @Override
    public void onValidationSucceeded() {
        try {

            queue = Volley.newRequestQueue(this);
            progress=new ProgressDialog(this);

            register_button.setFocusable(true);


            progress.setMessage("Please wait");
            progress.show();
            StringRequest sr = new StringRequest(Request.Method.POST, Utils.getApiUrl()+"register", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress.hide();
                    try {
                        JSONObject responseObject=new JSONObject(response);
                        Toast.makeText(Register.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();

                        if( responseObject.getString("message").equals("Successfully registered")){
                            register_name.setText("");
                            register_email.setText("");
                            register_password.setText("");
                            register_retype_password.setText("");
                            register_mobile.setText("");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    progress.hide();
//                    Toast.makeText(Register.this, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> registerForm= new HashMap<String, String>();
                    registerForm.put("name",register_name.getText().toString());
                    registerForm.put("mobile",register_mobile.getText().toString());
                    registerForm.put("email",register_email.getText().toString());
                    registerForm.put("password",register_password.getText().toString());
                    return registerForm;
                }
            };
            queue.add(sr);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}