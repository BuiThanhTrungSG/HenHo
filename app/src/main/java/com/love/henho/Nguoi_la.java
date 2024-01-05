package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.love.henho.Model.Model_nguoila;
import java.util.ArrayList;

public class Nguoi_la extends AppCompatActivity {

    TextView Txt_dangtim_nguoila;
    FirebaseAuth mAuth;
    FirebaseUser User;
    DatabaseReference mData;
    Button Btn_quaylai_nguoila;
    ArrayList<String> A;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoi_la);

        Txt_dangtim_nguoila = findViewById(R.id.Txt_dangtim_nguoila);
        Btn_quaylai_nguoila = findViewById(R.id.Btn_quaylai_nguoila);
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        A = new ArrayList<>();
// Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );

        mData.child("NGUOI_LA").child(User.getUid()).child("ID").setValue(User.getUid());

        mData.child("NGUOI_LA").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                A.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_nguoila trunggian = postSnapshot.getValue(Model_nguoila.class);
                    A.add(trunggian.getID());
                }
                if(A.size() > 1){
                    for(int i = 0; i < A.size(); i++){
                        if(A.get(i).equals(User.getUid())){
                            for(int y = 0; y < A.size(); y++){
                                if(!A.get(y).equals(User.getUid())){
                                    mData.child("NGUOI_LA").child(User.getUid()).removeValue();
                                    Intent intent = new Intent(getApplicationContext(), Nguoi_la_chat.class);
                                    intent.putExtra("guiidnguoichat", A.get(y));
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                  }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Btn_quaylai_nguoila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("NGUOI_LA").child(User.getUid()).removeValue();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    void createBannerAd() {
        adView = new MaxAdView( getResources().getString(R.string.id_banner), this );
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( this ).getHeight();
        int heightPx = AppLovinSdkUtils.dpToPx( this, heightDp );
        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
        adView.setExtraParameter( "adaptive_banner", "true" );
        ViewGroup rootView = findViewById(R.id.Banner_nguoila);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onBackPressed() {
        mData.child("NGUOI_LA").child(User.getUid()).removeValue();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}