package ch.xmatrix.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Runs a script against HSQLDB and prints the result to standard out.
 *
 * @author Daniel Frey 31.10.2010 13:55:05
 */
public class ScriptCommandLine
{
    private Connection connection;

    public static void main( final String[] arguments )
    {
        new ScriptCommandLine().execute( arguments );
    }

    public void execute( final String[] arguments )
    {
        final Properties properties = new Properties();
        for ( int i = 0; i < arguments.length; i++ )
        {
            final String p = arguments[i];

            if ( p.charAt( 0 ) == '-' )
            {
                properties.put( p.substring( 1 ), arguments[i + 1] );
                i++;
            }
        }

        final String url = properties.getProperty( "url", "jdbc:hsqldb:" );
        final String database = properties.getProperty( "database", "test" );
        final String user = properties.getProperty( "user", "sa" );
        final String password = properties.getProperty( "password", "" );
        final String script = properties.getProperty( "script", "st.sql" );

        try
        {
            Class.forName( "org.hsqldb.jdbcDriver" ).newInstance();
            connection = DriverManager.getConnection( url + database, user, password );
        }
        catch ( Exception e )
        {
            System.err.println( e.getMessage() );
            e.printStackTrace();
        }

        try
        {
            final Statement statement = connection.createStatement();
            final String sql = fileToString( new File( script ) );
            if ( sql != null && sql.length() > 1 )
            {
                statement.execute( sql );
                final ResultSet results = statement.getResultSet();
                final int updateCount = statement.getUpdateCount();

                if ( updateCount == -1 )
                {
                    System.out.println( toDebugString( results ) );
                }
                else
                {
                    System.out.println( "update count " + updateCount );
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                connection.close();
            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }
        }
    }

    private String fileToString( final File file )
    {
        try
        {
            final byte[] buffer = new byte[(int) file.length()];
            final FileInputStream inputStream = new FileInputStream( file );
            final int read = inputStream.read( buffer );
            if ( read == 0 )
            {
                System.err.println( "file is empty" );
            }
            inputStream.close();
            return new String( buffer );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return "";
        }
    }

    private String toDebugString( final ResultSet r )
    {
        try
        {
            if ( r == null )
            {
                return "No Result";
            }

            final ResultSetMetaData m = r.getMetaData();
            final int col = m.getColumnCount();
            StringBuffer stringBuffer = new StringBuffer();

            for ( int i = 1; i <= col; i++ )
            {
                stringBuffer = stringBuffer.append( m.getColumnLabel( i ) ).append( "\t" );
            }

            stringBuffer = stringBuffer.append( "\n" );

            while ( r.next() )
            {
                for ( int i = 1; i <= col; i++ )
                {
                    stringBuffer = stringBuffer.append( r.getString( i ) ).append( "\t" );

                    if ( r.wasNull() )
                    {
                        stringBuffer = stringBuffer.append( "(null)\t" );
                    }
                }

                stringBuffer = stringBuffer.append( "\n" );
            }

            return stringBuffer.toString();
        }
        catch ( SQLException e )
        {
            return null;
        }
    }

}
