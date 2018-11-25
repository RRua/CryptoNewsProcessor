package CryptoExtract;

import java.util.Objects;

public class PoSTriple {

    public int sentenceID=0;
    public int wordID = 0;
    public Triple<String, String , String> triple;


    public  PoSTriple (int sid, int wid, Triple t){
        this.sentenceID = sid;
        this.wordID = wid;
        this.triple = t;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PoSTriple)) {
            return false;
        }
        PoSTriple p = (PoSTriple) o;
        return Objects.equals(p.sentenceID, this.sentenceID) && Objects.equals(p.wordID, this.wordID) && Objects.equals(p.triple, this.triple);
    }

}
