package com.love.henho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.love.henho.Model.Model_thuemuon;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Dang_tin_thue_muon extends AppCompatActivity {

    Spinner spinner_hendi_dangtinthuemuon, spinner_muddich_dangtinthuemuon, spinner_nguoitratien_dangtinthuemuon, spinner_diadiemhen_dangtinthuemuon;
    TextView Txt_sotien_dangtinthuemuon;
    SeekBar seekBar_chiphi_dangtinthuemuon;
    Button Btn_quaylai_dangtinthuemuon, Btn_taocuochen_dangtinthuemuon;
    String ngayientai;
    String banvi = "nghìn";
    String sotien = "50";
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_thue_muon);

        spinner_hendi_dangtinthuemuon = findViewById(R.id.spinner_hendi_dangtinthuemuon);
        spinner_muddich_dangtinthuemuon = findViewById(R.id.spinner_muddich_dangtinthuemuon);
        spinner_nguoitratien_dangtinthuemuon = findViewById(R.id.spinner_nguoitratien_dangtinthuemuon);
        spinner_diadiemhen_dangtinthuemuon = findViewById(R.id.spinner_diadiemhen_dangtinthuemuon);
        Txt_sotien_dangtinthuemuon = findViewById(R.id.Txt_sotien_dangtinthuemuon);
        seekBar_chiphi_dangtinthuemuon = findViewById(R.id.seekBar_chiphi_dangtinthuemuon);
        Btn_taocuochen_dangtinthuemuon = findViewById(R.id.Btn_taocuochen_dangtinthuemuon);
        Btn_quaylai_dangtinthuemuon = findViewById(R.id.Btn_quaylai_dangtinthuemuon);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();

        // Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );

        DateFormat dinhdangngaythangnam = new SimpleDateFormat("yyyyMMdd");
        ngayientai = dinhdangngaythangnam.format(Calendar.getInstance().getTime()).toString();

         List<String> dulieu_hendi = new ArrayList<>();
        dulieu_hendi.add("Thuê bạn trai");
        dulieu_hendi.add("Thuê bạn gái");
        dulieu_hendi.add("Nhận làm bạn trai");
        dulieu_hendi.add("Nhận làm bạn gái");
        ArrayAdapter<String> Adapter_hendi = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_hendi);
        spinner_hendi_dangtinthuemuon.setAdapter(Adapter_hendi);

        List<String> dulieu_de = new ArrayList<>();
        dulieu_de.add("Dẫn về ra mắt");
        dulieu_de.add("Đi đám cưới");
        dulieu_de.add("Dự sinh nhật");
        dulieu_de.add("Thăm họ hàng");
        dulieu_de.add("Họp đồng hương");
        dulieu_de.add("Họp lớp");
        ArrayAdapter<String> Adapter_de = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_de);
        spinner_muddich_dangtinthuemuon.setAdapter(Adapter_de);

        List<String> dulieu_nguoitratien = new ArrayList<>();
        dulieu_nguoitratien.add("Hết cấp 1");
        dulieu_nguoitratien.add("Hết cấp 2");
        dulieu_nguoitratien.add("Hết cấp 3");
        dulieu_nguoitratien.add("Trung cấp");
        dulieu_nguoitratien.add("Cao đẳng");
        dulieu_nguoitratien.add("Đại học");
        dulieu_nguoitratien.add("Thạc sỹ");
        dulieu_nguoitratien.add("Tiến sỹ");
        ArrayAdapter<String> Adapter_nguoitratien = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_nguoitratien);
        spinner_nguoitratien_dangtinthuemuon.setAdapter(Adapter_nguoitratien);

        List<String> dulieu_noio = new ArrayList<>();
        dulieu_noio.add("An Giang");dulieu_noio.add("Hà Tĩnh");dulieu_noio.add("Quảng Ninh");
        dulieu_noio.add("Bà Rịa - Vũng Tàu");dulieu_noio.add("Hải Dương");dulieu_noio.add("Quảng Trị");
        dulieu_noio.add("Bắc Giang");dulieu_noio.add("Hậu Giang");dulieu_noio.add("Sóc Trăng");
        dulieu_noio.add("Bắc Kạn");dulieu_noio.add("Hòa Bình");dulieu_noio.add("Sơn La");
        dulieu_noio.add("Bạc Liêu");dulieu_noio.add("Hưng Yên");dulieu_noio.add("Tây Ninh");
        dulieu_noio.add("Bắc Ninh");dulieu_noio.add("Khánh Hòa");dulieu_noio.add("Thái Bình");
        dulieu_noio.add("Bến Tre");dulieu_noio.add("Kiên Giang");dulieu_noio.add("Thái Nguyên");
        dulieu_noio.add("Bình Định");dulieu_noio.add("Kon Tum");dulieu_noio.add("Thanh Hóa");
        dulieu_noio.add("Bình Dương");dulieu_noio.add("Lai Châu");dulieu_noio.add("Thừa Thiên Huế");
        dulieu_noio.add("Bình Phước");dulieu_noio.add("Lâm Đồng");dulieu_noio.add("Tiền Giang");
        dulieu_noio.add("Bình Thuận");dulieu_noio.add("Lạng Sơn");dulieu_noio.add("Trà Vinh");
        dulieu_noio.add("Cà Mau");dulieu_noio.add("Lào Cai");dulieu_noio.add("Tuyên Quang");
        dulieu_noio.add("Cao Bằng");dulieu_noio.add("Long An");dulieu_noio.add("Vĩnh Long");
        dulieu_noio.add("Đắk Lắk");dulieu_noio.add("Nam Định");dulieu_noio.add("Vĩnh Phúc");
        dulieu_noio.add("Đắk Nông");dulieu_noio.add("Nghệ An");dulieu_noio.add("Yên Bái");
        dulieu_noio.add("Điện Biên");dulieu_noio.add("Ninh Bình");dulieu_noio.add("Phú Yên");
        dulieu_noio.add("Đồng Nai");dulieu_noio.add("Ninh Thuận");dulieu_noio.add("Cần Thơ");
        dulieu_noio.add("Đồng Tháp");dulieu_noio.add("Phú Thọ");dulieu_noio.add("Đà Nẵng");
        dulieu_noio.add("Gia Lai");dulieu_noio.add("Quảng Bình");dulieu_noio.add("Hải Phòng");
        dulieu_noio.add("Hà Giang");dulieu_noio.add("Quảng Nam");
        dulieu_noio.add("Hà Nam");dulieu_noio.add("Quảng Ngãi");
        // Sắp xếp danh sách
        Collections.sort(dulieu_noio, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        dulieu_noio.add(0, "TP Hồ Chí Minh");
        dulieu_noio.add(1,"Hà Nội");
        dulieu_noio.add(2,"Nước ngoài");
        // Sắp xếp danh sách xong
        ArrayAdapter<String> Adapter_noio = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_noio);
        spinner_diadiemhen_dangtinthuemuon.setAdapter(Adapter_noio);

        Txt_sotien_dangtinthuemuon.setText(String.valueOf(seekBar_chiphi_dangtinthuemuon.getProgress()) + " nghìn");
        seekBar_chiphi_dangtinthuemuon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(progress < 100){
                    if(progress == 0){
                        sotien = "40";
                        banvi = "nghìn";
                    }
                    if(progress != 0){
                        sotien = String.valueOf(progress/10);
                        banvi = "trăm nghìn";
                    }
                }else {
                    banvi = "triệu";
                    sotien = String.valueOf(progress/10);
                }

                if(sotien.equals("0")){
                    Txt_sotien_dangtinthuemuon.setText("50 nghìn");
                }else {
                    if(sotien.equals("50")){
                        Txt_sotien_dangtinthuemuon.setText("100 triệu");
                    }else {
                        Txt_sotien_dangtinthuemuon.setText(sotien + " " + banvi);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Btn_taocuochen_dangtinthuemuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat dinhdangngaythangnam = new SimpleDateFormat("yyyyMMdd");
                String ngayientai = dinhdangngaythangnam.format(Calendar.getInstance().getTime()).toString();
                Model_thuemuon trunggian = new Model_thuemuon(USER.getUid(),
                        spinner_hendi_dangtinthuemuon.getSelectedItem().toString(),
                        spinner_muddich_dangtinthuemuon.getSelectedItem().toString(),
                        spinner_diadiemhen_dangtinthuemuon.getSelectedItem().toString(),
                        Txt_sotien_dangtinthuemuon.getText().toString(),
                        spinner_nguoitratien_dangtinthuemuon.getSelectedItem().toString(), ngayientai);

                mData.child("THUE_MUON").child(USER.getUid()).setValue(trunggian);
                Toast.makeText(Dang_tin_thue_muon.this, "Đăng tin thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Thue_muon.class);
                startActivity(intent);
            }
        });

        Btn_quaylai_dangtinthuemuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Thue_muon.class);
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
        ViewGroup rootView = findViewById(R.id.Banner_dangtinthuemuon);
        rootView.addView(adView);
        adView.loadAd();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Thue_muon.class);
        startActivity(intent);
    }
}