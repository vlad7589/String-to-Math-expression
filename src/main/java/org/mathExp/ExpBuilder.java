package org.mathExp;

import java.util.HashMap;
import java.util.Stack;

public class ExpBuilder {

    private final String stringExp;

    //Map with operands and their weight in expression
    private final HashMap<Character, Integer> operator;

    public ExpBuilder(String stringExp){
        this.stringExp = stringExp;
        this.operator = new HashMap<>();
        fillWeight();
    }

    private void fillWeight(){
        operator.put('+', 1);
        operator.put('-', 1);
        operator.put('*', 2);
        operator.put('/', 2);
        operator.put('%', 2);
        operator.put('^', 3);
    }

    private String infixToPostfix(){
        Stack<Character> helpStack = new Stack<>();
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < stringExp.length(); i++){
            char c = stringExp.charAt(i);
            if(c == ' ') continue;
            if(!operator.containsKey(c)){
                if(c == '(')
                    helpStack.push(c);
                else if(c == ')'){
                    while (helpStack.peek() != '(')
                        res.append(helpStack.pop());
                    helpStack.pop();
                } else {
                    // if c is operand
                    res.append(c);
                }
            } else {
                // if c is operator
                res.append(' ');
                while (!helpStack.isEmpty() && helpStack.peek() != '(' &&
                        (operator.get(c) <= operator.get(helpStack.peek()))){
                    res.append(helpStack.pop());
                }
                helpStack.push(c);
            }
        }
        res.append(' ');
        while (!helpStack.isEmpty()){
            res.append(helpStack.pop());
        }
        return res.toString();
    }

    private Double calcUtil(double a, double b, char c) {
        return switch (c) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            case '^' -> Math.pow(a, b);
            case '%' -> a % b;
            default -> 0.0;
        };
    }

    public Double equals(){
        Stack<Double> nums = new Stack<>();
        String s = infixToPostfix();
        StringBuilder number = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(!number.isEmpty() && (c == ' ' || operator.containsKey(c))){
                nums.push(Double.parseDouble(number.toString()));
                number.setLength(0);
            }
            if(c != ' ') {
                if (!operator.containsKey(c)) {
                    number.append(c);
                } else {
                    // operator
                    double a = nums.pop();
                    double b = nums.pop();
                    nums.push(calcUtil(b, a, c));
                }
            }
        }
        return nums.pop();
    }

}
