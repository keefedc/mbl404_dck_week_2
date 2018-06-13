package edu.phoenix.mbl404.mbl404_dck_week_2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button addActivityButton;
    static EditText addDate;
    EditText addDistance;
    ImageView addDateCalendar;
    Spinner activitySpinner;
    TextView data;
    ConstraintLayout mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.sisyphus);

        mainContainer = (ConstraintLayout)findViewById(R.id.main);
        addActivityButton = (Button)findViewById(R.id.add_activity_button);
        addDateCalendar = (ImageView) findViewById(R.id.add_date_calendar);
        addDate = (EditText)findViewById(R.id.add_date);
        addDistance = (EditText)findViewById(R.id.add_distance);
        data = (TextView)findViewById(R.id.data_view);

        activitySpinner = (Spinner)findViewById(R.id.add_activity);



        addActivitySelect();
        addActivity();
        dateAdd();
    }

    public void addActivity(){
        addActivityButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        if (activitySpinner.getSelectedItemPosition() == 0){
                            Toast.makeText(MainActivity.this, "Invalid selection: You must choose an activity.", Toast.LENGTH_SHORT).show();
                            activitySpinner.setBackgroundResource(R.drawable.activity_error_highlight);
                        } else if (addDistance.getText().toString().matches("")){
                            Toast.makeText(MainActivity.this, "Invalid selection: You must enter a distance.", Toast.LENGTH_SHORT).show();
                            activitySpinner.setBackgroundResource(R.drawable.box_border);
                            addDistance.setBackgroundResource(R.drawable.activity_error_highlight);
                            addDistance.requestFocus();
                        } else if (addDate.getText().toString().matches("")){
                            Toast.makeText(MainActivity.this, "Invalid selection: You must enter a date.", Toast.LENGTH_SHORT).show();
                            activitySpinner.setBackgroundResource(R.drawable.box_border);
                            addDistance.setBackgroundResource(R.drawable.box_border);
                            addDate.setBackgroundResource(R.drawable.activity_error_highlight);
                            addDate.requestFocus();
                        }

                        else {
                            String getActivity = activitySpinner.getSelectedItem().toString();
                            data.setText(getActivity);
                            clearFields();
                        }







                        //showDatePickerDialog(v);
                    }
                }
        );
    }

    public void clearFields(){
        activitySpinner.setSelection(0);
        activitySpinner.setBackgroundResource(R.drawable.box_border);
        addDistance.setText("");
        addDistance.setBackgroundResource(R.drawable.box_border);
        addDate.setText("");
        addDate.setBackgroundResource(R.drawable.box_border);
        mainContainer.requestFocus();
    }

    public void dateAdd(){
        addDateCalendar.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        showDatePickerDialog(v);
                    }
                }
        );
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            addDate.setText(month + "/" + day + "/" + year);
        }
    }

    public void addActivitySelect(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_text);
        activitySpinner.setAdapter(adapter);
        activitySpinner.setSelection(0);
        activitySpinner.setOnItemSelectedListener(new SpinnerActivity());
    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            activitySpinner.setBackgroundResource(R.drawable.box_border);
        }

        public void onNothingSelected(AdapterView<?> parent) { }
    }

}
