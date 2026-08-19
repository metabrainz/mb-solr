package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterSeriesTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"mbid", "d271a712-90a9-43fb-9470-3151ebb9389a", "series", "The World Roots Music Library",
				"type", "Release", "comment", "2008",
				"alias_json", "{\"name\":\"ザ・ワールド・ルーツ・ミュージック・ライブラリー\",\"sort_name\":\"ザ・ワールド・ルーツ・ミュージック・ライブラリー\",\"locale\":\"ja\",\"type\":\"Series name\",\"primary\":\"primary\"}",
				"alias_json", "{\"name\":\"The World Roots Music Library\",\"sort_name\":\"World Roots Music Library, The\",\"locale\":\"en\",\"type\":\"Series name\",\"primary\":\"primary\"}"}));
	}
}
