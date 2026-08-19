package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterAreaTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"area", "Thüringen", "mbid", "ff2ee1ad-febe-4b48-8999-e77870b62744",
				"type", "Subdivision", "iso2", "DE-TH", "ended", "false",
				"alias_json", "{\"name\":\"Thüringen\",\"sort_name\":\"Thüringen\",\"locale\":\"de\",\"type\":\"Area name\",\"primary\":\"primary\"}",
				"alias_json", "{\"name\":\"Thuringia\",\"sort_name\":\"Thuringia\",\"locale\":\"en\",\"type\":\"Area name\",\"primary\":\"primary\"}",
				"rel_json", "{\"type\":\"part of\",\"type_id\":\"de7cc874-8b1b-3a05-8272-f3834c968fb7\",\"direction\":\"backward\",\"target\":\"85752fda-13c4-31a3-bee5-0e5cb1f51dad\",\"area\":{\"id\":\"85752fda-13c4-31a3-bee5-0e5cb1f51dad\",\"name\":\"Germany\",\"type\":\"Country\"}}"}));
	}
}
