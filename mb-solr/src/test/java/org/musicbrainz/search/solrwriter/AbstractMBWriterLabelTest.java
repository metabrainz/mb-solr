package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterLabelTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"mbid", "5a584032-dcef-41bb-9f8b-19540116fb1c", "laid", "5a584032-dcef-41bb-9f8b-19540116fb1c",
				"label", "Deutsche Grammophon", "type", "Original Production", "code", "173", "country", "DE",
				"area_gid", "85752fda-13c4-31a3-bee5-0e5cb1f51dad", "area_name", "Germany", "begin", "1898", "ended", "false",
				"alias_json", "{\"name\":\"Deutsche Grammaphon\",\"sort_name\":\"Deutsche Grammaphon\"}",
				"alias_json", "{\"name\":\"Deutsche Grammophone\",\"sort_name\":\"Deutsche Grammophone\"}",
				"alias_json", "{\"name\":\"Deutsche Grammophon Gesellschaft\",\"sort_name\":\"Deutsche Grammophon Gesellschaft\"}",
				"alias_json", "{\"name\":\"Deutsche Grammophon GmbH\",\"sort_name\":\"Deutsche Grammophon GmbH\"}",
				"alias_json", "{\"name\":\"Deutsche Grammophon (Universal)\",\"sort_name\":\"Deutsche Grammophon (Universal)\"}",
				"alias_json", "{\"name\":\"Deutsche Gramophon\",\"sort_name\":\"Deutsche Gramophon\"}",
				"alias_json", "{\"name\":\"DG\",\"sort_name\":\"DG\"}",
				"tag", "classical", "tag_count", "2", "tag", "tomatito", "tag_count", "1",
				"tag", "deutsche grammophon beethoven", "tag_count", "1"}));
	}
}
