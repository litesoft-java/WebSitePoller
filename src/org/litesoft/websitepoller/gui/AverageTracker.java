package org.litesoft.websitepoller.gui;

public class AverageTracker {
    private long mTotal;
    private int mCount;

    public Integer add( Integer pValue ) {
        if ( pValue != null ) {
            mTotal += pValue;
            mCount++;
        }
        return (mCount == 0) ? null : (int) (mTotal / mCount);
    }
}
