package com.example.wirelesscrs.Wi_Lect.activities.file;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wirelesscrs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import es.dmoral.toasty.Toasty;

public class upload_file_cloud extends AppCompatActivity {

    Button upload;
    EditText fileName;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    public static final int PICK_FILE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file_cloud);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("Files");
        upload=findViewById(R.id.upload_File_cloud);
        fileName=findViewById(R.id.chs_File);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent,PICK_FILE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_FILE && resultCode==RESULT_OK
                && data!=null && data.getData()!=null){
            uploadFile(data.getData());
        }
    }

    private void uploadFile(Uri data) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading......");
        progressDialog.show();
        StorageReference reference=storageReference.child("Files/"+System.currentTimeMillis());
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri= taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url =uri.getResult();
                        String fid=databaseReference.push().getKey();
                        filesUpload filesUpload=new filesUpload(fileName.getText().toString(),url.toString(),fid.toString());
                        databaseReference.child(fid).setValue(filesUpload);
                        progressDialog.dismiss();
                        Toasty.success(upload_file_cloud.this,"File Uploaded",Toasty.LENGTH_SHORT).show();
                        Intent intent=new Intent(upload_file_cloud.this,view_files_cloud.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded: "+(int)progress+"%");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(upload_file_cloud.this,FileActivity.class);
        startActivity(intent);
        finish();
    }

}
