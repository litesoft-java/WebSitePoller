package org.litesoft.websitepoller.gui;

public class MaxTracker {
    private Integer mMax;

    public Integer add( Integer pValue ) {
        if ( pValue != null ) {
            mMax = (mMax == null) ? pValue : Math.max( mMax, pValue );
        }
        return mMax;
    }
}
