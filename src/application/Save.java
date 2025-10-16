package application;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Save {

	
	
	 @FXML
	    public  Pane  VerificationAffichage; 
	    @FXML
	    public  Pane XMLAffichage; 
	    @FXML
		public  TextArea XMLArea ;
	   
	    @FXML
	    public  AnchorPane  paneAffichage; 

	    @FXML
	    public  CheckBox  XMLHide; 
	    @FXML
	    public  Pane  config; 
	    @FXML
	    public Pane  system; 
	    public  Pane textv = new Pane();
	    public Pane textvv = new Pane();
	    public Text textconxml;
	    
	    
	    public Pane savedVerificationAffichage;


		public Pane getVerificationAffichage() {
			return VerificationAffichage;
		}


		public void setVerificationAffichage(Pane verificationAffichage) {
			VerificationAffichage = verificationAffichage;
		}


		public Pane getXMLAffichage() {
			return XMLAffichage;
		}


		public void setXMLAffichage(Pane xMLAffichage) {
			XMLAffichage = xMLAffichage;
		}


		public TextArea getXMLArea() {
			return XMLArea;
		}


		public void setXMLArea(TextArea xMLArea) {
			XMLArea = xMLArea;
		}


		public AnchorPane getPaneAffichage() {
			return paneAffichage;
		}


		public void setPaneAffichage(AnchorPane paneAffichage) {
			this.paneAffichage = paneAffichage;
		}


		public CheckBox getXMLHide() {
			return XMLHide;
		}


		public void setXMLHide(CheckBox xMLHide) {
			XMLHide = xMLHide;
		}


		public Pane getConfig() {
			return config;
		}


		public void setConfig(Pane config) {
			this.config = config;
		}


		public Pane getSystem() {
			return system;
		}


		public void setSystem(Pane system) {
			this.system = system;
		}


		public Pane getTextv() {
			return textv;
		}


		public void setTextv(Pane textv) {
			this.textv = textv;
		}


		public Pane getTextvv() {
			return textvv;
		}


		public void setTextvv(Pane textvv) {
			this.textvv = textvv;
		}


		public Text getTextconxml() {
			return textconxml;
		}


		public void setTextconxml(Text textconxml) {
			this.textconxml = textconxml;
		}


		public Pane getSavedVerificationAffichage() {
			return savedVerificationAffichage;
		}


		public void setSavedVerificationAffichage(Pane savedVerificationAffichage) {
			this.savedVerificationAffichage = savedVerificationAffichage;
		}


		public Save(Pane verificationAffichage, Pane xMLAffichage, TextArea xMLArea, AnchorPane paneAffichage,
				CheckBox xMLHide, Pane config, Pane system, Pane textv, Pane textvv, Text textconxml,
				Pane savedVerificationAffichage) {
			super();
			VerificationAffichage = verificationAffichage;
			XMLAffichage = xMLAffichage;
			XMLArea = xMLArea;
			this.paneAffichage = paneAffichage;
			XMLHide = xMLHide;
			this.config = config;
			this.system = system;
			this.textv = textv;
			this.textvv = textvv;
			this.textconxml = textconxml;
			this.savedVerificationAffichage = savedVerificationAffichage;
		}
	    
	    
	    
	    
	    
	    
	    
}
