package com.love.henho.Adapter;

import android.annotation.SuppressLint;
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

import com.love.henho.Model.Model_thanhvienmoidangnhap;
import com.love.henho.Model.OnItemClickListener_trangchu;
import com.love.henho.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Hoptuoi_adapter extends RecyclerView.Adapter<Hoptuoi_adapter.ViewHolder_thanhvienmoidangnhap> {

    ArrayList<Model_thanhvienmoidangnhap> DanhsachtrongAdapter;
    Context context;
    Integer sonam, sotuoi, congiap;
    public OnItemClickListener_trangchu Listener;

    public Hoptuoi_adapter(ArrayList<Model_thanhvienmoidangnhap> danhsachtrongAdapter, Context context, OnItemClickListener_trangchu listener) {
        this.DanhsachtrongAdapter = danhsachtrongAdapter;
        this.context = context;
        this.Listener = listener;
    }

    @NonNull
    @Override
    public Hoptuoi_adapter.ViewHolder_thanhvienmoidangnhap onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hinh_danhsachmoidangnhap,parent,false);
        return new Hoptuoi_adapter.ViewHolder_thanhvienmoidangnhap(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final Hoptuoi_adapter.ViewHolder_thanhvienmoidangnhap holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        holder.ten_dsdn.setText(DanhsachtrongAdapter.get(position).getTen());
        holder.nghenghiep_dsdn.setText(DanhsachtrongAdapter.get(position).getNghenghiep());
        holder.muctieu_dsdn.setText(DanhsachtrongAdapter.get(position).getMucdichthamgia());
        holder.gioithieu_dsdn.setText("Giới thiệu: " + DanhsachtrongAdapter.get(position).getGioithieubanthan());
        Picasso.get().load(DanhsachtrongAdapter.get(position).getAnhdaidien()).into(holder.avata_dsdn);

        @SuppressLint("SimpleDateFormat") DateFormat dinhdangnam = new SimpleDateFormat("yyyy");
        sonam = Integer.parseInt(dinhdangnam.format(Calendar.getInstance().getTime()));
        sotuoi = sonam - DanhsachtrongAdapter.get(position).getNamsinh();
        holder.tuoi_dsdn.setText(sotuoi.toString());
        congiap = DanhsachtrongAdapter.get(position).getNamsinh() % 12;
        if(congiap == 1){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_dau);}
        if(congiap == 2){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_tuat);}
        if(congiap == 3){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_hoi);}
        if(congiap == 4){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_ty);}
        if(congiap == 5){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_suu);}
        if(congiap == 6){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_dan);}
        if(congiap == 7){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_mao);}
        if(congiap == 8){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_thin);}
        if(congiap == 9){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_ran);}
        if(congiap == 10){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_ngo);}
        if(congiap == 11){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_mui);}
        if(congiap == 0){holder.Img_icontuoi_dsdn.setImageResource(R.drawable.t_than);}

        final int soVang = DanhsachtrongAdapter.get(position).getVang();
        // Đổi màu đẳng cấp
        if (soVang > 1000) {
            holder.dangcap_dsdn.setTextColor(Color.parseColor("#E91E63"));
            holder.dangcap_dsdn.setText("Đại gia");
        } else if (soVang > 300) {
            holder.dangcap_dsdn.setTextColor(Color.parseColor("#EB08FB"));
            holder.dangcap_dsdn.setText("Giàu có");
        } else if (soVang > 100) {
            holder.dangcap_dsdn.setTextColor(Color.parseColor("#04B1FF"));
            holder.dangcap_dsdn.setText("Khá giả");
        } else {
            holder.dangcap_dsdn.setTextColor(Color.parseColor("#4CAF50"));
            holder.dangcap_dsdn.setText("Bình dân");
        }
        // Đổi màu đẳng cấp xong

        if (DanhsachtrongAdapter.get(position).getGioitinh() != null) {
            final String gioiTinh = DanhsachtrongAdapter.get(position).getGioitinh();
            holder.gioitinh_dsdn.setText(gioiTinh);
            switch (gioiTinh) {
                case "Nam":
                    holder.ten_dsdn.setTextColor(Color.parseColor("#00BCD4"));
                    holder.gioitinh_dsdn.setTextColor(Color.parseColor("#00BCD4"));
                    holder.Img_icongioitinh_dsdn.setImageResource(R.drawable.icon_nam);
                    break;
                case "Nữ":
                    holder.ten_dsdn.setTextColor(Color.parseColor("#FF95B9"));
                    holder.gioitinh_dsdn.setTextColor(Color.parseColor("#FF95B9"));
                    holder.Img_icongioitinh_dsdn.setImageResource(R.drawable.icon_nu);
                    break;
                case "Les":
                    holder.ten_dsdn.setTextColor(Color.parseColor("#FFC003"));
                    holder.gioitinh_dsdn.setTextColor(Color.parseColor("#FFC003"));
                    holder.Img_icongioitinh_dsdn.setImageResource(R.drawable.icon_dongtinh);
                    break;
                default:
                    holder.ten_dsdn.setTextColor(Color.parseColor("#73D600"));
                    holder.gioitinh_dsdn.setTextColor(Color.parseColor("#73D600"));
                    holder.Img_icongioitinh_dsdn.setImageResource(R.drawable.icon_dongtinh);
                    break;
            }   }

        // Bổ sung cho việc tạo sự kiện Itemclick
        holder.itemclick = DanhsachtrongAdapter.get(position).getID();
    }

    @Override
    public int getItemCount() {
        return DanhsachtrongAdapter.size();
    }

    public class ViewHolder_thanhvienmoidangnhap extends RecyclerView.ViewHolder{

        ImageView avata_dsdn, Img_icongioitinh_dsdn, Img_icontuoi_dsdn;
        TextView ten_dsdn, tuoi_dsdn, nghenghiep_dsdn, dangcap_dsdn, gioitinh_dsdn, muctieu_dsdn, gioithieu_dsdn;
        // Bổ sung cho việc tạo sự kiện ItemClick
        String itemclick;

        public ViewHolder_thanhvienmoidangnhap(@NonNull View itemView) {
            super(itemView);
            avata_dsdn = itemView.findViewById(R.id.avata_dsdn);
            ten_dsdn = itemView.findViewById(R.id.ten_dsdn);
            tuoi_dsdn = itemView.findViewById(R.id.tuoi_dsdn);
            nghenghiep_dsdn = itemView.findViewById(R.id.nghenghiep_dsdn);
            dangcap_dsdn = itemView.findViewById(R.id.dangcap_dsdn);
            gioitinh_dsdn = itemView.findViewById(R.id.gioitinh_dsdn);
            muctieu_dsdn = itemView.findViewById(R.id.muctieu_dsdn);
            gioithieu_dsdn = itemView.findViewById(R.id.gioithieu_dsdn);
            Img_icongioitinh_dsdn = itemView.findViewById(R.id.Img_icongioitinh_dsdn);
            Img_icontuoi_dsdn = itemView.findViewById(R.id.Img_icontuoi_dsdn);
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
