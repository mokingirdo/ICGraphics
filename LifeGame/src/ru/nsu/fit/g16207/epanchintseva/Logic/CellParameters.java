package ru.nsu.fit.g16207.epanchintseva.Logic;

public class CellParameters {
    public static double LIVE_BEGIN = 2.0;
    public static double BIRTH_BEGIN = 2.3;
    public static double LIVE_END = 3.3;
    public static double BIRTH_END = 2.9;
    public static double FST_IMPACT = 1.0;
    public static double SND_IMPACT = 0.3;

    public static boolean stayAlive(Cell cell) {
        return (LIVE_BEGIN <= cell.getImpact()) && (cell.getImpact() <= LIVE_END);
    }

    static boolean newBorn(Cell cell) {
        return (BIRTH_BEGIN <= cell.getImpact()) && (cell.getImpact() <= BIRTH_END);
    }

    static boolean timeToDie(Cell cell) {
        return (LIVE_END < cell.getImpact()) || (cell.getImpact() < LIVE_BEGIN);
    }
}
