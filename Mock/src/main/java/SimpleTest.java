import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class SimpleTest {

    @Test
    public void test(){
        // 创建Mock对象，参数可以是类或者接口
        List<String> list = mock(List.class);

        //设置方法的预期返回值
        when(list.get(0)).thenReturn("zuozewei");
        when(list.get(1)).thenThrow(new RuntimeException("test exception"));

        String result = list.get(0);

        //验证方法调用
        verify(list).get(0);

        //断言，list的第一个元素是否是"zuozwei"
        Assert.assertEquals(result,"zuozewei");

    }
}
