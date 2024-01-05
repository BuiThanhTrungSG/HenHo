package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.love.henho.Adapter.Phongchat_adapter;
import com.love.henho.Model.Model_phongchat;
import com.love.henho.Model.OnItemClickListener_phongchat;
import com.vanniktech.emoji.EmojiPopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Phong_chat_tinh extends AppCompatActivity implements OnItemClickListener_phongchat, MaxAdListener {

    FirebaseAuth mAuth;
    FirebaseUser User;
    DatabaseReference mData;
    RecyclerView Recy_noidungchat_phongchattinh;
    EditText Edt_khungsoanthao_phongchattinh;
    TextView Txt_tieude_phongchattinh;
    ImageView Btn_gui_phongchattinh, Img_emoji_phongchattinh;
    Button Btn_vaochat_phongchattinh, Btn_quaylai_phongchattinh;
    ConstraintLayout Constran_chontinh_phongchattinh;
    Phongchat_adapter Adapter_phongchattinh;
    ArrayList<Model_phongchat> danhsachchattinh;
    String ten_tinh, dangcap_tinh, linkanh_tinh;
    ArrayList<String> Xoabottinnhan_tinh;
    Spinner Spin_chontinh;
    SharedPreferences mLay;
    SharedPreferences sp;
    SharedPreferences.Editor spLuu;
    Boolean doiiconemoji;
    private MaxAdView adView;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    int solan_hienthi_quangcao;
    SharedPreferences Pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong_chat_tinh);

        Anh_xa();
// Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );
        createInterstitialAd();

        // Tạo emoji
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.tongthe_phongchattinh)).build(Edt_khungsoanthao_phongchattinh);
        Img_emoji_phongchattinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.toggle();
                if(doiiconemoji){
                    Img_emoji_phongchattinh.setImageResource(R.drawable.icon_banphim);
                    doiiconemoji = false;
                }else {
                    Img_emoji_phongchattinh.setImageResource(R.drawable.icon_emoji);
                    doiiconemoji = true;
                }
            }
        });

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

        ArrayAdapter<String> Adapter_noio = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, dulieu_noio);
        Spin_chontinh.setAdapter(Adapter_noio);
        Spin_chontinh.setSelection(mLay.getInt("chontinh", 0));
// Tạo danh sách nơi ở xong

// Hiển thị danh sách tin nhắn
        Constran_chontinh_phongchattinh.setVisibility(View.VISIBLE);
        Recy_noidungchat_phongchattinh.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        Recy_noidungchat_phongchattinh.setLayoutManager(linearLayoutManager);
        Adapter_phongchattinh = new Phongchat_adapter(getApplicationContext(), danhsachchattinh, this);
        Recy_noidungchat_phongchattinh.setAdapter(Adapter_phongchattinh);
// Hiển thị danh sách tin nhắn xong

// Lấy thông tin người dùng

        if(User != null){

            mData.child("USERS").child(User.getUid()).child("ten").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        ten_tinh = snapshot.getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(User.getUid()).child("anhdaidien").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        linkanh_tinh = snapshot.getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(User.getUid()).child("dangcap").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        dangcap_tinh = snapshot.getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

// Lấy thông tin người dùng xong

        Btn_gui_phongchattinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noidung = Edt_khungsoanthao_phongchattinh.getText().toString();
                if(noidung.length() == 0){

                }else {
                    if(User == null){
                        Toast.makeText(getApplicationContext(), "Bạn chưa đăng nhập nên chưa thể tham gia trò chuyện", Toast.LENGTH_LONG).show();
                        Edt_khungsoanthao_phongchattinh.setText("");
                    }else {
                        String key = mData.child("PHONG_CHAT_CHUNG").push().getKey();
                        mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(key).child("ten").setValue(ten_tinh);
                        mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(key).child("dangcap").setValue(dangcap_tinh);
                        mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(key).child("noidung").setValue(noidung);
                        mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(key).child("linkanhdaidien").setValue(linkanh_tinh);
                        mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(key).child("id").setValue(User.getUid());
                        Edt_khungsoanthao_phongchattinh.setText("");

                        if(Xoabottinnhan_tinh.size() >= 35 ){
                            mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(Xoabottinnhan_tinh.get(0)).removeValue();
                            mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(Xoabottinnhan_tinh.get(1)).removeValue();
                            mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(Xoabottinnhan_tinh.get(2)).removeValue();
                            mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(Xoabottinnhan_tinh.get(3)).removeValue();
                            mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).child(Xoabottinnhan_tinh.get(4)).removeValue();
                        }

                    }
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
                Pre.edit().putInt("solan_hienthi_tinnhan",solan_hienthi_quangcao);
                Pre.edit().apply();
            }
        });

        Btn_vaochat_phongchattinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spLuu.remove("chontinh");
                spLuu.putInt("chontinh", (int) Spin_chontinh.getSelectedItemId());
                spLuu.apply();
                spLuu.commit();
                laydanhsachchat();
                Txt_tieude_phongchattinh.setText("Phòng chát " + Spin_chontinh.getSelectedItem().toString());
                Constran_chontinh_phongchattinh.setVisibility(View.INVISIBLE);
            }
        });

        Txt_tieude_phongchattinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constran_chontinh_phongchattinh.setVisibility(View.VISIBLE);

            }
        });

        Btn_quaylai_phongchattinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void Anh_xa() {
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        Recy_noidungchat_phongchattinh = findViewById(R.id.Recy_noidungchat_phongchattinh);
        Edt_khungsoanthao_phongchattinh = findViewById(R.id.Edt_khungsoanthao_phongchattinh);
        Btn_quaylai_phongchattinh = findViewById(R.id.Btn_quaylai_phongchattinh);
        Btn_gui_phongchattinh = findViewById(R.id.Btn_gui_phongchattinh);
        Btn_vaochat_phongchattinh = findViewById(R.id.Btn_vaochat_phongchattinh);
        Txt_tieude_phongchattinh = findViewById(R.id.Txt_tieude_phongchattinh);
        Img_emoji_phongchattinh = findViewById(R.id.Img_emoji_phongchattinh);
        Constran_chontinh_phongchattinh = findViewById(R.id.Constran_chontinh_phongchattinh);
        danhsachchattinh = new ArrayList<>();
        Xoabottinnhan_tinh = new ArrayList<>();
        Spin_chontinh = findViewById(R.id.Spin_chontinh_phongchattinh);
        doiiconemoji = true;
        mLay = PreferenceManager.getDefaultSharedPreferences(getApplication());
        sp = PreferenceManager.getDefaultSharedPreferences(getApplication());
        spLuu = sp.edit();
        Pre = PreferenceManager.getDefaultSharedPreferences(getApplication());
        solan_hienthi_quangcao = Pre.getInt("solan_hienthi_tinnhan", 0);
    }

    void createBannerAd() {
        adView = new MaxAdView( getResources().getString(R.string.id_banner), this );
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( this ).getHeight();
        int heightPx = AppLovinSdkUtils.dpToPx( this, heightDp );
        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
        adView.setExtraParameter( "adaptive_banner", "true" );
        ViewGroup rootView = findViewById(R.id.Banner_phongchattinh);
        rootView.addView(adView);
        adView.loadAd();
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

    private void laydanhsachchat() {
        // Lấy danh sách chát
        mData.child("PHONG_CHAT_TINH").child(Spin_chontinh.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsachchattinh.clear();
                Xoabottinnhan_tinh.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_phongchat trunggian = postSnapshot.getValue(Model_phongchat.class);
                    if(trunggian.getTen() != null && trunggian.getDangcap()!= null && trunggian.getNoidung()!= null && trunggian.getLinkanhdaidien()!= null && trunggian.getId()!= null) {
                        danhsachchattinh.add(new Model_phongchat(trunggian.getTen(), trunggian.getDangcap(), trunggian.getNoidung(), trunggian.getLinkanhdaidien(), trunggian.getId()));
                        Xoabottinnhan_tinh.add(postSnapshot.getKey());
                        Adapter_phongchattinh.notifyDataSetChanged();
                        Recy_noidungchat_phongchattinh.smoothScrollToPosition(danhsachchattinh.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
// lấy danh sách chát xong
    }

    @Override
    public void onItemClick(Model_phongchat itemclick) {
        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", itemclick.getId());
        intent.putExtra("thongbaoquaylai", "phongchattinh");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}