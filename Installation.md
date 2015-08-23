# Installation instructions #

#  #

**1.** [Download gtedtior executable](http://code.google.com/p/gteditor/downloads/list)

**2.** Unpack the archive with a zip tool (e.g winzip)

**3.** Open the extracted directory and go to the bin folder. The bin folder contains jar files(Java Archive files) that is required for the application to run.

**4.** To start the application a Java VM that support Java 1.6 is needed. It can be downloaded from e.g http://www.java.com (Sun Microsystems Java page).

**5.** When the Java VM is installed the application is started in different ways depending on the system's configuration. Many systems is configured so it is just to double click on the file called gteditor1.0.jar.

_Windows:_

If the application doesn't start when double clicking on the gteditor1.0.jar file then try  to right click on it and select 'open with...' or something similar. Then find the java or javaw executable and add the argument -jar.

_Other systems:_

Use a terminal and change it's current directory to the bin directory. Then execute the following command:
```
java -jar gteditor1.0.jar
```