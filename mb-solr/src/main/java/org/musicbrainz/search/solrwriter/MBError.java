package org.musicbrainz.search.solrwriter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "error")
@XmlType(propOrder = {"code", "message"})
public class MBError {
    /**
     * Custom XML bindings for JAXB to produce meaningful
     * errors compatible with MB SolrQueryWriters in XML and
     * JSON formats. It wraps SOLR Exceptions to a class that can
     * be marshalled.
     */

    private int code;
    private String message;

    @XmlElement(name = "code")
    /**
     * HTTP Error code returned by SOLR.
     */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @XmlElement(name = "message")
    /**
     * Error string extracted from the SOLR exception
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
