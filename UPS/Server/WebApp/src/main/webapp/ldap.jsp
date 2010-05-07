<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.wegmueller.ups.ldap.ILDAPUserRecord" %>
<%@ page import="com.wegmueller.ups.ldap.LDAPAuthenticate" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>LDAP Query</title></head>
<body>
<%
    final PrintWriter writer = response.getWriter();
    final String user = getParameter( request, "user", "dfrey" );
    final String host = getParameter( request, "server", "ldaps01.ethz.ch" );
    try
    {
        final ILDAPUserRecord rec = LDAPAuthenticate.getUserDetails( host, user );
        final Map att = rec.getAttributes();
        writer.write( "<h2>LDAP Response</h2>" );
        writer.write( "<table>" );
        for ( final Iterator iterator = att.keySet().iterator(); iterator.hasNext(); )
        {
            final String key = (String) iterator.next();
            writer.write( "<tr><td>" );
            writer.write( key );
            writer.write( "</td><td>" );
            writer.write( att.get( key ).toString() );
            writer.write( "</td></tr>" );
        }
        writer.write( "</table>" );
    }
    catch ( Throwable e )
    {
        writer.write( e.getMessage() );
    }
%>
</body>
</html>
<%!
    private String getParameter( final HttpServletRequest request, final String key, final String def )
    {
        String parameter = request.getParameter( key );
        if ( parameter == null )
        {
            parameter = def;
        }
        return parameter;
    }
%>
