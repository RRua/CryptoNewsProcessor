package Utils.Representations;

import java.io.IOException;

public class OrganizationGazeteer implements Gazeteer {

    @Override
    public void toFile(String path) throws IOException {

    }

    @Override
    public void fromFile(String path) {

    }

    @Override
    public boolean isInGazeteer(String toFind) {
        return false;
    }

    @Override
    public boolean serialize(String outputFile) {
        return false;
    }

    @Override
    public Gazeteer Deserialize(String inputFile) {
        return null;
    }
}
