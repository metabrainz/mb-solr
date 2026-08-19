package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.Series;

public class SeriesBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Series series = new Series();
        series.setId(getString(doc, "mbid"));
        series.setName(getString(doc, "series"));
        series.setDisambiguation(getString(doc, "comment"));
        series.setAliasList(buildAliasList(doc));
        series.setTagList(buildTagList(doc));
        series.setType(getString(doc, "type"));
        series.setTypeId(getString(doc, "type_gid"));
        return series;
    }
}
