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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.henho.banmuonhenho.Adapter.Nguoiduoccauhon_adapter;
import com.henho.banmuonhenho.Model.Model_nguoiduocthich;
import com.henho.banmuonhenho.Model.OnItemClickListener_nguoiduoccauhon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Nguoi_duoc_cau_hon extends AppCompatActivity implements OnItemClickListener_nguoiduoccauhon {

    RecyclerView Recy_danhsachnguoicauhon_nguoiduoccauhon;
    Nguoiduoccauhon_adapter Adapter;
    ArrayList<Model_nguoiduocthich> danhsachnguoiduoccauhon;
    Button Btn_quaylai_nguoiduoccauhon;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoi_duoc_cau_hon);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        Btn_quaylai_nguoiduoccauhon = findViewById(R.id.Btn_quaylai_nguoiduoccauhon);
        Recy_danhsachnguoicauhon_nguoiduoccauhon = findViewById(R.id.Recy_danhsachnguoicauhon_nguoiduoccauhon);
        danhsachnguoiduoccauhon = new ArrayList<>();
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
        Recy_danhsachnguoicauhon_nguoiduoccauhon.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Recy_danhsachnguoicauhon_nguoiduoccauhon.setLayoutManager(linearLayoutManager);
        Adapter = new Nguoiduoccauhon_adapter(this, danhsachnguoiduoccauhon, this);
        Recy_danhsachnguoicauhon_nguoiduoccauhon.setAdapter(Adapter);
// Hiển thị danh sách xong

// Đọc danh sách người được cầu hôn
        Query myTopPostsQuery = mData.child("USERS").child(USER.getUid()).child("nguoiduoccauhon").orderByChild("sovangsinhle");

        myTopPostsQuery.limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsachnguoiduoccauhon.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_nguoiduocthich trunggian = postSnapshot.getValue(Model_nguoiduocthich.class);
                    if(trunggian.getId_nguoigui() != null && trunggian.getLoicauhon() != null && trunggian.getNgaygui() != null && trunggian.getSovangsinhle() != null) {
                        danhsachnguoiduoccauhon.add(new Model_nguoiduocthich(trunggian.getId_nguoigui(), trunggian.getLoicauhon(), trunggian.getSovangsinhle(), trunggian.getNgaygui()));

                        // Sắp xếp danh sách
                        Collections.sort(danhsachnguoiduoccauhon, new Comparator<Model_nguoiduocthich>() {
                            @Override
                            public int compare(Model_nguoiduocthich model_nguoithich, Model_nguoiduocthich t1) {
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
// Đọc danh sách người được cầu hôn xong

        Btn_quaylai_nguoiduoccauhon.setOnClickListener(new View.OnClickListener() {
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
        ViewGroup rootView = findViewById(R.id.Banner_nguoiduoccauhon);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("sangtrang3", true);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Model_nguoiduocthich itemclick) {
        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", itemclick.getId_nguoigui());
        startActivity(intent);
    }
}