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
    private int width, height;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //nb de lignes pour la grille de l'interface
        int nbL = 0;
        //nb de colonnes pour la grille de l'interface
        int nbC = 0;
        int nbB=0; // nb bombes
        //fenetre modale pour les lvl 
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Niveau de difficulté");
        alert.setHeaderText("Choisir un niveau de difficulté");
        //  alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Facile(10*10)");
        ButtonType buttonTypeTwo = new ButtonType("Moyen(15*15)");
        ButtonType buttonTypeThree = new ButtonType("Difficile(15*25)");
        // ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            //l'utilisateur choisit facile(15*15)
            // System.out.println("Vous avez choisit : "+ result.get().getText());
            GrilleJeu griF = new GrilleJeu(10, 10, 10);
            nbL = 10;
            nbC = 10;
            nbB = 10 ;
            width = 600;
            height = 700;
        } else if (result.get() == buttonTypeTwo) {
            //l'utilisateur choisit moyen(25*25)
           
            GrilleJeu griM = new GrilleJeu(15, 15, 20);
            nbL = 15;
            nbC = 15;
            nbB = 25 ;
            width = 900;
            height = 900;
        } else if (result.get() == buttonTypeThree) {
            //l'utilisateur choisit difficile(15*25)
             //pour difficile, la grille des bombes est inversées(25*15) par rapport a la grille exterieur(15*25)
            GrilleJeu griD = new GrilleJeu(17, 15, 40);
            nbL = 15;
            nbC = 25;
            nbB = 80 ;
            width = 1400;
            height = 1000;
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
                //BorderPane border = new BorderPane();
                Pane pane = new Pane();

                Label nombre = new Label("0");
                nombre.setStyle("-fx-font: 40 arial; -fx-text-fill: red;");
                nombre.setVisible(false);
                nombre.setMinWidth(size);
                nombre.setAlignment(Pos.CENTER);

                pane.getChildren().add(nombre);

                Image img0 = new Image("images/tile.jpg");
                ImageView couverte = new ImageView(img0);
                couverte.setFitWidth(48);
                couverte.setPreserveRatio(true);
                couverte.setSmooth(true);
                couverte.setCache(true);

                jeu.add(couverte, i, j);//POURQUOI CA MARCHE PAS CA MET DES IMAGES BLANCHES SUR TOUTES LES CASES

                Image img = new Image("images/flag.jpg");
                ImageView flag = new ImageView(img);
                flag.setFitWidth(48);
                flag.setPreserveRatio(true);
                flag.setSmooth(true);
                flag.setCache(true);

                Image img2 = new Image("images/idk.jpg");
                ImageView idk = new ImageView(img2);
                idk.setFitWidth(48);
                idk.setPreserveRatio(true);
                idk.setSmooth(true);
                idk.setCache(true);
        //imgView.fitWidthProperty().bind(jeu.widthProperty()/colonnes); 

                //jeu.setCenter(img);
                StackPane.setMargin(pane, new Insets(1, 1, 1, 1)); // StackPane
                border.getChildren().add(pane);
                //border.setCenter(pane);
                
                //c est ici qu'il fallait modifier pour afficher ton image 
             //   pane.setStyle("-fx-background-color: white;");

                final int fi = i;
                final int fj = j;
                final int fnbL = nbL;
                final int fnbC = nbC;
                final int fnbB = nbB;
                pane.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY) && firstClick) {

                        firstClick = false;
                        demineur = new GrilleJeu(fnbL, fnbC, fnbB, fi, fj);
                        System.out.println(demineur.toString()); // temporaire
                        LinkedList<Cellule> listUpdate = new LinkedList(demineur.revele(fi, fj));
                        for (Cellule cel : listUpdate) {

                            Label lab = new Label(Integer.toString(cel.getStatus()));
                            lab.setMinWidth(size);
                            lab.setAlignment(Pos.CENTER);
                            lab.setStyle("-fx-font: 40 arial; -fx-text-fill: red;");
                            //lab.setMouseTransparent(true);
                            //pane.getChildren().add(lab);
                            //jeu.getChildren().add(fi*fj, lab);
                            jeu.add(lab, cel.getX(), cel.getY());

                        }

                    } else if (e.getButton().equals(MouseButton.PRIMARY) && (demineur.getGrilleExterieur()[fi][fj].getStatus() == -5))  {

                        LinkedList<Cellule> listUpdate = new LinkedList(demineur.revele(fi, fj));
                        listUpdate.toString();
                         for (Cellule cel : listUpdate) {
                            if(cel.getStatus()==-1){
                                Image img3 = new Image("images/bombe.png");
                                ImageView bombe = new ImageView(img3);
                                bombe.setFitWidth(48);
                                bombe.setPreserveRatio(true);
                                bombe.setSmooth(true);
                                bombe.setCache(true);
                                jeu.add(bombe, cel.getX(), cel.getY());
                                
                            }/*else if (cel.getStatus()==0){
                                Label lab = new Label("");
                                lab.setMinWidth(size);
                                lab.setAlignment(Pos.CENTER);
                                lab.setStyle("-fx-font: 40 arial; -fx-text-fill: red;");
                                //lab.setMouseTransparent(true);
                                //pane.getChildren().add(lab);
                                //jeu.getChildren().add(fi*fj, lab);
                                jeu.add(lab, cel.getX(), cel.getY());                                
                            }*/
                            else{
                                Label lab = new Label(Integer.toString(cel.getStatus()));
                                lab.setMinWidth(size);
                                lab.setAlignment(Pos.CENTER);
                                lab.setStyle("-fx-font: 40 arial; -fx-text-fill: red;");
                                //lab.setMouseTransparent(true);
                                //pane.getChildren().add(lab);
                                //jeu.getChildren().add(fi*fj, lab);
                                jeu.add(lab, cel.getX(), cel.getY());                                
                            }
                        }
                    } else if (e.getButton().equals(MouseButton.SECONDARY) && !firstClick) {
                        Cellule cFlag = demineur.flag(fi, fj);
                        flag.setMouseTransparent(true);
                        idk.setMouseTransparent(true);
                        switch (demineur.getGrilleExterieur()[fi][fj].getStatus()) {
                            // ça marche pas pour l'instant
                            case -2:

                                jeu.add(flag, cFlag.getX(), cFlag.getY());

                                break;
                            //on met ?
                            case -3:
                                jeu.getChildren().remove(flag);
                                jeu.add(idk, cFlag.getX(), cFlag.getY());

                                break;
                            //on recouvre
                            //jeu.setStyle("fx-img: ");
                            case -5:
                                jeu.getChildren().remove(idk);
                                break;
                            default:
                                break;
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
