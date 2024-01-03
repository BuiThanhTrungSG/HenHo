package com.henho.banmuonhenho;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sua_ho_so extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser User;

    Spinner gioitinh_suahoso, noio_suahoso, trinhdo_suahoso, tinhtranghonnhan_suahoso, muctieu_suahoso, namsinh_suahoso;
    Button Btn_luulai_suahoso;
    EditText Edt_ten_suahoso, Edt_nghenghiep_suahoso, Edt_gioithieu_suahoso, Edt_timnguoithenao_suahoso;
    Integer sonam_suahoso, songaythangnam_suahoso, dk_namsinh_suahoso;
    String dk_ten_suahoso, dk_nghenghiep_suahoso, dk_timnguoinhuthenao_suahoso;
    String dk_noio_suahoso, dk_gioitinh_suahoso, dk_tinhtranghonnhan_suahoso, dk_trinhdo_suahoso, dk_mucdichthamgia_suahoso, dk_tugioithieu_suahoso;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_ho_so);

        Anh_xa_suahoso();

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        User = mAuth.getCurrentUser();
// Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );

// Tạo danh sách giới tính
        List<String> dulieu_gioitinh = new ArrayList<>();
        dulieu_gioitinh.add("Nam");
        dulieu_gioitinh.add("Nữ");
        dulieu_gioitinh.add("Les");
        dulieu_gioitinh.add("Gay");
        ArrayAdapter<String> Adapter_gioitinh = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_gioitinh);
        gioitinh_suahoso.setAdapter(Adapter_gioitinh);
// Tạo xong danh sách giới tính

// Tạo danh sách nơi ở
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
        noio_suahoso.setAdapter(Adapter_noio);
// Tạo danh sách nơi ở xong

// Tạo danh sách học vấn
        List<String> dulieu_trinhdo = new ArrayList<>();
        dulieu_trinhdo.add("Hết cấp 1");
        dulieu_trinhdo.add("Hết cấp 2");
        dulieu_trinhdo.add("Hết cấp 3");
        dulieu_trinhdo.add("Trung cấp");
        dulieu_trinhdo.add("Cao đẳng");
        dulieu_trinhdo.add("Đại học");
        dulieu_trinhdo.add("Thạc sỹ");
        dulieu_trinhdo.add("Tiến sỹ");
        ArrayAdapter<String> Adapter_hocvan = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_trinhdo);
        trinhdo_suahoso.setAdapter(Adapter_hocvan);
// Tạo xong danh sách học vấn

// Tạo danh sách tính trạng hôn nhân
        List<String> dulieu_tinhtranghonnhan = new ArrayList<>();
        dulieu_tinhtranghonnhan.add("Độc thân");
        dulieu_tinhtranghonnhan.add("Đang có người yêu");
        dulieu_tinhtranghonnhan.add("Đã ly hôn");
        dulieu_tinhtranghonnhan.add("Ở góa");
        dulieu_tinhtranghonnhan.add("Quan hệ phức tạp");
        ArrayAdapter<String> Adapter_tinhtranghonnhan = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_tinhtranghonnhan);
        tinhtranghonnhan_suahoso.setAdapter(Adapter_tinhtranghonnhan);
// Tạo xong danh sách tình trạng hôn nhân

// Tạo danh sách mục tiêu
        List<String> dulieu_muctieu = new ArrayList<>();
        dulieu_muctieu.add("Tìm người yêu lâu dài");
        dulieu_muctieu.add("Tìm người yêu ngắn hạn");
        dulieu_muctieu.add("Tìm người kết hôn");
        dulieu_muctieu.add("Tìm người tâm sự");
        dulieu_muctieu.add("Tìm bạn bè mới");
        dulieu_muctieu.add("Tìm đối tác kinh doanh");
        ArrayAdapter<String> Adapter_muctieu = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_muctieu);
        muctieu_suahoso.setAdapter(Adapter_muctieu);
// Tạo xong danh sách mục tiêu

// Tạo danh sách năm sinh
        // Lấy năm hiện tại
        DateFormat dinhdangnam = new SimpleDateFormat("yyyy");
        sonam_suahoso = Integer.parseInt(dinhdangnam.format(Calendar.getInstance().getTime()));
        DateFormat dinhdangngaythangnam = new SimpleDateFormat("yyyyMMdd");
        songaythangnam_suahoso = Integer.parseInt(dinhdangngaythangnam.format(Calendar.getInstance().getTime()));
        // Lấy năm hiện tại xong

        List<String> dulieu_namsinh = new ArrayList<>();

        for(int i = 0; i <70; i++ ){
            int nambatdau = sonam_suahoso - 13 - i;
            dulieu_namsinh.add(String.valueOf(nambatdau));
        }
        ArrayAdapter<String> Adapter_namsinh = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_namsinh);
        namsinh_suahoso.setAdapter(Adapter_namsinh);
// Tạo danh sách năm sinh xong

        Btn_luulai_suahoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dk_ten_suahoso = Edt_ten_suahoso.getText().toString();
                dk_nghenghiep_suahoso = Edt_nghenghiep_suahoso.getText().toString();
                dk_namsinh_suahoso = Integer.parseInt(namsinh_suahoso.getSelectedItem().toString());
                dk_noio_suahoso = noio_suahoso.getSelectedItem().toString();
                dk_gioitinh_suahoso = gioitinh_suahoso.getSelectedItem().toString();
                dk_tinhtranghonnhan_suahoso = tinhtranghonnhan_suahoso.getSelectedItem().toString();
                dk_trinhdo_suahoso = trinhdo_suahoso.getSelectedItem().toString();
                dk_mucdichthamgia_suahoso = muctieu_suahoso.getSelectedItem().toString();
                dk_tugioithieu_suahoso = Edt_gioithieu_suahoso.getText().toString();
                dk_timnguoinhuthenao_suahoso = Edt_timnguoithenao_suahoso.getText().toString();

                if (dk_ten_suahoso.equals(getResources().getString(R.string.khachvanglai)) || dk_nghenghiep_suahoso.length() == 0 || dk_ten_suahoso.length() == 0 || dk_tugioithieu_suahoso.length() == 0 || dk_timnguoinhuthenao_suahoso.length() == 0){
                    if (dk_ten_suahoso.equals(getResources().getString(R.string.khachvanglai))){
                        Toast.makeText(Sua_ho_so.this, "Bạn không được đặt tên là " + getResources().getString(R.string.khachvanglai), Toast.LENGTH_SHORT).show();
                    }if(dk_ten_suahoso.length() == 0){
                        Toast.makeText(Sua_ho_so.this, "Bạn chưa nhập tên", Toast.LENGTH_SHORT).show();
                    }
                    if(dk_nghenghiep_suahoso.length() == 0){
                        Toast.makeText(Sua_ho_so.this, "Bạn chưa nhập nghề nghiệp", Toast.LENGTH_SHORT).show();
                    }
                    if(dk_tugioithieu_suahoso.length() == 0){
                        Toast.makeText(Sua_ho_so.this, "Bạn chưa nhập thông tin tự giới thiệu", Toast.LENGTH_SHORT).show();
                    }
                    if(dk_timnguoinhuthenao_suahoso.length() == 0){
                        Toast.makeText(Sua_ho_so.this, "Bạn chưa nhập mong muốn tìm bạn", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Sua_ho_so.this, "Sửa hồ sơ thành công", Toast.LENGTH_SHORT).show();
                    mData.child("USERS").child(User.getUid()).child("ten").setValue(dk_ten_suahoso);
                    mData.child("USERS").child(User.getUid()).child("nghenghiep").setValue(dk_nghenghiep_suahoso);
                    mData.child("USERS").child(User.getUid()).child("namsinh").setValue(dk_namsinh_suahoso);
                    mData.child("USERS").child(User.getUid()).child("noio").setValue(dk_noio_suahoso);
                    mData.child("USERS").child(User.getUid()).child("gioitinh").setValue(dk_gioitinh_suahoso);
                    mData.child("USERS").child(User.getUid()).child("tinhtranghonnhan").setValue(dk_tinhtranghonnhan_suahoso);
                    mData.child("USERS").child(User.getUid()).child("hocvan").setValue(dk_trinhdo_suahoso);
                    mData.child("USERS").child(User.getUid()).child("mucdichthamgia").setValue(dk_mucdichthamgia_suahoso);
                    mData.child("USERS").child(User.getUid()).child("gioithieubanthan").setValue("Thông tin này đang được xét duyệt và sẽ hiển thị nếu đảm bảo quy định.");
                    mData.child("USERS").child(User.getUid()).child("online").setValue(dk_tugioithieu_suahoso + "\nTìm người: " + dk_timnguoinhuthenao_suahoso);
                    mData.child("USERS").child(User.getUid()).child("ngaydangxuat").setValue(songaythangnam_suahoso);

                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("ten", dk_ten_suahoso);
                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("nghenghiep", dk_nghenghiep_suahoso);
                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("namsinh", dk_namsinh_suahoso);
                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("noio", dk_noio_suahoso);
                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("gioitinh", dk_gioitinh_suahoso);
                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("tinhtranghonnhan", dk_tinhtranghonnhan_suahoso);
                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("hocvan", dk_trinhdo_suahoso);
                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("mucdichthamgia", dk_mucdichthamgia_suahoso);
                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("gioithieubanthan","Thông tin này đang được xét duyệt và sẽ hiển thị nếu đảm bảo quy định.");
                    FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("ngaydangxuat", songaythangnam_suahoso);

                    // Cập nhật thông tin cho phần duyệt nội dung giới thiệu
                    mData.child("QUAN_LY").child("duyetnoidung").child(User.getUid()).child("ten").setValue(dk_ten_suahoso);
                    mData.child("QUAN_LY").child("duyetnoidung").child(User.getUid()).child("ngaysua").setValue(songaythangnam_suahoso);
                    mData.child("QUAN_LY").child("duyetnoidung").child(User.getUid()).child("noidung").setValue(dk_tugioithieu_suahoso + "\n" + dk_timnguoinhuthenao_suahoso);
                    mData.child("QUAN_LY").child("duyetnoidung").child(User.getUid()).child("id").setValue(User.getUid());
                    // Cập nhật thông tin cho phần duyệt nội dung giới thiệu xong
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("sangtrang3", true);
                    startActivity(intent);

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor spLuu = sp.edit();
                    spLuu.remove("ngaygiotai2");
                    spLuu.putInt("ngaygiotai2", 0);
                    spLuu.apply();
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
        ViewGroup rootView = findViewById(R.id.Banner_suahoso);
        rootView.addView(adView);
        adView.loadAd();
    }

    private void Anh_xa_suahoso() {

        gioitinh_suahoso = findViewById(R.id.spinner_gioitinh_suahoso);
        noio_suahoso = findViewById(R.id.spinner_noio_suahoso);
        trinhdo_suahoso = findViewById(R.id.spinner_trinhdo_suahoso);
        tinhtranghonnhan_suahoso = findViewById(R.id.spinner_tinhtranghonnhan_suahoso);
        muctieu_suahoso = findViewById(R.id.muctieu_suahoso);
        namsinh_suahoso = findViewById(R.id.namsinh_suahoso);
        Btn_luulai_suahoso = findViewById(R.id.nutluulai_suahoso);
        Edt_nghenghiep_suahoso = findViewById(R.id.nghenghiep_suahoso);
        Edt_ten_suahoso = findViewById(R.id.ten_suahoso);
        Edt_gioithieu_suahoso = findViewById(R.id.Edt_gioithieu_suahoso);
        Edt_timnguoithenao_suahoso = findViewById(R.id.Edt_timnguoithenao_suahoso);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("sangtrang3", true);
        startActivity(intent);
    }
}