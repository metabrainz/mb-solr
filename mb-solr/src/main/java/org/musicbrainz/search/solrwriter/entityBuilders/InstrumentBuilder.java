package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.Instrument;

public class InstrumentBuilder extends EntityBuilderBase{
    @Override
    public Object build(SolrDocument doc){
        Instrument instrument = new Instrument();
        instrument.setId(getString(doc, "mbid"));
        instrument.setName(getString(doc, "instrument"));
        instrument.setDisambiguation(getString(doc, "comment"));
        instrument.setDescription(getString(doc, "description"));
        instrument.setType(getString(doc, "type"));
        instrument.setTypeId(getString(doc, "type_gid"));
        instrument.setAliasList(buildAliasList(doc));
        instrument.setTagList(buildTagList(doc));
        return instrument;
    }
}
