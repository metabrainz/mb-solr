package org.musicbrainz.search.solrwriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
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
	private Unmarshaller unmarshaller = null;
	protected Marshaller marshaller = null;
	private ObjectFactory objectfactory = null;

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
			throw new IllegalArgumentException(entityType
					+ "is not a valid entity type");
		}
	}

	/**
	 * MetadataListWrapper wraps common functionality in dealing with
	 * {@link Metadata}. This includes generating the appropriate list object
	 * for different entity types, as well as calling the appropriate method to
	 * set the list on the {@link Metadata} object.
	 */
	class MetadataListWrapper {
		private List objList = null;
		private Object MMDList = null;
		private Metadata metadata = null;
		private Method setCountMethod = null;
		private Method setOffsetMethod = null;

		public MetadataListWrapper() {
			switch (entityType) {
			case work:
				MMDList = objectfactory.createWorkList();
				objList = ((WorkList) MMDList).getWork();
				break;
			default:
				// This should never happen because MBXMLWriters init method
				// aborts earlier
				throw new IllegalArgumentException("invalid entity type: "
						+ entityType);
			}

			this.metadata = objectfactory.createMetadata();
			try {
				setCountMethod = MMDList.getClass().getMethod("setCount",
						BigInteger.class);
				setOffsetMethod = MMDList.getClass().getMethod("setOffset",
						BigInteger.class);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
		}

		public List getLiveList() {
			return objList;
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

		public void setCountAndOffset(int count, int offset) {
			try {
				setCountMethod.invoke(MMDList, BigInteger.valueOf(count));
				setOffsetMethod.invoke(MMDList, BigInteger.valueOf(offset));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public String getContentType(SolrQueryRequest arg0, SolrQueryResponse arg1) {
		return CONTENT_TYPE_XML_UTF8;
	}

	private JAXBContext createJAXBContext() throws JAXBException {
		return JAXBContext
				.newInstance(org.musicbrainz.mmd2.ObjectFactory.class);
	}

	protected Marshaller createMarshaller() throws JAXBException {
		return context.createMarshaller();
	}

	public void init(NamedList initArgs) {
		try {
			context = createJAXBContext();
			marshaller = createMarshaller();
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
		MetadataListWrapper metadatalistwrapper = new MetadataListWrapper();

		NamedList vals = res.getValues();

		ResultContext con = (ResultContext) vals.get("response");

		metadatalistwrapper.setCountAndOffset(con.docs.matches(),
				con.docs.offset());

		List xmlList = metadatalistwrapper.getLiveList();

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
			Object unmarshalledObj = null;
			try {
				unmarshalledObj = unmarshaller
						.unmarshal(new ByteArrayInputStream(store.getBytes()));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			// TODO: this needs "score" in the field list of Solr, otherwise
			// this causes a NullPointerException
			adjustScore(maxScore, unmarshalledObj, iter.score());

			xmlList.add(unmarshalledObj);
		}

		doWrite(writer, metadatalistwrapper);
	}

	protected void doWrite(Writer writer, MetadataListWrapper mlwrapper)
			throws IOException {
		StringWriter sw = new StringWriter();
		try {
			marshaller.marshal(mlwrapper.getCompletedMetadata(), sw);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		writer.write(sw.toString());
	}
}
