#!/usr/bin/env bash

# Execute in a sub-shell to preserve the environment
# (See the script run-initdb from Solr 9.7 repository)
(
    set -e -o pipefail -u

    mbsssss_dir='/usr/lib/mbsssss'
    solr_conf_file='solr.xml'

    # Check if the Solr configuration file exists
    if [[ ! -f "$SOLR_HOME/$solr_conf_file" ]]
    then
        echo 'Installing Solr configuration file...'
        install --mode=0644 "$mbsssss_dir/$solr_conf_file" "$SOLR_HOME/"
        echo 'Done installing Solr configuration file.'
    fi
)
