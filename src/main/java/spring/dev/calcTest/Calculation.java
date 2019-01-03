package spring.dev.calcTest;
import java.io.IOException;
import java.sql.SQLException;

public interface Calculation {
    double makePreparedStatement(double initValue, double lineValue);
}
