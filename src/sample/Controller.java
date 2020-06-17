package sample;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TextField latitude;

    @FXML
    private TextField longitude;

    @FXML
    private Button annimation;

    @FXML
    private SplitMenuButton vitesse;

    Data data;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        data = new Data("src/sample/data/data.csv");
        data.setEarth(new Earth(pane3D));
        data.setLabel(labelAnnee);
        data.setSlider(sliderAnnee);
        data.setTextField(texteAnnee);

        sliderAnnee.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                data.setSelectAnnee(newValue.intValue()+"");
            }
        });

        texteAnnee.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    data.setSelectAnnee(texteAnnee.getText());
                }
            }
        });
    }
}
