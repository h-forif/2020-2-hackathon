package com.example.a2020_2forifhackathon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    //ReqCode
    private final static int SELECT_IMAGE = 101;
    private Uri mImageUrl;
    private Task<Uri> mUploadTask;
    private Toast mToast;

    //Stroage
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    ImageButton btnSetImage;
    ImageButton save;
    ImageView preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post);

        btnSetImage = findViewById(R.id.BTN_ImageFromGallery);
        save = findViewById(R.id.BTN_Save);
        preview = findViewById(R.id.IV_Preview);

        btnSetImage.setOnClickListener(this);
        save.setOnClickListener(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_IMAGE && data != null){
            mImageUrl = data.getData();
            Glide.with(this)
                    .load(mImageUrl)
                    .centerCrop()
                    .into(preview);
        }
    }

    @Override
    public void onClick(View view) {
        int cid = view.getId();

        switch (cid){
            case R.id.BTN_ImageFromGallery:
                selectImage();
                break;

            case R.id.BTN_Save:
                save();
                break;

        }
    }

    //userfunc
    private String getFileExtension(Uri mImageUrl){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mImageUrl));
    }

    private void isValidToast(Toast mToast){
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(PostActivity.this,"업로드중입니다!",Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void selectImage(){
        Intent selImgIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selImgIntent.setType("image/*");
        startActivityForResult(selImgIntent,SELECT_IMAGE);
    }

    private void save(){
        //TODO: SaveNote()
        saveImage();
    }

    private void saveImage(){
        if(mUploadTask != null){
            isValidToast(mToast);
        }else{
            if(mImageUrl != null){
                String filename = System.currentTimeMillis() + "." + getFileExtension(mImageUrl);
                final StorageReference mStorageRef = storageRef.child(filename.trim());
                mUploadTask = mStorageRef.putFile(mImageUrl)
                        .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(task.isSuccessful()){
                                    return mStorageRef.getDownloadUrl();
                                }else {
                                    throw Objects.requireNonNull(task.getException());
                                }
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloadUri = task.getResult();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(PostActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
}