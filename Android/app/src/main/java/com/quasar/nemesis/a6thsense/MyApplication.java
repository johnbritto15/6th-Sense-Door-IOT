package com.quasar.nemesis.a6thsense;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * Created by vipla on 21-01-2018.
 */

public class MyApplication extends Application {
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;


    public void setclient(GoogleSignInClient client)
    {
        this.googleSignInClient=client;
    }

    public GoogleSignInClient getclient()
    {
        return this.googleSignInClient;
    }

    public void setGoogleSignInOptions(GoogleSignInOptions signInOptions)
    {
        this.googleSignInOptions=signInOptions;
    }

    public GoogleSignInOptions getGoogleSignInOptions()
    {
        return this.googleSignInOptions;
    }

}
