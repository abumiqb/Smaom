package no.innovativdesign.smaom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import no.innovativdesign.smaom.R;
import java.util.HashMap;
import java.util.Map;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login);

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                loginUser();
            }
        });
    }

    public void login()
    {
        Log.d(TAG, "Login");

        if (!validate())
        {
            onLoginFailed();
            return;
        }
        loginUser();

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Laster inn..");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        new android.os.Handler().postDelayed
                (
                new Runnable()
                {
                    public void run()
                    {
                        onLoginSuccess();
                        //onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    public void onLoginSuccess()
    {
        _loginButton.setEnabled(true);

    }

    public void onLoginFailed()
    {
        Toast.makeText(getBaseContext(), "Feil ved logg inn, prøv igjen!", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate()
    {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() )
        {
            _emailText.setError("Skriv inn en gyldig e-post adresse");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10)
        {
            _passwordText.setError("Skriv inn riktig passord!");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void loginUser()
    {
        String url ="http://innovativdesign.no/smaom/LoginUser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();


                if(response.contains("Logg inn godkjent"))
                {
                    finish();

                }

                else
                {
                    validate();
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                if (volleyError instanceof TimeoutError)
                {
                    //SWAG
                }
            }
        }
        )
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("email", _emailText.getText().toString());
                headers.put("password", _passwordText.getText().toString());
                return headers;
            }

            @Override
            public Priority getPriority()
            {
                return Priority.IMMEDIATE;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}