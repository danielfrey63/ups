#!/bin/perl

`mvn help:effective-pom >eff.xml`;
`grep "^\\[WARN" eff.xml`;
`grep "^\\[ERRO" eff.xml`;
`sed -i "s/^\\[INFO.*//g" eff.xml`;
`sed -i "s/^\\[WARN.*//g" eff.xml`;
`sed -i "s/^\\[ERRO.*//g" eff.xml`;
`sed -i "s/^Effective.*//g" eff.xml`;
`sed -i "s/<keystore>/<keystore>..\\/..\\//g" eff.xml`;
`sed -i "s/<inputTemplateResourcePath>/<inputTemplateResourcePath>..\\/..\\//g" eff.xml`;
`sed -i "/^\$/d" eff.xml`;
print ("[INFO] Logging to log.txt");
`mvn -f eff.xml clean install org.codehaus.mojo.webstart:webstart-maven-plugin:jnlp >log.txt`;
