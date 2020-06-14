package sample.data;

import java.util.ArrayList;

public interface IData {
    public void addData(String[] data);
    public ArrayList selectAnnee(String annee);
    public Temperature selectPosition(float latitude, float longitude);
}
