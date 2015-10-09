package it.dpascotto.ontology.util;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class OntologyUtil {

	static Logger log = Logger.getLogger(OntologyUtil.class);
	static final int INDENT_SIZE = 4;
	static ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
	
	public static void printOntology(OWLOntology ontology) {
		OWLClass clazz = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
	
		try {
			printHierarchyFromClass(getDefaultReasoner(ontology), clazz);
		} catch (OWLException e) {
			log.error("[OntologyUtil::printOntology] Unable to print ontology " + ontology.getOntologyID(), e);
		}
	}
	
	private static void printHierarchyFromClass(OWLReasoner reasoner, OWLClass clazz) throws OWLException {
		printHierarchyFromClass(reasoner, clazz, 0);
	}
		
	private static void printHierarchyFromClass(OWLReasoner reasoner, OWLClass clazz, int level) throws OWLException {
		if (reasoner.isSatisfiable(clazz)) {
			String indent = "";
			for (int i = 0; i < level * INDENT_SIZE; i++) {
				indent += " ";
			}
			log.debug(indent + shortFormProvider.getShortForm(clazz));
			
			for (OWLClass child : reasoner.getSubClasses(clazz, true).getFlattened()) {
				if (!child.equals(clazz)) {
					printHierarchyFromClass(reasoner, child, level + 1);
				}
			}
		}
	}
	
	private static String owlClassToString(OWLClass clazz) {
		if (clazz == null) {
			return "[null]";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("Type: " + clazz.getEntityType() + ", ");
		sb.append("short form: " + clazz.getIRI().getShortForm() + ", ");
		
		return sb.toString();
	}

	private static OWLReasoner getDefaultReasoner(OWLOntology o) {
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();//(OWLReasonerFactory) Class.forName("org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory").newInstance();
		OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(o);
		
		return reasoner;
	}
	
	

}
