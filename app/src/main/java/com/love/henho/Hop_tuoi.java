package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.love.henho.Adapter.Hoptuoi_adapter;
import com.love.henho.Model.Model_thanhvienmoidangnhap;
import com.love.henho.Model.OnItemClickListener_trangchu;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Hop_tuoi extends AppCompatActivity implements OnItemClickListener_trangchu {

    FirebaseAuth mAuth;
    FirebaseUser User;
    DatabaseReference mData;
    ImageView Img_tuoi_hoptuoi, Img_tuoihop1_hoptuoi, Img_tuoihop2_hoptuoi;
    TextView Txt_nam_hoptuoi, Txt_camtinh_hoptuoi, Txt_tuoi_hoptuoi, Txt_tuoihop1_hoptuoi, Txt_tuoihop2_hoptuoi;
    Button Btn_quaylai_hoptuoi;
    RecyclerView Recyc_danhsach_hoptuoi;
    ArrayList<Model_thanhvienmoidangnhap> Danhsach;
    int congiap;
    SharedPreferences mLay;
    SharedPreferences sp;
    SharedPreferences.Editor spLuu;
    Hoptuoi_adapter danhsach;
    Gson gson;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hop_tuoi);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        Img_tuoi_hoptuoi = findViewById(R.id.Img_tuoi_hoptuoi);
        Img_tuoihop1_hoptuoi = findViewById(R.id.Img_tuoihop1_hoptuoi);
        Img_tuoihop2_hoptuoi = findViewById(R.id.Img_tuoihop2_hoptuoi);
        Txt_nam_hoptuoi = findViewById(R.id.Txt_nam_hoptuoi);
        Txt_camtinh_hoptuoi = findViewById(R.id.Txt_camtinh_hoptuoi);
        Txt_tuoi_hoptuoi = findViewById(R.id.Txt_tuoi_hoptuoi);
        Txt_tuoihop1_hoptuoi = findViewById(R.id.Txt_tuoihop1_hoptuoi);
        Txt_tuoihop2_hoptuoi = findViewById(R.id.Txt_tuoihop2_hoptuoi);
        Recyc_danhsach_hoptuoi = findViewById(R.id.Recyc_danhsach_hoptuoi);
        Btn_quaylai_hoptuoi = findViewById(R.id.Btn_quaylai_hoptuoi);

        mLay = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spLuu = sp.edit();
        gson = new Gson();
// Quảng cáo
//        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
//        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
//            @Override
//            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
//            {
//                createBannerAd();
//            }
//        } );

// Hiển thị danh sách
        Danhsach = new ArrayList<>();
        Recyc_danhsach_hoptuoi.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Recyc_danhsach_hoptuoi.setLayoutManager(linearLayoutManager);
        danhsach = new Hoptuoi_adapter(Danhsach, getApplicationContext(), this);
        Recyc_danhsach_hoptuoi.setAdapter(danhsach);
// Hiển thị danh sách xong

// Đọc danh sách mặc định khi mở màn hình
        mData.child("USERS").child(User.getUid()).child("namsinh").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue() != null) {

                    int namsinh = Integer.parseInt(snapshot.getValue().toString());
                    Txt_nam_hoptuoi.setText(String.valueOf(namsinh));
                    congiap = Integer.parseInt(snapshot.getValue().toString()) % 12;

                    if(congiap == 1){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_dau);Txt_camtinh_hoptuoi.setText("Cầm tinh con Gà");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_suu);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_ran);
                        Txt_tuoi_hoptuoi.setText("Tuổi Dậu");Txt_tuoihop1_hoptuoi.setText("Tuổi Sửu");Txt_tuoihop2_hoptuoi.setText("Tuổi Tỵ");}

                    if(congiap == 2){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_tuat); Txt_camtinh_hoptuoi.setText("Cầm tinh con Cún");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_dan);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_ngo);
                        Txt_tuoi_hoptuoi.setText("Tuổi Tuất");Txt_tuoihop1_hoptuoi.setText("Tuổi Dần");Txt_tuoihop2_hoptuoi.setText("Tuổi Ngọ");}

                    if(congiap == 3){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_hoi);Txt_camtinh_hoptuoi.setText("Cầm tinh con Heo");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_mao);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_mui);
                        Txt_tuoi_hoptuoi.setText("Tuổi Hợi");Txt_tuoihop1_hoptuoi.setText("Tuổi Mão");Txt_tuoihop2_hoptuoi.setText("Tuổi Mùi");}

                    if(congiap == 4){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_ty);Txt_camtinh_hoptuoi.setText("Cầm tinh con Chuột");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_than);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_thin);
                        Txt_tuoi_hoptuoi.setText("Tuổi Tý");Txt_tuoihop1_hoptuoi.setText("Tuổi Thân");Txt_tuoihop2_hoptuoi.setText("Tuổi Thìn");}

                    if(congiap == 5){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_suu);Txt_camtinh_hoptuoi.setText("Cầm tinh con Trâu");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_ran);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_dau);
                        Txt_tuoi_hoptuoi.setText("Tuổi Sửu");Txt_tuoihop1_hoptuoi.setText("Tuổi Tỵ");Txt_tuoihop2_hoptuoi.setText("Tuổi Dậu");}

                    if(congiap == 6){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_dan);Txt_camtinh_hoptuoi.setText("Cầm tinh con Hổ");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_ngo);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_tuat);
                        Txt_tuoi_hoptuoi.setText("Tuổi Dần");Txt_tuoihop1_hoptuoi.setText("Tuổi Ngọ");Txt_tuoihop2_hoptuoi.setText("Tuổi Tuất");}

                    if(congiap == 7){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_mao);Txt_camtinh_hoptuoi.setText("Cầm tinh con Mèo");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_hoi);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_mui);
                        Txt_tuoi_hoptuoi.setText("Tuổi Mão");Txt_tuoihop1_hoptuoi.setText("Tuổi Hợi");Txt_tuoihop2_hoptuoi.setText("Tuổi Mùi");}

                    if(congiap == 8){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_thin);Txt_camtinh_hoptuoi.setText("Cầm tinh con Rồng");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_than);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_ty);
                        Txt_tuoi_hoptuoi.setText("Tuổi Thìn");Txt_tuoihop1_hoptuoi.setText("Tuổi Thân");Txt_tuoihop2_hoptuoi.setText("Tuổi Tý");}

                    if(congiap == 9){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_ran);Txt_camtinh_hoptuoi.setText("Cầm tinh con Rắn");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_dau);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_suu);
                        Txt_tuoi_hoptuoi.setText("Tuổi Tỵ");Txt_tuoihop1_hoptuoi.setText("Tuổi Dậu");Txt_tuoihop2_hoptuoi.setText("Tuổi Sửu");}

                    if(congiap == 10){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_ngo);Txt_camtinh_hoptuoi.setText("Cầm tinh con Ngựa");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_dan);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_tuat);
                        Txt_tuoi_hoptuoi.setText("Tuổi Ngọ");Txt_tuoihop1_hoptuoi.setText("Tuổi Dần");Txt_tuoihop2_hoptuoi.setText("Tuổi Tuất");}

                    if(congiap == 11){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_mui);Txt_camtinh_hoptuoi.setText("Cầm tinh con Dê");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_hoi);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_mao);
                        Txt_tuoi_hoptuoi.setText("Tuổi Mùi");Txt_tuoihop1_hoptuoi.setText("Tuổi Hợi");Txt_tuoihop2_hoptuoi.setText("Tuổi Mão");}

                    if(congiap == 0){Img_tuoi_hoptuoi.setImageResource(R.drawable.t_than);Txt_camtinh_hoptuoi.setText("Cầm tinh con Khỉ");
                        Img_tuoihop1_hoptuoi.setImageResource(R.drawable.t_ty);Img_tuoihop2_hoptuoi.setImageResource(R.drawable.t_thin);
                        Txt_tuoi_hoptuoi.setText("Tuổi Thân");Txt_tuoihop1_hoptuoi.setText("Tuổi Tý");Txt_tuoihop2_hoptuoi.setText("Tuổi Thìn");}

                    String json = mLay.getString("Set", "");
                    Type type = new TypeToken<ArrayList<Model_thanhvienmoidangnhap>>() {
                    }.getType();
                    ArrayList<Model_thanhvienmoidangnhap> arrPackageData = gson.fromJson(json, type);
                    ArrayList<Model_thanhvienmoidangnhap> trunggian = new ArrayList<>();
                    trunggian.addAll(arrPackageData);

                    for(int i = 0; i < trunggian.size(); i++){
                        int namsinhnguoi = trunggian.get(i).getNamsinh();
                        if(namsinhnguoi%12 == (congiap + 4)%12 || namsinhnguoi%12 == (congiap + 8)%12){
                            Danhsach.add(trunggian.get(i));
                        }
                        danhsach.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Btn_quaylai_hoptuoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        ViewGroup rootView = findViewById(R.id.Banner_hoptuoi);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onItemClick(String itemclick) {
        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", itemclick);
        intent.putExtra("thongbaoquaylai", "xemtuoi");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}