package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.Utils;
import org.musicbrainz.mmd2.Release;
import org.musicbrainz.mmd2.ReleaseGroup;
import org.musicbrainz.mmd2.ReleaseList;
import org.musicbrainz.mmd2.Status;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ReleaseGroupBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        ReleaseGroup releaseGroup = new ReleaseGroup();
        releaseGroup.setArtistCredit(buildArtistCredit(doc));
        releaseGroup.setReleaseList(buildReleaseList(doc));
        releaseGroup.setId(getString(doc, "mbid"));
        releaseGroup.setTitle(getString(doc, "releasegroup"));
        releaseGroup.setDisambiguation(getString(doc, "comment"));
        releaseGroup.setFirstReleaseDate(getString(doc, "firstreleasedate"));
        releaseGroup.setPrimaryType(buildPrimaryType(doc));
        releaseGroup.setType(getString(doc, "calc_type"));
        releaseGroup.setTypeId(getString(doc, "calc_type_gid"));
        releaseGroup.setSecondaryTypeList(buildSecondaryTypeList(doc));
        releaseGroup.setTagList(buildTagList(doc));
        return releaseGroup;
    }

    private static ReleaseList buildReleaseList(SolrDocument doc) {
        ReleaseList releaseList = new ReleaseList();
        int count = 0;
        Collection<Object> values = doc.getFieldValues("release_json");
        if (values != null) {
            for (Object value : values) {
                String json = Objects.toString(value, null);
                if (json == null || json.isEmpty()) {
                    continue;
                }
                if (!(Utils.fromJSONString(json) instanceof Map<?, ?> releaseDict)) {
                    continue;
                }
                Release release = new Release();
                release.setId(getString(releaseDict, "id"));
                release.setTitle(getString(releaseDict, "title"));
                String statusName = getString(releaseDict, "status");
                if (statusName != null) {
                    Status status = new Status();
                    status.setContent(statusName);
                    status.setId(getString(releaseDict, "status_id"));
                    release.setStatus(status);
                }
                releaseList.getRelease().add(release);
                count++;
            }
        }
        releaseList.setCount(BigInteger.valueOf(count));
        return releaseList;
    }

}
