package CryptoExtract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Triple<F, S, T> {
    public final F first;
    public final S second;
    public final T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Triple)) {
            return false;
        }
        Triple<?, ?, ?> p = (Triple<?, ?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second) && Objects.equals(p.third, third);
    }



    public static List<Triple> fromFile(String filePath, String tokenSeparator) throws IOException{
        BufferedReader abc = new BufferedReader(new FileReader(filePath));
        List<Triple> list = new ArrayList<>();
        String line = null;

        while((line = abc.readLine()) !=null){
            String [] tokens = line.split(tokenSeparator);
            if (tokens.length==3){
                Triple <String,String,String> t = new Triple(tokens[0],tokens[1],tokens[2]);
                list.add(t);
            }
        }
        abc.close();
        return list;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + "," + second.toString() + "," + third.toString() + ")";
    }

    @Override
    public int hashCode() {
        return (first.hashCode() +  second.hashCode() + third.toString()).hashCode();
    }
}
