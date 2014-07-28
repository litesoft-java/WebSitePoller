package org.litesoft.websitepoller;

public class MockInfoSourcerFactory extends AbstractInfoSourcerFactory {
    private static final long TIME_PORTION = 1023;

    public static MockInfoSourcerFactory from( int pID, String pConfigLine ) {
        return new MockInfoSourcerFactory( new OurInfoSourcer( parse( pID, pConfigLine ) ) );
    }

    private MockInfoSourcerFactory( AbstractInfoSourcer pAbstractInfoSourcer ) {
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
