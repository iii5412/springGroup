package spring.dev.calcTest;
import org.junit.Test;

import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import spring.dev.calcTest.Calculator;

public class calcTest {
    @Test
    public void sumOfNumbers() throws IOException{
        Calculator calculator = new Calculator("numbers.txt");
        double sum = calculator.sum();
        assertThat(sum, is((double)10));
    }

    @Test
    public void multiOfNumbers() throws IOException{
        Calculator calculator = new Calculator("numbers.txt");
        double multi = calculator.multi();
        assertThat(multi, is((double)24));
    }

}

