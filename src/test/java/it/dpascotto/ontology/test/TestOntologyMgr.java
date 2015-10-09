package it.dpascotto.ontology.test;

import it.dpascotto.ontology.OntologyMgr;
import it.dpascotto.ontology.util.OntologyUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.owlapi.model.OWLOntology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })

public class TestOntologyMgr {
	@Autowired OntologyMgr ontologyMgr;
	
	//@Test
	public void testOntologyMgr() {
		try {
			OWLOntology ontology = ontologyMgr.loadOntologyFromFile("C:/projects/201510%20-%20saipem/eClassOWL/eclass_514en.owl");
			
			org.junit.Assert.assertNotNull(ontology);
			
			OntologyUtil.printOntology(ontology);
			System.out.println("Test OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testKoalaOntology() {
		try {
			OWLOntology ontology = ontologyMgr.loadOntologyFromClasspath("ontologies/koala.owl");
			
			org.junit.Assert.assertNotNull(ontology);
			
			OntologyUtil.printOntology(ontology);
			
			System.out.println("Test OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
