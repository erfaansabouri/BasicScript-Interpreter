package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BasicInterpreter extends InterpreterEngine{
    private ArrayList<String> toks;

    public BasicInterpreter(String filename) {
        super(filename);
    }

    @Override
    public void interpret(String filename) throws IOException {
        String fileContents = open_file(filename);
        Lexer lexer = new Lexer();
        toks =  lexer.lex(fileContents);
        Parser parser = new Parser(toks);
        parser.parse();

    }

    @Override
    public String open_file(String filename) throws IOException {

        File file = new File(filename);
        String path = file.getAbsolutePath();

        BufferedReader b = new BufferedReader(new FileReader(file));

        String readLine = "";
        String wholeLines = "";

        while ((readLine = b.readLine()) != null) {
            wholeLines += readLine + "\n";
        }

        return wholeLines;

    }
}
