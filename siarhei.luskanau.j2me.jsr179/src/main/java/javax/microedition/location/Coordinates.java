package javax.microedition.location;

// Compile-time stub of the JSR-179 Location API; the device's real implementation is used at runtime.
public class Coordinates {

    public static final int DD_MM = 1;
    public static final int DD_MM_SS = 2;

    private double latitude;
    private double longitude;
    private float altitude;

    public Coordinates(double latitude, double longitude, float altitude) {
        setLatitude(latitude);
        setLongitude(longitude);
        setAltitude(altitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public double azimuthTo(Coordinates to) {
        return 0;
    }

    public double distance(Coordinates to) {
        return 0;
    }

    public static double convert(String coordinate) {
        return 0;
    }

    public static String convert(double coordinate, int outputType) {
        return String.valueOf(coordinate);
    }
}
