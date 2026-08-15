package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.Annotation;

public class AnnotationBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Annotation annotation = new Annotation();
        annotation.setType(getString(doc, "type"));
        annotation.setEntity(getString(doc, "entity"));
        annotation.setName(getString(doc, "name"));
        annotation.setText(getString(doc, "text"));
        return annotation;
    }
}
