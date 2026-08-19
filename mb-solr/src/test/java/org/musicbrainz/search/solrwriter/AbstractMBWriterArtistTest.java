package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterArtistTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<String>(Arrays.asList(new String[]{
				"mbid", "9b58672a-e68e-4972-956e-a8985a165a1f",
				"artist", "Howard Shore",
				"sortname", "Shore, Howard",
				"type", "Person",
				"gender_name", "Male",
				"gender_gid", "36d3d30a-839d-3eda-8cb3-29be4384e4a9",
				"country", "CA",
				"area_gid", "71bbafaa-e825-3e15-8ca9-017dcad1748b",
				"area_name", "Canada",
				"beginarea_gid", "74b24e62-d2fe-42d2-9d96-31f2da756c77",
				"beginarea_name", "Toronto",
				"comment", "Canadian score composer",
				"begin", "1946-10-18",
				"ended", "false",
				"alias_json", "{\"name\":\"Shore\",\"sort_name\":\"Shore\"}",
				"alias_json", "{\"name\":\"Howard Shaw\",\"sort_name\":\"Howard Shaw\"}",
				"alias_json", "{\"name\":\"H. Shore\",\"sort_name\":\"H. Shore\"}",
				"tag", "lord of the rings", "tag_count", "1",
				"tag", "classical", "tag_count", "2",
				"tag", "canadian", "tag_count", "2",
				"tag", "film composer", "tag_count", "1",
				"tag", "score", "tag_count", "1",
				"tag", "academy award winner", "tag_count", "1",
				"tag", "easy listening soundtracks and musicals", "tag_count", "1",
				"tag", "soundtrack", "tag_count", "2",
				"tag", "howard", "tag_count", "1",
				"tag", "shore", "tag_count", "1"}));
	}
}
