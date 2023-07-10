package it.polito.tdp.itunes.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	ItunesDAO dao;
	DefaultDirectedWeightedGraph<Album,DefaultWeightedEdge> graph;
	
	public Model() {
		dao = new ItunesDAO();
	}
	
	public DefaultDirectedWeightedGraph<Album,DefaultWeightedEdge> creaGrafo(double n){ //n*1000
		
		graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		List<Album> vertex = new ArrayList<>(dao.getAlbums((int) n));
		
		Graphs.addAllVertices(graph, vertex);
		
		for (Album a1 : graph.vertexSet())
			for (Album a2 : graph.vertexSet())
				if ((!a1.equals(a2)) && (!graph.containsEdge(a2, a1)) && (!graph.containsEdge(a1, a2)) 
						&& (a1.getDurata() != a2.getDurata())) {
					double sommaDurate = a1.getDurata() + a2.getDurata();
					if (sommaDurate > 4*n) {
						if (a1.getDurata() < a2.getDurata())
							Graphs.addEdge(graph, a1, a2, sommaDurate);
						else
							Graphs.addEdge(graph, a2, a1, sommaDurate);
					}
				}
		
		return graph;
		
	}
	
	public List<AlbumBilancio> getSuccessori(Album a1){
		
		List<AlbumBilancio> successori = new ArrayList<>();
		
		for (Album suc : Graphs.successorListOf(graph, a1)) {
			int bilancio = 0;
			for (DefaultWeightedEdge edgeOut : graph.outgoingEdgesOf(suc))
				bilancio -= graph.getEdgeWeight(edgeOut);
			for (DefaultWeightedEdge edgeIn : graph.incomingEdgesOf(suc))
				bilancio += graph.getEdgeWeight(edgeIn);
			successori.add(new AlbumBilancio(suc,bilancio));
		}
		
		Collections.sort(successori);
		return successori;
	}
	
	private List<Album> soluzione;
	private double numBilancioMax;
	
	public List<Album> getPercorso(double x, Album partenza, Album arrivo){
		
		soluzione = new ArrayList<>();
		List<Album> parziale = new ArrayList<>();
		parziale.add(partenza);
		
		ricorsiva(partenza,partenza,arrivo,x,parziale);
		
		return soluzione;	
		
	}
	
	private void ricorsiva(Album partenza, Album corrente, Album arrivo, double x, List<Album> parziale) {
		
		if (corrente.equals(arrivo) && numBilancio(parziale,partenza) > numBilancioMax)
			soluzione = new ArrayList<>(parziale);
		
		List<Album> successori = Graphs.successorListOf(graph, corrente);
		
		for (Album suc : successori) {
			if (graph.getEdgeWeight(graph.getEdge(parziale.get(parziale.size()-1), suc)) >= x) {
				parziale.add(suc);
				ricorsiva(partenza,suc,arrivo,x,parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	private int numBilancio(List<Album> parziale, Album partenza) {
		
		int tot = 0;
		
		int bilancioPartenza = 0;
		for (DefaultWeightedEdge edgeOut : graph.outgoingEdgesOf(partenza))
			bilancioPartenza -= graph.getEdgeWeight(edgeOut);
		for (DefaultWeightedEdge edgeIn : graph.incomingEdgesOf(partenza))
			bilancioPartenza += graph.getEdgeWeight(edgeIn);
		
		for (Album a : parziale) {
			int bilancio = 0;
			for (DefaultWeightedEdge edgeOut : graph.outgoingEdgesOf(a))
				bilancio -= graph.getEdgeWeight(edgeOut);
			for (DefaultWeightedEdge edgeIn : graph.incomingEdgesOf(a))
				bilancio += graph.getEdgeWeight(edgeIn);
			if (bilancio > bilancioPartenza)
				tot++;
		}
		
		return tot;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
