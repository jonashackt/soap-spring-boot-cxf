# SOAP-Webservices with Apache CXF & SpringBoot using JAX-WS RI & JAXB - Annotations only, absolutely no XML
[![Build Status](https://travis-ci.org/jonashackt/soap-spring-boot-cxf.svg?branch=master)](https://travis-ci.org/jonashackt/soap-spring-boot-cxf)
[![Coverage Status](https://coveralls.io/repos/jonashackt/soap-spring-boot-cxf/badge.svg)](https://coveralls.io/r/jonashackt/soap-spring-boot-cxf)

As Example SOAP-Service I did some research, but after all the well-known [Weather-Service] seemed to be the best Use-Case, although (or because?) it is used by nearly every tutorial. It is really hard to find free SOAP-Services on the web.

But i had to extend the Weather-Service a lot through out development - e.g. Custom Exceptions, more complex
Input-Requests and a little less methods, so i can show my findings better. The biggest change was to split it into a WSDL (as "just the SOAP-interface") and a bunch of XSDs that import each other. That should represent a more complex domain and although they do not contain that much definitions, i can show many related techniques much better, that appear commonly in real-world scenarios.

So this example-project is capable for bigger Use-Cases in Realworld-Scenarios with huge WSDLs and lots of imported XSDs, which again import tons of other XSDs. If you want, test it with your Service and i appreciate feedback :)

### General choices

In the project I tried to use some relevant technologies for getting SOAP-Services running, like:
* [Spring] with the aim to use absolutely no XML-Configuration (just Annotations)
* [Spring Boot], for easy "not care about Container" (cause it has an embedded [Tomcat]) and simple deployment - like Microservices (without the "micro" in the interface, since we are bound to SOAP...)
* One of the most relevant SOAP-Stack [Apache CXF] 3 as the Webservice-Stack to expose the SOAP-Webservices
* Oracle´s JAX-WS RI (Reference Implementation) with the [JAX-WS-commons project] as "the Standard" to define Webservices in Java
* [JAXB Java-XML-Binding] for working with XML
* JAX-WS Commons for Generating the Class-Files for JAXB, managed by the maven plugin [jaxws-maven-plugin]

I reached my aim to not use any XML-configuration, but it was harder than i thought... If you look on some detail, you´ll see what i mean.

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

### Testing

For testing end-to-end purposes I would recommend also getting [SOAP-UI], where you can check WSDL/XSD-compliance of the provided services very easily and you "see" your services.

But getting to know, how stuff is working, it´s often better to have a look at some tests. There should be a amount of test-cases, that show standard (JAX-WS with CXF) ways to test webservices, but also non-standard approaches to test some
UseCases i came across developing e.g. the custom SoapFaults on incorrect XML-messages.

### Facade-Mode

Sometimes, you are in need of a facade-mode, where your implementation doesn´t call real backends and only returns Dummy-Responses. E.g. when you want to protect your backends when load is getting to high for them (not for your server :), that is based on solid Spring-technology) or even if you want to build up a new environment, where your backends are not available right from the start. And you want this configurable, so you can react fast, when needed.

For this Scenario, Spring´s powerful but yet easy to use [Profile-Mechanism] will serve you well. In combination with using org.springframework.core.io.Resource to load your Dummy-Response-Files instead of Java´s NIO.2 (that could [fuck you up] because of classloader-differences in other environments than your local machine), your done with that task quite fast.

### Done´s
* No XML-configuration, also for undocumented CXF-details :)
* Readable Namespace-Prefixes
* Testcases with Apache CXF
* Custom SoapFault, when non-schmeme-compliant or syntactically incorrect XML is send to the service
* Tests with Raw HTTP-Client for Reaction on syntactically incorrect XML
* Custom Exception in Weather-WSDL/XSDs
* Example of Controller and Mappers, that map to and from an internal Domain-Model - for loose coupling between generated JAXB-Classes and Backends
* Facade-Mode, that only returns Dummy-Responses, if configured
* Logging-Framework for centralization of logging and message-creation, including chance to define individial logging-Ids
* Log SoapMessages to logfile (configurable)

### Todo's

* Use Spring Boot Admin / actuator 
* Monitor with e.g. ELK
* Spring Boot Starter CXF
* Functional plausibility check of request-data with [decision tables]

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
[decision tables]:https://en.wikipedia.org/wiki/Decision_table
[Profile-Mechanism]:http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html
[fuck you up]:https://github.com/jonashackt/springbootreadfilejar
