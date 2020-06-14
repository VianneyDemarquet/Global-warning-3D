package sample.data;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Data implements IData {
    private HashMap<String, ArrayList<Temperature> mapTemperature;
    private String selectAnnee;

    public Data(){
        mapTemperature = new HashMap<>();
    }
    @Override
    public void addData(String[] data) {

    }

    private void addAnnee(){

    }

    @Override
    public ArrayList selectAnnee(String annee) {
        selectAnnee = annee;
        return mapTemperature.get(annee);
    }

    @Override
    public Temperature selectPosition(float latitude, float longitude) {
        Position pos = new Position(latitude, longitude);
        ArrayList<Temperature> temp = mapTemperature.get(selectAnnee);
        Iterator<Temperature> i = temp.iterator();
        Temperature t;
        while (i.hasNext()){
            t = i.next();
            if (t.inPosition(pos)){
                return t;
            }
        }
        return null;
    }
}
