package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	//PER LA RICORSIONE
	
	//1) struttura dati finale
	//lista di stati(STRING)in cui c'e' lo stato di partenza e un insieme di altri stati NON ripetuti
	private List<String> ottima;
	
	//2) struttura dati parziale=lista definita nel metodo ricorsivo
	
	//3) condizione di terminazione
	//dopo un determinato nodo, non ci sono piu' successori che non ho considerato
	
	//4) generare una nuova soluzione a partire da una sol. parziale
	//dato l'ultimo nodo inserito in parziale, considero tutti i successori di quel nodo che non ho ancora considerato
	
	//5)filtro
	//alla fine ritornero' una sola soluzione -> la migliore cioe' la piu' lunga(=quella in cui la size e' max)
	
	//6)livello di ricorsione
	//lunghezza del percorso parziale
	
	//7)il caso iniziale
	//parziale contiene il mio stato di partenza
	
	
	
	private SightingsDAO dao;
	private List<String>stati;//non serve la idMap, ma basta una lista di stato che usiamo ogni volta
	private Graph<String, DefaultEdge> grafo;
	
	public Model() {
		this.dao=new SightingsDAO();
	}
	
	public List<AnnoCount> getAnni(){
		return dao.getAnni();
	}

	
	public void creaGrafo(Year annoSelezionato) {
		this.grafo=new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		this.stati = this.dao.getStati(annoSelezionato);
		Graphs.addAllVertices(this.grafo, this.stati);
		
		//Soluzione semplice: doppio ciclo, controllo esistenza arco
		for(String s1: this.grafo.vertexSet()) {
			for(String s2: this.grafo.vertexSet()) {
				if(!s1.equals(s2)) {
					if(this.dao.esistenzaArco(s1, s2, annoSelezionato)) {
						grafo.addEdge(s1, s2);
					}
				}
			}
		}
		
		System.out.println("Grafo creato! Il grafo contiene "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+ " archi.");

	}
	
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
	public List<String> getStati(){
		return this.stati;
	}
	
	public List<String> getSuccessori(String stato){
		return Graphs.successorListOf(this.grafo, stato);
	}
	
	public List<String> getPredecessori(String stato){
		return Graphs.predecessorListOf(this.grafo, stato);
	}
	
	public List<String> getRaggiungibili(String stato) {
		List<String>raggiungibili= new LinkedList<>();
		DepthFirstIterator <String, DefaultEdge> dp = new DepthFirstIterator<String, DefaultEdge>(this.grafo, stato);
		
		dp.next();
		
		while(dp.hasNext()) {
			raggiungibili.add(dp.next());
		}
		
		return raggiungibili;

	}
	
	//partenza stabilita dall'utente quindi proviene dal controller
	public List<String> getPercorsoMassimo(String partenza){
		this.ottima=new LinkedList<String>();//ogni volta la ricreo
		//La soluzione parziale sara' anch'essa una lista di stringhe
		List<String>parziale=new LinkedList<String>();
		parziale.add(partenza);
		
		cercaPercorso(parziale);
		
		return this.ottima;
}

	private void cercaPercorso(List<String> parziale) {
		
		//Mi faccio calcolare i successori del mio stato di partenza che sara' nell'ultima posizione della lista 'parziale'
		List<String> candidati = this.getSuccessori(parziale.get(parziale.size()-1));
		for(String candidato: candidati) {
			//se e' un candidato che non ho ancora considerato, lo valuto
			if(!parziale.contains(candidato)) {			
				parziale.add(candidato);
				this.cercaPercorso(parziale);//ricorsione
				parziale.remove(parziale.size()-1);//backtracking
			}
		}
		
		//vedere se la soluzione corrente e' migliore della ottima corrente
		if(parziale.size() > ottima.size()) {
			this.ottima = new LinkedList<String>(parziale);
		}
		
	}





}
