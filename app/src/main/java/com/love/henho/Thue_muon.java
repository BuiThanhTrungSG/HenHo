package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.love.henho.Model.Model_thuemuon;
import com.love.henho.Model.Model_thuemuon_danhsach;
import com.love.henho.Adapter.Thuemuon_adapter;
import com.love.henho.Model.OnItemClickListener_trangchu;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Thue_muon extends AppCompatActivity implements OnItemClickListener_trangchu {

    FirebaseAuth mAuth;
    FirebaseUser User;
    DatabaseReference mData;
    RecyclerView Recy_danhsach_thuemuon;
    Button Btn_xemhoso_thuemuon, Btn_quaylai_thuemuon, Btn_themtinthue_thuemuon, Btn_xoatinthue_thuemuon;
    TextView Txt_themtin_thuemuon;
    String ngayientai;
    ArrayList<Model_thuemuon_danhsach> Danhsach;
    TextView Txt_ten_thuemuon, Txt_canthue_thuemuon, Txt_de_thuemuon, Txt_tai_thuemuon, Txt_gia_thuemuon, Txt_yeucau_thuemuon;
    ImageView Img_hinh_thuemuon;
    Thuemuon_adapter Adapter;
    String ID_nguoiduocchon;
    LinearLayout Linear_thongtin_thuemuon;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thue_muon);

        Anhxa();

        DateFormat dinhdangngaythangnam = new SimpleDateFormat("yyyyMMdd");
        ngayientai = dinhdangngaythangnam.format(Calendar.getInstance().getTime()).toString();
// Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );

// Hiển thị danh sách
        Danhsach = new ArrayList<>();
        Recy_danhsach_thuemuon.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        Recy_danhsach_thuemuon.setLayoutManager(gridLayoutManager);
        Adapter = new Thuemuon_adapter(Danhsach, getApplicationContext(), this);
        Recy_danhsach_thuemuon.setAdapter(Adapter);
// Hiển thị danh sách xong

// Đọc danh sách trên mạng

        Query myTopPostsQuery = mData.child("THUE_MUON").orderByChild("ngay").limitToLast(100);
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Danhsach.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_thuemuon_danhsach trunggian = postSnapshot.getValue(Model_thuemuon_danhsach.class);
                    if(trunggian.getCanthue() != null && trunggian.getId() != null){
                        Danhsach.add(trunggian);
                    }
                }
                Collections.reverse(Danhsach);
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(User != null){
            mData.child("THUE_MUON").child(User.getUid()).child("id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() == null){
                        Btn_themtinthue_thuemuon.setVisibility(View.VISIBLE);
                        Btn_xoatinthue_thuemuon.setVisibility(View.GONE);
                        Txt_themtin_thuemuon.setText("Tạo tin thuê mướn");

                    }else {
                        Btn_themtinthue_thuemuon.setVisibility(View.GONE);
                        Btn_xoatinthue_thuemuon.setVisibility(View.VISIBLE);
                        Txt_themtin_thuemuon.setText("Xóa tin thuê mướn");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
//        mData.child("THUE_MUON").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Danhsach.clear();
//                Btn_themtinthue_thuemuon.setVisibility(View.VISIBLE);
//                Btn_xoatinthue_thuemuon.setVisibility(View.INVISIBLE);
//                for(DataSnapshot snap : snapshot.getChildren()){
//                    Model_thuemuon trunggian = snap.getValue(Model_thuemuon.class);
//                    if(trunggian.getNgay() != null && trunggian.getCanthue() != null && trunggian.getDe() != null && trunggian.getGia() != null && trunggian.getId() != null && trunggian.getTai() != null && trunggian.getYeucau() != null) {
//                        Danhsach.add(trunggian);
//                        Collections.sort(Danhsach, new Comparator<Model_thuemuon>() {
//                            @Override
//                            public int compare(Model_thuemuon o1, Model_thuemuon o2) {
//                                return Integer.compare(Integer.parseInt(o2.getNgay()), Integer.parseInt(o1.getNgay()));
//                            }
//                        });
//                        Adapter.notifyDataSetChanged();
//
//                        if (User != null) {
//                            if (trunggian.getId().equals(User.getUid())) {
//                                Btn_themtinthue_thuemuon.setVisibility(View.INVISIBLE);
//                                Btn_xoatinthue_thuemuon.setVisibility(View.VISIBLE);
//                                Txt_themtin_thuemuon.setText("Xóa tin thuê mướn");
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

// Đọc danh sách trên mạng xong

        Btn_themtinthue_thuemuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User == null){
                    Toast.makeText(Thue_muon.this, "Bạn cần đăng nhập trước khi tạo tin thuê mướn", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), Dang_tin_thue_muon.class);
                    startActivity(intent);
                }
            }
        });

        Btn_xoatinthue_thuemuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Thue_muon.this);
                alerDialogBuilder.setMessage("Bạn muốn xóa tin thuê mướn của bạn?");
                alerDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mData.child("THUE_MUON").child(User.getUid()).removeValue();
                    }
                });
                alerDialogBuilder.setNegativeButton("Không", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();

            }
        });

        Btn_quaylai_thuemuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Btn_xemhoso_thuemuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyenmanhinh(ID_nguoiduocchon);
                Linear_thongtin_thuemuon.setVisibility(View.GONE);
            }
        });

        Linear_thongtin_thuemuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linear_thongtin_thuemuon.setVisibility(View.GONE);
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
        ViewGroup rootView = findViewById(R.id.Banner_thuemuon);
        rootView.addView(adView);
        adView.loadAd();
    }

    private void Anhxa() {
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        Btn_quaylai_thuemuon = findViewById(R.id.Btn_quaylai_thuemuon);
        Btn_xoatinthue_thuemuon = findViewById(R.id.Btn_xoatinthue_thuemuon);
        Btn_themtinthue_thuemuon = findViewById(R.id.Btn_themtinthue_thuemuon);
        Txt_themtin_thuemuon = findViewById(R.id.Txt_themtin_thuemuon);
        Recy_danhsach_thuemuon = findViewById(R.id.Recy_danhsach_thuemuon);
        Btn_themtinthue_thuemuon.setVisibility(View.VISIBLE);
        Btn_xoatinthue_thuemuon.setVisibility(View.GONE);
        Txt_canthue_thuemuon = findViewById(R.id.Txt_canthue_thuemuon);
        Txt_de_thuemuon = findViewById(R.id.Txt_de_thuemuon);
        Txt_tai_thuemuon = findViewById(R.id.Txt_tai_thuemuon);
        Txt_gia_thuemuon = findViewById(R.id.Txt_gia_thuemuon);
        Txt_yeucau_thuemuon = findViewById(R.id.Txt_yeucau_thuemuon);
        Img_hinh_thuemuon = findViewById(R.id.Img_hinh_thuemuon);
        Txt_ten_thuemuon = findViewById(R.id.Txt_ten_thuemuon);
        Btn_xemhoso_thuemuon = findViewById(R.id.Btn_xemhoso_thuemuon);
        Linear_thongtin_thuemuon = findViewById(R.id.Linear_thongtin_thuemuon);
    }

    @Override
    public void onBackPressed() {
        if(Linear_thongtin_thuemuon.getVisibility() == View.VISIBLE){
            Linear_thongtin_thuemuon.setVisibility(View.GONE);
        }else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(String itemclick) {

        ID_nguoiduocchon = itemclick;
        Linear_thongtin_thuemuon.setVisibility(View.VISIBLE);

        mData.child("THUE_MUON").child(itemclick).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model_thuemuon trunggian = snapshot.getValue(Model_thuemuon.class);
                if(trunggian.getCanthue() != null) {
                    Txt_canthue_thuemuon.setText(trunggian.getCanthue());
                }
                if(trunggian.getDe() != null) {
                    Txt_de_thuemuon.setText("Để: " + trunggian.getDe());
                }
                if(trunggian.getTai() != null) {
                    Txt_tai_thuemuon.setText("Tại: " + trunggian.getTai());
                }
                if(trunggian.getGia() != null) {
                    Txt_gia_thuemuon.setText("Với giá: " + trunggian.getGia());
                }
                if(trunggian.getYeucau() != null) {
                    Txt_yeucau_thuemuon.setText("Cần trình độ: " + trunggian.getYeucau());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("USERS").child(itemclick).child("anhdaidien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Picasso.get().load(snapshot.getValue().toString()).into(Img_hinh_thuemuon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("USERS").child(itemclick).child("ten").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Txt_ten_thuemuon.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void chuyenmanhinh(String itemclick) {
        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", itemclick);
        intent.putExtra("thongbaoquaylai", "thuemuon");
        startActivity(intent);
    }

}