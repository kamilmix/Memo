package pl.lodz.uni.math.kamilmucha.memo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    public static final String EXTRA_MESSAGE = "com.mucha.kamil.memo.MESSAGE.PLAY";


    ImageView mImageView;
    String mCurrentPhotoPath;
    Button buttonPhotos;
    ArrayList<String> photosPathsList;
    LinearLayout linearLayout;

    ArrayList<Bitmap> photosBitmaps;





    ImageView imageView;
    boolean visibility;
    private View.OnClickListener buttonPhotosOnClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonPhotosClicked();
        }
    };

    private void buttonPhotosClicked() {
       // Intent intent = new Intent(this, PhotosActivity.class);
        //startActivity(intent);

        for(String photo : photosPathsList){
            ImageView image = new ImageView(this);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(200,200));

            image.setMaxHeight(150);
            image.setMaxWidth(180);
            image.setImageBitmap(setPicA(photo, 200, 200 ));

            // Adds the view to the layout
            linearLayout.addView(image);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.imageView);
        imageView = findViewById(R.id.imageView2);
        buttonPhotos = findViewById(R.id.buttonPhotosActivity);
        buttonPhotos.setOnClickListener(buttonPhotosOnClickLister);
        linearLayout = findViewById(R.id.linearLayout);
        visibility= true;

        photosPathsList = new ArrayList<>();
        setPhotosPaths();

        photosBitmaps = new ArrayList<>();


    }

    private void setPhotosPaths() {
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232900_1353253075041324650.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232911_7516277580188711580.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232919_7853124197918384117.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232926_5093673749734754174.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232932_5850405677871970940.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232938_2119335713772628882.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232946_444297017623842049.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232954_7617219511505937433.jpg");
    }

    public void onClickImage(View v){
        if(visibility)
        imageView.setImageResource(R.drawable.ic_brightness_1_black_24dp);
        else
            imageView.setImageResource(R.drawable.ic_action_name);

        visibility = !visibility;

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
           //  Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
           // mImageView.setImageBitmap(imageBitmap);
           // photosBitmaps.add(imageBitmap);
            photosPathsList.add(mCurrentPhotoPath);
            setPic();

        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);

    }

    private Bitmap setPicA(String mCurrentPhotoPath, int width, int height) {
        // Get the dimensions of the View
       // int targetW = mImageView.getWidth();
       // int targetH = mImageView.getHeight();
        int targetW = width;
        int targetH = height;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        //mImageView.setImageBitmap(bitmap);
        return bitmap;

    }

    public void onClickButtonTakePhoto(View view) {
        dispatchTakePictureIntent();
    }

    public void onClickPlay(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putStringArrayListExtra(EXTRA_MESSAGE, photosPathsList);
        startActivity(intent);
    }

}




