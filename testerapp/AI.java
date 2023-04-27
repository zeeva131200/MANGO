package com.example.testerapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testerapp.databinding.MainActivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AI extends AppCompatActivity {
ImageView selectedImage;
Button upload, glrBtn, camBtn;
String currentPhotoPath;

//https://github.com/tutsplus/upload-images-to-firebase-from-an-android-app/blob/master/app/src/main/java/com/tutsplus/code/android/tutsplusupload/MainActivity.java
// https://stackoverflow.com/questions/42054501/i-cant-select-images-from-my-gallery-for-upload-android-studio

private MainActivityBinding binding;
private boolean isReadPermissionGranted = false;
private boolean isWritePermissionGranted = false;
ActivityResultLauncher<String[]> mPermissionLauncher;
ActivityResultLauncher<Intent> mGetImage;
private Uri filepath;
private static final int CAMERA = 100;
private static final int PICK_IMAGE_REQUEST = 10;
private ProgressDialog dialog;
String imagesUri,user_id;


//Firebase
private FirebaseAuth fAuth;
FirebaseStorage storage;
StorageReference storageReference;
DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai);

        selectedImage = findViewById(R.id.selectedImView);
        upload = findViewById(R.id.uploadBtn);
        glrBtn = findViewById(R.id.glryBtn);
        camBtn = findViewById(R.id.camBtn);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fAuth = FirebaseAuth.getInstance();
        user_id =fAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Driver").child(user_id);

        //getImagesUrl();
        setImage();

        // TODO - set camera
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(AI.this, "camera btn is clicked", Toast.LENGTH_SHORT).show();
                openCamera();
                galleryAddPic();
            }
        });
//
//        glrBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(AI.this, "gallery btn clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

        dialog = new ProgressDialog(this);

        glrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

    }

    private void chooseImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST   );
    }

    private void uploadImage() {

        if(filepath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ user_id);
            ref.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AI.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            //db.child("Users").child("Driver").child(user_id).child("images").setValue(user_id);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AI.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                //Toast.makeText(AI.this, "uri"+bitmap, Toast.LENGTH_SHORT).show();
                //Toast.makeText(AI.this, "filepath"+filepath, Toast.LENGTH_SHORT).show();
                selectedImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void getImagesUrl(){  // NOT USED
        StorageReference ref = storageReference.child("images/"+ user_id);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot rideshot: snapshot.getChildren()) {
                        String key = rideshot.getKey();
                        //Toast.makeText(AI.this, "a : "+key, Toast.LENGTH_SHORT).show();
                        if(key.equals("images")) {
                            imagesUri = rideshot.getValue().toString();
                            //Toast.makeText(AI.this, "key : " + imagesUri.toString(), Toast.LENGTH_SHORT).show();
                            setImage();

                        }
                        else{
                            Toast.makeText(AI.this, "please choose an image to continue", Toast.LENGTH_SHORT).show();
                        }

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AI.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setImage(){
        StorageReference ref = storageReference.child("images/"+ user_id);
        // Toast.makeText(this, "id"+user_id, Toast.LENGTH_SHORT).show();

        try{
            File localfile = File.createTempFile("tempfile",".jpg");
            ref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bit = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    selectedImage.setImageBitmap(bit);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(AI.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }



//    private static final int CAMERA_PERMISSION_CODE = 101;
//
//    public void askCameraPermission(){
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
//        }
//        else{
//            openCamera();
//            //dispatchTakePictureIntent();
//            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera();
//                //dispatchTakePictureIntent();
//            } else {
//                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private static final int CAMERA_REQUEST_CODE = 102;
    private void openCamera(){
        Intent camera =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }
//
//        @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
//        super.onActivityResult(requestCode, resultCode, data);
////            if (requestCode == CAMERA_REQUEST_CODE  && resultCode == RESULT_OK) {
////                Bundle extras = data.getExtras();
////                Bitmap imageBitmap = (Bitmap) extras.get("data");
////                selectedImage.setImageBitmap(imageBitmap);
////            }
//
//        if(requestCode == CAMERA_REQUEST_CODE){
//            if(resultCode == Activity.RESULT_OK){
//                File f = new File(currentPhotoPath);
//                selectedImage .setImageURI(Uri.fromFile(f));
//                Toast.makeText(this, "Absolute URi is "+ Uri.fromFile(f), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//
//    private File createImageFile() throws IOException {
//        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    static final int REQUEST_TAKE_PHOTO =1;
//    private void dispatchTakePictureIntent() {
//        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//        try {
//            File photoFile = createImageFile();
//
//            Toast.makeText(this, "File: "+ photoFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
//            //Log.d(LOG_TAG, "File: " + photoFile.getAbsolutePath());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //......HERE TILL

//        // Ensure that there's a camera activity to handle the intent
//        Toast.makeText(this, "1.2", Toast.LENGTH_SHORT).show();
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            Toast.makeText(this, "1.3", Toast.LENGTH_SHORT).show();
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                Toast.makeText(this, "1.4", Toast.LENGTH_SHORT).show();
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//            }
//        }
//        else{
//            Toast.makeText(this, "no se", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    //        binding = MainActivityBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        mPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
//            @Override
//            public void onActivityResult(Map<String, Boolean> result) {
//                if(result.get(Manifest.permission.READ_EXTERNAL_STORAGE)!= null){
//                    isReadPermissionGranted = result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                }
//            }
//        });
//
//        mGetImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//
//                if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
//                    Bundle bundle = result.getData().getExtras();
//                    Bitmap bitmap = (Bitmap) bundle.get("data");
//
//                    if(isWritePermissionGranted){
//                        if(saveImageToExternalStorage(UUID.randomUUID().toString(),bitmap)){
//                            Toast.makeText(AI.this, "saved image successfully", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else{
//                        Toast.makeText(AI.this, "permisson not granted", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//        requestPermission();
//
//        //binding.saveButton.set

//    private void requestPermission(){
//        boolean minSDK = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
//
//        isReadPermissionGranted = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//
//        isWritePermissionGranted = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//
//        isWritePermissionGranted = isWritePermissionGranted || minSDK;
//
//        List<String> permissionRequest = new ArrayList<String>();
//        if(!isReadPermissionGranted){
//            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//
//        if(!isWritePermissionGranted){
//            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//
//        if(!permissionRequest.isEmpty()){
//            mPermissionLauncher.launch(permissionRequest.toArray(new String[0]));
//        }
//
//    }

//    private boolean saveImageToExternalStorage (String imgName, Bitmap bmp){
//
//        Uri ImageCollection = null;
//        ContentResolver resolver = getContentResolver();
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//            ImageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
//        }
//        else{
//            ImageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        }
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName + ".jpg");
//        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
//        Uri imageUri = resolver.insert(ImageCollection, contentValues);
//
//        try{
//            OutputStream outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            Objects.requireNonNull(outputStream);
//            return true;
//        }
//        catch(Exception e){
//            Toast.makeText(this, "Image not saved \n"+ e, Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//        return false;
//    }



//

//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
//        return image;
//    }
//
//    static final int REQUEST_TAKE_PHOTO =1;
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        Toast.makeText(this, "here:" +takePictureIntent.resolveActivity(getPackageManager()), Toast.LENGTH_SHORT).show();
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                Toast.makeText(this, "Error occurred while creating the File", Toast.LENGTH_SHORT).show();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//                Toast.makeText(this, "2.1", Toast.LENGTH_SHORT).show();
//            }
//        }
//        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//    }
}