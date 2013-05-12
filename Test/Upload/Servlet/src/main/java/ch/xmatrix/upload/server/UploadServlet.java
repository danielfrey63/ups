package ch.xmatrix.upload.server;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet
{
    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
    {
        System.out.println( "File transfer request received, collecting file information and saving to server." );

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload( factory );

        try
        {
            List fileItems = upload.parseRequest( req );
            Iterator iterator = fileItems.iterator();
            if ( iterator.hasNext() )
            {
                FileItem fileItem = (FileItem) iterator.next();
                File file = new File( req.getHeader( "path" ) );
                fileItem.write( file );
                System.out.println( "File [" + fileItem.getName() + "] has been saved to the server." );
            }
        }
        catch ( Exception ex )
        {
            System.err.println( "Unable to retrieve or write file set..." );
            ex.printStackTrace();
        }
    }
}
