package sample.data;

public class Temperature {
    private Position position;
    private float temperature;

    public Temperature(Position pos, float temp){
        position = pos;
        temperature = temp;
    }

    public float getTemperature() {
         return temperature;
    }

    public boolean inPosition(Position p){
        return position.equals(p);
    }
}
