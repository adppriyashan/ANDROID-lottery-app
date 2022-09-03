package com.codetek.lottaryapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codetek.lottaryapp.Controllers.AuthController;
import com.codetek.lottaryapp.Models.LotteryIdentification;
import com.codetek.lottaryapp.Models.Utils;
import com.codetek.lottaryapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity  implements Validator.ValidationListener {

    boolean doubleBackToExitPressedOnce = false;

    Button login_create_account;
    @NotEmpty
    EditText login_username;
    @NotEmpty
    EditText login_password;
    Button login_button;
    CheckBox login_remember_password;

    private Validator validator;

    private AuthController authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initState();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void initState() {
        authController=new AuthController(Login.this, Utils.getApiUrl()+"login");

        validator = new Validator(this);
        validator.setValidationListener(Login.this);

        login_create_account=findViewById(R.id.login_create_account);
        login_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
        login_username=findViewById(R.id.login_username);
        login_password=findViewById(R.id.login_password);
        login_username.setText("user@gmail.com");
        login_password.setText("User@123");

        login_button=findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
        login_remember_password=findViewById(R.id.login_remember_password);
    }

    @Override
    public void onValidationSucceeded() {
        try {
            Map<String,String> loginForm= new HashMap<String, String>();
            loginForm.put("email",login_username.getText().toString());
            loginForm.put("password",login_password.getText().toString());
            authController.doLogin(loginForm);
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