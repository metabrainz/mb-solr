#!/usr/bin/env bash
#
# Build image from the currently checked out version of
# MusicBrainz Solr search server (mb-solr).
#
# Examples:
# - working tree at git tag v3.1.1 will build docker tag :3.1.1 only
# - working tree at git tag v3.1.1-rc.1 will build docker tag :3.1.1-rc.1 only
# - untagged working tree v3.1-1-gbfe66e3 will build docker tag 3.1.1-gbfe66e3 only
# - uncommitted/dirty working tree v3.1-dirty will build docker tag :3.1-dirty only
#
# This script is purposed for contributors.
#
# It is also called from `push.sh` which is for maintainers only.
#
# Usage:
#   $ ./build.sh

set -e -u

image_name='metabrainz/mb-solr'

cd "$(dirname "${BASH_SOURCE[0]}")/"

DOCKER_CMD=${DOCKER_CMD:-docker}
vcs_ref=`git describe --always --broken --dirty --tags`
version=${vcs_ref#v}

# enforce version format major.minor.patch if possible
if [[ $version =~ ^[0-9]+$ ]]; then
  version="${version}.0.0"
  echo "$0: appended .0.0 to version"
elif [[ $version =~ ^[0-9]+\.[0-9]+$ ]]; then
  version="${version}.0"
  echo "$0: appended .0 to version"
fi

tag=${version}
timestamp=`date -u +"%Y-%m-%dT%H:%M:%SZ"`

${DOCKER_CMD} build \
  --build-arg MB_SOLR_VERSION=${version} \
  --build-arg BUILD_DATE=${timestamp} \
  --build-arg VCS_REF=${vcs_ref} \
  --tag ${image_name}:${tag} . \
  | tee ./"build-${version}-at-${timestamp}.log"

