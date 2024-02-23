package org.musicbrainz.search.solrwriter.moxy;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public abstract class NotUnmarshallableXmlAdapter<T1, T2> extends XmlAdapter<T1, T2> {
    /*
    Not used in Search Server
     */
    @Override
    public T2 unmarshal(T1 t) throws Exception {
        throw new UnsupportedOperationException("Unmarshalling json back to model is not supported");
    }
}
