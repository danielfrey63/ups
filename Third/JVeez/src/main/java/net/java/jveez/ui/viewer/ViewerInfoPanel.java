/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */

package net.java.jveez.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import net.java.jveez.ui.widgets.BlurPanel;
import net.java.jveez.ui.widgets.StarRating;
import net.java.jveez.vfs.ExifPicture;

public class ViewerInfoPanel<T> extends BlurPanel
{
    private T currentPicture;

    private int currentImageWidth;

    private int currentImageHeight;

    private final StarRating starRating = new StarRating();

    private final JLabel nameLabel = new JLabel();

    private final JLabel sizeLabel = new JLabel();

    private final JLabel resolutionLabel = new JLabel();

    private final JLabel dateLabel = new JLabel();

    private final JLabel focalLabel = new JLabel();

    private final JLabel exposureLabel = new JLabel();

    private final JLabel apertureLabel = new JLabel();

    private final JLabel cameraLabel = new JLabel();

    private final JLabel modelLabel = new JLabel();

    private Font captionFont;

    private Font labelFont;

    private JPanel informationPanel;

    private JPanel loadingPanel;

    public ViewerInfoPanel()
    {
        super();

        setupComponents();
        layoutComponents();
    }

    private void setupComponents()
    {
        setBackground( new Color( 1f, 1f, 1f, 0.5f ) );
        setOpaque( false );
        setBorder( BorderFactory.createEtchedBorder( EtchedBorder.RAISED ) );
        setPreferredSize( new Dimension( 200, 10 ) );

        final Font font = nameLabel.getFont();
        labelFont = font.deriveFont( Font.ITALIC );
        captionFont = labelFont.deriveFont( Font.BOLD | Font.ITALIC );
    }

    private void layoutComponents()
    {
        // setup information panel
        informationPanel = new JPanel( new GridBagLayout() );
        informationPanel.setOpaque( false );
        final GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        informationPanel.setBackground( Color.WHITE );

        addInfoLabel( "Star Rating", starRating, informationPanel, c );
        addInfoLabel( "File Name", nameLabel, informationPanel, c );
        addInfoLabel( "File Size", sizeLabel, informationPanel, c );
        addInfoLabel( "Resolution", resolutionLabel, informationPanel, c );
        addInfoLabel( "Date", dateLabel, informationPanel, c );
        addInfoLabel( "Focal Length", focalLabel, informationPanel, c );
        addInfoLabel( "Exposure Time", exposureLabel, informationPanel, c );
        addInfoLabel( "Aperture Time", apertureLabel, informationPanel, c );
        addInfoLabel( "Camera Brand", cameraLabel, informationPanel, c );
        addInfoLabel( "Camera Model", modelLabel, informationPanel, c );

        // setup loading panel
        final JLabel loadingLabel = new JLabel( "Loading ...", JLabel.HORIZONTAL );
        loadingLabel.setBackground( Color.WHITE );
        loadingLabel.setFont( labelFont );
        loadingPanel = new JPanel( new BorderLayout() );
        loadingPanel.setOpaque( false );
        loadingPanel.setBackground( Color.WHITE );
        loadingPanel.add( loadingLabel, BorderLayout.CENTER );

        // setup main panel
        setLayout( new BorderLayout() );
        add( loadingPanel, BorderLayout.CENTER );
        add( Box.createVerticalStrut( 5 ), BorderLayout.NORTH );
        add( Box.createVerticalStrut( 5 ), BorderLayout.SOUTH );
        add( Box.createHorizontalStrut( 5 ), BorderLayout.EAST );
        add( Box.createHorizontalStrut( 5 ), BorderLayout.WEST );
    }

    private void addInfoLabel( final String text, final JComponent label, final Container container, final GridBagConstraints c )
    {
        final JLabel caption = new JLabel( text );
        caption.setBackground( Color.WHITE );
        caption.setFont( captionFont );
        label.setBackground( Color.WHITE );
        label.setFont( labelFont );

        container.add( caption, c );
        container.add( label, c );
        container.add( Box.createVerticalStrut( 5 ), c );
    }

    public void updateContent( final T picture, final BufferedImage image )
    {
        currentPicture = picture;
        currentImageWidth = ( image != null ? image.getWidth() : 0 );
        currentImageHeight = ( image != null ? image.getHeight() : 0 );

        // is this component visible ?
        if ( getParent() != null )
        {
            loadMetaData();
        }
    }

    public void addNotify()
    {
        loadMetaData();
        super.addNotify();
    }

    private void loadMetaData()
    {
        remove( loadingPanel );
        remove( informationPanel );

        if ( currentPicture == null )
        {
            nameLabel.setText( null );
            sizeLabel.setText( null );
            resolutionLabel.setText( null );
            dateLabel.setText( null );
            focalLabel.setText( null );
            exposureLabel.setText( null );
            apertureLabel.setText( null );
            cameraLabel.setText( null );
            modelLabel.setText( null );

            add( loadingPanel, BorderLayout.CENTER );
        }
        else
        {
            if ( currentImageWidth != 0 && currentImageHeight != 0 )
            {
//                nameLabel.setText( currentPicture.getFile().getName() );
//                sizeLabel.setText( currentPicture.getLength() + " bytes" );
                resolutionLabel.setText( String.format( "%d x %d", currentImageWidth, currentImageHeight ) );
            }

            if ( currentPicture instanceof ExifPicture )
            {
                final ExifPicture exifPicture = (ExifPicture) currentPicture;
                final String date = exifPicture.getExifDate();
                final String focal = exifPicture.getExifFocal();
                final String exposure = exifPicture.getExifExposure();
                final String aperture = exifPicture.getExifAperture();
                final String camera = exifPicture.getExifCamera();
                final String model = exifPicture.getExifModel();

                dateLabel.setText( date != null ? date : "n/a" );
                focalLabel.setText( focal != null ? focal : "n/a" );
                exposureLabel.setText( exposure != null ? exposure : "n/a" );
                apertureLabel.setText( aperture != null ? aperture : "n/a" );
                cameraLabel.setText( camera != null ? camera : "n/a" );
                modelLabel.setText( model != null ? model : "n/a" );
            }
            add( informationPanel, BorderLayout.CENTER );
        }
        repaint();
    }
}
