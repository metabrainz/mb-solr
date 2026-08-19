package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.Editor;
import java.math.BigInteger;

public class EditorBuilder extends EntityBuilderBase{
    @Override
    public Object build(SolrDocument doc){
        Editor editor = new Editor();
        editor.setId(new BigInteger(getString(doc, "id")));
        editor.setName(getString(doc, "editor"));
        editor.setBio(getString(doc, "bio"));
        return editor;
    }
}
