package com.henho.banmuonhenho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Cau_hon extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;

    String sohoso, linkanh, ten, dangcap;
    ImageView Img_anhnguoiduoccauhon_cauhon;
    TextView Txt_tennguoiduoccauhon_cauhon, Txt_dangcapnguoiduoccauhon_cauhon, Txt_sovangdangco_cauhon, Txt_cauhon_bentranghoso_cauhon;
    Button Btn_guiloicauhon_cauhon, Btn_quaylai_cauhon;
    EditText Edt_loicauhon_cauhon, Edt_sothoivang_cauhon;

    String loicauhon;
    Integer sovang, sovang_nguoicauhondangco, sovang_nguoiduoccauhondangco;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hon);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        Img_anhnguoiduoccauhon_cauhon = findViewById(R.id.Img_anhnguoiduoccauhon_cauhon);
        Txt_tennguoiduoccauhon_cauhon = findViewById(R.id.Txt_tennguoiduoccauhon_cauhon);
        Txt_dangcapnguoiduoccauhon_cauhon = findViewById(R.id.Txt_dangcapnguoiduoccauhon_cauhon);
        Txt_sovangdangco_cauhon = findViewById(R.id.Txt_sovangdangco_cauhon);
        Txt_cauhon_bentranghoso_cauhon = findViewById(R.id.tieude_cauhon);
        Btn_quaylai_cauhon = findViewById(R.id.Btn_quaylai_cauhon);
        Btn_guiloicauhon_cauhon = findViewById(R.id.Btn_guiloicauhon_cauhon);
        Edt_loicauhon_cauhon = findViewById(R.id.Edt_loicauhon_cauhon);
        Edt_sothoivang_cauhon = findViewById(R.id.Edt_sothoivang_cauhon);

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

// Lấy thông tin người được cầu hôn
        mData.child("USERS").child(sohoso).child("anhdaidien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    linkanh = snapshot.getValue().toString();
                    Picasso.get().load(linkanh).into(Img_anhnguoiduoccauhon_cauhon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("USERS").child(sohoso).child("ten").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    ten = snapshot.getValue().toString();
                    Txt_tennguoiduoccauhon_cauhon.setText(ten);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("USERS").child(sohoso).child("dangcap").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    dangcap = snapshot.getValue().toString();
                    Txt_dangcapnguoiduoccauhon_cauhon.setText(dangcap);
                    // Đổi màu đẳng cấp
                    if (dangcap.equals("Khá giả")) {
                        Txt_dangcapnguoiduoccauhon_cauhon.setTextColor(Color.parseColor("#04B1FF"));
                    } else if (dangcap.equals("Giàu có")) {
                        Txt_dangcapnguoiduoccauhon_cauhon.setTextColor(Color.parseColor("#EB08FB"));
                    } else if (dangcap.equals("Đại gia")) {
                        Txt_dangcapnguoiduoccauhon_cauhon.setTextColor(Color.parseColor("#E91E63"));
                    } else {
                        Txt_dangcapnguoiduoccauhon_cauhon.setTextColor(Color.parseColor("#4CAF50"));
                    }
                    // Đổi màu đẳng cấp xong
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

// Lấy thông tin người được cầu hôn xong

// Đếm số vàng người cầu hôn đang có
        mData.child("USERS").child(USER.getUid()).child("sovangdangco").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    sovang_nguoicauhondangco = Integer.parseInt(snapshot.getValue().toString());
                    Txt_sovangdangco_cauhon.setText("(Bạn đang có " + sovang_nguoicauhondangco.toString() + " thỏi vàng)");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
// Đếm số vàng người cầu hôn đang có xong

// Đếm số vàng người được cầu hôn đang có
        mData.child("USERS").child(sohoso).child("sovangdangco").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    sovang_nguoiduoccauhondangco = Integer.parseInt(snapshot.getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
// Đếm số vàng người được cầu hôn đang có xong

        Btn_guiloicauhon_cauhon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Edt_loicauhon_cauhon.getText().toString().length() == 0 || Edt_sothoivang_cauhon.getText().toString().length() == 0){
                    if(Edt_loicauhon_cauhon.getText().toString().length() == 0){
                        Toast.makeText(Cau_hon.this, "Bạn chưa ghi lời nhắn", Toast.LENGTH_SHORT).show();
                    }
                    if(Edt_sothoivang_cauhon.getText().toString().length() == 0){
                        Toast.makeText(Cau_hon.this, "Bạn chưa ghi số thỏi vàng", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loicauhon = Edt_loicauhon_cauhon.getText().toString();
                    sovang = Integer.parseInt(Edt_sothoivang_cauhon.getText().toString());
                    if(sovang > sovang_nguoicauhondangco){
                        Toast.makeText(Cau_hon.this, "Bạn không đủ vàng để tặng", Toast.LENGTH_SHORT).show();
                    }else {
                        // Lấy thời điểm hiện tại
                        DateFormat dinhdang = new SimpleDateFormat("yyyyMMdd");
                        Integer thoidiem = Integer.parseInt(dinhdang.format(Calendar.getInstance().getTime()));
                        // Lấy thời điểm hiện tại xong

                        // Ghi dữ liệu cho người được cầu hôn
                        mData.child("USERS").child(sohoso).child("sovangdangco").setValue(sovang_nguoiduoccauhondangco + sovang);
                        mData.child("USERS").child(sohoso).child("nguoicauhon").child(USER.getUid()).child("id_nguoigui").setValue(USER.getUid());
                        mData.child("USERS").child(sohoso).child("nguoicauhon").child(USER.getUid()).child("loicauhon").setValue(loicauhon);
                        mData.child("USERS").child(sohoso).child("nguoicauhon").child(USER.getUid()).child("sovangsinhle").setValue(sovang);
                        mData.child("USERS").child(sohoso).child("nguoicauhon").child(USER.getUid()).child("ngaygui").setValue(thoidiem);
                        // Ghi dữ liệu cho người được cầu hôn xong

                        // Ghi dữ liệu cho người cầu hôn
                        mData.child("USERS").child(USER.getUid()).child("sovangdangco").setValue(sovang_nguoicauhondangco - sovang);
                        mData.child("USERS").child(USER.getUid()).child("nguoiduoccauhon").child(sohoso).child("id_nguoigui").setValue(sohoso);
                        mData.child("USERS").child(USER.getUid()).child("nguoiduoccauhon").child(sohoso).child("loicauhon").setValue(loicauhon);
                        mData.child("USERS").child(USER.getUid()).child("nguoiduoccauhon").child(sohoso).child("sovangsinhle").setValue(sovang);
                        mData.child("USERS").child(USER.getUid()).child("nguoiduoccauhon").child(sohoso).child("ngaygui").setValue(thoidiem);
                        // Ghi dữ liệu cho người cầu hôn xong

                        // Thông báo lời cầu hôn
                        mData.child("USERS").child(sohoso).child("thongbao_thich").setValue(true);

                        Toast.makeText(Cau_hon.this, "Gửi thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
                        intent.putExtra("guiidnguoichat", sohoso);
                        startActivity(intent);
                    }
                }
            }
        });

        Btn_quaylai_cauhon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Ho_so.class);
                intent.putExtra("guiidnguoichat", sohoso);
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
        ViewGroup rootView = findViewById(R.id.Banner_cauhon);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", sohoso);
        startActivity(intent);
    }
}