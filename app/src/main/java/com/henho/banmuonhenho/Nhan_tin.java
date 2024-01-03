package com.henho.banmuonhenho;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.henho.banmuonhenho.Adapter.Noidungtinnhan_adapter;
import com.henho.banmuonhenho.Model.Model_noidungtinnhan;
import com.henho.banmuonhenho.Model.Model_noidungtinnhan_less;
import com.henho.banmuonhenho.Model.Model_ten_anh_dangcap;
import com.squareup.picasso.Picasso;
import com.vanniktech.emoji.EmojiPopup;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Nhan_tin extends AppCompatActivity implements MaxAdListener {

    ImageView Btn_gui_nhantin,Btn_lock_nhantin;
    TextView Txt_tieude_nhantin, tieude_dangcap_nhantin;
    EditText Edt_noidungtinnhan_nhantin;
    RecyclerView toanbotinnhan_nhantin;
    ArrayList<Model_noidungtinnhan_less> tinnhandagui;
    LinearLayout Layout_chuadangnhap_nhantin;
    ConstraintLayout Layout_dadangnhap_nhantin, Constran_khungnhap_nhantin;
    Button Btn_quaylai_nhantin, Btn_dangnhap_nhantin, Btn_dangky_nhantin, Btn_xoaanhgui_nhantin, Btn_xoaanhnhan_nhantin;
    ImageView Btn_xemhoso_nhantin, Btn_xoa_nhantin, Img_emoji_nhantin, Img_guianh_nhantin;
    Noidungtinnhan_adapter noidungtin;
    String idnguoichat;
    ViewPager viewPager;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    FirebaseFirestore db;
    ArrayList<String> Xoabot_nhantin;
    Intent intent_nhantin;
    String thoidiem, linkanhgui, linkanhnhan;
    ImageView Img_anhnguoigui_nhantin, Img_anhnguoiguikhung_nhantin, Img_anhnguoinhan_nhantin, Img_anhnguoinhankhung_nhantin, Img_xemanhlon_nhantin;
    Boolean doiiconemoji;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    int solan_hienthi_quangcao;
    SharedPreferences Pre;
    SharedPreferences.Editor Pre_luu;
    Boolean Toi_bi_chan, Toi_chan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_tin);

        Anh_xa_nhantin();
        createInterstitialAd();
// Tạo emoji
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.tongthe_nhantin)).build(Edt_noidungtinnhan_nhantin);
        Img_emoji_nhantin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.toggle();
                if(doiiconemoji){
                    Img_emoji_nhantin.setImageResource(R.drawable.icon_banphim);
                    doiiconemoji = false;
                }else {
                    Img_emoji_nhantin.setImageResource(R.drawable.icon_emoji);
                    doiiconemoji = true;
                }
            }
        });

// Kiểm tra đã đăng nhập chưa
        if (USER == null) {
            Layout_dadangnhap_nhantin.setVisibility(View.INVISIBLE);
            Layout_chuadangnhap_nhantin.setVisibility(View.VISIBLE);
            Btn_dangnhap_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Dang_nhap.class);
                    startActivity(intent);
                }
            });
            Btn_dangky_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Dang_ky.class);
                    startActivity(intent);
                }
            });
        } else {
            Layout_chuadangnhap_nhantin.setVisibility(View.INVISIBLE);
            Layout_dadangnhap_nhantin.setVisibility(View.VISIBLE);
// Kiểm tra đăng nhập xong, chú ý còn 1 dấu ngoặc ở dưới

// lấy số hồ sơ (ID chủ hồ sơ) từ trang trước truyền qua
            Intent intent = getIntent();
            idnguoichat = intent.getStringExtra("guiidnguoichat");
// lấy số hồ sơ (ID chủ hồ sơ) từ trang trước truyền qua xong

// Tạo tiêu đề trang nhắn tin
            Source source = Source.CACHE;
            db.collection("USER").document(idnguoichat).get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Model_ten_anh_dangcap layve = documentSnapshot.toObject(Model_ten_anh_dangcap.class);
                    if(layve != null) {
                        Txt_tieude_nhantin.setText(layve.getTen());
                        tieude_dangcap_nhantin.setText(layve.getDangcap());
                        Picasso.get().load(layve.getAnhdaidien()).into(Btn_xemhoso_nhantin);
                    }
                }
            });

// Đọc các tin nhắn trước đây

            // Hiển thị danh sách
            tinnhandagui = new ArrayList<>();
            toanbotinnhan_nhantin.setHasFixedSize(true);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            toanbotinnhan_nhantin.setLayoutManager(linearLayoutManager);
            noidungtin = new Noidungtinnhan_adapter(this, tinnhandagui);
            toanbotinnhan_nhantin.setAdapter(noidungtin);
            // Hiển thị danh sách xong

            // Đọc danh sách trên mạng

            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("chat").limitToLast(40).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Model_noidungtinnhan_less trungchuyen_tn = snapshot.getValue(Model_noidungtinnhan_less.class);
                    if(trungchuyen_tn != null) {
                        tinnhandagui.add(trungchuyen_tn);
                        Xoabot_nhantin.add(snapshot.getKey());
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.cancel(2);
                    }

                    toanbotinnhan_nhantin.scrollToPosition(tinnhandagui.size() - 1);
                    noidungtin.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

// Đọc các tin nhắn trước đây xong

            // Lấy thời điểm hiện tại
            DateFormat dinhdang = new SimpleDateFormat("yyyyMMddhhmmssSS");
            thoidiem = dinhdang.format(Calendar.getInstance().getTime());
            // Lấy thời điểm hiện tại xong

            // Kiểm tra chặn
            mData.child("blacklist").child(USER.getUid()).child(idnguoichat).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null){
                        Toi_chan = true;
                    }else {
                        Toi_chan = false;
                    }
                    Thay_doi_chan_tinnhan();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toi_chan = false;
                    Thay_doi_chan_tinnhan();
                }
            });

            // Kiểm tra bị chặn

            mData.child("blacklist").child(idnguoichat).child(USER.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null){
                        Toi_bi_chan = true;
                        Toast.makeText(Nhan_tin.this, "Bạn đang bị người này CHẶN TIN NHẮN", Toast.LENGTH_SHORT).show();
                    }else {
                        Toi_bi_chan = false;
                    }
                    Thay_doi_chan_tinnhan();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toi_bi_chan = false;
                    Thay_doi_chan_tinnhan();
                }
            });

            Btn_gui_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Edt_noidungtinnhan_nhantin.getText().length() == 0) {

                    } else {

                        Model_noidungtinnhan tinnhan = new Model_noidungtinnhan(USER.getUid(), Edt_noidungtinnhan_nhantin.getText().toString(), thoidiem);
                        mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("chat").push().setValue(tinnhan);
                        mData.child("USERS").child(idnguoichat).child("cacphongchat").child(USER.getUid()).child("chat").push().setValue(tinnhan);

                        mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("idnguoichat").setValue(idnguoichat);
                        mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("thoidiem").setValue(thoidiem);
                        mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("trangthai").setValue(false);

                        mData.child("USERS").child(idnguoichat).child("cacphongchat").child(USER.getUid()).child("idnguoichat").setValue(USER.getUid());
                        mData.child("USERS").child(idnguoichat).child("cacphongchat").child(USER.getUid()).child("thoidiem").setValue(thoidiem);
                        mData.child("USERS").child(idnguoichat).child("cacphongchat").child(USER.getUid()).child("trangthai").setValue(true);

                        // Gửi thông báo tin nhắn mới
                        mData.child("USERS").child(idnguoichat).child("thongbao_tinnhan").setValue(true);

                        Edt_noidungtinnhan_nhantin.setText("");

//                        if(Xoabot_nhantin.size() >=30 ){
//                            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("chat").child(Xoabot_nhantin.get(0)).removeValue();
//                            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("chat").child(Xoabot_nhantin.get(1)).removeValue();
//                            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("chat").child(Xoabot_nhantin.get(2)).removeValue();
//                            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("chat").child(Xoabot_nhantin.get(3)).removeValue();
//                            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("chat").child(Xoabot_nhantin.get(4)).removeValue();
//
//                        }

                    }

                    if (solan_hienthi_quangcao < 15) {
                        solan_hienthi_quangcao++;
                    }else {
                        solan_hienthi_quangcao = 0;
                        if ( interstitialAd.isReady() )
                        {
                            interstitialAd.showAd();
                        }
                    }
                    Pre_luu.putInt("solan_hienthi_tinnhan",solan_hienthi_quangcao);
                    Pre_luu.apply();

                }
            });

            Btn_xoa_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Nhan_tin.this);
                    alertBuilder.setMessage("Bạn muốn xóa tin nhắn này?");
                    alertBuilder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).removeValue();
                            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                            intent1.putExtra("sangtrang2", true);
                            startActivity(intent1);
                        }
                    });
                    alertBuilder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }
            });

            Btn_xemhoso_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Ho_so.class);
                    intent.putExtra("guiidnguoichat", idnguoichat);
                    intent.putExtra("thongbaoquaylai", "trangnhantin");
                    startActivity(intent);
                }
            });

            Btn_quaylai_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (USER == null){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else {
                        if(tinnhandagui.size() == 0){
                            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent2);
                        }else {
                            Intent intent = getIntent();
                            String idnguoichat = intent.getStringExtra("guiidnguoichat");
                            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("trangthai").setValue(false);
                            Boolean kiemtraquaylaitranghoso = intent.getBooleanExtra("quaylaitranghoso", false);
                            startActivity(intent);
                            if(kiemtraquaylaitranghoso){
                                Intent intent2 = new Intent(getApplicationContext(), Ho_so.class);
                                intent2.putExtra("guiidnguoichat", idnguoichat);
                                startActivity(intent2);
                            }else {
                                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                                intent2.putExtra("sangtrang2", true);
                                startActivity(intent2);
                            }
                        }
                    }
                    // chú ý còn nút quay lại của điện thoại
                }
            });

// chức năng gửi ảnh
            Img_guianh_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chọn file ảnh
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 1);
                    // Chọn file ảnh xong
                }
            });

            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("anhgui").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null){
                        linkanhgui = snapshot.getValue().toString();
                        Img_anhnguoigui_nhantin.setVisibility(View.VISIBLE);
                        Img_anhnguoiguikhung_nhantin.setVisibility(View.VISIBLE);
                        Picasso.get().load(linkanhgui).into(Img_anhnguoigui_nhantin);
                    }else {
                        Img_anhnguoigui_nhantin.setVisibility(View.INVISIBLE);
                        Img_anhnguoiguikhung_nhantin.setVisibility(View.INVISIBLE);
                        linkanhgui = null;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(idnguoichat).child("cacphongchat").child(USER.getUid()).child("anhgui").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null){
                        linkanhnhan = snapshot.getValue().toString();
                        Img_anhnguoinhan_nhantin.setVisibility(View.VISIBLE);
                        Img_anhnguoinhankhung_nhantin.setVisibility(View.VISIBLE);
                        Picasso.get().load(linkanhnhan).into(Img_anhnguoinhan_nhantin);
                    }else {
                        Img_anhnguoinhan_nhantin.setVisibility(View.INVISIBLE);
                        Img_anhnguoinhankhung_nhantin.setVisibility(View.INVISIBLE);
                        linkanhnhan = null;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Img_anhnguoinhan_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(linkanhnhan != null){
                        Img_xemanhlon_nhantin.setVisibility(View.VISIBLE);
                        Btn_xoaanhnhan_nhantin.setVisibility(View.VISIBLE);
                        Picasso.get().load(linkanhnhan).into(Img_xemanhlon_nhantin);
                    }
                }
            });

            Img_anhnguoinhankhung_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(linkanhnhan != null){
                        Img_xemanhlon_nhantin.setVisibility(View.VISIBLE);
                        Btn_xoaanhnhan_nhantin.setVisibility(View.VISIBLE);
                        Picasso.get().load(linkanhnhan).into(Img_xemanhlon_nhantin);
                    }
                }
            });

            Img_anhnguoiguikhung_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(linkanhgui != null){
                        Img_xemanhlon_nhantin.setVisibility(View.VISIBLE);
                        Btn_xoaanhgui_nhantin.setVisibility(View.VISIBLE);
                        Picasso.get().load(linkanhgui).into(Img_xemanhlon_nhantin);
                    }
                }
            });

            Img_anhnguoigui_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(linkanhgui != null){
                        Img_xemanhlon_nhantin.setVisibility(View.VISIBLE);
                        Btn_xoaanhgui_nhantin.setVisibility(View.VISIBLE);
                        Picasso.get().load(linkanhgui).into(Img_xemanhlon_nhantin);
                    }
                }
            });

            Img_xemanhlon_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Img_xemanhlon_nhantin.setVisibility(View.INVISIBLE);
                    Btn_xoaanhgui_nhantin.setVisibility(View.INVISIBLE);
                    Btn_xoaanhnhan_nhantin.setVisibility(View.INVISIBLE);
                }
            });

            Btn_xoaanhgui_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("anhgui").removeValue();
                    Img_xemanhlon_nhantin.setVisibility(View.INVISIBLE);
                    Btn_xoaanhgui_nhantin.setVisibility(View.INVISIBLE);
                    Btn_xoaanhnhan_nhantin.setVisibility(View.INVISIBLE);
                }
            });

            Btn_xoaanhnhan_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("USERS").child(idnguoichat).child("cacphongchat").child(USER.getUid()).child("anhgui").removeValue();
                    Img_xemanhlon_nhantin.setVisibility(View.INVISIBLE);
                    Btn_xoaanhgui_nhantin.setVisibility(View.INVISIBLE);
                    Btn_xoaanhnhan_nhantin.setVisibility(View.INVISIBLE);
                }
            });

            Btn_lock_nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String Btn_tag = (String) Btn_lock_nhantin.getTag();
                    String mo_dong;
                    if (Btn_tag.equals("mo")){
                        mo_dong = "CHẶN";
                    }else {
                        mo_dong = "BỎ CHẶN";
                    }

                    AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Nhan_tin.this);
                    alerDialogBuilder.setMessage("Bạn muốn " + mo_dong + " người dùng?");
                    alerDialogBuilder.setPositiveButton(mo_dong, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (Btn_tag.equals("mo")){
                                mData.child("blacklist").child(USER.getUid()).child(idnguoichat).setValue(true);
                                Toast.makeText(Nhan_tin.this, "Bạn đã CHẶN TIN NHẮN từ người này", Toast.LENGTH_SHORT).show();
                            }else {
                                mData.child("blacklist").child(USER.getUid()).child(idnguoichat).removeValue();
                                Toast.makeText(Nhan_tin.this, "Bạn đã BỎ CHẶN TIN NHẮN từ người này", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alerDialogBuilder.setNegativeButton("KHÔNG", null);
                    AlertDialog alerDialog = alerDialogBuilder.create();
                    alerDialog.show();


                }
            });
// chức năng gửi ảnh xong
        }
    }

    private void Thay_doi_chan_tinnhan(){
        if (Toi_bi_chan || Toi_chan){
            Constran_khungnhap_nhantin.setVisibility(View.GONE);
            toanbotinnhan_nhantin.setBackgroundColor(Color.parseColor("#C6F496E1"));
        }else {
            Constran_khungnhap_nhantin.setVisibility(View.VISIBLE);
            toanbotinnhan_nhantin.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (Toi_chan){
            Btn_lock_nhantin.setTag("dong");
            Btn_lock_nhantin.setBackgroundResource(R.drawable.unlock);
        }else {
            Btn_lock_nhantin.setTag("mo");
            Btn_lock_nhantin.setBackgroundResource(R.drawable.lock);
        }
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            selectedImage = getResizedBitmap(selectedImage, 400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data1 = baos.toByteArray();
            String anh = "anhgui/" + USER.getUid() + "/" + idnguoichat;
            StorageReference riversRef = storageReference.child(anh);
            riversRef.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> linkanh = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    linkanh.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String linkanhtiep = uri.toString();
                            mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("anhgui").setValue(linkanhtiep);
                        }
                    });
                }
            });

        }
    }

    private void Anh_xa_nhantin() {

        Toi_chan = false;
        Toi_bi_chan = false;
        Constran_khungnhap_nhantin = findViewById(R.id.Constran_khungnhap_nhantin);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Btn_lock_nhantin = findViewById(R.id.Btn_lock_nhantin);
        Img_anhnguoigui_nhantin = findViewById(R.id.Img_anhnguoigui_nhantin);
        Img_anhnguoiguikhung_nhantin = findViewById(R.id.Img_anhnguoiguikhung_nhantin);
        Img_anhnguoinhan_nhantin = findViewById(R.id.Img_anhnguoinhan_nhantin);
        Img_anhnguoinhankhung_nhantin = findViewById(R.id.Img_anhnguoinhankhung_nhantin);
        Img_xemanhlon_nhantin = findViewById(R.id.Img_xemanhlon_nhantin);
        Btn_xoaanhgui_nhantin = findViewById(R.id.Btn_xoaanhgui_nhantin);
        Btn_xoaanhnhan_nhantin = findViewById(R.id.Btn_xoaanhnhan_nhantin);
        linkanhgui = null;
        linkanhnhan = null;
        Xoabot_nhantin = new ArrayList<>();
        intent_nhantin = new Intent(this, MyService.class);
        doiiconemoji = true;
        Pre = PreferenceManager.getDefaultSharedPreferences(getApplication());
        Pre_luu = Pre.edit();
        Btn_gui_nhantin = findViewById(R.id.gui_nhantin);
        Txt_tieude_nhantin = findViewById(R.id.tieude_nhantin);
        Edt_noidungtinnhan_nhantin = findViewById(R.id.noidungtinnhan_nhantin);
        toanbotinnhan_nhantin = findViewById(R.id.toanbotinnhan_nhantin);
        viewPager = findViewById(R.id.viewpager);
        Layout_dadangnhap_nhantin = findViewById(R.id.Layout_dadangnhap_nhantin);
        Layout_chuadangnhap_nhantin = findViewById(R.id.Layout_chuadangnhap_nhantin);
        Btn_dangnhap_nhantin = findViewById(R.id.Btn_dangnhap_nhantin);
        Btn_dangky_nhantin = findViewById(R.id.Btn_dangky_nhantin);
        Btn_xoa_nhantin = findViewById(R.id.Btn_xoa_nhantin);
        Btn_xemhoso_nhantin = findViewById(R.id.Img_xemhoso_nhantin);
        tieude_dangcap_nhantin = findViewById(R.id.tieude_dangcap_nhantin);
        Btn_quaylai_nhantin = findViewById(R.id.Btn_quaylai_nhantin);
        Img_emoji_nhantin = findViewById(R.id.Img_emoji_nhantin);
        Img_guianh_nhantin = findViewById(R.id.Img_guianh_nhantin);
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

    @Override
    public void onBackPressed() {
        if (USER == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else {
            if(tinnhandagui.size() == 0){
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
            }else {
                Intent intent = getIntent();
                String idnguoichat = intent.getStringExtra("guiidnguoichat");
                mData.child("USERS").child(USER.getUid()).child("cacphongchat").child(idnguoichat).child("trangthai").setValue(false);
                Boolean kiemtraquaylaitranghoso = intent.getBooleanExtra("quaylaitranghoso", false);
                startActivity(intent);
                if(kiemtraquaylaitranghoso){
                    Intent intent2 = new Intent(getApplicationContext(), Ho_so.class);
                    intent2.putExtra("guiidnguoichat", idnguoichat);
                    startActivity(intent2);
                }else {
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    intent2.putExtra("sangtrang2", true);
                    startActivity(intent2);
                }

            }
        }
        // chú ý còn nút quay lại của ứng dụng
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.cancel(2);
    }

    // Bổ sung cho chức năng chọn ảnh để upload làm Avata xong
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