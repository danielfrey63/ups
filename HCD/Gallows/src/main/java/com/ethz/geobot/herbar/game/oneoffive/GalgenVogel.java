/*
 * Herbar CD-ROM version 2
 *
 * GalgenVogel.java
 *
 * Created on 26. April 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.game.util.SoundList;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

/**
 * Animated Bird at the end of the game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class GalgenVogel extends GalgenPolygon {

    private int clipPlayed;
    private SoundList soundList;
    private Image anim[] = new Image[4];
    private int birdX;
    private int birdY;
    private int index;
    private Canvas ios;


    /**
     * Constructor for the GalgenVogel object
     *
     * @param ios instance of Canvas
     */
    public GalgenVogel(Canvas ios) {
        this.ios = ios;
        final MediaTracker tracker = new MediaTracker(this.ios);
        for (int i = 0; i < anim.length; i++) {
            anim[ i ] = ImageLocator.getIcon("bird_" + i + ".gif").getImage();
            tracker.addImage(anim[ i ], i);
        }
        startLoadingSounds();
        init();
    }


    /**
     * sets initial values of birdposition, sound-stati, animation-stati
     */
    public void init() {
        this.birdX = 750;
        this.birdY = 150;
        this.clipPlayed = 0;
        setGrowing(true);
        this.setIsVisible(false);
    }


    /**
     * @see com.ethz.geobot.herbar.game.oneoffive.GalgenPolygon#paint(Graphics)
     */
    public void paint(Graphics g) {
        if (this.isVisible()) {
            if (clipPlayed == 0) {
                soundList.getClip("samba.au").play();
                clipPlayed = 1;
            }
            if (clipPlayed == 2) {
                soundList.getClip("doing.au").play();
                clipPlayed = 3;
            }
            if (clipPlayed == 4) {
                soundList.getClip("samba_2.au").play();
                clipPlayed = 5;
            }
            g.drawImage(anim[ index ], birdX, birdY, this.ios);
        }
    }


    /**
     * @see com.ethz.geobot.herbar.game.oneoffive.GalgenPolygon#update(Graphics)
     */
    public void update(Graphics g) {
        if (isVisible() && isGrowing()) {
            if (birdX >= 216) {
                birdX -= 15;
                index = (index + 1) % 4;
                if (birdX == 225) {
                    clipPlayed = 2;
                }
            }
            else if (birdX < 216 && birdY <= 410) {
                birdX = 215;
                index = 2;
                birdY += 100;
            }
            else {
                setGrowing(false);
                clipPlayed = 4;
            }
        }
        if (isVisible() && !isGrowing()) {
            birdX = 215;
            birdY = 415;
            index = 3;
        }
    }

    /**
     * Sounds prelouding
     */
    void startLoadingSounds() {
        final String codeBase = System.getProperty("xmatrix.sound.path") + "/";
        soundList = new SoundList(codeBase);
        soundList.startLoading("samba.au");
        soundList.startLoading("doing.au");
        soundList.startLoading("samba_2.au");
    }
}
