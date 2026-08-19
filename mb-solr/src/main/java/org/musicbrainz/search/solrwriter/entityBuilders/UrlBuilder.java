package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.Relation;
import org.musicbrainz.mmd2.RelationList;
import org.musicbrainz.mmd2.Release;
import org.musicbrainz.mmd2.Url;

import java.util.List;
import java.util.Map;

public class UrlBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Url url = new Url();
        url.setId(getString(doc, "mbid"));
        url.setResource(getString(doc, "url"));
        List<RelationList> relationLists = buildRelationLists(doc);
        if (!relationLists.isEmpty()){
            url.getRelationList().addAll(relationLists);
        }
        return url;
    }

    private List<RelationList> buildRelationLists(SolrDocument doc) {
        RelationList artistList = newRelationList("artist");
        RelationList releaseList = newRelationList("release");
        for (Map<?, ?> m : parseJsonList(doc, "rel_json")) {
            Relation relation = buildRelation(m);
            String targetType = getString(m, "target_type");
            if ("artist".equals(targetType) && m.get("artist") instanceof Map<?, ?> inner) {
                relation.setArtist(buildInnerArtist(inner));
                artistList.getRelation().add(relation);
            } else if ("release".equals(targetType) && m.get("release") instanceof Map<?, ?> inner) {
                relation.setRelease(buildInnerRelease(inner));
                releaseList.getRelation().add(relation);
            }
        }
        return nonEmptyRelationLists(artistList, releaseList);
    }

    private static Release buildInnerRelease(Map<?, ?> m) {
        Release release = new Release();
        release.setId(getString(m, "id"));
        release.setTitle(getString(m, "title"));
        release.setDisambiguation(getString(m, "disambiguation"));
        return release;
    }
}
