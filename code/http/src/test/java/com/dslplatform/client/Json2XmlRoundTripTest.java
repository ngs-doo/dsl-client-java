package com.dslplatform.client;

import static com.dslplatform.client.Helpers.getFileForResource;
import static com.dslplatform.client.Helpers.jsonStringFromXml;
import static com.dslplatform.client.Helpers.parseXmlFile;
import static com.dslplatform.client.Helpers.stringFromFile;
import static com.dslplatform.client.Helpers.xmlDocumentFromJson;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@RunWith(Parameterized.class)
public class Json2XmlRoundTripTest {

  File sourceFile;

  public Json2XmlRoundTripTest(String filename, File sourceFile) {
    this.sourceFile = sourceFile;
  }

  @Parameterized.Parameters(name="Testing on file: {0}")
  public static Collection sourceFiles() throws URISyntaxException {
    File[] sourceFilesArray = getFileForResource("/roundtripTests/json/source/").listFiles();

    List<Object[]> sourceFilesList = new ArrayList<Object[]>();
    for (File f : sourceFilesArray)
      if (f.isFile())
        sourceFilesList.add(new Object[] { f.getName(), f });

    return sourceFilesList;
  }

  @Test
  public void assertRoundTripEquivalenceWithReferenceConversion() throws URISyntaxException, JSONException,
      SAXException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException,
      TransformerFactoryConfigurationError {

    File jsonSourceFile = this.sourceFile;
    System.out.println("Testiramo za datoteku: " + jsonSourceFile.getName());

    /* Filename initialisation */
    final String sourceFilename_json = jsonSourceFile.getName();
    final String convertedFilename_xml = sourceFilename_json + ".xml";
    final String roundtripFilename_json = sourceFilename_json + ".xml.json";

    final File referenceFile_xml = getFileForResource("/roundtripTests/json/reference/" + convertedFilename_xml);
    assertTrue("The reference JSON file does not exist for: " + sourceFilename_json,
        (referenceFile_xml != null && referenceFile_xml.exists()));

    final File referenceRoundtripFile_json = getFileForResource("/roundtripTests/json/reference/"
        + roundtripFilename_json);
    assertTrue("The reference XML->JSON roundtrip file does not exist for: " + sourceFilename_json,
        (referenceRoundtripFile_json != null && referenceRoundtripFile_json.exists()));

    /* Parse input files */
    final String source_json = stringFromFile(jsonSourceFile);
    final Document referenceXml = parseXmlFile(referenceFile_xml);
    final String referenceRoundTrip_json = stringFromFile(referenceRoundtripFile_json);

    /* Convert to XML and compare with reference conversion */
    final Document convertedXml = xmlDocumentFromJson(source_json);
    assertXmlEquivalence("The converted XML does not match the reference XML", convertedXml, referenceXml);

    /* Convert back to Json, and compare with reference documents */
    final String roundtripJsonString = jsonStringFromXml(convertedXml);

    assertJsonEquivalence(roundtripJsonString, referenceRoundTrip_json);
    assertJsonEquivalence(roundtripJsonString, source_json);
    assertJsonEquivalence(referenceRoundTrip_json, source_json);
  }

  private static void assertJsonEquivalence(String lhs, String rhs) throws JSONException {
    System.out.println();
    System.out.println(lhs);
    System.out.println(rhs);
    System.out.println();
    JSONAssert.assertEquals(lhs, rhs, true);
  }

  private static void assertXmlEquivalence(String message, Document lhs, Document rhs) {
    XmlBruteForceComparator comparator = new XmlBruteForceComparator();

    assertTrue(message, comparator.compare(lhs.getDocumentElement(), rhs.getDocumentElement()) == 0);
  }

}
