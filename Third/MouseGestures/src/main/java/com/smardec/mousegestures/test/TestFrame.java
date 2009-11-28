/*
MouseGestures - pure Java library for recognition and processing mouse gestures.
Copyright (C) 2003-2004 Smardec

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.smardec.mousegestures.test;

import com.smardec.mousegestures.MouseGestures;
import com.smardec.mousegestures.MouseGesturesListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Simple test frame.
 */
public class TestFrame extends JFrame
{
    private MouseGestures mouseGestures = new MouseGestures();

    private final JLabel statusLabel = new JLabel( "Mouse gesture: " );

    public static void main( final String[] args )
    {
        final TestFrame frame = new TestFrame();
        frame.setVisible( true );
    }

    public TestFrame()
    {
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setTitle( "Mouse Gestures Test Frame" );
        setIconImage( new ImageIcon( getClass().getClassLoader().getResource( "com/smardec/mousegestures/test/img/logo.gif" ) ).getImage() );
        initSize();
        getContentPane().setLayout( new BorderLayout() );
        initControls();
        initStatusBar();
        initMouseGestures();
    }

    private void initSize()
    {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension size = new Dimension( 640, 480 );
        if ( size.height > screenSize.height )
            size.height = screenSize.height;
        if ( size.width > screenSize.width )
            size.width = screenSize.width;
        setSize( size );
        setLocation( ( screenSize.width - size.width ) / 2, ( screenSize.height - size.height ) / 2 );
    }

    private void initControls()
    {
        final JCheckBox jCheckBoxButton1 = new JCheckBox( "Right button" );
        jCheckBoxButton1.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                mouseGestures.setMouseButton( MouseEvent.BUTTON3_MASK );
            }
        } );
        final JCheckBox jCheckBoxButton2 = new JCheckBox( "Middle button" );
        jCheckBoxButton2.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                mouseGestures.setMouseButton( MouseEvent.BUTTON2_MASK );
            }
        } );
        final JCheckBox jCheckBoxButton3 = new JCheckBox( "Left button" );
        jCheckBoxButton3.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                mouseGestures.setMouseButton( MouseEvent.BUTTON1_MASK );
            }
        } );
        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add( jCheckBoxButton1 );
        buttonGroup.add( jCheckBoxButton2 );
        buttonGroup.add( jCheckBoxButton3 );
        jCheckBoxButton1.setSelected( true );

        final JPanel jPanel = new JPanel( new GridLayout( 4, 1 ) );
        jPanel.setBorder( BorderFactory.createEmptyBorder( 5, 10, 5, 10 ) );
        jPanel.add( new JLabel( "Select mouse button used for gestures handling." ) );
        jPanel.add( jCheckBoxButton1 );
        jPanel.add( jCheckBoxButton2 );
        jPanel.add( jCheckBoxButton3 );

        getContentPane().add( jPanel, BorderLayout.NORTH );
    }

    private void initStatusBar()
    {
        final JPanel jPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jPanel.setBorder( BorderFactory.createLoweredBevelBorder() );
        jPanel.add( statusLabel );
        getContentPane().add( jPanel, BorderLayout.SOUTH );
    }

    private void initMouseGestures()
    {
        mouseGestures = new MouseGestures();
        mouseGestures.addMouseGesturesListener( new MouseGesturesListener()
        {
            public void gestureMovementRecognized( final String currentGesture )
            {
                setGestureString( addCommas( currentGesture ) );
            }

            public void processGesture( final String gesture )
            {
                try
                {
                    Thread.sleep( 200 );
                }
                catch ( InterruptedException e )
                {
                }
                setGestureString( "" );
            }

            private String addCommas( final String gesture )
            {
                final StringBuffer stringBuffer = new StringBuffer();
                for ( int i = 0; i < gesture.length(); i++ )
                {
                    stringBuffer.append( gesture.charAt( i ) );
                    if ( i != gesture.length() - 1 )
                        stringBuffer.append( "," );
                }
                return stringBuffer.toString();
            }
        } );
        mouseGestures.start();
    }

    private void setGestureString( final String gesture )
    {
        statusLabel.setText( "Mouse gesture: " + gesture );
    }
}
