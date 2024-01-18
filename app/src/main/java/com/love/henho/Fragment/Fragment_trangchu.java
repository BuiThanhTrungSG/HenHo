package com.love.henho.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.love.henho.Adapter.Thanhvienmoidangnhap_adapter;
import com.love.henho.Dang_nhap;
import com.love.henho.Hen_gap;
import com.love.henho.Ho_so;
import com.love.henho.Hop_tuoi;
import com.love.henho.Model.Model_kiemtraphienban;
import com.love.henho.Model.Model_thanhvienmoidangnhap;
import com.love.henho.Model.OnItemClickListener_trangchu;
import com.love.henho.Nguoi_la;
import com.love.henho.Phong_chat_tinh;
import com.love.henho.R;
import com.love.henho.Tang_dang_cap;
import com.love.henho.Thue_muon;
import com.love.henho.Tim_quanh_day;
import com.squareup.picasso.BuildConfig;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Fragment_trangchu extends Fragment implements OnItemClickListener_trangchu {

    RecyclerView RecyclerView_danhsachmoidangnhap_fragment_trangchu;
    ArrayList<Model_thanhvienmoidangnhap> Danhsachthanhvienmoidangnhap, Danhsachthanhvienmoidangnhap_timkiem;
    FirebaseAuth mAuth;
    FirebaseUser User;
    DatabaseReference mData;
    FirebaseFirestore db;
    Query Query_locdanhsach;
    ImageView Btn_quaylai_trangchu, Btn_tangvang_trangchu, Btn_hengap_trangchu, Btn_thuemuon_trangchu, Btn_nguoila_trangchu, Btn_timkiem_trangchu, Btn_timquanhday_trangchu, Btn_phongchat_trangchu, Btn_hoptuoi_trangchu;
    Button Btn_huytimkiemthanhvien_trangchu, Btn_timkiemthanhvien_trangchu;
    LinearLayout Layuotphu1_timkiem_trangchu, ds_menu_trangchu;
    TextView Txt_danhsachthanhvien_trangchu;
    Spinner spinner_dangcap_timkiem_trangchu, spinner_mucdichthamgia_timkiem_trangchu, spinner_gioitinh_timkiem_trangchu, spinner_noio_timkiem_trangchu, muctieu_timkiem_trangchu;
    String Loc_noio = "Không chọn";
    String Loc_gioitinh = "Không chọn";
    String Loc_mucdichthamgia = "Không chọn";
    String Loc_dangcap = "Không chọn";
    String dangcap = "locdanhsach";
    String noio = "locdanhsach";
    String mucdichthamgia  = "locdanhsach";
    String gioitinh = "locdanhsach";

    Thanhvienmoidangnhap_adapter danhsach;
    Integer Sophienban_khuyenkhich = 1;
    Integer Sophienban_chan = 1;
    SharedPreferences mLay;
    SharedPreferences sp;
    SharedPreferences.Editor spLuu;
    Integer ngaygiohientai, ngaygiotaidanhsach;
    Button Btn_dong_quangcao;
    ConstraintLayout Constran_quangcao;
    ImageView Img_anh_quangcao;
    Gson gson;
    DocumentSnapshot lastVisible = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trangchu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {

        anhXa(view);

//        kiemtraphienban();

        hienthiloichaoadmin();

        taodanhsachmuctieu();

        taodanhssachgioitinh();

        taodanhsachnoio();

        taodanhsachdangcap();

        taokhunghienthithanhvien();

// Đọc danh sách mặc định khi mở màn hình

        DateFormat dinhdangngaygio = new SimpleDateFormat("yyyyMMddHH");
        ngaygiohientai = Integer.parseInt(dinhdangngaygio.format(Calendar.getInstance().getTime()));
        ngaygiotaidanhsach = mLay.getInt("ngaygiotai", 0);

        if(ngaygiotaidanhsach.equals(ngaygiohientai)){
            LayThongTinChon();
            Docdanhsachtrongmay();
        }else {
            Docdanhsachtrenmang();
            spLuu.remove("chonmuctieu");
            spLuu.remove("chongioitinh");
            spLuu.remove("chonnoio");
            spLuu.remove("chontrinhdo");
            spLuu.remove("chontinhtranghonnhan");
            spLuu.remove("chondangcap");
            spLuu.remove("ngaygiotai");
            spLuu.putInt("ngaygiotai", ngaygiohientai);
            spLuu.apply();
            spLuu.commit();
            daylendaudanhsach();
            quangcaonoibo();
        }

        RecyclerView_danhsachmoidangnhap_fragment_trangchu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)  && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    Docdanhsachtrenmang_taithem();
                }
            }
        });
// Đọc danh sách mặc định khi mở màn hình xong

// Chức năng tìm kiếm thành viên
        Btn_timkiem_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Txt_danhsachthanhvien_trangchu.setVisibility(View.INVISIBLE);
                Btn_quaylai_trangchu.setVisibility(View.VISIBLE);
                Layuotphu1_timkiem_trangchu.setVisibility(View.VISIBLE);
                ds_menu_trangchu.setVisibility(View.INVISIBLE);
                RecyclerView_danhsachmoidangnhap_fragment_trangchu.setVisibility(View.INVISIBLE);
            }
        });

        Btn_timkiemthanhvien_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Đang tìm kiếm! Xin chờ một chút", Toast.LENGTH_LONG).show();
                Txt_danhsachthanhvien_trangchu.setVisibility(View. VISIBLE);
                Btn_quaylai_trangchu.setVisibility(View.VISIBLE);
                ds_menu_trangchu.setVisibility(View.VISIBLE);
                Layuotphu1_timkiem_trangchu.setVisibility(View. INVISIBLE);
                Btn_quaylai_trangchu.setVisibility(View.INVISIBLE);
                RecyclerView_danhsachmoidangnhap_fragment_trangchu.setVisibility(View.VISIBLE);

// Lọc theo mục tiêu
                LayThongTinChon();

                Danhsachthanhvienmoidangnhap.clear();
                Docdanhsachtrenmang();

                spLuu.remove("chonmuctieu");
                spLuu.putInt("chonmuctieu", (int) spinner_mucdichthamgia_timkiem_trangchu.getSelectedItemId());
                spLuu.remove("chongioitinh");
                spLuu.putInt("chongioitinh", (int) spinner_gioitinh_timkiem_trangchu.getSelectedItemId());
                spLuu.remove("chonnoio");
                spLuu.putInt("chonnoio", (int) spinner_noio_timkiem_trangchu.getSelectedItemId());
                spLuu.remove("chondangcap");
                spLuu.putInt("chondangcap", (int) spinner_dangcap_timkiem_trangchu.getSelectedItemId());
                spLuu.apply();
                spLuu.commit();
            }
        });
// Lọc theo mục tiêu xong

        Btn_timquanhday_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Tim_quanh_day.class);
                startActivity(intent);
            }
        });

        Btn_tangvang_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User == null) {
                    AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getActivity());
                    alerDialogBuilder.setMessage("Bạn không thể lấy vàng do chưa đăng nhập.\nBạn có muốn đăng nhập không");
                    alerDialogBuilder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), Dang_nhap.class);
                            startActivity(intent);
                        }
                    });
                    alerDialogBuilder.setNegativeButton("Không", null);
                    AlertDialog alerDialog = alerDialogBuilder.create();
                    alerDialog.show();
                }else {
                    Intent intent = new Intent(getActivity(), Tang_dang_cap.class);
                    startActivity(intent);
                }
            }
        });

        Btn_hoptuoi_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User == null) {
                    AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getActivity());
                    alerDialogBuilder.setMessage("Bạn không thể xem tuổi do chưa đăng nhập.\nBạn có muốn đăng nhập không");
                    alerDialogBuilder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), Dang_nhap.class);
                            startActivity(intent);
                        }
                    });
                    alerDialogBuilder.setNegativeButton("Không", null);
                    AlertDialog alerDialog = alerDialogBuilder.create();
                    alerDialog.show();
                }else {
                    Intent intent = new Intent(getActivity(), Hop_tuoi.class);
                    startActivity(intent);
                  }
            }
        });

        Btn_huytimkiemthanhvien_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Layuotphu1_timkiem_trangchu.setVisibility(View. INVISIBLE);
                Btn_quaylai_trangchu.setVisibility(View.INVISIBLE);
                ds_menu_trangchu.setVisibility(View.VISIBLE);
                Btn_quaylai_trangchu.setVisibility(View.INVISIBLE);
                RecyclerView_danhsachmoidangnhap_fragment_trangchu.setVisibility(View.VISIBLE);
                Docdanhsachtrenmang();
                spLuu.remove("chonmuctieu");
                spLuu.putInt("chonmuctieu", 0);
                spLuu.remove("chongioitinh");
                spLuu.putInt("chongioitinh", 0);
                spLuu.remove("chonnoio");
                spLuu.putInt("chonnoio", 0);
                spLuu.remove("chondangcap");
                spLuu.putInt("chondangcap", 0);
                spLuu.apply();
                spLuu.commit();
                spinner_mucdichthamgia_timkiem_trangchu.setSelection(mLay.getInt("chontinhtranghonnhan", 0));
                spinner_gioitinh_timkiem_trangchu.setSelection(mLay.getInt("chontinhtranghonnhan", 0));
                spinner_noio_timkiem_trangchu.setSelection(mLay.getInt("chontinhtranghonnhan", 0));
                spinner_dangcap_timkiem_trangchu.setSelection(mLay.getInt("chondangcap", 0));
            }
        });

        Btn_hengap_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Hen_gap.class);
                startActivity(intent);
            }
        });

        Btn_phongchat_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Phong_chat_tinh.class);
                startActivity(intent);
            }
        });

        Btn_nguoila_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User == null) {
                    AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getActivity());
                    alerDialogBuilder.setMessage("Bạn không thể chát với người lạ do chưa đăng nhập.\nBạn có muốn đăng nhập không");
                    alerDialogBuilder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), Dang_nhap.class);
                            startActivity(intent);
                        }
                    });
                    alerDialogBuilder.setNegativeButton("Không", null);
                    AlertDialog alerDialog = alerDialogBuilder.create();
                    alerDialog.show();
                }else {
                    Intent intent = new Intent(getContext(), Nguoi_la.class);
                    startActivity(intent);
                }
            }
        });

        Btn_thuemuon_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Thue_muon.class);
                startActivity(intent);
            }
        });

        Btn_quaylai_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Layuotphu1_timkiem_trangchu.setVisibility(View. INVISIBLE);
                Btn_quaylai_trangchu.setVisibility(View.INVISIBLE);
                ds_menu_trangchu.setVisibility(View.VISIBLE);
                Btn_quaylai_trangchu.setVisibility(View.INVISIBLE);
                RecyclerView_danhsachmoidangnhap_fragment_trangchu.setVisibility(View.VISIBLE);
            }
        });

        Btn_dong_quangcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constran_quangcao.setVisibility(View.INVISIBLE);
            }
        });

        Constran_quangcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constran_quangcao.setVisibility(View.INVISIBLE);
            }
        });

        Img_anh_quangcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("QUAN_LY").child("LQC").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(snapshot.getValue().toString()));
                        getActivity().startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void anhXa(View view) {

        Sophienban_khuyenkhich = BuildConfig.VERSION_CODE;
        Sophienban_chan = BuildConfig.VERSION_CODE;

        RecyclerView_danhsachmoidangnhap_fragment_trangchu = view.findViewById(R.id.RecyclerView_danhsachmoidangnhap_fragment_trangchu);
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        Btn_timkiem_trangchu = view.findViewById(R.id.Btn_timkiem_trangchu);
        Btn_timkiemthanhvien_trangchu = view.findViewById(R.id.Btn_timkiemthanhvien_trangchu);
        Btn_timquanhday_trangchu = view.findViewById(R.id.Btn_timquanhday_trangchu);
        Btn_tangvang_trangchu = view.findViewById(R.id.Btn_tangvang_trangchu);
        Btn_huytimkiemthanhvien_trangchu = view.findViewById(R.id.Btn_huytimkiemthanhvien_trangchu);
        Btn_hengap_trangchu = view.findViewById(R.id.Btn_hengap_trangchu);
        Btn_quaylai_trangchu = view.findViewById(R.id.Btn_quaylai_trangchu);
        Btn_phongchat_trangchu = view.findViewById(R.id.Btn_phongchat_trangchu);
        Btn_hoptuoi_trangchu = view.findViewById(R.id.Btn_boitinhyeu_trangchu);
        Btn_nguoila_trangchu = view.findViewById(R.id.Btn_nguoila_trangchu);
        Btn_thuemuon_trangchu = view.findViewById(R.id.Btn_thuemuon_trangchu);
        Btn_dong_quangcao = view.findViewById(R.id.Btn_dong_quangcao);
        Layuotphu1_timkiem_trangchu = view.findViewById(R.id.Layuotphu1_timkiem_trangchu);
        Constran_quangcao = view.findViewById(R.id.Constran_quangcao);
        ds_menu_trangchu = view.findViewById(R.id.ds_menu_trangchu);
        spinner_mucdichthamgia_timkiem_trangchu = view.findViewById(R.id.muctieu_timkiem_trangchu);
        spinner_gioitinh_timkiem_trangchu = view.findViewById(R.id.spinner_gioitinh_timkiem_trangchu);
        spinner_noio_timkiem_trangchu = view.findViewById(R.id.spinner_noio_timkiem_trangchu);
        spinner_dangcap_timkiem_trangchu = view.findViewById(R.id.spinner_dangcap_timkiem_trangchu);
        muctieu_timkiem_trangchu = view.findViewById(R.id.muctieu_timkiem_trangchu);
        Txt_danhsachthanhvien_trangchu = view.findViewById(R.id.Txt_danhsachthanhvien_trangchu);
        Img_anh_quangcao = view.findViewById(R.id.Img_anh_quangcao);

        mLay = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        spLuu = sp.edit();
        gson = new Gson();
    }

    private void LayThongTinChon() {
        Loc_mucdichthamgia = spinner_mucdichthamgia_timkiem_trangchu.getSelectedItem().toString();
        Loc_gioitinh = spinner_gioitinh_timkiem_trangchu.getSelectedItem().toString();
        Loc_noio = spinner_noio_timkiem_trangchu.getSelectedItem().toString();
        Loc_dangcap = spinner_dangcap_timkiem_trangchu.getSelectedItem().toString();

        if (Loc_dangcap.equals("Tất cả") && Loc_mucdichthamgia.equals("Tất cả") && Loc_gioitinh.equals("Tất cả") && Loc_noio.equals("Tất cả")) {
            dangcap = "locdanhsach";
            noio = "locdanhsach";
            mucdichthamgia  = "locdanhsach";
            gioitinh = "locdanhsach";
            Loc_noio = "Không chọn";
            Loc_gioitinh = "Không chọn";
            Loc_mucdichthamgia = "Không chọn";
            Loc_dangcap = "Không chọn";
        } else {
            if(Loc_gioitinh.equals("Tất cả")){gioitinh = "locdanhsach"; Loc_gioitinh = "Không chọn"; }
            else {gioitinh = "gioitinh";}
            if(Loc_mucdichthamgia.equals("Tất cả")){mucdichthamgia = "locdanhsach"; Loc_mucdichthamgia = "Không chọn";}
            else {mucdichthamgia = "mucdichthamgia";}
            if(Loc_noio.equals("Tất cả")){noio = "locdanhsach"; Loc_noio = "Không chọn";}
            else {noio = "noio";}
            if(Loc_dangcap.equals("Tất cả")){dangcap = "locdanhsach"; Loc_dangcap = "Không chọn";}
            else {dangcap = "dangcap";}
        }
    }

    private void taokhunghienthithanhvien() {
        // Hiển thị danh sách thành viên mới đăng nhập
        Danhsachthanhvienmoidangnhap = new ArrayList<>();
        Danhsachthanhvienmoidangnhap_timkiem = new ArrayList<>();
        RecyclerView_danhsachmoidangnhap_fragment_trangchu.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView_danhsachmoidangnhap_fragment_trangchu.setLayoutManager(linearLayoutManager);
        danhsach = new Thanhvienmoidangnhap_adapter(Danhsachthanhvienmoidangnhap, getContext(), this);
        RecyclerView_danhsachmoidangnhap_fragment_trangchu.setAdapter(danhsach);
// Hiển thị danh sách thành viên mới đăng nhập xong
    }

    private void daylendaudanhsach() {
        if (User != null) {
            // lấy ngày hiện tại
            @SuppressLint("SimpleDateFormat") DateFormat dinhdangngaythangnam = new SimpleDateFormat("yyyyMMdd");
            Integer songaythangnam_suahoso = Integer.parseInt(dinhdangngaythangnam.format(Calendar.getInstance().getTime()));
            FirebaseFirestore.getInstance().collection("USER").document(User.getUid()).update("ngaydangxuat", songaythangnam_suahoso);
            // lấy ngày hiện tại xong
        }
    }

    private void hienthiloichaoadmin() {
        // Hiển thị lời chào Admin
        mData.child("QUAN_LY").child("Loichao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Txt_danhsachthanhvien_trangchu.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// Hiển thị lời chào Admin xong
    }

    private void kiemtraphienban() {
        // Kiểm tra phiên bản

        mData.child("QUAN_LY").child("phienban").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_kiemtraphienban kiemtra = postSnapshot.getValue(Model_kiemtraphienban.class);
                    if (Sophienban_khuyenkhich <= kiemtra.getPhienbankhuyenkhich()) {
                        Toast.makeText(getActivity(), "Ứng dụng đã có phiên bản mới\nVui lòng cập nhật để bổ sung tính năng và sửa lỗi", Toast.LENGTH_LONG).show();
                    }
                    if (Sophienban_chan <= kiemtra.getPhienbanchan()) {
                        final AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getActivity());
                        alerDialogBuilder.setMessage("Ứng dụng đã có bản mới\nVui lòng cập nhật để bổ sung tính năng và sửa lỗi");
                        alerDialogBuilder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        });
                        AlertDialog alerDialog = alerDialogBuilder.create();
                        alerDialog.show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

// Kiểm tra phiên bản xong
    }

    private void quangcaonoibo() {
        // Quảng cáo nội bộ
        mData.child("QUAN_LY").child("QC").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String quangcao = (String) snapshot.getValue();
                if(snapshot.getValue() != null) {
                    if (quangcao.equals("KHONG")) {
                        Constran_quangcao.setVisibility(View.INVISIBLE);
                    } else {
                        Constran_quangcao.setVisibility(View.VISIBLE);
                        Picasso.get().load(snapshot.getValue().toString()).into(Img_anh_quangcao);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// Quảng cáo nội bộ xong
    }

    private void taodanhsachdangcap() {
        // Tạo danh sách đẳng cấp
        List<String> dulieu_dangcap = new ArrayList<>();
        dulieu_dangcap.add("Tất cả");
        dulieu_dangcap.add("Bình dân");
        dulieu_dangcap.add("Khá giả");
        dulieu_dangcap.add("Giàu có");
        dulieu_dangcap.add("Đại gia");

        ArrayAdapter<String> Adapter_dangcap = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, dulieu_dangcap);
        spinner_dangcap_timkiem_trangchu.setAdapter(Adapter_dangcap);
        spinner_dangcap_timkiem_trangchu.setSelection(mLay.getInt("chondangcap", 0));
        spinner_dangcap_timkiem_trangchu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinner_dangcap_timkiem_trangchu.getSelectedItem().equals("Tất cả")){
                    spinner_dangcap_timkiem_trangchu.setBackgroundResource(R.drawable.thietke_nut_1);
                }else {
                    spinner_dangcap_timkiem_trangchu.setBackgroundResource(R.drawable.thietke_edittext);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
// Tạo xong danh sách đẳng cấp
    }

    private void taodanhsachnoio() {
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
        dulieu_noio.add(0, "Tất cả");
        dulieu_noio.add(1, "TP Hồ Chí Minh");
        dulieu_noio.add(2,"Hà Nội");
        dulieu_noio.add(3,"Nước ngoài");
        // Sắp xếp danh sách xong
        ArrayAdapter<String> Adapter_noio = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, dulieu_noio);
        spinner_noio_timkiem_trangchu.setAdapter(Adapter_noio);
        spinner_noio_timkiem_trangchu.setSelection(mLay.getInt("chonnoio", 0));
        spinner_noio_timkiem_trangchu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinner_noio_timkiem_trangchu.getSelectedItem().equals("Tất cả")){
                    spinner_noio_timkiem_trangchu.setBackgroundResource(R.drawable.thietke_nut_1);
                }else {
                    spinner_noio_timkiem_trangchu.setBackgroundResource(R.drawable.thietke_edittext);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
// Tạo danh sách nơi ở xong
    }

    private void taodanhsachmuctieu() {
        // Tạo danh sách mục tiêu
        List<String> dulieu_muctieu = new ArrayList<>();
        dulieu_muctieu.add("Tất cả");
        dulieu_muctieu.add("Tìm người yêu lâu dài");
        dulieu_muctieu.add("Tìm người yêu ngắn hạn");
        dulieu_muctieu.add("Tìm người kết hôn");
        dulieu_muctieu.add("Tìm người tâm sự");
        dulieu_muctieu.add("Tìm bạn bè mới");
        dulieu_muctieu.add("Tìm đối tác kinh doanh");
        ArrayAdapter<String> Adapter_muctieu = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, dulieu_muctieu);
        spinner_mucdichthamgia_timkiem_trangchu.setAdapter(Adapter_muctieu);
        spinner_mucdichthamgia_timkiem_trangchu.setSelection(mLay.getInt("chonmuctieu", 0));
        spinner_mucdichthamgia_timkiem_trangchu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinner_mucdichthamgia_timkiem_trangchu.getSelectedItem().equals("Tất cả")){
                    spinner_mucdichthamgia_timkiem_trangchu.setBackgroundResource(R.drawable.thietke_nut_1);
                }else {
                    spinner_mucdichthamgia_timkiem_trangchu.setBackgroundResource(R.drawable.thietke_edittext);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
// Tạo xong danh sách mục tiêu
    }

    private void taodanhssachgioitinh() {
        // Tạo danh sách giới tính
        List<String> dulieu_gioitinh = new ArrayList<>();
        dulieu_gioitinh.add("Tất cả");
        dulieu_gioitinh.add("Nam");
        dulieu_gioitinh.add("Nữ");
        dulieu_gioitinh.add("Les");
        dulieu_gioitinh.add("Gay");
        ArrayAdapter<String> Adapter_gioitinh = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, dulieu_gioitinh);
        spinner_gioitinh_timkiem_trangchu.setAdapter(Adapter_gioitinh);
        spinner_gioitinh_timkiem_trangchu.setSelection(mLay.getInt("chongioitinh", 0));
        spinner_gioitinh_timkiem_trangchu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinner_gioitinh_timkiem_trangchu.getSelectedItem().equals("Tất cả")){
                    spinner_gioitinh_timkiem_trangchu.setBackgroundResource(R.drawable.thietke_nut_1);
                }else {
                    spinner_gioitinh_timkiem_trangchu.setBackgroundResource(R.drawable.thietke_edittext);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
// Tạo xong danh sách giới tính
    }

    @SuppressLint("NotifyDataSetChanged")
    private void Docdanhsachtrongmay() {
        Toast.makeText(getActivity(), "Đọc danh sách lưu", Toast.LENGTH_SHORT).show();
        Danhsachthanhvienmoidangnhap.clear();
        String json = mLay.getString("Set", "");
        Type type = new TypeToken<ArrayList<Model_thanhvienmoidangnhap>>() {
        }.getType();
        ArrayList<Model_thanhvienmoidangnhap> arrPackageData = gson.fromJson(json, type);
        if(arrPackageData != null) {
            Danhsachthanhvienmoidangnhap.addAll(arrPackageData);
            Collections.sort(Danhsachthanhvienmoidangnhap, new Comparator<Model_thanhvienmoidangnhap>() {
                @Override
                public int compare(Model_thanhvienmoidangnhap o1, Model_thanhvienmoidangnhap o2) {
                    return o2.getNgaydangxuat().compareTo(o1.getNgaydangxuat());
                }
            });
            danhsach.notifyDataSetChanged();
        }else {
            Docdanhsachtrenmang();
        }
    }

    private void Docdanhsachtrenmang() {
        Toast.makeText(getActivity(), "Tải mới trên mạng", Toast.LENGTH_SHORT).show();
        Query_locdanhsach = db.collection("USER")
                .whereEqualTo(gioitinh, Loc_gioitinh)
                .whereEqualTo(mucdichthamgia, Loc_mucdichthamgia)
                .whereEqualTo(noio, Loc_noio)
                .whereEqualTo(dangcap, Loc_dangcap)
                .whereGreaterThan("ngaydangxuat", 0)
                .orderBy("ngaydangxuat", Query.Direction.DESCENDING)
        ;
        Query_locdanhsach.limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Danhsachthanhvienmoidangnhap.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Danhsachthanhvienmoidangnhap.add(document.toObject(Model_thanhvienmoidangnhap.class));
                }
                Collections.sort(Danhsachthanhvienmoidangnhap, new Comparator<Model_thanhvienmoidangnhap>() {
                    @Override
                    public int compare(Model_thanhvienmoidangnhap o1, Model_thanhvienmoidangnhap o2) {
                        return o2.getNgaydangxuat().compareTo(o1.getNgaydangxuat());
                    }
                });
                danhsach.notifyDataSetChanged();
                if (task.getResult().size() > 0){
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() -1);
                }else {
                    lastVisible = null;
                }
                Luudanhsach();
            }
        });
    }

    private void Docdanhsachtrenmang_taithem() {

        Toast.makeText(getActivity(), "Tải thêm nè", Toast.LENGTH_SHORT).show();

        Query_locdanhsach = db.collection("USER")
                .whereEqualTo(gioitinh, Loc_gioitinh)
                .whereEqualTo(mucdichthamgia, Loc_mucdichthamgia)
                .whereEqualTo(noio, Loc_noio)
                .whereEqualTo(dangcap, Loc_dangcap)
                .whereGreaterThan("ngaydangxuat", 0)
                .orderBy("ngaydangxuat", Query.Direction.DESCENDING)
        ;

        if (lastVisible == null){
            Query_locdanhsach.limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Model_thanhvienmoidangnhap thanhvien = document.toObject(Model_thanhvienmoidangnhap.class);
                        if (!Danhsachthanhvienmoidangnhap.contains(thanhvien)){
                            Danhsachthanhvienmoidangnhap.add(document.toObject(Model_thanhvienmoidangnhap.class));
                        }
                    }
                    danhsach.notifyDataSetChanged();
                    if (task.getResult().size() > 0){
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() -1);
                    }else {
                        lastVisible = null;
                    }
                }
            });
        }else {
            Query_locdanhsach.startAfter(lastVisible).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Model_thanhvienmoidangnhap thanhvien = document.toObject(Model_thanhvienmoidangnhap.class);
                        if (!Danhsachthanhvienmoidangnhap.contains(thanhvien)){
                            Danhsachthanhvienmoidangnhap.add(document.toObject(Model_thanhvienmoidangnhap.class));
                        }
                    }
                    danhsach.notifyDataSetChanged();
                    if (task.getResult().size() > 0){
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() -1);
                    }else {
                        lastVisible = null;
                    }
                    Luudanhsach();
                }
            });
        }
        Luudanhsach();
    }

    public void Luudanhsach(){
        String json = gson.toJson(Danhsachthanhvienmoidangnhap);
        spLuu.remove("Set");
        spLuu.commit();
        spLuu.putString("Set",json);
        spLuu.commit();
    }

    @Override
    public void onItemClick(String itemclick) {
        chuyenmanhinh(itemclick);
    }

    private void chuyenmanhinh(String itemclick) {
        Intent intent = new Intent(getContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", itemclick);
        startActivity(intent);
    }
}
