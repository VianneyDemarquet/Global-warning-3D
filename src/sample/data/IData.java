package sample.data;

import java.util.ArrayList;
import java.util.HashMap;

public interface IData {
    public ArrayList<Temperature> selectAnnee(String annee);
    public Temperature selectPositionAnnee(float latitude, float longitude, String annee);
    public HashMap<String, Temperature> selectPosition(float latitude, float longitude);
}
