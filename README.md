# MusicBrainz Solr

This package includes a
[QueryResponseWriter](https://solr.apache.org/guide/solr/9_7/query-guide/response-writers.html)
for Apache Solr that will generate
[mmd-schema](https://github.com/metabrainz/mmd-schema) compliant responses
for Solr cores running on an [mbsssss](https://github.com/metabrainz/mbsssss) schema.

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

Navigate to the **mb-solr** folder in a terminal and build a JAR
file:

    mvn package

This will create a file called
**solrwriter-0.0.1-SNAPSHOT-jar-with-dependencies.jar** in the **target** folder.

Now you need to make this JAR file available to all Solr cores that need it.
The easiest option is to configure a **sharedLib** in your
[solr.xml](https://solr.apache.org/guide/solr/9_7/configuration-guide/configuring-solr-xml.html)
and put the JAR file into that.

All that's left to do now is enabling the Query Response Writers in your cores
[solrconfig.xml](https://solr.apache.org/guide/solr/9_7/configuration-guide/configuring-solrconfig-xml.html).
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
[mbsssss](https://github.com/metabrainz/mbsssss) already includes this snippet, as
well as the **sharedLib** configuration in the solr.xml file.

**$entitytype** needs to be replaced by the entity type of the documents in the store.
Valid values are:

- annotation
- area
- artist
- cdstub
- editor
- event
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
[wt paramter](https://solr.apache.org/guide/solr/9_7/query-guide/common-query-parameters.html#wt-parameter)
are available:

- **mbxml**, which returns mmd-compliant XML documents
- **mbjson**, which returns JSON document as described by the
  [MusicBrainz API documentation page](https://musicbrainz.org/doc/MusicBrainz_API)

At the moment, the
[field list](https://solr.apache.org/guide/solr/9_7/query-guide/common-query-parameters.html#fl-field-list-parameter)
parameter of each query needs to include the **score** field for the code to
work correctly.

A branch of the MusicBrainz server that can query a Solr server with this
QueryResponseWriter is available on
[GitHub](https://github.com/mineo/musicbrainz-server/tree/solr-search).

## Docker Installation

Clone the repository with Git:

    git clone --recursive https://github.com/metabrainz/mb-solr.git

Either run it alone on port 8983:

    docker-compose up

Or build a tagged image to run with [MusicBrainz Docker](https://github.com/metabrainz/musicbrainz-docker):

    ./build.sh
