package assignment.com.spotabee;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;



public class DescriptionForm extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private AppCompatButton submit;
    private AppCompatEditText location;
    private AppCompatEditText flower;
    private AppCompatEditText description;
    private AppCompatTextView changesTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);
        this.sharedPreferences = this.getPreferences(MODE_PRIVATE);
        this.submit = findViewById(R.id.submit);
        this.flower = findViewById(R.id.flowerField);
        this.location = findViewById(R.id.locationField);
        this.description = findViewById(R.id.descriptionField);
        this.changesTextView = findViewById(R.id.num_changes_text);
    }
}
