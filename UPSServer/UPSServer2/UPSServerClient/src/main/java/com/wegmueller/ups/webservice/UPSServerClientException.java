package com.wegmueller.ups.webservice;



/**
 * Exceptions for a call to UPSServer via WebServices
 *
 *
 */
class UPSServerClientException extends Throwable {
    private String reason;
    /**
     * Anmelde daten sind ungültig
     */
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    /**
     * Server Error
     */
    public static final String SERVER_ERROR = "SERVER_ERROR";
    public static final String SERVER_NOT_AVAILABLE = "java.net.ConnectException";
    public static final String UNKNOWN_ERROR = "UNKNOWN";
    public static final String MISSING_DATA = "MISSING_DATA";

    public UPSServerClientException(final String reason, final Throwable e) {
        super(e);
        this.reason = reason;
    }

    public UPSServerClientException(final String reason) {
        super(REASON2MESSAGE(reason));
        this.reason = reason;
    }

    public static String REASON2MESSAGE(final String reason) {
        if (reason.equalsIgnoreCase(INVALID_CREDENTIALS)) return "Benutzername oder Passwort ist falsch";
        if (reason.equalsIgnoreCase(SERVER_ERROR)) return "Auf dem Server ist ein Fehler aufgetreten";
        if (reason.equalsIgnoreCase(SERVER_NOT_AVAILABLE)) return "Der UPS Server ist nicht verfügbar";
        if (reason.equalsIgnoreCase(MISSING_DATA)) return "Es sind nicht genügend Daten vorhanden";
        return "Ein unbekannter Fehler ist aufgetreten";
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }
}
