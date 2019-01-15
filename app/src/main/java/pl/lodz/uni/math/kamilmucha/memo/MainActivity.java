package pl.lodz.uni.math.kamilmucha.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PHOTO_HEIGHT = 500;
    private static final int PHOTO_WIDTH = 1000;

    public static final String EXTRA_MESSAGE = "com.mucha.kamil.memo.MESSAGE.PLAY";
    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mImageView;
    private String mCurrentPhotoPath;
    private Button buttonPhotos;
    private ArrayList<String> photosPathsList;
    private LinearLayout linearLayout;
    private TextView photosCount;

    private ArrayList<Bitmap> photosBitmaps;

    private View.OnClickListener buttonPhotosOnClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonPhotosClicked();
        }
    };

    private void buttonPhotosClicked() {
        setSamplePhotosPaths();
        for (String photo : photosPathsList) {
            linearLayout.addView(createPhotoImageView(photo));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPhotos = findViewById(R.id.buttonPhotosActivity);
        buttonPhotos.setOnClickListener(buttonPhotosOnClickLister);
        linearLayout = findViewById(R.id.linearLayout);
        photosCount = findViewById(R.id.textViewPhotosCount);

        photosPathsList = new ArrayList<>();
        photosBitmaps = new ArrayList<>();

    }

    private void updatePhotosCount() {
      //  photosCount.setText(photosPathsList.size());
        String size = String.valueOf(photosPathsList.size());
        photosCount.setText(size);
    }



    private void setSamplePhotosPaths() {
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232900_1353253075041324650.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232911_7516277580188711580.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232919_7853124197918384117.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232926_5093673749734754174.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232932_5850405677871970940.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232938_2119335713772628882.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232946_444297017623842049.jpg");
        photosPathsList.add("/storage/emulated/0/Android/data/pl.lodz.uni.math.kamilmucha.memo/files/Pictures/JPEG_20190113_232954_7617219511505937433.jpg");
        updatePhotosCount();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            photosPathsList.add(mCurrentPhotoPath);
            linearLayout.addView(createPhotoImageView(mCurrentPhotoPath));
            updatePhotosCount();
        }
    }

    private View createPhotoImageView(String mCurrentPhotoPath) {
        ImageView image = new ImageView(this);
        image.setLayoutParams(new android.view.ViewGroup.LayoutParams(PHOTO_WIDTH, PHOTO_HEIGHT));
        image.setPadding(8,8,8,8);
        image.setImageBitmap(photoToBitmap(mCurrentPhotoPath));

        return image;

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

    private Bitmap photoToBitmap(String mCurrentPhotoPath) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / MainActivity.PHOTO_WIDTH, photoH / MainActivity.PHOTO_HEIGHT);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

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
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);

    }

    public void onClickButtonTakePhoto(View view) {
        dispatchTakePictureIntent();
    }

    public void onClickPlay(View view) {
        if(photosPathsList.size() == 8){
            Intent intent = new Intent(this, GameActivity.class);
            intent.putStringArrayListExtra(EXTRA_MESSAGE, photosPathsList);
            startActivity(intent);
        }
        else {
            alertNotEnoughPhotos();
        }
    }

    private void alertNotEnoughPhotos() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("NOT ENOUGH PHOTOS")
                .setMessage("You must take 8 photos to can play.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                }).show();
    }

}




