package com.wegmueller.ups.webservice;


import com.wegmueller.ups.webservice.stub.UPSWebService;
import com.wegmueller.ups.webservice.stub.UPSWebServiceServiceLocator;
import org.apache.axis.AxisFault;

import javax.xml.rpc.ServiceException;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Example how to call the UPS Server remotly
 */
class UPSServerClientExample {


    public static void main(String[] args) throws Exception {
        String fileName = null;
        String userName = null;
        String password = null;
        if (args.length<2){
            JOptionPane.showMessageDialog(null,"Benutzername und Passwort angeben");
        }

        userName = args[0];
        password = args[1];
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
            fileName = fc.getSelectedFile().getAbsolutePath();
        }

        if (fileName==null) return;


        File file = new File(fileName);
        if (!file.exists()) return;

        FileInputStream fis = new FileInputStream(file);
        byte[] b = new byte[(int) file.length()];
        fis.read(b);

        byte[] pdf = new byte[0];
        try {
            pdf = UPSServerClient.submitPruefungsListe(userName, password, b);

            File outFile = new File("test.pdf");
            FileOutputStream f = new FileOutputStream(outFile);
            f.write(pdf);
            f.close();

            Runtime.getRuntime().exec("cmd /c "+outFile.getAbsolutePath());

        } catch (UPSServerClientException e) {
            JOptionPane.showMessageDialog(null,UPSServerClientException.REASON2MESSAGE(e.getReason()));
            e.printStackTrace();
        }

    }


}