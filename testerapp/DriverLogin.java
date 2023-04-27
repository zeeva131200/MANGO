package com.example.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DriverLogin extends AppCompatActivity {
    private TextView signup;
    private EditText fEmail,fPswd;
    private Button loginBtn;
    private ProgressBar progressBar;
    private ImageButton ai;
    private ImageView imageV1;
    String imagesUri,user_id,face1,face2,link1,link2;
    private Uri path1,path2;


    //Firebase
    private FirebaseAuth fAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference db,db1;

//    private FirebaseAuth.AuthStateListener FbAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_login);

        fEmail = (EditText) findViewById(R.id.email);
        fPswd = (EditText) findViewById(R.id.pswd);
        loginBtn = (Button) findViewById(R.id.loginBtn2);
        signup = (TextView)findViewById(R.id.signUpTxt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ai = (ImageButton) findViewById(R.id.ai);
        imageV1 = findViewById(R.id.loginIcon );

        //get current state of logging status
        fAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fAuth = FirebaseAuth.getInstance();
        //user_id =fAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Driver");
        db1 = FirebaseDatabase.getInstance().getReference();

        ActivityCompat.requestPermissions(DriverLogin.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(DriverLogin.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        //if user nvr logout, send them str8 to homescreen
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(DriverLogin.this, D_Homescreen.class));
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = fEmail.getText().toString().trim();
                String pswd =  fPswd.getText().toString().trim();

                //if field is null
                if(TextUtils.isEmpty(email)){
                    fEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(pswd)){
                    fPswd.setError("Password is Required");
                    return;
                }

                if(pswd.length() < 6){
                    fPswd.setError("Password must be more than or equal to 6 characters");
                    return;
                }

                //make progressbar visible
                progressBar.setVisibility(View.VISIBLE);

                //authenticate user from firebase
                fAuth.signInWithEmailAndPassword(email,pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DriverLogin.this, "Log in Successful! WELCOME!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), D_Homescreen.class));
                        }
                        else{
                            Toast.makeText(DriverLogin.this, "Log in failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(DriverLogin.this,DriverSignUp.class));
            }
        });

        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
                //openCamera();
            }
        });
    }

    private static final int CAMERA_PERMISSION_CODE = 101;

    public void askCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else{
            openCamera();
            //dispatchTakePictureIntent();
            //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
                //dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static final int CAMERA_REQUEST_CODE = 102;
    private void openCamera(){
        Intent camera =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
        //TODO - STR8 SAVE IMAGE TO FIREBASE & RETRIEVE & DISPLAY & COMPARE
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CAMERA_REQUEST_CODE  && resultCode == RESULT_OK) {
                //Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageV1.setImageBitmap(imageBitmap);
                path1 = getImageUri(this,imageBitmap);
                //Toast.makeText(this, "path"+path1, Toast.LENGTH_SHORT).show();
                //imageV1.setTag();
                //saveToGallery();
                getFBImagesUrl();
            }

//        if(requestCode == CAMERA_REQUEST_CODE){
//            if(resultCode == Activity.RESULT_OK){
//                File f = new File(currentPhotoPath);
//                selectedImage .setImageURI(Uri.fromFile(f));
//                Toast.makeText(this, "Absolute URi is "+ Uri.fromFile(f), Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void getFBImagesUrl(){
        StorageReference ref = storageReference.child("images/"+ user_id);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot rideshot : snapshot.getChildren()) {
                    for (DataSnapshot rideshot1 : rideshot.getChildren()) {
                        String key = rideshot1.getKey();
                        //Toast.makeText(DriverLogin.this, "a : " + key, Toast.LENGTH_SHORT).show();
                        if (key.equals("images")) {
                            imagesUri = rideshot1.getValue().toString();
                            //Toast.makeText(DriverLogin.this, "path id : " + imagesUri.toString(), Toast.LENGTH_SHORT).show();
                            setImage(imagesUri);

                        } else {
                            //Toast.makeText(DriverLogin.this, "please choose an image to continue", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverLogin.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setImage(String user_id) {
        StorageReference ref = storageReference.child("images/"+ user_id);
        // Toast.makeText(this, "id"+user_id, Toast.LENGTH_SHORT).show();

        try{
            File localfile = File.createTempFile("tempfile",".jpg");
            ref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bit = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    //selectedImage.setImageBitmap(bit);
                    path2 = getImageUri(DriverLogin.this,bit);
                    //Toast.makeText(DriverLogin.this, "path2"+path2, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(DriverLogin.this, "path1"+path1, Toast.LENGTH_SHORT).show();
                    getImage(path1,path2);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(DriverLogin.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void getImage(Uri uri1, Uri uri2) {
        try {
            InputImage image1 = InputImage.fromFilePath(this, uri1);
            InputImage image2 = InputImage.fromFilePath(this, uri2);
            detectFace(image1,image2);
            //Toast.makeText(this, "images "+image1+" "+image2, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
        }
    }

    public void detectFace(InputImage image1, InputImage image2){


        // High-accuracy landmark detection and face classification
        FaceDetectorOptions option1 = new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        // Real-time contour detection
        FaceDetectorOptions option2 = new FaceDetectorOptions.Builder()
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .build();

        FaceDetector detector = FaceDetection.getClient(option2);
        db1.child("images_id1").setValue(null);
        db1.child("images_id2").setValue(null);

        Task<List<Face>> result1 = detector.process(image1).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            List<PointF> leftEyeContour1,upperLipBottomContour1;
            @Override
            public void onSuccess(List<Face> faces) {
                // Task completed successfully
                for (Face face : faces) {
                    Rect bounds = face.getBoundingBox();
                    float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
                    //Toast.makeText(DriverLogin.this, "face: "+rotY+"rotz: "+rotZ, Toast.LENGTH_SHORT).show();

                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                    // nose available):
                    FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
                    if (leftEar != null) {
                        PointF leftEarPos = leftEar.getPosition();
                        Toast.makeText(DriverLogin.this, "leftEar "+leftEarPos, Toast.LENGTH_SHORT).show();
                    }

                    // If contour detection was enabled:
                    leftEyeContour1 = face.getContour(FaceContour.LEFT_EYE).getPoints();
                    upperLipBottomContour1 = face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();
                    //Toast.makeText(DriverLogin.this, "lefteye "+leftEyeContour1.toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(DriverLogin.this, "upperlip "+upperLipBottomContour1.toString(), Toast.LENGTH_SHORT).show();

                    // If classification was enabled:
                    if (face.getSmilingProbability() != null) {
                        float smileProb = face.getSmilingProbability();
                        Toast.makeText(DriverLogin.this, "smile "+smileProb, Toast.LENGTH_SHORT).show();
                    }
                    if (face.getRightEyeOpenProbability() != null) {
                        float rightEyeOpenProb = face.getRightEyeOpenProbability();
                        Toast.makeText(DriverLogin.this, "right "+rightEyeOpenProb, Toast.LENGTH_SHORT).show();
                    }

                    // If face tracking was enabled:
                    if (face.getTrackingId() != null) {
                        int id = face.getTrackingId();
                    }
                }
                face1 = leftEyeContour1.toString()+upperLipBottomContour1.toString();
                //Toast.makeText(DriverLogin.this, "result1: "+face1, Toast.LENGTH_SHORT).show();
                //assign(face1);
                db1.child("images_id1").setValue(face1);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Task failed with an exception
                // ...
                Toast.makeText(DriverLogin.this, "no face", Toast.LENGTH_SHORT).show();
            }
        });

        Task<List<Face>> result2 = detector.process(image2).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            List<PointF> leftEyeContour2,upperLipBottomContour2;
            @Override
            public void onSuccess(List<Face> faces) {
                // Task completed successfully
                for (Face face : faces) {
                    Rect bounds = face.getBoundingBox();
                    float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
                    //Toast.makeText(DriverLogin.this, "face: "+rotY+"rotz: "+rotZ, Toast.LENGTH_SHORT).show();

                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                    // nose available):
                    FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
                    if (leftEar != null) {
                        PointF leftEarPos = leftEar.getPosition();
                        Toast.makeText(DriverLogin.this, "leftEar "+leftEarPos, Toast.LENGTH_SHORT).show();
                    }

                    // If contour detection was enabled:
                    leftEyeContour2 = face.getContour(FaceContour.LEFT_EYE).getPoints();
                    upperLipBottomContour2 = face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();
                    //l\Toast.makeText(DriverLogin.this, "lefteye2 "+leftEyeContour2.toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(DriverLogin.this, "upperlip2 "+upperLipBottomContour2.toString(), Toast.LENGTH_SHORT).show();

                    // If classification was enabled:
                    if (face.getSmilingProbability() != null) {
                        float smileProb = face.getSmilingProbability();
                        Toast.makeText(DriverLogin.this, "smile "+smileProb, Toast.LENGTH_SHORT).show();
                    }
                    if (face.getRightEyeOpenProbability() != null) {
                        float rightEyeOpenProb = face.getRightEyeOpenProbability();
                        Toast.makeText(DriverLogin.this, "right "+rightEyeOpenProb, Toast.LENGTH_SHORT).show();
                    }

                    // If face tracking was enabled:
                    if (face.getTrackingId() != null) {
                        int id = face.getTrackingId();
                    }
                }
                face2 = leftEyeContour2.toString()+upperLipBottomContour2.toString();
                //Toast.makeText(DriverLogin.this, "result2: "+face2, Toast.LENGTH_SHORT).show();
                db1.child("images_id2").setValue(face2);
                findSimilarity();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Task failed with an exception
                // ...
                Toast.makeText(DriverLogin.this, "no face", Toast.LENGTH_SHORT).show();
            }
        });
        //Toast.makeText(DriverLogin.this, "here", Toast.LENGTH_SHORT).show();
        //assign2(face2);


    }

    private void assign(String str1){
        face1 = str1;
        //return face1;
    }
    private void assign2 (String str2){
        face2 = str2;
        //return face2;
    }

    private void findSimilarity(){
        Toast.makeText(DriverLogin.this, "f1,2 ", Toast.LENGTH_SHORT).show();

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str1,str2;
                for(DataSnapshot rideshot: snapshot.getChildren()) {
                    String key = rideshot.getKey();
                    //Toast.makeText(DriverLogin.this, "a : "+key, Toast.LENGTH_SHORT).show();
                    if(key.equals("images_id1")) {
                        face1 = rideshot.getValue().toString();
                        //Toast.makeText(DriverLogin.this, "face1 : " + face1, Toast.LENGTH_SHORT).show();
                        str1 = face1.substring(8,10);
                        //Toast.makeText(DriverLogin.this, "str: "+str1, Toast.LENGTH_SHORT).show();
                    }
                    if(key.equals("images_id2")) {
                        face2 = rideshot.getValue().toString();
                        //Toast.makeText(DriverLogin.this, "face2 : " + face2, Toast.LENGTH_SHORT).show();
                        str2 = face2.substring(8,11);
                        //Toast.makeText(DriverLogin.this, "str: "+str2, Toast.LENGTH_SHORT).show();
                        str1 = face1.substring(8,10);
                        if((Integer.valueOf(str2)-(Integer.valueOf(str1))<=258)){
                            //Toast.makeText(DriverLogin.this, "similar face", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DriverLogin.this,D_Homescreen.class));
                        }
                        else{
                            //Toast.makeText(DriverLogin.this, "No match! Try again", Toast.LENGTH_SHORT).show();
                            logout();
                        }
                    }
                    else{
                        //Toast.makeText(DriverLogin.this, "no path retrieved", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverLogin.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

//    private void saveToGallery(){
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageV1.getDrawable();
//        Bitmap bitmap = bitmapDrawable.getBitmap();
//
//        FileOutputStream outputStream = null;
//        File file = Environment.getExternalStorageDirectory();
//        File dir = new File(file.getAbsolutePath() + "/MyPics");
//        dir.mkdirs();
//
//        String filename = String.format("%d.png",System.currentTimeMillis());
//        File outFile = new File(dir,filename);
//        try{
//            outputStream = new FileOutputStream(outFile);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
//        try{
//            outputStream.flush();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try{
//            outputStream.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    //to register and unregister listeners
//    @Override
//    protected void onStart() {
//        super.onStart();
//        fAuth.addAuthStateListener(FbAuthListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        fAuth.removeAuthStateListener(FbAuthListener);
//    }

}