package sample;
import java.io.IOException;
import java.util.ArrayList;

abstract class InterpreterEngine {
    private String filename;
    public InterpreterEngine(String filename) {
        this.filename = filename;
    }
    abstract public void interpret(String filename) throws IOException;
    abstract public String open_file(String filename) throws IOException;

}

abstract class LexerEngine{
    abstract public ArrayList<String> lex(String content);
}

abstract class ParserEngine{
    ArrayList<String> toks;
    abstract public void parse();
    abstract boolean isIfStatement(int i);
    abstract void getInput(String interfaceString, String variable);
    abstract void printVarValue(int index);
    abstract void doAssign(String variable_name, String variable_value);
    abstract boolean isInputStatement(int i);
    abstract boolean isPrintStatement(int index);
    abstract boolean isVariableStatement(int index);
    abstract boolean isEqualStatement(int index);
    abstract boolean isString(int index);
    abstract boolean isNumber(int index);
    abstract boolean isExpr(int index);
    abstract void printString(int index);
    abstract void printNumber(int index);
    abstract void printExpr(int index);
    abstract void evalExpr(String expr);

}