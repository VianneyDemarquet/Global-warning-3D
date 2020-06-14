package sample.data;

import java.util.Objects;

public class Position {
    private float latitude;
    private float longitude;

    public Position(float latitude, float longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Float.compare(position.latitude, latitude) == 0 &&
                Float.compare(position.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
