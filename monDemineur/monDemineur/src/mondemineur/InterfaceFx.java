/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mondemineur;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
import javafx.application.Platform;
import javafx.scene.control.Button;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import static mondemineur.GrilleJeu.BOMBE;

/**
 *
 * @author Florian Hugo et Kevin
 */
public class InterfaceFx extends Application {

    private GridPane jeu = new GridPane();
    int size = 50;
    boolean firstClick = true;
    GrilleJeu demineur;
    private int width, height;
    private int tpsTimer = 0;
    private Timer leTimer;
    private Timer timerX;
    //nb de lignes pour la grille de l'interface
    private int nbL = 0;
    //nb de colonnes pour la grille de l'interface
    private int nbC = 0;
    private int nbB = 0; // nb bombes
    private Label label1;
    private Label label3;
    private int level;
    private boolean hardcore = false;
    private Button btRestart;
    private Pane[][] paneArray;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        tpsTimer = 0;
        afficheFenetreDifficulte();

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

        label1 = new Label("Temps: 0");
        label1.setTextAlignment(TextAlignment.CENTER);

        btRestart = new Button("Restart");
        btRestart.setAlignment(Pos.CENTER);
        label3 = new Label("Bombes: " + nbB);
        label3.setTextAlignment(TextAlignment.CENTER);

        buttons.add(label1, 0, 0);
        buttons.add(btRestart, 1, 0);
        buttons.add(label3, 2, 0);

        buttons.setGridLinesVisible(true);
        buttons.setAlignment(Pos.CENTER);

        btRestart.setOnMouseClicked(e -> {
            restart();
        });

        for (int i = 0; i < nbC; i++) {
            ColumnConstraints column = new ColumnConstraints(size);
            jeu.getColumnConstraints().add(column);
        }
        for (int i = 0; i < nbL; i++) {
            RowConstraints row = new RowConstraints(size);
            jeu.getRowConstraints().add(row);
        }
        if (hardcore) {
            paneArray = new Pane[nbL][nbC];
        }

        for (int i = 0; i < nbC; i++) {
            for (int j = 0; j < nbL; j++) {

                StackPane border = new StackPane();
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

                jeu.add(couverte, i, j);

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

                Image img4 = new Image("images/reveler.png");
                ImageView revele = new ImageView(img4);
                revele.setFitWidth(48);
                revele.setPreserveRatio(true);
                revele.setSmooth(true);
                revele.setCache(true);

                StackPane.setMargin(pane, new Insets(1, 1, 1, 1)); // StackPane
                border.getChildren().add(pane);

                final int fi = i;
                final int fj = j;
                final int fnbL = nbL;
                final int fnbC = nbC;
                final int fnbB = nbB;
                if (hardcore) {
                    paneArray[i][j] = pane;
                }

                pane.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY) && firstClick) {

                        firstClick = false;
                        demineur = new GrilleJeu(fnbC, fnbL, fnbB, fi, fj);
                        System.out.println(demineur.toString()); // temporaire
                        LinkedList<Cellule> listUpdate = new LinkedList(demineur.revele(fi, fj));
                        for (Cellule cel : listUpdate) {
                            String valLabel1 = Integer.toString(cel.getStatus());

                            Label lab = new Label(valLabel1);
                            lab.setMinWidth(size);
                            lab.setAlignment(Pos.CENTER);

                            lab.setStyle(coloreNb(valLabel1));

                            jeu.add(lab, cel.getX(), cel.getY());
                        }
                        leTimer = creTimer();
                        if (hardcore) {
                        timerX = TimerAleatoire();
                        }

                    } else if (e.getButton().equals(MouseButton.PRIMARY) && (demineur.getGrilleExterieur()[fi][fj].getStatus() == -5)) {
                        if (hardcore) {timerX.cancel();}
                        LinkedList<Cellule> listUpdate = new LinkedList(demineur.revele(fi, fj));
                        listUpdate.toString();
                        for (Cellule cel : listUpdate) {
                            if (cel.getStatus() == -1) {
                                Image img3 = new Image("images/bombe.png");
                                ImageView bombe = new ImageView(img3);
                                bombe.setFitWidth(48);
                                bombe.setPreserveRatio(true);
                                bombe.setSmooth(true);
                                bombe.setCache(true);
                                jeu.add(bombe, cel.getX(), cel.getY());

                            }else {
                                String valLabel2 = Integer.toString(cel.getStatus());
                                Label lab = new Label(valLabel2);
                                lab.setMinWidth(size);
                                lab.setAlignment(Pos.CENTER);

                                lab.setStyle(coloreNb(valLabel2));

                                jeu.add(lab, cel.getX(), cel.getY());

                            }
                        }

                        if (hardcore) {timerX = TimerAleatoire();}
                        if (demineur.estFini(fi, fj)) {
                            // on affiche une fenetre et on bloque le reste
                            //juste un test de l'affichage
                            //il faut pouvoir savoir si on a gagner ou perdu avant d'utiliser la fct affiche
                            if (hardcore) {timerX.cancel();}
                            jeu.setDisable(true);
                            leTimer.cancel();
                            if (demineur.gagne(fi, fj)) {
                                //on serialise le score
                                Score sc = new Score(level, tpsTimer);
                                sc.updateBestScore();
                                afficheFenetreFin(true);
                            } else {
                                afficheFenetreFin(false);
                            }

                        }

                    } else if (e.getButton().equals(MouseButton.SECONDARY) && !firstClick) {
                        if (hardcore) {timerX.cancel();}
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
                        if (hardcore) {timerX = TimerAleatoire();}
                        if (demineur.estFini(fi, fj)) {
                            if(hardcore){
                                timerX.cancel();
                            }
                            // on affiche une fenetre et on bloque le reste
                            jeu.setDisable(true);
                            
                            leTimer.cancel();
                            if (demineur.gagne(fi, fj)) {
                                //on serialise le score
                                Score sc = new Score(level, tpsTimer);
                                sc.updateBestScore();
                                afficheFenetreFin(true);
                            } else {
                                afficheFenetreFin(false);
                            }
                        }
                    }
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

    //methode pour afficher une fenetre a la fin de la partie
    public void afficheFenetreFin(boolean winner) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Fin du jeu !");
        alert.setHeaderText(null);
        if (winner) {
            alert.setContentText("Vous avez gagné en : " + tpsTimer + " secondes");
        } else {
            alert.setContentText("Dommage, retente ta chance!");
        }
        alert.showAndWait();

    }

    public void afficheFenetreDifficulte() {
        //fenetre modale pour les lvl
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Niveau de difficulté");
        alert.setHeaderText("Choisir un niveau de difficulté");
        
        ButtonType buttonTypeOne = new ButtonType("Facile(10*10)");
        ButtonType buttonTypeTwo = new ButtonType("Moyen(15*15)");
        ButtonType buttonTypeThree = new ButtonType("Difficile(15*25)");
        ButtonType buttonTypeFour = new ButtonType("Personnaliser");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeFour);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            //facile
            nbL = 10;
            nbC = 10;
            nbB = 10;
            width = nbC * 55;
            height = nbL * 55 + 150;
        } else if (result.get() == buttonTypeTwo) {
            //moyen
            nbL = 15;
            nbC = 15;
            nbB = 25;
            width = nbC * 55;
            if (width < 500) {
                width = 500;
            }
            height = nbL * 55 + 150;
        } else if (result.get() == buttonTypeThree) {
            //difficile
            nbL = 15;
            nbC = 25;
            nbB = 80;
            width = nbC * 55;
            if (width < 500) {
                width = 500;
            }
            height = nbL * 55 + 150;
        } else if (result.get() == buttonTypeFour) {
            Dialog<int[]> dialog = new Dialog<>();
            dialog.setTitle("Difficulté personnalisée");
            dialog.setHeaderText("Choisissez les paramètres :");
            dialog.setResizable(true);

            Label label1 = new Label("Lignes: (max.15) ");
            Label label2 = new Label("Colonnes: (max.20) ");
            Label label3 = new Label("Nombre de bombes: (<L*C) ");
            Label label4 = new Label("Mode hardcore :");
            
            CheckBox cb = new CheckBox();
            
 
            
            
            
            NumberTextField text1 = new NumberTextField();
            NumberTextField text2 = new NumberTextField();
            NumberTextField text3 = new NumberTextField();

            GridPane grid = new GridPane();
            grid.add(label1, 1, 1);
            grid.add(text1, 4, 1);
            grid.add(label2, 1, 2);
            grid.add(text2, 4, 2);
            grid.add(label3, 1, 3);
            grid.add(text3, 4, 3);
            grid.add(label4, 1, 4);
            grid.add(cb, 4, 4);
            dialog.getDialogPane().setContent(grid);

            ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

            Optional<int[]> result2 = dialog.showAndWait();

            nbL = Integer.parseInt(text1.getText());
            if (nbL > 15) {
                nbL = 15;
            }
            nbC = Integer.parseInt(text2.getText());
            if (nbC > 20) {
                nbC = 20;
            }
            nbB = Integer.parseInt(text3.getText());
            width = nbC * 55;
            if (width < 500) {
                width = 500;
            }
            height = nbL * 55 + 150;
            hardcore = cb.isSelected();
        } else {
            // pb rage quit
        }
    }

    public Timer creTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tpsTimer++;
                        //System.out.println(tpsTimer);
                        label1.setText("Temps : " + tpsTimer);
                    }
                });
            }
        }, 0, 1000);
        return timer;
    }

    public Timer TimerAleatoire() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Random random = new Random(System.currentTimeMillis());
                        int lx = Math.abs(random.nextInt() % nbL);
                        int cx = Math.abs(random.nextInt() % nbC);

                        while (demineur.getGrilleExterieur()[lx][cx].getStatus() != -5) {
                            lx = Math.abs(random.nextInt() % nbL);
                            cx = Math.abs(random.nextInt() % nbC);
                        }

                        Event.fireEvent(paneArray[lx][cx], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                        System.out.println("Clique");
                    }
                });
            }
        }, 5000, 5000);
        return timer;
    }

    public void restart() {
        System.out.println("TU VEUX RECOMMENCER ?");
        afficheFenetreDifficulte();

        //TODO 
        // voir si on relance l'appli (je pense tres dure a implementer)
        //soit tout reset et reafficher la fenetre avec les difficultes?
    }

    public String coloreNb(String valLab) {
        switch (valLab) {
            case "1":
                return "-fx-font: 40 arial; -fx-text-fill: blue;";

            case "2":
                return "-fx-font: 40 arial; -fx-text-fill: green;";

            case "3":
                return "-fx-font: 40 arial; -fx-text-fill: red;";

            case "4":
                return "-fx-font: 40 arial; -fx-text-fill: purple;";

            case "5":
                return "-fx-font: 40 arial; -fx-text-fill: #000099;";

            case "6":
                return "-fx-font: 40 arial; -fx-text-fill: #006600;";

            case "7":
                return "-fx-font: 40 arial; -fx-text-fill: #990000;";

            case "8":
                return "-fx-font: 40 arial; -fx-text-fill: #660066;";

            default:
                return "-fx-font: 40 arial; -fx-text-fill: red;";

        }
    }

    @Override
    public void stop() {
        leTimer.cancel();
        if(hardcore){
            timerX.cancel();
        }

    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
