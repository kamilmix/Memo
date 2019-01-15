package pl.lodz.uni.math.kamilmucha.memo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> photosPathsList;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public ImageAdapter(Context c, ArrayList photosList){
        mContext = c;
        photosPathsList = photosList;
    }

    public int getCount() {
        return photosPathsList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

       // imageView.setImageResource(mThumbIds[position]);
       Bitmap bitmap = setPicA(photosPathsList.get(position),200,200);
        imageView.setImageBitmap(bitmap);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    imageView.setImageResource(R.drawable.ic_brightness_1_black_24dp);

            }
        });

        return imageView;
/*
        ImageButton button;

        if(convertView == null){
            button = new ImageButton(mContext);
            button.setLayoutParams(new ViewGroup.LayoutParams(200,200));
            button.setPadding(8, 8, 8, 8);
        }
        else{
            button = (ImageButton) convertView;
        }

        button.setImageBitmap(setPicA(photosPathsList.get(position),200,200));
        button.bringToFront();

        return button;*/
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


}