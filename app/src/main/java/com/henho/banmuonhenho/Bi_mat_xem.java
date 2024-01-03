package com.henho.banmuonhenho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.henho.banmuonhenho.Model.Model_anhbimat;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

public class Bi_mat_xem extends AppCompatActivity {

    Button Btn_mokhoa_bimatxem;
    EditText Edt_matkhau_bimatxem;
    String sohoso;
    DatabaseReference mData;
    TextView Txt_tieude_bimatxem;
    String matkhau, matkhaunhap;
    ConstraintLayout Constran_dapnhap_bimatxem;
    ImageView Img_xemanh_bimatxem, Img_quaylai_bimatxem,
            Img_anh1_bimatxem, Img_anh2_bimatxem, Img_anh3_bimatxem
            ,Img_anh4_bimatxem,Img_anh5_bimatxem,Img_anh6_bimatxem;
    FirebaseFirestore db;
    LinearLayout Linear_xemanh_bimatxem;
    private MaxAdView adView;
    private MaxInterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bi_mat_xem);

        Anhxa();

        // Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );
        interstitialAd = new MaxInterstitialAd( getResources().getString(R.string.id_toanmanhinh), this );
        interstitialAd.loadAd();

        Intent intent = getIntent();
        sohoso = intent.getStringExtra("guiidnguoichat");
        mData.child("USERS").child(sohoso).child("bimat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    matkhau = snapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        Btn_mokhoa_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matkhaunhap = Edt_matkhau_bimatxem.getText().toString();
                if(matkhau.length() == 0){
                    Toast.makeText(Bi_mat_xem.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                }else {
                    if(matkhau.length() == 1){
                        Edt_matkhau_bimatxem.setText("0" + matkhaunhap);
                    }
                    if (matkhau.equals(matkhaunhap)){
                        Constran_dapnhap_bimatxem.setVisibility(View.GONE);
                        if ( interstitialAd.isReady() )
                        {
                            interstitialAd.showAd();
                        }else {
                            interstitialAd.loadAd();
                        }
                    }else {
                        Edt_matkhau_bimatxem.setText("");
                        Toast.makeText(Bi_mat_xem.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        db.collection("USER").document(sohoso).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Model_anhbimat layve = documentSnapshot.toObject(Model_anhbimat.class);
                String ten = documentSnapshot.getString("ten");
                if(ten != null){
                    Txt_tieude_bimatxem.setText("Ảnh bí mật của " + ten);
                }
                if(layve.getAnh1() != null){
                    Picasso.get().load(layve.getAnh1()).into(Img_anh1_bimatxem);
                }
                if(layve.getAnh2() != null){
                    Picasso.get().load(layve.getAnh2()).into(Img_anh2_bimatxem);
                }
                if(layve.getAnh3() != null){
                    Picasso.get().load(layve.getAnh3()).into(Img_anh3_bimatxem);
                }
                if(layve.getAnh4() != null){
                    Picasso.get().load(layve.getAnh4()).into(Img_anh4_bimatxem);
                }
                if(layve.getAnh5() != null){
                    Picasso.get().load(layve.getAnh5()).into(Img_anh5_bimatxem);
                }
                if(layve.getAnh6() != null){
                    Picasso.get().load(layve.getAnh6()).into(Img_anh6_bimatxem);
                }
            }
        });

        Img_anh1_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatxem.setImageDrawable(Img_anh1_bimatxem.getDrawable());
                Linear_xemanh_bimatxem.setVisibility(View.VISIBLE);
            }
        });
        Img_anh2_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatxem.setImageDrawable(Img_anh2_bimatxem.getDrawable());
                Linear_xemanh_bimatxem.setVisibility(View.VISIBLE);
            }
        });
        Img_anh3_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatxem.setImageDrawable(Img_anh3_bimatxem.getDrawable());
                Linear_xemanh_bimatxem.setVisibility(View.VISIBLE);
            }
        });
        Img_anh4_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatxem.setImageDrawable(Img_anh4_bimatxem.getDrawable());
                Linear_xemanh_bimatxem.setVisibility(View.VISIBLE);
            }
        });
        Img_anh5_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatxem.setImageDrawable(Img_anh5_bimatxem.getDrawable());
                Linear_xemanh_bimatxem.setVisibility(View.VISIBLE);
            }
        });
        Img_anh6_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatxem.setImageDrawable(Img_anh6_bimatxem.getDrawable());
                Linear_xemanh_bimatxem.setVisibility(View.VISIBLE);
            }
        });

        Linear_xemanh_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linear_xemanh_bimatxem.setVisibility(View.GONE);
            }
        });
        Img_xemanh_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linear_xemanh_bimatxem.setVisibility(View.GONE);
            }
        });

        Img_quaylai_bimatxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Linear_xemanh_bimatxem.getVisibility() == View.VISIBLE) {
                    Linear_xemanh_bimatxem.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
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
        ViewGroup rootView = findViewById(R.id.Banner_bimatxem);
        rootView.addView(adView);
        adView.loadAd();
    }

    private void Anhxa() {
        Img_quaylai_bimatxem = findViewById(R.id.Img_quaylai_bimatxem);
        Img_anh1_bimatxem = findViewById(R.id.Img_anh1_bimatxem);
        Img_anh2_bimatxem = findViewById(R.id.Img_anh2_bimatxem);
        Img_anh3_bimatxem = findViewById(R.id.Img_anh3_bimatxem);
        Img_anh4_bimatxem = findViewById(R.id.Img_anh4_bimatxem);
        Img_anh5_bimatxem = findViewById(R.id.Img_anh5_bimatxem);
        Img_anh6_bimatxem = findViewById(R.id.Img_anh6_bimatxem);
        Img_xemanh_bimatxem = findViewById(R.id.Img_xemanh_bimatxem);
        Txt_tieude_bimatxem = findViewById(R.id.Txt_tieude_bimatxem);
        Linear_xemanh_bimatxem = findViewById(R.id.Linear_xemanh_bimatxem);
        mData = FirebaseDatabase.getInstance().getReference();
        Constran_dapnhap_bimatxem = findViewById(R.id.Constran_dapnhap_bimatxem);
        Edt_matkhau_bimatxem = findViewById(R.id.Edt_matkhau_bimatxem);
        Btn_mokhoa_bimatxem = findViewById(R.id.Btn_mokhoa_bimatxem);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onBackPressed() {
        if (Linear_xemanh_bimatxem.getVisibility() == View.VISIBLE) {
            Linear_xemanh_bimatxem.setVisibility(View.GONE);
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("sangtrang1", true);
            startActivity(intent);
        }
    }
}