package org.litesoft.websitepoller.gui;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;

import java.awt.*;
import java.util.List;

public class PointGenerator {
    private final List<Integer> mDataY;

    public PointGenerator( List<Integer> pDataY ) {
        mDataY = Confirm.isNotNull( "DataY", pDataY );
    }

    public Point[] generate( int pHeight ) {
        Integer zMaxY = Lists.max( mDataY );
        if ( zMaxY == null ) {
            return new Point[0];
        }
        double zFactor = (double) pHeight / zMaxY;
        Point[] zPoints = new Point[mDataY.size()];
        for ( int x = 0; x < zPoints.length; x++ ) {
            zPoints[x] = new Point( x, scaleY( mDataY.get( x ), zFactor ) );
        }
        return zPoints;
    }

    private int scaleY( int y, double pFactor ) {
        double zScaled = pFactor * y;
        return (int) zScaled;
    }
}
