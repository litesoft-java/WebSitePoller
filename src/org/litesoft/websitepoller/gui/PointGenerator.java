package org.litesoft.websitepoller.gui;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;

import java.awt.*;
import java.util.List;

public class PointGenerator {
    private final List<Integer> mDataY;
    private Integer mMaxY;
    private int mHeight;

    public PointGenerator( List<Integer> pDataY, int pHeight ) {
        mMaxY = Lists.max( mDataY = Confirm.isNotNull( "DataY", pDataY ) );
        mHeight = pHeight;
    }

    public Point[] generate() {
        if ( mMaxY == null ) {
            return new Point[0];
        }
        Point[] zPoints = new Point[mDataY.size()];
        for ( int i = 0; i < zPoints.length; i++ ) {
            zPoints[i] = calcPoint( i, mDataY.get( i ) );
        }
        return zPoints;
    }

    private Point calcPoint( int x, int y ) {
        return new Point( x, scaleY( y ) );
    }

    private int scaleY( int y ) {
        double zFactor = (double) mHeight / mMaxY;
        double zScaled = zFactor * y;
        return (int) zScaled;
    }
}
