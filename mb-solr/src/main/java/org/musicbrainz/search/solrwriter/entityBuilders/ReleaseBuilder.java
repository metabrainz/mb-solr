package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.DiscList;
import org.musicbrainz.mmd2.Format;
import org.musicbrainz.mmd2.Label;
import org.musicbrainz.mmd2.LabelInfo;
import org.musicbrainz.mmd2.LabelInfoList;
import org.musicbrainz.mmd2.Medium;
import org.musicbrainz.mmd2.MediumList;
import org.musicbrainz.mmd2.Packaging;
import org.musicbrainz.mmd2.Release;
import org.musicbrainz.mmd2.ReleaseGroup;
import org.musicbrainz.mmd2.Status;
import org.musicbrainz.mmd2.TextRepresentation;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class ReleaseBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Release release = new Release();
        release.setId(getString(doc, "mbid"));
        release.setTitle(getString(doc, "release"));
        release.setArtistCredit(buildArtistCredit(doc));
        release.setBarcode(buildBarcode(doc));
        release.setDisambiguation(getString(doc, "comment"));
        release.setPackaging(buildPackaging(doc));
        List<Map<?, ?>> eventDicts = parseJsonList(doc, "release_event_json");
        if (!eventDicts.isEmpty()) {
            release.setReleaseEventList(buildReleaseEventList(eventDicts));
            Map<?, ?> firstEvent = eventDicts.get(0);
            release.setDate(getString(firstEvent, "date"));
            release.setCountry(firstCountryCode(firstEvent));
        }
        release.setLabelInfoList(buildLabelInfoList(doc));
        release.setMediumList(buildMediumList(doc));
        release.setReleaseGroup(buildReleaseGroup(doc));
        release.setStatus(buildStatus(doc));
        release.setTagList(buildTagList(doc));
        release.setTextRepresentation(buildTextRepresentation(doc));
        release.setAsin(getString(doc, "asin"));
        return release;
    }

    // A barcode can have three states.
    // 1. Known
    // 2. Unknown, stored as an empty string in DB. SIR uses the barcode_none flag to indicate this state.
    // 3. NULL
    private static String buildBarcode(SolrDocument doc) {
        String barcode = getString(doc, "barcode");
        if (barcode != null) {
            return barcode;
        }
        return Boolean.parseBoolean(getString(doc, "barcode_none")) ? "" : null;
    }

    private static Packaging buildPackaging(SolrDocument doc) {
        String name = getString(doc, "packaging");
        if (name == null) {
            return null;
        }
        Packaging packaging = new Packaging();
        packaging.setContent(name);
        packaging.setId(getString(doc, "packaging_gid"));
        return packaging;
    }

    private static LabelInfoList buildLabelInfoList(SolrDocument doc) {
        List<Map<?, ?>> labelInfoDicts = parseJsonList(doc, "label_info_json");
        if (labelInfoDicts.isEmpty()) {
            return null;
        }
        LabelInfoList labelInfoList = new LabelInfoList();
        for (Map<?, ?> labelInfoDict : labelInfoDicts) {
            LabelInfo labelInfo = new LabelInfo();
            labelInfo.setCatalogNumber(getString(labelInfoDict, "catalog_number"));
            if (labelInfoDict.get("label") instanceof Map<?, ?> labelDict) {
                Label label = new Label();
                label.setId(getString(labelDict, "id"));
                label.setName(getString(labelDict, "name"));
                labelInfo.setLabel(label);
            }
            labelInfoList.getLabelInfo().add(labelInfo);
        }
        return labelInfoList;
    }

    private static MediumList buildMediumList(SolrDocument doc) {
        List<Map<?, ?>> mediumDicts = parseJsonList(doc, "medium_json");
        if (mediumDicts.isEmpty()) {
            return null;
        }
        MediumList mediumList = new MediumList();
        long trackCount = 0;
        for (Map<?, ?> mediumDict : mediumDicts) {
            Medium medium = new Medium();
            medium.setId(getString(mediumDict, "id"));
            String formatName = getString(mediumDict, "format");
            if (formatName != null) {
                Format format = new Format();
                format.setContent(formatName);
                medium.setFormat(format);
            }
            DiscList discList = new DiscList();
            discList.setCount(toBigInteger(mediumDict.get("disc_count")));
            medium.setDiscList(discList);
            Medium.TrackList trackList = new Medium.TrackList();
            BigInteger mediumTrackCount = toBigInteger(mediumDict.get("track_count"));
            if (mediumTrackCount != null) {
                trackList.setCount(mediumTrackCount);
                trackCount += mediumTrackCount.longValue();
            }
            medium.setTrackList(trackList);
            mediumList.getMedium().add(medium);
        }
        mediumList.setCount(BigInteger.valueOf(mediumDicts.size()));
        mediumList.setTrackCount(BigInteger.valueOf(trackCount));
        return mediumList;
    }

    private static ReleaseGroup buildReleaseGroup(SolrDocument doc) {
        String gid = getString(doc, "rgid");
        if (gid == null) {
            return null;
        }
        ReleaseGroup releaseGroup = new ReleaseGroup();
        releaseGroup.setId(gid);
        releaseGroup.setTitle(getString(doc, "rg_name"));
        releaseGroup.setPrimaryType(buildPrimaryType(doc));
        releaseGroup.setType(getString(doc, "calc_type"));
        releaseGroup.setTypeId(getString(doc, "calc_type_gid"));
        releaseGroup.setSecondaryTypeList(buildSecondaryTypeList(doc));
        releaseGroup.setDisambiguation(getString(doc, "rg_comment"));
        return releaseGroup;
    }

    private static Status buildStatus(SolrDocument doc) {
        String name = getString(doc, "status");
        if (name == null) {
            return null;
        }
        Status status = new Status();
        status.setContent(name);
        status.setId(getString(doc, "status_gid"));
        return status;
    }

    private static TextRepresentation buildTextRepresentation(SolrDocument doc) {
        String language = getString(doc, "lang");
        String script = getString(doc, "script");
        if (language == null && script == null) {
            return null;
        }
        TextRepresentation textRepresentation = new TextRepresentation();
        textRepresentation.setLanguage(language);
        textRepresentation.setScript(script);
        return textRepresentation;
    }
}
