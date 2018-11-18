package Utils.Representations;

import java.io.IOException;
import java.util.List;

public interface Gazeteer {

    public void toFile(String path) throws IOException;
    public void fromFile(String path);
    //public List<String> getContent();
    public boolean isInGazeteer(String toFind);

    public boolean serialize(String outputFile);

    public Gazeteer Deserialize(String inputFile);




}
