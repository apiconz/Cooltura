package pe.gdgopenlima.cooltura.helpers;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import pe.gdgopenlima.cooltura.entities.Place;

/**
 * Created by armando on 21/09/14.
 */
public class CSVConverter {
    //
    public static InputStream getInputStream(Context context, String fileIdentifier) {
        InputStream ins = context.getResources().openRawResource(
                context.getResources().getIdentifier("raw/" + fileIdentifier,
                        "raw", context.getPackageName()));
        return ins;
    }

    public static ArrayList<Place> readPlaces(InputStream is) {
        ArrayList<Place> places = new ArrayList<Place>();
        CSVReader reader = new CSVReader(new InputStreamReader(is));
        try {
            String[] line;
            while ((line = reader.readNext()) != null) {
                //Process data
                Place place = new Place();
                place.category = line[0];
                place.name = line[1];
                place.address = line[2];
                place.district = line[3];
                place.longitude = Float.parseFloat(line[9]);
                place.latitude = Float.parseFloat(line[10]);
                places.add(place);
            }
        } catch (IOException ex) {
            //
        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
        return places;
    }

}