FROM solr:6.6-alpine

# Resetting value set in the parent image
USER root

RUN echo "http://dl-cdn.alpinelinux.org/alpine/edge/community" >> /etc/apk/repositories && \
    apk add --update-cache --no-cache \
            bash \
            git \
            maven \
            openjdk8 \
            openssh

# Caching the maven dependencies so that these are built only if
# the dependencies are changed and not the source code.
COPY ./mb-solrquerywriter/pom.xml mb-solrquerywriter/pom.xml
COPY ./mmd-schema/brainz-mmd2-jaxb/pom.xml brainz-mmd2-jaxb/pom.xml
RUN cd brainz-mmd2-jaxb && \
    mvn verify clean --fail-never && \
    cd ../mb-solrquerywriter && \
    mvn verify clean --fail-never && \
    cd ..

COPY ./mmd-schema/brainz-mmd2-jaxb brainz-mmd2-jaxb
COPY ./mb-solrquerywriter mb-solrquerywriter
RUN cd brainz-mmd2-jaxb && \
    mvn install && \
    cd ../mb-solrquerywriter && \
    mvn package -DskipTests && \
    mkdir -p /opt/solr/lib && \
    cp target/solrwriter-0.0.1-SNAPSHOT-jar-with-dependencies.jar /opt/solr/lib

ENV SOLR_HOME /opt/solr/server/solr
COPY ./mbsssss $SOLR_HOME/mycores/mbsssss

# Pointing default Solr config to our shared lib directory
# and fix permissions
RUN sed -i'' 's|</solr>|<str name="sharedLib">/opt/solr/lib</str></solr>|' \
        /opt/solr/server/solr/solr.xml && \
    mkdir $SOLR_HOME/data && \
    chown -R solr:solr /opt/solr

USER $SOLR_USER