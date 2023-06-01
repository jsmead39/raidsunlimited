package raidsunlimited.metrics;

/**
 * Constant values for use with metrics.
 */
public class MetricsConstants {
    public static final String GETRAID_RAIDNOTFOUND_COUNT = "GetRaid.RaidEventNotFoundException.Count";
    public static final String GETPROFILE_PROFILENOTFOUND_COUNT = "GetProfile.UserProfileNotFoundException.Count";
    public static final String UPDATEPROFILE_INVALIDATTRIBUTEVALUE_COUNT =
            "UpdateProfile.InvalidAttributeValueException.Count";
    public static final String UPDATEPROFILE_INVALIDATTRIBUTECHANGE_COUNT =
            "UpdateProfile.InvalidAttributeChangeException.Count";
    public static final String SERVICE = "Service";
    public static final String SERVICE_NAME = "raidsunlimited";
    public static final String NAMESPACE_NAME = "U3/raidsunlimited";
}
