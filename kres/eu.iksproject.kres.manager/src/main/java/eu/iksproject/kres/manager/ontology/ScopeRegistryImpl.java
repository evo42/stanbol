package eu.iksproject.kres.manager.ontology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;

import eu.iksproject.kres.api.manager.ontology.NoSuchScopeException;
import eu.iksproject.kres.api.manager.ontology.OntologyScope;
import eu.iksproject.kres.api.manager.ontology.ScopeEventListener;
import eu.iksproject.kres.api.manager.ontology.ScopeRegistry;

/**
 * Default implementation of an ontology scope registry.
 * 
 * @author alessandro
 * 
 */
public class ScopeRegistryImpl implements ScopeRegistry {

	private Set<IRI> activeScopeIRIs;

	private Set<ScopeEventListener> scopeListeners;

	private Map<IRI, OntologyScope> scopeMap;

	public ScopeRegistryImpl() {
		scopeMap = new HashMap<IRI, OntologyScope>();
		activeScopeIRIs = new HashSet<IRI>();
		scopeListeners = new HashSet<ScopeEventListener>();
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#addScopeRegistrationListener(eu.iksproject.kres.api.manager.ontology.ScopeEventListener)
	 */
	@Override
	public void addScopeRegistrationListener(ScopeEventListener listener) {
		scopeListeners.add(listener);

	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#clearScopeRegistrationListeners()
	 */
	@Override
	public void clearScopeRegistrationListeners() {
		scopeListeners.clear();

	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#containsScope(org.semanticweb.owlapi.model.IRI)
	 */
	@Override
	public boolean containsScope(IRI scopeID) {
		// containsKey() is not reliable enough
		return scopeMap.get(scopeID) != null;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#deregisterScope(eu.iksproject.kres.api.manager.ontology.OntologyScope)
	 */
	@Override
	public synchronized void deregisterScope(OntologyScope scope) {
		IRI id = scope.getID();
		if (!containsScope(id))
			throw new NoSuchScopeException(id);
		// For sure it is deactivated...
		setScopeActive(id, false);
//		activeScopeIRIs.remove(id);
		scopeMap.remove(id);
		fireScopeDeregistered(scope);
	}

	protected void fireScopeActivationChange(IRI scopeID, boolean activated) {
		OntologyScope scope = scopeMap.get(scopeID);
		if (activated)
			for (ScopeEventListener l : scopeListeners)
				l.scopeActivated(scope);
		else
			for (ScopeEventListener l : scopeListeners)
				l.scopeDeactivated(scope);
	}

	/**
	 * Notifies all registered scope listeners that an ontology scope has been
	 * removed.
	 * 
	 * @param scope
	 *            the scope that was removed.
	 */
	protected void fireScopeDeregistered(OntologyScope scope) {
		for (ScopeEventListener l : scopeListeners)
			l.scopeDeregistered(scope);
	}

	/**
	 * Notifies all registered scope listeners that an ontology scope has been
	 * added.
	 * 
	 * @param scope
	 *            the scope that was added.
	 */
	protected void fireScopeRegistered(OntologyScope scope) {
		for (ScopeEventListener l : scopeListeners)
			l.scopeRegistered(scope);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#getActiveScopes()
	 */
	@Override
	public Set<OntologyScope> getActiveScopes() {
		Set<OntologyScope> scopes = new HashSet<OntologyScope>();
		for (IRI id : activeScopeIRIs)
			scopes.add(scopeMap.get(id));
		return scopes;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#getRegisteredScopes()
	 */
	@Override
	public synchronized Set<OntologyScope> getRegisteredScopes() {
		return new HashSet<OntologyScope>(scopeMap.values());
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#getScope(org.semanticweb.owlapi.model.IRI)
	 */
	@Override
	public OntologyScope getScope(IRI scopeID) {
		return scopeMap.get(scopeID);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#getScopeRegistrationListeners()
	 */
	@Override
	public Set<ScopeEventListener> getScopeRegistrationListeners() {
		return scopeListeners;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#isScopeActive(org.semanticweb.owlapi.model.IRI)
	 */
	@Override
	public boolean isScopeActive(IRI scopeID) {
		if (!containsScope(scopeID))
			throw new NoSuchScopeException(scopeID);
		return activeScopeIRIs.contains(scopeID);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#registerScope(eu.iksproject.kres.api.manager.ontology.OntologyScope)
	 */
	@Override
	public synchronized void registerScope(OntologyScope scope) {
		registerScope(scope, false);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#registerScope(eu.iksproject.kres.api.manager.ontology.OntologyScope, boolean)
	 */
	@Override
	public synchronized void registerScope(OntologyScope scope, boolean activate) {
		scopeMap.put(scope.getID(), scope);
		setScopeActive(scope.getID(), activate);
		fireScopeRegistered(scope);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#removeScopeRegistrationListener(eu.iksproject.kres.api.manager.ontology.ScopeEventListener)
	 */
	@Override
	public void removeScopeRegistrationListener(ScopeEventListener listener) {
		scopeListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.iksproject.kres.api.manager.ontology.ScopeRegistry#setScopeActive(org.semanticweb.owlapi.model.IRI, boolean)
	 */
	@Override
	public void setScopeActive(IRI scopeID, boolean active) {
		if (!containsScope(scopeID))
			throw new NoSuchScopeException(scopeID);
		// Prevent no-changes from firing events.
		boolean previousStatus = isScopeActive(scopeID);
		OntologyScope scope = getScope(scopeID);
		if (active == previousStatus)
			return;
		if (active) {
			scope.setUp();
			activeScopeIRIs.add(scopeID);
		} else {
			scope.tearDown();
			activeScopeIRIs.remove(scopeID);
		}
		fireScopeActivationChange(scopeID, active);
	}
}
