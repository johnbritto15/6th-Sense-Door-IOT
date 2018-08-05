package com.quasar.nemesis.a6thsense.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.quasar.nemesis.a6thsense.MyApplication;
import com.quasar.nemesis.a6thsense.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private int RC_SIGN_IN=0;
    private TextView register;
    private Button login;
    private static int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        count++;
        System.out.println(count);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        ((MyApplication) this.getApplication()).setGoogleSignInOptions(gso);

         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        ((MyApplication) this.getApplication()).setclient(mGoogleSignInClient);
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        register=(TextView)findViewById(R.id.not_registered);
        register.setOnClickListener(this);

        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null)
        {
            //intent
            Intent intent=new Intent(LoginActivity.this,DashboardActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==signInButton)
        {
            SignIn();
        }
        else if(v==register)
        {
            Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
        else if(v==login)
        {
            Intent intent=new Intent(LoginActivity.this,DashboardActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private void SignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            // Signed in successfully, show authenticated UI.

            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            //send account to next activity
            startActivity(intent);
            LoginActivity.this.finish();
           // updateUI(account);
        } catch (ApiException e) {

            Log.w("", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
}
