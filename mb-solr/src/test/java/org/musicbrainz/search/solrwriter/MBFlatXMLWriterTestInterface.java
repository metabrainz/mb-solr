package org.musicbrainz.search.solrwriter;

/**
 * Test contract for the flat-field response writer.
 *
 * <p>The XML golden files are still useful as the wire-format expectation,
 * but the input documents for these tests must contain only the flat Solr
 * fields.  In particular, they must not be assembled by copying an entity
 * XML document into {@code _store}.</p>
 */
public interface MBFlatXMLWriterTestInterface extends MBXMLWriterTestInterface {
    @Override
    default String getWritername() {
        return "flatxml";
    }
}
