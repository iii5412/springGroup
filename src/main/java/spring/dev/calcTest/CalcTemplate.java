package spring.dev.calcTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CalcTemplate {
    private String filePath;
    private double initValue;

    public double template(Calculation ca) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(this.filePath));
        String line = null;

        while((line = br.readLine()) != null){
            this.initValue = ca.makePreparedStatement(this.initValue, Double.valueOf(line));
        }

        br.close();
        return this.initValue;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public void setInitValue(double initValue){
        this.initValue = initValue;
    }
}
