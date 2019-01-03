package spring.dev.calc;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import spring.dev.calc.Calculator;
public class CalcSumTest {
    Calculator calculator;
    String filePath;
    @Before
    public void setup(){
        calculator = new Calculator();
        filePath = getClass().getResource("numbers.txt").getPath();
    }
    @Test
    public void sumOfNumbers() throws IOException{
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum(filePath);
        assertThat(sum, is(10));
    }
    @Test
    public void concatenateStrings() throws IOException{
        assertThat(calculator.concatenate(this.filePath), is("1234"));
    }

}

