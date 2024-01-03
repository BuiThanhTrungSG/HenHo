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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.henho.banmuonhenho.Model.Model_ten_anh_gioitinh;
import com.henho.banmuonhenho.Model.Model_timquanhday;
import com.henho.banmuonhenho.Model.OnItemClickListener_timquanhday;
import com.henho.banmuonhenho.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Timquanhday_adapter extends RecyclerView.Adapter<Timquanhday_adapter.ViewHolder> {

    Context context;
    ArrayList<Model_timquanhday> danhsach;
    public OnItemClickListener_timquanhday Listener;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    FirebaseFirestore db;

    public Timquanhday_adapter(Context context, ArrayList<Model_timquanhday> danhsach, OnItemClickListener_timquanhday listener) {
        this.context = context;
        this.danhsach = danhsach;
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_nguoiquanhday,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if(danhsach.get(position).getId() != null) {

            Source source = Source.CACHE;
            db.collection("USER").document(danhsach.get(position).getId()).get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Model_ten_anh_gioitinh trunggian = documentSnapshot.toObject(Model_ten_anh_gioitinh.class);
                    if(trunggian != null){
                        holder.Txt_ten_quanhday.setText(trunggian.getTen());
                        Picasso.get().load(trunggian.getAnhdaidien()).into(holder.Img_avata_quanhday);
                        if (trunggian.getGioitinh().equals("Nữ") || trunggian.getGioitinh().equals("Nam")) {
                            if (trunggian.getGioitinh().equals("Nữ")) {
                                holder.Constran_nendanhsach_quanhday.setBackgroundColor(Color.parseColor("#F5FBECF1"));
                            } else {
                                holder.Constran_nendanhsach_quanhday.setBackgroundColor(Color.parseColor("#F5ECFAFB"));
                            }
                        } else {
                            holder.Constran_nendanhsach_quanhday.setBackgroundColor(Color.parseColor("#F5FBFBEC"));
                        }
                    }
                }
            });

//            mData.child("USERS").child(danhsach.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Model_timquanhday_thongtinnguoi trunggian = snapshot.getValue(Model_timquanhday_thongtinnguoi.class);
//                        if (trunggian.getGioitinh() != null && trunggian.getAnhdaidien() != null && trunggian.getTen() != null) {
//                            holder.Txt_ten_quanhday.setText(trunggian.getTen());
//                            Picasso.get().load(trunggian.getAnhdaidien()).into(holder.Img_avata_quanhday);
//                            if (trunggian.getGioitinh().equals("Nữ") || trunggian.getGioitinh().equals("Nam")) {
//                                if (trunggian.getGioitinh().equals("Nữ")) {
//                                    holder.Constran_nendanhsach_quanhday.setBackgroundColor(Color.parseColor("#F5FBECF1"));
//                                } else {
//                                    holder.Constran_nendanhsach_quanhday.setBackgroundColor(Color.parseColor("#F5ECFAFB"));
//                                }
//                            } else {
//                                holder.Constran_nendanhsach_quanhday.setBackgroundColor(Color.parseColor("#F5FBFBEC"));
//                            }
//                    }else {
//                        Toast.makeText(context, danhsach.get(position).getId(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }

        int dai = danhsach.get(position).getKhoangcach().length();
        String truocphay = danhsach.get(position).getKhoangcach().substring(0, dai-3);
        if(truocphay.length() == 0){
            truocphay = "0";
        }
        String sauphay = danhsach.get(position).getKhoangcach().substring(dai - 3, dai-2);

        holder.Txt_khoangcach_quanhday.setText(truocphay + "," + sauphay + " km");

//        holder.Txt_khoangcach_quanhday.setText(danhsach.get(position).getKhoangcach() + " km");

        holder.itemclick = danhsach.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return danhsach.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Txt_ten_quanhday, Txt_khoangcach_quanhday;
        ImageView Img_avata_quanhday;
        String itemclick;
        ConstraintLayout Constran_nendanhsach_quanhday;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Txt_ten_quanhday = itemView.findViewById(R.id.Txt_ten_quanhday);
            Txt_khoangcach_quanhday = itemView.findViewById(R.id.Txt_khoangcach_quanhday);
            Img_avata_quanhday = itemView.findViewById(R.id.Img_avata_quanhday);
            Constran_nendanhsach_quanhday = itemView.findViewById(R.id.Constran_nendanhsach_quanhday);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.onItemClick(itemclick);
                }
            });

        }
    }
}
