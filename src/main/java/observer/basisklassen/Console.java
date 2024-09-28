package observer.basisklassen;

public class Console implements IConsole {

    public Console() {
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
