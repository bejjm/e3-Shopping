

public class Test {
    public void test() throws Exception {
        try{
            int valuecode =2;
            if(0!=valuecode){
                System.out.println("hahaha");
                throw new Exception("RuntimeException!");
            }
        }catch (RuntimeException e){
            throw e;
        }
    }

    public static void main(String[] args ){
        try{

            new Test().test();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
