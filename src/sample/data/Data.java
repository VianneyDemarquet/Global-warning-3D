package sample.data;

import javafx.geometry.Pos;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Data implements IData {
    private HashMap<String, ArrayList<Temperature>> mapTemperature;
    private HashMap<Position, HashMap<String, Temperature>> mapPosition;
    private String selectAnnee = "2020";
    private ArrayList<Position> positions;
    private float min;
    private float max;

    public Data(String file){
        mapTemperature = new HashMap<>();
        mapPosition = new HashMap<>();
        positions = new ArrayList<>();
        initData(file);
    }

    /**
     * charge les donnés depuis un fichier
     * @param file le fichier à charger au format CSV s'épareré par des virgule.
     */
    private void initData(String file){
        String line = "";
        String[] data = null;
        String separator = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        int first;
        Position p;

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(file), StandardCharsets.ISO_8859_1))
        {
            //Read the first line
            line = bufferedReader.readLine();
            line = line.replace("\"", "");
            //Get data from the line
            data = line.split(separator, -1);

            //ajoute chaque année
            for (int i = 2; i < data.length; i++) {
                ArrayList<Temperature> l = new ArrayList<Temperature>();
                mapTemperature.put(data[i], l);
            }
            first = Integer.parseInt(data[2])-2;
            //Read the file line by line
            while ((line = bufferedReader.readLine()) != null)
            {
                data = line.split(separator, -1);
                try {
                    p = new Position(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
                    positions.add(p);
                    HashMap<String, Temperature> lp = new HashMap<>();
                    mapPosition.put(p,lp);
                    for (int i = 2; i < data.length; i++) {
                        if (data[i].equals("NA")){
                            data[i] = "0";
                        }
                        float temp = Float.parseFloat(data[i]);
                        if (temp < min){
                            min = temp;
                        }else if (temp > max){
                            max = temp;
                        }
                        ArrayList l = mapTemperature.get(first+i+"");
                        Temperature t = new Temperature(p,temp);
                        l.add(t);
                        lp.put(first+i+"",t);
                    }
                }catch (Exception e){

                }

            }

        }
        catch (IOException exception)
        {
            System.err.println(exception);
        }
    }

    /**
     * selectionne l'année que l'on veux afficher
     * @param annee l'année souhaité
     * @return les différentes anomalie et leur position
     */
    @Override
    public ArrayList selectAnnee(String annee) {
        selectAnnee = annee;
        return mapTemperature.get(annee);
    }

    /**
     * recherche d'une anomalie à une certaine position et pour une certaine année
     * @param latitude latitude de la position
     * @param longitude longitude de la position
     * @return la temperature demandé
     */
    @Override
    public Temperature selectPositionAnnee(float latitude, float longitude, String annee) {
        Position pos = new Position(latitude, longitude);
        ArrayList<Temperature> temp = mapTemperature.get(annee);
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

    /**
     * récupére toute les variations pour une certaine position
     * @param latitude latitude de la position
     * @param longitude longitude de la position
     * @return les différente anomalie de température et leur année
     */
    @Override
    public HashMap<String, Temperature> selectPosition(float latitude, float longitude) {
        Position p = new Position(latitude, longitude);
        return mapPosition.get(p);
    }

    /**
     *
     * @return valeur maximal des anomalies de températures
     */
    public float getMax() {
        return max;
    }

    /**
     *
     * @return valeur minimal des anomalies de températures
     */
    public float getMin() {
        return min;
    }

    /**
     *
     * @return liste de toute les positions connues
     */
    public ArrayList<Position> getPositions() {
        return positions;
    }
}
