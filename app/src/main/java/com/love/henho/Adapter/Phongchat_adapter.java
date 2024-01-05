package com.love.henho.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.love.henho.Model.Model_phongchat;
import com.love.henho.Model.OnItemClickListener_phongchat;
import com.love.henho.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Phongchat_adapter extends RecyclerView.Adapter<Phongchat_adapter.ViewHolder_phongchat> {

    Context context;
    ArrayList<Model_phongchat> noidung_trongAdapter;
    OnItemClickListener_phongchat Listener;

    FirebaseUser User;
    DatabaseReference mData;
    FirebaseAuth mAuth;

    public Phongchat_adapter(Context context, ArrayList<Model_phongchat> noidung_trongAdapter, OnItemClickListener_phongchat listener) {
        this.context = context;
        this.noidung_trongAdapter = noidung_trongAdapter;
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder_phongchat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_noidungtinnhan_phongchat,parent,false);
        return new ViewHolder_phongchat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder_phongchat holder, int position) {

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();

        holder.Txt_ten_phongchat.setText(noidung_trongAdapter.get(position).getTen());
        holder.Txt_dangcap_phongchat.setText(noidung_trongAdapter.get(position).getDangcap());
        holder.Txt_noidungtinnhan_phongchat.setText(noidung_trongAdapter.get(position).getNoidung());
        Picasso.get().load(noidung_trongAdapter.get(position).getLinkanhdaidien()).into(holder.Img_anhdaidien_phongchat);
        holder.itemclick = noidung_trongAdapter.get(position);

        // Đổi màu đẳng cấp
        if(noidung_trongAdapter.get(position).getDangcap() != null) {
            if (noidung_trongAdapter.get(position).getDangcap().equals("Khá giả")) {
                holder.Txt_dangcap_phongchat.setTextColor(Color.parseColor("#04B1FF"));
            } else if (noidung_trongAdapter.get(position).getDangcap().equals("Giàu có")) {
                holder.Txt_dangcap_phongchat.setTextColor(Color.parseColor("#EB08FB"));
            } else if (noidung_trongAdapter.get(position).getDangcap().equals("Đại gia")) {
                holder.Txt_dangcap_phongchat.setTextColor(Color.parseColor("#E91E63"));
            } else {
                holder.Txt_dangcap_phongchat.setTextColor(Color.parseColor("#4CAF50"));
            }
        }
        // Đổi màu đẳng cấp xong
        if(noidung_trongAdapter.get(position).getId() != null) {
            mData.child("USERS").child(noidung_trongAdapter.get(position).getId()).child("gioitinh").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        String gioitinh = (String) snapshot.getValue();
                        if (gioitinh.equals("Nữ") || gioitinh.equals("Nam")) {
                            if (gioitinh.equals("Nữ")) {
                                holder.Txt_noidungtinnhan_phongchat.setBackgroundColor(Color.parseColor("#F5FBECF1"));
                            } else {
                                holder.Txt_noidungtinnhan_phongchat.setBackgroundColor(Color.parseColor("#F5ECFAFB"));
                            }

                        } else {
                            holder.Txt_noidungtinnhan_phongchat.setBackgroundColor(Color.parseColor("#F5FBFBEC"));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return noidung_trongAdapter.size();
    }

    public class ViewHolder_phongchat extends RecyclerView.ViewHolder {

        TextView Txt_ten_phongchat, Txt_dangcap_phongchat, Txt_noidungtinnhan_phongchat;
        ImageView Img_anhdaidien_phongchat;
        Model_phongchat itemclick;

        public ViewHolder_phongchat(@NonNull View itemView) {
            super(itemView);

            Txt_ten_phongchat = itemView.findViewById(R.id.Txt_ten_phongchat);
            Txt_dangcap_phongchat = itemView.findViewById(R.id.Txt_dangcap_phongchat);
            Txt_noidungtinnhan_phongchat = itemView.findViewById(R.id.Txt_noidungtinnhan_phongchat);
            Img_anhdaidien_phongchat = itemView.findViewById(R.id.Img_anhdaidien_phongchat);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.onItemClick(itemclick);
                }
            });

        }
    }
}
