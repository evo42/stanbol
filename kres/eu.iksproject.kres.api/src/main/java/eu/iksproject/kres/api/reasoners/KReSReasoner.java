package eu.iksproject.kres.api.reasoners;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;


/**
 * The KReS Reasoner provides all the reasoning services to the KReS.
 * 
 * 
 * @author andrea.nuzzolese
 *
 */
public interface KReSReasoner {
	
	/**
	 * Gets the reasoner.
	 * 
	 * @param ontology {@link OWLOntology}
	 * @return the reasoner {@link OWLReasoner}.
	 */
	public OWLReasoner getReasoner(OWLOntology ontology);
	
	/**
	 * Runs a consistency check on the ontology.
	 * 
	 * @param owlReasoner {@link OWLReasoner}
	 * @return true if the ontology is consistent, false otherwise.
	 */
	public boolean consistencyCheck(OWLReasoner owlReasoner);
	
	
	/**
	 * Launch the reasoning on a set of rules applied to a gien ontology.
	 * @param ontology
	 * @param ruleOntology
	 * @return the inferred ontology
	 */
	public OWLOntology runRules(OWLOntology ontology, OWLOntology ruleOntology);

}
