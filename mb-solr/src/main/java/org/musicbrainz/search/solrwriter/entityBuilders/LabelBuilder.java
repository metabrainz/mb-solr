package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.Label;

import java.math.BigInteger;

public class LabelBuilder extends EntityBuilderBase{
    @Override
    public Object build(SolrDocument doc){
        Label label = new Label();
        label.setId(getString(doc, "mbid"));
        label.setName(getString(doc, "label"));
        // Label have no sort name of their own, the wscompat converter always emitted sort-name = name.
        label.setSortName(getString(doc, "label"));
        label.setType(getString(doc, "type"));
        label.setTypeId(getString(doc, "type_gid"));
        label.setArea(buildArea(doc, "area"));
        label.setCountry(getString(doc, "country"));
        String code = getString(doc, "code");
        if (code != null){
            BigInteger labelcode = new BigInteger(code);
            label.setLabelCode(labelcode);
        }
        label.setAliasList(buildAliasList(doc));
        label.setIpiList(buildIpiList(doc));
        label.setIsniList(buildIsniList(doc));
        label.setDisambiguation(getString(doc, "comment"));
        label.setLifeSpan(buildLifeSpan(doc.get("begin"), doc.get("end"), doc.get("ended")));
        label.setTagList(buildTagList(doc));

        return label;
    }
}

