package com.example.wirelesscrs.Wi_Lect.activities.file;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wirelesscrs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class view_files_cloud extends AppCompatActivity {

    ListView myvf_list;
    DatabaseReference databaseReference;
    private boolean isAdmin=false;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    List<filesUpload> filesUploadList;
    AVLoadingIndicatorView avLoadingIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_files_cloud);
        myvf_list=findViewById(R.id.vf_list_cloud);
        avLoadingIndicatorView =  findViewById(R.id.avi_view_files_cloud);
        filesUploadList=new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
        checkForAdmin();
        //for open file in android
        myvf_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filesUpload filesUpload= filesUploadList.get(position);
                Intent intent=new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(filesUpload.getUrl()));
                startActivity(intent);
            }
        });

        myvf_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(isAdmin) {
                    final int which_item = position;
                    new AlertDialog.Builder(view_files_cloud.this)
                            .setTitle("Are you sure ?")
                            .setMessage("Do you want to delete this item")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    filesUpload filesUpload = filesUploadList.get(which_item);
                                    final String str = filesUpload.getF_id();
                                    databaseReference.child("Files").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            databaseReference.child(str).removeValue();
                                            Toasty.success(view_files_cloud.this, "Deleted Successfully", Toasty.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                else{
                    Toasty.error(view_files_cloud.this,"You are not admin",Toasty.LENGTH_SHORT).show();
                }

                return false;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(isNetworkAvailable(view_files_cloud.this)){
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.smoothToShow();
        databaseReference= FirebaseDatabase.getInstance().getReference("Files");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.smoothToHide();
                filesUploadList.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    filesUpload filesUpload=postSnapshot.getValue(filesUpload.class);
                    filesUploadList.add(filesUpload);
                }

                String[] uploads=new String[filesUploadList.size()];
                for(int i=0;i<uploads.length;i++){
                    uploads[i]=filesUploadList.get(i).getName();
                }
                cus_view_adap adapter=new cus_view_adap(view_files_cloud.this,filesUploadList);
                myvf_list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
        else{
            alertNoConnection();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(view_files_cloud.this,FileActivity.class);
        startActivity(intent);
        finish();
    }
    public void checkForAdmin() {

        databaseReference.child("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Objects.requireNonNull(auth.getUid()))
                        .exists()&& Objects.requireNonNull(dataSnapshot.child(auth.getUid())
                        .getValue()).toString().equals("true")){
                    isAdmin=true;
//                    Toasty.info(getApplicationContext(),"Hello Admin Sir", Toasty.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public void alertNoConnection() {

        /*final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.nowifi);
        builder.setCancelable(true);
        builder.setTitle("No Connection Available!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();*/

        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.nowifi);
//        imageView.getLayoutParams().height = 100;
//        imageView.getLayoutParams().width = 100;
//        imageView.requestLayout();
        //imageView.setImage(R.drawable.nowifi);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                400,
                400));
        builder.show();
    }
}
