package edu.phoenix.mbl404.mbl404_dck_week_2;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Datret {

    //Class variables
    private ArrayList<LineObject> ouput = new ArrayList<>();
    private ArrayList<String> preSort = new ArrayList<>();

    //Constructor checks for file and then starts an input stream.

    public Datret (Context context) throws Exception{
        File file = new File(context.getFilesDir(), MainActivity.FILE_NAME);
        if(file.exists()){
            FileInputStream fin = new FileInputStream(file);

            //Function call returns ArrayList of lines from file.

            preSort = convertStreamToString(fin);

            //Each line is translated into an LineObject and stored into a ListArray of objects.

            for (String input: preSort) {

                //Each line is split into an array of three values by finding commas.

                String[] temp = input.split(", ");
                LineObject line = new LineObject(temp[1], temp[2], temp[0]);
                ouput.add(line);
            }

            //ListArray of objects are sorted in ascending order via custom Comparator.

            Collections.sort(ouput, LineObject.LineComparator);

            fin.close();
        }
    }

    //convertStreamToString buffers incoming data and returns an array holding one line each.

    public static ArrayList<String> convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        ArrayList<String> input = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                input.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    //Accessor for pulling sorted data back into the MainActivity.

    public ArrayList<LineObject> getOuput(){
        return this.ouput;
    }

    /*
    LineObject serves a twofold purpose, consolidating information into an object thus allowing
    for an override toString function for a custom print out AND a place to set up a string
    Comparator.
     */


    public static class LineObject {

        //Class variables.

        private String activity, distance, date;

        //Constructor simply assigns values.

        LineObject(String inAct, String inDis, String inDat){
            this.activity = inAct;
            this.distance = inDis;
            this.date = inDat;
        }

        //Accessor is needed as this is the item being used as the point of comparison.

        public String getDate() { return date; }

        //Invoking a new Comparator allows us to override the compare function.

        public static Comparator<LineObject> LineComparator = new Comparator<LineObject>() {

            @Override
            public int compare(LineObject lone, LineObject ltwo) {
                String lineOne = lone.getDate();
                String lineTwo = ltwo.getDate();

                //ascending order
                //return lineOne.compareTo(lineTwo);

                //descending order
                return lineTwo.compareTo(lineOne);
            }};

        @Override
        public String toString() {
            return "Date:  " + this.date + " | Activity:  " + this.activity + " | Distance:  " +
            this.distance + " mile(s)";
        }

    }
}
