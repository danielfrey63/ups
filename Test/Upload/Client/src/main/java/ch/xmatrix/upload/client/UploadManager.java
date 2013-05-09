package ch.xmatrix.upload.client;

import java.io.File;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class UploadManager
{
    public static void main( String[] args )
    {
        if ( args.length == 2 )
        {
            final File file = new File( args[0] );
            if ( file.exists() )
            {
                final HttpClient http = new DefaultHttpClient();
                HttpPost post = new HttpPost( args[1] );
                post.addHeader( "path", file.getAbsolutePath() );
                MultipartEntity multipart = new MultipartEntity();
                ContentBody fileContent = new FileBody( file ); //For tar.gz: "application/x-gzip"
                multipart.addPart( "package", fileContent );
                post.setEntity( multipart );
            }
            else
            {
                usage();
            }
        }
    }

    private static void usage()
    {
        System.out.println( "Usage: file url" );
    }
}
