import com.yen.service.RedshiftService;
import com.yen.web.service.impl.RedshiftServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class test {

    @Autowired
    RedshiftService redshiftService;

    @Autowired
    RedshiftServiceImpl redshiftServiceimpl;

    @Test
    public void test1(){

        System.out.println(123);
        System.out.println(redshiftService);
        //System.out.println(redshiftService.runQuery());
    }

}
