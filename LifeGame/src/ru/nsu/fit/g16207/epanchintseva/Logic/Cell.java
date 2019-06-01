package ru.nsu.fit.g16207.epanchintseva.Logic;

public class Cell {
    private double impact;
    private boolean isAlive;

    Cell(){
        impact = 0;
        isAlive = false;
    }

    void setImpact(double impact){
        this.impact = impact;
    }

    public double getImpact() {
        return impact;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive() {
        isAlive = true;
    }

    public void setDeath() {
        isAlive = false;
    }
}
