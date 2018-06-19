package edu.phoenix.mbl404.mbl404_dck_week_2;

import android.content.Context;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Workout {

    //Class variables

    private String wType, wDate;
    private double wDistance;

    //Constructor inserts values at invocation, no need for accessors or mutators.

    public Workout (String iType, double iDistance, String iDate){
        this.wType = iType;
        this.wDistance = iDistance;
        this.wDate = iDate;
    }

    //toString method has been modified to produce a single returned line with commas as separators.

    @Override
    public String toString() {
        return wDate + ", " + wType + ", " + wDistance + "\n";
    }

    /*
    writeToFile first checks to see if the flat file exists, if it does, append mode is declared and
    the toString entry is added to the end of the file.  If not, it creates a new file and the entry
    is used as first record.
     */

    public void writeToFile (Context context) {
        File file = new File(context.getFilesDir(), MainActivity.FILE_NAME);
        if (file.exists()) {
            try {
                FileOutputStream fos = context.openFileOutput(MainActivity.FILE_NAME, Context.MODE_APPEND);
                fos.write(this.toString().getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileOutputStream fos = context.openFileOutput(MainActivity.FILE_NAME, Context.MODE_PRIVATE);
                fos.write(this.toString().getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
