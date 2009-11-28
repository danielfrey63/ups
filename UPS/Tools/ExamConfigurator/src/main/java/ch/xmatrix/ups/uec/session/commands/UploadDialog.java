/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.uec.session.commands;

import ch.ethz.ssh2.ServerHostKeyVerifier;
import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.component.WaitOverlay;
import ch.jfactory.ganymed.DummyServerHostKeyVerifier;
import ch.jfactory.ganymed.Ssh2Helper;
import ch.jfactory.jar.JarHelper;
import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.model.ConstraintsPersister;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.model.SessionPersister;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import com.jgoodies.uif.application.Application;
import com.jgoodies.uif.application.ApplicationConfiguration;
import com.thoughtworks.xstream.XStream;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/06 10:16:20 $
 */
public class UploadDialog extends Upload
{
    private static final Logger LOG = Logger.getLogger( UploadDialog.class );

    private static final String PREF_ALIAS = "alias";

    private static final String PREF_KEYSTORE = "keystore";

    private static final String PREF_KEYSTORE_PASS = "keystorePassword";

    private static final String PREF_USER = "user";

    private static final String PREF_SERVER = "server";

    private static final String PREF_SERVER_PASS = "serverPassword";

    private static final String PREF_DIRECTORY = "directory";

    private static final String JAR_CONSTRAINTS = "ch.xmatrix.ups.data.constraints-SNAPSHOT.jar";

    private static final String JAR_TAXA = "ch.xmatrix.ups.data.taxa-SNAPSHOT.jar";

    private static final String JAR_SESSIONS = "ch.xmatrix.ups.data.sessions-SNAPSHOT.jar";

    private static final String FILE_EXAMINFO = "sessions.xml";

    private static final String FILE_CONSTRAINTS = "constraints.xml";

    private static final String FILE_TAXA = "taxa.xml";

    public static final String FILE_DATA = "data";

    private final ApplicationConfiguration configuration = Application.getConfiguration();

    private final Preferences preferences = Preferences.userRoot().node( configuration.getPreferencesRootName() );

    private transient Set<Constraints> constraints;

    private transient Set<TaxonTree> taxa;

    public UploadDialog( final ListModel model )
    {
        listExaminfos.setModel( model );
        loadSettings();
        initComponents();
    }

    private void initComponents()
    {
        getRootPane().setDefaultButton( okButton );
        listExaminfos.setCellRenderer( new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
            {
                final JLabel label = (JLabel) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
                final SessionModel sessionModel = (SessionModel) value;
                final boolean enabled = sessionModel.isFixed();
                label.setEnabled( enabled );
                label.setOpaque( enabled );
                if ( !enabled )
                {
                    label.setText( label.getText() + " (nicht fixiert)" );
                }
                return label;
            }
        } );
        listExaminfos.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                final SessionModel[] models = copyExamInfoModelsIntoArray();
                okButton.setEnabled( models.length > 0 );
            }
        } );
    }

    protected void doCancel()
    {
        setVisible( false );
        dispose();
    }

    protected void doOk()
    {
        try
        {
            final Properties props = collectSettings();
            storeSettings( props );
            final SessionModel[] sessionModels = copyExamInfoModelsIntoArray();
            final Thread thread = new UploadThread( sessionModels, props, constraints, taxa );
            final WaitOverlay waiter = new WaitOverlay( this, Color.white );
            waiter.setTickers(
                    waiter.new FadeInTicker( 0.5f, 500 ),
                    waiter.new WaitForThreadTicker( getRootPane(), thread ),
                    waiter.new FadeOutTicker( 300 ),
                    waiter.new DisposeDialogTicker( this ) );
            waiter.start();
            thread.start();
        }
        catch ( Exception e )
        {
            LOG.error( "error in dialog", e );
            JOptionPane.showMessageDialog( null,
                    "Beim Dialog wurde ein Fehler entdeckt:\n" + e.getMessage(),
                    "Fehler", JOptionPane.ERROR_MESSAGE );
        }
    }

    protected void doSave()
    {
        storeSettings( collectSettings() );
    }

    /**
     * Copies fixed exam info models into a new array.
     *
     * @return the new array
     */
    private SessionModel[] copyExamInfoModelsIntoArray()
    {
        final Object[] objects = listExaminfos.getSelectedValues();
        final List<SessionModel> list = new ArrayList<SessionModel>();
        for ( final Object object : objects )
        {
            final SessionModel sessionModel = (SessionModel) object;
            if ( sessionModel.isFixed() )
            {
                list.add( sessionModel );
            }
        }
        return list.toArray( new SessionModel[0] );
    }

    /**
     * Collects all settings into a property object. The same keys are used as for the preferences.
     *
     * @return properties with settings
     */
    private Properties collectSettings()
    {
        final Properties props = new Properties();
        props.put( PREF_KEYSTORE, fieldKeystore.getText() );
        props.put( PREF_ALIAS, fieldAlias.getText() );
        props.put( PREF_USER, fieldUser.getText() );
        props.put( PREF_KEYSTORE_PASS, new String( fieldKeystorePass.getPassword() ) );
        props.put( PREF_SERVER_PASS, new String( fieldServerPass.getPassword() ) );
        props.put( PREF_SERVER, fieldServer.getText() );
        props.put( PREF_DIRECTORY, fieldRemoteDir.getText() );
        return props;
    }

    /**
     * Stores the {@link #PREF_ALIAS}, {@link #PREF_KEYSTORE}, {@link #PREF_USER}, {@link #PREF_SERVER} and {@link
     * #PREF_DIRECTORY} into the preferences node.
     *
     * @param props the properties to read the values from
     */
    private void storeSettings( final Properties props )
    {
        preferences.put( PREF_KEYSTORE, props.getProperty( PREF_KEYSTORE ) );
        preferences.put( PREF_ALIAS, props.getProperty( PREF_ALIAS ) );
        preferences.put( PREF_USER, props.getProperty( PREF_USER ) );
        preferences.put( PREF_SERVER, props.getProperty( PREF_SERVER ) );
        preferences.put( PREF_DIRECTORY, props.getProperty( PREF_DIRECTORY ) );
    }

    private void loadSettings()
    {
        fieldKeystore.setText( preferences.get( PREF_KEYSTORE, "" ) );
        fieldRemoteDir.setText( preferences.get( PREF_DIRECTORY, "/var/www/html/ups/ust-test" ) );
        fieldServer.setText( preferences.get( PREF_SERVER, "balti.ethz.ch" ) );
        fieldAlias.setText( preferences.get( PREF_ALIAS, "dfrey" ) );
        fieldUser.setText( preferences.get( PREF_USER, "admin" ) );
    }

    private static class UploadThread extends Thread
    {
        /**
         * The session models to upload.
         */
        private final SessionModel[] sessionModels;

        private transient Set<Constraints> constraints;

        private transient Set<TaxonTree> taxa;

        /**
         * Dummy veryfier.
         */
        private final ServerHostKeyVerifier hostKeyVerifier = new DummyServerHostKeyVerifier();

        /**
         * The keystore to sign the temporary JAR with.
         */
        private final String keystore;

        /**
         * The password for the keystore.
         */
        private final String storePass;

        /**
         * The alias for the keystore.
         */
        private final String alias;

        /**
         * The upload server.
         */
        private final String server;

        /**
         * The user for the server.
         */
        private final String user;

        /**
         * The password for the server.
         */
        private final String serverPass;

        /**
         * The directory on the server.
         */
        private final String serverDirectory;

        public UploadThread( final SessionModel[] sessionModels, final Properties props,
                             final Set<Constraints> constraints, final Set<TaxonTree> taxa )
        {
            this.sessionModels = sessionModels;
            this.constraints = constraints;
            this.taxa = taxa;
            keystore = props.getProperty( PREF_KEYSTORE );
            storePass = props.getProperty( PREF_KEYSTORE_PASS );
            alias = props.getProperty( PREF_ALIAS );
            server = props.getProperty( PREF_SERVER );
            user = props.getProperty( PREF_USER );
            serverPass = props.getProperty( PREF_SERVER_PASS );
            serverDirectory = props.getProperty( PREF_DIRECTORY );
        }

        public void run()
        {
            try
            {
                checkConsistency( sessionModels );
                createAndUploadJar( Arrays.asList( sessionModels ), JAR_SESSIONS, FILE_EXAMINFO, SessionPersister.getConverter() );
                createAndUploadJar( constraints, JAR_CONSTRAINTS, FILE_CONSTRAINTS, ConstraintsPersister.getConverter() );
                createAndUploadJar( taxa, JAR_TAXA, FILE_TAXA, TaxonModels.getConverter() );
                JOptionPane.showMessageDialog( null, "Hochladen erfolgreich abgeschlossen." );
            }
            catch ( Throwable e )
            {
                Throwable lastCause = null;
                Throwable cause = e;
                final StringBuffer message = new StringBuffer();
                while ( cause != null && cause != lastCause )
                {
                    message.append( cause.getMessage() ).append( "\n" );
                    lastCause = cause;
                    cause = cause.getCause();
                }
                LOG.error( message.toString().replaceAll( "\n", " " ), e );
                JOptionPane.showMessageDialog( null,
                        "Beim Hochladen der Vorgaben ist ein Problem aufgetaucht:\n" + message,
                        "Fehler", JOptionPane.ERROR_MESSAGE );
            }
        }

        /**
         * Checks the following dependencies:
         *
         * <ul>
         *
         * <li>Each {@link SessionModel exam info model} must have a valid {@link ch.xmatrix.ups.domain.Constraint
         * constraint} uid</li>
         *
         * <li>Each {@link ch.xmatrix.ups.domain.Constraint constraint} must have a valid {@link TaxonTree taxon tree}
         * uid</li>
         *
         * </ul>
         *
         * @param sessionModels the models to investigate
         */
        private void checkConsistency( final SessionModel[] sessionModels )
        {
            constraints = new HashSet<Constraints>();
            taxa = new HashSet<TaxonTree>();
            for ( final SessionModel sessionModel : sessionModels )
            {
                final String constraintsUid = sessionModel.getConstraintsUid();
                final Constraints constraint = (Constraints) AbstractMainModel.findModel( constraintsUid );
                if ( constraint == null )
                {
                    JOptionPane.showMessageDialog( null, "Die Vorgaben mit der id \"" + constraintsUid +
                            "\" fehlen, werden aber von \"" + sessionModel + "\" benötigt." );
                    final String message = "missing constraints (" + constraintsUid +
                            ") for exam info model \"" + sessionModel + "\"";
                    LOG.warn( message );
                    throw new IllegalStateException( message );
                }
                constraints.add( constraint );
                final String taxaUid = constraint.getTaxaUid();
                final TaxonTree taxTree = TaxonModels.find( taxaUid );
                if ( taxTree == null )
                {
                    LOG.warn( "missing taxa with uid " + taxaUid );
                    JOptionPane.showMessageDialog( null, "Der Taxonbaum mit der id \"" + taxaUid +
                            "\" für die Vorgaben mit der id \"" + constraintsUid + "\" fehlt. " +
                            "Das kann zu Inkonsistenzen beim UPS Studenten Tool führen. " +
                            "Möchten Sie fortfahren?" );
                }
                taxa.add( taxTree );
            }
        }

        private void remoteBackup( final String remoteFilePath, final String remoteBackupFilePath, final String server,
                                   final String user, final String pass ) throws Exception
        {
            final File dir = File.createTempFile( "uec", "" );
            dir.delete();
            dir.mkdirs();
            final String remoteFileName = new File( remoteFilePath ).getName();
            final String remoteFile = serverDirectory + "/" + remoteFileName;
            final File localFile = new File( dir, remoteFileName );
            final String remoteBackupFileName = new File( remoteBackupFilePath ).getName();
            final File localBackupFile = new File( dir, remoteBackupFileName );
            try
            {
                Ssh2Helper.download( remoteFile, server, user, pass, dir.getAbsolutePath() );
                localFile.renameTo( localBackupFile );
                Ssh2Helper.upload( localBackupFile.getAbsolutePath(), server, user, pass, serverDirectory, "0644" );
                LOG.info( "renamed remote file " + remoteFileName + " to " + remoteBackupFileName );
            }
            catch ( IOException e )
            {
                // Ignore if file does not exists on server -> backup not necessary
            }
        }

        private void createAndUploadJar( final Collection<?> list, final String jarFileName, final String toPackFileName,
                                         final XStream converter ) throws Exception
        {
            File dir = null;
            try
            {
                final String remoteJarPath = serverDirectory + "/" + jarFileName;
                final String remoteBackupJarPath = remoteJarPath + "." + new SimpleDateFormat( "yyyyMMddHHmmSS" ).format( new Date() );
                remoteBackup( remoteJarPath, remoteBackupJarPath, server, user, serverPass );
                dir = writeTempFile( new SimpleModelList<Object>( new ArrayList<Object>( list ) ), toPackFileName, converter, FILE_DATA );
                final String localJarPath = new File( dir, jarFileName ).getAbsolutePath();
                JarHelper.build( localJarPath, dir, FILE_DATA + "/" + toPackFileName );
                JarHelper.sign( localJarPath, keystore, alias, storePass );
                Ssh2Helper.upload( localJarPath, server, user, serverPass, serverDirectory, "0644" );
                LOG.info( "jar " + localJarPath + " uploaded successfully" );
            }
            finally
            {
                if ( dir != null )
                {
                    try
                    {
                        FileUtils.deleteDirectory( dir );
                    }
                    catch ( IOException e )
                    {
                        LOG.error( "creating or uploading failed for \"" + jarFileName + "\"", e );
                    }
                }
            }
        }

        private File writeTempFile( final SimpleModelList<?> selected, final String filename,
                                    final XStream converter, final String subdirectory ) throws IOException
        {
            final File dir = File.createTempFile( "uec", "" );
            dir.delete();
            final File subdir = new File( dir, subdirectory );
            subdir.mkdirs();
            final File file = new File( subdir, filename );
            final FileWriter writer = new FileWriter( file );
            converter.toXML( selected, writer );
            writer.close();
            return dir;
        }
    }
}
