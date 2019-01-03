package spring.dev.singlton;

public class SingltonMain {
    public static void main(String[] args) {
        SingltonExample singlton =
                SingltonExample.getInstance();

        System.out.println(singlton);

        SingltonExample singlton2 =
                SingltonExample.getInstance();

        System.out.println(singlton2);
    }
}
