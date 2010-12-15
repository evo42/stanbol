package eu.iksproject.fise.engines.opencalais.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;

import org.apache.clerezza.rdf.core.LiteralFactory;
import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.access.TcManager;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.impl.TripleImpl;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.iksproject.fise.servicesapi.ContentItem;
import eu.iksproject.fise.servicesapi.EngineException;
import eu.iksproject.fise.servicesapi.helper.EnhancementEngineHelper;
import eu.iksproject.fise.servicesapi.rdf.Properties;

/** This class provides JUnit tests for OpenCalaisEngine.
 * 
 * @author <a href="mailto:kasper@dfki.de">Walter Kasper</a>
 */

public class TestOpenCalaisEngine {

  /**
   * This contains the logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TestOpenCalaisEngine.class);

  private static OpenCalaisEngine calaisExtractor;

  private static String TEST_LICENSE_KEY = System.getProperty(OpenCalaisEngine.LICENSE_KEY);
  private static String TEST_TEXT = "Israeli PM Netanyahu pulls out of US nuclear summit\nIsraeli PM Benjamin Netanyahu has cancelled a visit to the US where he was to attend a summit on nuclear security, Israeli officials say. Mr Netanyahu made the decision after learning that Egypt and Turkey intended to raise the issue of Israel's presumed nuclear arsenal, the officials said. Mr Obama is due to host dozens of world leaders at the two-day conference, which begins in Washington on Monday. Israel has never confirmed or denied that it possesses atomic weapons. Israel's Intelligence and Atomic Energy Minister Dan Meridor will take Netanyahu's place in the nuclear summit, Israeli radio said. More than 40 countries are expected at the meeting, which will focus on preventing the spread of nuclear weapons to militant groups.";
  
  @BeforeClass
  public static void oneTimeSetup() {
    calaisExtractor = new OpenCalaisEngine();
    calaisExtractor.tcManager = TcManager.getInstance();
    if (TEST_LICENSE_KEY.matches("\\w+")) {
      calaisExtractor.setLicenseKey(TEST_LICENSE_KEY);
    }
  }

  public static ContentItem wrapAsContentItem(final String text) {
    return new ContentItem() {
      
      SimpleMGraph metadata = new SimpleMGraph();
      String id = "urn:eu.iksproject.fise:test:engines.opencalais:content-item-"+EnhancementEngineHelper.randomUUID().toString();
      public InputStream getStream() {
        return new ByteArrayInputStream(text.getBytes());
      }
      
      public String getMimeType() {
        return "text/plain";
      }
      
      public MGraph getMetadata() {
        return metadata;
      }
      
      public String getId() {
        return id;
      }
    };
  }
  @Test
  public void testEntityExtraction() {
    String testFile = "calaisresult.owl";
    String format = "application/rdf+xml";
    InputStream in =
      this.getClass().getClassLoader().getResourceAsStream(
      testFile);
    Assert.assertNotNull("failed to load resource " + testFile, in);
    MGraph model = calaisExtractor.readModel(in, format);
    Assert.assertNotNull("model reader failed with format: "+format, model);
    Collection<CalaisEntityOccurrence> entities = calaisExtractor.queryModel(model);
    LOG.info("Found entities: {}",entities.size());
    LOG.debug("Entities:\n{}",entities);
    Assert.assertFalse("No entities found!", entities.isEmpty());
  }

  @Test
  public void testCalaisConnection() {
  	Assume.assumeNotNull(TEST_LICENSE_KEY);
  	try {
  	  ContentItem ci = wrapAsContentItem(TEST_TEXT);
  	  ci.getMetadata().add(new TripleImpl(new UriRef(ci.getId()), Properties.DC_LANGUAGE, LiteralFactory.getInstance().createTypedLiteral("en")));
  		MGraph model = calaisExtractor.getCalaisAnalysis(TEST_TEXT,ci);
  		Assert.assertNotNull("No model", model);
  		Collection<CalaisEntityOccurrence> entities = calaisExtractor.queryModel(model);
  		LOG.info("Found entities: {}",entities.size());
      LOG.debug("Entities:\n{}",entities);
  		Assert.assertFalse("No entities found!", entities.isEmpty());
  	} catch (EngineException e) {
  		Assert.fail("Connection problem: "+e.getMessage());
  	}
  }
  
  // problem with license keys for testing?
  // ask user to supply it as system property or whatever?
  
}
