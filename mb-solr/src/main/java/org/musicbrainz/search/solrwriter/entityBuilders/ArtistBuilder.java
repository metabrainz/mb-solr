package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.*;


public class ArtistBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Artist artist = new Artist();
        artist.setId(getString(doc, "mbid"));
        artist.setName(getString(doc, "artist"));
        artist.setSortName(getString(doc, "sortname"));
        artist.setDisambiguation(getString(doc, "comment"));
        artist.setGender(buildGender(doc));
        artist.setType(getString(doc, "type"));
        artist.setTypeId(getString(doc, "type_gid"));
        artist.setBeginArea(buildArea(doc, "beginarea"));
        artist.setArea(buildArea(doc, "area"));
        artist.setCountry(getString(doc, "country"));
        artist.setEndArea(buildArea(doc, "endarea"));
        artist.setLifeSpan(buildLifeSpan(doc.get("begin"), doc.get("end"), doc.get("ended")));
        artist.setAliasList(buildAliasList(doc));
        artist.setIpiList(buildIpiList(doc));
        artist.setIsniList(buildIsniList(doc));
        artist.setTagList(buildTagList(doc));
        return artist;
    }

    private Gender buildGender(SolrDocument doc) {
        String name = getString(doc, "gender_name");
        if (name == null) {
            return null;
        }
        Gender gender = new Gender();
        gender.setId(getString(doc, "gender_gid"));
        gender.setContent(name);
        return gender;
    }
}
