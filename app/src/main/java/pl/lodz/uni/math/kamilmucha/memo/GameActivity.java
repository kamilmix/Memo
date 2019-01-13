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
    private ArrayList<String> photosPathsList;
        ImageView curView = null;
        private int countPair = 0;
       /* final int[] drawable = new int[] {
                R.drawable.sample_0,
                R.drawable.sample_1,
                R.drawable.sample_2,
                R.drawable.sample_3,
                R.drawable.sample_4,
                R.drawable.sample_5,
                R.drawable.sample_6,
                R.drawable.sample_7
        };
        */
        int[] pos = {0,1,2,3,4,5,6,7,0,1,2,3,4,5,6,7};
        int currentPos = -1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

            Intent intent = getIntent();
            photosPathsList = intent.getStringArrayListExtra(MainActivity.EXTRA_MESSAGE);

            shuffleArray(pos);

            ImageAdapter2 imageAdapter = new ImageAdapter2(this);
            GridView gridView = (GridView)findViewById(R.id.gridviewAB);
            gridView.setAdapter(imageAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (currentPos < 0 ) {
                        currentPos = position;
                        curView = (ImageView) view;
                       // ((ImageView) view).setImageResource(drawable[pos[position]]);
                        Bitmap bitmap = setPicA(photosPathsList.get(pos[position]), 250,250);
                        ((ImageView) view).setImageBitmap(bitmap);

                    }
                    else {
                        if (currentPos == position) {
                            ((ImageView) view).setImageResource(R.drawable.ic_photo_black_24dp);


                        } else if (pos[currentPos] != pos[position]) {

                            Toast.makeText(GameActivity.this, "Not Match!", Toast.LENGTH_LONG).show();

                            Bitmap bitmap = setPicA(photosPathsList.get(pos[position]), 250,250);
                            ((ImageView) view).setImageBitmap(bitmap);
                            final View viewHandler = view;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    ((ImageView) viewHandler).setImageResource(R.drawable.ic_photo_black_24dp);
                                    curView.setImageResource(R.drawable.ic_photo_black_24dp);
                                }
                            }, 1000); // Millisecond 1000 = 1 sec





                        } else {
                            //((ImageView) view).setImageResource(drawable[pos[position]]);
                            Bitmap bitmap = setPicA(photosPathsList.get(pos[position]), 250,250);
                            ((ImageView) view).setImageBitmap(bitmap);
                            countPair++;
                            if (countPair == 8) {
                                Toast.makeText(GameActivity.this, "You Win!", Toast.LENGTH_LONG).show();
                            }
                        }
                        currentPos = -1;
                    }
                }
            });
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
    private static void shuffleArray(int[] array)
    {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
    }

