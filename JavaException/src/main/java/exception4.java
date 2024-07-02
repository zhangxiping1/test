import java.io.IOException;

public class exception4 {
    public exception4() {
    }

    boolean testEx() throws Exception {
        boolean ret = true;
        try {
            ret = testEx1();
        } catch (Exception e) {
            System.out.println("testEx, catch exception");
            ret = false;
           // throw e;
        } finally {
            System.out.println("testEx, finally; return value=" + ret);

        }
        System.out.println("meiyou");
        return ret;
    }

    boolean testEx1() throws Exception {
        boolean ret = true;
        try {
            ret = testEx2();
            if (!ret) {
                return false;
            }
            System.out.println("testEx1, at the end of try");

        } catch (Exception e) {
            System.out.println("testEx1, catch exception");
            ret = false;
            throw e;
        } finally {
            System.out.println("testEx1, finally; return value=" + ret);

        }
        return ret;
    }

    boolean  testEx2() throws Exception {

        boolean ret = true;

            int b = 12;
            int c;
            for (int i = 2; i >= -2; i--) {
                c = b / i;
                System.out.println("i=" + i);
            }
            ret = false;
            System.out.println("--------------------");

            return false;

    }

    public static void main(String[] args) throws IOException {
        exception4 testException1 = new exception4();
        IOException cause = null;
        if("a" == "a")
        throw (IOException)
                new IOException("Failed to move to trash: ");
        if(2/2==1){
            try {
                if(2/1==1) {
                    System.out.println("1-111111");
                    return;
                }
                else
                    System.out.println("2-1111111");
            } catch (Exception e) {
                cause = (IOException) e;
            }finally {
                System.out.println("3-111111");
            }
        }else{
            try {
                if(2/2==1) {
                    System.out.println("1-222222");
                    return;
                }
                else
                    System.out.println("2-222222");
            } catch (Exception e) {
                cause = (IOException) e;
            }finally {
                System.out.println("3-222222");
            }

        }
        throw (IOException)
                new IOException("Failed to move to trash: " + cause.getMessage());
    }
}