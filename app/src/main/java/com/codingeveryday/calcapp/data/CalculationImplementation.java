package com.codingeveryday.calcapp.data;

import static com.codingeveryday.calcapp.data.TransformationImplementation.toOtherNumberSystem;
import static java.lang.Math.abs;
import static java.lang.Math.max;

import androidx.annotation.UiThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codingeveryday.calcapp.CalculatorApplication;
import com.codingeveryday.calcapp.R;
import com.codingeveryday.calcapp.domain.interfaces.CalculationInterface;
import com.codingeveryday.calcapp.presentation.MainActivity;

import java.util.Stack;

public class CalculationImplementation implements CalculationInterface {

    public static final String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ.,π";
    public static int accuracy = 10;
    public final static String PI = "3.1415926535897932384626433832795"; // 0288419716939937510582097494459230781640628620899862803482534211706798
    public final static String E = "0.000000001";
    private int number = 1;
    private String solution = "";
    private StringBuilder solutionBuilder;
    private final MutableLiveData<String> solutionLD = new MutableLiveData<>();
    private String expr = "";
    private final MutableLiveData<String> exprLD = new MutableLiveData<>();
    private static Boolean radCalc = true;

    @Override
    public void appendDigit(char c) {
        expr += c + "";
        updateExpr();
    }

    @Override
    public void setAccuracy(int value) {
        accuracy = value;
    }

    @Override
    public LiveData<String> getSolution() {
        return solutionLD;
    }

    @Override
    public LiveData<String> getExpr() {
        return exprLD;
    }

    public void updateSolution() {
        solutionLD.setValue(solution);
    }

    @Override
    public void updateExpr() {
        exprLD.setValue(expr);
    }

    public static boolean hasSymbol(char c, String str) {
        for (int i = 0; i < str.length(); i++)
            if (c == str.charAt(i))
                return true;
        return false;
    }

    public static int find(char sim, final String str) {
        int result = 0;
        while (result != str.length() && str.charAt(result) != sim)
            result++;
        if (result == str.length())
            return -1;
        else
            return result;
    }

    public static String correct(String num) {
        if (num.length() == 0)
            return null;
        char z = '+';
        if (num.charAt(0) == '-'){
            z = '-';
            num = num.substring(1);
        }
        if (hasSymbol('.', num))
            while(num.charAt(num.length() - 1) == '0')
                num = num.substring(0, num.length() - 1);
        if (num.charAt(num.length() - 1) == '.')
            num = num.substring(0, num.length() - 1);
        while (num.length() > 0 && num.charAt(0) == '0')
            num = num.substring(1);
        if (num.equals("") || num.charAt(0) == '.')
            num = '0' + num;
        if (z == '-' && !num.equals("0"))
            num = '-' + num;
        return num;
    }

    public static int cmpAbs(String a, String b) {
        if (a.charAt(0) == '-')
            a = a.substring(1);
        if (b.charAt(0) == '-')
            b = b.substring(1);
        if (!hasSymbol('.', a))
            a += '.';
        if (!hasSymbol('.', b))
            b += '.';
        while (find('.', a) < find('.', b))
            a = '0' + a;
        while (find('.', a) > find('.', b))
            b = '0' + b;

        StringBuilder tmp = new StringBuilder();
        tmp.append(a);
        int n = b.length() - a.length();
        for (int i = 0; i < n; i++)
            tmp.append('0');
        a = tmp.toString();

        tmp = new StringBuilder();
        tmp.append(b);
        n = a.length() - b.length();
        for (int i = 0; i < n; i++)
            tmp.append('0');
        b = tmp.toString();

        int i = 0;
        while (i != a.length() && a.charAt(i) == b.charAt(i))
            ++i;
        if (i == a.length())
            return 0;
        if (a.charAt(i) > b.charAt(i))
            return 1;
        return -1;
    }

    public static String sum(String a, String b, int d) {
        char z1 = '+', z2 = '+';
        if (a.charAt(0) == '-'){
            a = a.substring(1);
            z1 = '-';
        }
        if (b.charAt(0) == '-') {
            b = b.substring(1);
            z2 = '-';
        }

        //alignment
        if (!hasSymbol('.', a))
            a += '.';
        if (!hasSymbol('.', b))
            b += '.';
        StringBuilder sb = new StringBuilder();
        int n = find('.', a) - find('.', b);
        for (int i = 0; i < n; i++)
            sb.append('0');
        b = sb + b;
        sb = new StringBuilder();
        n = find('.', b) - find('.', a);
        for (int i = 0; i < n; i++)
            sb.append('0');
        a = sb + a;

        sb = new StringBuilder();
        n = a.length() - b.length();
        sb.append(b);
        for (int i = 0; i < n; i++)
            sb.append('0');
        b = sb.toString();

        sb = new StringBuilder();
        n = b.length() - a.length();
        sb.append(a);
        for (int i = 0; i < n; i++)
            sb.append('0');
        a = sb.toString();

        sb = new StringBuilder();
        int buf = 0;
        if (z1 == z2)
        {
            while (a.length() > 0)
            {
                if (a.charAt(a.length() - 1) == '.') {
                    a = a.substring(0, a.length() - 1);
                    b = b.substring(0, b.length() - 1);
                    sb.append('.');
                    continue;
                }
                char x = a.charAt(a.length() - 1);
                char y = b.charAt(b.length() - 1);
                a = a.substring(0, a.length() - 1);
                b = b.substring(0, b.length() - 1);
                buf += find(x, digits) + find(y, digits);
                sb.append(digits.charAt(buf % d));
                buf /= d;
            }
            sb.append(digits.charAt(buf));
            if (z1 == '-')
                sb.append('-');
            return correct(sb.reverse().toString());
        }
        else
        {
            if (cmpAbs(a, b) < 0) {
                String str = a;
                a = b;
                b = str;
                char tmp = z1;
                z1 = z2;
                z2 = tmp;
            }
            while (a.length() > 0) {
                if (a.charAt(a.length() - 1) == '.') {
                    a = a.substring(0, a.length() - 1);
                    b = b.substring(0, b.length() - 1);
                    sb.append('.');
                    continue;
                }
                char x = a.charAt(a.length() - 1);
                char y = b.charAt(b.length() - 1);
                a = a.substring(0, a.length() - 1);
                b = b.substring(0, b.length() - 1);
                buf = find(x, digits) - find(y, digits);
                if (buf < 0) {
                    buf = d + find(x, digits) - find(y, digits);
                    int i = a.length() - 1;
                    while (a.charAt(i) == '0' || a.charAt(i) == '.') {
                        if (a.charAt(i) != '.')
                            a = a.substring(0, i) + digits.charAt(d - 1) + a.substring(i + 1);
                        --i;
                    }
                    char num = a.charAt(i);
                    num = digits.charAt( find(num, digits) - 1 );
                    a = a.substring(0, i) + num + a.substring(i + 1);
                }
                sb.append(digits.charAt(buf));
            }
            if (z1 == '-')
                sb.append('-');
            return correct(sb.reverse().toString());
        }
    }

    public static String divBase(String a, int n) {
        if (n < 0)
            return mulBase(a, -n);
        char z = '+';
        if (a.charAt(0) == '-') {
            z = '-';
            a = a.substring(1);
        }
        if (!hasSymbol('.', a))
            a += ".";
        int index = find('.', a);
        String ip = a.substring(0, index);
        String fp = a.substring(index + 1);
        String buf = "";
        int k = n - ip.length();
        for (int i = 0; i <= k; i++)
            buf += '0';
        ip = buf + ip;
        k = ip.length() - n;
        String result = "";
        if (z == '-')
            result += '-';
        result += ip.substring(0, k);
        result += '.';
        result += ip.substring(k);
        result += fp;
        return correct(result);
    }

    public static String mulBase(String a, int n) {
        if (n < 0)
            return divBase(a, -n);
        if (!hasSymbol('.', a))
            a += ".";
        int index = find('.', a);
        String ip = a.substring(0, index);
        String fp = a.substring(index + 1);
        int k = n - fp.length();
        for (int i = 0; i < k; i++)
            fp += '0';
        String result = ip;
        result += fp.substring(0, n);
        result += '.';
        result += fp.substring(n);
        return correct(result);
    }

    static String intMul(String a, String b, int d) {
        String result = "0";
        int buf = 0, n = 0;
        b = '0' + b;
        for (int i = a.length() - 1; i >= 0; --i){
            char x = a.charAt(i);
            StringBuilder num = new StringBuilder();
            for (int j = b.length() - 1; j >= 0; --j) {
                char y = b.charAt(j);
                buf += (y - '0') * (x - '0');
                num.append(digits.charAt(buf % d));
                buf /= d;
            }
            num.reverse();
            for (int j = 0; j < n; ++j)
                num.append('0');
            ++n;
            result = sum(result, num.toString(), d);
        }
        return correct(result);
    }

    public static String mul(String a, String b, int d) {
        a = correct(a);
        b = correct(b);
        char z1 = '+', z2 = '+';
        if (a.charAt(0) == '-') {
            a = a.substring(1);
            z1 = '-';
        }
        if (b.charAt(0) == '-') {
            b = b.substring(1);
            z2 = '-';
        }
        int fp = 0;
        if (!hasSymbol('.', a))
            a += '.';
        fp += a.length() - find('.', a) - 1;
        if (!hasSymbol('.', b))
            b += '.';
        fp += b.length() - find('.', b) - 1;
        int index = find('.', a);
        a = a.substring(0, index) + a.substring(index + 1);
        index = find('.', b);
        b = b.substring(0, index) + b.substring(index + 1);
        if (a.length() + b.length() < 30) {
            String result = intMul(a, b, d);
            result = divBase(result, fp);
            if (z1 != z2)
                result = '-' + result;
            return result;
        }
        if (a.length() < b.length()) {
            int k = b.length() - a.length();
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < k; i++)
                buf.append('0');
            buf.append(a);
            a = buf.toString();
        }
        if (b.length() < a.length()) {
            int k = a.length() - b.length();
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < k; i++)
                buf.append('0');
            buf.append(b);
            b = buf.toString();
        }
        int n = max(a.length(), b.length());
        n /= 2;
        String a1 = a.substring(0, a.length() - n);
        String b1 = b.substring(0, b.length() - n);
        String a0 = a.substring(a.length() - n);
        String b0 = b.substring(b.length() - n);
        String x = mul(a1, b1, d);
        String y = mul(a0, b0, d);
        String z = mul( sum(a0, a1, d), sum(b0, b1, d), d );
        String c = sum( z, '-' + sum(x, y, d), d );
        x = mulBase(x, 2 * n);
        c = mulBase(c, n);
        String result = sum( x, sum(c, y, d), d );
        result = divBase(result, fp);
        if (z1 != z2)
            result = '-' + result;
        return result;
    }

    public static boolean isZero(final String s) {
        for (int i = 0; i < s.length(); i++) {
            char sim = s.charAt(i);
            if (sim != '.' && sim != '0')
                return false;
        }
        return true;
    }

    public static String div(String a, String b, int d) throws ArithmeticException {
        a = correct(a);
        b = correct(b);
        if (b.equals("0"))
            throw new ArithmeticException("Division by zero occurred");
        char sgn_a = '+', sgn_b = '+';
        if (a.charAt(0) == '-'){
            sgn_a = '-';
            a = a.substring(1);
        }
        if (b.charAt(0) == '-'){
            sgn_b = '-';
            b = b.substring(1);
        }
        String result = "0";
        int i = 0, j = 0;
        while (i < a.length() && a.charAt(i) != '.')
            i++;
        while (j < b.length() && b.charAt(j) != '.')
            j++;
        String delta = divBase("1", accuracy + 2);
        if (cmpAbs(a, delta) == -1)
            delta = a;
        int basePower = i - j - 1;
        String decrement = mulBase(b, basePower);
        String resultIncrement = mulBase("1", basePower);
        while (cmpAbs(a, delta) != -1) {
            while (cmpAbs(a, decrement) != -1) {
                result = sum(result, resultIncrement, d);
                a = sum(a, '-' + decrement, d);
            }
            decrement = divBase(decrement, 1);
            resultIncrement = divBase(resultIncrement, 1);
        }
        if (sgn_a != sgn_b)
            result = '-' + result;
        return result;
        /*if (z1 != z2)
            result = '-' + result;
        if (!hasSymbol('.', result) || result.length() - find('.', result) - 1 <= accuracy)
            return result;
        char lastDigit = result.charAt(result.length() - 1);
        if (find(lastDigit, digits) >= d / 2) {
            i = result.length() - 2;
            while (result.charAt(i) != '.' && find(result.charAt(i), digits) == d - 1)
                i--;
            if (result.charAt(i) == '.') {
                result = result.substring(0, i);
                result = sum(result, "1", d);
            }
            else {
                lastDigit = result.charAt(i);
                lastDigit += 1;
                result = result.substring(0, i) + lastDigit;
            }
        }
        return correct(result);*/
    }

    public static String factorial(String a, int d) {
        a = correct(a);
        if (hasSymbol('.', a))
            return "error";
        String result = "1";
        while (!a.equals("0")){
            result = mul(result, a, d);
            a = sum(a, "-1", d);
        }
        return result;
    }

    public static String pow(String a, String b, int d) throws ArithmeticException {
        if (b.equals("0")){
            if (a.equals("0"))
                return "error";
            return "1";
        }
        if (a.equals("0") && b.charAt(0) == '-')
            return "error";
        if (a.equals("0"))
            return "0";
        if (b.charAt(0) == '-') {
            String ans = "";
            int tmp = accuracy;
            accuracy = 100;
            try {
                ans = div("1", pow(a, b.substring(1), d), d);
            } catch (Exception ignored) { }
            accuracy = tmp;
            return ans;
        }
        if (a.equals("1"))
            return "1";
        if (find(b.charAt(b.length() - 1), digits) % 2 == 1)
            return mul(pow(a, sum(b, "-1", d), d), a, d);
        String x = "2";
        if (d == 2)
            x = "10";
        try{ x = div(b, x, d); } catch (Exception ignored) {}
        String ans =  pow(a, x, d);
        return mul(ans, ans, d);
    }

    public static String sqrt(String x, int d) {
        int buf = accuracy;
        accuracy = 10;
        x = correct(x);
        if (x.charAt(0) == '-')
            return "error";
        if (hasSymbol('.', x)) {
            int index = find('.', x);
            int n = x.length() - index - 1;
            if (n > accuracy)
                n = accuracy;
            x = x.substring(0, index) + x.substring( index + 1, index + 1 + n );
            if (n % 2 == 1) {
                n++;
                x += '0';
            }
            x = round(sqrt(x, d), d);
            if (!hasSymbol('.', x))
                x += '.';
            x = divBase(x, n / 2);
            return x;
        }
        String r = x, l = "0", num = div("1", sum("1", "1", d), d);
        if (cmpAbs(x, "1") == -1)
            r = "1";
        while (cmpAbs(sum(r, '-' + l, d), E) > -1) {
            String m = sum(r, l, d);
            m = mul(m, num, d);
            String res = mul(m, m, d);
            if (cmpAbs(res, x) < 1)
                l = m;
            else
                r = m;
        }
        accuracy = buf;
        return l;
    }

    public static String round(String a, int d) {
        char z = '+';
        if (a.charAt(0) == '-'){
            z = '-';
            a = a.substring(1, a.length());
        }
        if (!hasSymbol('.', a))
            a += '.';
        String ip = a.substring(0, find('.', a));
        String num = sum("1", ip, d);
        if (cmpAbs(sum(num, '-' + a, d), E) <= 0) {
            if (z == '-')
                return '-' + num;
            return num;
        }
        if (cmpAbs(sum(a, '-' + ip, d), E) <= 0) {
            if (z == '+')
                return ip;
            return '-' + ip;
        }
        if (z == '-')
            return '-' + a;
        return a;
    }

    public static String applyPeriod(String a, String t, int d) {
        char z = '+';
        if (a.charAt(0) == '-') {
            z = '-';
            a = a.substring(1);
        }
        if (cmpAbs(a, t) == -1)
            return (z == '-' ? '-' + a : a);
        t = '-' + t;
        int n = 0;
        while (cmpAbs(a, t) == 1) {
            t = mulBase(t, 1);
            n++;
        }
        if (cmpAbs(a, t) == -1) {
            t = divBase(t, 1);
            n--;
        }
        while (n >= 0) {
            while (cmpAbs(a, t) > -1)
                a = sum(a, t, d);
            t = divBase(t, 1);
            n--;
        }
        if (z == '-')
            a = '-' + a;
        return a;
    }

    public static String convert(int a, int d) {
        String res = "";
        while (a > 0){
            res = digits.charAt(a % d) + res;
            a /= d;
        }
        if (res.equals(""))
            res = "0";
        return res;
    }

    public boolean corBracketSeq(final String str) {
        int b = 0;
        String s = "";
        for (int i = 0; i < str.length(); ++i)
        {
            char sim = str.charAt(i);
            if (hasSymbol(sim, "([{<")) {
                b++;
                s += sim;
            }
            else if (hasSymbol(sim, ")]}>")){
                b--;
                if (s.length() == 0)
                    return false;
                char prev = s.charAt(s.length() - 1);
                if (!(prev == '(' && sim == ')' || prev == '[' && sim == ']' || prev == '{' && sim == '}' || prev == '<' && sim == '>'))
                    return false;
                else{
                    s = s.substring(0, s.length() - 1);
                }
            }
            if (b < 0)
                return false;
        }
        return b == 0 && s.length() == 0;
    }

    @Override
    public Boolean checkExpr(int base) {
        if (expr.equals(""))
            return false;
        for (int i = 0; i < expr.length(); ++i) {
            if (!hasSymbol(expr.charAt(i), digits + "<>{}[]()-+:|/*×!^√stcgino"))
                return false;
            else if (hasSymbol(expr.charAt(i), digits.substring(0, 36)) && base <= find(expr.charAt(i), digits))
                return false;
        }
        if (!corBracketSeq(expr))
            return false;
        return true;
    }

    @Override
    public void parseExpr() {
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < expr.length(); ++i) {
            if (expr.charAt(i) == ',')
                tmp.append('.');
            else if (hasSymbol(expr.charAt(i), digits + "<>[]{}()+-×/!^|π√stingoc"))
                tmp.append(expr.charAt(i));
            else if (expr.charAt(i) == ':')
                tmp.append('/');
            else if (expr.charAt(i) == '*')
                tmp.append('×');
        }
        String s = tmp.toString();
        tmp = new StringBuilder();
        if (s.length() > 0 && s.charAt(0) == '|')
            s = '<' + s.substring(1);
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '|') {
                if (!hasSymbol(s.charAt(i - 1), digits + ">)]}!"))
                    tmp.append('<');
                else
                    tmp.append('>');
            } else
                tmp.append(s.charAt(i));
        }
        expr = tmp.toString();
    }

    public static String sin(String a, int d) {
        if (!radCalc)
            a = toRadians(a, d);
        a = toOtherNumberSystem(correct(a), d, 10);
        a = applyPeriod(a, mul("2", PI, 10), 10);
        double val = Double.valueOf(a);
        val = Math.sin(val);
        if (abs(val - 0.5) <= 1e-10)
            val = 0.5;
        else if (abs(val + 0.5) <= 1e-10)
            val = -0.5;
        else if (abs(val) <= 1e-10)
            val = 0;
        String ans =  correct(String.valueOf(val));
        if (hasSymbol('E', ans)) {
            int p = find('E', ans);
            int n = abs(Integer.valueOf(ans.substring(p + 1, ans.length())));
            ans = ans.substring(0, p);
            ans = divBase(ans, n);
        }
        ans = toOtherNumberSystem(ans, 10, d);
        return ans;
    }

    public static String cos(String a, int d) {
        if (!radCalc)
            a = toRadians(a, d);
        a = toOtherNumberSystem(correct(a), d, 10);
        if (a.charAt(0) == '-')
            a = a.substring(1);
        a = applyPeriod(a, mul("2", PI, 10), 10);
        double val = Double.valueOf(a);
        val = Math.cos(val);
        if (abs(val) <= 1e-10)
            val = 0;
        else if (abs(val - 0.5) <= 1e-10)
            val = 0.5;
        else if (abs(val + 0.5) <= 1e-10)
            val = -0.5;
        String ans = correct(String.valueOf(val));
        if (hasSymbol('E', ans)) {
            int p = find('E', ans);
            int n = abs(Integer.valueOf(ans.substring(p + 1)));
            ans = ans.substring(0, p);
            ans = divBase(ans, n);
        }
        return toOtherNumberSystem(ans, 10, d);
    }

    public static String tg(String a, int d) {
        try {
            return div(sin(a, d), cos(a, d), d);
        } catch (Exception e){
            return "error";
        }
    }

    public static String ctg(String a, int d) {
        try {
            return div(cos(a, d), sin(a, d), d);
        } catch (Exception e){
            return "error";
        }
    }

    public String value(String s, int d) {
        while (hasSymbol('π', s)) {
            int pos = find('π', s);
            s = s.substring(0, pos) + toOtherNumberSystem(PI, 10, d) + s.substring(pos + 1);
        }
        while (hasSymbol('(', s)) {
            int beg = find('(', s), b = 1, end = beg;
            while (b != 0) {
                end++;
                if (s.charAt(end) == '(')
                    ++b;
                else if (s.charAt(end) == ')')
                    --b;
            }
            String num = s.substring(beg + 1, end);
            String val = value(num, d);
            if (beg > 0 && s.charAt(beg - 1) == 'n') {
                String buf = sin(val, d);
                s = s.substring(0, beg - 3) + buf + s.substring(end + 1);
                solutionBuilder.append(number).append(") sin(").append(val).append(") = ").append(buf).append('\n');
                number++;
            }
            else if (beg > 0 && s.charAt(beg - 1) == 's') {
                String buf = cos(val, d);
                s = s.substring(0, beg - 3) + buf + s.substring(end + 1);
                solutionBuilder.append(number).append(") cos(").append(val).append(") = ").append(buf).append('\n');
                number++;
            }
            else if (beg > 2 && s.substring(beg - 3, beg).equals("ctg")) {
                String buf = round(ctg(val, d), d);
                s = s.substring(0, beg - 3) + buf + s.substring(end + 1);
                solutionBuilder.append(number).append(") ctg(").append(val).append(") = ").append(buf).append('\n');
                number++;
            }
            else if (beg > 1 && s.substring(beg - 2, beg).equals("tg")) {
                String buf = round(tg(val, d), d);
                s = s.substring(0, beg - 2) + round(tg(val, d), d) + s.substring(end + 1);
                solutionBuilder.append(number).append(") tg(").append(val).append(") = ").append(buf).append('\n');
                number++;
            }
            else
                s = s.substring(0, beg) + val + s.substring(end + 1);
        }
        while (hasSymbol('[', s)) {
            int beg = find('[', s), b = 1, end = beg;
            while (b != 0) {
                end++;
                if (s.charAt(end) == '[')
                    ++b;
                else if (s.charAt(end) == ']')
                    --b;
            }
            String num = s.substring(beg + 1, end);
            String val = value(num, d);
            solutionBuilder.append(number).append(") ").append('[').append(val).append("] = ");
            if (hasSymbol('.', val)) {
                if (val.charAt(0) != '-')
                    val = val.substring(0, find('.', val));
                else
                    val = sum(val.substring(0, find('.', val)), "-1", d);
            }
            solutionBuilder.append(val).append('\n');
            s = s.substring(0, beg) + val + s.substring(end + 1);
            number++;
        }
        while (hasSymbol('{', s)) {
            int beg = find('{', s), b = 1, end = beg;
            while (b != 0) {
                end++;
                if (s.charAt(end) == '{')
                    ++b;
                else if (s.charAt(end) == '}')
                    --b;
            }
            String num = s.substring(beg + 1, end);
            String val = value(num, d);
            solutionBuilder.append(number).append(") ").append('{').append(val).append("} = ");
            if (!hasSymbol('.', val))
                val = "0";
            else if (val.charAt(0) != '-')
                val = "0." + val.substring(find('.', val) + 1);
            else {
                String intPart = sum(val.substring(0, find('.', val)), "-1", d);
                val = sum(val, intPart.substring(1), d);
            }
            solutionBuilder.append(val).append('\n');
            s = s.substring(0, beg) + val + s.substring(end + 1);
            number++;
        }
        while (hasSymbol('<', s)) {
            int beg = find('<', s), b = 1, end = beg;
            while (b != 0) {
                end++;
                if (s.charAt(end) == '<')
                    ++b;
                else if (s.charAt(end) == '>')
                    --b;
            }
            String num = s.substring(beg + 1, end);
            String res = value(num, d);
            if (res.charAt(0) == '-')
                res = res.substring(1);
            s = s.substring(0, beg) + res + s.substring(end + 1);
        }
        while (hasSymbol('!', s)) {
            int end = find('!', s), beg = end;
            while (beg > 0 && hasSymbol(s.charAt(beg - 1), digits))
                beg--;
            String num = factorial(s.substring(beg, end), d);
            solutionBuilder.append(number).append(") ").append(s.substring(beg, end + 1)).append(" = ").append(num).append('\n');
            s = s.substring(0, beg) + num + s.substring(end + 1);
            number++;
        }
        while (hasSymbol('^', s)) {
            int pos = find('^', s), beg = pos, end = beg;
            while (beg > 0 && hasSymbol(s.charAt(beg - 1), digits))
                beg--;
            if (beg == 1 && s.charAt(0) == '-')
                beg--;
            if (s.charAt(end + 1) == '-')
                ++end;
            while (end < s.length() - 1 && hasSymbol(s.charAt(end + 1), digits))
                end++;
            solutionBuilder.append(number).append(") ").append(s.substring(beg, end + 1)).append(" = ");
            String a = s.substring(beg, pos);
            String b = s.substring(find('^', s) + 1, end + 1);
            if (hasSymbol('.', b))
                throw new ArithmeticException("cannot pow to fractional power");
            String num = pow(a, b, d);
            solutionBuilder.append(num).append('\n');
            s = s.substring(0, beg) + num + s.substring(end + 1);
            number++;
        }
        while (hasSymbol('√', s)) {
            int i = 0;
            while (s.charAt(i) != '√')
                i++;
            int j = i + 1;
            if (s.charAt(i + 1) == '-' || s.charAt(i + 1) == '+')
                j++;
            while (j < s.length() && hasSymbol(s.charAt(j), digits))
                j++;
            String num = s.substring(i, j);
            int index = 0;
            while (!hasSymbol(num.charAt(index), digits) && num.charAt(index) != '-')
                index++;
            num = num.substring(index);
            String val = round(sqrt(num, d), d);
            solutionBuilder.append(number).append(") √(").append(num).append(") = ").append(val).append('\n');
            s = s.substring(0, i) + val + s.substring(j);
            number++;
        }
        while (hasSymbol('×', s) || hasSymbol('/', s)) {
            int pos = 0;
            while (s.charAt(pos) != '×' && s.charAt(pos) != '/')
                ++pos;
            int beg = pos, end = pos;
            while (beg > 0 && hasSymbol(s.charAt(beg - 1), digits))
                --beg;
            if (beg == 1 && s.charAt(0) == '-')
                --beg;
            if (s.charAt(end + 1) == '-')
                ++end;
            while (end < s.length() - 1 && hasSymbol(s.charAt(end + 1), digits))
                ++end;
            String a = s.substring(beg, pos);
            String b = s.substring(pos + 1, end + 1);
            String num;
            a = correct(a);
            b = correct(b);
            if (s.charAt(pos) == '×')
                num = mul(a, b, d);
            else
                num = div(a, b, d);
            if (b.charAt(0) == '-')
                solutionBuilder.append(number).append(") ").append(a).append(" ").append(s.charAt(pos)).append(" (").append(b).append(") = ").append(num).append('\n');
            else
                solutionBuilder.append(number).append(") ").append(a).append(" ").append(s.charAt(pos)).append(" ").append(b).append(" = ").append(num).append('\n');
            s = s.substring(0, beg) + num + s.substring(end + 1);
            number++;
        }
        while (hasSymbol('+', s) || hasSymbol('-', s.substring(1))) {
            int pos = 1;
            while (s.charAt(pos) != '+' && s.charAt(pos) != '-')
                ++pos;
            int beg = pos, end = pos;
            while (beg > 0 && hasSymbol(s.charAt(beg - 1), digits))
                --beg;
            if (beg == 1 && s.charAt(0) == '-')
                --beg;
            if (s.charAt(end + 1) == '-')
                ++end;
            while (end < s.length() - 1 && hasSymbol(s.charAt(end + 1), digits))
                ++end;
            String a = s.substring(beg, pos);
            String b = s.substring(pos + 1, end + 1);
            String num;
            if (s.charAt(pos) == '-')
            {
                if (b.charAt(0) == '-') {
                    solutionBuilder.append(number).append(") ").append(a).append(" - (").append(b).append(") = ");
                    b = b.substring(1);
                }
                else {
                    solutionBuilder.append(number).append(") ").append(a).append(" - ").append(b).append(" = ");
                    b = '-' + b;
                }
            }
            else {
                if (b.charAt(0) == '-')
                    solutionBuilder.append(number).append(") ").append(a).append(" + (").append(b).append(") = ");
                else
                    solutionBuilder.append(number).append(") ").append(a).append(" + ").append(b).append(" = ");
            }
            num = sum(a, b, d);
            solutionBuilder.append(num).append('\n');
            s = s.substring(0, beg) + num + s.substring(end + 1);
            number++;
        }
        return correct(s);
    }

    public static String toRadians(final String a, int d)
    {
        String ans = "";
        try {
            ans = div(
                    mul(a, toOtherNumberSystem(PI, 10, d), d),
                    toOtherNumberSystem("180", 10, d),
                    d
            );
        } catch(Exception ignored) {}
        return ans;
    }

    @Override
    public void calculateValue(int d) {
        runCalcProcess(d);
        solutionLD.setValue(solution);
        exprLD.setValue(expr);
    }

    public void runCalcProcess(int d) {
        solutionBuilder = new StringBuilder();
        try {
            expr = value(expr, d);
        } catch (Exception e) {
            solution = "";
            return;
        }
        solution = solutionBuilder.toString();
        number = 1;
    }

    public void clearSolution() {
        solution = "";
        updateSolution();
    }

    public void clearExpr() {
        expr = "";
        updateExpr();
    }

    @Override
    public void popBack() {
        if (expr.length() > 0)
            expr = expr.substring(0, expr.length() - 1);
        updateExpr();
    }

    @Override
    public void clear() {
        expr = "";
        updateExpr();
    }

    private boolean bracketPair(char a, char b) {
        return a == '(' && b == ')' || a == '[' && b == ']' || a == '{' && b == '}';
    }

    @Override
    public void openBracket(char openBracket) {
        if (expr.length() == 0) {
            expr = "" + openBracket;
            updateExpr();
            return;
        }
        if (expr.charAt(expr.length() - 1) == '√') {
            expr += openBracket;
            updateExpr();
            return;
        }
        if (expr.length() >= 2 && expr.substring(expr.length() - 2).equals("tg")) {
            expr += openBracket;
            updateExpr();
            return;
        }
        if (expr.length() >= 3) {
            String subStr = expr.substring(expr.length() - 3);
            if (subStr.equals("sin") || subStr.equals("cos") || subStr.equals("ctg")) {
                expr = expr + openBracket;
                updateExpr();
                return;
            }
        }
        char closeBracket = ')';
        if (openBracket == '[')
            closeBracket = ']';
        else if (openBracket == '{')
            closeBracket = '}';

        if (hasSymbol(expr.charAt(expr.length() - 1), "<([{+-×*/^√"))
            expr += openBracket;
        else {
            Stack<Character> stack = new Stack<>();
            for (int i = 0; i < expr.length(); i++) {
                char c = expr.charAt(i);
                if (c == '(' || c == '[' || c == '{')
                    stack.add(c);
                else if (c == ')' || c == ']' || c == '}')
                    stack.pop();
            }
            if (stack.empty() || !bracketPair(stack.peek(), closeBracket))
                expr += "×" + openBracket;
            else
                expr += closeBracket;
        }
        updateExpr();
    }

    @Override
    public void addOperation(char operation) {
        if (expr.length() == 0) {
            if (operation == '-') {
                expr += '-';
                updateExpr();
            }
            return;
        }
        char end = expr.charAt(expr.length() - 1);
        if (hasSymbol(end, "+-×/^") || hasSymbol(end, "([{<") && operation != '-')
            return;
        expr += operation;
        updateExpr();
    }

    public void addFunc(int funcId) {
        char end;
        if (expr.length() == 0)
            end = '\0';
        else
            end = expr.charAt(expr.length() - 1);
        if (end != '\0' && !hasSymbol(end, "<([{+-×/"))
            expr += '×';
        if (funcId == SIN_ID)
            expr += "sin(";
        else if (funcId == COS_ID)
            expr += "cos(";
        else if (funcId == TG_ID)
            expr += "tg(";
        else if (funcId == CTG_ID)
            expr += "ctg(";
        else if (funcId == SQRT_ID)
            expr += "√(";
        updateExpr();
    }

    public void addAbsStick() {
        expr += "|";
        updateExpr();
    }

    public void addDot() {
        char end;
        if (expr.length() == 0)
            end = '\0';
        else
            end = expr.charAt(expr.length() - 1);
        if (end == '.')
            return;
        if (hasSymbol(end, ")>]}!"))
            expr += "×0.";
        else if (end == '\0' || hasSymbol(end, "+-×/^"))
            expr += "0.";
        else
            expr += '.';
        updateExpr();
    }

    public void addPi() {
        expr += 'π';
        updateExpr();
    }

    @Override
    public void switchRadDeg() {
        radCalc = !radCalc;
    }

    @Override
    public void setExpr(String newExpr) {
        expr = newExpr;
        updateExpr();
    }
}
