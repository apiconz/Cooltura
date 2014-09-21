package pe.gdgopenlima.cooltura.helpers;

/**
 * Created by armando on 21/09/14.
 */
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import pe.gdgopenlima.cooltura.entities.Place;

public class DatabaseConfig extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[] {
            Place.class
    };

    public static void main(String[] args) throws Exception {
        writeConfigFile("cooltura_config.txt", classes);
    }
}