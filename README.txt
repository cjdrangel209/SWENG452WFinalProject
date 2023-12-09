This project uses Pi4J. Pi4J is a library that allows a Java program to access and utilize the GPIO board on the Raspberry Pi.
To be able to utilize the Pi4J library, first enusre that your Raspberry has the most updated version of Java.
This can be done by running:

$java -version

from your terminal. Next, because Pi4J is based on Maven, and not every terminal has Maven automatically installed, 
I developed this using JBang so that it could be more of a cross platform project. JBang allows a Java program to be run
without the need of the Java compiler and uses the appropriate libraries that are needed. 
To install JBang, run on your terminal:

$curl -Ls https://sh.jbang.dev | bash -s - app setup

(this will also install Java onto your Raspberry Pi if there was no previous version of Java installed).
Then to run the program, in your terminal, move to the directory where all of the files are stored and run:

$sudo `which jbang` Main.java

This will run the program with all of the proper Pi4J libraries.
