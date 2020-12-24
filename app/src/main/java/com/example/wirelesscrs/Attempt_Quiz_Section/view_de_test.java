package com.example.wirelesscrs.Attempt_Quiz_Section;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wirelesscrs.Create_Quiz.create_quiz;
import com.example.wirelesscrs.Model.Test;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Wi_Lect.activities.file.filesUpload;
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

public class view_de_test extends AppCompatActivity {

    ListView myvf_list;
    DatabaseReference databaseReference;
    private boolean isAdmin=false;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    List<Test> filesTestList;
    AVLoadingIndicatorView avLoadingIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_de_test);
        myvf_list=findViewById(R.id.vf_list_test);
        avLoadingIndicatorView =  findViewById(R.id.avi_view_files_test);
        filesTestList=new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
        checkForAdmin();

//        myvf_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                if(isAdmin) {
//                    final int which_item = position;
//                    new AlertDialog.Builder(view_de_test.this)
//                            .setTitle("Are you sure ?")
//                            .setMessage("Do you want to delete this item")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Test filesUpload = filesTestList.get(which_item);
//                                    final String str = filesUpload.getName();
//                                    databaseReference.child("tests").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            databaseReference.child(str).removeValue();
//                                            Toasty.success(view_de_test.this, "Deleted Successfully", Toasty.LENGTH_SHORT).show();
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//
//
//                                }
//                            })
//                            .setNegativeButton("No", null)
//                            .show();
//                }
//                else{
//                    Toasty.error(view_de_test.this,"You are not admin",Toasty.LENGTH_SHORT).show();
//                }
//
//                return false;
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isNetworkAvailable(view_de_test.this)){
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            avLoadingIndicatorView.smoothToShow();
            databaseReference= FirebaseDatabase.getInstance().getReference("tests");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    avLoadingIndicatorView.smoothToHide();
                    filesTestList.clear();
                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Test testFile=postSnapshot.getValue(Test.class);
                        filesTestList.add(testFile);
                    }

                    String[] uploads=new String[filesTestList.size()];
                    for(int i=0;i<uploads.length;i++){
                        uploads[i]=filesTestList.get(i).getName();
                    }
                    cus_view_test adapter=new cus_view_test(view_de_test.this,filesTestList);
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

    public class cus_view_test extends ArrayAdapter<Test> {

        private Activity context;
        private List<Test> fu;

        public cus_view_test(Activity context,List<Test> fu){
            super(context, R.layout.custom_view_file_test,fu);
            this.context=context;
            this.fu=fu;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();

            View listViewItem=inflater.inflate(R.layout.custom_view_file_test,null,true);
            ((TextView)listViewItem.findViewById(R.id.txt_list_sh_test))
                    .setText(fu.get(position).getName());
            ((ImageView)listViewItem.findViewById(R.id.img_v_de_test))
                    .setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_delete_black_24dp));
            ((Button)listViewItem.findViewById(R.id.btn_v_de_test))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isAdmin) {
                                final int which_item = position;
                                new AlertDialog.Builder(view_de_test.this)
                                        .setTitle("Are you sure ?")
                                        .setMessage("Do you want to delete this item")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Test filesUpload = fu.get(which_item);
                                                final String str = filesUpload.getName();
                                                databaseReference.child("tests").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        databaseReference.child(str).removeValue();
                                                        Toasty.success(view_de_test.this, "Deleted Successfully", Toasty.LENGTH_SHORT).show();
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
                                Toasty.error(view_de_test.this,"You are not admin",Toasty.LENGTH_SHORT).show();
                            }
                        }
                    });
            return listViewItem;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(view_de_test.this, create_quiz.class));
        finish();
    }
}
