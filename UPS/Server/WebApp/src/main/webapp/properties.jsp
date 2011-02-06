<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.Properties" %>
<html>
<title>System Properties</title>

<h1>System Properties</h1>

<body>
<%
    final PrintWriter writer = response.getWriter();
    try
    {
        writer.write( "<table><tr>\n" );
        final Properties properties = System.getProperties();
        while ( properties.keySet().iterator().hasNext() )
        {
            final Object key = properties.keySet().iterator().next();
            final Object value = properties.get( key );
            writer.write( "<td>" + key + "</td>" + "<td>" + value + "</td>\n" );
        }
        writer.write( "</tr></table>\n" );
    }
    catch ( Throwable e )
    {
        writer.write( e.getMessage() + "\n" );
    }
%>
</body>
</html>
