package application;
 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage; 

public class Alert {
	
    static  boolean answer;
    public  Alert() {}
    
    public static boolean display(String title, String message) {

        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(title.toUpperCase());
        window.getIcons().add(new Image(Alert.class.getResourceAsStream("data-flow.png")));

        GridPane layout = new GridPane();

        layout.setBackground(Background.fill(Color.web("#ebd3d9")));
        layout.setBorder(Border.stroke(Color.web("#CF203E")));

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(10);
        layout.setHgap(10);

        HBox messageBox = new HBox(10);

         

        Text text = new Text(message);
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        text.setWrappingWidth(400);

        messageBox.getChildren().addAll(text);
        GridPane.setConstraints(messageBox, 1, 0);
        layout.getChildren().add(messageBox);

        Button button1 = new Button("YES");
        button1.setMinWidth(60); // Set minimum width for button1
        button1.setTextFill(Color.BLACK);
        button1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button1.setBackground(Background.fill(Color.web("#6873A9")));

        Button button2 = new Button("NO");
        button2.setMinWidth(60); // Set minimum width for button2 for consistency
        button2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button2.setBackground(Background.fill(Color.web("#CF203E")));
        button2.setTextFill(Color.BLACK);

        button1.setOnAction(e -> {
            answer = true;
            window.close();
        });

        button2.setOnAction(e -> {
            answer = false;
            window.close();
        });

        // Create an empty region to act as a spacer between buttons
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBox = new HBox(10); // HBox to contain buttons with spacing
        buttonBox.getChildren().addAll(button2, spacer, button1);
        buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right
        buttonBox.setPadding(new Insets(10, 10, 10, 10)); // Add padding around the buttons
        GridPane.setConstraints(buttonBox, 1, 3); // Adjust the constraints as needed

        layout.getChildren().add(buttonBox);

        Scene scene = new Scene(layout, 500, 130);

        window.setScene(scene);
        window.setMaximized(false);
        window.showAndWait();

        return answer;
    }
    
    public static void display2(String title, String message) {
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title.toUpperCase());

        GridPane layout = new GridPane();

        window.getIcons().add(new Image(Alert.class.getResourceAsStream("data-flow.png")));

        layout.setBackground(Background.fill(Color.web("#ebd3d9")));
        layout.setBorder(Border.stroke(Color.web("#CF203E")));
        layout.setPadding(new Insets(10));
        layout.setVgap(10);
        layout.setHgap(10);

        HBox messageBox = new HBox(10);

        // Charge l'image depuis le fichier warning.png
        ImageView imageView = new ImageView(new Image(Alert.class.getResourceAsStream("alert.png")));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);

        // Ajuster la taille et le style du message
        Text text = new Text(message);
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        text.setWrappingWidth(400); // Largeur de la fenêtre - quelques marges

        // Ajoute l'image et le texte à la boîte de message
        messageBox.getChildren().addAll(imageView, text);
        layout.getChildren().addAll(messageBox);

        GridPane.setConstraints(messageBox, 0, 0); // Positionne messageBox en haut à gauche

        Button button1 = new Button("Okay");
        button1.setTextFill(Color.BLACK);
        button1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button1.setBackground(Background.fill(Color.web("#6873A9")));
        button1.setOnAction(e -> {
            window.close();
        });

        HBox buttonBox = new HBox(button1);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        layout.getChildren().add(buttonBox);
        GridPane.setConstraints(buttonBox, 0, 1);

        Scene scene = new Scene(layout, 464, 120); // Augmentez la hauteur de la fenêtre pour accueillir l'image et le message

        window.setScene(scene);
        window.setMaximized(false);
        window.showAndWait();
        
        
    }


    
    public static void display3(String title,String message)
    {
        
        Stage window =new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title.toUpperCase());
  
        
        GridPane layout =new GridPane();
        
        window.getIcons().add(new Image(Alert.class.getResourceAsStream( "data-flow.png" )));
        
        layout.setBackground(Background.fill(Color.web("#ebd3d9")));
        layout.setBorder(Border.stroke(Color.web("#CF203E")));
       
        
        
        layout.setPadding(new Insets(10,10,10,10));
        layout.setVgap(10);
        layout.setHgap(10);
        
        Label label =new Label(message);
       label.setTextFill(Color.BLACK);
        
        GridPane.setConstraints(label, 1, 0);
        
        Button button1 =new Button("OKAY");
        button1.setTextFill(Color.BLACK);
        GridPane.setConstraints(button1, 1, 4);
        button1.setBackground(Background.fill(Color.web("#ebd3d9")));
       
        
        button1.setOnAction(e ->{
       answer=true;
        window.close();
        });
        
        
        
        layout.getChildren().addAll(button1,label);
        
        Scene scene =new Scene(layout,500,125);
        
        window.setScene(scene);
        window.setMaximized(false);
        window.showAndWait();
        
        
        
    }
    


   












}