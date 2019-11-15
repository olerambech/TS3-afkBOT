import java.time.LocalTime;
import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class TS3AfkSjekk {

	private static TS3Query query;
	private static TS3Api api;
	private static int afkChannelId;
	private static int tidFørFlytting = 15; // i minutter
	private static int tidFørFlyttisek = tidFørFlytting * 60;

	public TS3AfkSjekk(String ip, String username, String password, String channelToMove) {
		final TS3Config config = new TS3Config();
		config.setHost(ip);
		config.setEnableCommunicationsLogging(false);
		query = new TS3Query(config);
		query.connect();
		// Log in, select the virtual server and set a nickname
		api = query.getApi();
		api.login(username, password);
		api.selectVirtualServerById(1);
		api.setNickname("ServerBot");
		api.registerAllEvents();
		afkChannelId = Integer.parseInt(channelToMove);
	}

	public void start() {
		while(true) {
			List<Client> klienter = getKlienter();
			flyttAfktilAfk(klienter);
			try {
				System.out.println("Sov i 5 sek");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private static void flyttAfktilAfk(List<Client> klienter) {
		for (int i = 0; i < klienter.size(); i++) {
			//if (klienter.get(i).getIdleTime() / (1000) > tidFørFlyttisek) {
			if (klienter.get(i).getUniqueIdentifier().equals("p0OyKIFH3QvQtac3G6+Iu0UXs9Q=")) {

				try {
					if (klienter.get(i).getChannelId() != 11) {
						System.out.print(klienter.get(i).getNickname() + ": ");
						int channelId = klienter.get(i).getChannelId();
						
						api.moveClient(klienter.get(i).getId(), afkChannelId);
						api.sendChannelMessage(channelId, klienter.get(i).getNickname()
								+ " ble flyttet fordi han var borte i mer enn " + tidFørFlytting + " minutter :(");
						api.sendPrivateMessage(klienter.get(i).getId(),
								"Du ble flyttet fordi du var afk lengre enn 15 minutter");
						System.out.print("[" + LocalTime.now().toString().substring(0, 5) + "] ");
						System.out.println(klienter.get(i).getNickname() + " ble flyttet til afk! Han var afk i " + tidFørFlytting + " minutter!");
					}

				} catch (TS3CommandFailedException e) {
					System.out.println(klienter.get(i).getNickname() + " er allerede i afk!");
				}

			}
		}

	}

	private static List<Client> getKlienter() {
		return api.getClients();
	}

}
