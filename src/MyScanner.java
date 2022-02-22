import java.io.*;

public class MyScanner implements AutoCloseable {

    private final Reader in;
    private final int SIZE = 512;
    private final char[] buffer = new char[SIZE];
    private int last = 0;
    private int current = 0;
    private int[] types = {};
    private char[] chars = {};

    public MyScanner(InputStream source) throws IOException {
        in = new InputStreamReader(source);
        readBuffer();
    }

    public MyScanner(File source, String encoding) throws IOException {
        in = new InputStreamReader(new FileInputStream(source), encoding);
        readBuffer();
    }

    public void setCharacters(int[] types, char ... chars) {
        this.types = types;
        this.chars = chars;
    }

    void readBuffer() throws IOException{
        last = in.read(buffer);
        current = 0;
    }

    boolean isPartOfWord(char chr) {
        boolean isGoodChar = false;
        for (var type: types) {
            if (Character.getType(chr) == type){
                isGoodChar = true;
                break;
            }
        }
        for (var c: chars) {
            if (chr == c){
                isGoodChar = true;
                break;
            }
        }
        return isGoodChar;
    }

    public boolean isNextLine() throws IOException {
        if (current == last) {
            readBuffer();
        }
        while (!isPartOfWord(buffer[current]) && buffer[current] != '\n' && buffer[current] != '\r') {
            current++;
            if (current == last) {
                readBuffer();
            }
        }
        if (last == -1) {
            return false;
        } else if (buffer[current] == '\n') {
            current++;
            return true;
        } else if (buffer[current] == '\r') {
            current++;
            if (current == last) {
                readBuffer();
            }
            if (buffer[current] == '\n') {
                current++;
            }
            return true;
        }
        return false;
    }

    public String next() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (last != -1) {
            if (current == last) {
                readBuffer();
            } else {
                if (!isPartOfWord(buffer[current])) {
                    if (!sb.isEmpty()) {
                        return sb.toString();
                    }
                } else {
                    sb.append(buffer[current]);
                }
                current++;
            }
        }
        if (sb.isEmpty()) {
            return null;
        }
        return sb.toString();
    }

    @Override
    public void close() {
        try{
            in.close();
        } catch(IOException e) {
            System.out.println("File does not exist");
        }
    }
}