/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mondemineur;

import java.util.LinkedList;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author Florian
 */
public class InterfaceFx extends Application {

    private GridPane jeu = new GridPane();
    int size = 50;
    boolean firstClick = true;
    GrilleJeu demineur;
    private int width,height;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //nb de lignes pour la grille de l'interface
        int nbL=0;
        //nb de colonnes pour la grille de l'interface
        int nbC=0;
        //fenetre modale pour les lvl 
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Niveau de difficulté");
        alert.setHeaderText("Choisir un niveau de difficulté");
      //  alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Facile(10*10)");
        ButtonType buttonTypeTwo = new ButtonType("Moyen(15*15)");
        ButtonType buttonTypeThree = new ButtonType("Difficile(17*15)");
       // ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree,null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
        //l'utilisateur choisit facile(15*15)
       // System.out.println("Vous avez choisit : "+ result.get().getText());
        GrilleJeu griF = new GrilleJeu(10,10,10);
        nbL=10;
        nbC=10;
        width=600;
        height=700;
        } else if (result.get() == buttonTypeTwo) {
         //l'utilisateur choisit moyen(25*25)
          GrilleJeu griM = new GrilleJeu(15,15,20);
           nbL=15;
            nbC=15;
             width=800;
            height=900;
        } else if (result.get() == buttonTypeThree) {
         //l'utilisateur choisit difficile(50*50)
          GrilleJeu griD = new GrilleJeu(17,15,40);
           nbL=17;
        nbC=15;
        width=950;
         height=1000;
        } else {
        // pb rage quit 
        }
        
       
        
        stage.setTitle("Demineur");
        stage.setWidth(width);
        stage.setHeight(height);

        jeu.setGridLinesVisible(true);
        jeu.setAlignment(Pos.CENTER);
        jeu.setPadding(new Insets(20, 20, 20, 20));

        GridPane buttons = new GridPane();
        RowConstraints haut = new RowConstraints(75);
        buttons.getRowConstraints().add(haut);
        ColumnConstraints col = new ColumnConstraints(150);

        buttons.getColumnConstraints().add(col);
        buttons.getColumnConstraints().add(col);
        buttons.getColumnConstraints().add(col);

        Label label1 = new Label("Temps");
        label1.setTextAlignment(TextAlignment.CENTER);

        Label label2 = new Label("Jouer");
        label2.setTextAlignment(TextAlignment.CENTER);

        Label label3 = new Label("Flags");
        label3.setTextAlignment(TextAlignment.CENTER);

        buttons.add(label1, 0, 0);
        buttons.add(label2, 1, 0);
        buttons.add(label3, 2, 0);

        buttons.setGridLinesVisible(true);
        buttons.setAlignment(Pos.CENTER);

        //IL Faut aligner les label au centre
        //Peut etre tenter de les mettre dans un autre composant...
        for (int i = 0; i < nbC; i++) {
            ColumnConstraints column = new ColumnConstraints(size);
            jeu.getColumnConstraints().add(column);
        }
        for (int i = 0; i < nbL; i++) {
            RowConstraints row = new RowConstraints(size);
            jeu.getRowConstraints().add(row);
        }

        for (int i = 0; i < nbC; i++) {
            for (int j = 0; j < nbL; j++) {
                //addPane(i, j);
                StackPane border = new StackPane();
                Pane pane = new Pane();

                Label nombre = new Label("0");
                nombre.setStyle("-fx-font: 40 arial; -fx-text-fill: red;");
                nombre.setVisible(false);
                nombre.setMinWidth(size);
                nombre.setAlignment(Pos.CENTER);

                pane.getChildren().add(nombre);

                Image img = new Image("images/flag.jpg");
                ImageView imgView = new ImageView(img);
        //imgView.fitWidthProperty().bind(jeu.widthProperty()/colonnes); 

                //jeu.setCenter(img);
                StackPane.setMargin(pane, new Insets(1, 1, 1, 1));
                border.getChildren().add(pane);
                pane.setStyle("-fx-background-color: grey;");
               // border.setStyle("-fx-background-color: black;");
                InnerShadow shad = new InnerShadow();
                shad.setWidth(20);
                shad.setHeight(20);
                shad.setOffsetX(10);
                shad.setOffsetY(10);
                shad.setColor(Color.WHITE);
                shad.setRadius(50);
                pane.setEffect(shad);
               final int fi = i;
                final int fj = j;
                pane.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY) && firstClick) {

                        firstClick = false ;
                        demineur = new GrilleJeu(9, 9, 10, fi, fj);
                        System.out.println(demineur.toString());
                        LinkedList<Cellule> listUpdate = new LinkedList(demineur.revele(fi, fj));
                        for (Cellule cel : listUpdate) {
                            Label lab = new Label(Integer.toString(cel.getStatus()));
                            lab.setMinWidth(size);
                            lab.setAlignment(Pos.CENTER);
                            lab.setStyle("-fx-font: 40 arial; -fx-text-fill: red;");
                            pane.getChildren().add(lab);
                           
                            
                        }

                    }
                    else if(e.getButton().equals(MouseButton.PRIMARY))
                    {
                        LinkedList<Cellule> listUpdate = new LinkedList(demineur.revele(fi, fj));
                        for (Cellule cel : listUpdate) {
                            Label lab = new Label(Integer.toString(cel.getStatus()));
                            lab.setMinWidth(size);
                            lab.setAlignment(Pos.CENTER);
                            lab.setStyle("-fx-font: 40 arial; -fx-text-fill: red;");
                            pane.getChildren().add(lab);
                        }
                    }

                    //if (e.getButton().equals(MouseButton.SECONDARY)) {
                    //System.out.println(pane.getChildren());
                        /*if (pane.getChildren().contains(imgView)) {
                     pane.getChildren().remove(imgView);
                     } else {
                     imgView.relocate(0, 0);
                     pane.getChildren().addAll(imgView);
                     }*/
                        //System.out.println("Clicked on row = " + i + " and colum  = " + j);
                    //} else {
                    //System.out.printf("Mouse enetered cell [%d, %d]%n", i, j);
                        /*nombre.setVisible(true);
                     pane.setDisable(true);
                     pane.setStyle("-fx-background-color: white");
                     if (pane.getChildren().contains(imgView)) {
                     pane.getChildren().remove(imgView);
                     }*/
                    //}
                });
                jeu.add(border, i, j);
            }
        }

        final VBox vbox = new VBox();
        //vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(buttons, jeu);
        vbox.setAlignment(Pos.CENTER);
        //jeu.setGridLinesVisible(true);
        stage.setScene(new Scene(vbox));
        stage.show();

        //initialisation();
    }

}
