FROM solr:6.1
MAINTAINER Wieland Hoffmann
EXPOSE 8983
COPY target/solrwriter-0.0.1-SNAPSHOT-jar-with-dependencies.jar /opt/solr/server/solr/lib
COPY mbsssss /opt/solr/server/solr/mycores
USER root
RUN chown -R solr:solr /opt/solr/server/solr/mycores
USER solr
