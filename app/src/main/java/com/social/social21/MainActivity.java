package com.social.social21;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private static final int GOOGLE_SIGN_IN_REQUEST = 112;
    FirebaseAuth auth;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        InitializeGoogleLogin();
        InitializeFaceBookLogin();
    }

    private void InitializeFaceBookLogin() {
        LoginButton fb_login = findViewById(R.id.Fb_login);
        callbackManager = CallbackManager.Factory.create();

        fb_login.setPermissions("email","public_profile");
        fb_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                handleFacebookLogin(loginResult);

            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "FaceBook Login canceld ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Login  Error !=="+ error, Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void handleFacebookLogin(LoginResult loginResult) {
        AuthCredential credential= FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());

        auth.signInWithCredential(credential).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        SendUserData(user);
                    }
//                    Toast.makeText(MainActivity.this, "login Done Bitch :" , Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("faild","FB Login Faieled ");
                }

            }
        });
    }

    private void InitializeGoogleLogin() {
        SignInButton GoogleLogin =findViewById(R.id.Google_Login);


        GoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DogoogleLogin();

            }
        });
    }

    private void DogoogleLogin() {
//--------------creating google Signing Object
        GoogleSignInOptions gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("419323160551-9gf26g4tbflmvf1gh3ha0fqor1qhg6mt.apps.googleusercontent.com")
                .requestEmail().requestId().requestProfile().build();

//--------------creating Google cline Object
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this,gso);

//--------------lainchung Google Login Dilog intent
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,GOOGLE_SIGN_IN_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//--------Chcek the result From Google Sign in
        if (requestCode==GOOGLE_SIGN_IN_REQUEST){
            Task<GoogleSignInAccount> accountTask=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=accountTask.getResult(ApiException.class);
                if (account != null) {
                    ProcessFirebaselogindata(account.getIdToken());
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
        else {
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void ProcessFirebaselogindata(String token) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(token,null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user=auth.getCurrentUser();
                    if (user != null) {
                        SendUserData(user);
                    }
                }
//                else {
//
//                }

            }
        });
    }

    private void SendUserData(FirebaseUser user) {
        Log.d("Login Successful !","Login Successful !");
        Log.d("User :",user.getUid());
//        Toast.makeText(this, "User :"+user.getUid()+"  Email  "+user.getEmail()+"   Name : "+user.getDisplayName(), Toast.LENGTH_LONG).show();
        openActivity2();

    }
    private void openActivity2() {
        Intent intent=new Intent(MainActivity.this,user_Profile.class);
//        Intent intent=new Intent(MainActivity.this,profile_try.class);
        startActivity(intent);
        finish();
    }

}