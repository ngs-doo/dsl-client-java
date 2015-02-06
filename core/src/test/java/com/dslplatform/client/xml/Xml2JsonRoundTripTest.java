package com.dslplatform.client.xml;

import static com.dslplatform.client.xml.XMLHelpers.*;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import com.dslplatform.client.TestLogging;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.skyscreamer.jsonassert.JSONAssert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@RunWith(Parameterized.class)
public class Xml2JsonRoundTripTest extends TestLogging {
	private final File sourceFile;

	public Xml2JsonRoundTripTest(final String unused, final File sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Parameterized.Parameters(name = "Testing on file: {0}")
	public static List<Object[]> sourceFiles() throws URISyntaxException {
		final List<Object[]> sourceFilesList = new ArrayList<Object[]>();
		for (final File f : getFileForResource("/roundtripTests/xml/source/").listFiles()) {
			if (f.isFile()) sourceFilesList.add(new Object[] { f.getName(), f });
		}
		return sourceFilesList;
	}

	/**
	 * For each of the XML files in 'resources/roundtripTests/source/xml'
	 * generates the entire roundtrip conversion (xml -> json -> xml) using
	 * io.jvm.json.serializers.XmlJsonSerializer, and asserts the generated XML
	 * equivalence with the reference conversions (obtained by using Json.NET)
	 */
	@Test
	public void assertRoundTripEquivalenceWithReferenceConversion() throws URISyntaxException, JSONException,
			SAXException, IOException, ParserConfigurationException, TransformerConfigurationException,
			TransformerException, TransformerFactoryConfigurationError {

		final File xmlSourceFile = sourceFile;

		debug("+----------------------------------------------------");
		debug("| Testing filename: " + xmlSourceFile.getName());
		debug("+----------------------------------------------------");

		/* Filename initialisation */
		final String sourceFilename_xml = xmlSourceFile.getName();
		final String convertedFilename_json = sourceFilename_xml + ".json";
		final String roundtripFilename_xml = sourceFilename_xml + ".json.xml";

		final File referenceFile_json = getFileForResource("/roundtripTests/xml/reference/" + convertedFilename_json);
		assertTrue("The reference JSON file does not exist for: " + sourceFilename_xml, referenceFile_json != null
				&& referenceFile_json.exists());

		final File referenceRoundtripFile_xml =
				getFileForResource("/roundtripTests/xml/reference/" + roundtripFilename_xml);
		assertTrue(
				"The reference XML->JSON roundtrip file does not exist for: " + sourceFilename_xml,
				referenceRoundtripFile_xml != null && referenceRoundtripFile_xml.exists());

		/* Parse input files */
		final Document source_xml = parseXmlFile(xmlSourceFile);
		final String referenceJson = stringFromFile(referenceFile_json);
		final Document referenceRoundTrip_xml = parseXmlFile(referenceRoundtripFile_xml);

		/* Convert to json and compare with reference conversion */
		final String convertedJson = jsonStringFromXml(source_xml);
		assertJsonEquivalence(convertedJson, referenceJson);

		/* Convert back to XML, and compare with reference documents */
		final Document roundtripXmlDocument = xmlDocumentFromJson(convertedJson);

		debug("Our roundtrip XML:");
		debug(roundtripXmlDocument);
		debug("Reference roundtrip xml:");
		debug(referenceRoundTrip_xml);

		assertXmlEquivalence(
				"The reference roundtrip XML does not match the converted roundtrip XML",
				roundtripXmlDocument,
				referenceRoundTrip_xml);
		assertXmlEquivalence("The roundtrip XML does not match the source XML", roundtripXmlDocument, source_xml);
		assertXmlEquivalence(
				"The reference roundtrip XML does not match the source XML",
				referenceRoundTrip_xml,
				source_xml);
	}

	private static void assertJsonEquivalence(final String lhs, final String rhs) throws JSONException {
		JSONAssert.assertEquals(lhs, rhs, true);
	}

	private static void assertXmlEquivalence(final String message, final Document lhs, final Document rhs) {
		final XmlBruteForceComparator comparator = new XmlBruteForceComparator();
		assertTrue(message, comparator.compare(lhs.getDocumentElement(), rhs.getDocumentElement()) == 0);
	}
}
