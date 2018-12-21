package basics.example;

/**
 * 静态变量(类变量) 实例变量区别
 *
 */ 
public class StaticTest {
    private static int staticInt = 1;
    private int random = 1;

    private StaticTest() {
        staticInt++;
        random++;
        System.out.println("staticInt = " + staticInt + "  random = " + random);
    }

    public static void main(String[] args) {
        StaticTest test = new StaticTest();
        StaticTest test2 = new StaticTest();
        StaticTest.staticInt++;
        test.random++;
        test2.random++;
    }
}