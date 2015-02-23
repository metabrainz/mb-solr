# MusicBrainz Query Response Writer for Solr

This package includes a
[QueryResponseWriter](https://cwiki.apache.org/confluence/display/solr/Response+Writers)
for Apache Solr that will generate
[mmd-schema](https://github.com/metabrainz/mmd-schema) compliant responses
for Solr cores running on an [mbsssss](https://github.com/mineo/mbsssss) schema.

## Installation

### Installing brainz-mmd2-jaxb

Clone the repository with Git:

    git clone https://github.com/metabrainz/mmd-schema.git

And install the package:

    cd mmd-schema/brainz-mmd2-jaxb
    mvn install

### Installing the query writer

Clone the repository with Git:

    git clone -r https://github.com/mineo/mb-solrquerywriter.git

Navigate to the **mb-solrquerywriter** folder in a terminal and build a JAR
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
- label
- place
- recording
- release
- release_group
- tag
- work

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
