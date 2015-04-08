# SOAP-Webservices with Apache CXF & SpringBoot using JAX-WS & JAXB - Annotations only, absolutely no XML

As Example SOAP-Service I did some research, but after all the well-known [Weather-Service] seemed to be the best Use-Case, although (or because?) it is used by nearly every tutorial. It is really hard to find free SOAP-Services on the web.

Nevertheless this example is capable for bigger Use-Cases in Realworld-Scenarios with huge WSDLs and lots of imported XSDs, which again import tons of other XSDs - it is derived from experience of a costumer-project with that scenario. If you want, test it with your Service and leave some feedback.

### General choices

In the project I tried to use some relevant technologies for getting SOAP-Services running, like:
* [Spring] with absolutely no XML-Configuration needed (just Annotations)
* [Spring Boot], for easy "not care about Container" (cause it has an embedded [Tomcat]) and simple deployment
* One of the most relevant SOAP-Stack [Apache CXF] 3 as the Webservice-Stack to expose the SOAP-Webservices
* Oracle´s JAX-WS RI (Reference Implementation) with the [JAX-WS-commons project] as "the Standard" to define Webservices in Java
* [JAXB Java-XML-Binding] for working with XML
* JAX-WS Commons for Generating the Class-Files for JAXB, managed by the maven plugin [jaxws-maven-plugin]

### HowTo Use

Run "mvn clean install"-command at command-line, to ensure that all necessary Java-Classes & JAXB-Bindings are generated

Then, you could use Spring Boot with maven to expose your SOAP-Webservices
```sh
mvn spring-boot:run
```
or run the build .jar-File with
```sh
java -jar soap-spring-boot-cxf-0.0.5-SNAPSHOT.jar
```

For testing purposes I would recommend also getting [SOAP-UI], where you can check WSDL/XSD-compliance of the provided services very easily and you "see" your services.


### Todo's
* Testcases with Apache CXF
* Use Spring Boot Admin / actuator 
* Monitor with e.g. ELK
* Spring Boot Starter CXF

[Spring]:https://spring.io
[Spring Boot]:http://projects.spring.io/spring-boot/
[Spring WS]:http://projects.spring.io/spring-ws/
[Apache CXF]:http://cxf.apache.org/
[JAXB Java-XML-Binding]:http://en.wikipedia.org/wiki/Java_Architecture_for_XML_Binding
[SOAP-UI]:http://www.soapui.org/
[jaxws-maven-plugin]:https://jax-ws-commons.java.net/jaxws-maven-plugin/
[JAX-WS-commons project]:https://jax-ws-commons.java.net/spring/
[Weather-Service]:http://wsf.cdyne.com/WeatherWS/Weather.asmx
[Tomcat]:http://tomcat.apache.org/