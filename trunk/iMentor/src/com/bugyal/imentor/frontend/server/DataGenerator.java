package com.bugyal.imentor.frontend.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bugyal.imentor.MentorException;
import com.bugyal.imentor.server.MentorManager;
import com.bugyal.imentor.server.ParticipantManager;
import com.bugyal.imentor.server.data.Location;
import com.bugyal.imentor.server.data.Participant;

public class DataGenerator {
	private Random r = new Random();
	private RandomString rs = new RandomString(10);

	DataGenerator(int count) throws MentorException {
		createRandomParticipants(count);
	}

	void createRandomParticipants(int count) throws MentorException {
		for (int i = 0; i < count; i++) {
			String name = rs.nextString();
			String email = rs.nextString() + "@kawanan.com";

			List<String> hasSubjects = getRandomList();
			List<String> needSubjects = getRandomList();

			ParticipantManager participantManager = MentorManager.INSTANCE
					.getParticipantManager();
			Participant participant = participantManager.createParticipant(
					name, getRandomLocation(), email);

			for (String subject : hasSubjects) {
				participantManager.addHasKnowledge(participant, subject,
						r.nextInt(5), participant);
			}
			for (String subject : needSubjects) {
				participantManager.addNeedKnowledge(participant, subject,
						r.nextInt(5), participant);
			}

			if (r.nextFloat() < 0.1) {
				List<Participant> contacts = new ArrayList<Participant>();
				contacts.add(participant);
				MentorManager.INSTANCE.getOppurtunityManager()
						.createOpportunity(getRandomLocation(),
								getRandomList(), r.nextInt(7), contacts,
								r.nextInt(4));
			}

		}
	}

	private Location getRandomLocation() {
		Location location = new Location(nextDouble(70.00, 71.00, r),
				nextDouble(70.00, 71.00, r), rs.nextString(), r.nextInt(100));
		return location;
	}

	private List<String> getRandomList() {
		Random random = new Random();
		String[] arr = { "ENGLISH", "TELUGU", "MATH", "PHYSICS", "CHEMISTRY",
				"ECONOMICS", "CIVICS", "HISTORY", "GEOGRAPHY",
				"COMPUTER_SCIENCE", "Science", "NETWORKING" };

		List<String> returnList = new ArrayList<String>();
		returnList.add(arr[random.nextInt(arr.length)]);
		int count = random.nextInt(3);
		for (int i = 0; i < count; i++) {
			returnList.add(arr[random.nextInt(arr.length)]);
		}
		return returnList;
	}

	public double nextDouble(double min, double max, Random r) {
		double randomValue = min + (max - min) * r.nextDouble();
		return randomValue;
	}
}

class RandomString {
	private static final char[] symbols = new char[36];

	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}

	private final Random random = new Random();

	private final char[] buf;

	public RandomString(int length) {
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);
		buf = new char[length];
	}

	public String nextString() {
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}

}
