package com.henho.banmuonhenho.Adapter;

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
import com.henho.banmuonhenho.Model.Model_nguoinhanloi;
import com.henho.banmuonhenho.Model.OnItemClickListener_nguoinhanloi;
import com.henho.banmuonhenho.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Danhsachnhanloi_adapter extends RecyclerView.Adapter<Danhsachnhanloi_adapter.ViewHolder> {

    Context context;
    ArrayList<Model_nguoinhanloi> danhsach;
    OnItemClickListener_nguoinhanloi Listener;

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;

    public Danhsachnhanloi_adapter(Context context, ArrayList<Model_nguoinhanloi> danhsach, OnItemClickListener_nguoinhanloi listener) {
        this.context = context;
        this.danhsach = danhsach;
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_nguoinhanloi,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();

        holder.Txt_loinhan_dsnl.setText("Lời nhắn: " + danhsach.get(position).getLoinhan());
        holder.itemclick = danhsach.get(position).getId();

        if(danhsach.get(position).getId() != null) {
            mData.child("USERS").child(danhsach.get(position).getId()).child("ten").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        holder.Txt_ten_hinhdsnl.setText(snapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(danhsach.get(position).getId()).child("anhdaidien").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        Picasso.get().load(snapshot.getValue().toString()).into(holder.Img_avata_hinhdsnl);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(danhsach.get(position).getId()).child("dangcap").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        holder.Txt_dangcap_hinhdsnl.setText(snapshot.getValue().toString());

                        // Đổi màu đẳng cấp
                        if (holder.Txt_dangcap_hinhdsnl.getText().equals("Khá giả")) {
                            holder.Txt_dangcap_hinhdsnl.setTextColor(Color.parseColor("#04B1FF"));
                        } else if (holder.Txt_dangcap_hinhdsnl.getText().equals("Giàu có")) {
                            holder.Txt_dangcap_hinhdsnl.setTextColor(Color.parseColor("#EB08FB"));
                        } else if (holder.Txt_dangcap_hinhdsnl.getText().equals("Đại gia")) {
                            holder.Txt_dangcap_hinhdsnl.setTextColor(Color.parseColor("#E91E63"));
                        } else {
                            holder.Txt_dangcap_hinhdsnl.setTextColor(Color.parseColor("#4CAF50"));
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
        return danhsach.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Txt_ten_hinhdsnl, Txt_dangcap_hinhdsnl, Txt_loinhan_dsnl;
        ImageView Img_avata_hinhdsnl;
        String itemclick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Img_avata_hinhdsnl = itemView.findViewById(R.id.Img_avata_hinhdsnl);
            Txt_ten_hinhdsnl = itemView.findViewById(R.id.Txt_ten_hinhdsnl);
            Txt_dangcap_hinhdsnl = itemView.findViewById(R.id.Txt_dangcap_hinhdsnl);
            Txt_loinhan_dsnl = itemView.findViewById(R.id.Txt_loinhan_dsnl);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.onItemClick(itemclick);
                }
            });

        }
    }
}
