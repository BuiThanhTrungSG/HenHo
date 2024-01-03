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
import com.henho.banmuonhenho.Model.Model_anh_gioitinh;
import com.henho.banmuonhenho.Model.Model_thuemuon_danhsach;
import com.henho.banmuonhenho.Model.OnItemClickListener_trangchu;
import com.henho.banmuonhenho.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Thuemuon_adapter extends RecyclerView.Adapter<Thuemuon_adapter.ViewHolder_thuemuon> {

    ArrayList<Model_thuemuon_danhsach> Danhsach;
    Context context;
    public OnItemClickListener_trangchu Listener;
    FirebaseAuth mAuth;
    FirebaseUser User;
    DatabaseReference mData;
    FirebaseFirestore db;

    public Thuemuon_adapter(ArrayList<Model_thuemuon_danhsach> danhsach, Context context, OnItemClickListener_trangchu listener) {
        Danhsach = danhsach;
        this.context = context;
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder_thuemuon onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_nguoithuemuon,parent,false);
        return new ViewHolder_thuemuon(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder_thuemuon holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        User = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        holder.itemclick = Danhsach.get(position).getId();

        if(Danhsach.get(position).getId() != null) {

            Source source = Source.CACHE;
            db.collection("USER").document(Danhsach.get(position).getId()).get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Model_anh_gioitinh layve = documentSnapshot.toObject(Model_anh_gioitinh.class);
                    if (layve != null){
                        Picasso.get().load(layve.getAnhdaidien()).into(holder.Img_avata_hinhnguoithuemuon);
//                        if (layve.getGioitinh().equals("Nữ") || layve.getGioitinh().equals("Nam")) {
//                            if (layve.getGioitinh().equals("Nữ")) {
//                                holder.tong_hinhnguoithuemuon.setBackgroundColor(Color.parseColor("#F5FBECF1"));
//                            } else {
//                                holder.tong_hinhnguoithuemuon.setBackgroundColor(Color.parseColor("#F5ECFAFB"));
//                            }
//
//                        } else {
//                            holder.tong_hinhnguoithuemuon.setBackgroundColor(Color.parseColor("#8DFFFAD2"));
//                        }
                    }
                }
            });

//            mData.child("USERS").child(Danhsach.get(position).getId()).child("anhdaidien").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.getValue() != null) {
//                        Picasso.get().load(snapshot.getValue().toString()).into(holder.Img_avata_hinhnguoithuemuon);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//            mData.child("USERS").child(Danhsach.get(position).getId()).child("gioitinh").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.getValue() != null) {
//                        if (snapshot.getValue().toString().equals("Nữ") || snapshot.getValue().toString().equals("Nam")) {
//                            if (snapshot.getValue().toString().equals("Nữ")) {
//                                holder.tong_hinhnguoithuemuon.setBackgroundColor(Color.parseColor("#F5FBECF1"));
//                            } else {
//                                holder.tong_hinhnguoithuemuon.setBackgroundColor(Color.parseColor("#F5ECFAFB"));
//                            }
//
//                        } else {
//                            holder.tong_hinhnguoithuemuon.setBackgroundColor(Color.parseColor("#8DFFFAD2"));
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

            mData.child("USERS").child(Danhsach.get(position).getId()).child("sovangdangco").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!= null){

                        Integer sovang = Integer.parseInt(snapshot.getValue().toString());

                        if (sovang < 100) {
                            holder.Img_sao_hinhnguoithuemuon.setImageResource(R.drawable.icon_sao0);
//                            holder.Img_sao_hinhnguoithuemuon.setVisibility(View.VISIBLE);
                        }
                        if (sovang >= 100 && sovang < 300) {
                            holder.Img_sao_hinhnguoithuemuon.setImageResource(R.drawable.icon_sao1);
//                            holder.Img_sao_hinhnguoithuemuon.setVisibility(View.VISIBLE);

                        }
                        if (sovang >= 300 && sovang < 1000) {
                            holder.Img_sao_hinhnguoithuemuon.setVisibility(View.VISIBLE);
//                            holder.Img_sao_hinhnguoithuemuon.setImageResource(R.drawable.icon_sao2);
                        }
                        if (sovang >= 1000) {
                            holder.Img_sao_hinhnguoithuemuon.setVisibility(View.VISIBLE);
//                            holder.Img_sao_hinhnguoithuemuon.setImageResource(R.drawable.icon_sao3);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        holder.Txt_canthue_hinhnguoithuemuon.setText(Danhsach.get(position).getCanthue());

        if(Danhsach.get(position).getCanthue().equals("Thuê bạn trai") || Danhsach.get(position).getCanthue().equals("Nhận làm bạn gái") ){
            holder.Txt_canthue_hinhnguoithuemuon.setTextColor(Color.parseColor("#FFF63A00"));
        }
        if(Danhsach.get(position).getCanthue().equals("Thuê bạn gái") || Danhsach.get(position).getCanthue().equals("Nhận làm bạn trai") ){
            holder.Txt_canthue_hinhnguoithuemuon.setTextColor(Color.parseColor("#FF008D9F"));
        }
    }

    @Override
    public int getItemCount() {
        return Danhsach.size();
    }

    public class ViewHolder_thuemuon extends RecyclerView.ViewHolder {

        ImageView Img_avata_hinhnguoithuemuon, Img_sao_hinhnguoithuemuon;
        TextView Txt_canthue_hinhnguoithuemuon;
        String itemclick;
//        ConstraintLayout tong_hinhnguoithuemuon;

        public ViewHolder_thuemuon(@NonNull View itemView) {
            super(itemView);
            Txt_canthue_hinhnguoithuemuon = itemView.findViewById(R.id.Txt_canthue_hinhnguoithuemuon);
            Img_avata_hinhnguoithuemuon = itemView.findViewById(R.id.Img_avata_hinhnguoithuemuon);
            Img_sao_hinhnguoithuemuon = itemView.findViewById(R.id.Img_sao_hinhnguoithuemuon);
//            tong_hinhnguoithuemuon = itemView.findViewById(R.id.tong_hinhnguoithuemuon);
            // Tạo sự kiện ItemClick cho Recyclerview, có thê thay itemView bằng các View con để bắt sự kiện cho View con
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.onItemClick(itemclick);
                }
            });
// Tạo xong sự kiện ItemClick cho Recyclerview
        }
    }
}
