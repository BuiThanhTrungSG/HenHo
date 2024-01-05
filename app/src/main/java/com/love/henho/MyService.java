package com.love.henho;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {

    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;

    @Override
    public void onCreate() {
        super.onCreate();

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Thông báo")
                    .setContentText("Ứng dụng sẽ chạy ngầm").build();
            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(USER != null) {
            mData.child("USERS").child(USER.getUid()).child("thongbao_tinnhan").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        Boolean kiemtra = false;
                        kiemtra = (Boolean) snapshot.getValue();
                        if (kiemtra) {
                            createNotificationChannel();
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_02")
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Tin nhắn mới")
                                    .setContentText("Có ai đó vừa nhắn tin cho bạn")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                            mBuilder.setContentIntent(intent);
                            mBuilder.setAutoCancel(true);

                            notificationManager.notify(2, mBuilder.build());
                            mData.child("USERS").child(USER.getUid()).child("thongbao_tinnhan").setValue(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(USER.getUid()).child("thongbao_thich").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        Boolean kiemtra = false;
                        kiemtra = (Boolean) snapshot.getValue();
                        if (kiemtra) {
                            createNotificationChannel();
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_02")
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Tin vui")
                                    .setContentText("Có ai đó vừa thích bạn")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                            mBuilder.setContentIntent(intent);
                            mBuilder.setAutoCancel(true);

                            notificationManager.notify(3, mBuilder.build());
                            mData.child("USERS").child(USER.getUid()).child("thongbao_thich").setValue(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mData.child("USERS").child(USER.getUid()).child("thongbao_nhanloi").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        Boolean kiemtra = false;
                        kiemtra = (Boolean) snapshot.getValue();
                        if (kiemtra) {
                            createNotificationChannel();
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_02")
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Tin vui")
                                    .setContentText("Có ai đó vừa nhận lời mời của bạn")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                            mBuilder.setContentIntent(intent);
                            mBuilder.setAutoCancel(true);

                            notificationManager.notify(4, mBuilder.build());
                            mData.child("USERS").child(USER.getUid()).child("thongbao_nhanloi").setValue(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
            return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel_02", "my_channel_02", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is channel 2");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
