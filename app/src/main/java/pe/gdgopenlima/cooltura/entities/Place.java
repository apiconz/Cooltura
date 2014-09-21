package pe.gdgopenlima.cooltura.entities;

/**
 * Created by armando on 21/09/14.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "culturalplaces")
public class Place {

    @DatabaseField(columnName = "name")
    public String name;
    @DatabaseField(columnName = "category")
    public String category;
    @DatabaseField(columnName = "address")
    public String address;
    @DatabaseField(columnName = "district")
    public String district;
    @DatabaseField(columnName = "reference")
    public String reference;
    @DatabaseField(columnName = "latitude")
    public float latitude;
    @DatabaseField(columnName = "longitude")
    public float longitude;
    @DatabaseField(columnName = "visitingHour")
    public String visitingHour;
    @DatabaseField(columnName = "cost")
    public String cost;
    @DatabaseField(columnName = "phoneNumber")
    public String phoneNumber;
    @DatabaseField(columnName = "email")
    public String email;
    @DatabaseField(columnName = "url")
    public String url;
    @DatabaseField(columnName = "area")
    public String area;
    @DatabaseField(columnName = "altitude")
    public String altitude;
    @DatabaseField(columnName = "bibliography")
    public String bibliography;
    @DatabaseField(generatedId = true)
    int id;

    public Place() {
    }

    public Place(String name, float longitude, float latitude, String category, String address, String district) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
        this.address = address;
        this.district = district;
    }

    public String getName() {
        return this.name;
    }
}
