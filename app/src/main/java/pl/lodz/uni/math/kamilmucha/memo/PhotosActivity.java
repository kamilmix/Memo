package pl.lodz.uni.math.kamilmucha.memo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class PhotosActivity extends AppCompatActivity {

    private ArrayList<String> photosPathsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        Intent intent = getIntent();
        photosPathsList = intent.getStringArrayListExtra(MainActivity.EXTRA_MESSAGE);



        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, photosPathsList));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(PhotosActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();




            }
        });

    }
}
