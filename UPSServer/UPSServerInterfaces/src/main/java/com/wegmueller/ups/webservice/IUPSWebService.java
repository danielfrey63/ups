package com.wegmueller.ups.webservice;

import com.wegmueller.ups.UPSServerException;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.IPruefung;
import com.wegmueller.ups.lka.IPruefungsSession;
import java.rmi.RemoteException;
import java.util.Calendar;

/**
 * UPS WebService zur Verwaltung der Pr�fungsanmeldungen
 *
 * @author Thomas Wegm�ller
 * @version 0.1 (2005-09-29)
 */
public interface IUPSWebService {

    /**
     * Kompatibilit�tscheck des Clients mit dem Server
     *
     * @param application ApplikationsID
     * @param version     Version der Applikation
     * @return true, fals die App mit dem Server kommunizieren kann, false falls eine neue Version n�tig ist
     * @throws UPSServerException
     * @throws RemoteException
     */
    Boolean isClientVersionOk(String application, String version) throws UPSServerException, RemoteException;

    /**
     * Gib alle gespeicherten Pruefungssessionen zur�ck Der Server �berpr�ft vorher ob schon irgendwelche Daten gecached
     * sind, wenn ja liefert er die Pruefungssessions-Daten aus der Datenbank
     *
     * @param userName LDAP Username des Dozenten
     * @param password LDAP Passwort des Dozenten
     * @return Array aller PruefungsSessions-Daten
     * @throws UPSServerException
     * @throws RemoteException
     */
    IPruefungsSession[] getPruefungsSessionen(String userName, String password) throws UPSServerException, RemoteException;

    /**
     * Einreichen einer Pr�fungsListe
     *
     * @param seskz    Sessionskenzzeichnung aus IPruefungssession
     * @param lknumber LKNummer aus IAnmeldedaten
     * @param userName LDAP Username des Studenten
     * @param password LDAP Passwort des Dozenten
     * @param bytes    PruefungsListe als byte[]
     * @return pdf als byte-Array
     * @throws UPSServerException
     * @throws RemoteException
     * @see com.wegmueller.ups.lka.IPruefungsSession#getSeskez()
     * @see com.wegmueller.ups.lka.IAnmeldedaten#getLkNummer()
     */
    byte[] submitPruefungsListe(String seskz, String lknumber, String userName, String password, byte[] bytes) throws UPSServerException, RemoteException;

    /**
     * Liefert die eingereichte Prufungsliste eines Studenten oder null, wenn keine Liste eingereicht wurde
     *
     * @param dozent      LDAP Username des Dozenten
     * @param passwrd     LDAP Passwort des Dozenten
     * @param seskz       Sessionskenzzeichnung aus IPruefungssession
     * @param lk          LKNummer aus IAnmeldedaten
     * @param studiNumber Studenten (Leginummer) des Studenten
     * @return Pruefungsliste als byte[] oder null
     * @throws UPSServerException
     * @throws RemoteException
     * @see com.wegmueller.ups.lka.IPruefungsSession#getSeskez()
     * @see com.wegmueller.ups.lka.IAnmeldedaten#getLkNummer()
     * @see com.wegmueller.ups.lka.IAnmeldedaten#getStudentennummer()
     */
    byte[] getPruefungsListe(String dozent, String passwrd, String seskz, String lk, String studiNumber) throws UPSServerException, RemoteException;

    /**
     * Einrichten eines Mapping's zwischen LDAP und OIS-Konto Nach diesem Mapping kann der LDAP-User mit seinen Konto
     * angaben auf OIS-Daten mit den gemappten Konto infos zugreifen Achtung: Dies ist nur f�r Administratoren (dfrey)
     * m�glich
     *
     * @param userName    LDAP-UserName des Administrators
     * @param password    LDAP-Passwort des Administrators
     * @param ldapUser    LDAP-UserName des Dozenten
     * @param oisUser     OIS-Usernamen des Dozenten
     * @param oisPassword OIS-Passwort des Dozenten
     * @return
     * @throws UPSServerException
     * @throws RemoteException
     */
    String addMapping(String userName, String password, String ldapUser, String oisUser, String oisPassword) throws UPSServerException, RemoteException;

    /**
     * @param dozent  LDAP-UserName des Dozenten
     * @param passwrd LDAP-Passwort des Dozenten
     * @return Pruefungssession's-Daten der neu geladenen Daten
     * @throws UPSServerException
     * @throws RemoteException
     * @see com.wegmueller.ups.lka.IPruefungsSession
     */
    IPruefungsSession reloadPruefungsDaten(String dozent, String passwrd) throws UPSServerException, RemoteException;

    /**
     * Daten (Tage) an denen f�r den angegebenen Dozenten und die angegebene Pr�fung Pr�fungen durchgef�hrt werden
     *
     * @param dozent   LDAP-UserName des Dozenten
     * @param passwrd  LDAP-Passwort des Dozenten
     * @param seskz    Sessionskenzzeichnung aus IPruefungssession
     * @param lkNumber Pruefung (LKNummer) aus IAnmeldedaten
     * @return Calendar[] welche die Tage (aufsteigend sortiert) zur�ckgibt
     * @throws UPSServerException
     * @throws RemoteException
     * @see com.wegmueller.ups.lka.IPruefungsSession#getSeskez()
     * @see com.wegmueller.ups.lka.IAnmeldedaten#getLkNummer()
     */
    Calendar[] getPruefungsDaten(String dozent, String passwrd, String seskz, String lkNumber) throws UPSServerException, RemoteException;

    /**
     * Daten (Tage) an denen f�r den angegebenen Dozenten und die angegebenen Pr�fungen Pr�fungen durchgef�hrt werden
     *
     * @param dozent   LDAP-UserName des Dozenten
     * @param passwrd  LDAP-Passwort des Dozenten
     * @param seskz    Sessionskenzzeichnung aus IPruefungssession
     * @param lkNumber Array von Pruefungen (LKNummer) aus IAnmeldedaten
     * @return Calendar[] welche die Tage (aufsteigend sortiert) zur�ckgibt
     * @throws UPSServerException
     * @throws RemoteException
     * @see com.wegmueller.ups.lka.IPruefungsSession#getSeskez()
     * @see com.wegmueller.ups.lka.IAnmeldedaten#getLkNummer()
     */
    Calendar[] getPruefungsDaten(String dozent, String passwrd, String seskz, String[] lkNumber) throws UPSServerException, RemoteException;

    /**
     * Gib die angemeldeten Studenten f�r diese Pr�fungssession und diesen Dozenten
     *
     * @param dozent  LDAP-UserName des Dozenten
     * @param passwrd LDAP-Passwort des Dozenten
     * @param seskz   Sessionskenzzeichnung aus IPruefungssession
     * @return
     * @throws UPSServerException
     * @throws RemoteException
     * @see com.wegmueller.ups.lka.IPruefungsSession#getSeskez()
     * @see com.wegmueller.ups.lka.IAnmeldedaten
     */
    IAnmeldedaten[] getAnmeldungen(String dozent, String passwrd, String seskz) throws UPSServerException, RemoteException;

    /**
     * Gib die angemeldeten Studenten f�r diese Pr�fung an diesem Tag zur�ck
     *
     * @param dozent   LDAP-UserName des Dozenten
     * @param passwrd  LDAP-Passwort des Dozenten
     * @param seskz    Sessionskenzzeichnung aus IPruefungssession
     * @param lkNumber Pruefung (LKNummer) aus IAnmeldedaten
     * @param cal      Tag an dem die Pr�fungen stattfinden
     * @return
     * @throws UPSServerException
     * @throws RemoteException
     * @see com.wegmueller.ups.lka.IPruefungsSession#getSeskez()
     * @see com.wegmueller.ups.lka.IAnmeldedaten
     */
    IAnmeldedaten[] getAnmeldungen(String dozent, String passwrd, String seskz, String lkNumber, Calendar cal) throws UPSServerException, RemoteException;

    /**
     * Gib die angemeldeten Studenten f�r diese Pr�fung an diesem Tag zur�ck
     *
     * @param dozent   LDAP-UserName des Dozenten
     * @param passwrd  LDAP-Passwort des Dozenten
     * @param seskz    Sessionskenzzeichnung aus IPruefungssession
     * @param lkNumber Pruefungen (LKNummer) aus IAnmeldedaten
     * @param cal      Tag an dem die Pr�fungen stattfinden
     * @return
     * @throws UPSServerException
     * @throws RemoteException
     * @see com.wegmueller.ups.lka.IPruefungsSession#getSeskez()
     * @see com.wegmueller.ups.lka.IAnmeldedaten
     */
    IAnmeldedaten[] getAnmeldungen(String dozent, String passwrd, String seskz, String[] lkNumber, Calendar cal) throws UPSServerException, RemoteException;

    /**
     * welche Pr�fungen werden in dieser Pr�fungssession von diesem Dozenten gepr�ft
     *
     * @param dozent  LDAP-UserName des Dozenten
     * @param passwrd LDAP-Passwort des Dozenten
     * @param seskz   Sessionskenzzeichnung aus IPruefungssession
     * @return
     * @throws UPSServerException
     * @throws RemoteException
     * @see com.wegmueller.ups.lka.IPruefungsSession#getSeskez()
     * @see com.wegmueller.ups.lka.IAnmeldedaten
     */
    IPruefung[] getPruefungen(String dozent, String passwrd, String seskz) throws UPSServerException, RemoteException;
}
