package com.ethz.geobot.herbar.game.util;


import java.applet.AudioClip;
import javax.swing.JApplet;

/**
 * Loads and holds a bunch of audio files whose locations are specified relative to a fixed base URL.
 */
public class SoundList extends java.util.Hashtable {

    JApplet applet;
    String baseURL;

    public SoundList(String baseURL) {
        super(5); //Initialize Hashtable with capacity of 5 entries.
        this.baseURL = baseURL;
    }

    public void startLoading(String relativeURL) {
        new SoundLoader(this, baseURL, relativeURL);
    }

    public AudioClip getClip(String relativeURL) {
        return (AudioClip) get(relativeURL);
    }

    public void putClip(AudioClip clip, String relativeURL) {
        put(relativeURL, clip);
    }
}
// $Log