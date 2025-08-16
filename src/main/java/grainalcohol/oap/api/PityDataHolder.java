package grainalcohol.oap.api;

public interface PityDataHolder {
    int oap$getPityCount(String poolId);
    void oap$incrementPity(String poolId);
    void oap$resetPity(String poolId);
}
