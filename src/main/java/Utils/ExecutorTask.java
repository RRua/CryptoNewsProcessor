package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExecutorTask implements Runnable{

    public String [] command = {
            "/bin/sh",
            "-c",
            ""
    };
    public List<String> returnList = new ArrayList<>();

    public ExecutorTask(String command) {
        this.command[2] = command;
    }
    @Override
    public void run() {

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(this.command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));
            String line="";
            while ((line = reader.readLine()) != null) {
                returnList.add(line);
            }
            while ((line = stdError.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
