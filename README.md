# MusicBrainz Solr

This package includes a
[QueryResponseWriter](https://solr.apache.org/guide/solr/9_7/query-guide/response-writers.html)
for Apache Solr that will generate
[mmd-schema](https://github.com/metabrainz/mmd-schema) compliant responses
for Solr cores running on an [mbsssss](https://github.com/metabrainz/mbsssss) schema.

## Prerequisites

In May 2025, Solr data for indexing the whole MusicBrainz database takes ~75 GB of disk space.
Since indexing takes hours, you might want to load the Zstandard-compressed backup archives.
Those archives take ~60 GB of disk space and their temporarily extracted files take ~75 GB more.
Hence, it is currently recommended to provision at least 250 GB of disk space for Solr.

Recommendations:
* RAM: 4 GB
* CPU: 8 threads, x86-64 architecture
* Disk Space: 250 GB (or 100 GB if you don't load backups.)

Required software:
* Git
* Docker 2
  (or Java SE 17, Maven 3, and Solr 9.7.0)

## Installation

The generally recommended installation method is using [Docker](https://docs.docker.com).
A Docker image is available through the eponymous [Docker Hub repository](https://hub.docker.com/r/metabrainz/mb-solr) for each release of MusicBrainz Solr.
These images can be used through the [MusicBrainz Docker Compose project](https://github.com/metabrainz/musicbrainz-docker) which comes with instructions.

For [development](#development) or other purposes, these images can also be run alone, see the below subsection “[Installation without MusicBrainz Server](#installation-without-musicbrainz-server)”.

To set up a cluster of Solr nodes, it can be preferred to install to the host system directly, see the below subsection “[Installation without Docker](#installation-without-docker)” and the [Apache Solr Reference Guide 9.7](https://solr.apache.org/guide/solr/9_7/).

### Installation without MusicBrainz Server

Clone the repository with Git:

    git clone --recursive https://github.com/metabrainz/mb-solr.git

Run it alone on port 8983 with Docker:

    docker compose up

### Installation without Docker

:warning: This section is outdated as it predates upgrading to SolrCloud 9.

:newspaper: The MusicBrainz Solr cluster powering musicbrainz.org is using [Ansible](https://docs.ansible.com/) for deployment.

#### Installing brainz-mmd2-jaxb

Clone the repository with Git:

    git clone https://github.com/metabrainz/mmd-schema.git

And install the package:

    cd mmd-schema/brainz-mmd2-jaxb
    mvn install

#### Installing the query writer

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

## Development

If you followed “[Installation without MusicBrainz Server](#installation-without-musicbrainz-server)”,
just stop the last command and run it again after having made some changes.

Otherwise, if you need it to be tested with either the indexer or MusicBrainz Server:

1. Make some changes in your local Git clone (of MusicBrainz Solr)
   (The two next steps are optional but recommended to better identify images.)
2. Commit these changes to your local Git branch.
3. Tag this commit with a meaningful version:

    git tag adastrawberry-1969

4. Build a docker image:

    ./build.sh

5. Use this tag to [set `MB_SOLR_VERSION` in MusicBrainz Docker Compose project](https://github.com/metabrainz/musicbrainz-docker?tab=readme-ov-file#local-development-of-musicbrainz-solr).

See also [The Debugger’s Guide to MusicBrainz Solr](HACKING.md).
