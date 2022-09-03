package com.codetek.lottaryapp.Views;

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

import com.codetek.lottaryapp.Controllers.AuthController;
import com.codetek.lottaryapp.Models.Utils;
import com.codetek.lottaryapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity  implements Validator.ValidationListener {

    private TextView register_back_login;
    @NotEmpty
    private EditText register_name;
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

    @Override
    public void onValidationSucceeded() {
        try {
            register_button.setFocusable(true);
            Map<String,String> registerForm= new HashMap<String, String>();
            registerForm.put("name",register_name.getText().toString());
            registerForm.put("email",register_email.getText().toString());
            registerForm.put("password",register_password.getText().toString());

           new AuthController(this, Utils.getApiUrl()+"register").doRegister(registerForm);

            register_name.setText("");
            register_email.setText("");
            register_password.setText("");
            register_retype_password.setText("");

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