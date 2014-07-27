package org.litesoft.websitepoller.support;

public class InfoSource {
    private final int mID;
    private final Integer mPollDurationMillisecs;
    private final String mMessage;

    public InfoSource( int pID, Integer pPollDurationMillisecs, String pMessage ) {
        mID = pID;
        mPollDurationMillisecs = pPollDurationMillisecs;
        mMessage = pMessage;
    }

    public InfoSource( int pID ) {
        this( pID, null, null );
    }

    public int getID() {
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
        return mID;
    }

    @Override
    public boolean equals( Object o ) {
        return (this == o) || ((o instanceof InfoSource) && equals( (InfoSource) o ));
    }

    public boolean equals( InfoSource them ) {
        return (this == them) || ((them != null)
                                  && (this.mID == them.mID));
    }
}
