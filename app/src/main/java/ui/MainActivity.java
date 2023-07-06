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


/**
 * @author Fawaz Mezher
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * This method is used in checking for special characters
     *
     * @param c object through which this method checks for special characters
     * @return returns true if specifications are met
     */
    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '?':
            case '*':
            case '@':
            case '%':
            case '^':
            case '&':
            case '!':
                return true;
            default:
                return false;
        }
    }


    /**
     * This method checks if password complexity input by a user, meets the requirements
     *
     * @param password string object through which this method checks if password
     *                 complexity, meets requirements.
     * @return Returns true if password typed in meets requirements
     */
    boolean CheckPasswordComplexity(String password) {
        TextView tv = findViewById(R.id.textView);

        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;

        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }
        /**
         * method for when uppercase is missing from the password
         */
        if (!foundUpperCase) {
            tv.setText("You shall not pass");
            Toast.makeText(getApplicationContext(), "an upper case is missing", Toast.LENGTH_SHORT).show();

            return false;
            /**
             * method for when lowercase is missing from the password
             */
        } else if (!foundLowerCase) {
            tv.setText("You shall not pass");
            Toast.makeText(getApplicationContext(), "a lower case is missing", Toast.LENGTH_SHORT).show();

            return false;
            /**
             * method for when a number is missing from the password
             */
        } else if (!foundNumber) {
            tv.setText("You shall not pass");
            Toast.makeText(getApplicationContext(), "a number character is missing", Toast.LENGTH_SHORT).show();

            return false;
            /**
             * method for when special character is missing from the password
             */
        } else if (!foundSpecial) {
            Toast.makeText(getApplicationContext(), "you are missing a special character", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass");
            return false;
            /**
             * method for when password meets criteria
             */
        } else
            tv.setText("You shall pass");
        return true;

    }

    /**
     * This function contains declared variables and calls function for checking password complexity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.editText);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();

            CheckPasswordComplexity(password);

        });
    }
}