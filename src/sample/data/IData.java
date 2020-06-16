package sample.data;

import java.util.ArrayList;

public interface IData {
    public ArrayList<Temperature> selectAnnee(String annee);
    public Temperature selectPositionAnnee(float latitude, float longitude, String annee);
    public ArrayList<Temperature> selectPosition(float latitude, float longitude);
}
