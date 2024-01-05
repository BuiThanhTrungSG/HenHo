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
import com.love.henho.Model.Model_nguoiduocthich;
import com.love.henho.Model.OnItemClickListener_nguoiduoccauhon;
import com.love.henho.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Nguoiduoccauhon_adapter extends RecyclerView.Adapter<Nguoiduoccauhon_adapter.ViewHolder> {

    Context context;
    ArrayList<Model_nguoiduocthich> dsnguoiduoccauhon_Adapter;
    OnItemClickListener_nguoiduoccauhon Listener;

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;

    public Nguoiduoccauhon_adapter(Context context, ArrayList<Model_nguoiduocthich> dsnguoiduoccauhon_Adapter, OnItemClickListener_nguoiduoccauhon listener) {
        this.context = context;
        this.dsnguoiduoccauhon_Adapter = dsnguoiduoccauhon_Adapter;
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_nguoiduoccauhon,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        mAuth = FirebaseAuth.getInstance();
        USER = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();

        holder.Txt_loicauhon_nguoiduoccauhon.setText("Lời nhắn: " + dsnguoiduoccauhon_Adapter.get(position).getLoicauhon());
        holder.Txt_sinhle_nguoiduoccauhon.setText("Quà tặng: " + dsnguoiduoccauhon_Adapter.get(position).getSovangsinhle().toString() + " thỏi vàng");
        holder.Txt_ngaygui_nguoiduoccauhon.setText("Ngày gửi: " + dsnguoiduoccauhon_Adapter.get(position).getNgaygui()%100 + "/" + dsnguoiduoccauhon_Adapter.get(position).getNgaygui()%10000/100 + "/" + dsnguoiduoccauhon_Adapter.get(position).getNgaygui()/10000);
        holder.itemclick = dsnguoiduoccauhon_Adapter.get(position);

        if(dsnguoiduoccauhon_Adapter.get(position).getId_nguoigui() != null) {

            mData.child("USERS").child(dsnguoiduoccauhon_Adapter.get(position).getId_nguoigui()).child("ten").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        holder.Txt_ten_nguoiduoccauhon.setText(snapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(dsnguoiduoccauhon_Adapter.get(position).getId_nguoigui()).child("anhdaidien").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        Picasso.get().load(snapshot.getValue().toString()).into(holder.Img_hinh_nguoiduoccauhon);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(dsnguoiduoccauhon_Adapter.get(position).getId_nguoigui()).child("dangcap").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {

                        holder.Txt_dangcap_nguoiduoccauhon.setText(snapshot.getValue().toString());
                        // Đổi màu đẳng cấp
                        if (snapshot.getValue().toString().equals("Khá giả")) {
                            holder.Txt_dangcap_nguoiduoccauhon.setTextColor(Color.parseColor("#04B1FF"));
                        } else if (snapshot.getValue().toString().equals("Giàu có")) {
                            holder.Txt_dangcap_nguoiduoccauhon.setTextColor(Color.parseColor("#EB08FB"));
                        } else if (snapshot.getValue().toString().equals("Đại gia")) {
                            holder.Txt_dangcap_nguoiduoccauhon.setTextColor(Color.parseColor("#E91E63"));
                        } else {
                            holder.Txt_dangcap_nguoiduoccauhon.setTextColor(Color.parseColor("#4CAF50"));
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
        return dsnguoiduoccauhon_Adapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Img_hinh_nguoiduoccauhon;
        TextView Txt_ten_nguoiduoccauhon, Txt_dangcap_nguoiduoccauhon, Txt_loicauhon_nguoiduoccauhon, Txt_sinhle_nguoiduoccauhon, Txt_ngaygui_nguoiduoccauhon;
        Model_nguoiduocthich itemclick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img_hinh_nguoiduoccauhon = itemView.findViewById(R.id.Img_hinh_nguoiduoccauhon);
            Txt_ten_nguoiduoccauhon = itemView.findViewById(R.id.Txt_ten_nguoiduoccauhon);
            Txt_dangcap_nguoiduoccauhon = itemView.findViewById(R.id.Txt_dangcap_nguoiduoccauhon);
            Txt_loicauhon_nguoiduoccauhon = itemView.findViewById(R.id.Txt_loicauhon_nguoiduoccauhon);
            Txt_sinhle_nguoiduoccauhon = itemView.findViewById(R.id.Txt_sinhle_nguoiduoccauhon);
            Txt_ngaygui_nguoiduoccauhon = itemView.findViewById(R.id.Txt_ngaygui_nguoiduoccauhon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.onItemClick(itemclick);
                }
            });
        }
    }
}
