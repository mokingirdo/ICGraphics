package ru.nsu.fit.g16207.epanchintseva;

public class Function {

double f_Waves(double x, double y){
    double result;
    result = 1/(1+Math.pow(x-2.5, 4));
    result += 1/(1+Math.pow(y-2.5, 4));
    result += Math.sin(x*y)*0.3;
    return result;
   // return Math.sin(x) + x*y + Math.cos(y);
}

}
