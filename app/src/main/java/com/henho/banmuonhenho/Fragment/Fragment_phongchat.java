package com.henho.banmuonhenho.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.henho.banmuonhenho.Adapter.Phongchat_adapter;
import com.henho.banmuonhenho.Ho_so;
import com.henho.banmuonhenho.Model.Model_phongchat;
import com.henho.banmuonhenho.Model.OnItemClickListener_phongchat;
import com.henho.banmuonhenho.R;
import com.vanniktech.emoji.EmojiPopup;

import java.util.ArrayList;

public class Fragment_phongchat extends Fragment implements OnItemClickListener_phongchat{

    FirebaseAuth mAuth;
    FirebaseUser User;
    DatabaseReference mData;
    RecyclerView Recy_noidungchat_phongchat;
    EditText Edt_khungsoanthao_phongchat;
    ImageView Btn_gui_phongchat, Img_emoji_phongchat;
    Phongchat_adapter Adapter_phongchat;
    FrameLayout tongthe_phongchat;
    Boolean doiiconemoji;
    ArrayList<Model_phongchat> danhsachchat;
    String ten, dangcap, linkanh;
    ArrayList<String> Xoabottinnhan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phongchat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        Recy_noidungchat_phongchat = view.findViewById(R.id.Recy_noidungchat_phongchat);
        Edt_khungsoanthao_phongchat = view.findViewById(R.id.Edt_khungsoanthao_phongchat);
        Btn_gui_phongchat = view.findViewById(R.id.Btn_gui_phongchat);
        Img_emoji_phongchat = view.findViewById(R.id.Img_emoji_phongchat);
        tongthe_phongchat = view.findViewById(R.id.tongthe_phongchat);
        danhsachchat = new ArrayList<>();
        Xoabottinnhan = new ArrayList<>();
        doiiconemoji = true;

// Tạo emoji
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(view.findViewById(R.id.tongthe_phongchat)).build(Edt_khungsoanthao_phongchat);
        Img_emoji_phongchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.toggle();
                if(doiiconemoji){
                    Img_emoji_phongchat.setImageResource(R.drawable.icon_banphim);
                    doiiconemoji = false;
                }else {
                    Img_emoji_phongchat.setImageResource(R.drawable.icon_emoji);
                    doiiconemoji = true;
                }
            }
        });

// Hiển thị danh sách tin nhắn
        Recy_noidungchat_phongchat.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Recy_noidungchat_phongchat.setLayoutManager(linearLayoutManager);
        Adapter_phongchat = new Phongchat_adapter(getContext(), danhsachchat, this);
        Recy_noidungchat_phongchat.setAdapter(Adapter_phongchat);
// Hiển thị danh sách tin nhắn xong

// Lấy danh sách chát
        mData.child("PHONG_CHAT_CHUNG").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhsachchat.clear();
                Xoabottinnhan.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_phongchat trunggian = postSnapshot.getValue(Model_phongchat.class);
                    danhsachchat.add(new Model_phongchat(trunggian.getTen(), trunggian.getDangcap(), trunggian.getNoidung(), trunggian.getLinkanhdaidien(), trunggian.getId()));
                    Xoabottinnhan.add(postSnapshot.getKey());
                    Adapter_phongchat.notifyDataSetChanged();
                    Recy_noidungchat_phongchat.smoothScrollToPosition(danhsachchat.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// lấy danh sách chát xong

// Lấy thông tin người dùng

        if(User != null){
            mData.child("USERS").child(User.getUid()).child("ten").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ten = snapshot.getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(User.getUid()).child("anhdaidien").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    linkanh = snapshot.getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(User.getUid()).child("dangcap").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dangcap = snapshot.getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

// Lấy thông tin người dùng xong

        Btn_gui_phongchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noidung = Edt_khungsoanthao_phongchat.getText().toString();
                if(noidung.length() == 0){

                }else {
                    if(User == null){
                        Toast.makeText(getActivity(), "Bạn chưa đăng nhập nên chưa thể tham gia trò chuyện", Toast.LENGTH_LONG).show();
                        Edt_khungsoanthao_phongchat.setText("");
                    }else {
                        String key = mData.child("PHONG_CHAT_CHUNG").push().getKey();
                        mData.child("PHONG_CHAT_CHUNG").child(key).child("ten").setValue(ten);
                        mData.child("PHONG_CHAT_CHUNG").child(key).child("dangcap").setValue(dangcap);
                        mData.child("PHONG_CHAT_CHUNG").child(key).child("noidung").setValue(noidung);
                        mData.child("PHONG_CHAT_CHUNG").child(key).child("linkanhdaidien").setValue(linkanh);
                        mData.child("PHONG_CHAT_CHUNG").child(key).child("id").setValue(User.getUid());
                        Edt_khungsoanthao_phongchat.setText("");

                        if(Xoabottinnhan.size() >= 35 ){
                            mData.child("PHONG_CHAT_CHUNG").child(Xoabottinnhan.get(0)).removeValue();
                            mData.child("PHONG_CHAT_CHUNG").child(Xoabottinnhan.get(1)).removeValue();
                            mData.child("PHONG_CHAT_CHUNG").child(Xoabottinnhan.get(2)).removeValue();
                            mData.child("PHONG_CHAT_CHUNG").child(Xoabottinnhan.get(3)).removeValue();
                            mData.child("PHONG_CHAT_CHUNG").child(Xoabottinnhan.get(4)).removeValue();
                        }

                    }
                }
            }
        });

    }

    @Override
    public void onItemClick(Model_phongchat itemclick) {
        Intent intent = new Intent(getContext(), Ho_so.class);
        intent.putExtra("guiidnguoichat", itemclick.getId());
        intent.putExtra("thongbaoquaylai", "phongchat");
        startActivity(intent);
    }
}