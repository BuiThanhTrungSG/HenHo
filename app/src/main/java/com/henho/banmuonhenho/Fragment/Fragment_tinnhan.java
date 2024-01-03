package com.henho.banmuonhenho.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.henho.banmuonhenho.Adapter.Danhsachnguoinhantin_adapter;
import com.henho.banmuonhenho.Dang_ky;
import com.henho.banmuonhenho.Dang_nhap;
import com.henho.banmuonhenho.Model.Model_danhsachnguoinhantin;
import com.henho.banmuonhenho.Model.Model_danhsachnguoinhantin_xoa;
import com.henho.banmuonhenho.Model.OnItemClickListener;
import com.henho.banmuonhenho.Nhan_tin;
import com.henho.banmuonhenho.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Fragment_tinnhan extends Fragment implements OnItemClickListener {

    FirebaseAuth mAuth;
    FirebaseUser User;
    DatabaseReference mData;
    LinearLayout LinearLayuot1_tinnhan, LinearLayuot2_tinnhan;
    RecyclerView RecyclerView_danhsachnguoinhantin_fragment_tinnhan;
    ArrayList<Model_danhsachnguoinhantin_xoa> Danhsachnguoinhantin, trunggian;
    Button Btn_dangnhap_tinnhan, Btn_dangky_tinnhan;
    Danhsachnguoinhantin_adapter danhsach_tn;
    CheckBox Checkb_xoahet_tinnhan;
    ImageView Img_xoahet_tinnhan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tinnhan, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        Danhsachnguoinhantin = new ArrayList<>();
        trunggian = new ArrayList<>();
        LinearLayuot1_tinnhan = view.findViewById(R.id.LinearLayuot1_tinnhan);
        LinearLayuot2_tinnhan = view.findViewById(R.id.LinearLayuot2_tinnhan);
        RecyclerView_danhsachnguoinhantin_fragment_tinnhan = view.findViewById(R.id.RecyclerView_danhsachnguoinhantin_fragment_tinnhan);
        Btn_dangky_tinnhan = view.findViewById(R.id.Btn_dangky_tinnhan);
        Btn_dangnhap_tinnhan = view.findViewById(R.id.Btn_dangnhap_tinnhan);
        Checkb_xoahet_tinnhan = view.findViewById(R.id.Checkb_xoahet_tinnhan);
        Img_xoahet_tinnhan = view.findViewById(R.id.Img_xoahet_tinnhan);

// Kiểm tra đăng nhập
        if (User == null) {
            LinearLayuot1_tinnhan.setVisibility(View.INVISIBLE);
            LinearLayuot2_tinnhan.setVisibility(View.VISIBLE);
            Btn_dangnhap_tinnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),Dang_nhap.class);
                    startActivity(intent);
                }
            });
            Btn_dangky_tinnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Dang_ky.class);
                    startActivity(intent);
                }
            });
        } else {
            LinearLayuot1_tinnhan.setVisibility(View.VISIBLE);
            LinearLayuot2_tinnhan.setVisibility(View.INVISIBLE);
// Kiểm tra đăng nhập xong, chú ý còn 1 dấu ngoặc ở gần dưới cùng


// Đọc danh sách từ Firebase
            mData.child("USERS").child(User.getUid()).child("cacphongchat").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Danhsachnguoinhantin.clear();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Model_danhsachnguoinhantin dstn = postSnapshot.getValue(Model_danhsachnguoinhantin.class);
                        Danhsachnguoinhantin.add(0, new Model_danhsachnguoinhantin_xoa(dstn.getIdnguoichat(), dstn.getThoidiem(), dstn.getTrangthai(), 1));
                        sap_xep_danh_sach();
                    }

                    for(int i = 0; i < Danhsachnguoinhantin.size(); i++){
                        if(Danhsachnguoinhantin.get(i).getTrangthai() != null) {
                            if (Danhsachnguoinhantin.get(i).getTrangthai()) {
                                Danhsachnguoinhantin.add(0, Danhsachnguoinhantin.get(i));
                                Danhsachnguoinhantin.remove(i + 1);
                            }
                        }
                    }
                    danhsach_tn.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
// Đọc danh sách từ Firebase xong

            // Hiển thị danh sách thành viên
            RecyclerView_danhsachnguoinhantin_fragment_tinnhan.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            RecyclerView_danhsachnguoinhantin_fragment_tinnhan.setLayoutManager(linearLayoutManager);
            danhsach_tn = new Danhsachnguoinhantin_adapter(getContext(), Danhsachnguoinhantin, this, this, this);
            RecyclerView_danhsachnguoinhantin_fragment_tinnhan.setAdapter(danhsach_tn);
            // Hiển thị danh sách thành viên

            Checkb_xoahet_tinnhan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    trunggian.clear();;
                    for(int i = 0; i < Danhsachnguoinhantin.size(); i++){
                        trunggian.add(new Model_danhsachnguoinhantin_xoa(Danhsachnguoinhantin.get(i).getIdnguoichat(),
                                Danhsachnguoinhantin.get(i).getThoidiem(),
                                Danhsachnguoinhantin.get(i).getTrangthai(),
                                Danhsachnguoinhantin.get(i).getTrangthaixoa()));
                    }
                    Danhsachnguoinhantin.clear();
                    if(isChecked){
                        for(int i = 0; i < trunggian.size(); i++){
                            Danhsachnguoinhantin.add(new Model_danhsachnguoinhantin_xoa(trunggian.get(i).getIdnguoichat(), trunggian.get(i).getThoidiem(), trunggian.get(i).getTrangthai(), 3));
                        }
                    }else {
                        for (int i = 0; i < trunggian.size(); i++) {
                            Danhsachnguoinhantin.add(new Model_danhsachnguoinhantin_xoa(trunggian.get(i).getIdnguoichat(), trunggian.get(i).getThoidiem(), trunggian.get(i).getTrangthai(), 2));
                        }
                    }
                    danhsach_tn.notifyDataSetChanged();
                }
            });

            Img_xoahet_tinnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trunggian.clear();;
                    for(int i = 0; i < Danhsachnguoinhantin.size(); i++){
                        trunggian.add(new Model_danhsachnguoinhantin_xoa(Danhsachnguoinhantin.get(i).getIdnguoichat(),
                                Danhsachnguoinhantin.get(i).getThoidiem(),
                                Danhsachnguoinhantin.get(i).getTrangthai(),
                                Danhsachnguoinhantin.get(i).getTrangthaixoa()));
                    }
                    Danhsachnguoinhantin.clear();
                    if(Checkb_xoahet_tinnhan.getVisibility() == View.VISIBLE){
                        Checkb_xoahet_tinnhan.setVisibility(View.INVISIBLE);
                        Boolean kiemtra = false;
                        for(int i = 0; i < trunggian.size(); i++) {
                            if (trunggian.get(i).getTrangthaixoa() == 3) {
                                kiemtra = true;
                            }
                        }
                        if(kiemtra){
                            AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getActivity());
                            alerDialogBuilder.setMessage("Bạn muốn xóa các tin nhắn này?");
                            alerDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    for(int i = 0; i < trunggian.size(); i++) {
                                        if (trunggian.get(i).getTrangthaixoa() == 2) {
                                            Danhsachnguoinhantin.add(new Model_danhsachnguoinhantin_xoa(trunggian.get(i).getIdnguoichat(), trunggian.get(i).getThoidiem(), trunggian.get(i).getTrangthai(), 1));
                                        }else if(trunggian.get(i).getTrangthaixoa() == 3){
                                            mData.child("USERS").child(User.getUid()).child("cacphongchat").child(trunggian.get(i).getIdnguoichat()).removeValue();
                                        }
                                    }
                                    danhsach_tn.notifyDataSetChanged();

                                }
                            });
                            alerDialogBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < trunggian.size(); i++) {
                                        Danhsachnguoinhantin.add(new Model_danhsachnguoinhantin_xoa(trunggian.get(i).getIdnguoichat(), trunggian.get(i).getThoidiem(), trunggian.get(i).getTrangthai(), 1));
                                    }
                                    danhsach_tn.notifyDataSetChanged();

                                }
                            });
                            AlertDialog alerDialog = alerDialogBuilder.create();
                            alerDialog.show();
                        }else {
                            for (int i = 0; i < trunggian.size(); i++) {
                                Danhsachnguoinhantin.add(new Model_danhsachnguoinhantin_xoa(trunggian.get(i).getIdnguoichat(), trunggian.get(i).getThoidiem(), trunggian.get(i).getTrangthai(), 1));
                            }
                        }

                    }else {
                        Checkb_xoahet_tinnhan.setVisibility(View.VISIBLE);
                        for(int i = 0; i < trunggian.size(); i++){
                            Danhsachnguoinhantin.add(new Model_danhsachnguoinhantin_xoa(trunggian.get(i).getIdnguoichat(), trunggian.get(i).getThoidiem(), trunggian.get(i).getTrangthai(), 2));
                        }
                    }
                    danhsach_tn.notifyDataSetChanged();
                }
            });

        }
    }

    @Override
    public void onItemClick(Model_danhsachnguoinhantin_xoa itemclick) {
        if(Checkb_xoahet_tinnhan.getVisibility() == View.VISIBLE){
            Checkb_xoahet_tinnhan.setVisibility(View.INVISIBLE);
            trunggian.clear();;
            for(int i = 0; i < Danhsachnguoinhantin.size(); i++){
                trunggian.add(new Model_danhsachnguoinhantin_xoa(Danhsachnguoinhantin.get(i).getIdnguoichat(),
                        Danhsachnguoinhantin.get(i).getThoidiem(),
                        Danhsachnguoinhantin.get(i).getTrangthai(),
                        Danhsachnguoinhantin.get(i).getTrangthaixoa()));
            }
            Danhsachnguoinhantin.clear();
            for (int i = 0; i < trunggian.size(); i++) {
                Danhsachnguoinhantin.add(new Model_danhsachnguoinhantin_xoa(trunggian.get(i).getIdnguoichat(), trunggian.get(i).getThoidiem(), trunggian.get(i).getTrangthai(), 1));
            }
            danhsach_tn.notifyDataSetChanged();
//            int trangthai = 2;
//            for(int i = 0; i < Danhsachnguoinhantin.size(); i++){
//                if(Danhsachnguoinhantin.get(i).getIdnguoichat().equals(itemclick.getIdnguoichat())){
//                    if(Danhsachnguoinhantin.get(i).getTrangthaixoa() == 2) {
//                        trangthai = 3;
//                    }else if(Danhsachnguoinhantin.get(i).getTrangthaixoa() == 3){
//                        trangthai = 2;
//                    }
//                    Danhsachnguoinhantin.set(i, new Model_danhsachnguoinhantin_xoa(Danhsachnguoinhantin.get(i).getIdnguoichat(),
//                            Danhsachnguoinhantin.get(i).getThoidiem(),
//                            Danhsachnguoinhantin.get(i).getTrangthai(),
//                            trangthai));
//                }
//            }
//            danhsach_tn.notifyDataSetChanged();
        }else {
            mData.child("USERS").child(User.getUid()).child("cacphongchat").child(itemclick.getIdnguoichat()).child("trangthai").setValue(false);
            Intent intent = new Intent(getContext(), Nhan_tin.class);
            intent.putExtra("guiidnguoichat", itemclick.getIdnguoichat());
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick_long(final Model_danhsachnguoinhantin_xoa itemclick_long) {

        if(Checkb_xoahet_tinnhan.getVisibility() == View.INVISIBLE) {
            Checkb_xoahet_tinnhan.setVisibility(View.VISIBLE);
            trunggian.clear();;
            for(int i = 0; i < Danhsachnguoinhantin.size(); i++){
                trunggian.add(new Model_danhsachnguoinhantin_xoa(Danhsachnguoinhantin.get(i).getIdnguoichat(),
                        Danhsachnguoinhantin.get(i).getThoidiem(),
                        Danhsachnguoinhantin.get(i).getTrangthai(),
                        Danhsachnguoinhantin.get(i).getTrangthaixoa()));
            }
            Danhsachnguoinhantin.clear();
            for (int i = 0; i < trunggian.size(); i++) {
                Danhsachnguoinhantin.add(new Model_danhsachnguoinhantin_xoa(trunggian.get(i).getIdnguoichat(), trunggian.get(i).getThoidiem(), trunggian.get(i).getTrangthai(), 2));
            }
        }
        danhsach_tn.notifyDataSetChanged();
    }


    @Override
    public void onItemClick_xoa(Model_danhsachnguoinhantin_xoa itemclick) {
        int trangthai = 2;
        for(int i = 0; i < Danhsachnguoinhantin.size(); i++){
            if(Danhsachnguoinhantin.get(i).getIdnguoichat().equals(itemclick.getIdnguoichat())){
                if(Danhsachnguoinhantin.get(i).getTrangthaixoa() == 2) {
                    trangthai = 3;
                }else if(Danhsachnguoinhantin.get(i).getTrangthaixoa() == 2){
                    trangthai = 2;
                }
                Danhsachnguoinhantin.set(i, new Model_danhsachnguoinhantin_xoa(Danhsachnguoinhantin.get(i).getIdnguoichat(),
                        Danhsachnguoinhantin.get(i).getThoidiem(),
                        Danhsachnguoinhantin.get(i).getTrangthai(),
                        trangthai));
            }
        }
        danhsach_tn.notifyDataSetChanged();
    }


    private void sap_xep_danh_sach() {
        // Sắp xếp thứ tự danh sách
        Collections.sort(Danhsachnguoinhantin, new Comparator<Model_danhsachnguoinhantin_xoa>() {
            @Override
            public int compare(Model_danhsachnguoinhantin_xoa model_danhsachnguoinhantin, Model_danhsachnguoinhantin_xoa t1) {
                if (model_danhsachnguoinhantin == null || model_danhsachnguoinhantin.getThoidiem() == null) {
                    return 0;  //null values will be displayed at bottom in sorted list
                }

                if (t1 == null || t1.getThoidiem() == null ) {
                    return 0;  //null values will be displayed at bottom in sorted list
                }
                return t1.getThoidiem().compareTo(model_danhsachnguoinhantin.getThoidiem());
            }
        });
    // Sắp xếp thứ tự danh sách xong
    }
}
