package com.codingeveryday.calcapp.domain.interfaces;

import androidx.lifecycle.LiveData;

public interface CalculationInterface {

    int SIN_ID = 1, COS_ID = 2, TG_ID = 3, CTG_ID = 4, SQRT_ID = 5;

    void appendDigit(char c);

    void setAccuracy(int value);

    LiveData<String> getSolution();

    LiveData<String> getExpr();

    void calculateValue(int d);

    Boolean checkExpr(int base);

    void parseExpr();

    void popBack();

    void clear();

    void openBracket(char openBracket);

    void addOperation(char operation);

    void addFunc(int funcId);

    void addAbsStick();

    void addDot();

    void addPi();

    void switchRadDeg();

    void clearSolution();

    void clearExpr();

    void setExpr(String newExpr);

    void updateExpr();
}
