package ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.mezh0013.R;


public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Intent fromPrevious = getIntent();
        Intent call = new Intent(Intent.ACTION_DIAL);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        TextView welcome = findViewById(R.id.textView3);
        ImageView profilePicture = findViewById(R.id.imageViewCamera);
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        welcome.setText("welcome " + emailAddress);
        EditText phoneNumber = findViewById(R.id.editTextPhone);

        //shared preferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefs.getString("VariableName", emailAddress);

        File file = new File(getFilesDir(), "Picture.png");
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile("Picture.png");
            profilePicture.setImageBitmap(theImage);
        }

        Button button = (Button) findViewById(R.id.buttonCallNumber);
        button.setOnClickListener(clk -> {
            String getCallNumber = button.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", getCallNumber);
            editor.apply();
            call.setData(Uri.parse("tel:" + phoneNumber.getText()));
            startActivity(call);
        });

        Button changePic = (Button) findViewById(R.id.buttonChangePicture);
        changePic.setOnClickListener(clk -> {
            String getChangePicture = button.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", getChangePicture);
            editor.apply();
            startActivity(cameraIntent);
        });

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    ImageView profileImage = findViewById(R.id.imageViewCamera);
                    profileImage.setImageBitmap(thumbnail);

                    FileOutputStream fOut = null;
                    try {
                        fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);

                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        cameraResult.launch(cameraIntent);
    }
}