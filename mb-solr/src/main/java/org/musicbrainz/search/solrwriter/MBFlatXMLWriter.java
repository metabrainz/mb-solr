package org.musicbrainz.search.solrwriter;

import org.apache.lucene.index.IndexableField;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.ResultContext;
import org.apache.solr.search.DocList;
import org.musicbrainz.search.solrwriter.entityBuilders.ArtistBuilder;

import java.util.*;

public class MBFlatXMLWriter extends MBXMLWriter {

    private MMDObjectBuilder flatBuilder;

    @Override
    public void init(NamedList initArgs) {
        super.init(initArgs);
        flatBuilder = new ArtistBuilder();
    }

    @Override
    public void parseSolrResponse(ResultContext con, MetadataListWrapper metadatalistwrapper, SolrQueryRequest req) {
        DocList docList = con.getDocList();
        metadatalistwrapper.setCountAndOffset(docList.matches(), docList.offset());
        var liveList = metadatalistwrapper.getLiveList();
        float maxScore = docList.maxScore();
        Iterator<SolrDocument> iter = con.getProcessedDocuments();
        while (iter.hasNext()) {
            SolrDocument solrDoc = processSolrDocument(iter.next());
            Object artist = flatBuilder.build(solrDoc);
            try{
                adjustScore(maxScore, artist, Float.parseFloat(solrDoc.get("score").toString()));
            }
            catch(NullPointerException e) {
                throw new RuntimeException(SCORE_NOT_IN_FIELD_LIST);
            }
            liveList.add(artist);
        }
    }

    @Override
    public void parseSolrResponse(SolrDocumentList docList, MetadataListWrapper metadatalistwrapper) {
        metadatalistwrapper.setCountAndOffset(docList.getNumFound(), docList.getStart());

        List liveList = metadatalistwrapper.getLiveList();
        float maxScore = docList.getMaxScore();

        for (SolrDocument doc : docList) {
            Object artist = flatBuilder.build(doc);

            try {
                adjustScore(maxScore, artist, Float.parseFloat(doc.get("score").toString()));
            } catch (NullPointerException e) {
                throw new RuntimeException(SCORE_NOT_IN_FIELD_LIST);
            }

            liveList.add(artist);
        }
    }

    private SolrDocument processSolrDocument(SolrDocument rawDoc){
        SolrDocument cleanDoc = new SolrDocument();
        for (String fieldName : rawDoc.getFieldNames()) {
            Collection<Object> values = rawDoc.getFieldValues(fieldName);
            if (values == null) {
                continue;
            }
            List<Object> cleanValues = new ArrayList<>();
            for (Object val : values) {
                if (val instanceof IndexableField f) {
                    cleanValues.add(f.stringValue() != null ? f.stringValue() : f.numericValue());
                } else {
                    cleanValues.add(val);
                }
            }
            if (cleanValues.size() == 1) {
                cleanDoc.setField(fieldName, cleanValues.get(0));
            } else {
                cleanDoc.setField(fieldName, cleanValues);
            }
        }
        return cleanDoc;
    }
}