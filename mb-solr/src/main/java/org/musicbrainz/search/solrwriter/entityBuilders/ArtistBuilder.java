package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.*;
import org.musicbrainz.search.solrwriter.MMDObjectBuilder;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;


public class ArtistBuilder implements MMDObjectBuilder {
    @Override
    public Object build(SolrDocument doc) {
        Artist artist = new Artist();
        artist.setId(Objects.toString(doc.get("mbid"), null));
        artist.setName(Objects.toString(doc.get("artist"), null));
        artist.setSortName(Objects.toString(doc.get("sortname"), null));
        artist.setDisambiguation(Objects.toString(doc.get("comment"), null));
        artist.setCountry(Objects.toString(doc.get("country"), null));
        artist.setType(Objects.toString(doc.get("type_name"), null));
        artist.setTypeId(Objects.toString(doc.get("type_gid"), null));

        artist.setGender(buildGender(doc));
        artist.setLifeSpan(buildLifeSpan(doc.get("begin"), doc.get("end"), doc.get("ended")));
        artist.setArea(buildArea(doc));
        artist.setBeginArea(buildBeginArea(doc));
        artist.setEndArea(buildEndArea(doc));
        artist.setTagList(buildTagList(doc));
        artist.setIpiList(buildIpiList(doc));
        artist.setIsniList(buildIsniList(doc));
        artist.setAliasList(buildAliasList(doc));

        return artist;
    }

    private Gender buildGender(SolrDocument doc) {
        Gender gender = new Gender();
        gender.setId(Objects.toString(doc.get("gender_gid"), null));
        gender.setContent(Objects.toString(doc.get("gender_name"), null));
        return gender;
    }

    private LifeSpan buildLifeSpan(Object begin, Object end, Object ended) {
        LifeSpan lifeSpan = new LifeSpan();
        lifeSpan.setBegin(Objects.toString(begin, null));
        lifeSpan.setEnd(Objects.toString(end, null));
        lifeSpan.setEnded(Objects.toString(ended, null));
        return lifeSpan;
    }

    private AliasList buildAliasList(SolrDocument doc) {
        AliasList aliasList = new AliasList();
        List<String> names = toStringList(doc.getFieldValues("alias"));
        List<String> primaries = toStringList(doc.getFieldValues("alias_primary_for_locale"));
        List<String> sortNames = toStringList(doc.getFieldValues("alias_sortname"));
        List<String> locales = toStringList(doc.getFieldValues("alias_locale"));
        List<String> beginDates = toStringList(doc.getFieldValues("alias_begindate"));
        List<String> endDates = toStringList(doc.getFieldValues("alias_enddate"));
        List<String> typeNames = toStringList(doc.getFieldValues("alias_type_name"));
        List<String> typeIds = toStringList(doc.getFieldValues("alias_type_gid"));
        for (int i = 0; i < names.size(); i++){
            aliasList.getAlias().add(buildAlias(
                    names.get(i),
                    getOrNull(sortNames, i),
                    getOrNull(locales, i),
                    getOrNull(primaries, i),
                    getOrNull(beginDates, i),
                    getOrNull(endDates, i),
                    getOrNull(typeNames, i),
                    getOrNull(typeIds, i)
            ));
        }
        return aliasList;
    }
    private static String getOrNull(List<String> list, int index) {
        return index < list.size() ? list.get(index) : null;
    }

    private Alias buildAlias(String content, String sortName, String locale, String primary,
                             String beginDate, String endDate, String typeName, String typeId) {
        Alias alias = new Alias();
        if (content != null && !content.isEmpty()) alias.setContent(content);
        if (sortName != null && !sortName.isEmpty()) alias.setSortName(sortName);
        if (locale != null && !locale.isEmpty()) alias.setLocale(locale);
        if (primary != null && !primary.isEmpty()) alias.setPrimary(primary);
        if (beginDate != null && !beginDate.isEmpty()) alias.setBeginDate(beginDate);
        if (endDate != null && !endDate.isEmpty()) alias.setEndDate(endDate);
        if (typeName != null && !typeName.isEmpty()) alias.setType(typeName);
        if (typeId != null && !typeId.isEmpty()) alias.setTypeId(typeId);

        return alias;
    }

    private AliasList buildAreaAliasList(Collection<Object> areaAliases){
        AliasList aliasList = new AliasList();
        if (areaAliases != null) {
            for (Object o : areaAliases) {
                Alias alias = new Alias();
                alias.setContent(Objects.toString(o, null));
                aliasList.getAlias().add(alias);
            }
        }
        return aliasList;
    }

    private DefAreaElementInner buildArea(SolrDocument doc) {
        DefAreaElementInner Area = new DefAreaElementInner();
        Area.setName(Objects.toString(doc.get("area_name"), null));
        Area.setId(Objects.toString(doc.get("area_gid"), null));
        Area.setTypeId(Objects.toString(doc.get("area_type_gid"), null));
        Area.setType(Objects.toString(doc.get("area_type_name"), null));
        Area.setLifeSpan(buildLifeSpan(doc.get("area_begindate"), doc.get("area_enddate"), doc.get("area_ended")));
        Area.setAliasList(buildAreaAliasList(doc.getFieldValues("area_aliases_name")));

        return Area;
    }

    private DefAreaElementInner buildEndArea(SolrDocument doc) {
        DefAreaElementInner endArea = new DefAreaElementInner();
        endArea.setName(Objects.toString(doc.get("endarea_name"), null));
        endArea.setId(Objects.toString(doc.get("endarea_gid"), null));
        endArea.setTypeId(Objects.toString(doc.get("endarea_type_gid"), null));
        endArea.setType(Objects.toString(doc.get("endarea_type_name"), null));
        endArea.setLifeSpan(buildLifeSpan(doc.get("endarea_begindate"), doc.get("endarea_enddate"), doc.get("endarea_ended")));
        endArea.setAliasList(buildAreaAliasList(doc.getFieldValues("endarea_aliases_name")));

        return endArea;
    }

    private DefAreaElementInner buildBeginArea(SolrDocument doc) {
        DefAreaElementInner beginArea = new DefAreaElementInner();
        beginArea.setName(Objects.toString(doc.get("beginarea_name"), null));
        beginArea.setId(Objects.toString(doc.get("beginarea_gid"), null));
        beginArea.setTypeId(Objects.toString(doc.get("beginarea_type_gid"), null));
        beginArea.setType(Objects.toString(doc.get("beginarea_type_name"), null));
        beginArea.setLifeSpan(buildLifeSpan(doc.get("beginarea_begindate"), doc.get("beginarea_enddate"), doc.get("beginarea_ended")));
        beginArea.setAliasList(buildAreaAliasList(doc.getFieldValues("beginarea_aliases_name")));

        return beginArea;
    }

    private TagList buildTagList(SolrDocument doc) {
        TagList tagList = new TagList();
        List<String> tagNames = toStringList(doc.getFieldValues("tag_name"));
        List<String> tagCounts = toStringList(doc.getFieldValues("tag_count"));
        for (int i = 0; i < tagNames.size(); i++){
            Tag tag = new Tag();
            tag.setName(tagNames.get(i));
            tag.setCount(BigInteger.valueOf(Long.parseLong(tagCounts.get(i))));
            tagList.getTag().add(tag);
        }
        return tagList;

    }

    private IpiList buildIpiList(SolrDocument doc) {
        IpiList ipiList = new IpiList();
        Collection<Object> raw_ipi_list = doc.getFieldValues("ipi");
        if (raw_ipi_list != null) {
            for (Object o : raw_ipi_list) {
                ipiList.getIpi().add(Objects.toString(o, null));
            }
        }
        return ipiList;
    }

    private IsniList buildIsniList(SolrDocument doc) {
        IsniList isniList = new IsniList();
        Collection<Object> raw_isni_list = doc.getFieldValues("isni");
        if (raw_isni_list != null) {
            for (Object o : raw_isni_list) {
                isniList.getIsni().add(Objects.toString(o, null));
            }
        }
        return isniList;
    }
    private List<String> toStringList(Collection<Object> coll) {
        List<String> list = new ArrayList<>();
        if (coll != null) {
            for (Object obj : coll) {
                list.add(Objects.toString(obj, null));
            }
        }
        return list;
    }
}