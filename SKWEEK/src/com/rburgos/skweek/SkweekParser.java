package com.rburgos.skweek;

import java.util.*;
import java.util.regex.*;

public class SkweekParser {
    private static final String REGEX =
            "((t\\b)|(x\\b)|(y\\b)|(z\\b)|" +
                    "(\\-?\\d*\\.\\d+)|(\\-?\\d+)|" +
                    "([\\+\\-\\*/\\%\\(\\)]|" +
                    "(\\|{1,2})|(\\&{1,2})|(\\<{1,2})|(\\>{1,2})|(\\^{1})))";

    private static int t, x, y, z;

    private SkweekParser() {
    }

    public static void setT(int time) {
        t = time;
    }

    public static void setX(int xVal) {
        x = xVal;
    }

    public static void setY(int yVal) {
        y = yVal;
    }

    public static void setZ(int zVal) {
        z = zVal;
    }

    public static double eval(String exp) {
        return calc(toPostfix(split(exp)));
    }

    private static List<String> split(String expression) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(expression);
        ArrayList<String> tokenList = new ArrayList<>();

        while (matcher.find()) {
            tokenList.add(matcher.group());
        }

        return tokenList;
    }

    private static List<String> toPostfix(List<String> tokenList) {
        Stack<String> postfixStack = new Stack<>();
        Stack<String> operators = new Stack<>();
        for (int i = 0; i < tokenList.size(); i++) {
            if (isLowPrecedence(tokenList.get(i))) {
                if (operators.isEmpty()) {
                    operators.push(tokenList.get(i));
                } else {
                    if (isLeftParen(operators.peek())) {
                        operators.push(tokenList.get(i));
                    } else {
                        postfixStack.push(operators.pop());
                        operators.push(tokenList.get(i));
                    }
                }
            } else if (isHighPrecedence(tokenList.get(i))) {
                if (operators.isEmpty()) {
                    operators.push(tokenList.get(i));
                } else {
                    if (isHighPrecedence(operators.peek())) {
                        postfixStack.push(operators.pop());
                        operators.push(tokenList.get(i));
                    } else {
                        operators.push(tokenList.get(i));
                    }
                }
            } else if (isLeftParen(tokenList.get(i))) {
                operators.push(tokenList.get(i));
            } else if (isRightParen(tokenList.get(i))) {
                while (!operators.isEmpty()) {
                    String rightP = operators.pop();
                    if (!isLeftParen(rightP)) {
                        postfixStack.push(rightP);
                    }
                }
            } else {
                postfixStack.push(tokenList.get(i));
            }

        } // end for loop

        // push remaining elements from operator stack if it's not empty
        while (!operators.isEmpty()) {
            postfixStack.push(operators.pop());
        }

        return postfixStack;
    }

    private static double calc(List<String> postfixList) {
        Stack<Double> calc = new Stack<>();
        for (int i = 0; i < postfixList.size(); i++) {
            if ("+".equals(postfixList.get(i))) {
                calc.push(SkweekMath.add(calc.pop(), calc.pop()));
            } else if ("-".equals(postfixList.get(i))) {
                calc.push(SkweekMath.sub(calc.pop(), calc.pop()));
            } else if ("*".equals(postfixList.get(i))) {
                calc.push(SkweekMath.mul(calc.pop(), calc.pop()));
            } else if ("/".equals(postfixList.get(i))) {
                calc.push(SkweekMath.div(calc.pop(), calc.pop()));
            } else if ("^".equals(postfixList.get(i))) {
                calc.push(SkweekMath.pow(calc.pop(), calc.pop()));
            } else if ("<<".equals(postfixList.get(i))) {
                calc.push(SkweekMath.lShift(calc.pop(), calc.pop()));
            } else if (">>".equals(postfixList.get(i))) {
                calc.push(SkweekMath.rShift(calc.pop(), calc.pop()));
            } else if ("|".equals(postfixList.get(i))) {
                calc.push(SkweekMath.or(calc.pop(), calc.pop()));
            } else if ("&".equals(postfixList.get(i))) {
                calc.push(SkweekMath.and(calc.pop(), calc.pop()));
            } else if ("%".equals(postfixList.get(i))) {
                calc.push(SkweekMath.mod(calc.pop(), calc.pop()));
            } else if ("t".equals(postfixList.get(i))) {
                calc.push((double) t);
            } else if ("x".equals(postfixList.get(i))) {
                calc.push((double) x);
            } else if ("y".equals(postfixList.get(i))) {
                calc.push((double) y);
            } else if ("z".equals(postfixList.get(i))) {
                calc.push((double) z);
            } else {
                calc.push(Double.parseDouble(postfixList.get(i)));
            }
        }
        return calc.pop();
    }

    private static boolean isLowPrecedence(String op) {
        return op.equals("+") || op.equals("-");
    }

    private static boolean isHighPrecedence(String op) {
        return op.equals("*") || op.equals("/") || op.equals("^") ||
                op.equals("<<") || op.equals(">>") || op.equals("|") ||
                op.equals("&") || op.equals("%");
    }

    private static boolean isLeftParen(String op) {
        return op.equals("(");
    }

    private static boolean isRightParen(String op) {
        return op.equals(")");
    }
}
