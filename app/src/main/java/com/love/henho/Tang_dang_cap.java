package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxRewardedAd;
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

import java.util.concurrent.TimeUnit;

public class Tang_dang_cap extends AppCompatActivity implements MaxRewardedAdListener {

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    Integer sovang;
    TextView Txt_sothoivang_dangcap, Txt_dangcap_dangcap, Txt_loimoimuavaqng_dangcap;
    Button Btn_quangcao_dangcap, Btn_quaylai_dangcap;
    private MaxAdView adView;
    private MaxRewardedAd rewardedAd;
    private int retryAttempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tang_dang_cap);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        Txt_sothoivang_dangcap = findViewById(R.id.Txt_sothoivang_dangcap);
        Txt_dangcap_dangcap = findViewById(R.id.Txt_dangcap_dangcap);
        Txt_loimoimuavaqng_dangcap = findViewById(R.id.Txt_loimoimuavaqng_dangcap);
        Btn_quangcao_dangcap = findViewById(R.id.Btn_quangcao_dangcap);
        Btn_quaylai_dangcap = findViewById(R.id.Btn_quaylai_dangcap);
// Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );
        createRewardedAd();
// Lấy thông tin đẳng cấp
        mData.child("USERS").child(USER.getUid()).child("dangcap").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Txt_dangcap_dangcap.setText("Nên thuộc tầng lớp " + snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// Lấy thông tin đẳng cấp xong

// Đếm số vàng đang có
        mData.child("USERS").child(USER.getUid()).child("sovangdangco").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sovang = Integer.parseInt(snapshot.getValue().toString());
                Txt_sothoivang_dangcap.setText("Hiện bạn đang có " + sovang + " thỏi vàng");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// Đếm số vàng đang có xong

// Lấy thông tin mời mua vàng
        mData.child("QUAN_LY").child("muadangcap").child("loimoi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    Txt_loimoimuavaqng_dangcap.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

// Lấy thông tin mời mua vàng xong

// Tạo quảng cáo tặng thưởng

        Btn_quangcao_dangcap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( rewardedAd.isReady() )
                {
                    rewardedAd.showAd();
                }else {
                    Toast.makeText(Tang_dang_cap.this, "Hiện nay chưa có quảng cáo. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            }
        });


// Tạo quảng cáo tặng thưởng xong

        Btn_quaylai_dangcap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("sangtrang3", true);
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
        ViewGroup rootView = findViewById(R.id.Banner_dangcap);
        rootView.addView(adView);
        adView.loadAd();
    }


    void createRewardedAd()
    {
        rewardedAd = MaxRewardedAd.getInstance( getResources().getString(R.string.id_video_thuong), this );
        rewardedAd.setListener( this );
        rewardedAd.loadAd();
    }

    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd)
    {
         retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error)
    {
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                rewardedAd.loadAd();
            }
        }, delayMillis );
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error)
    {
        rewardedAd.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {}

    @Override
    public void onAdClicked(final MaxAd maxAd) {}

    @Override
    public void onAdHidden(final MaxAd maxAd)
    {
        rewardedAd.loadAd();
    }

    @Override
    public void onRewardedVideoStarted(final MaxAd maxAd) {} // deprecated

    @Override
    public void onRewardedVideoCompleted(final MaxAd maxAd) {
        mData.child("USERS").child(USER.getUid()).child("sovangdangco").setValue(sovang + 1);
    } // deprecated

    @Override
    public void onUserRewarded(final MaxAd maxAd, final MaxReward maxReward) {}

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("sangtrang3", true);
        startActivity(intent);
    }
}