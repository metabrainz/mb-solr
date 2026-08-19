package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.Cdstub;

import java.math.BigInteger;
import java.util.Objects;


public class CdstubBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Cdstub cdstub = new Cdstub();
        // wscompat converter for cdstub sets artist to an empty string when the artist is null.
        cdstub.setArtist(Objects.toString(doc.get("artist"), ""));
        cdstub.setTitle(getString(doc, "title"));
        cdstub.setId(getString(doc, "discid"));
        cdstub.setTrackList(buildTrackList(doc));
        cdstub.setBarcode(getString(doc, "barcode"));
        String comment = getString(doc, "comment");
        if (comment != null && !comment.isEmpty()) {
            cdstub.setDisambiguation(comment);
        }
        return cdstub;
    }

    private Cdstub.TrackList buildTrackList(SolrDocument doc) {
        String tracks = getString(doc, "tracks");
        if (tracks == null) {
            return null;
        }
        Cdstub.TrackList trackList = new Cdstub.TrackList();
        trackList.setCount(new BigInteger(tracks));
        return trackList;
    }
}
