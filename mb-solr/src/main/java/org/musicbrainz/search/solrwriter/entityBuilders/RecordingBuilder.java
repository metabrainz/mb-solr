package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.DefTrackData;
import org.musicbrainz.mmd2.Format;
import org.musicbrainz.mmd2.Isrc;
import org.musicbrainz.mmd2.IsrcList;
import org.musicbrainz.mmd2.Medium;
import org.musicbrainz.mmd2.MediumList;
import org.musicbrainz.mmd2.Recording;
import org.musicbrainz.mmd2.Release;
import org.musicbrainz.mmd2.ReleaseGroup;
import org.musicbrainz.mmd2.ReleaseList;
import org.musicbrainz.mmd2.Status;

import java.util.List;
import java.util.Map;

public class RecordingBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Recording recording = new Recording();
        recording.setId(getString(doc, "mbid"));
        recording.setTitle(getString(doc, "recording"));
        recording.setArtistCredit(buildArtistCredit(doc));
        recording.setDisambiguation(getString(doc, "comment"));
        recording.setFirstReleaseDate(getString(doc, "firstreleasedate"));
        recording.setLength(toBigInteger(doc.get("dur")));
        recording.setIsrcList(buildIsrcList(doc));
        recording.setTagList(buildTagList(doc));
        recording.setReleaseList(buildReleaseList(doc));
        if (Boolean.parseBoolean(getString(doc, "video"))) {
            recording.setVideo("true");
        }
        return recording;
    }

    private static IsrcList buildIsrcList(SolrDocument doc) {
        List<String> isrcs = toStringValues(doc.get("isrc"));
        if (isrcs.isEmpty()) {
            return null;
        }
        IsrcList isrcList = new IsrcList();
        for (String value : isrcs) {
            Isrc isrc = new Isrc();
            isrc.setId(value);
            isrcList.getIsrc().add(isrc);
        }
        return isrcList;
    }

    private static ReleaseList buildReleaseList(SolrDocument doc) {
        List<Map<?, ?>> releaseDicts = parseJsonList(doc, "release_json");
        if (releaseDicts.isEmpty()) {
            return null;
        }
        ReleaseList releaseList = new ReleaseList();
        for (Map<?, ?> releaseDict : releaseDicts) {
            releaseList.getRelease().add(buildRelease(releaseDict));
        }
        return releaseList;
    }

    private static Release buildRelease(Map<?, ?> releaseDict) {
        Release release = new Release();
        release.setId(getString(releaseDict, "id"));
        release.setTitle(getString(releaseDict, "title"));
        // skipped by the indexer when release and recording artist credits are the same.
        if (releaseDict.get("artist_credit") instanceof Map<?, ?> creditDict) {
            release.setArtistCredit(buildArtistCreditFromDict(creditDict));
        }
        release.setDisambiguation(getString(releaseDict, "disambiguation"));
        Object rawEvents = releaseDict.get("release_events");
        release.setReleaseEventList(buildReleaseEventList(rawEvents));
        if (rawEvents instanceof List<?> events && !events.isEmpty()
                && events.get(0) instanceof Map<?, ?> firstEvent) {
            release.setDate(getString(firstEvent, "date"));
            release.setCountry(firstCountryCode(firstEvent));
        }

        release.setMediumList(buildMediumList(releaseDict));
        release.setReleaseGroup(buildReleaseGroup(releaseDict.get("release_group")));
        String statusName = getString(releaseDict, "status");
        if (statusName != null) {
            Status status = new Status();
            status.setContent(statusName);
            status.setId(getString(releaseDict, "status_id"));
            release.setStatus(status);
        }
        return release;
    }

    private static MediumList buildMediumList(Map<?, ?> releaseDict) {
        if (!(releaseDict.get("medium") instanceof Map<?, ?> mediumDict)) {
            return null;
        }
        Medium medium = new Medium();
        medium.setId(getString(mediumDict, "id"));
        String formatName = getString(mediumDict, "format");
        if (formatName != null) {
            Format format = new Format();
            format.setContent(formatName);
            medium.setFormat(format);
        }
        medium.setPosition(toBigInteger(mediumDict.get("position")));
        // convert_medium_from_track calls convert_medium with disc_list=False, so no DiscList here
        Medium.TrackList trackList = new Medium.TrackList();
        trackList.setCount(toBigInteger(mediumDict.get("track_count")));
        trackList.setOffset(toBigInteger(mediumDict.get("track_offset")));
        if (mediumDict.get("track") instanceof Map<?, ?> trackDict) {
            DefTrackData track = new DefTrackData();
            track.setId(getString(trackDict, "id"));
            track.setLength(toBigInteger(trackDict.get("length")));
            track.setNumber(getString(trackDict, "number"));
            track.setTitle(getString(trackDict, "title"));
            trackList.getDefTrack().add(track);
        }
        medium.setTrackList(trackList);

        MediumList mediumList = new MediumList();
        mediumList.getMedium().add(medium);
        mediumList.setCount(toBigInteger(releaseDict.get("medium_count")));
        mediumList.setTrackCount(toBigInteger(releaseDict.get("medium_track_count")));
        return mediumList;
    }

    private static ReleaseGroup buildReleaseGroup(Object raw) {
        if (!(raw instanceof Map<?, ?> releaseGroupDict)) {
            return null;
        }
        ReleaseGroup releaseGroup = new ReleaseGroup();
        releaseGroup.setId(getString(releaseGroupDict, "id"));
        releaseGroup.setTitle(getString(releaseGroupDict, "title"));
        releaseGroup.setPrimaryType(buildPrimaryType(releaseGroupDict));
        releaseGroup.setType(getString(releaseGroupDict, "calc_type"));
        releaseGroup.setTypeId(getString(releaseGroupDict, "calc_type_gid"));
        releaseGroup.setSecondaryTypeList(buildSecondaryTypeList(releaseGroupDict));
        releaseGroup.setDisambiguation(getString(releaseGroupDict, "disambiguation"));
        return releaseGroup;
    }
}
