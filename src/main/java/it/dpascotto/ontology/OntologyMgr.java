package it.dpascotto.ontology;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.stereotype.Service;

@Service("ontology-mgr")
public class OntologyMgr {
	Logger log = Logger.getLogger(OntologyMgr.class);
	
	public OWLOntology loadOntologyFromFile(String pathToFile) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		log.info("[OntologyMgr::loadOntologyFromFile] Loading ontology from file " + pathToFile);
		IRI iri = IRI.create("file:///" + pathToFile);
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(iri);
		
		log.info("[OntologyMgr::loadOntologyFromFile] Ontology loaded: " + ontology);
		return ontology;
	}

	public OWLOntology loadOntologyFromClasspath(String relativePath) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		log.info("[OntologyMgr::loadOntologyFromClasspath] Loading ontology from relative path " + relativePath);
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath);
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputStream);
		
		log.info("[OntologyMgr::loadOntologyFromClasspath] Ontology loaded: " + ontology);
		return ontology;
	}

}
