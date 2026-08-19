package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.Utils;
import org.musicbrainz.mmd2.*;
import org.musicbrainz.search.solrwriter.MMDObjectBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public abstract class EntityBuilderBase implements MMDObjectBuilder {

    protected static String getString(Map<?, ?> m, String key) {
        return Objects.toString(m.get(key), null);
    }

    protected static List<String> toStringList(Collection<Object> coll) {
        List<String> list = new ArrayList<>();
        if (coll != null) {
            for (Object obj : coll) {
                list.add(Objects.toString(obj, null));
            }
        }
        return list;
    }

    protected static List<Map<?, ?>> parseJsonList(SolrDocument doc, String field) {
        List<Map<?, ?>> parsed = new ArrayList<>();
        Collection<Object> values = doc.getFieldValues(field);
        if (values != null) {
            for (Object raw : values) {
                String json = Objects.toString(raw, null);
                if (json == null || json.isEmpty()) {
                    continue;
                }
                if (Utils.fromJSONString(json) instanceof Map<?, ?> parsedDict) {
                    parsed.add(parsedDict);
                }
            }
        }
        return parsed;
    }

    protected static BigInteger toBigInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return BigInteger.valueOf(number.longValue());
        }
        return new BigInteger(value.toString());
    }

    protected static RelationList newRelationList(String targetType) {
        RelationList list = new RelationList();
        list.setTargetType(targetType);
        return list;
    }

    protected static Alias buildAlias(String content, String sortName, String locale, String primary,
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

    protected static AliasList buildAliasList(SolrDocument doc) {
        AliasList aliasList = new AliasList();
        Collection<Object> aliasJsons = doc.getFieldValues("alias_json");
        if (aliasJsons != null) {
            for (Object raw : aliasJsons) {
                String json = Objects.toString(raw, null);
                if (json == null || json.isEmpty()) {
                    continue;
                }
                Object parsed = Utils.fromJSONString(json);
                if (!(parsed instanceof Map<?, ?> m)) {
                    continue;
                }
                aliasList.getAlias().add(buildAlias(
                        getString(m, "name"),
                        getString(m, "sort_name"),
                        getString(m, "locale"),
                        getString(m, "primary"),
                        getString(m, "begin_date"),
                        getString(m, "end_date"),
                        getString(m, "type"),
                        getString(m, "type_id")
                ));
            }
        }
        return aliasList.getAlias().isEmpty() ? null : aliasList;
    }

    protected static LifeSpan buildLifeSpan(Object begin, Object end, Object ended) {
        String beginStr = Objects.toString(begin, null);
        String endStr = Objects.toString(end, null);
        String endedStr = Objects.toString(ended, null);
        if (beginStr == null && endStr == null && endedStr == null) {
            return null;
        }
        LifeSpan lifeSpan = new LifeSpan();
        lifeSpan.setBegin(beginStr);
        lifeSpan.setEnd(endStr);
        lifeSpan.setEnded(endedStr);
        return lifeSpan;
    }

    protected static TagList buildTagList(SolrDocument doc) {
        TagList tagList = new TagList();
        List<String> tagNames = toStringList(doc.getFieldValues("tag"));
        List<String> tagCounts = toStringList(doc.getFieldValues("tag_count"));
        for (int i = 0; i < tagNames.size(); i++) {
            Tag tag = new Tag();
            tag.setName(tagNames.get(i));
            if (i < tagCounts.size() && tagCounts.get(i) != null) {
                tag.setCount(BigInteger.valueOf(Long.parseLong(tagCounts.get(i))));
            }
            tagList.getTag().add(tag);
        }
        return tagList.getTag().isEmpty() ? null : tagList;
    }

    protected static Relation buildRelation(Map<?, ?> m) {
        Relation relation = new Relation();
        relation.setType(getString(m, "type"));
        relation.setTypeId(getString(m, "type_id"));
        String direction = getString(m, "direction");
        if (direction != null) {
            relation.setDirection(DefDirection.fromValue(direction));
        }
        String targetGid = getString(m, "target");
        if (targetGid != null) {
            Target target = new Target();
            target.setValue(targetGid);
            relation.setTarget(target);
        }
        return relation;
    }

    protected static DefAreaElementInner buildArea(SolrDocument doc, String prefix) {
        String gid = getString(doc, prefix + "_gid");
        if (gid == null) {
            return null;
        }
        DefAreaElementInner area = new DefAreaElementInner();
        area.setId(gid);
        area.setName(getString(doc, prefix + "_name"));
        // Areas have no sort name of their own, wscompat converter always emitted sort-name = name
        area.setSortName(getString(doc, prefix + "_name"));
        area.setType(getString(doc, prefix + "_type"));
        area.setTypeId(getString(doc, prefix + "_type_gid"));
        area.setLifeSpan(buildLifeSpan(doc.get(prefix + "_begindate"), doc.get(prefix + "_enddate"),
                doc.get(prefix + "_ended")));
        return area;
    }

    protected static IpiList buildIpiList(SolrDocument doc) {
        IpiList ipiList = new IpiList();
        Collection<Object> raw_ipi_list = doc.getFieldValues("ipi");
        if (raw_ipi_list != null) {
            for (Object o : raw_ipi_list) {
                ipiList.getIpi().add(Objects.toString(o, null));
            }
        }
        return ipiList.getIpi().isEmpty() ? null : ipiList;
    }

    protected static IsniList buildIsniList(SolrDocument doc) {
        IsniList isniList = new IsniList();
        Collection<Object> raw_isni_list = doc.getFieldValues("isni");
        if (raw_isni_list != null) {
            for (Object o : raw_isni_list) {
                isniList.getIsni().add(Objects.toString(o, null));
            }
        }
        return isniList.getIsni().isEmpty() ? null : isniList;
    }

    protected static Artist buildInnerArtist(Map<?, ?> m) {
        Artist artist = new Artist();
        artist.setId(getString(m, "id"));
        artist.setName(getString(m, "name"));
        artist.setDisambiguation(getString(m, "disambiguation"));
        artist.setSortName(getString(m, "sort_name"));
        return artist;
    }

    // Function used to discard empty RelationLists to avoid adding empty keys to output.
    protected static List<RelationList> nonEmptyRelationLists(RelationList... lists) {
        List<RelationList> out = new ArrayList<>();
        for (RelationList list : lists) {
            if (!list.getRelation().isEmpty()) {
                out.add(list);
            }
        }
        return out;
    }

    protected static ArtistCredit buildArtistCredit(SolrDocument doc) {
        String json = getString(doc, "artist_credit_json");
        if (json == null || json.isEmpty()) {
            return null;
        }
        if (!(Utils.fromJSONString(json) instanceof Map<?, ?> artistCreditDict)) {
            return null;
        }
        return buildArtistCreditFromDict(artistCreditDict);
    }

    protected static ArtistCredit buildArtistCreditFromDict(Map<?, ?> artistCreditDict) {
        ArtistCredit artistCredit = new ArtistCredit();
        artistCredit.setId(getString(artistCreditDict, "id"));
        if (artistCreditDict.get("name_credits") instanceof List<?> credits) {
            for (Object entry : credits) {
                if (entry instanceof Map<?, ?> creditDict) {
                    artistCredit.getNameCredit().add(buildNameCredit(creditDict));
                }
            }
        }
        return artistCredit;
    }

    private static NameCredit buildNameCredit(Map<?, ?> creditDict) {
        NameCredit nameCredit = new NameCredit();
        nameCredit.setName(getString(creditDict, "name"));
        if (creditDict.get("artist") instanceof Map<?, ?> artistDict) {
            Artist artist = buildInnerArtist(artistDict);
            artist.setAliasList(buildAliasListFromDicts(artistDict.get("aliases")));
            nameCredit.setArtist(artist);
        }
        nameCredit.setJoinphrase(getString(creditDict, "joinphrase"));
        return nameCredit;
    }

    protected static AliasList buildAliasListFromDicts(Object raw) {
        if (!(raw instanceof List<?> values)) {
            return null;
        }
        AliasList aliasList = new AliasList();
        for (Object value : values) {
            if (value instanceof Map<?, ?> aliasDict) {
                aliasList.getAlias().add(buildAlias(
                        getString(aliasDict, "name"),
                        getString(aliasDict, "sort_name"),
                        getString(aliasDict, "locale"),
                        getString(aliasDict, "primary"),
                        getString(aliasDict, "begin_date"),
                        getString(aliasDict, "end_date"),
                        getString(aliasDict, "type"),
                        getString(aliasDict, "type_id")
                ));
            }
        }
        return aliasList.getAlias().isEmpty() ? null : aliasList;
    }

    // Values of a key that may hold a scalar (single-valued field, or multiValued one collapsed by processSolrDocument)
    // or a collection.
    protected static List<String> toStringValues(Object raw) {
        List<String> values = new ArrayList<>();
        if (raw instanceof Collection<?> collection) {
            for (Object value : collection) {
                values.add(Objects.toString(value, null));
            }
        } else if (raw != null) {
            values.add(Objects.toString(raw, null));
        }
        return values;
    }

    protected static ReleaseEventList buildReleaseEventList(Object raw) {
        if (!(raw instanceof List<?> values) || values.isEmpty()) {
            return null;
        }
        ReleaseEventList eventList = new ReleaseEventList();
        for (Object value : values) {
            if (value instanceof Map<?, ?> eventDict) {
                ReleaseEvent releaseEvent = new ReleaseEvent();
                releaseEvent.setArea(buildReleaseEventArea(eventDict.get("area")));
                releaseEvent.setDate(getString(eventDict, "date"));
                eventList.getReleaseEvent().add(releaseEvent);
            }
        }
        return eventList;
    }

    protected static DefAreaElementInner buildReleaseEventArea(Object raw) {
        if (!(raw instanceof Map<?, ?> areaDict)) {
            return null;
        }
        DefAreaElementInner area = new DefAreaElementInner();
        area.setId(getString(areaDict, "id"));
        area.setName(getString(areaDict, "name"));
        // Areas have no sort name of their own, convert_area_for_release_event always emitted sort-name = name
        area.setSortName(getString(areaDict, "name"));
        if (areaDict.get("iso_3166_1_codes") instanceof List<?> codes && !codes.isEmpty()) {
            Iso31661CodeList codeList = new Iso31661CodeList();
            for (Object code : codes) {
                codeList.getIso31661Code().add(Objects.toString(code, null));
            }
            area.setIso31661CodeList(codeList);
        }
        return area;
    }

    protected static String firstCountryCode(Map<?, ?> eventDict) {
        if (eventDict.get("area") instanceof Map<?, ?> areaDict
                && areaDict.get("iso_3166_1_codes") instanceof List<?> codes
                && !codes.isEmpty()) {
            return Objects.toString(codes.get(0), null);
        }
        return null;
    }

    protected static PrimaryType buildPrimaryType(Map<?, ?> m) {
        String name = getString(m, "primarytype");
        if (name == null) {
            return null;
        }
        PrimaryType primaryType = new PrimaryType();
        primaryType.setContent(name);
        primaryType.setId(getString(m, "primarytype_gid"));
        return primaryType;
    }

    protected static SecondaryTypeList buildSecondaryTypeList(Map<?, ?> m) {
        List<String> names = toStringValues(m.get("secondarytype"));
        if (names.isEmpty()) {
            return null;
        }
        List<String> gids = toStringValues(m.get("secondarytype_gid"));
        SecondaryTypeList typeList = new SecondaryTypeList();
        for (int i = 0; i < names.size(); i++) {
            SecondaryType secondaryType = new SecondaryType();
            secondaryType.setContent(names.get(i));
            if (i < gids.size()) {
                secondaryType.setId(gids.get(i));
            }
            typeList.getSecondaryType().add(secondaryType);
        }
        return typeList;
    }






}
