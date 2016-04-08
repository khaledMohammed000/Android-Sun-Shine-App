package app.com.example.com.sun_shine;

/**
 * Created by user-1 on 4/3/2016.
 */
public class WeatherModel {

    long dt;
    TempModel temp;
    double pressure;
    int humidity;
    WeatherArrayModel weather;
    double speed;
    int deg;
    int clouds;

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public TempModel getTemp() {
        return temp;
    }

    public void setTemp(TempModel temp) {
        this.temp = temp;
    }

    public WeatherArrayModel getWeather() {
        return weather;
    }

    public void setWeather(WeatherArrayModel weather) {
        this.weather = weather;
    }
}
