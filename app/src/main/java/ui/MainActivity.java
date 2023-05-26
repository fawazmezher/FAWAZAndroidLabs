package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.mezh0013.R;
import algonquin.cst2335.mezh0013.data.MainViewModel;
import algonquin.cst2335.mezh0013.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.mybutton.setOnClickListener(click ->
        {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });
        model.editString.observe(this, s -> {
            variableBinding.textview.setText("Your edit text has: " + s);
        });

        model.isSelected.observe(this, selected -> {
            variableBinding.CheckBox.setChecked(selected);
            variableBinding.RadioButton.setChecked(selected);
            variableBinding.Switch.setChecked(selected);

            String message = "The value is now: " + selected;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
        variableBinding.CheckBox.setOnCheckedChangeListener((CheckBox, isChecked)-> {
            model.isSelected.postValue(variableBinding.CheckBox.isChecked());
        });

        variableBinding.RadioButton.setOnCheckedChangeListener((RadioButton, isChecked)-> {
            model.isSelected.postValue(variableBinding.RadioButton.isChecked());
        });

        variableBinding.Switch.setOnCheckedChangeListener((Switch, isChecked)-> {
            model.isSelected.postValue(variableBinding.Switch.isChecked());
        });

        variableBinding.ImageView.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "ImageView clicked", Toast.LENGTH_SHORT).show();
        });
        variableBinding.ImageButton.setOnClickListener(view -> {
            int width = view.getWidth();
            int height = view.getHeight();
            String message = "The width = " + width + " and height = " + height;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });

    }

}