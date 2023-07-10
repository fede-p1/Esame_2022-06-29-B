/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.AlbumBilancio;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	
    	if (this.cmbA1.getValue() == null) {
    		txtResult.setText("Scegli un album (a1)");
    		return;
    	}
    	
    	txtResult.clear();
    	
    	for (AlbumBilancio ab : model.getSuccessori(cmbA1.getValue()))
    		txtResult.appendText(ab.toString() + '\n');
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	if (txtX.getText() == "") {
    		txtResult.setText("Inserisci una soglia x");
    		return;
    	}
    	
    	double x;
    	
    	try {
    		x = Double.parseDouble(txtX.getText());
    	}
    	catch(Exception e) {
    		txtResult.setText("Inserisci un numero");
    		return;
    	}
    	
    	if (this.cmbA1.getValue() == null) {
    		txtResult.setText("Scegli un album (a1)");
    		return;
    	}
    	
    	if (this.cmbA2.getValue() == null) {
    		txtResult.setText("Scegli un album (a2)");
    		return;
    	}
    	
    	txtResult.setText("SEQUENZA:\n");
    	
    	for (Album a : model.getPercorso(x, cmbA1.getValue(), cmbA2.getValue()))
    		txtResult.appendText(a.toString() + '\n');
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	if (txtN.getText() == "") {
    		txtResult.setText("Inserisci una durata n");
    		return;
    	}
    	
    	double n;
    	
    	try {
    		n = Double.parseDouble(txtN.getText());
    	}
    	catch(Exception e) {
    		txtResult.setText("Inserisci un numero");
    		return;
    	}
    	
    	DefaultDirectedWeightedGraph<Album,DefaultWeightedEdge> graph = model.creaGrafo(n);
    	
    	txtResult.setText("Grafo creato con " + graph.vertexSet().size() + " vertici e " + graph.edgeSet().size() + " archi.\n\n");
    	
    	this.cmbA1.getItems().clear();
    	this.cmbA2.getItems().clear();
    	List<Album> vertici = new ArrayList<>(graph.vertexSet());
    	Collections.sort(vertici);
    	this.cmbA1.getItems().addAll(vertici);
    	this.cmbA2.getItems().addAll(vertici);
    	
    	this.btnPercorso.setDisable(false);
    	this.btnAdiacenze.setDisable(false);
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.btnPercorso.setDisable(true);
    	this.btnAdiacenze.setDisable(true);
    }
}
