ARG MAVEN_TAG=3.9.6-eclipse-temurin-11
ARG SOLR_NAME=solr
ARG SOLR_TAG=7.7.2-slim

FROM maven:${MAVEN_TAG} AS builder

# Caching the maven dependencies so that these are built only if
# the dependencies are changed and not the source code.
COPY ./mb-solr/pom.xml mb-solr/pom.xml
COPY ./mmd-schema/brainz-mmd2-jaxb/pom.xml brainz-mmd2-jaxb/pom.xml
RUN cd brainz-mmd2-jaxb && \
    mvn verify clean --fail-never && \
    echo BUILD SUCCESS is expected above && \
    cd ../mb-solr && \
    mvn verify clean --fail-never && \
    echo BUILD FAILURE is expected above because brainz-mmd-jaxb is not installed yet

COPY ./mmd-schema/brainz-mmd2-jaxb brainz-mmd2-jaxb
COPY ./mb-solr mb-solr
RUN cd brainz-mmd2-jaxb && \
    mvn install && \
    cd ../mb-solr && \
    mvn package -DskipTests

FROM ${SOLR_NAME}:${SOLR_TAG}

ARG MAVEN_TAG
ARG SOLR_NAME
ARG SOLR_TAG

ARG MB_SOLR_VERSION
ARG BUILD_DATE
ARG VCS_REF

LABEL org.label-schema.build-date="${BUILD_DATE}" \
      org.label-schema.schema-version="1.0.0-rc1" \
      org.label-schema.vcs-ref="${VCS_REF}" \
      org.label-schema.vcs-url="https://github.com/metabrainz/mb-solr.git" \
      org.label-schema.vendor="MetaBrainz Foundation" \
      org.metabrainz.based-on-image="${SOLR_NAME}:${SOLR_TAG}" \
      org.metabrainz.builder-image="maven:${MAVEN_TAG}" \
      org.metabrainz.mb-solr.version="${MB_SOLR_VERSION}"

# Resetting value set in the parent image
USER root

RUN apt-get update && \
    apt-get install --no-install-recommends \
        # Needed to decompress search index dumps
        zstd \
        && \
    rm -rf /var/lib/apt/lists/*

COPY --from=builder \
     mb-solr/target/mb-solr-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
     /opt/solr/lib/

ENV SOLR_HOME /opt/solr/server/solr
COPY ./mbsssss $SOLR_HOME/mycores/mbsssss

# Pointing default Solr config to our shared lib directory
# and fix permissions
RUN sed -i'' 's|</solr>|<str name="sharedLib">/opt/solr/lib</str></solr>|' \
        /opt/solr/server/solr/solr.xml && \
    mkdir $SOLR_HOME/data && \
    chown -R solr:solr /opt/solr

USER $SOLR_USER

# Mitigation for CVE-2021-44228
ENV LOG4J_FORMAT_MSG_NO_LOOKUPS true
