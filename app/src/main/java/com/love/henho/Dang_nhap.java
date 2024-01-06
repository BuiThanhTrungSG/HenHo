package com.love.henho;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.love.henho.Model.Model_thanhvien;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Dang_nhap extends AppCompatActivity{

    Button Btn_dangnhap, Btn_dangky, Btn_quenmatkhau;
    EditText Edt_emaildangnhap, Edt_matkhaudangnhap;
    ImageView Btn_Email_dangnhap, Btn_Google_dangnhap;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    CheckBox checkBox_laumatkhau_dangnhap, checkBox_dieukhoan_dangnhap;
    LinearLayout Linear_emailkhac_dangnhap;
    String dn_email, dn_matkhau;
    TextView Txt_noidungdieukhoan_dangnhap;
    Boolean trangthailuu;
    SharedPreferences mLay;
    SharedPreferences sp;
    SharedPreferences.Editor spLuu;
    private MaxAdView adView;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        Anh_xa();
// Quảng cáo
//        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
//        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
//            @Override
//            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
//            {
//                createBannerAd();
//            }
//        } );

// đăng nhập google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
// Chức năng lưu mật khẩu
        Edt_emaildangnhap.setText(mLay.getString("emaildangnhap", ""));
        Edt_matkhaudangnhap.setText(mLay.getString("matkhaudangnhap", ""));
        trangthailuu = mLay.getBoolean("trangthai", true);
        if(trangthailuu){
            checkBox_laumatkhau_dangnhap.setChecked(true);
        }else {
            checkBox_laumatkhau_dangnhap.setChecked(false);
        }
// Chức năng lưu mật khẩu xong
        Btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox_dieukhoan_dangnhap.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), Dang_ky.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(Dang_nhap.this, "Bạn chưa chọn ĐỒNG Ý VỚI CÁC ĐIỀU KHOẢN", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Btn_quenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Quen_mat_khau.class);
                startActivity(intent);
            }
        });
        Txt_noidungdieukhoan_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/bmhh/home"));
                startActivity(browserIntent);
            }
        });
        Btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Edt_emaildangnhap.getText().length() == 0 || Edt_matkhaudangnhap.getText().length() == 0) {

                    if (Edt_emaildangnhap.getText().length() == 0) {
                        Toast.makeText(Dang_nhap.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                    }
                    if (Edt_matkhaudangnhap.getText().length() == 0) {
                        Toast.makeText(Dang_nhap.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (checkBox_dieukhoan_dangnhap.isChecked()) {
                        dn_email = Edt_emaildangnhap.getText().toString();
                        dn_matkhau = Edt_matkhaudangnhap.getText().toString();
                        mAuth.signInWithEmailAndPassword(dn_email, dn_matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Dang_nhap_thanh_cong();

                                } else {
                                    Toast.makeText(Dang_nhap.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                }

                                if (checkBox_laumatkhau_dangnhap.isChecked()) {
                                    spLuu.remove("emaildangnhap");
                                    spLuu.remove("matkhaudangnhap");
                                    spLuu.remove("trangthai");
                                    spLuu.putString("emaildangnhap", Edt_emaildangnhap.getText().toString());
                                    spLuu.putString("matkhaudangnhap", Edt_matkhaudangnhap.getText().toString());
                                    spLuu.putBoolean("trangthai", true);
                                    spLuu.commit();
                                    spLuu.apply();
                                } else {
                                    spLuu.remove("emaildangnhap");
                                    spLuu.remove("matkhaudangnhap");
                                    spLuu.remove("trangthai");
                                    spLuu.putBoolean("trangthai", false);
                                    spLuu.commit();
                                    spLuu.apply();
                                }

                            }
                        });
                    }else {
                        Toast.makeText(Dang_nhap.this, "Bạn chưa chọn ĐỒNG Ý VỚI CÁC ĐIỀU KHOẢN", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        Btn_Email_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Linear_emailkhac_dangnhap.getVisibility() == View.GONE){
                    Linear_emailkhac_dangnhap.setVisibility(View.VISIBLE);
                }else {
                    Linear_emailkhac_dangnhap.setVisibility(View.GONE);
                }
            }
        });

        Btn_Google_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
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
        ViewGroup rootView = findViewById(R.id.Banner_dangnhap);
        rootView.addView(adView);
        adView.loadAd();
    }

    private void Anh_xa() {
        Btn_Google_dangnhap = findViewById(R.id.Btn_Google_dangnhap);
        Btn_Email_dangnhap = findViewById(R.id.Btn_Email_dangnhap);
        Linear_emailkhac_dangnhap = findViewById(R.id.Linear_emailkhac_dangnhap);
        Txt_noidungdieukhoan_dangnhap = findViewById(R.id.Txt_noidungdieukhoan_dangnhap);
        checkBox_dieukhoan_dangnhap = findViewById(R.id.checkBox_dieukhoan_dangnhap);
        Btn_dangnhap = findViewById(R.id.nutdangnhap);
        Btn_dangky = findViewById(R.id.nutdangky);
        Btn_quenmatkhau = findViewById(R.id.nutquenmatkhau);
        Edt_emaildangnhap = findViewById(R.id.email_dangnhap);
        Edt_matkhaudangnhap = findViewById(R.id.matkhau_dangnhap);
        checkBox_laumatkhau_dangnhap = findViewById(R.id.checkBox_laumatkhau_dangnhap);
        mLay = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spLuu = sp.edit();
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                Dangnhap_google_thongtin(account);
            }catch (Exception e){

            }

        }
    }

    private void Dangnhap_google_thongtin(GoogleSignInAccount account) {

        DateFormat dinhdangngaythangnam = new SimpleDateFormat("yyyyMMdd");
        Integer songaythangnam = Integer.parseInt(dinhdangngaythangnam.format(Calendar.getInstance().getTime()));
        // Lấy năm hiện tại xong
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()){

                    FirebaseUser USER = mAuth.getCurrentUser();
                    assert USER != null;
                    Model_thanhvien trunggian = new Model_thanhvien(USER.getUid(), getResources().getString(R.string.khachvanglai), 1980, "Gay", "An Giang", "Ở góa", "Hết cấp 1", "Người này chưa viết gì.", "Tìm bạn bè mới", "Tự do", songaythangnam, songaythangnam, "https://firebasestorage.googleapis.com/v0/b/banmuonhh-a42dd.appspot.com/o/logonho.png?alt=media&token=3fcf15c8-2402-4edf-8102-7a2a0347eb6b", 0, USER.getEmail(), 0, "Không chọn");
                    FirebaseFirestore.getInstance().collection("USER").document(USER.getUid()).set(trunggian);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else {
                    Dang_nhap_thanh_cong();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void Dang_nhap_thanh_cong() {
        Toast.makeText(Dang_nhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}