package com.henho.banmuonhenho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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

import com.henho.banmuonhenho.Adapter.Hengap_adapter;
import com.henho.banmuonhenho.Model.Model_hengap;
import com.henho.banmuonhenho.Model.Model_hengap_danhsach;
import com.henho.banmuonhenho.Model.Model_nguoinhanloi;
import com.henho.banmuonhenho.Model.OnItemClickListener_hengap;

import java.util.ArrayList;
import java.util.Collections;

public class Hen_gap extends AppCompatActivity implements OnItemClickListener_hengap {

    Button Btn_taocuochen_hengap, Btn_danhsachnhanloi_hengap;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    RecyclerView Recyc_danhsachhen_hengap;
    ArrayList<Model_hengap_danhsach> danhsachnguoihen;
    Hengap_adapter Adapter;
    TextView Txt_songuoinhanloi_hengap, Txt_loichao_hengap, Txt_taocuochen_hengap, Txt_diadiem_hengap, Txt_nguoitratien_hengap, Txt_sotien_hengap, Txt_mucdich_hegap, Txt_hengi_hengap;
    ConstraintLayout Constran_phantrenphai_henhap;
    Button Btn_quaylai_hengap, Btn_xoacuochen_hengap;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hen_gap);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        Btn_taocuochen_hengap = findViewById(R.id.Btn_taocuochen_hengap);
        Btn_danhsachnhanloi_hengap = findViewById(R.id.Btn_danhsachnhanloi_hengap);
        Txt_hengi_hengap = findViewById(R.id.Txt_hengi_hengap);
        Txt_mucdich_hegap = findViewById(R.id.Txt_mucdich_hegap);
        Txt_sotien_hengap = findViewById(R.id.Txt_sotien_hengap);
        Txt_nguoitratien_hengap = findViewById(R.id.Txt_nguoitratien_hengap);
        Txt_diadiem_hengap = findViewById(R.id.Txt_diadiem_hengap);
        Txt_taocuochen_hengap = findViewById(R.id.Txt_taocuochen_hengap);
        Txt_loichao_hengap = findViewById(R.id.Txt_loichao_hengap);
        Txt_songuoinhanloi_hengap = findViewById(R.id.Txt_songuoinhanloi_hengap);
        Constran_phantrenphai_henhap = findViewById(R.id.Constran_phantrenphai_henhap);
        Recyc_danhsachhen_hengap = findViewById(R.id.Recyc_danhsachhen_hengap);
        Btn_quaylai_hengap = findViewById(R.id.Btn_quaylai_hengap);
        Btn_xoacuochen_hengap = findViewById(R.id.Btn_xoacuochen_hengap);
        danhsachnguoihen = new ArrayList<>();

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
        Recyc_danhsachhen_hengap.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        Recyc_danhsachhen_hengap.setLayoutManager(gridLayoutManager);
        Adapter = new Hengap_adapter(this, danhsachnguoihen,this);
        Recyc_danhsachhen_hengap.setAdapter(Adapter);
        // Hiển thị danh sách xong

        // Đọc danh sách trên mạng
        Query myTopPostsQuery = mData.child("HEN_GAP").orderByChild("ngaymoi").limitToLast(100);
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsachnguoihen.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_hengap_danhsach trunggian = postSnapshot.getValue(Model_hengap_danhsach.class);
                    if(trunggian.getHengi() != null && trunggian.getId() != null){
                        danhsachnguoihen.add(trunggian);
//                        Adapter.notifyDataSetChanged();
                    }
                }
                Collections.reverse(danhsachnguoihen);
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Đọc danh sách trên mạng xong

        // Lấy thông tin người hẹn
        if(USER == null){
            Txt_loichao_hengap.setVisibility(View.VISIBLE);
            Constran_phantrenphai_henhap.setVisibility(View.INVISIBLE);
            Txt_taocuochen_hengap.setText("TẠO CUỘC HẸN");
        }else {
            mData.child("HEN_GAP").child(USER.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Model_hengap trungchuyen = snapshot.getValue(Model_hengap.class);
                    if(trungchuyen != null) {
                        Txt_hengi_hengap.setText(trungchuyen.getHengi());
                        Txt_mucdich_hegap.setText(trungchuyen.getMucdich());
                        Txt_sotien_hengap.setText(trungchuyen.getSotien());
                        Txt_nguoitratien_hengap.setText(trungchuyen.getNguoitratien());
                        Txt_diadiem_hengap.setText(trungchuyen.getDiadiem());
                        Txt_loichao_hengap.setVisibility(View.INVISIBLE);
                        Constran_phantrenphai_henhap.setVisibility(View.VISIBLE);
                        Txt_taocuochen_hengap.setText("SỬA CUỘC HẸN");
                    }else {
                        Txt_loichao_hengap.setVisibility(View.VISIBLE);
                        Constran_phantrenphai_henhap.setVisibility(View.INVISIBLE);
                        Txt_taocuochen_hengap.setText("TẠO CUỘC HẸN");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // Đếm số người nhận lời
            mData.child("NHAN_LOI").child(USER.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Txt_songuoinhanloi_hengap.setText(String.valueOf(snapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            // Đếm số người nhận lời xong
        }

        // Lấy thông tin người hẹn xong

        Btn_taocuochen_hengap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(USER == null){
                    AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Hen_gap.this);
                    alerDialogBuilder.setMessage("Bạn không thể tạo cuộc hẹn do chưa đăng nhập.\nBạn có muốn đăng nhập không");
                    alerDialogBuilder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(),Dang_nhap.class);
                            startActivity(intent);
                        }
                    });
                    alerDialogBuilder.setNegativeButton("Không", null);
                    AlertDialog alerDialog = alerDialogBuilder.create();
                    alerDialog.show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), Tao_cuoc_hen.class);
                    startActivity(intent);
                }
            }
        });

        Btn_danhsachnhanloi_hengap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Txt_songuoinhanloi_hengap.getText().equals("0")){
                    Toast.makeText(Hen_gap.this, "Chưa có ai nhận lời mời của bạn", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), Danh_sach_nguoi_nhan_loi.class);
                    startActivity(intent);
                }
            }
        });

        Btn_xoacuochen_hengap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Hen_gap.this);
                alerDialogBuilder.setMessage("Bạn muốn xóa cuộc hen?");
                alerDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mData.child("HEN_GAP").child(USER.getUid()).removeValue();
                        mData.child("NHAN_LOI").child(USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snap1 : snapshot.getChildren()){
                                    Model_nguoinhanloi layve = snap1.getValue(Model_nguoinhanloi.class);
                                    if(layve.getId() != null && layve.getLoinhan() != null) {
                                        mData.child("NHAN_LOI").child(USER.getUid()).child(layve.getId()).child("id").removeValue();
                                        mData.child("NHAN_LOI").child(USER.getUid()).child(layve.getId()).child("loinhan").removeValue();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });
                alerDialogBuilder.setNegativeButton("Không", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();
            }
        });

        Btn_quaylai_hengap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        ViewGroup rootView = findViewById(R.id.Banner_hengap);
        rootView.addView(adView);
        adView.loadAd();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(String itemclick) {
        chuyenmanhinh(itemclick);
    }

    private void chuyenmanhinh(String itemclick) {
        if(USER == null){
            Intent intent = new Intent(getApplicationContext(), Ho_so_hen_gap.class);
            intent.putExtra("guiidnguoihen", itemclick);
            startActivity(intent);
        }else {
            if(!USER.getUid().equals(itemclick)) {
                Intent intent = new Intent(getApplicationContext(), Ho_so_hen_gap.class);
                intent.putExtra("guiidnguoihen", itemclick);
                startActivity(intent);
            }
        }
    }
}