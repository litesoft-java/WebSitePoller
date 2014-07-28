package org.litesoft.websitepoller.gui;

import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.websitepoller.support.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Create the GUI and show it.  For thread safety,
 * this method should be invoked from the
 * event-dispatching thread.
 */
public class GUI extends JFrame implements StartableSourceTarget {
    private final WidgetSourceTargetAccessorFactory mFactory;
    private final Map<InfoSource, SourcePair> mPairsBySource = Maps.newLinkedHashMap();

    public GUI( String pName, WidgetSourceTargetAccessorFactory pFactory, List<InfoSource> pSources ) {
        super( pName );
        mFactory = pFactory;
        for ( InfoSource zSource : pSources ) {
            mPairsBySource.put( zSource, new SourcePair( zSource ) );
        }
    }

    @Override
    public void updateWith( InfoSource pSource ) {
        mPairsBySource.get( pSource ).update( pSource );
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                getContentPane().add( createGUI() );

                //Display the window.
//                pack();
                setVisible( true );
            }
        } );
    }

    private Component createGUI() {
        Set<InfoSource> zSources = mPairsBySource.keySet();
        int zCols = colsFor( zSources.size() );
        int zRows = rowsFor( zSources.size(), zCols );
        JPanel zPanel = new JPanel( new GridLayout( zRows, zCols, 3, 3 ) );
        for ( InfoSource zSource : zSources ) {
            WidgetSourceTargetAccessor zAccessor = mFactory.create( zSource );
            zPanel.add( zAccessor.getWidget() );
            mPairsBySource.get( zSource ).set( zAccessor.getTarget() );
        }
        return zPanel;
    }

    private int rowsFor( int pSize, int pCols ) {
        return (pSize + pCols - 1) / pCols;
    }

    private int colsFor( int pSize ) {
        return Math.min( 4, pSize );
    }

    private static class SourcePair {
        private volatile SourceTarget mTarget;
        private volatile InfoSource mSource;

        private SourcePair( InfoSource pSource ) {
            mSource = pSource;
        }

        public void set( SourceTarget pTarget ) {
            notify( mTarget = pTarget, mSource );
        }

        public void update( InfoSource pSource ) {
            notify( mTarget, mSource = pSource );
        }

        public static void notify( final SourceTarget pTarget, final InfoSource pSource ) {
            if ( pTarget != null ) {
                SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        pTarget.updateWith( pSource );
                    }
                } );
            }
        }
    }
}
