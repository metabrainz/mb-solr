#!/bin/sh

docker build -t metabrainz/solr .
docker push metabrainz/solr