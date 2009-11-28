package com.ethz.geobot.herbar.game.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class SoundLoader extends Thread
{
    SoundList soundList;

    URL completeURL;

    String relativeURL;

    public SoundLoader( final SoundList soundList, final String baseURL, final String relativeURL )
    {
        this.soundList = soundList;
        completeURL = SoundLoader.class.getResource( baseURL + relativeURL );
        this.relativeURL = relativeURL;
        setPriority( MIN_PRIORITY );
        start();
    }

    public void run()
    {
        final AudioClip audioClip = Applet.newAudioClip( completeURL );
        if ( audioClip == null )
        {
            System.err.println( "could not load sound file " + completeURL );
        }
        soundList.putClip( audioClip, relativeURL );
    }
}
