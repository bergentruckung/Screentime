package cetchoriyans.screentime;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joseph on 18/2/16.
 */
public class SignupActivity extends LoginActivity{
    protected EditText confirmPassword,mName;
    protected SignupTask signupTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEmailView.setText(getIntent().getExtras().getString("email"));
        mPasswordView.setText(getIntent().getExtras().getString("pass"));
    }
    public SignupActivity()
    {

        layout=R.layout.signup;
    }
    @Override
    protected void attemptLogin() {
        confirmPassword=(EditText)findViewById(R.id.confirmpassword);
        mName=(EditText)findViewById(R.id.name);

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirm= confirmPassword.getText().toString();
        String name=mName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            mName.setError("Must not be empty");
            focusView = mName;
            cancel = true;
        }
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (!isPasswordConfirmed(password, confirm)) {
            confirmPassword.setError("Password not confirmed");
            focusView = confirmPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            signupTask= new SignupTask(email, password,confirm,name);
            signupTask.execute((Void) null);
        }
    }
    protected boolean isPasswordConfirmed(String password,String confirmPassword) {
        //TODO: Replace this with your own logic
        return password.equals(confirmPassword);
    }
    public class SignupTask extends AsyncTask<Void, Void, Boolean> {

        protected final String mEmail;
        protected final String mPassword;
        protected final String mName;

        SignupTask(String email, String name,String password,String confirm) {
            mEmail = email;
            mPassword = password;
            mName=name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String response;
            try {
                HashMap<String,String> postDataParams=new HashMap<>();
                postDataParams.put("user_id",mEmail);
                postDataParams.put("user_name",mName);
                postDataParams.put("pass",mPassword);
                URL url = new URL("http://192.168.1.57:8080/api/android/signup");
                URLConnection urlConn = url.openConnection();

                if (!(urlConn instanceof HttpURLConnection)) {
                    throw new IOException("URL is not an Http URL");
                }
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setReadTimeout(15000);
                httpConn.setConnectTimeout(15000);
                httpConn.setRequestMethod("POST");
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);
                httpConn.setRequestMethod("POST");

                OutputStream os = httpConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpConn.getInputStream();
                    response=readIt(in,Integer.MAX_VALUE);
                    Log.i("webresponse",response);
                }
            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            }

            catch (IOException e) {
                e.printStackTrace();
            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                intent.putExtra("email",mEmail);
                startActivity(intent);

                finish();

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


}
