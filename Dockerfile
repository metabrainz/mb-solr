ARG MAVEN_TAG=3.9.6-eclipse-temurin-17
ARG SOLR_NAME=solr
ARG SOLR_TAG=9.5.0

FROM maven:${MAVEN_TAG} AS builder

# Caching the maven dependencies so that these are built only if
# the dependencies are changed and not the source code.
COPY ./mb-solr/pom.xml mb-solr/pom.xml
COPY ./mmd-schema/brainz-mmd2-jaxb/pom.xml brainz-mmd2-jaxb/pom.xml
RUN --mount=type=cache,target=/root/.m2 \
    cd brainz-mmd2-jaxb && \
    mvn verify clean --fail-never && \
    echo BUILD SUCCESS is expected above && \
    cd ../mb-solr && \
    mvn verify clean --fail-never && \
    echo BUILD FAILURE is expected above because brainz-mmd-jaxb is not installed yet

COPY ./mmd-schema/brainz-mmd2-jaxb brainz-mmd2-jaxb
COPY ./mb-solr mb-solr
RUN --mount=type=cache,target=/root/.m2 \
    cd brainz-mmd2-jaxb && \
    # Assume that Java classes have been regenerated and patched
    find src/main/java -type f -print0 | xargs -0 touch && \
    mvn install && \
    cd ../mb-solr && \
    mvn package -DskipTests

RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:get -Dartifact=jakarta.activation:jakarta.activation-api:2.1.3 && \
    cp -a /root/.m2/repository/jakarta/activation/jakarta.activation-api/2.1.3/jakarta.activation-api-2.1.3.jar .

FROM ${SOLR_NAME}:${SOLR_TAG}
ARG SOLR_TAG

# Resetting value set in the parent image
USER root

ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        # Needed to decompress search index dumps
        zstd \
        && \
    rm -rf /var/lib/apt/lists/*

COPY --from=builder --chown=root:root \
     jakarta.activation-api-2.1.3.jar \
     /opt/solr/server/lib/ext

COPY --from=builder --chown=solr:solr \
     mb-solr/target/mb-solr-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
     /opt/solr/lib/

COPY --chown=solr:solr \
    ./mbsssss \
    /var/solr/data/mycores/mbsssss

# Creating directory for seach indexes data
# with ownership to `solr` user/group,
# so to be used for mounting volume.
RUN install -d -g solr -o solr /var/solr/data/data

ARG MAVEN_TAG
ARG SOLR_NAME

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

# Restoring value set in the parent image
USER solr
