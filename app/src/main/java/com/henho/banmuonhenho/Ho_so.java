package com.henho.banmuonhenho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.henho.banmuonhenho.Model.Model_laythongtin_trangcanhan;
import com.henho.banmuonhenho.Model.Model_nguoithich;
import com.squareup.picasso.Picasso;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Ho_so extends AppCompatActivity implements MaxAdListener {

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    String sohoso;
    Integer sonam;
    ImageView Btn_bimat_hoso, Img_sao_hoso, Btn_thich, avata_hoso, Img_icontuoi_hoso, Btn_guitinnhan;
    Button Btn_nguoicauhon_hoso, Btn_quaylai_hoso;
    TextView camtinh_hoso, Txt_sothoivang_hoso, Txt_songuoicauhon_hoso, Txt_thich, ten_hoso, dangcap_hoso, tuoi_hoso, gioitinh_hoso, noio_hoso, hocvan_hoso, tinhtranghonnhan_hoso, mucdichthamgia_hoso, nghenghiep_hoso, ngaythamgia_hoso, gioithieubanthan_hoso;
    LinearLayout phanguitinnhanvathich_hoso;
    Integer sovang_hoso;
    FirebaseFirestore db;
    LinearLayout Linear_anhbimat_hoso;

    private MaxAdView adView;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    int solan_hienthi_quangcao;
    SharedPreferences Pre;
    SharedPreferences.Editor Pre_luu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Anhxa();
// Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
                createInterstitialAd();
            }
        } );

// lấy số hồ sơ (ID chủ hồ sơ) từ trang trước truyền qua
        Intent intent = getIntent();
        sohoso = intent.getStringExtra("guiidnguoichat");
// lấy số hồ sơ (ID chủ hồ sơ) từ trang trước truyền qua xong


// Kiểm tra xem có đang xem hồ sơ của chỉnh mình không
        if(USER != null){
            if(sohoso.equals(USER.getUid())){
                phanguitinnhanvathich_hoso.setVisibility(View.INVISIBLE);
            }else {
                phanguitinnhanvathich_hoso.setVisibility(View.VISIBLE);
            }
        }else {
            phanguitinnhanvathich_hoso.setVisibility(View.VISIBLE);
        }
// Kiểm tra xem có đang xem hồ sơ của chỉnh mình không xong

// Lấy trạng thái của nút "Thích"
        if(USER != null) {
            mData.child("USERS").child(sohoso).child("nguoicauhon").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Model_nguoithich nguoithich = postSnapshot.getValue(Model_nguoithich.class);
                        if (nguoithich.getId_nguoigui().equals(USER.getUid())) {
                            Txt_thich.setText("Đã thích");
                            Btn_thich.setBackgroundResource(R.drawable.icon_unlike);
                            break;
                        } else {
                            Txt_thich.setText("Thích");
                            Btn_thich.setBackgroundResource(R.drawable.icon_like);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

// Lấy trạng thái của nút Thích xong.

// Đếm số người cầu hôn
        mData.child("USERS").child(sohoso).child("nguoicauhon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mData.child("USERS").child(sohoso).child("songuoithich").setValue(snapshot.getChildrenCount());
//                FirebaseFirestore.getInstance().collection("USER").document(sohoso).update("songuoithich", snapshot.getChildrenCount());
                Txt_songuoicauhon_hoso.setText(snapshot.getChildrenCount() + "");
                if(snapshot.getChildrenCount() == 0){
                    Btn_nguoicauhon_hoso.setVisibility(View.INVISIBLE);
                }else {
                    Btn_nguoicauhon_hoso.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

// Đếm số người cầu hôn xong

// Đếm số vàng đang có
        mData.child("USERS").child(sohoso).child("sovangdangco").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sovang_hoso = Integer.parseInt(snapshot.getValue().toString());
                Txt_sothoivang_hoso.setText("Đang có " + sovang_hoso + " thỏi vàng");

                if(sovang_hoso < 100){
                    dangcap_hoso.setText("Bình dân");
                    Img_sao_hoso.setImageResource(R.drawable.icon_sao0);
                    Img_sao_hoso.setVisibility(View.GONE);
                    mData.child("USERS").child(sohoso).child("dangcap").setValue("Bình dân");
                    FirebaseFirestore.getInstance().collection("USER").document(sohoso).update("dangcap", "Bình dân");

                }
                if(sovang_hoso >= 100 && sovang_hoso < 300){
                    dangcap_hoso.setText("Khá giả");
                    Img_sao_hoso.setImageResource(R.drawable.icon_sao1);
                    Img_sao_hoso.setVisibility(View.VISIBLE);
                    mData.child("USERS").child(sohoso).child("dangcap").setValue("Khá giả");
                    FirebaseFirestore.getInstance().collection("USER").document(sohoso).update("dangcap", "Khá giả");
                }
                if(sovang_hoso >= 300 && sovang_hoso < 1000){
                    Img_sao_hoso.setImageResource(R.drawable.icon_sao2);
                    Img_sao_hoso.setVisibility(View.VISIBLE);
                    dangcap_hoso.setText("Giàu có");
                    mData.child("USERS").child(sohoso).child("dangcap").setValue("Giàu có");
                    FirebaseFirestore.getInstance().collection("USER").document(sohoso).update("dangcap", "Giàu có");
                }
                if(sovang_hoso >= 1000){
                    Img_sao_hoso.setImageResource(R.drawable.icon_sao3);
                    Img_sao_hoso.setVisibility(View.VISIBLE);
                    dangcap_hoso.setText("Đại gia");
                    mData.child("USERS").child(sohoso).child("dangcap").setValue("Đại gia");
                    FirebaseFirestore.getInstance().collection("USER").document(sohoso).update("dangcap", "Đại gia");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// Đếm số vàng đang có xong

        // Lấy năm hiện tại
        DateFormat dinhdangnam = new SimpleDateFormat("yyyy");
        sonam = Integer.parseInt(dinhdangnam.format(Calendar.getInstance().getTime()));
        // Lấy năm hiện tại xong

// đọc danh sách
        Source source = Source.CACHE;
        db.collection("USER").document(sohoso).get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Model_laythongtin_trangcanhan layve = documentSnapshot.toObject(Model_laythongtin_trangcanhan.class);
                ten_hoso.setText(layve.getTen());
                Integer sotuoi = sonam - layve.getNamsinh();
                tuoi_hoso.setText(sotuoi.toString());
                gioitinh_hoso.setText(layve.getGioitinh());
                noio_hoso.setText(layve.getNoio());
                hocvan_hoso.setText(layve.getHocvan());
                tinhtranghonnhan_hoso.setText(layve.getTinhtranghonnhan());
                mucdichthamgia_hoso.setText(layve.getMucdichthamgia());
                nghenghiep_hoso.setText(layve.getNghenghiep());
                ngaythamgia_hoso.setText(layve.getNgaydangky()% 100 + "/" + layve.getNgaydangky()%10000/100 + "/" + layve.getNgaydangky()/10000);
                gioithieubanthan_hoso.setText(layve.getGioithieubanthan());
                Picasso.get().load(layve.getAnhdaidien()).into(avata_hoso);

                // tính tuổi cầm tinh con gì
                int congiap = layve.getNamsinh() % 12;
                if(congiap == 1){Img_icontuoi_hoso.setImageResource(R.drawable.t_dau); camtinh_hoso.setText("Cầm tinh con Gà");}
                if(congiap == 2){Img_icontuoi_hoso.setImageResource(R.drawable.t_tuat); camtinh_hoso.setText("Cầm tinh con Cún");}
                if(congiap == 3){Img_icontuoi_hoso.setImageResource(R.drawable.t_hoi);camtinh_hoso.setText("Cầm tinh con Heo");}
                if(congiap == 4){Img_icontuoi_hoso.setImageResource(R.drawable.t_ty);camtinh_hoso.setText("Cầm tinh con Chuột");}
                if(congiap == 5){Img_icontuoi_hoso.setImageResource(R.drawable.t_suu);camtinh_hoso.setText("Cầm tinh con Trâu");}
                if(congiap == 6){Img_icontuoi_hoso.setImageResource(R.drawable.t_dan);camtinh_hoso.setText("Cầm tinh con Hổ");}
                if(congiap == 7){Img_icontuoi_hoso.setImageResource(R.drawable.t_mao);camtinh_hoso.setText("Cầm tinh con Mèo");}
                if(congiap == 8){Img_icontuoi_hoso.setImageResource(R.drawable.t_thin);camtinh_hoso.setText("Cầm tinh con Rồng");}
                if(congiap == 9){Img_icontuoi_hoso.setImageResource(R.drawable.t_ran);camtinh_hoso.setText("Cầm tinh con Rắn");}
                if(congiap == 10){Img_icontuoi_hoso.setImageResource(R.drawable.t_ngo);camtinh_hoso.setText("Cầm tinh con Ngựa");}
                if(congiap == 11){Img_icontuoi_hoso.setImageResource(R.drawable.t_mui);camtinh_hoso.setText("Cầm tinh con Dê");}
                if(congiap == 12){Img_icontuoi_hoso.setImageResource(R.drawable.t_than);camtinh_hoso.setText("Cầm tinh con Khỉ");}
                // tính tuổi cầm tinh con gì xong

                if(documentSnapshot.getString("anh1") != null ||
                        documentSnapshot.getString("anh2") != null ||
                        documentSnapshot.getString("anh3") != null ||
                        documentSnapshot.getString("anh4") != null ||
                        documentSnapshot.getString("anh5") != null ||
                        documentSnapshot.getString("anh6") != null){
                    Linear_anhbimat_hoso.setVisibility(View.VISIBLE);
                }else {
                    Linear_anhbimat_hoso.setVisibility(View.GONE);
                }
            }
        });
// kết thúc đọc danh sách

        Btn_guitinnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Nhan_tin.class);
                intent.putExtra("guiidnguoichat", sohoso);
                intent.putExtra("quaylaitranghoso", true);
                startActivity(intent);
            }
        });

        Btn_thich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (USER != null) {
                    if (Txt_thich.getText().equals("Thích")) {
                        Intent intent = new Intent(getApplicationContext(), Cau_hon.class);
                        intent.putExtra("guiidnguoichat", sohoso);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Cau_hon_bo_sung.class);
                        intent.putExtra("guiidnguoichat", sohoso);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(Ho_so.this, "Bạn chưa đăng nhập nên chưa được thích", Toast.LENGTH_LONG).show();
                }
            }
        });

        Btn_nguoicauhon_hoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Nguoi_cau_hon.class);
                intent.putExtra("guiidnguoichat", sohoso);
                startActivity(intent);
            }
        });

        Btn_bimat_hoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Bi_mat_xem.class);
                intent.putExtra("guiidnguoichat", sohoso);
                startActivity(intent);
            }
        });

        Btn_quaylai_hoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(USER == null){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = getIntent();
                    String quaylaitrang = intent.getStringExtra("thongbaoquaylai");
                    if(quaylaitrang == null){
                        quaylaitrang = "Trangchu";
                    }
                    if(quaylaitrang.equals("thuemuon") || quaylaitrang.equals("phongchat") || quaylaitrang.equals("phongchattinh") || quaylaitrang.equals("xemtuoi") || quaylaitrang.equals("trangnhantin") || quaylaitrang.equals("trangtimquanhday") || quaylaitrang.equals("tranghosohen") || quaylaitrang.equals("trangdanhsachnhanloi")){
                        if(quaylaitrang.equals("thuemuon")){
                            Intent intent2 = new Intent(getApplicationContext(), Thue_muon.class);
                            startActivity(intent2);
                        }
                        if(quaylaitrang.equals("phongchat")){
                            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                            intent2.putExtra("sangtrang1", true);
                            startActivity(intent2);
                        }
                        if(quaylaitrang.equals("phongchattinh")){
                            Intent intent2 = new Intent(getApplicationContext(), Phong_chat_tinh.class);
                            startActivity(intent2);
                        }
                        if(quaylaitrang.equals("trangnhantin")){
                            Intent intent2 = new Intent(getApplicationContext(), Nhan_tin.class);
                            intent2.putExtra("guiidnguoichat", sohoso);
                            startActivity(intent2);
                        }
                        if(quaylaitrang.equals("trangtimquanhday")){
                            Intent intent2 = new Intent(getApplicationContext(), Tim_quanh_day.class);
                            startActivity(intent2);
                        }
                        if(quaylaitrang.equals("tranghosohen")){
                            Intent intent2 = new Intent(getApplicationContext(), Ho_so_hen_gap.class);
                            intent2.putExtra("guiidnguoihen", sohoso);
                            startActivity(intent2);
                        }
                        if(quaylaitrang.equals("trangdanhsachnhanloi")){
                            Intent intent2 = new Intent(getApplicationContext(), Danh_sach_nguoi_nhan_loi.class);
                            startActivity(intent2);
                        }
                        if(quaylaitrang.equals("xemtuoi")){
                            Intent intent2 = new Intent(getApplicationContext(), Hop_tuoi.class);
                            startActivity(intent2);
                        }
                    }else {
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent2);
                    }
                }
            }
        });

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {Hien_thi_qc();}
        }.start();
    }


    private void Hien_thi_qc() {
        Pre = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Pre_luu = Pre.edit();
        solan_hienthi_quangcao = Pre.getInt("solan_hienthi", 0);
        if (solan_hienthi_quangcao < 10) {
            solan_hienthi_quangcao++;
            Pre_luu.putInt("solan_hienthi",solan_hienthi_quangcao);
            Pre_luu.apply();
        }else {
            Pre_luu.putInt("solan_hienthi",0);
            Pre_luu.apply();
            if (interstitialAd.isReady())
            {
                interstitialAd.showAd();
            }
        }
    }

    void createBannerAd() {
        adView = new MaxAdView( getResources().getString(R.string.id_banner), this );
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( this ).getHeight();
        int heightPx = AppLovinSdkUtils.dpToPx( this, heightDp );
        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
        adView.setExtraParameter( "adaptive_banner", "true" );
        ViewGroup rootView = findViewById(R.id.Banner_hoso);
        rootView.addView(adView);
        adView.loadAd();
    }

    void createInterstitialAd() {
        interstitialAd = new MaxInterstitialAd( getResources().getString(R.string.id_toanmanhinh), this );
        interstitialAd.setListener(this);
        interstitialAd.loadAd();
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
                interstitialAd.loadAd();
            }
        }, delayMillis );
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error)
    {
        interstitialAd.loadAd();
    }
    @Override
    public void onAdDisplayed(final MaxAd maxAd) {}
    @Override
    public void onAdClicked(final MaxAd maxAd) {}
    @Override
    public void onAdHidden(final MaxAd maxAd)
    {
        interstitialAd.loadAd();
    }

    private void Anhxa() {
        Linear_anhbimat_hoso = findViewById(R.id.Linear_anhbimat_hoso);
        Btn_bimat_hoso = findViewById(R.id.Btn_bimat_hoso);
        Img_sao_hoso = findViewById(R.id.Img_sao_hoso);
        Btn_guitinnhan = findViewById(R.id.guitinnhan_hoso);
        Btn_thich = findViewById(R.id.thich_hoso);
        Btn_nguoicauhon_hoso = findViewById(R.id.Btn_nguoicauhon_hoso);
        Btn_quaylai_hoso = findViewById(R.id.Btn_quaylai_hoso);
        Txt_thich = findViewById(R.id.tieudethich_hoso);
        Img_icontuoi_hoso = findViewById(R.id.Img_icontuoi_hoso);
        camtinh_hoso = findViewById(R.id.camtinh_hoso);
        ten_hoso = findViewById(R.id.ten_hoso);
        dangcap_hoso = findViewById(R.id.dangcap_hoso);
        tuoi_hoso = findViewById(R.id.tuoi_hoso);
        gioitinh_hoso = findViewById(R.id.gioitinh_hoso);
        hocvan_hoso = findViewById(R.id.hocvan_hoso);
        noio_hoso = findViewById(R.id.noio_hoso);
        tinhtranghonnhan_hoso = findViewById(R.id.tinhtranghonnhan_hoso);
        mucdichthamgia_hoso = findViewById(R.id.mucdich_hoso);
        nghenghiep_hoso = findViewById(R.id.nghenghiep_hoso);
        ngaythamgia_hoso = findViewById(R.id.ngaythamgia_hoso);
        gioithieubanthan_hoso = findViewById(R.id.gioithieubanthan_hoso);
        avata_hoso = findViewById(R.id.imageView3);
        phanguitinnhanvathich_hoso = findViewById(R.id.phanguitinnhanvathich_hoso);
        Txt_songuoicauhon_hoso = findViewById(R.id.Txt_songuoicauhon_hoso);
        Txt_sothoivang_hoso = findViewById(R.id.Txt_sothoivang_hoso);
    }

    @Override
    public void onBackPressed() {
        if(USER == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = getIntent();
            String quaylaitrang = intent.getStringExtra("thongbaoquaylai");
            if(quaylaitrang == null){
                quaylaitrang = "Trangchu";
            }
            if(quaylaitrang.equals("thuemuon") || quaylaitrang.equals("phongchat") || quaylaitrang.equals("phongchattinh") || quaylaitrang.equals("xemtuoi") || quaylaitrang.equals("trangnhantin") || quaylaitrang.equals("trangtimquanhday") || quaylaitrang.equals("tranghosohen") || quaylaitrang.equals("trangdanhsachnhanloi")){
                if(quaylaitrang.equals("thuemuon")){
                    Intent intent2 = new Intent(getApplicationContext(), Thue_muon.class);
                    startActivity(intent2);
                }
                if(quaylaitrang.equals("phongchat")){
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    intent2.putExtra("sangtrang1", true);
                    startActivity(intent2);
                }
                if(quaylaitrang.equals("phongchattinh")){
                    Intent intent2 = new Intent(getApplicationContext(), Phong_chat_tinh.class);
                    startActivity(intent2);
                }
                if(quaylaitrang.equals("trangnhantin")){
                    Intent intent2 = new Intent(getApplicationContext(), Nhan_tin.class);
                    intent2.putExtra("guiidnguoichat", sohoso);
                    startActivity(intent2);
                }
                if(quaylaitrang.equals("trangtimquanhday")){
                    Intent intent2 = new Intent(getApplicationContext(), Tim_quanh_day.class);
                    startActivity(intent2);
                }
                if(quaylaitrang.equals("tranghosohen")){
                    Intent intent2 = new Intent(getApplicationContext(), Ho_so_hen_gap.class);
                    intent2.putExtra("guiidnguoihen", sohoso);
                    startActivity(intent2);
                }
                if(quaylaitrang.equals("trangdanhsachnhanloi")){
                    Intent intent2 = new Intent(getApplicationContext(), Danh_sach_nguoi_nhan_loi.class);
                    startActivity(intent2);
                }
                if(quaylaitrang.equals("xemtuoi")){
                    Intent intent2 = new Intent(getApplicationContext(), Hop_tuoi.class);
                    startActivity(intent2);
                }
            }else {
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
            }
        }

    }
}