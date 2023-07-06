package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.mezh0013.R;
import algonquin.cst2335.mezh0013.data.MainViewModel;
import algonquin.cst2335.mezh0013.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("MainActivity", "In onCreate() - Loading Widgets");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "In onCreate() - Loading Widgets");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "In onCreate() - Loading Widgets");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity", "In onCreate() - Loading Widgets");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("MainActivity", "In onCreate() - Loading Widgets");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w("MainActivity", "In onCreate() - Loading Widgets");
        Log.d(TAG, "Message");
        Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);


        Button button = (Button) findViewById(R.id.button);
        EditText email = findViewById(R.id.emailEditText);
        EditText emailEditText = (EditText) findViewById(R.id.emailEditText);
        EditText password = findViewById(R.id.passwordEditText);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");

        email.setText(prefs.getString("LoginName", ""));


        button.setOnClickListener(clk -> {
            String getEmail = email.getText().toString();
            String getPassword = password.getText().toString();

            if (getEmail.trim().equals("") || getPassword.trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter Right Credentials", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email", getEmail);
                editor.putString("password", getPassword);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                nextPage.putExtra("EmailAddress", ((EditText) emailEditText).getText().toString());
                startActivity(nextPage);
                return;

            }

//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString("LoginName", "");
//
//
//            startActivity(nextPage);

        });


    }
}