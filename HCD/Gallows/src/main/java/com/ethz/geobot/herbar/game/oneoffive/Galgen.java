/*
 * Herbar CD-ROM version 2
 *
 * Galgen.java
 *
 * Created on 16. April 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import ch.jfactory.resource.ImageLocator;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Thread class with run-method of the oneoffive-game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class Galgen extends Canvas implements Runnable {

    public static final int TOTAL_STEPS = 5;

    private static final int STEP_ZERO = 0;
    private static final int STEP_BASE = 1;
    private static final int STEP_SENKRECHT = 2;
    private static final int STEP_WAAGRECHT = 3;
    private static final int STEP_SCHLINGE = 4;
    private static final int STEP_BIRD = TOTAL_STEPS;

    private boolean threadRunning = false;
    private Thread motor;
    private ImageIcon bgBild = ImageLocator.getIcon("bg.jpg");
    private int galgenBauStep = 0;
    private Color colorPBL = new Color(110, 102, 87);
    private Color colorPBR = new Color(61, 55, 47);
    private Color colorPBO = new Color(146, 113, 85);
    private Color colorPSL = new Color(127, 36, 0);
    private Color colorPSR = new Color(96, 31, 5);
    private Color colorPSO = new Color(152, 60, 24);
    private Color colorPWL = new Color(152, 60, 24);
    private Color colorPWR = new Color(96, 31, 5);
    private Color bgGalgen = new Color(63, 59, 21);
    private Color bgGalgenHell = new Color(87, 82, 32);
    private Color polygonKontur = new Color(80, 25, 4);
    private Color baseKontur = new Color(53, 49, 11);
    private Image image;
    private Graphics offscreen;
    // Coordinates from the Base. begins always in the lower right corner.
    private double[] pBUx = {250, 201, 167, 208};
    private double[] pBUy = {462, 473, 473, 462};
    private double[] pBOx = {250, 199, 164, 208};
    private double[] pBOy = {444, 457, 457, 446};
    // Coordinates from the vertical bar.
    private double[] pSUx = {234, 215, 180, 196};
    private double[] pSUy = {448, 453, 453, 448};
    private double[] pSOx = {217, 187, 122, 154};
    private double[] pSOy = {110, 124, 124, 110};
    // Coordinates from the horizontal bar.
    private double[] pWUx = {219, 217, 154, 157};
    private double[] pWUy = {152, 110, 110, 152};
    private double[] pWOx = {435, 446, 376, 374};
    private double[] pWOy = {66, 14, 14, 66};
    // Coordinates from the galgen seil.
    private double[] pOx = {376, 380, 380, 376};
    private double[] pOy = {90, 88, 88, 90};
    private double[] pUx = {376, 380, 380, 376};
    private double[] pUy = {173, 175, 173, 175};
    private GalgenPolygon[] spritesPolygon = new GalgenPolygon[5];
    private GalgenPolygon spriteBase = new GalgenPolygon(pBUx, pBUy, pBOx, pBOy, colorPBR, colorPBL, colorPBO, baseKontur, true);
    private GalgenPolygon spriteSenkrecht = new GalgenPolygon(pSUx, pSUy,
            pSOx, pSOy, colorPSR, colorPSL, colorPSO, polygonKontur, true);
    private GalgenPolygon spriteWaagrecht = new GalgenPolygon(pWUx, pWUy,
            pWOx, pWOy, colorPWR, colorPWL, colorPSO, polygonKontur, false);
    private GalgenPolygon spriteGalgen = new GalgenSchlinge(pOx, pOy, pUx,
            pUy, bgGalgen, bgGalgen, bgGalgen, bgGalgenHell, false);
    private GalgenPolygon spriteBird = new GalgenVogel(this);

    /**
     * Constructor for the Galgen object
     */
    public Galgen() {
        spritesPolygon[ 0 ] = spriteGalgen;
        spritesPolygon[ 1 ] = spriteBase;
        spritesPolygon[ 2 ] = spriteWaagrecht;
        spritesPolygon[ 3 ] = spriteSenkrecht;
        spritesPolygon[ 4 ] = spriteBird;
    }

    /**
     * Sets the galgenBauStep attribute of the Galgen object
     *
     * @param count The new galgenBauStep value
     */
    public void setGalgenBauStep(int count) {
        this.galgenBauStep = count;
    }

    /**
     * THE paint method
     *
     * @param g Description of the Parameter
     */
    public void paint(Graphics g) {
        if (offscreen == null) {
            image = createImage(this.getSize().width, this.getSize().height);
            offscreen = image.getGraphics();
        }
        offscreen.drawImage(bgBild.getImage(), 0, 0, this);

        for (int i = 0; i < spritesPolygon.length; i++) {
            spritesPolygon[ i ].paint(offscreen);
        }
        g.drawImage(image, 0, 0, this);
    }

    /**
     * Main processing method for the Galgen object
     */
    public void run() {
        while (threadRunning) {
            try {
                Thread.sleep(80);
            }
            catch (InterruptedException e) {
            }

            if (!threadRunning) {
                return;
            }
            repaint();
        }
    }

    /**
     * initializes the Thread
     */
    public void initThread() {
        motor = new Thread(this);
        motor.start();
        threadRunning = true;
    }

    /**
     * @param g Graphics object
     */
    public void update(Graphics g) {
        if (galgenBauStep == STEP_ZERO) {
            for (int i = 0; i < spritesPolygon.length; i++) {
                spritesPolygon[ i ].setIsVisible(false);
                spritesPolygon[ i ].setGrowing(false);
                spritesPolygon[ i ].init();
            }
        }
        if (galgenBauStep == STEP_BASE) {
            spriteBase.setGrowing(true);
            spriteBase.setIsVisible(true);
        }
        if (galgenBauStep == STEP_SENKRECHT) {
            spriteSenkrecht.setGrowing(true);
            spriteSenkrecht.setIsVisible(true);
            spriteBase.setGrowing(true);
            spriteBase.setIsVisible(true);
        }
        if (galgenBauStep == STEP_WAAGRECHT) {
            spriteWaagrecht.setGrowing(true);
            spriteWaagrecht.setIsVisible(true);
            spriteSenkrecht.setGrowing(true);
            spriteSenkrecht.setIsVisible(true);
            spriteBase.setGrowing(true);
            spriteBase.setIsVisible(true);
        }
        if (galgenBauStep == STEP_SCHLINGE) {
            spriteGalgen.setGrowing(true);
            spriteGalgen.setIsVisible(true);
            spriteWaagrecht.setGrowing(true);
            spriteWaagrecht.setIsVisible(true);
            spriteSenkrecht.setGrowing(true);
            spriteSenkrecht.setIsVisible(true);
            spriteBase.setGrowing(true);
            spriteBase.setIsVisible(true);
        }
        if (galgenBauStep == STEP_BIRD) {
            for (int i = 0; i < spritesPolygon.length - 1; i++) {
                spritesPolygon[ i ].setIsVisible(true);
                spritesPolygon[ i ].setGrowing(true);
            }

            spriteBird.setIsVisible(true);
        }

        for (int i = 0; i < spritesPolygon.length; i++) {
            if (spritesPolygon[ i ].isVisible()) {
                spritesPolygon[ i ].update(g);
            }
        }
        paint(g);
    }

    /**
     * stops the Thread
     */
    public void stopThread() {
        threadRunning = false;
    }
}
