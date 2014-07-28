package org.litesoft.websitepoller.gui;

import org.litesoft.websitepoller.support.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class SimpleWST implements WidgetSourceTargetAccessor,
                                  SourceTarget {
    private final JPanel mTitledBorderedPanel = new JPanel();
    private final JLabel mLabel = new JLabel( "..." );
    private final CanvasPanel mCanvasPanel = new CanvasPanel( mLabel );
    private final InfoSourceFormatter mFormatter;

    public SimpleWST( InfoSource pSource, InfoSourceFormatter pFormatter ) {
        mFormatter = pFormatter;
        TitledBorder zBorder = BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.black ),
                                                                 pSource.getID() );
        zBorder.setTitleJustification( TitledBorder.LEFT );
        mTitledBorderedPanel.setBorder( zBorder );
        mTitledBorderedPanel.setLayout( new BorderLayout() );

        mTitledBorderedPanel.add( mCanvasPanel, BorderLayout.CENTER );

        updateWith( pSource );
    }

    public SimpleWST( InfoSource pSource ) {
        this( pSource, new InfoSourceFormatter() );
    }

    @Override
    public Component getWidget() {
        return mTitledBorderedPanel;
    }

    @Override
    public SourceTarget getTarget() {
        return this;
    }

    @Override
    public void updateWith( InfoSource pSource ) {
        mLabel.setText( mFormatter.format( pSource ) );
        Integer zDuration = pSource.getPollDurationMillisecs();
        if ( zDuration != null ) {
            mCanvasPanel.addDataPoint( zDuration );
        }
    }
}
