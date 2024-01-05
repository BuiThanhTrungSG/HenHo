package com.love.henho;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.love.henho.Model.Model_anhbimat;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class Bi_mat_them extends AppCompatActivity {

    TextView Txt_matkhau_bimatthem;
    ImageView Img_doimatkhau_bimatthem, Img_xemanh_bimatthem, Img_quaylai_bimatthem
            ,Img_anh1_bimatthem, Img_doi1_bimatthem, Img_xoa1_bimatthem
            ,Img_anh2_bimatthem, Img_doi2_bimatthem, Img_xoa2_bimatthem
            ,Img_anh3_bimatthem, Img_doi3_bimatthem, Img_xoa3_bimatthem
            ,Img_anh4_bimatthem, Img_doi4_bimatthem, Img_xoa4_bimatthem
            ,Img_anh5_bimatthem, Img_doi5_bimatthem, Img_xoa5_bimatthem
            ,Img_anh6_bimatthem, Img_doi6_bimatthem, Img_xoa6_bimatthem;
    LinearLayout Linear_xemanh_bimatthem;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser USER;
    FirebaseFirestore db;
    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bi_mat_them);

        Anhxa();

        layanh();

        // Quảng cáo
        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                createBannerAd();
            }
        } );

        Img_doimatkhau_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                int so = rand.nextInt(100);
                String matkhau;
                if(so < 10){
                    matkhau = "0" + so;
                }else {
                    matkhau = String.valueOf(so);
                }
                Txt_matkhau_bimatthem.setText(matkhau);
                mData.child("USERS").child(USER.getUid()).child("bimat").setValue(matkhau);
            }
        });

        ActivityResultLauncher<Intent> anh1 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            imageUri = data.getData();
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            selectedImage = getResizedBitmap(selectedImage, 400);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data1 = baos.toByteArray();

                            StorageReference riversRef = storageReference.child("anhbimat/" + USER.getUid() + "/anh1.jpeg");
                            riversRef.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> linkanh = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    linkanh.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String linkanhtiep = uri.toString();
                                            FirebaseFirestore.getInstance().collection("USER").document(USER.getUid()).update("anh1", linkanhtiep);
                                            Picasso.get().load(linkanhtiep).into(Img_anh1_bimatthem);
                                            Toast.makeText(Bi_mat_them.this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

        ActivityResultLauncher<Intent> anh2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            imageUri = data.getData();
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            selectedImage = getResizedBitmap(selectedImage, 400);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data1 = baos.toByteArray();

                            StorageReference riversRef = storageReference.child("anhbimat/" + USER.getUid() + "/anh2.jpeg");
                            riversRef.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> linkanh = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    linkanh.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String linkanhtiep = uri.toString();
                                            FirebaseFirestore.getInstance().collection("USER").document(USER.getUid()).update("anh2", linkanhtiep);
                                            Picasso.get().load(linkanhtiep).into(Img_anh2_bimatthem);
                                            Toast.makeText(Bi_mat_them.this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

        ActivityResultLauncher<Intent> anh3 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            imageUri = data.getData();
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            selectedImage = getResizedBitmap(selectedImage, 400);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data1 = baos.toByteArray();

                            StorageReference riversRef = storageReference.child("anhbimat/" + USER.getUid() + "/anh3.jpeg");
                            riversRef.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> linkanh = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    linkanh.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String linkanhtiep = uri.toString();
                                            FirebaseFirestore.getInstance().collection("USER").document(USER.getUid()).update("anh3", linkanhtiep);
                                            Picasso.get().load(linkanhtiep).into(Img_anh3_bimatthem);
                                            Toast.makeText(Bi_mat_them.this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

        ActivityResultLauncher<Intent> anh4 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            imageUri = data.getData();
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            selectedImage = getResizedBitmap(selectedImage, 400);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data1 = baos.toByteArray();

                            StorageReference riversRef = storageReference.child("anhbimat/" + USER.getUid() + "/anh4.jpeg");
                            riversRef.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> linkanh = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    linkanh.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String linkanhtiep = uri.toString();
                                            FirebaseFirestore.getInstance().collection("USER").document(USER.getUid()).update("anh4", linkanhtiep);
                                            Picasso.get().load(linkanhtiep).into(Img_anh4_bimatthem);
                                            Toast.makeText(Bi_mat_them.this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

        ActivityResultLauncher<Intent> anh5 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            imageUri = data.getData();
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            selectedImage = getResizedBitmap(selectedImage, 400);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data1 = baos.toByteArray();

                            StorageReference riversRef = storageReference.child("anhbimat/" + USER.getUid() + "/anh5.jpeg");
                            riversRef.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> linkanh = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    linkanh.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String linkanhtiep = uri.toString();
                                            FirebaseFirestore.getInstance().collection("USER").document(USER.getUid()).update("anh5", linkanhtiep);
                                            Picasso.get().load(linkanhtiep).into(Img_anh5_bimatthem);
                                            Toast.makeText(Bi_mat_them.this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

        ActivityResultLauncher<Intent> anh6 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            imageUri = data.getData();
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            selectedImage = getResizedBitmap(selectedImage, 400);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data1 = baos.toByteArray();

                            StorageReference riversRef = storageReference.child("anhbimat/" + USER.getUid() + "/anh6.jpeg");
                            riversRef.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> linkanh = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    linkanh.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String linkanhtiep = uri.toString();
                                            FirebaseFirestore.getInstance().collection("USER").document(USER.getUid()).update("anh6", linkanhtiep);
                                            Picasso.get().load(linkanhtiep).into(Img_anh6_bimatthem);
                                            Toast.makeText(Bi_mat_them.this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

        Img_anh1_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatthem.setImageDrawable(Img_anh1_bimatthem.getDrawable());
                Linear_xemanh_bimatthem.setVisibility(View.VISIBLE);
            }
        });
        Img_anh2_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatthem.setImageDrawable(Img_anh2_bimatthem.getDrawable());
                Linear_xemanh_bimatthem.setVisibility(View.VISIBLE);
            }
        });
        Img_anh3_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatthem.setImageDrawable(Img_anh3_bimatthem.getDrawable());
                Linear_xemanh_bimatthem.setVisibility(View.VISIBLE);
            }
        });
        Img_anh4_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatthem.setImageDrawable(Img_anh4_bimatthem.getDrawable());
                Linear_xemanh_bimatthem.setVisibility(View.VISIBLE);
              }
        });
        Img_anh5_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatthem.setImageDrawable(Img_anh5_bimatthem.getDrawable());
                Linear_xemanh_bimatthem.setVisibility(View.VISIBLE);
            }
        });
        Img_anh6_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_xemanh_bimatthem.setImageDrawable(Img_anh6_bimatthem.getDrawable());
                Linear_xemanh_bimatthem.setVisibility(View.VISIBLE);
            }
        });

        Linear_xemanh_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linear_xemanh_bimatthem.setVisibility(View.GONE);
            }
        });
        Img_xemanh_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linear_xemanh_bimatthem.setVisibility(View.GONE);
            }
        });

        Img_doi1_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                anh1.launch(intent);
            }
        });
        Img_doi2_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                anh2.launch(intent);
            }
        });
        Img_doi3_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                anh3.launch(intent);
            }
        });
        Img_doi4_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                anh4.launch(intent);
            }
        });
        Img_doi5_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                anh5.launch(intent);
            }
        });
        Img_doi6_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                anh6.launch(intent);
            }
        });

        Img_xoa1_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Bi_mat_them.this);
                alerDialogBuilder.setMessage("Bạn muốn xóa ảnh này ?");
                alerDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("USER").document(USER.getUid()).update("anh1", FieldValue.delete());
                        Img_anh1_bimatthem.setImageResource(R.drawable.nen_bimat2);
                    }
                });
                alerDialogBuilder.setNegativeButton("Không", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();
            }
        });

        Img_xoa2_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Bi_mat_them.this);
                alerDialogBuilder.setMessage("Bạn muốn xóa ảnh này ?");
                alerDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("USER").document(USER.getUid()).update("anh2", FieldValue.delete());
                        Img_anh2_bimatthem.setImageResource(R.drawable.nen_bimat2);
                    }
                });
                alerDialogBuilder.setNegativeButton("Không", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();
            }
        });

        Img_xoa3_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Bi_mat_them.this);
                alerDialogBuilder.setMessage("Bạn muốn xóa ảnh này ?");
                alerDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("USER").document(USER.getUid()).update("anh3", FieldValue.delete());
                        Img_anh3_bimatthem.setImageResource(R.drawable.nen_bimat2);
                    }
                });
                alerDialogBuilder.setNegativeButton("Không", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();
            }
        });

        Img_xoa4_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Bi_mat_them.this);
                alerDialogBuilder.setMessage("Bạn muốn xóa ảnh này ?");
                alerDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("USER").document(USER.getUid()).update("anh4", FieldValue.delete());
                        Img_anh4_bimatthem.setImageResource(R.drawable.nen_bimat2);
                    }
                });
                alerDialogBuilder.setNegativeButton("Không", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();
            }
        });

        Img_xoa5_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Bi_mat_them.this);
                alerDialogBuilder.setMessage("Bạn muốn xóa ảnh này ?");
                alerDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("USER").document(USER.getUid()).update("anh5", FieldValue.delete());
                        Img_anh5_bimatthem.setImageResource(R.drawable.nen_bimat2);
                    }
                });
                alerDialogBuilder.setNegativeButton("Không", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();
            }
        });

        Img_xoa6_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Bi_mat_them.this);
                alerDialogBuilder.setMessage("Bạn muốn xóa ảnh này ?");
                alerDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("USER").document(USER.getUid()).update("anh6", FieldValue.delete());
                        Img_anh6_bimatthem.setImageResource(R.drawable.nen_bimat2);
                    }
                });
                alerDialogBuilder.setNegativeButton("Không", null);
                AlertDialog alerDialog = alerDialogBuilder.create();
                alerDialog.show();
            }
        });

        Img_quaylai_bimatthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Linear_xemanh_bimatthem.getVisibility() == View.VISIBLE) {
                    Linear_xemanh_bimatthem.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("sangtrang3", true);
                    startActivity(intent);
                }
            }
        });
    }

    void createBannerAd() {
        adView = new MaxAdView( getResources().getString(R.string.id_banner), this );
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( this ).getHeight();
        int heightPx = AppLovinSdkUtils.dpToPx( this, heightDp );
        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
        adView.setExtraParameter( "adaptive_banner", "true" );
        ViewGroup rootView = findViewById(R.id.Banner_bimatthem);
        rootView.addView(adView);
        adView.loadAd();
    }

    private void layanh() {
        mData.child("USERS").child(USER.getUid()).child("bimat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    String matkhau = snapshot.getValue().toString();
                    if(matkhau.length() == 2){
                        Txt_matkhau_bimatthem.setText(matkhau);
                    }else {
                        mData.child("USERS").child(USER.getUid()).child("bimat").setValue("76");
                        Txt_matkhau_bimatthem.setText("76");
                    }
                }else {
                    mData.child("USERS").child(USER.getUid()).child("bimat").setValue("76");
                    Txt_matkhau_bimatthem.setText("76");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        db.collection("USER").document(USER.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Model_anhbimat layve = documentSnapshot.toObject(Model_anhbimat.class);
                if(layve.getAnh1() != null){
                    Picasso.get().load(layve.getAnh1()).into(Img_anh1_bimatthem);
                }
                if(layve.getAnh2() != null){
                    Picasso.get().load(layve.getAnh2()).into(Img_anh2_bimatthem);
                }
                if(layve.getAnh3() != null){
                    Picasso.get().load(layve.getAnh3()).into(Img_anh3_bimatthem);
                }
                if(layve.getAnh4() != null){
                    Picasso.get().load(layve.getAnh4()).into(Img_anh4_bimatthem);
                }
                if(layve.getAnh5() != null){
                    Picasso.get().load(layve.getAnh5()).into(Img_anh5_bimatthem);
                }
                if(layve.getAnh6() != null){
                    Picasso.get().load(layve.getAnh6()).into(Img_anh6_bimatthem);
                }
            }
        });
    }

    private void Anhxa() {
        Img_quaylai_bimatthem = findViewById(R.id.Img_quaylai_bimatthem);
        Img_xoa2_bimatthem = findViewById(R.id.Img_xoa2_bimatthem);
        Img_xoa3_bimatthem = findViewById(R.id.Img_xoa3_bimatthem);
        Img_xoa4_bimatthem = findViewById(R.id.Img_xoa4_bimatthem);
        Img_xoa5_bimatthem = findViewById(R.id.Img_xoa5_bimatthem);
        Img_xoa6_bimatthem = findViewById(R.id.Img_xoa6_bimatthem);
        Img_xoa1_bimatthem = findViewById(R.id.Img_xoa1_bimatthem);
        Img_doi3_bimatthem = findViewById(R.id.Img_doi3_bimatthem);
        Img_doi4_bimatthem = findViewById(R.id.Img_doi4_bimatthem);
        Img_doi5_bimatthem = findViewById(R.id.Img_doi5_bimatthem);
        Img_doi6_bimatthem = findViewById(R.id.Img_doi6_bimatthem);
        Img_doi2_bimatthem = findViewById(R.id.Img_doi2_bimatthem);
        Img_doi1_bimatthem = findViewById(R.id.Img_doi1_bimatthem);
        Img_xemanh_bimatthem = findViewById(R.id.Img_xemanh_bimatthem);
        Linear_xemanh_bimatthem = findViewById(R.id.Linear_xemanh_bimatthem);
        Txt_matkhau_bimatthem = findViewById(R.id.Txt_matkhau_bimatthem);
        Img_doimatkhau_bimatthem = findViewById(R.id.Img_doimatkhau_bimatthem);
        Img_anh1_bimatthem = findViewById(R.id.Img_anh1_bimatthem);
        Img_anh2_bimatthem = findViewById(R.id.Img_anh2_bimatthem);
        Img_anh3_bimatthem = findViewById(R.id.Img_anh3_bimatthem);
        Img_anh4_bimatthem = findViewById(R.id.Img_anh4_bimatthem);
        Img_anh5_bimatthem = findViewById(R.id.Img_anh5_bimatthem);
        Img_anh6_bimatthem = findViewById(R.id.Img_anh6_bimatthem);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        USER = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onBackPressed() {
        if (Linear_xemanh_bimatthem.getVisibility() == View.VISIBLE) {
            Linear_xemanh_bimatthem.setVisibility(View.GONE);
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("sangtrang3", true);
            startActivity(intent);
        }
    }
}