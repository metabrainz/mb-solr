package org.musicbrainz.search.solrwriter.entityBuilders;

import org.apache.solr.common.SolrDocument;
import org.musicbrainz.mmd2.Coordinates;
import org.musicbrainz.mmd2.Place;

public class PlaceBuilder extends EntityBuilderBase {
    @Override
    public Object build(SolrDocument doc) {
        Place place = new Place();
        place.setId(getString(doc, "mbid"));
        place.setName(getString(doc, "place"));
        place.setAddress(getString(doc, "address"));
        place.setAliasList(buildAliasList(doc));
        place.setArea(buildArea(doc, "area"));
        place.setDisambiguation(getString(doc, "comment"));
        place.setCoordinates(buildCoordinates(doc));
        place.setLifeSpan(buildLifeSpan(doc.get("begin"), doc.get("end"), doc.get("ended")));
        place.setType(getString(doc, "type"));
        place.setTypeId(getString(doc, "type_gid"));
        return place;
    }

    private static Coordinates buildCoordinates(SolrDocument doc) {
        String latitude = getString(doc, "lat");
        String longitude = getString(doc, "long");
        if (latitude == null || longitude == null) {
            return null;
        }
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(latitude);
        coordinates.setLongitude(longitude);
        return coordinates;
    }
}
