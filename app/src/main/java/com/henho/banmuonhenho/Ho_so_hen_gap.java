package com.henho.banmuonhenho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.henho.banmuonhenho.Model.Model_hengap;
import com.henho.banmuonhenho.Model.Model_thanhvien;
import com.squareup.picasso.Picasso;

public class Ho_so_hen_gap extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    ImageView Img_hinh_hshg;
    TextView Txt_diadiem_hshg, Txt_nguoitratien_hshg, Txt_sotien_hshg, Txt_mucdich_hshg, Txt_hengi_hshg, Txt_tennguoimoi_hshg;
    Button Btn_nhanloi_hshg, Btn_xemhoso_hshg, Btn_quaylai_hshg;
    EditText Edt_loinhan_hshg;
    String sohosohengap, kiemtra;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so_hen_gap);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        Img_hinh_hshg = findViewById(R.id.Img_hinh_hshg);
        Txt_tennguoimoi_hshg = findViewById(R.id.Txt_tennguoimoi_hshg);
        Txt_hengi_hshg = findViewById(R.id.Txt_hengi_hshg);
        Txt_mucdich_hshg = findViewById(R.id.Txt_mucdich_hshg);
        Txt_sotien_hshg = findViewById(R.id.Txt_sotien_hshg);
        Txt_nguoitratien_hshg = findViewById(R.id.Txt_nguoitratien_hshg);
        Txt_diadiem_hshg = findViewById(R.id.Txt_diadiem_hshg);
        Btn_nhanloi_hshg = findViewById(R.id.Btn_nhanloi_hshg);
        Btn_xemhoso_hshg = findViewById(R.id.Btn_xemhoso_hshg);
        Btn_quaylai_hshg = findViewById(R.id.Btn_quaylai_hshg);
        Edt_loinhan_hshg = findViewById(R.id.Edt_loinhan_hshg);
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
        sohosohengap = intent.getStringExtra("guiidnguoihen");
// lấy số hồ sơ (ID chủ hồ sơ) từ trang trước truyền qua xong

        mData.child("HEN_GAP").child(sohosohengap).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model_hengap trungchuyen = snapshot.getValue(Model_hengap.class);
                if(trungchuyen.getHengi() != null && trungchuyen.getMucdich() != null && trungchuyen.getSotien() != null && trungchuyen.getNguoitratien() != null && trungchuyen.getDiadiem() != null) {
                    Txt_hengi_hshg.setText(trungchuyen.getHengi());
                    Txt_mucdich_hshg.setText(trungchuyen.getMucdich());
                    Txt_sotien_hshg.setText(trungchuyen.getSotien());
                    Txt_nguoitratien_hshg.setText(trungchuyen.getNguoitratien());
                    Txt_diadiem_hshg.setText(trungchuyen.getDiadiem());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("USERS").child(sohosohengap).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model_thanhvien trungchuyen = snapshot.getValue(Model_thanhvien.class);
                if(trungchuyen.getAnhdaidien() != null && trungchuyen.getTen() != null) {
                    Picasso.get().load(trungchuyen.getAnhdaidien()).into(Img_hinh_hshg);
                    Txt_tennguoimoi_hshg.setText(trungchuyen.getTen());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Img_hinh_hshg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Ho_so.class);
                intent.putExtra("guiidnguoichat", sohosohengap);
                intent.putExtra("thongbaoquaylai", "tranghosohen");
                startActivity(intent);
            }
        });

        Btn_xemhoso_hshg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Ho_so.class);
                intent.putExtra("guiidnguoichat", sohosohengap);
                intent.putExtra("thongbaoquaylai", "tranghosohen");
                startActivity(intent);
            }
        });

// Kiểm tra xem đã từng gửi lời chưa
        if(USER != null){
            kiemtra = null;
            mData.child("NHAN_LOI").child(sohosohengap).child(USER.getUid()).child("id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    kiemtra = (String) snapshot.getValue();
                    if(kiemtra != null){
                        Edt_loinhan_hshg.setText("Bạn đã nhận lời mời của bạn này rồi. Hãy đợi bạn ấy phản hồi nhé");
                        Btn_nhanloi_hshg.setVisibility(View.INVISIBLE);
                    }else {
                        Btn_nhanloi_hshg.setVisibility(View.VISIBLE);
                        Edt_loinhan_hshg.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
// Kiểm tra xem đã từng gửi lời chưa xong

        Btn_nhanloi_hshg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(USER == null) {
                    AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Ho_so_hen_gap.this);
                    alerDialogBuilder.setMessage("Bạn không thể nhận lời mời do chưa đăng nhập.\nBạn có muốn đăng nhập không");
                    alerDialogBuilder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), Dang_nhap.class);
                            startActivity(intent);
                        }
                    });
                    alerDialogBuilder.setNegativeButton("Không", null);
                    AlertDialog alerDialog = alerDialogBuilder.create();
                    alerDialog.show();
                }else {
                    if(Edt_loinhan_hshg.getText().toString().length() == 0){
                        Toast.makeText(Ho_so_hen_gap.this, "Bạn chưa nhập lời nhắn cho người mời", Toast.LENGTH_SHORT).show();
                    }else {
                        mData.child("NHAN_LOI").child(sohosohengap).child(USER.getUid()).child("id").setValue(USER.getUid());
                        mData.child("NHAN_LOI").child(sohosohengap).child(USER.getUid()).child("loinhan").setValue(Edt_loinhan_hshg.getText().toString());

                        // Thông báo lời cầu hôn
                        mData.child("USERS").child(sohosohengap).child("thongbao_nhanloi").setValue(true);

                        Intent intent = new Intent(getApplicationContext(), Hen_gap.class);
                        startActivity(intent);
                    }
                }
            }
        });

        Btn_quaylai_hshg.setOnClickListener(new View.OnClickListener() {
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
        ViewGroup rootView = findViewById(R.id.Banner_hshg);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Hen_gap.class);
        startActivity(intent);
    }
}