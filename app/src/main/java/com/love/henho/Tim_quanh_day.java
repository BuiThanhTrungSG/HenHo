package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.love.henho.Adapter.Timquanhday_adapter;
import com.love.henho.Model.Model_timquanhday;
import com.love.henho.Model.Model_timquanhday_upload;
import com.love.henho.Model.OnItemClickListener_timquanhday;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Tim_quanh_day extends AppCompatActivity implements OnItemClickListener_timquanhday {

    TextView Txt_vitri;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    String userAddress;
    RecyclerView Recy_dsquanhday_quanhday;
    ArrayList<Model_timquanhday> Danhsachquanhday;
    Timquanhday_adapter Adapter;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    Button Btn_quaylai_quanhday;
    LocationManager locationManager;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_quanh_day);

        Txt_vitri = findViewById(R.id.Txt_vitri);
        Recy_dsquanhday_quanhday = findViewById(R.id.Recy_dsquanhday_quanhday);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        Danhsachquanhday = new ArrayList<>();
        Btn_quaylai_quanhday = findViewById(R.id.Btn_quaylai_quanhday);
// Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );

        // Lấy địa chỉ

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else {
            latitude = 10.4788;
            longitude = 106.9962;
        }

        Txt_vitri.setText("Không xác định được vị trí hiện tại của bạn. Bạn cần mở GPS/Google Map để có vị trí chính xác hơn");

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            userAddress = addresses.get(0).getAddressLine(0);

            if(latitude == 10.4788 && longitude == 106.9962) {
                Txt_vitri.setText("Không xác định được vị trí hiện tại của bạn, nên vị trí ngẫu nhiên là: " + userAddress + "\nCần mở GPS/Google Map để có vị trí chính xác hơn");
            }else {
                Txt_vitri.setText(userAddress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
// Lấy địa chỉ xong
// Kiểm tra đã đăng nhập chưa và gửi tọa độ
        if (USER != null && latitude != 10.4788 && longitude != 106.9962) {
            // Lấy thời điểm hiện tại
            DateFormat dinhdang = new SimpleDateFormat("yyyyMMdd");
            String thoidiem = dinhdang.format(Calendar.getInstance().getTime());
            // Lấy thời điểm hiện tại xong
            mData.child("TIM_QUANH_DAY").child(USER.getUid()).child("id").setValue(USER.getUid());
            mData.child("TIM_QUANH_DAY").child(USER.getUid()).child("latitude").setValue(latitude);
            mData.child("TIM_QUANH_DAY").child(USER.getUid()).child("longitude").setValue(longitude);
            mData.child("TIM_QUANH_DAY").child(USER.getUid()).child("ngay").setValue(thoidiem);
            Model_timquanhday_upload trunggian = new Model_timquanhday_upload(USER.getUid(), latitude, longitude, thoidiem);
            FirebaseFirestore.getInstance().collection("TIM_QUANH_DAY").document(USER.getUid()).set(trunggian);
        }
// Kiểm tra đăng nhập và gửi tọa độ xong

        // Hiển thị danh sách
        Recy_dsquanhday_quanhday.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false);
        Recy_dsquanhday_quanhday.setLayoutManager(gridLayoutManager);
        Adapter = new Timquanhday_adapter(this, Danhsachquanhday,this);
        Recy_dsquanhday_quanhday.setAdapter(Adapter);
        // Hiển thị danh sách xong

        // Đọc danh sách trên mạng
        mData.child("TIM_QUANH_DAY").orderByChild("ngay").limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    Model_timquanhday_upload trungchuyen = snap.getValue(Model_timquanhday_upload.class);
                    if(trungchuyen.getId() != null && trungchuyen.getNgay() != null) {
                        float[] results = new float[1];
                        final_loc.distanceBetween(latitude, longitude, trungchuyen.getLatitude(), trungchuyen.getLongitude(), results);
                        float e = results[0] * 0.00621371192f;
                        float a = Math.round(e);

                        Danhsachquanhday.add(new Model_timquanhday(trungchuyen.getId(), String.valueOf(a)));

                        Collections.sort(Danhsachquanhday, new Comparator<Model_timquanhday>() {
                            @Override
                            public int compare(Model_timquanhday o1, Model_timquanhday o2) {
                                if (o1 == null || o2 == null || o1.getKhoangcach() == null || o2.getKhoangcach() == null) {
                                    return 0;
                                }
                                return Float.compare(Float.parseFloat(o1.getKhoangcach()), Float.parseFloat(o2.getKhoangcach()));
                            }
                        });
                    }
                }
                Adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Đọc danh sách trên mạng xong

        Btn_quaylai_quanhday.setOnClickListener(new View.OnClickListener() {
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
        ViewGroup rootView = findViewById(R.id.Banner_quanhday);
        rootView.addView(adView);
        adView.loadAd();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Để tìm kiếm quanh đây, bạn cần bât chức năng VỊ TRÍ - GPS")
                .setCancelable(false)
                .setPositiveButton("Bật", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Không bật", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(String itemclick) {
        chuyenmanhinh(itemclick);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void chuyenmanhinh(String itemclick) {
        Intent intent = new Intent(getApplicationContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", itemclick);
        intent.putExtra("thongbaoquaylai", "trangtimquanhday");
        startActivity(intent);
    }
}