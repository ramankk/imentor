package com.bugyal.imentor.frontend.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.bugyal.imentor.MentorException;
import com.bugyal.imentor.server.MentorManager;
import com.bugyal.imentor.server.ParticipantManager;
import com.bugyal.imentor.server.data.Location;
import com.bugyal.imentor.server.data.Participant;

public class DataGenerator {
	private Random r = new Random();
	private RandomString rs = new RandomString(5);
	
	DataGenerator(int count) throws MentorException {
		createRandomParticipants(count);
	}

	void createRandomParticipants(int count) throws MentorException {
		for (int i = 0; i < count; i++) {
			String name = "";
			String gender = getRandomGender();
			if (gender.equals("male")) {
				name = Names.BOYS.get(r.nextInt(Names.BOYS.size()));
			} else {
				name = Names.GIRLS.get(r.nextInt(Names.GIRLS.size()));
			}
			String email = name + "." + rs.nextString() + "@kawanan.com";
			
			List<String> hasSubjects = getRandomList();
			List<String> needSubjects = getRandomList();

			ParticipantManager participantManager = MentorManager.INSTANCE
					.getParticipantManager();
			Participant participant = participantManager.createParticipant(
					name, gender, getRandomLocation(), email);

			participantManager.addHasKnowledge(participant,  hasSubjects, 1, participant);
			participantManager.addNeedKnowledge(participant, needSubjects, 1, participant);
			

			if (r.nextFloat() < 0.1) {
				List<Participant> contacts = new ArrayList<Participant>();
				contacts.add(participant);
				MentorManager.INSTANCE.getOpportunityManager()
						.createOpportunity(getRandomLocation(), getRandomList(),
								r.nextInt(7), contacts, r.nextInt(4), rs.nextString(), participant);
			}

		}
	}

	// 17.535368,78.222656, 17.264105,78.717041
	// 31.989442,72.949219, 7.406048,87.758789
	
	private Location getRandomLocation() {
	    Location location = new Location(nextDouble(17.26, 17.53, r),
                            nextDouble(78.22, 78.72, r), rs.nextString(), r.nextInt(100));
	    // For more spread data (most of india)
	    //    	Location location = new Location(nextDouble(7.4, 32.0, r),
	    //				nextDouble(72.95, 87.75, r), rs.nextString(), r.nextInt(100));
		return location;
	}

	private String getRandomGender(){
		String[] arr = {"male", "female"};
		String gender = arr[r.nextInt(2)];
		return gender;
	}
	
	static final String[] subjects = { "ENGLISH", "TELUGU", "MATH", "PHYSICS", "CHEMISTRY",
			"ECONOMICS", "CIVICS", "HISTORY", "GEOGRAPHY",
			"COMPUTER_SCIENCE", "Science", "NETWORKING" };
	
	private List<String> getRandomList() {
		List<String> returnList = new ArrayList<String>();
		int count = r.nextInt(3) + 1;
				
		Set<Integer> included = new HashSet<Integer>();
		for (int i = 0; i < count; ) {
			int r1 = r.nextInt(subjects.length);
			if (! included.contains(r)) {
			  i++;
			  included.add(r1);
			  returnList.add(subjects[r1]);
			}
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
