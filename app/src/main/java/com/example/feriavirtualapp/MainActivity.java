package com.example.feriavirtualapp;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;

public class MainActivity extends Activity {
    private Lock lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //setContentView(R.layout.activity_main);


        Auth0 auth0 = new Auth0(getString(R.string.com_auth0_client_id), getString(R.string.com_auth0_domain));
        // Auth0 auth0 = new Auth0("yC3U4Nc3yG4kjLP1IT3EFdtrSHrrck6r","dev-5f39w36l.auth0.com");
        auth0.setOIDCConformant(true);

        WebAuthProvider.login(auth0)
                .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
                .start(this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull Dialog dialog) {

                    }

                    @Override
                    public void onFailure(AuthenticationException exception) {

                    }

                    @Override
                    public void onSuccess(@NonNull Credentials credentials) {

                    }
                });

      /*  lock = Lock.newBuilder(auth0,callback)
                .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
                .build(this);
                */


    }

    @Override
    protected  void  onDestroy () {
        super . onDestroy ();
        // Tu propio código de
        startActivity(lock.newIntent(this));
        lock.onDestroy(this);
        lock = null;
    }

    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            System.out.println("LLEGO AUTENTICADO SUCCES");
        }

        @Override
        public void onCanceled() {
            System.out.println("AUTH0 CANCELADO");
        }

        @Override
        public void onError(LockException error) {
            System.out.println("AUTH0 ERROR");
        }
    };
}
