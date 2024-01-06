package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.love.henho.Model.Model_thanhvien;
import com.love.henho.Model.Thongtin_user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Dang_ky extends AppCompatActivity {

    Spinner gioitinh_dangky, noio_dangky, trinhdo_dangky, tinhtranghonnhan_dangky, muctieu_dangky, namsinh_dangky;
    Button Btn_dangky;
    EditText Edt_email, Edt_matkhau, Edt_ten, Edt_nghenghiep, Edt_gioithieu, Edt_timnguoithenao;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    String dk_ten, dk_email, dk_matkhau, dk_nghenghiep;
    String dk_noio, dk_gioitinh, dk_tinhtranghonnhan, dk_trinhdo, dk_mucdichthamgia, dk_tugioithieu, dk_timnguoinhuthenao;
    Integer sonam, songaythangnam, dk_namsinh;


    DocumentReference Danhsach_thanhvien_document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        Anh_xa_dangky();
        mAuth = FirebaseAuth.getInstance();
        Danhsach_thanhvien_document = FirebaseFirestore.getInstance().collection("USER").document("sd");

// Tạo danh sách giới tính
        List<String> dulieu_gioitinh = new ArrayList<>();
        dulieu_gioitinh.add("Nam");
        dulieu_gioitinh.add("Nữ");
        dulieu_gioitinh.add("Les");
        dulieu_gioitinh.add("Gay");
        ArrayAdapter<String> Adapter_gioitinh = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_gioitinh);
        gioitinh_dangky.setAdapter(Adapter_gioitinh);
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
        ArrayAdapter<String> Adapter_noio = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dulieu_noio);
        noio_dangky.setAdapter(Adapter_noio);
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
        ArrayAdapter<String> Adapter_hocvan = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dulieu_trinhdo);
        trinhdo_dangky.setAdapter(Adapter_hocvan);
// Tạo xong danh sách học vấn

// Tạo danh sách tính trạng hôn nhân
        List<String> dulieu_tinhtranghonnhan = new ArrayList<>();
        dulieu_tinhtranghonnhan.add("Độc thân");
        dulieu_tinhtranghonnhan.add("Đang có người yêu");
        dulieu_tinhtranghonnhan.add("Đã ly hôn");
        dulieu_tinhtranghonnhan.add("Ở góa");
        dulieu_tinhtranghonnhan.add("Quan hệ phức tạp");
        ArrayAdapter<String> Adapter_tinhtranghonnhan = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dulieu_tinhtranghonnhan);
        tinhtranghonnhan_dangky.setAdapter(Adapter_tinhtranghonnhan);
// Tạo xong danh sách tình trạng hôn nhân

// Tạo danh sách mục tiêu
        List<String> dulieu_muctieu = new ArrayList<>();
        dulieu_muctieu.add("Tìm người yêu lâu dài");
        dulieu_muctieu.add("Tìm người yêu ngắn hạn");
        dulieu_muctieu.add("Tìm người kết hôn");
        dulieu_muctieu.add("Tìm người tâm sự");
        dulieu_muctieu.add("Tìm bạn bè mới");
        dulieu_muctieu.add("Tìm đối tác kinh doanh");
        ArrayAdapter<String> Adapter_muctieu = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dulieu_muctieu);
        muctieu_dangky.setAdapter(Adapter_muctieu);
// Tạo xong danh sách mục tiêu

// Tạo danh sách năm sinh
        // Lấy năm hiện tại
        @SuppressLint("SimpleDateFormat") DateFormat dinhdangnam = new SimpleDateFormat("yyyy");
        sonam = Integer.parseInt(dinhdangnam.format(Calendar.getInstance().getTime()));
        @SuppressLint("SimpleDateFormat") DateFormat dinhdangngaythangnam = new SimpleDateFormat("yyyyMMdd");
        songaythangnam = Integer.parseInt(dinhdangngaythangnam.format(Calendar.getInstance().getTime()));
        // Lấy năm hiện tại xong

        List<String> dulieu_namsinh = new ArrayList<>();

        for(int i = 0; i <70; i++ ){
            int nambatdau = sonam - 13 - i;
            dulieu_namsinh.add(String.valueOf(nambatdau));
        }
        ArrayAdapter<String> Adapter_namsinh = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dulieu_namsinh);
        namsinh_dangky.setAdapter(Adapter_namsinh);
// Tạo danh sách năm sinh xong

        Btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dk_ten = Edt_ten.getText().toString();
                dk_email = Edt_email.getText().toString();
                dk_matkhau = Edt_matkhau.getText().toString();
                dk_nghenghiep = Edt_nghenghiep.getText().toString();
                dk_timnguoinhuthenao = Edt_timnguoithenao.getText().toString();

                dk_namsinh = Integer.parseInt(namsinh_dangky.getSelectedItem().toString());
                dk_noio = noio_dangky.getSelectedItem().toString();
                dk_gioitinh = gioitinh_dangky.getSelectedItem().toString();
                dk_tinhtranghonnhan = tinhtranghonnhan_dangky.getSelectedItem().toString();
                dk_trinhdo = trinhdo_dangky.getSelectedItem().toString();
                dk_mucdichthamgia = muctieu_dangky.getSelectedItem().toString();
                dk_tugioithieu = Edt_gioithieu.getText().toString();


                if (dk_ten.equals(getResources().getString(R.string.khachvanglai)) || dk_email.length() == 0 || dk_matkhau.length() == 0 || dk_nghenghiep.length() == 0 || dk_ten.length() == 0 || dk_tugioithieu.length() == 0 || dk_timnguoinhuthenao.length() == 0){
                    if (dk_ten.equals(getResources().getString(R.string.khachvanglai))){
                        Toast.makeText(Dang_ky.this, "Bạn không được đặt tên là " + getResources().getString(R.string.khachvanglai), Toast.LENGTH_SHORT).show();
                    }
                    if(dk_email.length() == 0){
                        Toast.makeText(Dang_ky.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                    }
                    if(dk_matkhau.length() == 0) {
                        Toast.makeText(Dang_ky.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                    if(dk_ten.length() == 0){
                        Toast.makeText(Dang_ky.this, "Bạn chưa nhập tên", Toast.LENGTH_SHORT).show();
                    }
                    if(dk_nghenghiep.length() == 0){
                        Toast.makeText(Dang_ky.this, "Bạn chưa nhập nghề nghiệp", Toast.LENGTH_SHORT).show();
                    }
                    if(dk_tugioithieu.length() == 0){
                        Toast.makeText(Dang_ky.this, "Bạn chưa nhập thông tin tự giới thiệu", Toast.LENGTH_SHORT).show();
                    }
                    if(dk_timnguoinhuthenao.length() == 0){
                        Toast.makeText(Dang_ky.this, "Bạn chưa nhập mong muốn tìm bạn", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Dang_ky.this, "Đang đăng ký, xin chờ một chút", Toast.LENGTH_SHORT).show();

                    mAuth.createUserWithEmailAndPassword(dk_email, dk_matkhau)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        USER = mAuth.getCurrentUser();
                                        mData = FirebaseDatabase.getInstance().getReference();
                                        Toast.makeText(Dang_ky.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                        Model_thanhvien trunggian = new Model_thanhvien(USER.getUid(), dk_ten, dk_namsinh, dk_gioitinh, dk_noio, dk_tinhtranghonnhan, dk_trinhdo, dk_tugioithieu + "\nTìm người: " + dk_timnguoinhuthenao, dk_mucdichthamgia, dk_nghenghiep, songaythangnam, songaythangnam, "https://firebasestorage.googleapis.com/v0/b/banmuonhh-a42dd.appspot.com/o/logonho.png?alt=media&token=3fcf15c8-2402-4edf-8102-7a2a0347eb6b", 0,dk_email, 0, "Không chọn");
                                        FirebaseFirestore.getInstance().collection("USER").document(USER.getUid()).set(trunggian);

                                        // Cập nhật thông tin cho phần duyệt nội dung giới thiệu
                                        Map<String,Object> noidung = new HashMap<>();
                                        noidung.put("ten", trunggian.getTen());
                                        noidung.put("ngaysua", trunggian.getNgaydangxuat());
                                        noidung.put("noidung", trunggian.getGioithieubanthan());
                                        noidung.put("id", trunggian.getID());
                                        mData.child("QUAN_LY").child("duyetnoidung").child(USER.getUid()).setValue(noidung);

                                        // Cập nhật thông tin cho phần duyệt nội dung giới thiệu xong
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(Dang_ky.this, "Đăng ký thất bại.Email của bạn đã được dùng để đăng ký trước đây hoặc mật khẩu quả yếu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

    }
    private void Anh_xa_dangky() {
        gioitinh_dangky = findViewById(R.id.spinner_gioitinh_dangky);
        noio_dangky = findViewById(R.id.spinner_noio_dangky);
        trinhdo_dangky = findViewById(R.id.spinner_trinhdo_dangky);
        tinhtranghonnhan_dangky = findViewById(R.id.spinner_tinhtranghonnhan_dangky);
        muctieu_dangky = findViewById(R.id.muctieu_dangky);
        namsinh_dangky = findViewById(R.id.namsinh_dangky);
        Btn_dangky = findViewById(R.id.nutdangky);
        Edt_email = findViewById(R.id.email_dangky);
        Edt_matkhau = findViewById(R.id.matkhau_dangky);
        Edt_nghenghiep = findViewById(R.id.nghenghiep_dangky);
        Edt_ten = findViewById(R.id.ten_dangky);
        Edt_gioithieu = findViewById(R.id.Edt_gioithieu);
        Edt_timnguoithenao = findViewById(R.id.Edt_timnguoithenao);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Dang_nhap.class);
        startActivity(intent);
    }
}