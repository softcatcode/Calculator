package com.codingeveryday.calcapp.data;

import static com.codingeveryday.calcapp.data.CalculationImplementation.convert;
import static com.codingeveryday.calcapp.data.CalculationImplementation.correct;
import static com.codingeveryday.calcapp.data.CalculationImplementation.digits;
import static com.codingeveryday.calcapp.data.CalculationImplementation.find;
import static com.codingeveryday.calcapp.data.CalculationImplementation.hasSymbol;
import static com.codingeveryday.calcapp.data.CalculationImplementation.mul;
import static com.codingeveryday.calcapp.data.CalculationImplementation.sum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codingeveryday.calcapp.domain.interfaces.TransformationInterface;

import javax.inject.Inject;

public class TransformationImplementation implements TransformationInterface {

    private MutableLiveData<String> numberLD = new MutableLiveData<>();
    private String number = "";

    @Override
    public LiveData<String> getResult() {
        return numberLD;
    }

    @Override
    public Boolean checkNumber(String a, int base) {
        a = a.toUpperCase();
        if (a.equals(""))
            return false;
        for (int i = 0; i < a.length(); i++) {
            if (!hasSymbol(a.charAt(i), digits.substring(0, 36)))
                return false;
            else if (base <= find(a.charAt(i), digits))
                return false;
        }
        return true;
    }

    private void updateNumber() {
        numberLD.setValue(number);
    }

    @Override
    public void clear() {
        number = "";
        updateNumber();
    }

    @Override
    public void append(char c) {
        number += c;
        updateNumber();
    }

    @Override
    public void pop_back() {
        if (number.length() > 0)
            number = number.substring(0, number.length() - 1);
        updateNumber();
    }

    @Override
    public void transformNS(String s, int baseSource, int baseDest) {
        if (!checkNumber(s, baseSource))
            number = "invalid number";
        else
            number = toOtherNumberSystem(s, baseSource, baseDest);
        updateNumber();
    }

    public static String toOtherNumberSystem(String str, int d1, int d2) {
        str = str.toUpperCase();
        if (d1 == d2)
            return str;
        char z = '+';
        if (str.charAt(0) == '-'){
            str = str.substring(1);
            z = '-';
        }
        String fp = "0";
        if (hasSymbol('.', str)) {
            fp = "0" + str.substring(find('.', str));
            str = str.substring(0, find('.', str));
        }
        String num1 = convert(d1, d2), res = "0", k = "1";
        while (str.length() > 0)
        {
            String nextDigit = str.substring(str.length() - 1);
            nextDigit = convert(find(nextDigit.charAt(0), digits), d2);
            res = sum(res, mul(nextDigit, k, d2), d2);
            str = str.substring(0, str.length() - 1);
            k = mul(k, num1, d2);
        }
        if (fp.equals("0")) {
            if (z == '-')
                res = '-' + res;
            return res;
        }
        res += '.';
        String num2 = convert(d2, d1);
        int n = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(res);
        while (!fp.equals("0") && n < CalculationImplementation.accuracy) {
            fp = mul(fp, num2, d1);
            if (!hasSymbol('.', fp))
                fp += ".0";
            String ip = fp.substring(0, find('.', fp));
            k = "1";
            String s = "0";
            while (ip.length() > 0){
                String nextDigit = ip.substring(ip.length() - 1);
                nextDigit = convert(find(nextDigit.charAt(0), digits), d2);
                s = sum(s, mul(nextDigit, k, d2), d2);
                ip = ip.substring(0, ip.length() - 1);
                k = mul(k, num1, d2);
            }
            fp = correct('0' + fp.substring(find('.', fp)));
            sb.append(s);
            ++n;
        }
        res = sb.toString();
        if (z == '-' && !res.equals("0"))
            res = '-' + res;
        return res;
    }
}
