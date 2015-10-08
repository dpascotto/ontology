package it.dpascotto.ontology;

import java.io.File;
import java.util.Collections;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.util.PriorityCollection;

public class SbordaccioClass {
	
	static final String PATH = "C:/projects/201510 - saipem/eClassOWL/eclass_514en.owl";

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
			
		 
			System.out.println("End");
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
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
		IRI iri = IRI.create("file:///C:/projects/201510%20-%20saipem/eClassOWL/eclass_514en.owl");
		OWLOntology pizzaOntology = manager.loadOntologyFromOntologyDocument(iri);
		System.out.println("Loaded ontology: " + pizzaOntology);
		return pizzaOntology;
	}

}
