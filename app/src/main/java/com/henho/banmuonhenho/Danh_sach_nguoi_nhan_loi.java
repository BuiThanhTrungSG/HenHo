package com.henho.banmuonhenho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

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
import com.henho.banmuonhenho.Adapter.Danhsachnhanloi_adapter;
import com.henho.banmuonhenho.Model.Model_nguoinhanloi;
import com.henho.banmuonhenho.Model.OnItemClickListener_nguoinhanloi;
import java.util.ArrayList;

public class Danh_sach_nguoi_nhan_loi extends AppCompatActivity implements OnItemClickListener_nguoinhanloi {

    RecyclerView Recyc_danhsach_dsnl;
    ArrayList<Model_nguoinhanloi> danhsachnguoinhanloi;
    Danhsachnhanloi_adapter Adapter;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    Button Btn_quaylai_dsnl;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nguoi_nhan_loi);

        Recyc_danhsach_dsnl = findViewById(R.id.Recyc_danhsach_dsnl);
        danhsachnguoinhanloi = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        Btn_quaylai_dsnl = findViewById(R.id.Btn_quaylai_dsnl);

// Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );

// Hiển thị danh sách
        Recyc_danhsach_dsnl.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Recyc_danhsach_dsnl.setLayoutManager(linearLayoutManager);
        Adapter = new Danhsachnhanloi_adapter(this, danhsachnguoinhanloi, this);
        Recyc_danhsach_dsnl.setAdapter(Adapter);
// Hiển thị danh sách xong

        mData.child("NHAN_LOI").child(USER.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsachnguoinhanloi.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Model_nguoinhanloi trunggian = snap.getValue(Model_nguoinhanloi.class);
                    if(trunggian.getId() != null && trunggian.getLoinhan() != null) {
                        danhsachnguoinhanloi.add(new Model_nguoinhanloi(trunggian.getId(), trunggian.getLoinhan()));
                        Adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Btn_quaylai_dsnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Hen_gap.class);
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
        ViewGroup rootView = findViewById(R.id.Banner_danhsach_dsnl);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onItemClick(String itemclick) {
        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", itemclick);
        intent.putExtra("thongbaoquaylai", "trangdanhsachnhanloi");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Hen_gap.class);
        startActivity(intent);
    }
}