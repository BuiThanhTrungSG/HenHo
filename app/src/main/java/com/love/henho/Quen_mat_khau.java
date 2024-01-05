package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Quen_mat_khau extends AppCompatActivity {

    Button Btn_quenmatkhau;
    EditText Edt_emailquenmatkhau;
    DatabaseReference mData;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);

        Btn_quenmatkhau = findViewById(R.id.nutquenmatkhau);
        Edt_emailquenmatkhau = findViewById(R.id.email_quenmatkhau);
        mData = FirebaseDatabase.getInstance().getReference();

        // Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );

        Btn_quenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Edt_emailquenmatkhau.length() == 0){
                    Toast.makeText(Quen_mat_khau.this, "Bạn chưa nhập emai", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseAuth.getInstance().setLanguageCode("vi");
                    FirebaseAuth.getInstance().sendPasswordResetEmail(Edt_emailquenmatkhau.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Quen_mat_khau.this, "Đã gửi mật khẩu đến email của bạn", Toast.LENGTH_SHORT).show();;
                                        Intent intent = new Intent(getApplicationContext(),Dang_nhap.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(Quen_mat_khau.this, "Lỗi! Không gửi được", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

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
        ViewGroup rootView = findViewById(R.id.Banner_quenmatkhau);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Dang_nhap.class);
        startActivity(intent);
    }
}