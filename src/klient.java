import java.time.LocalTime;
import java.util.Timer;

public class klient {

	public static void main(String[] args) {
		TS3AfkSjekk sjekk = new TS3AfkSjekk(args[0], args[1], args[2], args[3]);
		sjekk.start();

	}

}
