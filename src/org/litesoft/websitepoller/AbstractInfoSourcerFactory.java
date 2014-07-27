package org.litesoft.websitepoller;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.csv.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.websitepoller.support.*;

import java.util.*;

public abstract class AbstractInfoSourcerFactory implements InfoSourcerFactory {
    private static final CsvSupport PARSER = new CsvSupport();

    @Override
    public Pair create() {
        Pair zPair = new Pair();
        zPair.source = mSource;
        zPair.sourcer = mSourcer;
        return zPair;
    }

    private final InfoSource mSource;
    private final InfoSourcer mSourcer;

    protected AbstractInfoSourcerFactory( AbstractInfoSourcer pAbstractInfoSourcer ) {
        mSource = new InfoSource( pAbstractInfoSourcer.mData.mID );
        mSourcer = pAbstractInfoSourcer;
    }

    protected static class Data {
        private final int mID;
        private final int mPollingFrequency;
        private final String mURL;

        private Data( int pID, int pPollingFrequency, String pURL ) {
            mID = pID;
            mPollingFrequency = Integers.assertAtLeast( "PollingFrequency", pPollingFrequency, 10 );
            mURL = Confirm.significant( "URL", pURL );
        }

        public int getID() {
            return mID;
        }

        public int getPollingFrequency() {
            return mPollingFrequency;
        }

        public String getURL() {
            return mURL;
        }
    }

    protected static Data parse( int pID, String pConfigLine ) {
        if ( null == (pConfigLine = ConstrainTo.significantOrNull( pConfigLine )) ) {
            return null;
        }
        Exception zException = null;
        try {
            String[] zFields = PARSER.decode( pConfigLine );
            if ( zFields.length == 2 ) {
                int zPollingFrequency = Integer.parseInt( Confirm.significant( "PollingFrequency", zFields[0] ) );
                return new Data( pID, zPollingFrequency, zFields[1] );
            }
        }
        catch ( RuntimeException e ) {
            zException = e;
        }
        throw new IllegalArgumentException( "Expected two CSV fields (PollingFrequency & URL) from line (" + pID + "): " + pConfigLine, zException );
    }

    protected static abstract class AbstractInfoSourcer implements InfoSourcer {
        private final Data mData;
        protected final Random mRandom;
        private volatile boolean mRunning;

        protected AbstractInfoSourcer( Data pData ) {
            mData = pData;
            mRandom = new Random( pData.getID() );
        }

        protected String getURL() {
            return mData.getURL();
        }

        abstract protected String pollForMessage();

        @Override
        public void start( final SourceTarget pTarget ) {
            Thread zThread = new Thread( new Runner( pTarget ) );
            zThread.setDaemon( mRunning = true );
            zThread.start();
        }

        @Override
        public void stop() {
            mRunning = false;
        }

        private boolean shouldContinue() {
            return mRunning;
        }

        protected void delay( long pMilliSecs ) {
            waitTill( System.currentTimeMillis() + pMilliSecs );
        }

        protected void waitTill( long pTill ) {
            for ( long zToWait; 0 < (zToWait = pTill - System.currentTimeMillis()); ) {
                try {
                    Thread.sleep( zToWait );
                }
                catch ( InterruptedException e ) {
                    if ( !shouldContinue() ) {
                        return;
                    }
                }
            }
        }

        private class Runner implements Runnable {
            private final SourceTarget mTarget;

            private Runner( SourceTarget pTarget ) {
                mTarget = pTarget;
            }

            private void send( String pMessage, int pDuration ) {
                try {
                    mTarget.updateWith( new InfoSource( mData.getID(), pDuration, pMessage ) );
                }
                catch ( Exception e ) {
                    e.printStackTrace();
                }
            }

            private String getNextMessage() {
                try {
                    return pollForMessage();
                }
                catch ( RuntimeException e ) {
                    return "Error: " + e.getMessage();
                }
            }

            @Override
            public void run() {
                int zPollingFrequency = mData.getPollingFrequency();
                delay( mRandom.nextInt( zPollingFrequency ) );
                do {
                    long zFrom = System.currentTimeMillis();
                    String zMessage = getNextMessage();
                    long zFor = System.currentTimeMillis();
                    send( zMessage, (int) (zFor - zFrom) );
                    long zTill = zFrom + zPollingFrequency;
                    if ( zPollingFrequency > 500 ) {
                        long zDelay = zTill - System.currentTimeMillis();
                        if ( 250 <= zDelay ) {
                            System.out.println( "Fetch: " + getURL() + " in (MilliSecs) " + zDelay );
                        }
                    }
                    waitTill( zTill );
                } while ( shouldContinue() );
            }
        }
    }
}
