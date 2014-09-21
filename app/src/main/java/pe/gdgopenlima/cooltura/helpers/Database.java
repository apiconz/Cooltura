package pe.gdgopenlima.cooltura.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.UUID;

import pe.gdgopenlima.cooltura.R;
import pe.gdgopenlima.cooltura.entities.Place;

/**
 * Created by armando on 21/09/14.
 */
public class Database extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "cooltura.sqlite";
    private static final int DATABASE_VERSION = 1;

    protected static Logger logger = LoggerFactory.getLogger(OrmLiteSqliteOpenHelper.class);
    protected AndroidConnectionSource connectionSource = new AndroidConnectionSource(this);

    protected boolean cancelQueriesEnabled;
    private volatile boolean isOpen = true;

    public final RuntimeExceptionDao<Place, UUID> entity;


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        entity = getRuntimeExceptionDao(Place.class);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Place.class);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {

    }

    /**
     * Get a DAO for our class. This uses the {@link DaoManager} to cache the DAO for future gets.
     *
     * <p>
     * NOTE: This routing does not return Dao<T, ID> because of casting issues if we are assigning it to a custom DAO.
     * Grumble.
     * </p>
     */
    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        // special reflection fu is now handled internally by create dao calling the database type
        Dao<T, ?> dao = DaoManager.createDao(getConnectionSource(), clazz);
        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }


    private static InputStream openFileId(Context context, int fileId) {
        InputStream stream = context.getResources().openRawResource(fileId);
        if (stream == null) {
            throw new IllegalStateException("Could not find object config file with id " + fileId);
        }
        return stream;
    }

    private static InputStream openFile(File configFile) {
        try {
            if (configFile == null) {
                return null;
            } else {
                return new FileInputStream(configFile);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not open config file " + configFile, e);
        }
    }

}
