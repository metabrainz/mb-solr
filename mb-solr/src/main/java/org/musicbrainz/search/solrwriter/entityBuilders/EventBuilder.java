package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.*;

import java.util.List;
import java.util.Map;


public class EventBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Event event = new Event();
        event.setId(getString(doc, "mbid"));
        event.setName(getString(doc, "event"));
        event.setDisambiguation(getString(doc, "comment"));
        event.setType(getString(doc, "type"));
        event.setTypeId(getString(doc, "type_gid"));
        event.setLifeSpan(buildEventLifeSpan(doc));
        event.setTime(getString(doc, "time"));
        List<RelationList> relationLists = buildRelationLists(doc);
        if (!relationLists.isEmpty()){
            event.getRelationList().addAll(relationLists);
        }
        event.setAliasList(buildAliasList(doc));
        event.setTagList(buildTagList(doc));
        return event;
    }

    private Event.LifeSpan buildEventLifeSpan(SolrDocument doc) {
        // There is no `ended` field in Event.LifeSpan mmd object
        String begin = getString(doc, "begin");
        String end = getString(doc, "end");
        if (begin == null && end == null) {
            return null;
        }
        Event.LifeSpan lifeSpan = new Event.LifeSpan();
        lifeSpan.setBegin(begin);
        lifeSpan.setEnd(end);
        return lifeSpan;
    }

    private List<RelationList> buildRelationLists(SolrDocument doc) {
        RelationList areaList = newRelationList("area");
        RelationList artistList = newRelationList("artist");
        RelationList placeList = newRelationList("place");
        for (Map<?, ?> m : parseJsonList(doc, "rel_json")) {
            Relation relation = buildRelation(m);
            String targetType = getString(m, "target_type");
            if ("area".equals(targetType) && m.get("area") instanceof Map<?, ?> inner) {
                relation.setArea(buildInnerArea(inner));
                areaList.getRelation().add(relation);
            } else if ("artist".equals(targetType) && m.get("artist") instanceof Map<?, ?> inner) {
                relation.setArtist(buildInnerArtist(inner));
                artistList.getRelation().add(relation);
            } else if ("place".equals(targetType) && m.get("place") instanceof Map<?, ?> inner) {
                relation.setPlace(buildInnerPlace(inner));
                placeList.getRelation().add(relation);
            }
        }
        return nonEmptyRelationLists(areaList, artistList, placeList);
    }

    private static DefAreaElementInner buildInnerArea(Map<?, ?> m) {
        DefAreaElementInner area = new DefAreaElementInner();
        area.setId(getString(m, "id"));
        area.setName(getString(m, "name"));
        return area;
    }

    private static Place buildInnerPlace(Map<?, ?> m) {
        Place place = new Place();
        place.setId(getString(m, "id"));
        place.setName(getString(m, "name"));
        return place;
    }
}
