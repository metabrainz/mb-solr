#!/usr/bin/env bash

# This script installs the Solr configuration file from
# MusicBrainz simple Solr search server schema (mbsssss).

# Execute in a sub-shell to preserve the environment
# (See the script run-initdb from Solr 9.7 repository)
(
    set -e -o pipefail -u

    if [[ "${VERBOSE:-}" == "yes" ]]; then
        set -x
    fi

    mbsssss_dir='/usr/lib/mbsssss'
    solr_conf_file='solr.xml'

    # Check if the Solr configuration file exists
    if [[ ! -f "$SOLR_HOME/$solr_conf_file" ]]
    then
        echo 'Installing Solr configuration file...'
        install --mode=0644 "$mbsssss_dir/$solr_conf_file" "$SOLR_HOME/"
        touch $SOLR_HOME/musicbrainz-solrcloud-first-run
        echo 'Done installing Solr configuration file.'
    fi
)
