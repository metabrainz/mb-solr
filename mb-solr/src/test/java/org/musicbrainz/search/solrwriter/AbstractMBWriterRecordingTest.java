package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterRecordingTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"mbid", "3e0bdde6-74e0-46d4-855a-4493589fd6b2",
				"recording", "Roots and Beginnings",
				"arid", "9b58672a-e68e-4972-956e-a8985a165a1f", "artist", "Howard Shore",
				"artistname", "Howard Shore", "creditname", "Howard Shore",
				"firstreleasedate", "2007-11-05",
				"dur", "391000", "video", "false", "isrc", "USRE10701410",
				"artist_credit_json", "{\"id\":\"aca3f13c-7e5d-3f4c-8cf8-97a4cd2cf9d4\",\"name_credits\":[{\"artist\":{\"id\":\"9b58672a-e68e-4972-956e-a8985a165a1f\",\"name\":\"Howard Shore\",\"sort_name\":\"Shore, Howard\",\"aliases\":[{\"name\":\"Howard Shaw\",\"sort_name\":\"Howard Shaw\"},{\"name\":\"H. Shore\",\"sort_name\":\"H. Shore\"},{\"name\":\"Shore\",\"sort_name\":\"Shore\"}]}}]}",
				"release_json", "{\"id\":\"e7fe61ca-65b8-4abb-8723-1e7c1a5e0a49\",\"title\":\"The Lord of the Rings: The Return of the King: The Complete Recordings\",\"status\":\"Official\",\"status_id\":\"4e304316-386d-3409-af2e-78857eec5cfe\",\"release_events\":[{\"date\":\"2007-11-20\",\"area\":{\"id\":\"489ce91b-6658-3307-9877-795b68554c98\",\"name\":\"United States\",\"iso_3166_1_codes\":[\"US\"]}}],\"release_group\":{\"id\":\"f5fd971c-a1a2-33da-95b0-473e0176df83\",\"primarytype\":\"Album\",\"primarytype_gid\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"calc_type\":\"Soundtrack\",\"secondarytype\":[\"Soundtrack\",\"Live\"],\"secondarytype_gid\":[\"22a628ad-c082-3c4f-b1b6-d41665107b88\",\"6fd474e2-6b58-3102-9d17-d6f7eb7da0a0\"]},\"medium_track_count\":106,\"medium\":{\"id\":\"ac404674-891f-4e01-8d28-d77b43690d2a\",\"position\":1,\"format\":\"CD\",\"track_count\":15,\"track_offset\":0,\"track\":{\"id\":\"fc51bf50-c329-3d93-bb1b-e4f55cf00db8\",\"number\":\"1\",\"title\":\"Roots and Beginnings\",\"length\":391000}}}",
				"release_json", "{\"id\":\"e7fe61ca-65b8-4abb-8723-1e7c1a5e0a49\",\"title\":\"The Lord of the Rings: The Return of the King: The Complete Recordings\",\"status\":\"Official\",\"status_id\":\"4e304316-386d-3409-af2e-78857eec5cfe\",\"release_events\":[{\"date\":\"2007-11-20\",\"area\":{\"id\":\"489ce91b-6658-3307-9877-795b68554c98\",\"name\":\"United States\",\"iso_3166_1_codes\":[\"US\"]}}],\"release_group\":{\"id\":\"f5fd971c-a1a2-33da-95b0-473e0176df83\",\"primarytype\":\"Album\",\"primarytype_gid\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"calc_type\":\"Soundtrack\",\"secondarytype\":[\"Soundtrack\",\"Live\"],\"secondarytype_gid\":[\"22a628ad-c082-3c4f-b1b6-d41665107b88\",\"6fd474e2-6b58-3102-9d17-d6f7eb7da0a0\"]},\"medium_track_count\":106,\"medium\":{\"id\":\"01b9d64d-1d6d-4494-af8e-830ecf9f737d\",\"position\":5,\"format\":\"DVD\",\"track_count\":53,\"track_offset\":0,\"track\":{\"id\":\"af77973d-3ca5-38e3-89ff-c742c94f59af\",\"number\":\"1\",\"title\":\"Roots and Beginnings\",\"length\":391000}}}",
				"tag", "soundtrack", "tag_count", "2"
		}));
	}
}
