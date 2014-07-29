package org.musicbrainz.search.solrwriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.lucene.document.Document;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.*;
import org.apache.solr.search.DocIterator;
import org.musicbrainz.mmd2.*;

public class MBXMLWriter implements QueryResponseWriter {

	/**
	 * The context used to (un-)serialize XML
	 */
	private JAXBContext context = null;
	private Marshaller marshaller = null;
	private Unmarshaller unmarshaller = null;
	private ObjectFactory objectfactory = null;
	private MetadataListWrapper metadatalistwrapper = null;

	/**
	 * The entity type of this MBXMLWriter
	 */
	private entityTypes entityType = null;

	private enum entityTypes {
		artist, area, label, recording, release, release_group, work;

		public static entityTypes getType(String entityType) {
			for (entityTypes et : entityTypes.values()) {
				if (et.name().equalsIgnoreCase(entityType)) {
					return et;
				}
			}
			throw new IllegalArgumentException(entityType + "is not a valid entity type");
		}
	}

	/**
	 * MetadataListWrapper wraps common functionality in dealing with
	 * {@link Metadata}. This includes generating the appropriate list object
	 * for different entity types, as well as calling the appropriate method to
	 * set the list on the {@link Metadata} object.
	 */
	private class MetadataListWrapper {
		/**
		 * The class of object that's kept in the list
		 */
		private Object MMDList = null;
		private Metadata metadata = null;

		public MetadataListWrapper() {
			switch (entityType) {
			case work:
				MMDList = objectfactory.createWorkList();
				break;
			default:
				// This should never happen because MBXMLWriters init method
				// aborts earlier
				throw new IllegalArgumentException("invalid entity type: "
						+ entityType);
			}

			this.metadata = objectfactory.createMetadata();
		}

		public List getLiveList() {
			switch (entityType) {
			case work:
				return ((WorkList) MMDList).getWork();
			default:
				// This should never happen because MBXMLWriters init method
				// aborts earlier
				throw new IllegalArgumentException("invalid entity type: "
						+ entityType);
			}
		}

		public Metadata getCompletedMetadata() {
			switch (entityType) {
			case work:
				metadata.setWorkList((WorkList) MMDList);
				break;
			default:
				// This should never happen because MBXMLWriters init method
				// aborts earlier
				throw new IllegalArgumentException("invalid entity type: "
						+ entityType);
			}
			return metadata;
		}
	}

	public String getContentType(SolrQueryRequest arg0, SolrQueryResponse arg1) {
		return CONTENT_TYPE_XML_UTF8;
	}

	public void init(NamedList initArgs) {
		try {
			context = JAXBContext
					.newInstance(org.musicbrainz.mmd2.ObjectFactory.class);
			marshaller = context.createMarshaller();
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

		/*
		 * Check for which entity type we're generating responses
		 */
		objectfactory = new ObjectFactory();
		Object entityTypeTemp = initArgs.get("entitytype");

		if (entityTypeTemp == null) {
			throw new RuntimeException("no entitytype given");
		} else {
			entityType = entityTypes.getType((String) entityTypeTemp);
		}

	}

	private static void adjustScore(float maxScore, Object object,
			float objectScore) {
		Method setScoreMethod = null;
		try {
			setScoreMethod = object.getClass().getMethod("setScore",
					String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(
					"Expected an object with a setScore method");
		}

		int adjustedScore = (((int) (((objectScore / maxScore)) * 100)));

		try {
			setScoreMethod.invoke(object, Integer.toString(adjustedScore));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void write(Writer writer, SolrQueryRequest req, SolrQueryResponse res)
			throws IOException {
		NamedList vals = res.getValues();

		org.apache.solr.response.ResultContext con = (ResultContext) vals
				.get("response");

		List xmlList = this.metadatalistwrapper.getLiveList();

		float maxScore = con.docs.maxScore();
		DocIterator iter = con.docs.iterator();

		while (iter.hasNext()) {
			Integer id = iter.nextDoc();
			Document doc = req.getSearcher().doc(id);
			String store = doc.getField("_store").stringValue();
			if (store == null) {
				throw new RuntimeException(
						"_store should be a string value but wasn't");
			}
			Work w = null;
			try {
				w = (Work) unmarshaller.unmarshal(new ByteArrayInputStream(
						store.getBytes()));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			// TODO: this needs "score" in the field list of Solr, otherwise
			// this causes a NullPointerException
			adjustScore(maxScore, w, iter.score());

			xmlList.add(w);
		}


		StringWriter sw = new StringWriter();
		try {
			marshaller.marshal(this.metadatalistwrapper.getCompletedMetadata(), sw);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		writer.write(sw.toString());
	}
}
