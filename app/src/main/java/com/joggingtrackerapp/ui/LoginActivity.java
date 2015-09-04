package com.joggingtrackerapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joggingtrackerapp.R;
import com.joggingtrackerapp.server.CheckLogin;
import com.joggingtrackerapp.server.SignUp;
import com.joggingtrackerapp.utils.Checks;
import com.joggingtrackerapp.utils.Utils;

public class LoginActivity extends Activity {
    private boolean isSignUpFormVisible = false;

    private TextView changeFormTV;
    private Button submitFormBT;
    private EditText emailET, passET, pass2ET;
    private Spinner levelSp;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        defineViews();

    }


    public void Submit (View v) {
        Utils.hideKeyboard(LoginActivity.this);
        if (isSignUpFormVisible) { // Sign up process
            String emailStr = emailET.getText().toString().trim();
            String passStr = passET.getText().toString().trim();
            String pass2Str = pass2ET.getText().toString().trim();
            String levelStr = String.valueOf(levelSp.getSelectedItemPosition());
            if (emailStr.length() <= 0 || passStr.length() <= 0 || pass2ET.length() <= 0) {
                Toast.makeText(this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Checks.isEmailValid(emailStr)) {
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass2Str.equals(passStr)) {
                Toast.makeText(this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                return;
            }
            new SignUp(LoginActivity.this).execute(emailStr, passStr, levelStr);
        } else { // Login process
            String emailStr = emailET.getText().toString().trim();
            String passStr = passET.getText().toString().trim();
            if (emailStr.length() <= 0 || passStr.length() <= 0) {
                Toast.makeText(this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Checks.isEmailValid(emailStr)) {
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
                return;
            }
            new CheckLogin(LoginActivity.this).execute(emailStr, passStr);
        }
    }

    private void defineViews () {
        changeFormTV = (TextView) findViewById(R.id.changeForm);
        submitFormBT = (Button) findViewById(R.id.submitForm);
        emailET = (EditText) findViewById(R.id.email);
        passET = (EditText) findViewById(R.id.pass);
        pass2ET = (EditText) findViewById(R.id.pass2);
        levelSp = (Spinner) findViewById(R.id.level);
    }

    public void changeForm (View v) {
        if (isSignUpFormVisible) {
            changeFormTV.setText(getResources().getString(R.string.signup));
            submitFormBT.setText(getResources().getString(R.string.login));
            pass2ET.setVisibility(View.GONE);
            levelSp.setVisibility(View.GONE);
            pass2ET.setText("");
            passET.setText("");
            emailET.setText("");
        } else {
            changeFormTV.setText(getResources().getString(R.string.login));
            submitFormBT.setText(getResources().getString(R.string.signup));
            pass2ET.setVisibility(View.VISIBLE);
            levelSp.setVisibility(View.VISIBLE);
            pass2ET.setText("");
            passET.setText("");
            emailET.setText("");
        }
        isSignUpFormVisible = !isSignUpFormVisible;
    }
}
