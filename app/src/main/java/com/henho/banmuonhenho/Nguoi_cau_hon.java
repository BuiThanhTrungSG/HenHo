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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.henho.banmuonhenho.Adapter.Nguoicauhon_adapter;
import com.henho.banmuonhenho.Model.Model_nguoithich;
import com.henho.banmuonhenho.Model.OnItemClickListener_nguoicauhon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Nguoi_cau_hon extends AppCompatActivity implements OnItemClickListener_nguoicauhon {

    RecyclerView Recy_danhsachnguoicauhon_nguoicauhon;
    ArrayList<Model_nguoithich> danhsachnguoicauhon;
    Nguoicauhon_adapter Adapter;
    String sohoso;
    TextView Txt_nguoiduoccauhon_nguoicauhon;
    Button Btn_quaylai_nguoicauhon;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoi_cau_hon);

        Recy_danhsachnguoicauhon_nguoicauhon = findViewById(R.id.Recy_danhsachnguoicauhon_nguoicauhon);
        danhsachnguoicauhon = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        Txt_nguoiduoccauhon_nguoicauhon = findViewById(R.id.Txt_nguoiduoccauhon_nguoicauhon);
        Btn_quaylai_nguoicauhon = findViewById(R.id.Btn_quaylai_nguoicauhon);
// Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );

// lấy số hồ sơ (ID chủ hồ sơ) từ trang trước truyền qua
        Intent intent = getIntent();
        sohoso = intent.getStringExtra("guiidnguoichat");
// lấy số hồ sơ (ID chủ hồ sơ) từ trang trước truyền qua xong



// Hiển thị danh sách
        Recy_danhsachnguoicauhon_nguoicauhon.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Recy_danhsachnguoicauhon_nguoicauhon.setLayoutManager(linearLayoutManager);
        Adapter = new Nguoicauhon_adapter(this, danhsachnguoicauhon, this);
        Recy_danhsachnguoicauhon_nguoicauhon.setAdapter(Adapter);
// Hiển thị danh sách xong

// Đọc danh sách người cầu hôn

        Query myTopPostsQuery = mData.child("USERS").child(sohoso).child("nguoicauhon").orderByChild("sovangsinhle").limitToLast(10);

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsachnguoicauhon.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_nguoithich trunggian = postSnapshot.getValue(Model_nguoithich.class);
                    if(trunggian.getId_nguoigui() != null && trunggian.getLoicauhon() != null && trunggian.getNgaygui() != null && trunggian.getSovangsinhle() != null) {
                        if (USER == null) {
                            danhsachnguoicauhon.add(new Model_nguoithich(trunggian.getId_nguoigui(), "Nội dung riêng tư không hiển thị", trunggian.getSovangsinhle(), trunggian.getNgaygui()));
                        } else {
                            if (USER.getUid().equals(sohoso)) {
                                danhsachnguoicauhon.add(new Model_nguoithich(trunggian.getId_nguoigui(), trunggian.getLoicauhon(), trunggian.getSovangsinhle(), trunggian.getNgaygui()));
                            } else {
                                danhsachnguoicauhon.add(new Model_nguoithich(trunggian.getId_nguoigui(), "Nội dung riêng tư không hiển thị", trunggian.getSovangsinhle(), trunggian.getNgaygui()));
                            }
                        }

                        Collections.sort(danhsachnguoicauhon, new Comparator<Model_nguoithich>() {
                            @Override
                            public int compare(Model_nguoithich model_nguoithich, Model_nguoithich t1) {
                                if (model_nguoithich == null || model_nguoithich.getSovangsinhle() == null) {
                                    return 0;
                                }
                                if (t1 == null || t1.getSovangsinhle() == null) {
                                    return 0;
                                }
                                return t1.getSovangsinhle().compareTo(model_nguoithich.getSovangsinhle());
                            }
                        });
                        // Sắp xếp danh sách xong

                        Adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

// Đọc danh sách người cầu hôn xong

// Đọc tên người được cầu hôn
        mData.child("USERS").child(sohoso).child("ten").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    Txt_nguoiduoccauhon_nguoicauhon.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// Đọc tên người được cầu hôn xong

        Btn_quaylai_nguoicauhon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(USER != null){
                    if(sohoso.equals(USER.getUid())){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("sangtrang3", true);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
                        intent.putExtra("guiidnguoichat", sohoso);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(), Ho_so.class);
                    intent.putExtra("guiidnguoichat", sohoso);
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
        ViewGroup rootView = findViewById(R.id.Banner_nguoicauhon);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onItemClick(Model_nguoithich itemclick) {
        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", itemclick.getId_nguoigui());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(USER != null){
            if(sohoso.equals(USER.getUid())){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("sangtrang3", true);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getApplicationContext(), Ho_so.class);
                intent.putExtra("guiidnguoichat", sohoso);
                startActivity(intent);
            }
        }else {
            Intent intent = new Intent(getApplicationContext(), Ho_so.class);
            intent.putExtra("guiidnguoichat", sohoso);
            startActivity(intent);
        }

    }
}