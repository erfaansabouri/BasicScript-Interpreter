package sample;

import java.util.ArrayList;

class Lexer extends LexerEngine {
    private ArrayList<String> toks = new ArrayList<String>(); // Create an ArrayList of tokens

    @Override
    public ArrayList<String> lex(String fileContents) {
        String tok = "";
        boolean state = false;
        boolean isExpr = false;
        boolean varStarted = false;
        boolean functionStarted = false;
        String var = "";
        String function = "";
        String str = "";
        String regex_isDigit = "[0-9]+";
        String expr = "";
        for (char c : fileContents.toCharArray()) {

            tok += c;


            if (tok.equals(" ")) {
                if (!state)
                    tok = "";
                else
                    tok = " ";
            }

            else if (tok.equals("\n") || tok.equals("END")){
                if (!(expr.equals("")) && isExpr) {
                    // if expr is not empty
                    toks.add("EXPR:" + expr);
                    expr = "";
                }
                else if (!(expr.equals("")) && !isExpr){
                    toks.add("NUM:" + expr);
                    expr = "";
                }

                else if(var != ""){
                    toks.add("VAR:" + var);
                    var = "";
                    varStarted = false;
                }
                tok = "";
            }


            /*Variables*/
            else if (tok.equals("=") && !state){
                if (!(expr.equals("")) && !isExpr){
                    toks.add("NUM:" + expr);
                    expr = "";
                }
                else if (!(var.equals(""))){
                    toks.add("VAR:" + var);
                    var = "";
                    varStarted = false;
                    toks.add("EQUALS");
                }
                else if (toks.get(toks.size() - 1).equals("EQUALS")){
                    toks.remove(toks.size() - 1);
                    toks.add("EQEQ");
                }

                else
                    toks.add("EQUALS");
                tok = "";
            }


            else if (tok.equals("$") && !state){
                varStarted = true;
                var += tok;
                tok = "";
            }

            else if (tok.equals("#") && !state && !functionStarted){
                functionStarted = true;
                function += tok;
                tok = "";
            }

            else if (varStarted){

                var += tok;
                tok = "";
            }

            else if (functionStarted){
                if(tok.equals("#")){
                    toks.add("NAME:" + function);
                    function = "";
                }
                else{
                    function += tok;
                    tok = "";
                }

            }

            else if (tok.equals("PRINT") || tok.equals("print")) {
                toks.add("PRINT");
                tok = "";
            }

            else if (tok.equals("ENDIF") || tok.equals("endif")) {
                toks.add("ENDIF");
                tok = "";
            }

            else if (tok.equals("IF") || tok.equals("if")) {
                toks.add("IF");
                tok = "";
            }




            else if (tok.equals("FUNCTION") || tok.equals("function")) {
                toks.add("FUNCTION");
                tok = "";
            }



            else if (tok.equals("THEN") || tok.equals("then")) {
                if (!(expr.equals("")) && !isExpr){
                    toks.add("NUM:" + expr);
                    expr = "";
                }
                toks.add("THEN");
                tok = "";
            }

            else if (tok.equals("INPUT") || tok.equals("input")){
                toks.add("INPUT");
                tok = "";
            }

            else if(tok.matches(regex_isDigit)){
                expr += tok;
                tok = "";
            }

            else if(tok.equals("+") || tok.equals("-") || tok.equals("*") || tok.equals("/") || tok.equals("(") ||  tok.equals(")")){
                isExpr = true;
                expr += tok;
                tok = "";
            }

            else if (tok.equals("\t"))
                tok = "";

            else if (tok.equals("\"") || tok.equals(" \"")) {

                if (!state)
                    state = true;

                else if (state) {
                    toks.add("STRING:" + str + "\"");
                    str = "";
                    state = false;
                    tok = "";
                }

            } else if (state) {
                str += tok;
                tok = "";
            }

        }
        System.out.println(toks);
        return toks;
    }


}
