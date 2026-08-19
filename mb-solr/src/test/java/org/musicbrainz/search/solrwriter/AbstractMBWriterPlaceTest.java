package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterPlaceTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"mbid", "fd60135e-d173-4a56-87fd-1f1c4bc62bd1", "place", "Ilmbühne", "type", "Venue",
				"lat", "50.99508", "long", "11.36062", "area_gid", "c7644e45-dec4-43fd-aad6-35036b8e911d", "area_name", "Weimar",
				"alias_json", "{\"name\":\"Lindenrondell\",\"sort_name\":\"Lindenrondell\",\"type\":\"Search hint\"}"}));
	}
}
