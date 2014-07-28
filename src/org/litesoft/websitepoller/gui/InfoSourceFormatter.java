package org.litesoft.websitepoller.gui;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.websitepoller.support.*;

import java.util.*;

public class InfoSourceFormatter {
    public static final int MIN_LINES = 10;

    private final AverageTracker mAverageDuration = new AverageTracker();
    private final MaxTracker mMaxDuration = new MaxTracker();

    public String format( InfoSource pSource ) {
        List<String> zLines = Lists.newArrayList( Strings.toLines( ConstrainTo.notNull( pSource.getMessage() ) ) );
        while ( zLines.size() < (MIN_LINES - 1) ) {
            zLines.add( "" );
        }
        zLines.add( 1, firstLine( pSource ) );
        return htmlize( zLines );
    }

    private String htmlize( List<String> pLines ) {
        StringBuilder sb = new StringBuilder();
        sb.append( "<html><br>" );
        for ( String zLine : pLines ) {
            sb.append( Strings.replace( zLine, ' ', "&nbsp;" ) ).append( "<br>" );
        }
        return sb.append( "</html>" ).toString();
    }

    private String firstLine( InfoSource pSource ) {
        Integer zDuration = pSource.getPollDurationMillisecs();

        return "Duration (MAC): "
               + format( mMaxDuration.add( zDuration ) )
               + format( mAverageDuration.add( zDuration ) )
               + format( zDuration );
    }

    private String format( Integer pValue ) {
        return Strings.padIt( 10, (pValue == null) ? "-.---" : asMilliSecs( pValue ) );
    }

    private String asMilliSecs( int pValue ) {
        String s = Integers.zeroPadIt( 4, pValue );
        return s.substring( 0, s.length() - 3 ) + "." + s.substring( s.length() - 3 );
    }
}
