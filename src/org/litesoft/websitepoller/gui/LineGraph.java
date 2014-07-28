package org.litesoft.websitepoller.gui;

import org.litesoft.commonfoundation.base.*;

import java.awt.*;
import java.util.List;

public class LineGraph {
    public static final int LINE_THICKNESS = 2;
    private static final int SCALE_X = 4;
    private static final int SCALE_Y = 4;
    private static final Stroke STROKE = new BasicStroke( LINE_THICKNESS );

    private final List<Integer> mDataY;
    private final Color mColor;

    public LineGraph( List<Integer> pDataY, Color pColor ) {
        mDataY = Confirm.isNotNull( "DataY", pDataY );
        mColor = Confirm.isNotNull( "Color", pColor );
    }

    public void render( Graphics2D g, Point pLeftTop, Dimension pSize ) {
        if ( mDataY.isEmpty() ) {
            return;
        }
        int zWidth = pSize.width / SCALE_X;
        int zHeight = pSize.height / 4;

        zWidth = Math.min( zWidth, mDataY.size() );

        Point[] zPoints = new PointGenerator( mDataY.subList( mDataY.size() - zWidth, mDataY.size() ) ).generate( zHeight );
        if ( zPoints.length == 0 ) {
            return;
        }
        PointTransformer zTransformer = new PointTransformer( pLeftTop, SCALE_X, SCALE_Y, pSize.height );
        g.setColor( mColor );
        Point zLastPoint = zTransformer.tranform( zPoints[0] );
        if ( zPoints.length == 1 ) {
            g.fillRect( zLastPoint.x, zLastPoint.y, LINE_THICKNESS, LINE_THICKNESS );
            return;
        }
        g.setStroke( STROKE );
        for ( int i = 1; i < zPoints.length; i++ ) {
            Point zPoint = zTransformer.tranform( zPoints[i] );
            g.drawLine( zLastPoint.x, zLastPoint.y, zPoint.x, zPoint.y );
            zLastPoint = zPoint;
        }
    }
}
