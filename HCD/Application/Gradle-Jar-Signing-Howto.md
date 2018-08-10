### How to build with Gradle and eToken
#### Prerequisites
* JDK 7 doesn't support 64bit PKCS11. The support for tokens is not fixed there, see [this issue](https://bugs.openjdk.java.net/browse/JDK-7105065).
* JDK 8 fails when doing `gradle build`.
* Use JDK 9. Make sure to set `JAVA_HOME` to the JDKs path and add its `bin` directory to the `path` environment variable.

#### Setup eToken
Make sure to either reference the eToken configuration in a file or alter the Java installation

In case of a file it must contain something like this:
```
name=eToken
library=C:\Windows\System32\eTPKCS11.dll
slot=0
```

In case of a JDK patch open the JREs file `conf/security/java.security` and add the following line (replace `13` with a subsequent number)
  ```
  security.provider.13=SunPKCS11
  ```

Test it by running 
```
keytool -list -keystore NONE -storetype PKCS11 -providerclass sun.security.pkcs11.SunPKCS11 -providerArg ./eToken.cfg
```
either with or without `providerArg` dependent on your SunPKCS11 setup.
   
####Testing
To test whether the jar can be signed properly
```
jarsigner -tsa http://tsa.quovadisglobal.com/TSS/HttpTspServer -keystore NONE -storetype PKCS11 -providerClass sun.security.pkcs11.SunPKCS11 -providerArg ./eToken.cfg ch.xmatrix.le.application-1.0.jar e9449309-6396-2950-b2dc-e7e7c987ccd2
```

In case you don't want to sign the jars and just build the distribution
```
gradle build -x signJars
```

####Listing all Root certificates in JDK

```
keytool -keystore '$env:JAVA_HOME\lib\security\cacerts' -storepass changeit -list
```