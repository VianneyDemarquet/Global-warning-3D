package sample;

import javafx.fxml.Initializable;
import sample.data.Data;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Data d = new Data("src/sample/data/data.csv");
    }
}
