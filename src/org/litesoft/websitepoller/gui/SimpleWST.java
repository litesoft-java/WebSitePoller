package org.litesoft.websitepoller.gui;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.websitepoller.support.*;

import javax.swing.*;
import java.awt.*;

public class SimpleWST extends JLabel implements WidgetSourceTargetAccessor,
                                                 SourceTarget {
    public static final int MIN_LINES = 9;
    public static final String SOURCE = "Source: ";

    public SimpleWST( InfoSource pSource ) {
        super( SOURCE + "..." );
        updateWith( pSource );
    }

    @Override
    public Component getWidget() {
        return this;
    }

    @Override
    public SourceTarget getTarget() {
        return this;
    }

    @Override
    public void updateWith( InfoSource pSource ) {
        String[] zTargetLines = Strings.toLines( Strings.padRight( "", '\n', MIN_LINES ) );
        zTargetLines[0] = firstLine( pSource );
        String[] zSourceLines = Strings.toLines( ConstrainTo.notNull( pSource.getMessage() ) );
        for ( int i = 0; i < zSourceLines.length; ) {
            String zSourceLine = zSourceLines[i++];
            zTargetLines[i] = zSourceLine;
        }
        setText( Strings.linesToString( zTargetLines ) );
    }

    private String firstLine( InfoSource pSource ) {
        StringBuilder sb = new StringBuilder( SOURCE );

        sb.append( Integers.padIt( 2, pSource.getID() ) );

        String s = "";
        Integer zDuration = pSource.getPollDurationMillisecs();
        if ( zDuration != null ) {
            s = Integers.zeroPadIt( 4, zDuration );
            s = s.substring( 0, s.length() - 3 ) + "." + s.substring( s.length() - 3 );
        }
        return sb.append( Strings.padIt( 11, s ) ).toString();
    }
}
