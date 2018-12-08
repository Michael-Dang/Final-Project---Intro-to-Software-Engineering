package ceg4110_f18_g19.isitfood.client;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Set the URL to connect to according to the value stored in the default preferences file
        ServerConfiguration.getInstance().setURL(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_url", "18.224.175.235"));
        setContentView(R.layout.activity_main);
        Button imagePicker = findViewById(R.id.selectImageButton);
        imagePicker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 42);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData)
    {
        if (requestCode == 42 && resultCode == RESULT_OK)
        {
            Uri uri = null;
            if (resultData != null)
            {
                uri = resultData.getData();
            }
            if (uri != null)
            {
                try
                {
                    InputStream stream = getContentResolver().openInputStream(uri);
                    ArrayList<InputStream> selection = new ArrayList<>();
                    selection.add (stream);
                    ResponseDecoder.decode(this, CommunicationModule.request(CommunicationModule.encode(selection)), BitmapFactory.decodeStream(stream));
                } catch (Exception e) {
                    Toast.makeText(this, "Could not open " + uri.getPath() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
