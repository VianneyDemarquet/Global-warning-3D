package sample.data;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import sample.view.Earth;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Data implements IData {
    private HashMap<String, ArrayList<Temperature>> mapTemperature;
    private HashMap<Position, HashMap<String, Temperature>> mapPosition;
    private HashMap<Position, ArrayList<Temperature>> mapPositionList;
    private String selectAnnee = "2020";
    private ArrayList<Position> positions;
    private float min;
    private float max;
    private String firstYear;
    private Earth earth;
    private Label label;
    private Slider slider;
    private TextField textField;

    public Data(String file){
        mapTemperature = new HashMap<>();
        mapPosition = new HashMap<>();
        mapPositionList = new HashMap<>();
        positions = new ArrayList<>();
        initData(file);
    }


    public void setEarth(Earth earth) {
        this.earth = earth;
        earth.setStep((max-min)/6);//taille de chaque intervale de valeur pour la légende
        earth.initialisePosition(mapTemperature.get(selectAnnee));
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public void setSlider(Slider slider) {
        this.slider = slider;
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
            firstYear = data[2];
            //Read the file line by line
            while ((line = bufferedReader.readLine()) != null)
            {
                data = line.split(separator, -1);
                try {
                    p = new Position(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
                    positions.add(p);
                    HashMap<String, Temperature> lp = new HashMap<>();
                    ArrayList<Temperature> list = new ArrayList<>();
                    mapPosition.put(p,lp);
                    for (int i = 2; i < data.length; i++) {
                        float temp;
                        if (data[i].equals("NA")){
                            temp = Float.NaN;
                        }else{
                            temp = Float.parseFloat(data[i]);
                        }

                        if (temp < min){
                            min = temp;
                        }else if (temp > max){
                            max = temp;
                        }
                        ArrayList l = mapTemperature.get(first+i+"");
                        Temperature t = new Temperature(p,temp);
                        l.add(t);
                        lp.put(first+i+"",t);
                        list.add(t);
                    }
                    mapPositionList.put(p, list);
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
    public ArrayList<Temperature> setAnnee(String annee) {
        selectAnnee = annee;
        slider.adjustValue(Double.parseDouble(annee));
        textField.setText(annee);
        label.setText(annee);
        earth.afficheAnnee(mapTemperature.get(annee));
        return mapTemperature.get(annee);
    }

    /**
     * recherche d'une anomalie à une certaine position et pour une certaine année
     * @param latitude latitude de la position
     * @param longitude longitude de la position
     * @param annee l'anne souhaité
     * @return la temperature demandé
     */
    @Override
    public Temperature selectPositionAnnee(float latitude, float longitude, String annee) {
        Position pos = new Position(latitude, longitude);

        return mapPosition.get(pos).get(annee);
    }

    /**
     * récupére toute les variations pour une certaine position
     * @param latitude latitude de la position
     * @param longitude longitude de la position
     * @return les différente anomalie de température et leur année
     */
    @Override
    public ArrayList<Temperature> selectPosition(float latitude, float longitude) {
        Position p = new Position(latitude, longitude);
        return mapPositionList.get(p);
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

    public HashMap<String, ArrayList<Temperature>> getMapTemperature() {
        return mapTemperature;
    }
}
