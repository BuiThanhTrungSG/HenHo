package com.henho.banmuonhenho;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tao_cuoc_hen extends AppCompatActivity {

    Spinner spinner_diadiemhen_taocuochen, spinner_nguoitratien_taocuochen, spinner_muddich_taocuochen, spinner_hendi_taocuochen;
    TextView Txt_sotien_taocuochen;
    SeekBar seekBar_chiphi_taocuochen;
    Button Btn_taocuochen_taocuochen, Btn_quaylai_taocuochen;
    String banvi = "nghìn";
    String sotien = "50";
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_cuoc_hen);

        spinner_hendi_taocuochen = findViewById(R.id.spinner_hendi_taocuochen);
        spinner_muddich_taocuochen = findViewById(R.id.spinner_muddich_taocuochen);
        spinner_nguoitratien_taocuochen = findViewById(R.id.spinner_nguoitratien_taocuochen);
        spinner_diadiemhen_taocuochen = findViewById(R.id.spinner_diadiemhen_taocuochen);
        Txt_sotien_taocuochen = findViewById(R.id.Txt_sotien_taocuochen);
        seekBar_chiphi_taocuochen = findViewById(R.id.seekBar_chiphi_taocuochen);
        Btn_taocuochen_taocuochen = findViewById(R.id.Btn_taocuochen_taocuochen);
        Btn_quaylai_taocuochen = findViewById(R.id.Btn_quaylai_taocuochen);
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

// Tạo danh sách mục tiêu
        List<String> dulieu_muctieu = new ArrayList<>();
        dulieu_muctieu.add("Tìm người yêu lâu dài");
        dulieu_muctieu.add("Tìm người yêu ngắn hạn");
        dulieu_muctieu.add("Tìm người kết hôn");
        dulieu_muctieu.add("Tìm người tâm sự");
        dulieu_muctieu.add("Tìm bạn bè mới");
        dulieu_muctieu.add("Tìm đối tác kinh doanh");
        ArrayAdapter<String> Adapter_muctieu = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_muctieu);
        spinner_muddich_taocuochen.setAdapter(Adapter_muctieu);
// Tạo xong danh sách mục tiêu

// Tạo danh sách người trả tiền
        List<String> dulieu_nguoitratien = new ArrayList<>();
        dulieu_nguoitratien.add("Người hẹn trả tiền");
        dulieu_nguoitratien.add("Người được mời trả tiền");
        dulieu_nguoitratien.add("Hai bên chia đôi tiền");
        dulieu_nguoitratien.add("Tùy tâm! Ai trả tiền cũng được");
        ArrayAdapter<String> Adapter_nguoitratien = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_nguoitratien);
        spinner_nguoitratien_taocuochen.setAdapter(Adapter_nguoitratien);
// Tạo xong danh sách người trả tiền

// Tạo danh sách địa điểm
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
        spinner_diadiemhen_taocuochen.setAdapter(Adapter_noio);
// Tạo danh sách nơi ở xong

// Tạo danh sách người trả tiền
        final List<String> dulieu_hendi = new ArrayList<>();
        dulieu_hendi.add("Uống cà phê"); dulieu_hendi.add("Xem phim"); dulieu_hendi.add("Mua sắm"); dulieu_hendi.add("Chơi game");
        dulieu_hendi.add("Du lịch trong nước"); dulieu_hendi.add("Du lịch nước ngoài"); dulieu_hendi.add("Lên Bar"); dulieu_hendi.add("Từ thiện");
        dulieu_hendi.add("Chụp ảnh"); dulieu_hendi.add("Vẽ tranh"); dulieu_hendi.add("Cắm hoa"); dulieu_hendi.add("Đọc sách"); dulieu_hendi.add("Học nhóm");
        dulieu_hendi.add("Ăn vặt"); dulieu_hendi.add("Ăn nhà hàng"); dulieu_hendi.add("Ăn chay"); dulieu_hendi.add("Ăn gì cũng được");
        dulieu_hendi.add("Đánh cờ vua"); dulieu_hendi.add("Đánh cờ tướng"); dulieu_hendi.add("Đánh cờ vây");
        dulieu_hendi.add("Đạp xe"); dulieu_hendi.add("Chạy bộ"); dulieu_hendi.add("Đi dạo");
        dulieu_hendi.add("Tập GYM"); dulieu_hendi.add("Tập YOGA"); dulieu_hendi.add("Khiêu vũ");  dulieu_hendi.add("Tập dưỡng sinh");
        dulieu_hendi.add("Chơi cầu lông"); dulieu_hendi.add("Chơi bóng bàn"); dulieu_hendi.add("Chơi Bida");
        dulieu_hendi.add("Chơi đá bóng"); dulieu_hendi.add("Chơi bóng chuyền"); dulieu_hendi.add("Chơi bóng rổ"); dulieu_hendi.add("Bơi lội");
        dulieu_hendi.add("Tắm biển"); dulieu_hendi.add("Leo núi"); dulieu_hendi.add("Chèo thuyền"); dulieu_hendi.add("Nấu ăn"); dulieu_hendi.add("Tập võ");
        dulieu_hendi.add("Chơi golf"); dulieu_hendi.add("Thả diều"); dulieu_hendi.add("Câu cá"); dulieu_hendi.add("Chơi công viên");
        dulieu_hendi.add("Ngắm sông"); dulieu_hendi.add("Ngắm trăng"); dulieu_hendi.add("Ngắm sao"); dulieu_hendi.add("Dạo phố"); dulieu_hendi.add("Sự kiện");
        dulieu_hendi.add("Xem mặt"); dulieu_hendi.add("Hát Karaoke"); dulieu_hendi.add("Đám cưới"); dulieu_hendi.add("Sinh nhật");
        dulieu_hendi.add("Thôi nôi"); dulieu_hendi.add("Họp lớp"); dulieu_hendi.add("Họp đồng hương"); dulieu_hendi.add("Về quê"); dulieu_hendi.add("Thăm bệnh");
        dulieu_hendi.add("Đóng phim"); dulieu_hendi.add("Diễn kịch"); dulieu_hendi.add("Tìm việc làm");  dulieu_hendi.add("Chăm thú cưng");
        dulieu_hendi.add("Phượt"); dulieu_hendi.add("Làm tóc"); dulieu_hendi.add("Suy nghĩ về cuộc đời"); dulieu_hendi.add("Đâu cũng được");

        ArrayAdapter<String> Adapter_hendi = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_hendi);
        spinner_hendi_taocuochen.setAdapter(Adapter_hendi);
// Tạo xong danh sách người trả tiền

// Tạo số tiền
        Txt_sotien_taocuochen.setText(String.valueOf(seekBar_chiphi_taocuochen.getProgress()) + " nghìn");
        seekBar_chiphi_taocuochen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                    Txt_sotien_taocuochen.setText("50 nghìn");
                }else {
                    if(sotien.equals("50")){
                        Txt_sotien_taocuochen.setText("100 triệu");
                    }else {
                        Txt_sotien_taocuochen.setText(sotien + " " + banvi);
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

        Btn_taocuochen_taocuochen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("HEN_GAP").child(USER.getUid()).child("id").setValue(USER.getUid());
                mData.child("HEN_GAP").child(USER.getUid()).child("hengi").setValue(spinner_hendi_taocuochen.getSelectedItem().toString());
                mData.child("HEN_GAP").child(USER.getUid()).child("mucdich").setValue(spinner_muddich_taocuochen.getSelectedItem().toString());
                mData.child("HEN_GAP").child(USER.getUid()).child("sotien").setValue(Txt_sotien_taocuochen.getText().toString());
                mData.child("HEN_GAP").child(USER.getUid()).child("nguoitratien").setValue(spinner_nguoitratien_taocuochen.getSelectedItem().toString());
                mData.child("HEN_GAP").child(USER.getUid()).child("diadiem").setValue(spinner_diadiemhen_taocuochen.getSelectedItem().toString());
                // Lấy năm hiện tại
                DateFormat dinhdangngaythangnam = new SimpleDateFormat("yyyyMMdd");
                String hanhientai = dinhdangngaythangnam.format(Calendar.getInstance().getTime()).toString();
                // Lấy năm hiện tại xong
                mData.child("HEN_GAP").child(USER.getUid()).child("ngaymoi").setValue(hanhientai);
                Toast.makeText(Tao_cuoc_hen.this, "Tạo cuộc hẹn thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Hen_gap.class);
                startActivity(intent);
            }
        });

        Btn_quaylai_taocuochen.setOnClickListener(new View.OnClickListener() {
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
        ViewGroup rootView = findViewById(R.id.Banner_taocuochen);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Hen_gap.class);
        startActivity(intent);
    }
}