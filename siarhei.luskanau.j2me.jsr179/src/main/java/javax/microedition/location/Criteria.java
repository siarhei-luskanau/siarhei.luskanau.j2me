package javax.microedition.location;

public class Criteria {

    public static final int NO_REQUIREMENT = 0;
    public static final int POWER_USAGE_LOW = 1;
    public static final int POWER_USAGE_MEDIUM = 2;
    public static final int POWER_USAGE_HIGH = 3;

    private boolean costAllowed;
    private int preferredPowerConsumption = NO_REQUIREMENT;
    private int horizontalAccuracy = NO_REQUIREMENT;
    private int verticalAccuracy = NO_REQUIREMENT;
    private int preferredResponseTime;
    private boolean speedAndCourseRequired;
    private boolean altitudeRequired;
    private boolean addressInfoRequired;

    public Criteria() {
    }

    public boolean isAllowedToCost() {
        return costAllowed;
    }

    public void setCostAllowed(boolean costAllowed) {
        this.costAllowed = costAllowed;
    }

    public int getPreferredPowerConsumption() {
        return preferredPowerConsumption;
    }

    public void setPreferredPowerConsumption(int level) {
        this.preferredPowerConsumption = level;
    }

    public int getHorizontalAccuracy() {
        return horizontalAccuracy;
    }

    public int getVerticalAccuracy() {
        return verticalAccuracy;
    }

    public void setHorizontalAccuracy(int accuracy) {
        this.horizontalAccuracy = accuracy;
    }

    public void setVerticalAccuracy(int accuracy) {
        this.verticalAccuracy = accuracy;
    }

    public int getPreferredResponseTime() {
        return preferredResponseTime;
    }

    public void setPreferredResponseTime(int time) {
        this.preferredResponseTime = time;
    }

    public boolean isSpeedAndCourseRequired() {
        return speedAndCourseRequired;
    }

    public void setSpeedAndCourseRequired(boolean speedAndCourseRequired) {
        this.speedAndCourseRequired = speedAndCourseRequired;
    }

    public boolean isAltitudeRequired() {
        return altitudeRequired;
    }

    public void setAltitudeRequired(boolean altitudeRequired) {
        this.altitudeRequired = altitudeRequired;
    }

    public boolean isAddressInfoRequired() {
        return addressInfoRequired;
    }

    public void setAddressInfoRequired(boolean addressInfoRequired) {
        this.addressInfoRequired = addressInfoRequired;
    }
}
