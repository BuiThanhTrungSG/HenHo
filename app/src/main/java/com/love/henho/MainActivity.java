package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.love.henho.Adapter.Fragment_adapter;
import com.love.henho.Model.Model_thanhvien;
import com.love.henho.Model.Model_tinnhanmoi;
import com.love.henho.Model.Thongtin_user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements MaxAdListener {

    BottomNavigationView BottomNavigationView_trangchu;
    ViewPager2 viewPager;
    FirebaseAuth mAuth;
    FirebaseUser User;
    FirebaseFirestore db;
    DatabaseReference mData;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Anh_xa_trangchu();

//        createInterstitialAd();

        if( getIntent().getBooleanExtra("Exit me", false)){finishAffinity();}

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        if (User == null){
            db.collection("USER").document(User.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        Model_thanhvien thanhvien = documentSnapshot.toObject(Model_thanhvien.class);
                        Thongtin_user.ID = thanhvien.getID();
                        Thongtin_user.Ten = thanhvien.getTen();
                        Thongtin_user.Namsinh = thanhvien.getNamsinh();
                        Thongtin_user.Gioitinh = thanhvien.getGioitinh();
                        Thongtin_user.Noio = thanhvien.getNoio();
                        Thongtin_user.Tinhtranghonnhan = thanhvien.getTinhtranghonnhan();
                        Thongtin_user.Hocvan = thanhvien.getHocvan();
                        Thongtin_user.Gioithieubanthan = thanhvien.getGioithieubanthan();
                        Thongtin_user.Mucdichthamgia = thanhvien.getMucdichthamgia();
                        Thongtin_user.Nghenghiep = thanhvien.getNghenghiep();
                        Thongtin_user.Ngaydangky = thanhvien.getNgaydangky();
                        Thongtin_user.Ngaydangxuat = thanhvien.getNgaydangxuat();
                        Thongtin_user.Anhdaidien = thanhvien.getAnhdaidien();
                        Thongtin_user.Songuoithich = thanhvien.getSonguoithich();
                        Thongtin_user.Email = thanhvien.getEmail();
                        Thongtin_user.Vang = thanhvien.getVang();
                        Thongtin_user.Locdanhsach = thanhvien.getLocdanhsach();
                    }else {
                        DateFormat dinhdangngaythangnam = new SimpleDateFormat("yyyyMMdd");
                        Integer songaythangnam = Integer.parseInt(dinhdangngaythangnam.format(Calendar.getInstance().getTime()));
                        Model_thanhvien trunggian = new Model_thanhvien(User.getUid(), getResources().getString(R.string.khachvanglai), 1980, "Gay", "An Giang", "Ở góa", "Hết cấp 1", "Người này chưa viết gì.", "Tìm bạn bè mới", "Tự do", songaythangnam, songaythangnam, "https://firebasestorage.googleapis.com/v0/b/banmuonhh-a42dd.appspot.com/o/logonho.png?alt=media&token=3fcf15c8-2402-4edf-8102-7a2a0347eb6b", 0, User.getEmail(), 0, "Không chọn");
                        FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).set(trunggian);
                    }
                }
            });

        }

        // tạo menu
        BottomNavigationView_trangchu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.menu_tinhan:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.menu_canhan:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return true;
        });

        xuly_viewpager();
         // tạo menu xong

// Kiểm tra xem quay lại từ trang tin nhắn thì hiện fragment tin nhắn luôn
        Intent intent2 = getIntent();
        Boolean kiemtra1 = intent2.getBooleanExtra("sangtrang2", false);
        Boolean kiemtra2 = intent2.getBooleanExtra("sangtrang3", false);
        Boolean kiemtra3 = intent2.getBooleanExtra("sangtrang1", false);
        if(kiemtra1){
            viewPager.setCurrentItem(1);
        }
        if(kiemtra2){
            viewPager.setCurrentItem(2);
        }
        if(kiemtra3){
            viewPager.setCurrentItem(0);
        }
// Kiểm tra xem quay lại từ trang tin nhắn thì hiện fragment tin nhắn luôn xong


    }

    private void Anh_xa_trangchu () {
        BottomNavigationView_trangchu = findViewById(R.id.BottomNavigationView_trangchu);
        viewPager = findViewById(R.id.viewpager);
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
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'
        // Reset retry attempt
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
    public void onAdClicked(final MaxAd maxAd) {
        Thoat();
    }

    @Override
    public void onAdHidden(final MaxAd maxAd)
    {
        Thoat();
    }

    @SuppressLint("RestrictedApi")
    private void xuly_viewpager () {
        Fragment_adapter fragment_adapter = new Fragment_adapter(this);

        viewPager.setAdapter(fragment_adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch (position) {
                    case 0:
                        BottomNavigationView_trangchu.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        BottomNavigationView_trangchu.getMenu().findItem(R.id.menu_tinhan).setChecked(true);
                        break;
                    case 2:
                        BottomNavigationView_trangchu.getMenu().findItem(R.id.menu_canhan).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        // Tạo class chạy ngầm để thông báo tin nhắn
        if(User != null) {
            Intent intent_nhantin = new Intent(getApplicationContext(), MyService.class);
            startService(intent_nhantin);

            //đếm số tin nhắn mới
            mData.child("USERS").child(User.getUid()).child("cacphongchat").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int sotinnhan = 0;
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Model_tinnhanmoi trunggian = postSnapshot.getValue(Model_tinnhanmoi.class);
                        if(trunggian != null) {
                            if (trunggian.getTrangthai() != null) {
                                if (trunggian.getTrangthai()) {
                                    sotinnhan = sotinnhan + 1;
                                }
                            }
                        }
                    }
                    if(sotinnhan > 0){
                        BottomNavigationView_trangchu.getOrCreateBadge(R.id.menu_tinhan).setNumber(sotinnhan);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            //đếm số tin nhắn mới xong


        }else {
            Intent intent_tinnhan = new Intent(getApplicationContext(), MyService.class);
            stopService(intent_tinnhan);
        }
        // Tạo class chạy ngầm để thông báo tin nhắn xong

    }

    @Override
    public void onBackPressed() {
        if(BottomNavigationView_trangchu.getMenu().findItem(R.id.menu_tinhan).isChecked()||BottomNavigationView_trangchu.getMenu().findItem(R.id.menu_canhan).isChecked()){
            viewPager.setCurrentItem(0);
        }else {
            AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alerDialogBuilder.setMessage("Bạn muốn thoát khỏi ứng dụng?");
            alerDialogBuilder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if ( interstitialAd.isReady() )
                    {
                        interstitialAd.showAd();
                    }else {
                        Thoat();
                    }
                }
            });
            alerDialogBuilder.setNegativeButton("Ở lại", null);
            AlertDialog alerDialog = alerDialogBuilder.create();
            alerDialog.show();
        }

    }
    private void Thoat() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);
        finish();
    }

}