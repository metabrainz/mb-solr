# MusicBrainz Solr [![Build Status](https://travis-ci.org/metabrainz/mb-solr.svg?branch=master)](https://travis-ci.org/metabrainz/mb-solr)

This package includes a
[QueryResponseWriter](https://cwiki.apache.org/confluence/display/solr/Response+Writers)
for Apache Solr that will generate
[mmd-schema](https://github.com/metabrainz/mmd-schema) compliant responses
for Solr cores running on an [mbsssss](https://github.com/mineo/mbsssss) schema.

## Licensing

Note - Part of the code at org.musicbrainz.search.analysis is adapted almost entirely from Lucene core libs.
As such those files are licensed under Apache 2.0 license which is compatible with the existing BSD license of MB-Solr.

## Installation

### Installing brainz-mmd2-jaxb

Clone the repository with Git:

    git clone https://github.com/metabrainz/mmd-schema.git

And install the package:

    cd mmd-schema/brainz-mmd2-jaxb
    mvn install

### Installing the query writer

Clone the repository with Git:

    git clone --recursive https://github.com/metabrainz/mb-solr.git


Navigate to **mbsolr-jaxb** and build dependencies:

    mvn assembly:single

This will create a file **target/mbsolr-jaxb-1.0-SNAPSHOT-jar-with-dependencies.jar**.
Copy this file to jetty's lib directory inside your solr installation. On a default 
binary download of solr this is `server/lib`.


Navigate to the **mb-solr** folder in a terminal and build a JAR
file:

    mvn package

This will create a file called
**solrwriter-0.0.1-SNAPSHOT-jar-with-dependencies.jar** in the **target** folder.

Now you need to make this JAR file available to all Solr cores that need it.
The easiest option is to configure a **sharedLib** in your
[solr.xml](https://cwiki.apache.org/confluence/display/solr/Format+of+solr.xml)
and put the JAR file into that.

All that's left to do now is enabling the Query Response Writers in your cores
[solrconfig.xml](https://cwiki.apache.org/confluence/display/solr/Configuring+solrconfig.xml).
To do that, add the following lines as children of the **config** element:

```xml
    <queryResponseWriter name="mbxml" class="org.musicbrainz.search.solrwriter.MBXMLWriter">
        <str name="entitytype">$entitytype</str>
    </queryResponseWriter>
    <queryResponseWriter name="mbjson" class="org.musicbrainz.search.solrwriter.MBJSONWriter">
        <str name="entitytype">$entitytype</str>
    </queryResponseWriter>
```

The solrconfig.xml of the cores defined by
[mbsssss](https://github.com/mineo/mbsssss) already includes this snippet, as
well as the **sharedLib** configuration in the solr.xml file.

**$entitytype** needs to be replaced by the entity type of the documents in the store.
Valid values are:

- annotation
- area
- artist
- cdstub
- editor
- event
- freedb
- instrument
- label
- place
- recording
- release
- release_group
- series
- tag
- work
- url

Now the core needs to be reloaded.
After that, two new values for the
[wt paramter](https://cwiki.apache.org/confluence/display/solr/Response+Writers)
are available:

- **mbxml**, which returns mmd-compliant XML documents
- **mbjson**, which returns JSON document as described by
  [this page](https://beta.musicbrainz.org/doc/Development/JSON_Web_Service)

At the moment, the
[field list](https://cwiki.apache.org/confluence/display/solr/Common+Query+Parameters#CommonQueryParameters-Thefl)
parameter of each query needs to include the **score** field for the code to
work correctly.

A branch of the MusicBrainz server that can query a Solr server with this
QueryResponseWriter is available on
[GitHub](https://github.com/mineo/musicbrainz-server/tree/solr-search).

### Known Vulnerabilities

- [CVE-2021-44228](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2021-44228):
  It can be mitigated by setting the environement variable `LOG4J_FORMAT_MSG_NO_LOOKUPS=true`.

## Docker Installation

Clone the repository with Git:

    git clone --recursive https://github.com/metabrainz/mb-solr.git

Either run it alone on port 8983:

    docker-compose up

Or build a tagged image to run with [MusicBrainz Docker](https://github.com/metabrainz/musicbrainz-docker):

    ./build.sh
