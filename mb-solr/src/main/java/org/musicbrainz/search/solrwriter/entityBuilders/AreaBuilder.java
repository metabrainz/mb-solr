package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.*;

import java.util.List;
import java.util.Map;


public class AreaBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        DefAreaElementInner area = new DefAreaElementInner();
        area.setId(getString(doc, "mbid"));
        area.setName(getString(doc, "area"));
        // Area have no sort name of their own, the wscompat converter always emitted sort-name = name.
        area.setSortName(getString(doc, "area"));
        area.setAliasList(buildAliasList(doc));
        area.setDisambiguation(getString(doc, "comment"));
        area.setType(getString(doc, "type"));
        area.setTypeId(getString(doc, "type_gid"));
        area.setLifeSpan(buildLifeSpan(doc.get("begin"), doc.get("end"), doc.get("ended")));
        area.setTagList(buildTagList(doc));
        area.setIso31661CodeList(buildIso31661CodeList(doc));
        area.setIso31662CodeList(buildIso31662CodeList(doc));
        area.setIso31663CodeList(buildIso31663CodeList(doc));
        RelationList relationList = buildRelationList(doc);
        if (relationList != null) {
            area.getRelationList().add(relationList);
        }
        return area;
}
    private Iso31661CodeList buildIso31661CodeList(SolrDocument doc) {
        List<String> codes = toStringList(doc.getFieldValues("iso1"));
        if (codes.isEmpty()) {
            return null;
        }
        Iso31661CodeList codeList = new Iso31661CodeList();
        codeList.getIso31661Code().addAll(codes);
        return codeList;
    }

    private Iso31662CodeList buildIso31662CodeList(SolrDocument doc) {
        List<String> codes = toStringList(doc.getFieldValues("iso2"));
        if (codes.isEmpty()) {
            return null;
        }
        Iso31662CodeList codeList = new Iso31662CodeList();
        codeList.getIso31662Code().addAll(codes);
        return codeList;
    }

    private Iso31663CodeList buildIso31663CodeList(SolrDocument doc) {
        List<String> codes = toStringList(doc.getFieldValues("iso3"));
        if (codes.isEmpty()) {
            return null;
        }
        Iso31663CodeList codeList = new Iso31663CodeList();
        codeList.getIso31663Code().addAll(codes);
        return codeList;
    }

    private RelationList buildRelationList(SolrDocument doc) {
        RelationList relationList = newRelationList("area");
        for (Map<?, ?> m : parseJsonList(doc, "rel_json")) {
            Relation relation = buildRelation(m);
            if (m.get("area") instanceof Map<?, ?> innerMap) {
                relation.setArea(buildInnerArea(innerMap));
            }
            relationList.getRelation().add(relation);
        }
        return relationList.getRelation().isEmpty() ? null : relationList;
    }

    private DefAreaElementInner buildInnerArea(Map<?, ?> m) {
        DefAreaElementInner area = new DefAreaElementInner();
        area.setId(getString(m, "id"));
        area.setName(getString(m, "name"));
        // Area have no sort name of their own, the wscompat converter always emitted sort-name = name.
        area.setSortName(getString(m, "name"));
        area.setType(getString(m, "type"));
        area.setTypeId(getString(m, "type_id"));
        area.setLifeSpan(buildLifeSpan(m.get("begin_date"), m.get("end_date"), m.get("ended")));
        return area;
    }
}
