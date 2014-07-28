package org.litesoft.websitepoller;

import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.util.*;

import java.io.*;
import java.net.*;

public class UrlInfoSourcerFactory extends AbstractInfoSourcerFactory {
    public static UrlInfoSourcerFactory from( int pID, String pConfigLine ) {
        return new UrlInfoSourcerFactory( new OurInfoSourcer( parse( pID, pConfigLine ) ) );
    }

    private UrlInfoSourcerFactory( AbstractInfoSourcer pAbstractInfoSourcer ) {
        super( pAbstractInfoSourcer );
    }

    private static class OurInfoSourcer extends AbstractInfoSourcer {

        private OurInfoSourcer( Data pData ) {
            super( pData );
        }

        @Override
        protected String pollForMessage()
                throws IOException {
            InputStream zInputStream = new URL( getURL() ).openStream();
            String[] zLines = IOUtils.loadTextFile( IOUtils.createReader( zInputStream ) );
            return Strings.combineAsLines( zLines );
        }
    }
}
