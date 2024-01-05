package com.love.henho.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.love.henho.Model.Model_danhsachnguoinhantin_xoa;
import com.love.henho.Model.OnItemClickListener;
import com.love.henho.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Danhsachnguoinhantin_adapter extends RecyclerView.Adapter<Danhsachnguoinhantin_adapter.ViewHolder> {

    Context context;
    ArrayList<Model_danhsachnguoinhantin_xoa> danhsachnguoinhantintrongAdapter;
    FirebaseUser User;
    DatabaseReference mData;
    FirebaseAuth mAuth;
    public OnItemClickListener Listener, Listener_long, Listener_check;

    public Danhsachnguoinhantin_adapter(Context context, ArrayList<Model_danhsachnguoinhantin_xoa> danhsachnguoinhantintrongAdapter, OnItemClickListener listener, OnItemClickListener listener_long, OnItemClickListener listener_check) {
        this.context = context;
        this.danhsachnguoinhantintrongAdapter = danhsachnguoinhantintrongAdapter;
        Listener = listener;
        Listener_long = listener_long;
        Listener_check = listener_check;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_danhsachnguoinhantin,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();

        if(danhsachnguoinhantintrongAdapter.get(position).getIdnguoichat() != null) {

            mData.child("USERS").child(danhsachnguoinhantintrongAdapter.get(position).getIdnguoichat()).child("ten").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        holder.nguoigui_tinnhan.setText(snapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            mData.child("USERS").child(danhsachnguoinhantintrongAdapter.get(position).getIdnguoichat()).child("dangcap").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {

                        holder.dangcap_tinnhan.setText(snapshot.getValue().toString());
                        // Đổi màu đẳng cấp
                        if (snapshot.getValue().equals("Khá giả")) {
                            holder.dangcap_tinnhan.setTextColor(Color.parseColor("#04B1FF"));
                        } else if (snapshot.getValue().equals("Giàu có")) {
                            holder.dangcap_tinnhan.setTextColor(Color.parseColor("#EB08FB"));
                        } else if (snapshot.getValue().equals("Đại gia")) {
                            holder.dangcap_tinnhan.setTextColor(Color.parseColor("#E91E63"));
                        } else {
                            holder.dangcap_tinnhan.setTextColor(Color.parseColor("#4CAF50"));
                        }
                        // Đổi màu đẳng cấp xong
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(danhsachnguoinhantintrongAdapter.get(position).getIdnguoichat()).child("anhdaidien").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        Picasso.get().load(snapshot.getValue().toString()).into(holder.avata_tinnhan);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (danhsachnguoinhantintrongAdapter.get(position).getTrangthai() != null) {
                if (danhsachnguoinhantintrongAdapter.get(position).getTrangthai()) {
                    holder.nendanhsachtinnhan.setBackgroundColor(Color.parseColor("#6D79EDFF"));
                } else {
                    holder.nendanhsachtinnhan.setBackgroundColor(Color.parseColor("#00FA6698"));
                }
            }

        }

        // Bổ sung cho việc tạo sự kiện Itemclick
        holder.itemclick = danhsachnguoinhantintrongAdapter.get(position);

        if(danhsachnguoinhantintrongAdapter.get(position).getTrangthaixoa() == 1){
            holder.checkb_tinnhan.setVisibility(View.INVISIBLE);
        }else if (danhsachnguoinhantintrongAdapter.get(position).getTrangthaixoa() == 2){
            holder.checkb_tinnhan.setVisibility(View.VISIBLE);
            holder.checkb_tinnhan.setChecked(false);
        }else if (danhsachnguoinhantintrongAdapter.get(position).getTrangthaixoa() == 3) {
            holder.checkb_tinnhan.setVisibility(View.VISIBLE);
            holder.checkb_tinnhan.setChecked(true);
        }else if(danhsachnguoinhantintrongAdapter.get(position).getTrangthaixoa() == 4){

        }

    }

    @Override
    public int getItemCount() {
        return danhsachnguoinhantintrongAdapter.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avata_tinnhan;
        TextView nguoigui_tinnhan;
        TextView dangcap_tinnhan;
        ConstraintLayout nendanhsachtinnhan;
        CheckBox checkb_tinnhan;
        // Bổ sung cho việc tạo sự kiện ItemClick
        Model_danhsachnguoinhantin_xoa itemclick;
        Boolean check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avata_tinnhan = itemView.findViewById(R.id.avata_tinnhan);
            nguoigui_tinnhan = itemView.findViewById(R.id.nguoigui_tinnhan);
            dangcap_tinnhan = itemView.findViewById(R.id.dangcap_tinnhan);
            nendanhsachtinnhan = itemView.findViewById(R.id.nendanhsachtinnhan);
            checkb_tinnhan = itemView.findViewById(R.id.checkb_tinnhan);
// Tạo sự kiện ItemClick cho Recyclerview, có thê thay itemView bằng các View con để bắt sự kiện cho View con
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.onItemClick(itemclick);
                }
            });

            checkb_tinnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Listener_check.onItemClick_xoa(itemclick);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Listener_long.onItemClick_long(itemclick);
                    return false;
                }
            });


// Tạo xong sự kiện ItemClick cho Recyclerview
        }
    }
}
