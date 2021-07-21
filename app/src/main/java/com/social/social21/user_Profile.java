package com.social.social21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class user_Profile extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        auth=FirebaseAuth.getInstance();
        FirebaseUser user= auth.getCurrentUser();


        TextView name = findViewById(R.id.username);
        ImageView profile_img = findViewById(R.id.Profile);
        TextView email = findViewById(R.id.Email);
        TextView id = findViewById(R.id.userId);
        Button signOutBtn = findViewById(R.id.Google_signout);

        if (user != null) {
            name.append(user.getDisplayName());
            email.append(user.getEmail());
            id.append(user.getUid());

            if (user.getPhotoUrl() != null) {
                Picasso.get().load(user.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profile_img);
            }

        }

        signOutBtn.setOnClickListener(view -> {
            auth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(user_Profile.this,MainActivity.class));
            finish();
        });
    }
}