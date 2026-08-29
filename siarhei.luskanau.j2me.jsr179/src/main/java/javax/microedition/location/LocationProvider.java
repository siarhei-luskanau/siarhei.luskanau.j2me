package javax.microedition.location;

public abstract class LocationProvider {

    public static final int AVAILABLE = 1;
    public static final int TEMPORARILY_UNAVAILABLE = 2;
    public static final int OUT_OF_SERVICE = 3;

    protected LocationProvider() {
    }

    public static LocationProvider getInstance(Criteria criteria) throws LocationException {
        throw new LocationException("javax.microedition.location is a compile-time stub; no runtime provider is available");
    }

    public static Location getLastKnownLocation() {
        return null;
    }

    public static void addProximityListener(ProximityListener listener, Coordinates coordinates,
            float proximityRadius) throws LocationException {
        throw new LocationException("javax.microedition.location is a compile-time stub; no runtime provider is available");
    }

    public static void removeProximityListener(ProximityListener listener) {
    }

    public abstract int getState();

    public abstract Location getLocation(int timeout) throws LocationException, InterruptedException;

    public abstract void setLocationListener(LocationListener listener, int interval, int timeout, int maxAge);

    public abstract void reset();
}
