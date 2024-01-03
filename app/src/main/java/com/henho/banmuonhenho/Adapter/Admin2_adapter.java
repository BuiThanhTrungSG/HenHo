package com.henho.banmuonhenho.Adapter;

import android.content.Context;
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
import com.henho.banmuonhenho.Model.Model_duyetanh;
import com.henho.banmuonhenho.Model.OnItemClickListener_admin2;
import com.henho.banmuonhenho.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Admin2_adapter extends RecyclerView.Adapter<Admin2_adapter.ViewHolder> {

    FirebaseUser User;
    DatabaseReference mData;
    FirebaseAuth mAuth;

    Context context;
    ArrayList<Model_duyetanh> danhsachtrong_adapter_admin2;
    public OnItemClickListener_admin2 Listener;

    public Admin2_adapter(Context context, ArrayList<Model_duyetanh> danhsachtrong_adapter_admin2, OnItemClickListener_admin2 listener) {
        this.context = context;
        this.danhsachtrong_adapter_admin2 = danhsachtrong_adapter_admin2;
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_admin2,parent,false);
        return new Admin2_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();

        Picasso.get().load(danhsachtrong_adapter_admin2.get(position).getLinkanhchoduyet()).into(holder.Img_anhchoduyet);

        if(danhsachtrong_adapter_admin2.get(position).getIdchuanh() != null) {
            mData.child("USERS").child(danhsachtrong_adapter_admin2.get(position).getIdchuanh()).child("ten").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.Txt_ten_admin2.setText(snapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        // Bổ sung cho việc tạo sự kiện Itemclick
        holder.itemclick = danhsachtrong_adapter_admin2.get(position);

    }

    @Override
    public int getItemCount() {
        return danhsachtrong_adapter_admin2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Img_anhchoduyet;
        TextView Txt_ten_admin2;
        // Bổ sung cho việc tạo sự kiện ItemClick
        Model_duyetanh itemclick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img_anhchoduyet = itemView.findViewById(R.id.Img_anhchoduyet);
            Txt_ten_admin2 = itemView.findViewById(R.id.Txt_ten_admin2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.onItemClick(itemclick);
                }
            });
        }
    }
}
