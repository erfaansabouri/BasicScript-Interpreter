package sample;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Parser extends ParserEngine{

    ArrayList<String> toks;
    ArrayList<String> stack = new ArrayList<String>();
    Map<String, String> vars = new HashMap<String, String>();


    public Parser(ArrayList<String> toks) {
        this.toks = toks;
    }
    
    
    @Override
    public void parse(){
        int i = 0;
        while (i < toks.size()){


            if (isPrintStatement(i)){

                if(isNumber(i+1))
                    printNumber(i+1);

                else if (isVariableStatement(i+1))
                    printVarValue(i+1);

                else if(isExpr(i+1))
                    printExpr(i+1);


                else if (isString(i+1))
                    printString(i+1);


                i+=2;
            }
            else if(isVariableStatement(i) && isEqualStatement(i+1)){
                if(isNumber(i+2))
                    doAssign(toks.get(i).substring(4) , toks.get(i+2).substring(4));
                else if(isVariableStatement(i+2)){
                    doAssign(toks.get(i).substring(4) , vars.get((toks.get(i+2).substring(4))));
                }
                else if(isExpr(i+2))
                    doAssign(toks.get(i).substring(4) , eval(toks.get(i+2).substring(5)) +"");
                else if(isString(i+2))
                    doAssign(toks.get(i).substring(4) , toks.get(i+2).substring(7));
                i+=3;
            }

            else if (isInputStatement(i)){
                getInput(toks.get(i+1).substring(8,toks.get(i+1).length()-1) , toks.get(i+2).substring(4));
                i+=3;
            }

            else if(isIfStatement(i)){
                if (isNumber(i+1) && isNumber(i+3)){
                    if (toks.get(i+1).substring(4).equals(toks.get(i+3).substring(4))){ //true condition
                        if ("PRINT".equals(toks.get(i + 5))) {
                            if (isNumber(i + 6)) {
                                printNumber(i + 6);
                            } else if (isVariableStatement(i + 6)) {
                                printVarValue(i + 6);
                            } else if (isString(i + 6)) {
                                printString(i + 6);
                            }
                        }

                    }
                    else
                        System.out.println("False");
                }

                i+=8;
            }

        }
        System.out.println(vars);
    }

    @Override
    public boolean isIfStatement(int i) {
        return toks.get(i).length() == 2 && ((toks.get(i)).substring(0,2)).equals("IF");

    }


    @Override
    public void getInput(String interfaceString, String variable) {

        Scanner userInput = new Scanner(System.in);  // Create a Scanner object
        System.out.println(interfaceString);
        String input = userInput.nextLine();  // Read user input
        doAssign(variable,input);

    }


    @Override
    public void printVarValue(int index) {
        System.out.println(vars.get((toks.get(index).substring(4))));
    }


    @Override
    public void doAssign(String variable_name, String variable_value) {
        vars.put(variable_name,variable_value);
    }


    @Override
    public boolean isInputStatement(int i) {
        return ( toks.get(i).equals("INPUT")  && isString(i+1) && isVariableStatement(i+2));
    }


    @Override
    public boolean isPrintStatement(int index){
        return (toks.get(index)).equals("PRINT");
    }


    @Override
    public boolean isVariableStatement(int index){
        return toks.get(index).length() >= 3 &&(((toks.get(index)).substring(0,3)).equals("VAR")  )  ;
    }

    @Override
    public boolean isEqualStatement(int index){
        return (toks.get(index).equals("EQUALS"));
    }

    @Override
    public boolean isString(int index){
        return ((toks.get(index)).substring(0,6)).equals("STRING");
    }

    @Override
    public boolean isNumber(int index){
        return ((toks.get(index)).substring(0,3)).equals("NUM");
    }


    @Override
    public boolean isExpr(int index){
        return ((toks.get(index)).substring(0,4)).equals("EXPR");
    }


    @Override
    public void printString(int index){

        System.out.println(toks.get(index).substring(8,toks.get(index).length()-1));
    }


    @Override
    public void printNumber(int index){
        System.out.println(toks.get(index).substring(4));
    }


    @Override
    public void printExpr(int index){
        evalExpr(toks.get(index).substring(5));
    }


    @Override
    public void evalExpr(String expr){
        /*
        expr = "," + expr;
        int i = (expr.length())-1;
        String num = "";
        while (i >= 0){
            if(expr.toCharArray()[i] == '+' ||
                expr.toCharArray()[i] == '-' ||
                expr.toCharArray()[i] == '/' ||
                expr.toCharArray()[i] == '%' ||
                expr.toCharArray()[i] == '*' )
            {
                stack.add(num);
                stack.add(expr.toCharArray()[i] + "");
                num = "";
            }
            else if(expr.toCharArray()[i] == ','){
                StringBuilder reverse = new StringBuilder();
                reverse.append(num);
                reverse = reverse.reverse();
                stack.add(reverse + "");
                num = "";
            }
            else{
                num += expr.toCharArray()[i] + "";
            }
            i--;
        }
        System.out.println(stack);
        */
        System.out.println(eval(expr));

    }



    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar :
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

}
