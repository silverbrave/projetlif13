/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mondemineur;

import java.util.LinkedList;
import javafx.scene.image.Image;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Demineur");
        stage.setWidth(600);
        stage.setHeight(700);

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
        for (int i = 0; i < 9; i++) {
            ColumnConstraints column = new ColumnConstraints(size);
            jeu.getColumnConstraints().add(column);
        }
        for (int i = 0; i < 9; i++) {
            RowConstraints row = new RowConstraints(size);
            jeu.getRowConstraints().add(row);
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
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
                border.setStyle("-fx-background-color: black;");
                final int fi = i;
                final int fj = j;
                pane.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY) && firstClick) {

                        firstClick = false ;
                        demineur = new GrilleJeu(9, 9, 10, fi, fj);
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
