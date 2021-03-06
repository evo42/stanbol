package eu.iksproject.kres.api.manager.ontology;

import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * An ontology scope for application use. There exists exactly one scope for
 * each live (active or halted) KReS session. <br>
 * <br>
 * This is the only type of ontology scope that allows public access to its OWL
 * ontology manager.
 * 
 * @author alessandro
 * 
 */
public interface SessionOntologySpace extends OntologySpace {

	/**
	 * Returns the OWL ontology manager associated to this scope.
	 * 
	 * @return the associated ontology manager
	 */
	public OWLOntologyManager getOntologyManager();

}
