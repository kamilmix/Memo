package pl.lodz.uni.math.kamilmucha.memo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private static final int PHOTO_WIDTH = 250;
    private static final int PHOTO_HEIGHT = 250;

    private ArrayList<Bitmap> photoBitmaps;
    private ImageView currentView = null;
    private int countPair = 0;

    private int[] position = {0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7};
    private int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        ArrayList<String> photoFilePaths = intent.getStringArrayListExtra(MainActivity.EXTRA_MESSAGE);
        photoBitmaps = convertListToBitmap(photoFilePaths);
        shuffleArray(position);

        ImageAdapter imageAdapter = new ImageAdapter(this);
        GridView gridView = findViewById(R.id.gridviewAB);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(gridViewOnItemClickListener);

    }

    private ArrayList<Bitmap> convertListToBitmap(ArrayList<String> photoFilePaths) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (String photo : photoFilePaths) {
            bitmaps.add(photoToBitmap(photo));
        }
        return bitmaps;
    }

    private AdapterView.OnItemClickListener gridViewOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (currentPosition < 0) {
                currentPosition = position;
                currentView = (ImageView) view;
                Bitmap bitmap = photoBitmaps.get(GameActivity.this.position[position]);
                ((ImageView) view).setImageBitmap(bitmap);

            } else {
                if (currentPosition == position) {
                    ((ImageView) view).setImageResource(R.drawable.ic_photo_black_24dp);
                } else if (GameActivity.this.position[currentPosition] != GameActivity.this.position[position]) {
                    Toast.makeText(GameActivity.this, "Not Match!", Toast.LENGTH_LONG).show();

                    Bitmap bitmap = photoBitmaps.get(GameActivity.this.position[position]);
                    ((ImageView) view).setImageBitmap(bitmap);
                    final View viewHandler = view;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) viewHandler).setImageResource(R.drawable.ic_photo_black_24dp);
                            currentView.setImageResource(R.drawable.ic_photo_black_24dp);
                        }
                    }, 800); // Millisecond 1000 = 1 sec

                } else {
                    Bitmap bitmap = photoBitmaps.get(GameActivity.this.position[position]);
                    ((ImageView) view).setImageBitmap(bitmap);
                    countPair++;
                    if (countPair == 8) {
                        Toast.makeText(GameActivity.this, "You Win!", Toast.LENGTH_LONG).show();
                    }
                }
                currentPosition = -1;
            }
        }
    };

    private Bitmap photoToBitmap(String mCurrentPhotoPath) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / GameActivity.PHOTO_WIDTH, photoH / GameActivity.PHOTO_HEIGHT);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }

    private static void shuffleArray(int[] array) {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}

