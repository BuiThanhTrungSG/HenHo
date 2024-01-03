package com.henho.banmuonhenho.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.henho.banmuonhenho.Model.Model_duyetnoidung_admin;
import com.henho.banmuonhenho.Model.OnItemClickListener_admin;
import com.henho.banmuonhenho.R;

import java.util.ArrayList;

public class Admin_adapter extends RecyclerView.Adapter<Admin_adapter.ViewHolder> {

    FirebaseUser User;
    DatabaseReference mData;
    FirebaseAuth mAuth;

    Context context;
    ArrayList<Model_duyetnoidung_admin> danhsachtrong_adapter_admin;
    public OnItemClickListener_admin Listener;

    public Admin_adapter(Context context, ArrayList<Model_duyetnoidung_admin> danhsachtrong_adapter_admin, OnItemClickListener_admin listener) {
        this.context = context;
        this.danhsachtrong_adapter_admin = danhsachtrong_adapter_admin;
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_admin,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();

        holder.Txt_ten_admin.setText(danhsachtrong_adapter_admin.get(position).getTen().toString());
        holder.Txt_ngaysua_admin.setText(danhsachtrong_adapter_admin.get(position).getNgaysua().toString());
        holder.Txt_gioithieu_admin.setText(danhsachtrong_adapter_admin.get(position).getNoidung().toString());
        holder.itemclick = danhsachtrong_adapter_admin.get(position);

    }

    @Override
    public int getItemCount() {
        return danhsachtrong_adapter_admin.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Txt_ten_admin, Txt_ngaysua_admin, Txt_gioithieu_admin;
        // Bổ sung cho việc tạo sự kiện ItemClick
        Model_duyetnoidung_admin itemclick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Txt_ten_admin = itemView.findViewById(R.id.Txt_ten_admin);
            Txt_ngaysua_admin = itemView.findViewById(R.id.Txt_ngaysua_admin);
            Txt_gioithieu_admin = itemView.findViewById(R.id.Txt_gioithieu_admin);
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
