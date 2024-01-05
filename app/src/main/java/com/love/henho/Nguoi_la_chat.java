package com.love.henho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.love.henho.Adapter.Noidungtinnhan_adapter;
import com.love.henho.Model.Model_noidungtinnhan;
import com.love.henho.Model.Model_noidungtinnhan_less;

import java.util.ArrayList;

public class Nguoi_la_chat extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;

    RecyclerView Recy_toanbotinnhan_nguoila;
    EditText Edt_noidungnhan_nguoila;
    ImageView Btn_gui_nguoila;
    Button Btn_quaylai_nguoila, Btn_ketban_nguoilachat;
    String idnguoichat;
    ArrayList<Model_noidungtinnhan_less> tinnhandagui;
    Noidungtinnhan_adapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoi_la_chat);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        Recy_toanbotinnhan_nguoila = findViewById(R.id.Recy_toanbotinnhan_nguoilachat);
        Edt_noidungnhan_nguoila = findViewById(R.id.Edt_noidungnhan_nguoilachat);
        Btn_gui_nguoila = findViewById(R.id.Btn_gui_nguoilachat);
        Btn_ketban_nguoilachat = findViewById(R.id.Btn_ketban_nguoilachat);
        Btn_quaylai_nguoila = findViewById(R.id.Btn_quaylai_nguoilachat);

// lấy số hồ sơ (ID chủ hồ sơ) từ trang trước truyền qua
        Intent intent = getIntent();
        idnguoichat = intent.getStringExtra("guiidnguoichat");
// lấy số hồ sơ (ID chủ hồ sơ) từ trang trước truyền qua xong

        // Hiển thị danh sách
        tinnhandagui = new ArrayList<>();
        Recy_toanbotinnhan_nguoila.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Recy_toanbotinnhan_nguoila.setLayoutManager(linearLayoutManager);
        Adapter = new Noidungtinnhan_adapter(getApplicationContext(), tinnhandagui);
        Recy_toanbotinnhan_nguoila.setAdapter(Adapter);
        // Hiển thị danh sách xong

        mData.child("NGUOI_LA").child(USER.getUid()).removeValue();
        mData.child("NGUOI_LA").child(idnguoichat).removeValue();
        mData.child("Phong_chat_nguoi_la").child(USER.getUid()).removeValue();
        mData.child("Phong_chat_nguoi_la").child(idnguoichat).removeValue();

        // Đọc danh sách trên mạng
        mData.child("Phong_chat_nguoi_la").child(USER.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tinnhandagui.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Model_noidungtinnhan_less trungchuyen = snap.getValue(Model_noidungtinnhan_less.class);
                    if(trungchuyen != null) {
                        tinnhandagui.add(trungchuyen);
                        Adapter.notifyDataSetChanged();
                        Recy_toanbotinnhan_nguoila.smoothScrollToPosition(tinnhandagui.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Đọc danh sách trên mạng xong

        mData.child("Phong_chat_nguoi_la").child("Ket_ban").child(USER.getUid()).child("ketban").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    if (snapshot.getValue().equals("daketban")) {
                        Toast.makeText(Nguoi_la_chat.this, "Đã bỏ chế độ ẩn danh với người lạ", Toast.LENGTH_LONG).show();
                        mData.child("Phong_chat_nguoi_la").child("Ket_ban").child(USER.getUid()).removeValue();
                        Intent intent = new Intent(getApplicationContext(), Nhan_tin.class);
                        intent.putExtra("guiidnguoichat", idnguoichat);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Btn_gui_nguoila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model_noidungtinnhan tinnhan = new Model_noidungtinnhan(USER.getUid(), Edt_noidungnhan_nguoila.getText().toString(), "a");
                mData.child("Phong_chat_nguoi_la").child(USER.getUid()).push().setValue(tinnhan);
                mData.child("Phong_chat_nguoi_la").child(idnguoichat).push().setValue(tinnhan);
                Edt_noidungnhan_nguoila.setText("");
            }
        });

        Btn_ketban_nguoilachat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("Phong_chat_nguoi_la").child("Ket_ban").child(idnguoichat).child("ketban").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() !=null) {
                            if (snapshot.getValue().equals("sansang")) {
                                Toast.makeText(Nguoi_la_chat.this, "Đã bỏ chế độ ẩn danh với người lạ", Toast.LENGTH_LONG).show();
                                mData.child("Phong_chat_nguoi_la").child("Ket_ban").child(idnguoichat).child("ketban").setValue("daketban");
                                mData.child("Phong_chat_nguoi_la").child("Ket_ban").child(USER.getUid()).removeValue();
                                Intent intent = new Intent(getApplicationContext(), Nhan_tin.class);
                                intent.putExtra("guiidnguoichat", idnguoichat);
                                startActivity(intent);
                            } else {
                                mData.child("Phong_chat_nguoi_la").child("Ket_ban").child(USER.getUid()).child("ketban").setValue("sansang");
                                Btn_ketban_nguoilachat.setVisibility(View.INVISIBLE);
                            }
                        }else {
                            mData.child("Phong_chat_nguoi_la").child("Ket_ban").child(USER.getUid()).child("ketban").setValue("sansang");
                            Btn_ketban_nguoilachat.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        Btn_quaylai_nguoila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Nguoi_la_chat.this);
                alerDialogBuilder.setMessage("Bạn muốn thoát khỏi cuộc trò chuyện này?");
                alerDialogBuilder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mData.child("Phong_chat_nguoi_la").child("Ket_ban").child(USER.getUid()).removeValue();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
                alerDialogBuilder.setNegativeButton("Ở lại", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Nguoi_la_chat.this);
        alerDialogBuilder.setMessage("Bạn muốn thoát khỏi cuộc trò chuyện này?");
        alerDialogBuilder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mData.child("Phong_chat_nguoi_la").child("Ket_ban").child(USER.getUid()).removeValue();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        alerDialogBuilder.setNegativeButton("Ở lại", null);
        AlertDialog alerDialog = alerDialogBuilder.create();
        alerDialog.show();
    }
}