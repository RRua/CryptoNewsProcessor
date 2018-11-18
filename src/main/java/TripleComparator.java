import java.util.Comparator;

public class TripleComparator implements Comparator<Triple<Integer,Integer,String>> {


    @Override
    public int compare(Triple<Integer, Integer, String> o1, Triple<Integer, Integer, String> o2) {
        return o1.second.compareTo(o2.second);
    }
}
