package com.codingeveryday.calcapp.domain.interfaces;

import androidx.lifecycle.LiveData;

public interface TransformationInterface {

    LiveData<String> getResult();

    Boolean checkNumber(String a, int base);

    void clear();

    void append(char c);

    void pop_back();

    void transformNS(String s, int baseSource, int baseDest);
}
