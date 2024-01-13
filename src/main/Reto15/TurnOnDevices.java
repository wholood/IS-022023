package main.Reto15;

public class TurnOnDevices {
    public static void main(String[] args) {
        turnOnDevice(new Lamp());
        turnOnDevice(new Computer());
        turnOnDevice(new CoffeMakerAdapter());
    }

    private static void turnOnDevice (Connectable device){
        device.turnOn();
        System.out.println(device.isOn());
    }
}

class Lamp extends Connectable{}
class Computer extends Connectable{}

class CoffeMakerAdapter extends Connectable{
    private CoffeMaker adaptee;
    public void turnOn(){
        adaptee.on();
    }
    public void turnOff(){
        adaptee.off();
    }
    public boolean isOn(){
        return !adaptee.isOff();
    }
}
class CoffeMaker {
    public void on();
    public void off();
    public boolean isOff();
}