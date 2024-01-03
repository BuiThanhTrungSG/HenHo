package com.henho.banmuonhenho.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.henho.banmuonhenho.Model.Model_noidungtinnhan_less;
import com.henho.banmuonhenho.R;

import java.util.ArrayList;

public class Noidungtinnhan_adapter extends RecyclerView.Adapter<Noidungtinnhan_adapter.ViewHolder>{

    FirebaseUser User;
    DatabaseReference mData;
    FirebaseAuth mAuth;

    Context context;
    ArrayList<Model_noidungtinnhan_less> noidungtinnhan_trongAdapter;

    public Noidungtinnhan_adapter(Context context, ArrayList<Model_noidungtinnhan_less> noidungtinnhan_trongAdapter) {
        this.context = context;
        this.noidungtinnhan_trongAdapter = noidungtinnhan_trongAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_noidungtinnhan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();

        String nguoigui = noidungtinnhan_trongAdapter.get(position).getNguoigui();
        if(nguoigui.equals(User.getUid())){
            holder.phantinnhan.setGravity(Gravity.RIGHT);
            holder.noidungtin_tinnhan.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.noidungtin_tinnhan.setText(noidungtinnhan_trongAdapter.get(position).getNoidung());

        }else {
            holder.phantinnhan.setGravity(Gravity.LEFT);
            holder.noidungtin_tinnhan.setBackgroundColor(Color.parseColor("#FFF1FA"));
            holder.noidungtin_tinnhan.setText(noidungtinnhan_trongAdapter.get(position).getNoidung());

        }

    }

    @Override
    public int getItemCount() {
        return noidungtinnhan_trongAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView noidungtin_tinnhan;
        LinearLayout phantinnhan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noidungtin_tinnhan = itemView.findViewById(R.id.noidungtin_nhantin);
            phantinnhan = itemView.findViewById(R.id.phantinnhan);
        }
    }
}
