package edu.phoenix.mbl404.mbl404_dck_week_2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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
import java.util.ArrayList;
import java.util.Calendar;

/*
Implementation of TextWatcher needed for on-the-fly data validation.  Requires override of
onTextChange, beforeTextChange, and afterTextChange.
 */


public class MainActivity extends AppCompatActivity implements TextWatcher {

    //Constant for flat file storage.

    public static final String FILE_NAME = "workout.txt";

    //Activity variables.

    //Layout items (in order of appearance).
    ConstraintLayout mainContainer;
    Spinner activitySpinner;
    EditText addDistance;
    static EditText addDate;
    ImageView addDateCalendar;
    Button addActivityButton;
    TextView data;

    //Program entry point.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize first layout.

        setContentView(R.layout.activity_main);

        //Bind action bar to variable and bind program Icon to the bar (pure visual/marketing).

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.sisyphus);

        //Bind java variable layout items to layout xml items (in order of appearance).

        mainContainer = (ConstraintLayout)findViewById(R.id.main);//Used to prevent wandering focus
        activitySpinner = (Spinner)findViewById(R.id.add_activity);
        addDistance = (EditText)findViewById(R.id.add_distance);
        addDate = (EditText)findViewById(R.id.add_date);
        addDateCalendar = (ImageView) findViewById(R.id.add_date_calendar);
        addActivityButton = (Button)findViewById(R.id.add_activity_button);
        data = (TextView)findViewById(R.id.data_view);
        data.setMovementMethod(new ScrollingMovementMethod());

        //Bind the textChanged listener to the java variable representing the field.

        addDate.addTextChangedListener(this);

        //Call function to create and bind an adapter to the activitySpinner.

        addActivitySelect();

        //Call function to set listener for date selection fragment.

        dateAdd();

        //Call function to process adding an activity.

        addActivity();

        //Call function to retrieve, sort, and display data from flat file.

        try {
            printData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    All three TextChanged overrides are required to ensure the TextChanged listener can function
    properly.  All processing happens within the onTextChanged as the employment of the function
    is used to insert back-slashes between days, months, and years.
     */

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String working = s.toString();

        //Boolean isValid is used to display error messaging.

        boolean isValid = true;

        /*
        Outer if-else finds the location of the cursor within the field based on length of the
        working string, which is fetched every time a new change is made.  The inner if-else
        conditional statements test for validity of the data and insert back-slashes where
        appropriate.
         */

        //Check to ensure length is at least 2.

        if (working.length()==2 && before ==0) {

            //Checks to see that the month falls within the 12 month Gregorian calender.

            if (Integer.parseInt(working) < 1 || Integer.parseInt(working)>12) {
                isValid = false;
            } else {
                working+="/";
                addDate.setText(working);
                addDate.setSelection(working.length());
            }

        //Checks to ensure length is at least 5 (month plus a single slash plus two value day).

        } else if(working.length() == 5 && before == 0) {
            String enteredDate = working.substring(3);

            //Checks to see that the date falls within a rational range.

            if (Integer.parseInt(enteredDate) < 1 || Integer.parseInt(enteredDate) > 31) {
                isValid = false;
            } else {
                working+="/";
                addDate.setText(working);
                addDate.setSelection(working.length());
            }

        //Checks to ensure length is at least 10 ( MM/DD/YYYY ).

        } else if (working.length()==10 && before ==0) {
            String enteredYear = working.substring(6);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            //Checks to ensure the year is not greater than the current year.

            if (Integer.parseInt(enteredYear) > currentYear) {
                isValid = false;
            }

        //Final check to ensure total length is exactly 10.

        } else if (working.length()!=10) {
            isValid = false;
        }

        //When isValid is false, the error message will appear next to the field.

        if (!isValid) {
            addDate.setError("Enter a valid date: MM/DD/YYYY");
        } else {
            addDate.setError(null);
        }

    }

    //Override applied only to gain onTextChanged functionality.

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    //Override applied only to gain onTextChanged functionality.

    @Override
    public void afterTextChanged(Editable s) {  }

    /*
    addActivitySelect invokes the spinner adapter, sets dropdown options via array in values,
    dropdown layout, and initial position
    */

    public void addActivitySelect(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_text);
        activitySpinner.setAdapter(adapter);
        activitySpinner.setSelection(0);
        activitySpinner.setOnItemSelectedListener(new SpinnerActivity());
    }

    /*
    SpinnerActivity class allows for various behaviors to be monitored by the listener, specifically
    position selection data.  Also used to remove error background from spinner after valid
    selection
     */

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            activitySpinner.setBackgroundResource(R.drawable.box_border);
        }

        public void onNothingSelected(AdapterView<?> parent) { }
    }

    //dateAdd sets onClickListener for the calender image, starting the DatePicker fragment.

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

    //showDatePickerDialog starts new fragment to select the date.

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //DatePickerFragment class handles the details of invoking the DatePicker

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

        //onDateSet formats the users selection into a string and sets the addDate field.

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String mo, da;
            if (month >= 10){
                mo = Integer.toString(month);
            } else {
                mo = "0" + Integer.toString(month);
            }
            if (day >= 10){
                da = Integer.toString(day);
            } else {
                da = "0" + Integer.toString(day);
            }
            addDate.setText(mo + "/" + da + "/" + year);
        }
    }

    /*
    Warning:  addActivity handles not only validation (both visual and text ques) but data writing
    and retrieval from a .txt file.
     */

    public void addActivity(){
        addActivityButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        //Tiered validation checks.

                        if (activitySpinner.getSelectedItemPosition() == 0){
                            Toast.makeText(MainActivity.this, "Invalid selection: " +
                                    "You must choose an activity.", Toast.LENGTH_SHORT).show();
                            activitySpinner.setBackgroundResource(R.drawable.activity_error_highlight);
                        } else if (addDistance.getText().toString().matches("")){
                            Toast.makeText(MainActivity.this, "Invalid selection: " +
                                    "You must enter a distance.", Toast.LENGTH_SHORT).show();
                            activitySpinner.setBackgroundResource(R.drawable.box_border);
                            addDistance.setBackgroundResource(R.drawable.activity_error_highlight);
                            addDistance.requestFocus();
                        } else if (addDate.getText().toString().matches("")){
                            Toast.makeText(MainActivity.this, "Invalid selection: " +
                                            "You must enter a date.", Toast.LENGTH_SHORT).show();
                            activitySpinner.setBackgroundResource(R.drawable.box_border);
                            addDistance.setBackgroundResource(R.drawable.box_border);
                            addDate.setBackgroundResource(R.drawable.activity_error_highlight);
                            addDate.requestFocus();
                        }

                        //Document write and text field refresh.

                        else {
                            String getActivity = activitySpinner.getSelectedItem().toString();
                            data.setText(getActivity);
                            Workout workout = new Workout(getActivity,
                                    Double.parseDouble(addDistance.getText().toString()),
                                    addDate.getText().toString());
                            workout.writeToFile(getApplicationContext());

                            //Call function to refresh, sort, and display data from flat file.

                            try {
                                printData();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            clearFields();
                        }
                    }
                }
        );
    }

    /*
    Datret (Data Retrieval) is a class that pulls data from the field and parses it for general
    consumption.  A string builder is used to concatenate the lines of strings held by the
    LineObject
     */

    public void printData() throws Exception{
        Datret datret = new Datret(getApplicationContext());
        ArrayList<Datret.LineObject> temp = datret.getOuput();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < temp.size(); i++){
            sb.append(temp.get(i).toString()).append("\n");
        }
        data.setBackgroundResource(R.drawable.box_border);
        data.setText(sb.toString());
    }

    /*
    Simple reset function to clear all fields, reset all background behaviors, and return spinner
    selection to default.
     */

    public void clearFields(){
        activitySpinner.setSelection(0);
        activitySpinner.setBackgroundResource(R.drawable.box_border);
        addDistance.setText("");
        addDistance.setBackgroundResource(R.drawable.box_border);
        addDate.setText("");
        addDate.setBackgroundResource(R.drawable.box_border);
        addDate.setError(null);
        data.requestFocus();
    }
}
