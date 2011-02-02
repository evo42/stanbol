package org.apache.stanbol.entityhub.yard.clerezza.impl;

import junit.framework.Assert;

import org.apache.clerezza.rdf.ontologies.RDF;
import org.apache.stanbol.entityhub.core.yard.SimpleYardConfig;
import org.apache.stanbol.entityhub.core.yard.AbstractYard.YardConfig;
import org.apache.stanbol.entityhub.servicesapi.model.Reference;
import org.apache.stanbol.entityhub.servicesapi.model.Representation;
import org.apache.stanbol.entityhub.servicesapi.model.ValueFactory;
import org.apache.stanbol.entityhub.servicesapi.model.rdf.RdfResourceEnum;
import org.apache.stanbol.entityhub.servicesapi.yard.Yard;
import org.apache.stanbol.entityhub.servicesapi.yard.YardException;
import org.apache.stanbol.entityhub.test.yard.YardTest;
import org.junit.Before;
import org.junit.Test;

public class ClerezzaYardTest extends YardTest {
    
    private Yard yard;
    
    @Before
    public final void initYard(){
        YardConfig config = new SimpleYardConfig("urn:yard.clerezza:testYardId");
        config.setName("Clerezza Yard Test");
        config.setDescription("The Clerezza Yard instance used to execute the Unit Tests defined for the Yard Interface");
        yard = new ClerezzaYard(config);
    }
    
    @Override
    protected Yard getYard() {
        return yard;
    }
    
    /**
     * The Clerezza Yard uses the Statement<br>
     * <code>representationId -> rdf:type -> Representation</code><br>
     * to identify that an UriRef in the RDF graph (MGraph) represents a
     * Representation. This Triple is added when a Representation is stored and
     * removed if retrieved from the Yard.<p>
     * This tests if this functions as expected
     * @throws YardException
     */
    @Test
    public void testRemovalOfTypeRepresentationStatement() throws YardException {
        Yard yard = getYard();
        ValueFactory vf = yard.getValueFactory();
        Reference representationType = vf.createReference(RdfResourceEnum.Representation.getUri());
        Representation test = create();
        //the rdf:type Representation MUST NOT be within the Representation
        Assert.assertFalse(test.get(RDF.type.getUnicodeString()).hasNext());
        //now add the statement and see if an IllegalStateException is thrown
        /*
         * The triple within this Statement is internally used to "mark" the
         * URI of the Representation as 
         */
        test.add(RDF.type.getUnicodeString(), representationType);
    }
    
}
