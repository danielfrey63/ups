Howto build WebStart?

    mvn install org.codehaus.mojo.webstart:webstart-maven-plugin:jnlp

Howto import WebStart application from local file system to WebStart cache?

    javaws -import Application\target\jnlp\hcd.jnlp -codebase file:///D:/Daten/UPS2/HCD/Application/target/jnlp

Howto debug WebStart application with java 6?

    javaws -verbose -offline -J-Xdebug -J-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 Webstart\target\jnlp\hcd.jnlp
    See hint at http://danis-java-posts.blogspot.com/

Command line debugging?

    javaws -offline -J-agentlib:jdwp=transport=dt_shmem,server=y,suspend=y,address=jdbconn target\jnlp\hcd.jnlp
    jdb -attach jdbconn