package com.love.henho.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.love.henho.Model.Model_nguoithich;
import com.love.henho.Model.OnItemClickListener_nguoicauhon;
import com.love.henho.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Nguoicauhon_adapter extends RecyclerView.Adapter<Nguoicauhon_adapter.ViewHolder> {

    Context context;
    ArrayList<Model_nguoithich> dsnguoicauhontrongAdapter;
    OnItemClickListener_nguoicauhon Listener;

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;

    public Nguoicauhon_adapter(Context context, ArrayList<Model_nguoithich> dsnguoicauhontrongAdapter, OnItemClickListener_nguoicauhon listener) {
        this.context = context;
        this.dsnguoicauhontrongAdapter = dsnguoicauhontrongAdapter;
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_nguoicauhon,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();

        holder.Txt_sinhle_nguoicauhon.setText("Quà tặng: " + dsnguoicauhontrongAdapter.get(position).getSovangsinhle().toString() + " thỏi vàng");
        holder.Txt_loicauhon_nguoicauhon.setText("Lời nhắn: " + dsnguoicauhontrongAdapter.get(position).getLoicauhon());
        holder.itemclick = dsnguoicauhontrongAdapter.get(position);

        if(dsnguoicauhontrongAdapter.get(position).getId_nguoigui() != null) {

            mData.child("USERS").child(dsnguoicauhontrongAdapter.get(position).getId_nguoigui()).child("ten").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.getValue() != null) {
                       holder.Txt_ten_nguoicauhon.setText(snapshot.getValue().toString());
                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(dsnguoicauhontrongAdapter.get(position).getId_nguoigui()).child("anhdaidien").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        Picasso.get().load(snapshot.getValue().toString()).into(holder.Img_anh_nguoicauhon);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(dsnguoicauhontrongAdapter.get(position).getId_nguoigui()).child("dangcap").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        holder.Txt_dangcap_nguoicauhon.setText(snapshot.getValue().toString());
                        // Đổi màu đẳng cấp
                        if (snapshot.getValue().toString().equals("Khá giả")) {
                            holder.Txt_dangcap_nguoicauhon.setTextColor(Color.parseColor("#04B1FF"));
                        } else if (snapshot.getValue().toString().equals("Giàu có")) {
                            holder.Txt_dangcap_nguoicauhon.setTextColor(Color.parseColor("#EB08FB"));
                        } else if (snapshot.getValue().toString().equals("Đại gia")) {
                            holder.Txt_dangcap_nguoicauhon.setTextColor(Color.parseColor("#E91E63"));
                        } else {
                            holder.Txt_dangcap_nguoicauhon.setTextColor(Color.parseColor("#4CAF50"));
                        }
                        // Đổi màu đẳng cấp xong
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
        return dsnguoicauhontrongAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Img_anh_nguoicauhon;
        TextView Txt_ten_nguoicauhon, Txt_dangcap_nguoicauhon, Txt_sinhle_nguoicauhon, Txt_loicauhon_nguoicauhon;
        Model_nguoithich itemclick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img_anh_nguoicauhon = itemView.findViewById(R.id.Img_anh_nguoicauhon);
            Txt_ten_nguoicauhon = itemView.findViewById(R.id.Txt_ten_nguoicauhon);
            Txt_dangcap_nguoicauhon = itemView.findViewById(R.id.Txt_dangcap_nguoicauhon);
            Txt_sinhle_nguoicauhon = itemView.findViewById(R.id.Txt_sinhle_nguoicauhon);
            Txt_loicauhon_nguoicauhon = itemView.findViewById(R.id.Txt_loicauhon_nguoicauhon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.onItemClick(itemclick);
                }
            });
        }
    }
}
