/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.ust.main;

import ch.jfactory.component.Dialogs;
import ch.jfactory.convert.Converter;
import ch.jfactory.file.ExtentionFileFilter;
import ch.jfactory.file.SaveChooser;
import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.domain.Credentials;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.ust.main.commands.Commands;
import ch.xmatrix.ups.view.CredentialsDialog;
import com.wegmueller.ups.UPSServerException;
import com.wegmueller.ups.ldap.LDAPAuthException;
import com.wegmueller.ups.webservice.UPSServerClient2;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.JFrame;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;

/**
 * Dialog that asks for credentials and submits the plantlist to the UPS Server. As a result of a successful submission
 * a pdf is given back which may by saved.
 *
 * @author Daniel Frey
 * @version $Revision: 1.9 $ $Date: 2008/01/06 10:16:20 $
 */
public class PlantListSubmitDialog extends CredentialsDialog
{
    private static final Logger LOG = Logger.getLogger( CredentialsDialog.class );

    private final UserModel userModel;

    private final String lknr;

    public PlantListSubmitDialog( final JFrame parent, final UserModel userModel, final String lknr )
    {
        super( parent );
        this.userModel = userModel;
        this.lknr = lknr;
    }

    protected void doApply() throws ComponentDialogException
    {
        final Converter<List<String>> writer = Commands.getConverter();
        final String plantlist = writer.from( userModel.getTaxa() );
        try
        {
            final Credentials credentials = (Credentials) model.getBean();
            final String username = credentials.getUsername();
            final String password = credentials.getPassword();
            final byte[] list = plantlist.getBytes();
            final UPSServerClient2 client = new UPSServerClient2();
            final SessionModel session = (SessionModel) MainModel.findModel( userModel.getExamInfoUid() );
            final String seskz = session.getSeskz();
            final byte[] pdf = client.submitPruefungsListe( seskz, lknr, username, password, list );
            new PdfSaveChooser( pdf ).open();
        }
        catch ( MalformedURLException e )
        {
            final String message = MessageFormat.format( Strings.getString( "error.net.connection" ), e.getLocalizedMessage() );
            LOG.warn( message, e );
            Dialogs.showErrorMessage( PlantListSubmitDialog.this.getRootPane(), "Fehler", message );
        }
        catch ( RemoteException e )
        {
            final String message = MessageFormat.format( Strings.getString( "error.net.connection" ), e.getLocalizedMessage() );
            LOG.warn( message, e );
            Dialogs.showErrorMessage( PlantListSubmitDialog.this.getRootPane(), "Fehler", message );
        }
        catch ( ServiceException e )
        {
            final String message = MessageFormat.format( Strings.getString( "error.net.service" ), e.getLocalizedMessage() );
            LOG.warn( message, e );
            Dialogs.showErrorMessage( PlantListSubmitDialog.this.getRootPane(), "Fehler", message );
        }
        catch ( UPSServerException e )
        {
            final Throwable cause = e.getCause();
            final String message;
            if ( cause != null )
            {
                final Throwable inner = cause.getCause();
                if ( inner instanceof LDAPAuthException )
                {
                    message = MessageFormat.format( Strings.getString( "error.net.credentials" ), inner.getLocalizedMessage() );
                }
                else
                {
                    message = MessageFormat.format( Strings.getString( "error.net.service" ), e.getLocalizedMessage() );
                }
            }
            else
            {
                message = MessageFormat.format( Strings.getString( "error.net.service" ), e.getLocalizedMessage() );
            }
            LOG.warn( message, e );
            Dialogs.showErrorMessage( PlantListSubmitDialog.this.getRootPane(), "Fehler", message );
        }
        catch ( Throwable e )
        {
            final String message = MessageFormat.format( Strings.getString( "error.net.unknown" ), e.getLocalizedMessage() );
            LOG.warn( message, e );
            Dialogs.showErrorMessage( PlantListSubmitDialog.this.getRootPane(), "Fehler", message );
        }
    }

    private static class PdfSaveChooser extends SaveChooser
    {
        private final byte[] pdf;

        public PdfSaveChooser( final byte[] pdf )
        {
            super( new ExtentionFileFilter( "PDF-Datei", new String[]{".pdf"}, true ), "pdfconfirmation", System.getProperty( "user.dir" ) );
            this.pdf = pdf;
        }

        protected void save( final File file )
        {
            if ( file != null )
            {
                final FileOutputStream out;
                try
                {
                    out = new FileOutputStream( file );
                    out.write( pdf );
                    out.close();
                }
                catch ( IOException x )
                {
                    LOG.error( "problem during write of confirmation PDF.", x );
                }
            }
        }
    }
}
