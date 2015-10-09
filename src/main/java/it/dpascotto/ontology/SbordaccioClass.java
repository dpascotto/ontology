package it.dpascotto.ontology;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.util.PriorityCollection;

public class SbordaccioClass {
	
	//static final String PATH = "C:/projects/201510 - saipem/eClassOWL/eclass_514en.owl";
	static final String PATH = "C:/javasource/my/ontology/src/main/resources/ontologies/koala.owl";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello OWL!");
		
		OWLOntologyManager m = create();
		try {
			//OWLOntology o = loadEClassOntology(m);
			OWLOntology o = loadProductsOntology();
			
			System.out.println("Ontology loaded:\r\n" + o);
			
			/*
			int i;
			
			i = 0;
			System.out.println("---------------------------------------------------------------------");
			System.out.println("Classes:");
			for (OWLClass cls : o.getClassesInSignature()) {
				i++;
				System.out.println(cls);
				if (i >= 100) {
					break;
				}
			}
			
			i = 0;
			System.out.println("---------------------------------------------------------------------");
			System.out.println("Axioms:");
			for (OWLAxiom axi : o.getAxioms()) {
				i++;
				System.out.println(axi);
				if (i >= 100) {
					break;
				}
			}
			
			System.out.println("---------------------------------------------------------------------");
			System.out.println("Walker Texas Ranger:");
			OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(o));
			// Now ask our walker to walk over the ontology
			OWLOntologyWalkerVisitor visitor = new OWLOntologyWalkerVisitor(
					walker) {
				@Override
				public void visit(OWLObjectSomeValuesFrom desc) {
					System.out.println(desc);
					System.out.println(" " + getCurrentAxiom());
				}
			};
			// Have the walker walk...
			walker.walkStructure(visitor);
			
			*/
			
			System.out.println("---------------------------------------------------------------------");
			System.out.println("Reasoner:");
			OWLClass clazz = m.getOWLDataFactory().getOWLThing();
			
			OWLReasonerFactory reasonerFactory = (OWLReasonerFactory) Class.forName("org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory").newInstance();
			OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(o);

			printHierarchy(reasoner, clazz, 1);
			
			
			OWLDataFactory dataFactory = m.getOWLDataFactory();
		 
			//String base = "http://www.ebusiness-unibw.org/ontologies/eclass/5.1.4/";
			//OWLDataProperty hasName = dataFactory.getOWLDataProperty(IRI.create(base + "#ID"));
			//OWLClassExpression ce = dataFactory.getOWLDataHasValue(hasName, dataFactory.getOWLLiteral("C_AKK950002-gen"));
			OWLClassExpression ce = dataFactory.getOWLClass(IRI.create("http://www.ebusiness-unibw.org/ontologies/eclass/5.1.4/#C_AKK950002-gen"));
			System.out.println("Class expression: " + ce);
			
			Set<OWLNamedIndividual> result = reasoner.getInstances(ce, true).getFlattened();        
			for (OWLNamedIndividual owlNamedIndividual : result) {
			    System.out.println(owlNamedIndividual);
			}
			
			System.out.println("End");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void printHierarchy(@Nonnull OWLReasoner reasoner, @Nonnull OWLClass clazz, int level) throws OWLException {
		/*
		 * Only print satisfiable classes -- otherwise we end up with bottom
		 * everywhere
		 */
		if (reasoner.isSatisfiable(clazz)) {
			for (int i = 0; i < level * 4; i++) {
				System.out.print(" ");
			}
			// System.out.println(labelFor(clazz));
			System.out.println(clazz);
			/* Find the children and recurse */
			for (OWLClass child : reasoner.getSubClasses(clazz, true).getFlattened()) {
				if (!child.equals(clazz)) {
					printHierarchy(reasoner, child, level + 1);
				}
			}
		}
	}
	
    public static OWLOntologyManager create() {
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        PriorityCollection<OWLOntologyIRIMapper> iriMappers = m.getIRIMappers();
        iriMappers.add(new AutoIRIMapper(new File("materializedOntologies"), true));
        return m;
    }
    
    private static OWLOntology loadEClassOntology(OWLOntologyManager m) throws OWLOntologyCreationException {
        return m.loadOntologyFromOntologyDocument(new StringDocumentSource(PATH));
    }

	private static OWLOntology loadPizzaOntology() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		// Load an ontology from the Web
		IRI iri = IRI.create("http://protege.stanford.edu/ontologies/pizza/pizza.owl");
		OWLOntology pizzaOntology = manager.loadOntologyFromOntologyDocument(iri);
		System.out.println("Loaded ontology: " + pizzaOntology);
		return pizzaOntology;
	}

	private static OWLOntology loadProductsOntology() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		// Load an ontology from the Web
		IRI iri = IRI.create("file:///" + PATH);
		OWLOntology pizzaOntology = manager.loadOntologyFromOntologyDocument(iri);
		System.out.println("Loaded ontology: " + pizzaOntology);
		return pizzaOntology;
	}

}
