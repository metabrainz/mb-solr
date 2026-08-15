package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.Tag;

public class TagBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Tag tag = new Tag();
        tag.setName(getString(doc, "tag"));
        return tag;
    }
}
