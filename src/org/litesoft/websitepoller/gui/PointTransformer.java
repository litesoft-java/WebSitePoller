package org.litesoft.websitepoller.gui;

import java.awt.*;

public class PointTransformer {
    private final int mOffsetX, mScaleX;
    private final int mOffsetY, mScaleY, mHeight;

    public PointTransformer( Point pLeftTop, int pScaleX, int pScaleY, int pHeight ) {
        mOffsetX = pLeftTop.x;
        mOffsetY = pLeftTop.y;
        mScaleX = pScaleX;
        mScaleY = pScaleY;
        mHeight = pHeight;
    }

    public Point tranform( Point pPoint ) {
        return new Point( adjust( pPoint.x, mScaleX, mOffsetX ),
                          mHeight - adjust( pPoint.y, mScaleY, mOffsetY ) );
    }

    private int adjust( int pPoint, int pScale, int pOffset ) {
        return (pPoint * pScale) + pOffset;
    }
}
