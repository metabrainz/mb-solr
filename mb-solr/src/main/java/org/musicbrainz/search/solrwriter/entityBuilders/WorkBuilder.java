package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.IswcList;
import org.musicbrainz.mmd2.LanguageList;
import org.musicbrainz.mmd2.Recording;
import org.musicbrainz.mmd2.Relation;
import org.musicbrainz.mmd2.RelationList;
import org.musicbrainz.mmd2.Work;

import java.util.List;
import java.util.Map;

public class WorkBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Work work = new Work();
        work.setId(getString(doc, "mbid"));
        work.setTitle(getString(doc, "work"));
        work.setAliasList(buildAliasList(doc));
        List<RelationList> relationLists = buildRelationLists(doc);
        if (!relationLists.isEmpty()){
            work.getRelationList().addAll(relationLists);
        }
        work.setDisambiguation(getString(doc, "comment"));
        work.setType(getString(doc, "type"));
        work.setTypeId(getString(doc, "type_gid"));
        work.setLanguageList(buildLanguageList(doc));
        work.setLanguage(buildLanguage(doc));
        work.setIswcList(buildIswcList(doc));
        return work;
    }

    private List<RelationList> buildRelationLists(SolrDocument doc) {
        RelationList artistList = newRelationList("artist");
        RelationList recordingList = newRelationList("recording");
        for (Map<?, ?> m : parseJsonList(doc, "rel_json")) {
            Relation relation = buildRelation(m);
            String targetType = getString(m, "target_type");
            if ("artist".equals(targetType) && m.get("artist") instanceof Map<?, ?> inner) {
                relation.setArtist(buildInnerArtist(inner));
                artistList.getRelation().add(relation);
            } else if ("recording".equals(targetType) && m.get("recording") instanceof Map<?, ?> inner) {
                relation.setRecording(buildInnerRecording(inner));
                recordingList.getRelation().add(relation);
            }
        }
        return nonEmptyRelationLists(artistList, recordingList);
    }

    private static Recording buildInnerRecording(Map<?, ?> m) {
        Recording recording = new Recording();
        recording.setId(getString(m, "id"));
        recording.setTitle(getString(m, "title"));
        recording.setVideo(getString(m, "video"));
        return recording;
    }

    private static LanguageList buildLanguageList(SolrDocument doc) {
        List<String> codes = toStringList(doc.getFieldValues("lang"));
        if (codes.isEmpty()) {
            return null;
        }
        LanguageList languageList = new LanguageList();
        for (String code : codes) {
            LanguageList.Language language = new LanguageList.Language();
            language.setValue(code);
            languageList.getLanguage().add(language);
        }
        return languageList;
    }

    private static String buildLanguage(SolrDocument doc) {
        List<String> codes = toStringList(doc.getFieldValues("lang"));
        if (codes.isEmpty()) {
            return null;
        }
        return codes.size() == 1 ? codes.get(0) : "mul";
    }

    private static IswcList buildIswcList(SolrDocument doc) {
        List<String> iswcs = toStringList(doc.getFieldValues("iswc"));
        if (iswcs.isEmpty()) {
            return null;
        }
        IswcList iswcList = new IswcList();
        iswcList.getIswc().addAll(iswcs);
        return iswcList;
    }
}
