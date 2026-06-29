package org.musicbrainz.search.solrwriter;

import org.apache.solr.common.SolrDocument;

public interface MMDObjectBuilder{
    Object build(SolrDocument doc);
}