package org.litesoft.websitepoller.support;

import org.litesoft.commonfoundation.base.*;

public class InfoSource {
    private final String mID;
    private final Integer mPollDurationMillisecs;
    private final String mMessage;

    public InfoSource( String pID, Integer pPollDurationMillisecs, String pMessage ) {
        mID = pID;
        mPollDurationMillisecs = pPollDurationMillisecs;
        mMessage = pMessage;
    }

    public InfoSource( String pID ) {
        this( pID, null, null );
    }

    public String getID() {
        return mID;
    }

    public Integer getPollDurationMillisecs() {
        return mPollDurationMillisecs;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public int hashCode() {
        return mID.hashCode();
    }

    @Override
    public boolean equals( Object o ) {
        return (this == o) || ((o instanceof InfoSource) && equals( (InfoSource) o ));
    }

    public boolean equals( InfoSource them ) {
        return (this == them) || ((them != null)
                                  && Currently.areEqual( this.mID, them.mID));
    }
}
