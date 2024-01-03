package com.henho.banmuonhenho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.henho.banmuonhenho.Adapter.Admin2_adapter;
import com.henho.banmuonhenho.Adapter.Admin_adapter;
import com.henho.banmuonhenho.Model.Model_duyetanh;
import com.henho.banmuonhenho.Model.Model_duyetnoidung_admin;
import com.henho.banmuonhenho.Model.OnItemClickListener_admin;
import com.henho.banmuonhenho.Model.OnItemClickListener_admin2;

import java.util.ArrayList;

public class Admin_control extends AppCompatActivity implements OnItemClickListener_admin, OnItemClickListener_admin2 {

    FirebaseAuth mAuth;
    FirebaseUser User;
    DatabaseReference mData;
    RecyclerView Recycle_danhsachdoiduyet;
    ArrayList<Model_duyetnoidung_admin> danhsach;
    ArrayList<Model_duyetanh> danhsach_anh;
    Admin_adapter adapter;
    Admin2_adapter adapter_anh;
    Button Btn_doi_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        Recycle_danhsachdoiduyet = findViewById(R.id.danhsach_admin);
        Btn_doi_admin = findViewById(R.id.Btn_doi_admin);
        danhsach = new ArrayList<>();
        danhsach_anh = new ArrayList<>();


        // Hiển thị danh sách thành viên
        Recycle_danhsachdoiduyet.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Recycle_danhsachdoiduyet.setLayoutManager(linearLayoutManager);
        adapter = new Admin_adapter(Admin_control.this, danhsach, this);
        adapter_anh = new Admin2_adapter(Admin_control.this, danhsach_anh, this);
        Recycle_danhsachdoiduyet.setAdapter(adapter);

        Btn_doi_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Btn_doi_admin.getText().equals("Đổi")){
                    Btn_doi_admin.setText("Đổi lại");
                    Recycle_danhsachdoiduyet.setAdapter(adapter);
                }else {
                    Btn_doi_admin.setText("Đổi");
                    Recycle_danhsachdoiduyet.setAdapter(adapter_anh);
                }
            }
        });

        // Hiển thị danh sách thành viên

// lấy danh sách về - gioi thiệu
        mData.child("QUAN_LY").child("duyetnoidung").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsach.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_duyetnoidung_admin trunggian = postSnapshot.getValue(Model_duyetnoidung_admin.class);
                    danhsach.add(new Model_duyetnoidung_admin(trunggian.getTen(), trunggian.getNgaysua(), trunggian.getNoidung(), trunggian.getId()));
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// lấy danh sách về - giới thiệu xong

// lấy danh sách về - anhdaidien
        mData.child("QUAN_LY").child("duyetanh").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsach_anh.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_duyetanh trunggian2 = postSnapshot.getValue(Model_duyetanh.class);
                    danhsach_anh.add(new Model_duyetanh(trunggian2.getLinkanhchoduyet(), trunggian2.getIdchuanh()));
                    adapter_anh.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// lấy danh sách về - anhdaidien xong
    }

    @Override
    public void onItemClick(final Model_duyetnoidung_admin itemclick) {
        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Admin_control.this);
        alerDialogBuilder.setMessage("Làm gì với thằng này?");
        alerDialogBuilder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mData.child("QUAN_LY").child("duyetnoidung").child(itemclick.getId()).removeValue();
                mData.child("USERS").child(itemclick.getId()).child("gioithieubanthan").setValue(itemclick.getNoidung());
                mData.child("USERS").child(itemclick.getId()).child("online").setValue("Trống");
                FirebaseFirestore.getInstance().collection("USER").document(itemclick.getId()).update("gioithieubanthan", itemclick.getNoidung());
                FirebaseFirestore.getInstance().collection("USER").document(itemclick.getId()).update("online", "Trống");
                adapter.notifyDataSetChanged();
            }
        });
        alerDialogBuilder.setNegativeButton("Không duyệt", new DialogInterface.OnClickListener() {
                    @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                        mData.child("QUAN_LY").child("duyetnoidung").child(itemclick.getId()).removeValue();
                        mData.child("USERS").child(itemclick.getId()).child("gioithieubanthan").setValue(" Nội dung giới thiệu không phù hợp (có địa chỉ liên lạc hoặc đề cập đến vấn đề tế nhị)\n Đề nghị sửa lại");
                        mData.child("USERS").child(itemclick.getId()).child("online").setValue("Trống");
                        FirebaseFirestore.getInstance().collection("USER").document(itemclick.getId()).update("gioithieubanthan", " Nội dung giới thiệu không phù hợp (có địa chỉ liên lạc hoặc đề cập đến vấn đề tế nhị)\n Đề nghị sửa lại");
                        FirebaseFirestore.getInstance().collection("USER").document(itemclick.getId()).update("online", "Trống");
                        adapter.notifyDataSetChanged();

             }
        });
        AlertDialog alerDialog = alerDialogBuilder.create();
        alerDialog.show();
    }

    @Override
    public void onItemClick(final Model_duyetanh itemclick) {

        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Admin_control.this);
        alerDialogBuilder.setMessage("Làm gì với thằng này?");
        alerDialogBuilder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mData.child("QUAN_LY").child("duyetanh").child(itemclick.getIdchuanh()).removeValue();
                mData.child("USERS").child(itemclick.getIdchuanh()).child("anhdaidien").setValue(itemclick.getLinkanhchoduyet());
                FirebaseFirestore.getInstance().collection("USER").document(itemclick.getIdchuanh()).update("anhdaidien", itemclick.getLinkanhchoduyet());
                adapter_anh.notifyDataSetChanged();
            }
        });
        alerDialogBuilder.setNegativeButton("Không duyệt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mData.child("QUAN_LY").child("duyetanh").child(itemclick.getIdchuanh()).removeValue();
                mData.child("USERS").child(itemclick.getIdchuanh()).child("anhdaidien").setValue("https://firebasestorage.googleapis.com/v0/b/banmuonhh-a42dd.appspot.com/o/logonho3.png?alt=media&token=7bcfd9a4-c098-40f0-b3f4-2b4d72b41f53");
                FirebaseFirestore.getInstance().collection("USER").document(itemclick.getIdchuanh()).update("anhdaidien", "https://firebasestorage.googleapis.com/v0/b/banmuonhh-a42dd.appspot.com/o/logonho3.png?alt=media&token=7bcfd9a4-c098-40f0-b3f4-2b4d72b41f53");
                adapter_anh.notifyDataSetChanged();

            }
        });
        AlertDialog alerDialog = alerDialogBuilder.create();
        alerDialog.show();

    }
}