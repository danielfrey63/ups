package com.wegmueller.ups.storage;

import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.IPruefung;

import java.util.Properties;
import java.util.Calendar;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  18:22:27
 */
public interface IStorage {

    void open() throws StorageException;
    void close();

    /**
     * Speichert eine Pruefungsliste eines Studenten f�r eine
     * @param pruefungsSession      ID der Pruefungssession
     * @param pruefung              ID der Pruefung
     * @param uid                   ID des Users
     * @param studentenNummer       Studentennummer
     * @param input                 PruefungsListe (als byte[])
     * @param userPropertes         Ext. Properties des Users (aus LDAP)
     * @param pdf                   PDF Report der Best�tigung (als byte[])
     * @throws StorageException
     */
    void storePruefungsListe(String pruefungsSession,
                             String pruefung,
                             String uid,
                             String studentenNummer,
                             byte[] input,
                             Properties userPropertes,
                             byte[] pdf) throws StorageException;

    /**
     * Gibt die Pruefungsliste eines Users zur�ck
     * @param studentenNummer
     * @return  Pruefungsliste als byte[]
     * @throws StorageException
     */
    byte[] getPruefungsliste(String pruefungsSession, String pruefung, String studentenNummer) throws StorageException;


    /**
     * Wurden �berhaupt schon Daten f�r diesen User schon gespeichert?
     * @param userName      userName des Dozenten
     * @return
     * @throws StorageException
     */
    boolean isStored(String userName) throws StorageException;
    /**
     * Wurden die Daten f�r diese Pruefungssession f�r diesen User schon gespeichert?
     * @param session       id der Session
     * @param userName      userName des Dozenten
     * @return
     * @throws StorageException
     */
    boolean isStored(String session, String userName) throws StorageException;

    /**
     * Speichere die Information einer Pr�fungssession
     * Die bestehenden Infos werden �berschrieben
     * @param session
     * @return die gespeicherte Pruefungssession
     * @throws StorageException
     */
    IPruefungsSession storePruefungsSession(IPruefungsSession session) throws StorageException;

    /**
     * Speichere die Anmeldungen f�r den Dozenten und dieser Pruefungssession
     * Die allenfalls bestehenden Daten werden gel�scht
     * @param userName
     * @param session
     * @param daten
     * @throws StorageException
     */
    void storeAnmdeldeDaten(String userName, String session, IAnmeldedaten[] daten) throws StorageException;

    /**
     * erm�gliche es einem LDAP-User auf ein OIS-Konto zuzugreifen
     * @param ldap
     * @param ois
     * @param pass
     * @throws StorageException
     */
    void mapOIS2LDAP(String ldap, String ois, String pass) throws StorageException;

    /**
     * Gib die OIS-Anmeldedaten (Array[username,passwort]) f�r den LDAP-User zur�ck
     *
     * @param userName
     * @return
     * @throws StorageException
     */
    String[] getOISAccount(String userName) throws StorageException;

    /**
     * Gibt die Sessionen zur�ck
     * @return
     * @throws StorageException
     */
    IPruefungsSession[] getPruefungssessionen() throws StorageException;

    /**
     * gibt die Pr�fungen zur�ck
     * @param seskez
     * @param userName
     * @return
     * @throws StorageException
     */
    IPruefung[] getPruefungen(String seskez, String userName) throws StorageException;

    /**
     * Gib die Pr�fungs tage zur�ck
     * @param seskez
     * @param lkNummer
     * @param userName
     * @return
     * @throws StorageException
     */
    Calendar[] getPruefungsZeiten(String seskez, String lkNummer, String userName) throws StorageException;

    /**
     * Gib die Pr�fungs tage zur�ck
     * @param seskez
     * @param lkNummer
     * @param userName
     * @return
     * @throws StorageException
     */
    Calendar[] getPruefungsZeiten(String seskez, String[] lkNummer, String userName) throws StorageException;

    /**
     * Angemeldete Studis
     * @param seskez
     * @param lkNummer
     * @param userName
     * @param c
     * @return
     * @throws StorageException
     */
    IAnmeldedaten[] getAnmeldungen(String seskez, String userName, String lkNummer, Calendar c) throws StorageException;


    /**
     * Pruefungssessions record
     * @param s
     * @return
     * @throws StorageException
     */
    IPruefungsSession getPruefungsSessionByID(String s) throws StorageException;

    /**
     * Hat sich der Student angemeldet?
     * @param s
     * @param seskz
     * @param lk
     * @param studiNumber
     * @return
     * @throws StorageException
     */
    boolean isAngemeldet(String s, String seskz, String lk, String studiNumber) throws StorageException;

    /**
     * Titel der Pr�fung
     * @param lk
     * @return
     * @throws StorageException
     */
    String getPrufungsTitel(String lk) throws StorageException;

    /**
     * Anmeldungen
     * @param seskz
     * @param userName
     * @return
     * @throws StorageException
     */
    IAnmeldedaten[] getAnmeldungen(String seskz, String userName) throws StorageException;


    /**
     * Anmeldungen
     * @param seskz
     * @param userName
     * @return
     * @throws StorageException
     */
    IAnmeldedaten[] getAnmeldungen(String seskz, String userName, String[] lkNumber, Calendar cal) throws StorageException;
}
