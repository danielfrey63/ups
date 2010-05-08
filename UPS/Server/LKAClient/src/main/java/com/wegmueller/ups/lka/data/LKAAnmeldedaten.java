package com.wegmueller.ups.lka.data;

import ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsAnmeldedaten;
import com.wegmueller.ups.lka.IAnmeldedaten;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Created by: Thomas Wegmueller Date: 20.09.2005,  12:45:45 */
public class LKAAnmeldedaten implements IAnmeldedaten
{
    private static final Logger LOG = LoggerFactory.getLogger( LKAAnmeldedaten.class );

    private String lkNummer;

    private int lkForm;

    private String lkFormText;

    private String pruefungsmodeText;

    private String fachrichtung;

    private String studentennummer;

    private String vorname;

    private boolean repetent;

    private String lkEinheitTitel;

    private String lkEinheitTyp;

    private String pruefungsraum;

    private String lkEinheitNummerzusatz;

    private String nachname;

    private String studiengang;

    private String email;

    private String seskez;

    private String lkEinheitTypText;

    private Calendar pruefungsdatum;

    private Calendar pruefungsdatumVon;

    private Calendar pruefungsdatumBis;

    public LKAAnmeldedaten( final WsAnmeldedaten orig )
    {
        setLkNummer( orig.getLkNummer() );
        setLkForm( orig.getLkForm() );
        setLkFormText( orig.getLkFormText() );
        setPruefungsmodeText( orig.getPruefungsmodeText() );
        setFachrichtung( orig.getFachrichtung() );
        setStudentennummer( orig.getStudentennummer() );
        setVorname( orig.getVorname() );
        setRepetent( orig.getRepetent() );
        setLkEinheitTitel( orig.getLkEinheitTitel() );
        setLkEinheitTyp( orig.getLkEinheitTyp() );
        setPruefungsraum( orig.getPruefungsraum() );
        setLkEinheitNummerzusatz( orig.getLkEinheitNummerzusatz() );
        setNachname( orig.getNachname() );
        setStudiengang( orig.getStudiengang() );
        setEmail( orig.getEmail() );
        setSeskez( orig.getSeskez() );
        setLkEinheitTypText( orig.getLkEinheitTypText() );

        setPruefungsdatum( CalendarUtils.parsePruefungsdatum( orig.getPruefungsdatum() ) );

        final String pruefungszeit = orig.getPruefungszeit();
        if ( "".equals( pruefungszeit ) )
        {
            LOG.warn( "Prüfungszeit ungültig für:" +
                    " LkNummer: " + getLkNummer() +
                    " Fachrichtung: " + getFachrichtung() +
                    " Studentennummer: " + getStudentennummer() +
                    " Vorname: " + getVorname() +
                    " Nachname: " + getNachname() +
                    " Studiengang: " + getStudiengang() +
                    " Seskez: " + getSeskez()
            );
        }
        else
        {
            final String vonStr = pruefungszeit.substring( 0, 5 );
            final String bisStr = pruefungszeit.substring( 6, 11 );
            setPruefungsdatumVon( CalendarUtils.parseTime( getPruefungsdatum(), vonStr ) );
            setPruefungsdatumBis( CalendarUtils.parseTime( getPruefungsdatum(), bisStr ) );
        }
    }

    public String getLkNummer()
    {
        return lkNummer;
    }

    public void setLkNummer( final String lkNummer )
    {
        this.lkNummer = lkNummer;
    }

    public int getLkForm()
    {
        return lkForm;
    }

    public void setLkForm( final int lkForm )
    {
        this.lkForm = lkForm;
    }

    public String getLkFormText()
    {
        return lkFormText;
    }

    public void setLkFormText( final String lkFormText )
    {
        this.lkFormText = lkFormText;
    }

    public String getPruefungsmodeText()
    {
        return pruefungsmodeText;
    }

    public void setPruefungsmodeText( final String pruefungsmodeText )
    {
        this.pruefungsmodeText = pruefungsmodeText;
    }

    public String getFachrichtung()
    {
        return fachrichtung;
    }

    public void setFachrichtung( final String fachrichtung )
    {
        this.fachrichtung = fachrichtung;
    }

    public String getStudentennummer()
    {
        return studentennummer;
    }

    public void setStudentennummer( final String studentennummer )
    {
        this.studentennummer = studentennummer;
    }

    public String getVorname()
    {
        return vorname;
    }

    public void setVorname( final String vorname )
    {
        this.vorname = vorname;
    }

    public boolean isRepetent()
    {
        return repetent;
    }

    public void setRepetent( final boolean repetent )
    {
        this.repetent = repetent;
    }

    public String getLkEinheitTitel()
    {
        return lkEinheitTitel;
    }

    public void setLkEinheitTitel( final String lkEinheitTitel )
    {
        this.lkEinheitTitel = lkEinheitTitel;
    }

    public String getLkEinheitTyp()
    {
        return lkEinheitTyp;
    }

    public void setLkEinheitTyp( final String lkEinheitTyp )
    {
        this.lkEinheitTyp = lkEinheitTyp;
    }

    public String getPruefungsraum()
    {
        return pruefungsraum;
    }

    public void setPruefungsraum( final String pruefungsraum )
    {
        this.pruefungsraum = pruefungsraum;
    }

    public String getLkEinheitNummerzusatz()
    {
        return lkEinheitNummerzusatz;
    }

    public void setLkEinheitNummerzusatz( final String lkEinheitNummerzusatz )
    {
        this.lkEinheitNummerzusatz = lkEinheitNummerzusatz;
    }

    public String getNachname()
    {
        return nachname;
    }

    public void setNachname( final String nachname )
    {
        this.nachname = nachname;
    }

    public String getStudiengang()
    {
        return studiengang;
    }

    public void setStudiengang( final String studiengang )
    {
        this.studiengang = studiengang;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( final String email )
    {
        this.email = email;
    }

    public String getSeskez()
    {
        return seskez;
    }

    public void setSeskez( final String seskez )
    {
        this.seskez = seskez;
    }

    public String getLkEinheitTypText()
    {
        return lkEinheitTypText;
    }

    public void setLkEinheitTypText( final String lkEinheitTypText )
    {
        this.lkEinheitTypText = lkEinheitTypText;
    }

    public Calendar getPruefungsdatum()
    {
        return pruefungsdatum;
    }

    public void setPruefungsdatum( final Calendar pruefungsdatum )
    {
        this.pruefungsdatum = pruefungsdatum;
    }

    public Calendar getPruefungsdatumVon()
    {
        return pruefungsdatumVon;
    }

    public void setPruefungsdatumVon( final Calendar pruefungsdatumVon )
    {
        this.pruefungsdatumVon = pruefungsdatumVon;
    }

    public Calendar getPruefungsdatumBis()
    {
        return pruefungsdatumBis;
    }

    public void setPruefungsdatumBis( final Calendar pruefungsdatumBis )
    {
        this.pruefungsdatumBis = pruefungsdatumBis;
    }
}
