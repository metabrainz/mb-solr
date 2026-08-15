package org.musicbrainz.search.solrwriter;

import org.apache.lucene.index.IndexableField;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.ResultContext;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.DocList;
import org.musicbrainz.search.solrwriter.entityBuilders.*;

import java.util.*;

public class MBFlatXMLWriter extends MBXMLWriter {

    private static final Map<entityTypes, MMDObjectBuilder> BUILDER_MAP =
            new EnumMap<>(entityTypes.class);
    static {
        BUILDER_MAP.put(entityTypes.artist, new ArtistBuilder());
        BUILDER_MAP.put(entityTypes.annotation, new AnnotationBuilder());
        BUILDER_MAP.put(entityTypes.area, new AreaBuilder());
        BUILDER_MAP.put(entityTypes.cdstub, new CdstubBuilder());
        BUILDER_MAP.put(entityTypes.editor, new EditorBuilder());
        BUILDER_MAP.put(entityTypes.event, new EventBuilder());
        BUILDER_MAP.put(entityTypes.instrument, new InstrumentBuilder());
        BUILDER_MAP.put(entityTypes.label, new LabelBuilder());
        BUILDER_MAP.put(entityTypes.place, new PlaceBuilder());
        BUILDER_MAP.put(entityTypes.series, new SeriesBuilder());
        BUILDER_MAP.put(entityTypes.url, new UrlBuilder());
        BUILDER_MAP.put(entityTypes.tag, new TagBuilder());
        BUILDER_MAP.put(entityTypes.work, new WorkBuilder());
        BUILDER_MAP.put(entityTypes.release_group, new ReleaseGroupBuilder());
        BUILDER_MAP.put(entityTypes.release, new ReleaseBuilder());
        BUILDER_MAP.put(entityTypes.recording, new RecordingBuilder());
    }
    private MMDObjectBuilder flatBuilder;

    @Override
    public void init(NamedList initArgs) {
        super.init(initArgs);
        flatBuilder = BUILDER_MAP.get(entityType);
        if (flatBuilder == null) {
            throw new RuntimeException(
                    "MBFlatXMLWriter has no flat builder registered for entity type '"
                            + entityType);
        }
    }

    @Override
    public void parseSolrResponse(ResultContext con, MetadataListWrapper metadatalistwrapper,
                                  SolrQueryRequest req) {
        DocList docList = con.getDocList();
        emitDocuments(con.getProcessedDocuments(), docList.matches(), docList.offset(),
                docList.maxScore(), req.getSchema(), metadatalistwrapper);
    }

    @Override
    public void parseSolrResponse(SolrDocumentList docList, MetadataListWrapper metadatalistwrapper,
                                  SolrQueryRequest req) {
        emitDocuments(docList.iterator(), docList.getNumFound(), docList.getStart(),
                docList.getMaxScore(), req.getSchema(), metadatalistwrapper);
    }

    private void emitDocuments(Iterator<SolrDocument> docs, long numFound, long offset,
                               Float maxScore, IndexSchema schema,
                               MetadataListWrapper metadatalistwrapper) {
        metadatalistwrapper.setCountAndOffset(numFound, offset);
        List<Object> liveList = metadatalistwrapper.getLiveList();

        while (docs.hasNext()) {
            SolrDocument doc = processSolrDocument(docs.next(), schema);
            Object entity = flatBuilder.build(doc);
            Object score = doc.get("score");
            if (score == null || maxScore == null) {
                throw new RuntimeException(SCORE_NOT_IN_FIELD_LIST);
            }
            adjustScore(maxScore, entity, Float.parseFloat(score.toString()));
            liveList.add(entity);
        }
    }

    private SolrDocument processSolrDocument(SolrDocument rawDoc, IndexSchema schema){
        SolrDocument cleanDoc = new SolrDocument();
        for (String fieldName : rawDoc.getFieldNames()) {
            Collection<Object> values = rawDoc.getFieldValues(fieldName);
            if (values == null) {
                continue;
            }
            SchemaField sf = schema.getFieldOrNull(fieldName);
            List<Object> cleanValues = new ArrayList<>();
            for (Object val : values) {
                if (val instanceof IndexableField f) {
                    cleanValues.add(sf != null
                            ? sf.getType().toObject(f)
                            : (f.stringValue() != null ? f.stringValue() : f.numericValue()));
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