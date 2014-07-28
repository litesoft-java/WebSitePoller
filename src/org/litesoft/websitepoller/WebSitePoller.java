package org.litesoft.websitepoller;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.file.*;
import org.litesoft.websitepoller.gui.*;
import org.litesoft.websitepoller.support.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class WebSitePoller {
    public static final String VERSION = "0.9";

    private final StartableSourceTarget mTarget;
    private final List<InfoSourcer> mSourcers = Lists.newArrayList();

    public WebSitePoller( SourceTargetFactory pTargetFactory, List<InfoSourcerFactory> pSourcerFactories ) {
        List<InfoSource> pSources = Lists.newArrayList();
        for ( InfoSourcerFactory zFactory : pSourcerFactories ) {
            if ( zFactory != null ) {
                InfoSourcerFactory.Pair zPair = zFactory.create();
                mSourcers.add( zPair.sourcer );
                pSources.add( zPair.source );
            }
        }
        mTarget = pTargetFactory.create( getClass().getSimpleName(), pSources );
    }

    public void start() {
        mTarget.start();
        for ( InfoSourcer zSourcer : mSourcers ) {
            zSourcer.start( mTarget );
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void stop() {
        for ( InfoSourcer zSourcer : mSourcers ) {
            zSourcer.stop();
        }
    }

    public static void main( String[] args ) {
        System.out.println( "WebSitePoller vs " + VERSION );
        if ( (args = ConstrainTo.notNull( args )).length != 1 ) {
            System.err.println( "Must provide a Configuration file to Load" );
            System.exit( 1 );
        }
        List<InfoSourcerFactory> zFactories = Lists.newArrayList();
        String[] zLines = FileUtils.loadTextFile( new File( args[0] ) );
        for ( int i = 0; i < zLines.length; i++ ) {
            zFactories.add( UrlInfoSourcerFactory.from( i + 1, zLines[i] ) );
        }
        new WebSitePoller( new FrameFactory( new WidgetFactory() ), zFactories ).start();
    }

    private static class FrameFactory implements SourceTargetFactory {
        private final WidgetSourceTargetAccessorFactory mFactory;

        private FrameFactory( WidgetSourceTargetAccessorFactory pFactory ) {
            mFactory = pFactory;
        }

        @Override
        public StartableSourceTarget create( String pName, List<InfoSource> pSources ) {
            //Make sure we have nice window decorations.
            JFrame.setDefaultLookAndFeelDecorated( true );

            GUI zFrame = new GUI( pName, mFactory, pSources );
            zFrame.setSize( 1010, 600 );
            zFrame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
            return zFrame;
        }
    }

    private static class WidgetFactory implements WidgetSourceTargetAccessorFactory {
        @Override
        public WidgetSourceTargetAccessor create( InfoSource pInitialSource ) {
            return new SimpleWST( pInitialSource );
        }
    }
}
