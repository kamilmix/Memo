package pl.lodz.uni.math.kamilmucha.memo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    boolean visibility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView2);
        visibility= true;
    }

    public void onClickImage(View v){
        if(visibility)
        imageView.setImageResource(R.drawable.ic_brightness_1_black_24dp);
        else
            imageView.setImageResource(R.drawable.ic_action_name);

        visibility = !visibility;

    }
}

