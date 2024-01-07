package com.love.henho.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.love.henho.Bi_mat_them;
import com.love.henho.Dang_ky;
import com.love.henho.Dang_nhap;
import com.love.henho.Doi_mat_khau;
import com.love.henho.MainActivity;
import com.love.henho.Model.Model_laythongtin_trangcanhan;
import com.love.henho.Model.Thongtin_user;
import com.love.henho.MyService;
import com.love.henho.Nguoi_cau_hon;
import com.love.henho.Nguoi_duoc_cau_hon;
import com.love.henho.R;
import com.love.henho.Sua_ho_so;
import com.love.henho.Tang_dang_cap;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Fragment_canhan extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    String sonam;
    ImageView Btn_bimat_canhan, avata_hoso, Img_sao_canhan;
    ConstraintLayout phanhoso;
    LinearLayout LinearLayuot_denghidangnhap;
    ImageView Btn_suahoso, Btn_doimatkhau, Btn_dangxuat_canhan, Btn_tangdangcap_canhan;
    Button Btn_nguoiduocauhon_canhan, Btn_danhsachnguoithich_canhan, Btn_dangnhap_canhan, Btn_dangky_canhan, Btn_doianhdaidien;
    TextView Txt_nguoidathich_canhan, Txt_dangcap_canhan, Txt_nguoithich_canhan, Txt_sotien_dangcap_canhan, ten_hoso, tuoi_hoso, gioitinh_hoso, noio_hoso, hocvan_hoso, tinhtranghonnhan_hoso, mucdichthamgia_hoso, nghenghiep_hoso, ngaythamgia_hoso, ngaydangnhap_hoso, gioithieubanthan_hoso;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Integer sovang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_canhan, container,false);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        AnhXa(view);

        if(USER == null){
            phanhoso.setVisibility(View.INVISIBLE);
            LinearLayuot_denghidangnhap.setVisibility(View.VISIBLE);
            Btn_dangky_canhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Dang_ky.class);
                    startActivity(intent);
                }
            });
            Btn_dangnhap_canhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),Dang_nhap.class);
                    startActivity(intent);
                }
            });
        }else {
            phanhoso.setVisibility(View.VISIBLE);
            LinearLayuot_denghidangnhap.setVisibility(View.INVISIBLE);

            // Lấy năm hiện tại
            DateFormat dinhdangnam = new SimpleDateFormat("yyyy");
            sonam = dinhdangnam.format(Calendar.getInstance().getTime());
            final DateFormat dinhdanglai = new SimpleDateFormat("dd/MM/yyyy");
            // Lấy năm hiện tại xong

// Đếm số vàng đang có
            Txt_sotien_dangcap_canhan.setText(String.valueOf(sovang));
            if (sovang > 1000) {
                Txt_dangcap_canhan.setText("Đại gia");
                Txt_dangcap_canhan.setTextColor(Color.parseColor("#E91E63"));
                Img_sao_canhan.setImageResource(R.drawable.icon_sao3);
            }
            else if (sovang > 300) {
                Txt_dangcap_canhan.setText("Giàu có");
                Txt_dangcap_canhan.setTextColor(Color.parseColor("#EB08FB"));
                Img_sao_canhan.setImageResource(R.drawable.icon_sao2);
            }
            else if (sovang > 100) {
                Txt_dangcap_canhan.setText("Khá giả");
                Txt_dangcap_canhan.setTextColor(Color.parseColor("#04B1FF"));
                Img_sao_canhan.setImageResource(R.drawable.icon_sao1);
            }
            else {
                Txt_dangcap_canhan.setText("Bình dân");
                Txt_dangcap_canhan.setTextColor(Color.parseColor("#4CAF50"));
                Img_sao_canhan.setImageResource(R.drawable.icon_sao0);
            }

            Txt_nguoithich_canhan.setText(String.valueOf(Thongtin_user.Songuoithich));
            ten_hoso.setText(Thongtin_user.Ten);
            tuoi_hoso.setText("Tuổi: " + String.valueOf(Integer.parseInt(sonam) - Thongtin_user.Namsinh));
            gioitinh_hoso.setText("Giới tính: " + Thongtin_user.Gioitinh);
            noio_hoso.setText("Nơi ở: " + Thongtin_user.Noio);
            hocvan_hoso.setText("Trình độ: " + Thongtin_user.Hocvan);
            tinhtranghonnhan_hoso.setText("Tình trạng: " + Thongtin_user.Tinhtranghonnhan);
            mucdichthamgia_hoso.setText("Mục tiêu: " + Thongtin_user.Mucdichthamgia);
            nghenghiep_hoso.setText("Nghề nghiệp: " + Thongtin_user.Nghenghiep);
            ngaythamgia_hoso.setText("Tham gia ngày " + Thongtin_user.Ngaydangky % 100 + "/" + Thongtin_user.Ngaydangky %10000/100 + "/" + Thongtin_user.Ngaydangky /10000);
            ngaydangnhap_hoso.setText("Đăng nhập lần cuối ngày " + Thongtin_user.Ngaydangxuat % 100 + "/" + Thongtin_user.Ngaydangxuat %10000/100 + "/" + Thongtin_user.Ngaydangxuat /10000);
            gioithieubanthan_hoso.setText(Thongtin_user.Gioithieubanthan);
            Picasso.get().load(Thongtin_user.Anhdaidien).into(avata_hoso);

            if (Thongtin_user.Ten.equals(getResources().getString(R.string.khachvanglai))){
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getActivity());
                alerDialogBuilder.setMessage("Bạn chưa hoàn tất thông tin trong hồ sơ?");
                alerDialogBuilder.setPositiveButton("Bổ sung ngay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), Sua_ho_so.class);
                        startActivity(intent);
                    }
                });
                alerDialogBuilder.setNegativeButton("Để sau", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();
            }

            Btn_dangxuat_canhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getActivity());
                    alerDialogBuilder.setMessage("Bạn muốn đăng xuất khỏi tài khoản này?");
                    alerDialogBuilder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                            Intent intent_tinnhan = new Intent(getActivity(), MyService.class);
                            getActivity().stopService(intent_tinnhan);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alerDialogBuilder.setNegativeButton("Không", null);
                    AlertDialog alerDialog = alerDialogBuilder.create();
                    alerDialog.show();
                }
            });

            Btn_doimatkhau.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Doi_mat_khau.class);
                    startActivity(intent);
                }
            });

            ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                // There are no request codes
                                Intent data = result.getData();
                                imageUri = data.getData();
                                InputStream imageStream = null;
                                try {
                                    imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                selectedImage = getResizedBitmap(selectedImage, 200);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data1 = baos.toByteArray();

                                StorageReference riversRef = storageReference.child("anhdaidien/" + USER.getUid());
                                riversRef.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> linkanh = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                        linkanh.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String linkanhtiep = uri.toString();
                                                FirebaseFirestore.getInstance().collection("USER").document(USER.getUid()).update("anhdaidien", "https://firebasestorage.googleapis.com/v0/b/banmuonhh-a42dd.appspot.com/o/logonho2.png?alt=media&token=5ec0d354-b15c-4b52-971f-b249b6ca33cc");
                                                mData.child("QUAN_LY").child("duyetanh").child(USER.getUid()).child("linkanhchoduyet").setValue(linkanhtiep);
                                                mData.child("QUAN_LY").child("duyetanh").child(USER.getUid()).child("idchuanh").setValue(USER.getUid());
                                                Toast.makeText(getActivity(), "Đổi ảnh thành công.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });

            Btn_doianhdaidien.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    someActivityResultLauncher.launch(intent);
                }
            });

            Btn_suahoso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Sua_ho_so.class);
                    startActivity(intent);
                }
            });

            Btn_tangdangcap_canhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Tang_dang_cap.class);
                    startActivity(intent);
                }
            });

            Btn_danhsachnguoithich_canhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Nguoi_cau_hon.class);
                    intent.putExtra("guiidnguoichat", USER.getUid());
                    startActivity(intent);
                }
            });

            Btn_nguoiduocauhon_canhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Nguoi_duoc_cau_hon.class);
                    startActivity(intent);
                }
            });

            Btn_bimat_canhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Bi_mat_them.class);
                    startActivity(intent);
                }
            });

        }
    }

    private void AnhXa(View view) {
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Btn_bimat_canhan = view.findViewById(R.id.Btn_bimat_canhan);
        Btn_dangxuat_canhan = view.findViewById(R.id.dangxuat_canhan);
        Btn_dangnhap_canhan = view.findViewById(R.id.Btn_dangnhap_canhan);
        Btn_dangky_canhan = view.findViewById(R.id.Btn_dangky_canhan);
        Btn_doimatkhau = view.findViewById(R.id.doimatkhau_canhan);
        Btn_doianhdaidien = view.findViewById(R.id.doianhdaidien_canhan);
        Btn_suahoso = view.findViewById(R.id.suahoso_canhan);
        Btn_tangdangcap_canhan = view.findViewById(R.id.tangdangcap_canhan);
        Btn_danhsachnguoithich_canhan = view.findViewById(R.id.Btn_danhsachnguoithich_canhan);
        Btn_nguoiduocauhon_canhan = view.findViewById(R.id.Btn_nguoiduocauhon_canhan);

        ten_hoso = view.findViewById(R.id.ten_canhan);
        tuoi_hoso = view.findViewById(R.id.tuoi_canhan);
        gioitinh_hoso = view.findViewById(R.id.gioitinh_canhan);
        hocvan_hoso = view.findViewById(R.id.hocvan_canhan);
        noio_hoso = view.findViewById(R.id.noio_canhan);
        tinhtranghonnhan_hoso = view.findViewById(R.id.tinhtranghonnhan_canhan);
        mucdichthamgia_hoso = view.findViewById(R.id.mucdich_canhan);
        nghenghiep_hoso = view.findViewById(R.id.nghenghiep_canhan);
        ngaythamgia_hoso = view.findViewById(R.id.ngaythamgia_canhan);
        ngaydangnhap_hoso = view.findViewById(R.id.ngaydangnhap_canhan);
        gioithieubanthan_hoso = view.findViewById(R.id.gioithieubanthan_canhan);
        phanhoso = view.findViewById(R.id.Layout_canhan);
        avata_hoso = view.findViewById(R.id.avata_canhan);
        LinearLayuot_denghidangnhap = view.findViewById(R.id.LinearLayuot_denghidangnhap);
        Txt_nguoithich_canhan = view.findViewById(R.id.Txt_nguoithich_canhan);
        Txt_dangcap_canhan = view.findViewById(R.id.Txt_dangcap_canhan);
        Txt_nguoidathich_canhan = view.findViewById(R.id.Txt_nguoidathich_canhan);
        Img_sao_canhan = view.findViewById(R.id.Img_sao_canhan);

        Txt_sotien_dangcap_canhan = view.findViewById(R.id.Txt_sotien_dangcap_canhan);
        sovang = Thongtin_user.Vang;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
