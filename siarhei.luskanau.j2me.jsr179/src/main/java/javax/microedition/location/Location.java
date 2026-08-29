package javax.microedition.location;

public abstract class Location {

    public static final int MTE_SATELLITE = 0x00000001;
    public static final int MTE_TIMEDIFFERENCE = 0x00000002;
    public static final int MTE_TIMEOFARRIVAL = 0x00000004;
    public static final int MTE_CELLID = 0x00000008;
    public static final int MTE_SHORTRANGE = 0x00000010;
    public static final int MTE_ANGLEOFARRIVAL = 0x00000020;
    public static final int MTY_TERMINALBASED = 0x00010000;
    public static final int MTY_NETWORKBASED = 0x00020000;
    public static final int MTA_ASSISTED = 0x00040000;
    public static final int MTA_UNASSISTED = 0x00080000;

    protected Location() {
    }

    public abstract boolean isValid();

    public abstract QualifiedCoordinates getQualifiedCoordinates();

    public abstract float getSpeed();

    public abstract float getCourse();

    public abstract long getTimestamp();

    public abstract int getLocationMethod();

    public abstract AddressInfo getAddressInfo();

    public abstract String getExtraInfo(String mimetype);
}
