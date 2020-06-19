package sample;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import sample.data.Data;
import sample.view.Earth;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private Label labelAnnee;

    @FXML
    private Pane pane3D;

    @FXML
    private TextField texteAnnee;

    @FXML
    private Slider sliderAnnee;

    @FXML
    private TextField textlatitude;

    @FXML
    private TextField textlongitude;

    @FXML
    private Button annimation;

    @FXML
    private ComboBox vitesse;

    @FXML
    private Label legende1;

    @FXML
    private Label legende2;

    @FXML
    private Label legende3;

    @FXML
    private Label legende4;

    @FXML
    private Label legende5;

    @FXML
    private Label legende6;

    @FXML
    private ToggleGroup mode;

    @FXML
    private LineChart graph;

    private  Data data;
    private float vitesseAnimation;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        data = new Data("src/sample/data/data.csv");
        data.setLabel(labelAnnee);
        data.setSlider(sliderAnnee);
        data.setTextField(texteAnnee);
        data.setEarth(new Earth(pane3D, this));
        data.setGraph(graph);

        sliderAnnee.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                data.setAnnee(newValue.intValue()+"");
            }
        });

        texteAnnee.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    data.setAnnee(texteAnnee.getText());
                }
            }
        });

        mode.selectedToggleProperty().addListener(new javafx.beans.value.ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                data.changeMode(((RadioButton) newValue).getText());
            }
        });

        final long startTime = System.nanoTime();
        vitesseAnimation = 1.f;
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(((now - startTime) / 1_000_000 % (vitesseAnimation*10)) == 0.){
                    data.nextYear();
                }

            }
        };

        annimation.setOnAction(new EventHandler(){
            private boolean state = false;
            @Override
            public void handle(Event event) {
                if (state){
                    state = false;
                    animationTimer.stop();
                }else {
                    state = true;
                    animationTimer.start();
                }
            }
        });

        //initialisation combox
        ObservableList<String> itemsListeWiew = FXCollections.observableArrayList("x0.5","x1","x2");
        vitesse.getItems().addAll(itemsListeWiew);

        //event choix vitesse annimation
        vitesse.setOnAction(actionEvent ->
        {
            switch ((String) vitesse.getSelectionModel().getSelectedItem()){
                case "x0.5":
                    vitesseAnimation = 2f;
                    break;
                case "x1":
                    vitesseAnimation = 1.0f;
                    break;
                case  "x2":
                    vitesseAnimation = 0.5f;
                    break;
                }
        });


        textlatitude.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    data.modifGraph(Float.parseFloat(textlatitude.getText()), Float.parseFloat(textlongitude.getText()));
                }
            }
        });

        textlongitude.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    data.modifGraph(Float.parseFloat(textlatitude.getText()), Float.parseFloat(textlongitude.getText()));
                }
            }
        });

        float min = data.getMin();
        float max = data.getMax();
        float step = (max - min)/6;
        legende1.setText(step*2+" ; "+max);
        legende2.setText(step+" ; "+step*2);
        legende3.setText("0 ; "+step);
        legende4.setText("0 ; "+-step);
        legende5.setText(-step+" ; "+-step*2);
        legende6.setText(-step*2+" ; "+min);
    }

    public void setPosition(float latitude, float longitude){
        textlatitude.setText(latitude+"");
        textlongitude.setText(longitude+"");
        data.modifGraph(latitude,longitude);
    }
}
