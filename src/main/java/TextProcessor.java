import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TextProcessor {

    Map<Integer,String> getSentences();
    void readWordsFromFile(String filepath, String tokenSeparator) throws IOException;
    void wordsToPoS(List<String> listPoS , String tokenSeparator)  throws Exception;
    Set<Pair<String,String>> getRelevantEntities ();

}
