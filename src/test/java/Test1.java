import org.junit.Test;

import java.math.BigDecimal;

public class Test1 {
    @Test
    public void t1(){
        BigDecimal d=BigDecimal.ZERO;
        BigDecimal add = d.add(BigDecimal.TEN);
        BigDecimal div=add.divide(new BigDecimal(3),7,BigDecimal.ROUND_CEILING);
        BigDecimal sc=div.setScale(2,BigDecimal.ROUND_CEILING);
        System.out.println("");

    }

}
