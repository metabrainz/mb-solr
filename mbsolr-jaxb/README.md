# MusicBrainz Solr search JAXB runtime dependencies

November 2022

This project contains a shim to include jaxb-impl class files in a location where Solr's
class loader can find them.

In Java 8, the javax.bind API was deprecated, and removed from Java in version 11.
In order to use these packages in Java 11+, you need to include the dependencies yourself.

https://stackoverflow.com/questions/52502189/java-11-package-javax-xml-bind-does-not-exist

```xml
<dependency>
  <groupId>javax.xml.bind</groupId>
  <artifactId>jaxb-api</artifactId>
  <version>2.3.0</version>
</dependency>
<dependency>
  <groupId>com.sun.xml.bind</groupId>
  <artifactId>jaxb-core</artifactId>
  <version>2.3.0</version>
</dependency>
<dependency>
  <groupId>com.sun.xml.bind</groupId>
  <artifactId>jaxb-impl</artifactId>
  <version>2.3.0</version>
</dependency>
```

The jaxb project continues to be maintained as part of the Jakarta EE project, but as of Jakarta EE version 9
the java package path changed from `javax.xml.bind` to `jakarta.xml.bind`.
This is a problem for mb-solr, because the `maven-jaxb2-plugin` plugin used in the brainz-mmd2-jaxb project
(version 0.14.0) hasn't been updated to this new package path and still generates source files with the 
old `javax.xml.bind` package.

Therefore at this stage we must still keep the old javax.xml.bind dependencies.

## Solr runtime dependencies and classloader

Adding the `com.sun.xml.bind.jaxb-impl` dependency to `mb-solr` fails at solr startup with an error:

```
java.lang.RuntimeException: javax.xml.bind.JAXBException: Implementation of JAXB-API has not been found on module path or classpath.
- with linked exception:
[java.lang.ClassNotFoundException: com.sun.xml.bind.v2.ContextFactory]
```

This is despite the fact that the generated `solrwriter-0.0.1-SNAPSHOT-jar-with-dependencies.jar` file contains
`ContextFactory.class` in the correct location.

The root of this problem is due to https://github.com/jakartaee/jaxb-api/issues/99

> Solr plugins use a custom classloader (SolrResourceLoader). This classloader is not the same the thread’s context classloader, 
> which is Jetty’s WebAppClassLoader. I should note, that Solr’s plugin loader does set Jetty’s classloader as its parent.

> Because ContextFinder#newInstance looks up the default factory class using the thread’s classloader, it’s unable to locate the
> implementation JAR, if it’s installed in the Solr plugin directory. This issue can be worked around by installing the JAXB 
> implementation JARs in Jetty’s lib.

Therefore, we generate a jar with `com.sun.xml.bind.jaxb-impl` and all of its dependencies that can be installed into
jetty's lib directory, separate to the `solrwriter` package which is added to solr's lib directory.

## Installation

1. Run

    mvn assembly:single

2. Copy `mbsolr-jaxb/target/mbsolr-jaxb-1.0-SNAPSHOT-jar-with-dependencies.jar` to `solr/server/lib`
