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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.love.henho.Model.Model_hengap_danhsach;
import com.love.henho.Model.Model_ten_anh_gioitinh;
import com.love.henho.Model.OnItemClickListener_hengap;
import com.love.henho.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Hengap_adapter extends RecyclerView.Adapter<Hengap_adapter.ViewHolder> {

    Context context;
    ArrayList<Model_hengap_danhsach> danhsach;
    OnItemClickListener_hengap Listener;

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    FirebaseFirestore db;

    public Hengap_adapter(Context context, ArrayList<Model_hengap_danhsach> danhsach, OnItemClickListener_hengap listener) {
        this.context = context;
        this.danhsach = danhsach;
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_nguoihengap,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        mAuth = FirebaseAuth.getInstance();
        USER = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        holder.Txt_hengi_hinhhengap.setText("Mời " + danhsach.get(position).getHengi());
        holder.itemclick = danhsach.get(position).getId();

        if(danhsach.get(position).getId() != null) {

            Source source = Source.CACHE;
            db.collection("USER").document(danhsach.get(position).getId()).get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Model_ten_anh_gioitinh layve = documentSnapshot.toObject(Model_ten_anh_gioitinh.class);
                    if(layve != null){
                        holder.Txt_ten_hinhhengap.setText(layve.getTen());
                        Picasso.get().load(layve.getAnhdaidien()).into(holder.Img_avata_hinhhengap);
                        if (layve.getGioitinh().equals("Nữ") || layve.getGioitinh().equals("Nam")) {
                            if (layve.getGioitinh().equals("Nữ")) {
                                holder.Txt_ten_hinhhengap.setTextColor(Color.parseColor("#FF1F6B"));
                                holder.Img_gioitinh_hinhhengap.setImageResource(R.drawable.icon_nu);
                            } else {
                                holder.Txt_ten_hinhhengap.setTextColor(Color.parseColor("#0492A5"));
                                holder.Img_gioitinh_hinhhengap.setImageResource(R.drawable.icon_nam);
                            }

                        } else {
                            holder.Txt_ten_hinhhengap.setTextColor(Color.parseColor("#FFE500"));
                            holder.Img_gioitinh_hinhhengap.setImageResource(R.drawable.icon_dongtinh);

                        }
                    }
                }
            });

//            mData.child("USERS").child(danhsach.get(position).getId()).child("ten").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.getValue() != null) {
//                        holder.Txt_ten_hinhhengap.setText(snapshot.getValue().toString());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//            mData.child("USERS").child(danhsach.get(position).getId()).child("anhdaidien").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.getValue() != null) {
//                        Picasso.get().load(snapshot.getValue().toString()).into(holder.Img_avata_hinhhengap);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//            mData.child("USERS").child(danhsach.get(position).getId()).child("gioitinh").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.getValue() != null) {
//                        if (snapshot.getValue().toString().equals("Nữ") || snapshot.getValue().toString().equals("Nam")) {
//                            if (snapshot.getValue().toString().equals("Nữ")) {
//                                holder.Txt_ten_hinhhengap.setTextColor(Color.parseColor("#FF1F6B"));
//                                holder.Img_gioitinh_hinhhengap.setImageResource(R.drawable.icon_nu);
//                            } else {
//                                holder.Txt_ten_hinhhengap.setTextColor(Color.parseColor("#0492A5"));
//                                holder.Img_gioitinh_hinhhengap.setImageResource(R.drawable.icon_nam);
//                            }
//
//                        } else {
//                            holder.Txt_ten_hinhhengap.setTextColor(Color.parseColor("#FFE500"));
//                            holder.Img_gioitinh_hinhhengap.setImageResource(R.drawable.icon_dongtinh);
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

            mData.child("USERS").child(danhsach.get(position).getId()).child("sovangdangco").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!= null){

                        Integer sovang = Integer.parseInt(snapshot.getValue().toString());

                        if (sovang < 100) {
                            holder.Img_sao_hinhhengap.setImageResource(R.drawable.icon_sao0);
//                            holder.Img_sao_hinhhengap.setVisibility(View.GONE);
                        }
                        if (sovang >= 100 && sovang < 300) {
                            holder.Img_sao_hinhhengap.setImageResource(R.drawable.icon_sao1);
//                            holder.Img_sao_hinhhengap.setVisibility(View.VISIBLE);

                        }
                        if (sovang >= 300 && sovang < 1000) {
//                            holder.Img_sao_hinhhengap.setVisibility(View.VISIBLE);
                            holder.Img_sao_hinhhengap.setImageResource(R.drawable.icon_sao2);
                        }
                        if (sovang >= 1000) {
//                            holder.Img_sao_hinhhengap.setVisibility(View.VISIBLE);
                            holder.Img_sao_hinhhengap.setImageResource(R.drawable.icon_sao3);
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
        return danhsach.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Img_avata_hinhhengap, Img_gioitinh_hinhhengap, Img_sao_hinhhengap;
        TextView Txt_hengi_hinhhengap, Txt_ten_hinhhengap;
        String itemclick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img_avata_hinhhengap = itemView.findViewById(R.id.Img_avata_hinhhengap);
            Txt_ten_hinhhengap = itemView.findViewById(R.id.Txt_ten_hinhhengap);
            Txt_hengi_hinhhengap = itemView.findViewById(R.id.Txt_hengi_hinhhengap);
            Img_gioitinh_hinhhengap = itemView.findViewById(R.id.Img_gioitinh_hinhhengap);
            Img_sao_hinhhengap = itemView.findViewById(R.id.Img_sao_hinhhengap);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.onItemClick(itemclick);
                }
            });
        }
    }
}
