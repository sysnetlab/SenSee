package sysnetlab.android.sdc.replay;

/**
 * Created by kosta on 12/13/2014.
 */
public interface SensingAssertionEnforcer {
    public void checkAssertion(long timeStamp, int state);
}
