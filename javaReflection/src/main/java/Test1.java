import java.lang.reflect.Field;

public class Test1 {
  public int publicField;
  private String privateField;

  public static void main(String[] args) {

//		Class<?> myClass = test1.class;
    // 获取所有公有字段
    Field[] fields = Test1.class.getFields();

    for (Field field : fields) {
      System.out.println("Public Field: " + field.getName());
    }
  }
}

