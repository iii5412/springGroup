package spring.dev.singlton;

public class SingltonExample {

    private static SingltonExample _instance;

    private SingltonExample() {
    }
    public synchronized static SingltonExample getInstance() {
        if (null == _instance) {
            _instance = new SingltonExample();
        }
        return _instance;
    }
}
