package org.litesoft.websitepoller;

public class SimpleInfoSourcerFactory extends AbstractInfoSourcerFactory {
    private static final long TIME_PORTION = 1023;

    public static SimpleInfoSourcerFactory from( int pID, String pConfigLine ) {
        return new SimpleInfoSourcerFactory( new OurInfoSourcer( parse( pID, pConfigLine ) ) );
    }

    private SimpleInfoSourcerFactory( AbstractInfoSourcer pAbstractInfoSourcer ) {
        super( pAbstractInfoSourcer );
    }

    private static class OurInfoSourcer extends AbstractInfoSourcer {

        private OurInfoSourcer( Data pData ) {
            super( pData );
        }

        @Override
        protected String pollForMessage() {
            delay( 25 + mRandom.nextInt( 10 ) );
            return "SimStuff: " + Long.toString( System.currentTimeMillis() & TIME_PORTION );
        }
    }
}
