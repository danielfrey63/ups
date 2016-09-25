Instructions to install UPS Student Tool to server

1. Make a release:
   > perl ../../../Common/Build/src/main/bin/release.pl
2. Make a JNLP build:
   > cd target/checkout
   > perl ../../../../../Common/Build/src/main/bin/jnlp.pl
3. Check the JNLP build for remaining SNAPSHOT dependencies. If there are any, fix it.
4. Upload to balti.ethz.ch ust-test directory
5. Copy all ch.xmatrix.ups.data.* files to such without version number.
6. Patch JNLP file to link those data JARs.