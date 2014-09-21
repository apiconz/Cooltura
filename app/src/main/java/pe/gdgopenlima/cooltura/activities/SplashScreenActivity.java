package pe.gdgopenlima.cooltura.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import pe.gdgopenlima.cooltura.R;
import pe.gdgopenlima.cooltura.entities.Place;
import pe.gdgopenlima.cooltura.helpers.CSVConverter;
import pe.gdgopenlima.cooltura.helpers.Database;


public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Database db = new Database(this);
        Dao<Place, Integer> dao;
        try {
            dao = db.getDao(Place.class);
            ArrayList<Place> metroBusStops = CSVConverter.readPlaces(CSVConverter.getInputStream(this.getApplicationContext(), "cooltura"));
            for (Place stop : metroBusStops) {
                dao.create(stop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
