package ceg4110_f18_g19.isitfood.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    private TextView confidencePercentage,
    confidenceFlavorText;
    private ImageView image;
    private ProgressBar confidenceMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        confidencePercentage = findViewById(R.id.confidencePercentage);
        confidenceFlavorText = findViewById(R.id.confidenceFlavorText);
        image = findViewById(R.id.analyzedImage);
        Intent intent = getIntent();
        load((Bitmap)intent.getParcelableExtra("ceg4110_f18_g19.isitfood.BITMAP"), intent.getStringExtra("ceg4110_f18_g19.isitfood.RESULT"));
    }

    private void load(Bitmap bmp, String results)
    {
        String[] splitResults = results.split(" ");
        long confidence = Math.round(clamp(50 + (25 * (Double.parseDouble(splitResults[0]) - Double.parseDouble(splitResults[1]))), 0, 100));
        image.setImageBitmap(bmp);
        confidenceMeter.setProgress((int)confidence);
        confidencePercentage.setText(Long.toString(confidence));
        if(confidence<=10) confidenceFlavorText.setText("Definitely Not Food");
        else if(confidence>10 && confidence<=25) confidenceFlavorText.setText("Probably Not Food");
        else if(confidence>25 && confidence<=40) confidenceFlavorText.setText("Maybe Not Food");
        else if(confidence>40 && confidence<=60) confidenceFlavorText.setText("Very Ambiguous");
        else if(confidence>60 && confidence<=75) confidenceFlavorText.setText("Maybe Food");
        else if(confidence>75 && confidence<=90) confidenceFlavorText.setText("Probably Food");
        else if(confidence>90) confidenceFlavorText.setText("Definitely Food");
        else confidenceFlavorText.setText("Error Calculating Confidence");
    }

    public static double clamp(double x, double min, double max)
    {
        if(x>max) return max;
        if(x<min) return min;
        return x;
    }
}
