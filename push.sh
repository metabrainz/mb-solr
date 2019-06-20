#!/bin/bash
#
# Build image from the currently checked out version of
# MusicBrainz Solr search server (mb-solr)
# and push it to Docker Hub, tagged with versions and variants.
#
# Examples:
# - working tree at git tag v3.1.1 will push docker tags :3.1.1 :3.1 and :3
# - working tree at git tag v3.1.1-rc.1 will push docker tag :3.1.1-rc.1 only
# - untagged working tree v3.1-1-gbfe66e3 will push docker tag 3.1.1-gbfe66e3 only
# - uncommitted/dirty working tree v3.1-dirty will push docker tag :3.1-dirty only
#
# Usage:
#   $ ./push.sh

set -e -u

image_name='metabrainz/mb-solr'

cd "$(dirname "${BASH_SOURCE[0]}")/"

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

# add aliases if version is of format major.minor.patch
if [[ $version =~ ^([0-9]+)\.([0-9]+)\.[0-9]\+$ ]]; then
  major=${BASH_REMATCH[1]}
  minor=${BASH_REMATCH[2]}
  version_aliases=( "${major}.${minor}" "${major}" )
  echo "$0: building version '$version' with aliases:"
  for version_alias in ${version_aliases[@]}; do
    echo "$0: - '$version_alias'"
  done
else
  version_aliases=()
  echo "$0: building version '$version' without alias"
fi

tag=${version}
tag_aliases=${version_aliases[@]}
timestamp=`date -u +"%Y-%m-%dT%H:%M:%SZ"`

docker build \
  --build-arg MB_SOLR_VERSION=${version} \
  --build-arg BUILD_DATE=${timestamp} \
  --build-arg VCS_REF=${vcs_ref} \
  --tag ${image_name}:${tag} . \
  | tee ./"build-${version}-at-${timestamp}.log"

docker push ${image_name}:${tag}

for tag_alias in ${tag_aliases[@]}; do
  docker tag ${image_name}:${tag} metabrainz/mb-solr:${tag_alias}
  docker push ${image_name}:${tag_alias}
done
