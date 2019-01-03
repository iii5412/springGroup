package spring.dev.calcTest;

import java.io.IOException;

public class Calculator {
    private CalcTemplate calcTemplate;

    public Calculator(String fileName){
        this.calcTemplate = new CalcTemplate();
        this.calcTemplate.setFilePath(getClass().getResource(fileName).getPath());
    }

    public double sum_old() throws IOException{
        this.calcTemplate.setInitValue(0);
        double result = this.calcTemplate.template(
                new Calculation() {
                    @Override
                    public double makePreparedStatement(double initValue, double lineValue) {
                        initValue = initValue + lineValue;
                        return initValue;
                    }
                }
        );
        return result;
    }

    public double multi_old() throws IOException{
        this.calcTemplate.setInitValue(1);
        double result = this.calcTemplate.template(
                new Calculation() {
                    @Override
                    public double makePreparedStatement(double initValue, double lineValue){
                        initValue = initValue * lineValue;
                        return initValue;
                    }
                }
        );
        return result;
    }

    public double sum() throws IOException{
        this.calcTemplate.setInitValue(0);
        Calculation calculation = new Calculation(){
            @Override
            public double makePreparedStatement(double initValue, double lineValue){
                initValue = initValue + lineValue;
                return initValue;
            }
        };
        return calcTemplate.template(calculation);
    }

    public double multi() throws IOException{
        this.calcTemplate.setInitValue(1);
        Calculation calculation = new Calculation(){
            @Override
            public double makePreparedStatement(double initValue, double lineValue){
                initValue = initValue * lineValue;
                return initValue;
            }
        };
        return calcTemplate.template(calculation);
    }

}
