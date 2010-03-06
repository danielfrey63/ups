package com.ethz.geobot.herbar.game.util;

import java.applet.AudioClip;
import java.util.Hashtable;
import javax.swing.JApplet;

/**
 * Loads and holds a bunch of audio files whose locations are specified relative to a fixed base URL.
 */
public class SoundList extends Hashtable
{
    JApplet applet;

    String baseURL;

    public SoundList( final String baseURL )
    {
        super( 5 ); //Initialize Hashtable with capacity of 5 entries.
        this.baseURL = baseURL;
    }

    public void startLoading( final String relativeURL )
    {
        new SoundLoader( this, baseURL, relativeURL );
    }

    public AudioClip getClip( final String relativeURL )
    {
        return (AudioClip) get( relativeURL );
    }

    public void putClip( final AudioClip clip, final String relativeURL )
    {
        put( relativeURL, clip );
    }
}
// $Log