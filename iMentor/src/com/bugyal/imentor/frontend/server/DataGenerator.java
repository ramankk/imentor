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
import com.bugyal.imentor.server.data.Opportunity;
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
				Opportunity o = MentorManager.INSTANCE.getOpportunityManager()
						.createOpportunity(getRandomLocation(), getRandomList(),
								r.nextInt(7), contacts, r.nextInt(4), rs.nextString(), participant);
				for(int loc=0; loc< o.getContacts().size(); loc++){
					Participant p = MentorManager.INSTANCE.getParticipantManager().findById(o.getContacts().get(loc));
					p.addCreatedOpportuny(o.getKey());
					MentorManager.INSTANCE.getParticipantManager().save(p);
				}				
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
	enum Gender {
		M, F;
	}
	private String getRandomGender(){
		//String[] arr = {"male", "female"};		
		String gender = Gender.values()[r.nextInt(2)].toString();
		return gender;
	}
	
	static final String[] subjects = { "Accounting", "Accounting And Financial Management",
		"Adaptive Control Systems",
		"Advanced Communication Skills Lab",
		"Advanced Computational Aerodynamics",
		"Advanced Computer Architecture	",
		"Advanced Computing Concepts", "Advanced Control Systems",
		"Advanced Data Structures",
		"Advanced Data Structures And Algorithms",
		"Advanced Data Structures Lab",
		"Advanced English Communications Skills Lab",
		"Advanced Structural Analysis", "Advanced Structural Design",
		"Advertising And Brand Management",
		"Aerodynamics And Propulsion Lab", "Aerodynamics – I",
		"Aerodynamics – II", "Aeroelasticity",
		"Aerospace Materials And Composites", "Aerospace Propulsion I",
		"Aerospace Propulsion II", "Aerospace Structures Lab",
		"Aerospace Vehicle Structures – I",
		"Aerospace Vehicle Structures II", "African Studies",
		"Afrikaans", "Aircraft Engineering Drawing",
		"Aircraft Maintenance Management",
		"Aircraft Materials And Production Lab",
		"Aircraft Production Technology",
		"Aircraft Systems And Instrumentation", "Air Line Management",
		"Air Pollution And Control", "Airport Management", "Albanian",
		"Alternative Energy Sources For Automobiles",
		"American Studies", "Analog Communications",
		"Analog Communications Lab",
		"Analysis Of Composites Structure",
		"Analytical And Biomedical Instrumentation Lab",
		"Analytical Chemistry", "Analytical Chemistry Lab",
		"Analytical Instrumentation", "Anatomy And Physiology",
		"Anatomy And Physiology Lab",
		"Animal Cell Science And Technology",
		"Antennas And Wave Propagation", "Anthropology",
		"Applied Biochemistry Lab",
		"Applied Chemistry And Biochemistry", "Applied Linguistics",
		"Applied Management", "Applied Mechanics",
		"Applied Philosophy", "Applied Physics", "Arabic",
		"Archaeology", "Armenian", "Art", "Art History",
		"Artificial Intelligence",
		"Artificial Intelligence And Neural Networks",
		"Artificial Neural Networks", "Asian Studies", "Astrology",
		"Astronomy", "Auto Air Conditioning",
		"Automata And Compiler Design", "Automation In Manufacturing",
		"Automation In Manufacturing",
		"Automation Of Industrial Processes", "Automobile Engineering",
		"Automobile Engineering Lab I",
		"Automobile Engineering Lab II And Cad/Cam Lab",
		"Automotive Chassis And Suspension",
		"Automotive Electrical And Autotronics", "Automotive Engines",
		"Automotive Pollution And Control",
		"Auto Scanning And Vehicle Testing Lab", "Avionics",
		"Basic Clinical Sciences I", "Basic Clinical SciencesII",
		"Basic Electronic Devices And Circuits",
		"Basic Industrial And Environmental Biotechnology",
		"Bible Studies", "Biochemical Engineering",
		"Biochemical Reaction Engineering I",
		"Biochemical Reaction EngineeringII", "Bio-Chemistry",
		"Biochemistry Lab", "Bioelectricity And Electrodes",
		"Bio-Engineering",
		"Bio Ethics, Bio Safety And Intellectual Property Rights",
		"Biofluids And Mechanics", "Bio Informatics",
		"Bioinformatics Lab", "Biological Control Systems", "Biology",
		"Biomaterials", "Biomaterials Science And Technology",
		"Biomedical Equipment", "Biomedical Equipment Lab",
		"Bio Medical Instrumentation", "Biomedical Signal Processing",
		"Biomedical Signal Processing Lab", "Biomems", "Biometrics",
		"Biometric Technology", "Biopharmaceutical Technology",
		"Biopharmaceuticsand Pharmacokinetics",
		"Bio Process Economics And Plant Design",
		"Bioprocess Engineering", "Bioprocess Engineering Lab",
		"Bioprocess Optimization", "Bio-Sciences",
		"Biosensors And Bioelectronics	", "Biotechnics",
		"Biotransducers And Applications", "Botany",
		"Boundary Layer Theory", "Building Materials And Construction",
		"Building Planning And Drawing", "Bulgarian",
		"Business Administration", "Business Environment",
		"Business Ethics And Governance", "Business Laws",
		"Business Management", "Business Studies", "Cad/Cam",
		"Cad/Cam Lab", "Cad Lab",
		"Calibration And Electronic Measurements", "Cancer Biology",
		"Cantonese", "Cartography", "Cell Biology",
		"Cell Biology And Microbiology Lab", "Cell Signaling",
		"Cellular And Mobile Communications",
		"Chemical Engineering Mathematics",
		"Chemical Engineering Plant Design And Economics",
		"Chemical Engineering Thermodynamics I",
		"Chemical Engineering Thermodynamics II",
		"Chemical Process Calculations",
		"Chemical Process Equipment Design",
		"Chemical Reaction Engineering I",
		"Chemical Reaction Engineering II",
		"Chemical Reaction Engineering Lab", "Chemical Technology",
		"Chemistry", "Chemistry Of Natural Drugs", "Chinese",
		"Cinematography", "Civics", "Civil Law", "Classical Arts",
		"Classical History", "Classical Literature", "Classical Music",
		"Client Server Computing",
		"Clinical Pharmacy And Therapeutics", "Cognition",
		"Cognitive Studies", "Commerce",
		"Commercial Property Management", "Company Law",
		"Compiler Design", "Computational Aero Dynamics",
		"Computational Fluid Dynamics",
		"Computational Molecular Biology",
		"Computational Structural And Aerodynamics Lab",
		"Computer Aided Design Of Control Systems",
		"Computer Engineering", "Computer Graphics",
		"Computer Networks", "Computer Networks And Case Tools Lab",
		"Computer Networks And Operating Systems Lab",
		"Computer Organization",
		"Computer Organization And Architecture",
		"Computer Programming", "Computer Programming Lab",
		"Computer Science", "Computer Studies",
		"Computer System Organization", "Concrete And Highway",
		"Concrete Technology", "Contemporary Art",
		"Contemporary History", "Contemporary Literature",
		"Contemporary Studies", "Control Systems",
		"Control Systems And Simulation Lab", "Control Systems Lab I",
		"Control Systems Lab II", "Corporate Law",
		"Cost And Management Accounting", "Counselling",
		"C Programming And Data Structures",
		"C Programming And Data Structures Lab",
		"Creativity Innovation And Product Development",
		"Criminal Justice", "Criminal Law", "Criminal Science",
		"Criminology", "Cybernetics", "Cybertechnics", "Danish",
		"Database Management Systems",
		"Database Management Systems Lab", "Data Communications",
		"Data Communication Systems", "Data Management",
		"Data Warehousing And Data Mining", "Decision Support Systems",
		"Defence Sciences", "Defence Studies",
		"Design And Analysis Of Algorithms",
		"Design And Analysis Of Experiments",
		"Design And Drawing Of Hydraulic Structures",
		"Design Of Machine Elements", "Design Of Machine Members – I",
		"Design Of Machine Members – II",
		"Design Of Reinforced Concrete Structures",
		"Design Of Steel Structures", "Design Patterns",
		"Digital And Optimal Control Systems",
		"Digital Communications", "Digital Communications Lab",
		"Digital Control Systems", "Digital Design Through Verilog",
		"Digital Ic Applications", "Digital Image Processing",
		"Digital Image Processing Lab",
		"Digital Logic And Computer Systems Organization",
		"Digital Logic And Computer Systems Organization Lab",
		"Digital Logic Design", "Digital Signal Processing",
		"Digital Signal Processing Lab",
		"Discrete Structures And Graph Theory",
		"Dispensing And Hospital Pharmacy",
		"Distributed Computer Control Systems",
		"Distributed Databases", "Divinity", "Downstream Processing",
		"Downstream Processing Lab",
		"Dsp Processors And Architectures", "Dutch",
		"Dynamics Of Machinery", "Earthquake Resistant Design",
		"Ecology", "E Commerce", "Economics", "Ehv Ac Transmission",
		"Electrical And Electronic Measurements",
		"Electrical And Electronics Engineering",
		"Electrical And Electronics Engineering Lab",
		"Electrical Circuit Analysis",
		"Electrical Circuits And Simulation Lab",
		"Electrical Circuits Lab", "Electrical Distribution Systems",
		"Electrical Engineering", "Electrical Machines I",
		"Electrical Machines II", "Electrical Machines III",
		"Electrical Machines Lab – I", "Electrical Machines Lab – II",
		"Electrical Measurements", "Electrical Measurements Lab",
		"Electrical Technology", "Electrical Technology Lab",
		"Electromagnetic Fields", "Electro Magnetic Fields",
		"Electromagnetic Waves And Transmission Lines",
		"Electronic Circuit Analysis", "Electronic Circuits Lab",
		"Electronic Computer Aided Design Lab",
		"Electronic Devices And Circuits Lab",
		"Electronic Measurements And Instrumentation",
		"Electronics Design Automation Lab",
		"Embedded And Real Time Systems", "Embedded Systems",
		"Em Waves And Transmission Lines", "Energy Engineering",
		"Engineering", "Engineering Drawing", "Engineering Geology",
		"Engineering Geology Lab", "Engineering Lab",
		"Engineering Optimization", "Engineering Physics",
		"Engineering Technology",
		"Engineering Workshop And It Workshop", "English",
		"English Language Communication Skills",
		"English Language Communication Skills Lab",
		"English Literature", "Entomology", "Entrepreneur Ship",
		"Entrepreneurship And Project Management",
		"Environmental Engineering I",
		"Environmental Engineering – II",
		"Environmental Engineering Lab",
		"Environmental Impact Assessment And Management",
		"Environmental Science", "Environmental Studies",
		"Estimating And Costing", "Estonian", "Etymology",
		"European Studies", "Experimental Stress Analysis",
		"Fatigue And Fracture Mechanics", "Feminism", "Finance",
		"Financial Accounting And Analysis",
		"Financial Institutions And Services", "Financial Management",
		"Fine Arts", "Finite Element And Modeling Methods",
		"Finite Element Methods",
		"Finite Element Methods In Civil Engineering", "Finnish",
		"Flight Mechanics I", "Flight Mechanics II",
		"Flight Vehicle Design", "Flight Vehicle Design Lab",
		"Fluidization Engineering", "Fluid Mechanics",
		"Fluid Mechanics And Hydraulic Machinery",
		"Fluid Mechanics And Hydraulic Machinery Lab",
		"Food Science And Technology",
		"Formal Languages And Automata Theory",
		"Foundation Of Solid Mechanics", "French", "Gaelic",
		"Genealogy", "Genetic Engineering", "Genetics", "Geography",
		"Geology", "Geology", "Geophysics",
		"Geotechnical Engineering I", "Geotechnical Engineering II",
		"Geotechnical Engineering Lab", "German", "Gis And Cad Lab",
		"Global Hrm", "Gnosticism", "Gnostic Theology", "Graphic Arts",
		"Graphic Design", "Graphic Technologies", "Greek",
		"Ground Improvement Techniques",
		"Ground Water Development And Management",
		"Health Education And Pathophysiology", "Heat Transfer",
		"Heat Transfer And Instrumentation Lab",
		"Heat Transfer In Bioprocesses", "Heat Transfer Lab",
		"Helicopter Engineering", "High Voltage Engineering", "Hindi",
		"History", "History Of Art", "Hospital System Management",
		"Hotel Management", "Human Computer Interaction", "Humanities",
		"Human Resource Management", "Hungarian", "Hvdc Transmission",
		"Hydraulic And Pneumatic Control Systems",
		"Hydraulic Machines And Production Technology Lab",
		"Hydraulics And Hydraulic Machinery",
		"Hydraulics And Pneumatic Control Systems",
		"Hypersonic Aerodynamics", "Hypnotherapy",
		"Ic And Pulse And Digital Circuits Lab", "Ic Application Lab",
		"Ic Applications And Ecad Lab", "Ic Applications Lab",
		"Image Processing", "Image Processing And Pattern Recognition",
		"Immunology", "Immunology Lab", "Industrial Aerodynamics",
		"Industrial Biotechnology", "Industrial Electronics",
		"Industrial Instrumentation", "Industrial Instrumentation Lab",
		"Industrial Management",
		"Industrial Pollution Control Engineering",
		"Industrial Safety And Hazard Management",
		"Industrial Training And Seminar",
		"Industrial Waste And Waste Water Management",
		"Information Retrieval Systems", "Information Security",
		"Information Systems Management",
		"Information Systems Software", "Information Technology",
		"Information Technology Engineering",
		"Information Technology II Lab",
		"Information Technology I –Lab",
		"Information Technology Management",
		"Instrumental Methods Of Analysis",
		"Instrumental Methods Of Analysis Lab", "Instrumentation",
		"Instrumentation And Bio Process Control",
		"Instrumentation And Control In Manufacturing Systems",
		"Instrumentation And Control System Components",
		"Instrumentation And Control Systems",
		"Instrumentation Lab – I", "Instrumentation Lab – II",
		"Instrumentation LabIII", "Interactive Computer Graphics",
		"Interactive Computer Graphics", "Interior Design",
		"International Business", "International Financial Management",
		"International Law", "International Marketing",
		"International Studies", "International Trade And Business",
		"Internet Affiliate Management",
		"Internet Affiliate Marketing", "Internet Commerce",
		"Internet Investment Strategies", "Internet Law",
		"Internet Marketing", "Internet Publicity Management",
		"Internet Sales And Marketing",
		"Internet Security Development",
		"Internet Security Management",
		"Internet Software Development", "Internet Strategic Planning",
		"Internet Systems Management", "Internet Website Development",
		"Introduction To Space Technology",
		"Introduction To Technologymanagement", "Italian", "Japanese",
		"Java Lab", "Kinematics Of Machinery", "Knowledge Management",
		"Korean", "Language Studies",
		"Lasers And Fiber Optics In Medicine", "Latvian", "Law",
		"Letters", "Liberal Arts", "Library Sciences",
		"Linear And Digital Ic Applications", "Linear Ic Applications",
		"Linear Systems Analysis", "Literature", "Lithuanian",
		"Litigation", "Logic", "Logistic And Supply Chain Management",
		"Machine Drawing", "Machine Tools",
		"Machine Tools And Metrology", "Management",
		"Management Information Systems",
		"Management Of Change And Development",
		"Management Of Industrial Relations", "Management Science",
		"Management Theory And Practice", "Managerial Economics",
		"Managerial Economics And Financial Analysis", "Mandarin",
		"Maritime Law", "Maritime Sciences", "Maritime Studies",
		"Marketing", "Marketing Management",
		"Mass Transfer And Separation", "Mass Transfer Operations I",
		"Mass Transfer Operations – II",
		"Mass Transfer Operations Lab",
		"Material Science For Chemical Engineers",
		"Mathematical Foundations Of Computer Science",
		"Mathematical Methods", "Mathematics", "Mathematics 1a",
		"Mathematics 1b", "Mathematics 2a", "Mathematics 2b",
		"Mathematics For Aerospace Engineers", "Mathematics I",
		"Mathematics II", "Mathematics III",
		"Mechanical Unit Operations", "Mechanical Unit Operations Lab",
		"Mechanics", "Mechanics Of Fluids", "Mechanics Of Solids",
		"Mechanics Of Solids And Mechanics Of Fluids Lab",
		"Mechanics Of Solids And Metallurgy Lab",
		"Mechanisms And Mechanical Design", "Mechatronics",
		"Media Studies", "Medical Imaging Techniques",
		"Medical Imaging Techniques Lab", "Medical Informatics",
		"Medicinal Chemistry – I", "Medicinal Chemistry II",
		"Medicinal Chemistry III", "Membrane Technology",
		"Mercantile Administration", "Mercantile Management",
		"Mercantile Studies", "Metabolic Engineering",
		"Metallurgy And Material Science",
		"Metallurgy And Mechanics Of Solids Lab", "Metaphysics",
		"Metrology And Machine Tools Lab",
		"Metrology And Surface Engineering", "Microbiology",
		"Micro Controllers And Applications",
		"Micro Electro Mechanical Systems",
		"Microprocessor And Interfacing",
		"Microprocessors And Interfacing",
		"Microprocessors And Interfacing Lab",
		"Microprocessors And Microcontrollers",
		"Micro Processors And Micro Controllers",
		"Microprocessors And Microcontrollers Lab",
		"Microprocessors Lab",
		"Microwave And Optical Communications Lab",
		"Microwave Engineering", "Middleware Technologies",
		"Military Science", "Military Studies", "Mobile Computing",
		"Modern Art", "Modern Languages", "Modern Literature",
		"Molecular Biology",
		"Molecular Biology And Genetic Engineering Lab",
		"Molecular Modeling And Drug Design", "Momentum Transfer",
		"Momentum Transfer Lab",
		"Multimedia And Application Development",
		"Multimedia And Application Development Lab",
		"Multimedia Databases", "Multimedia Design And Application",
		"Multimedia Development", "Multimedia Investment Strategies",
		"Multimedia Marketing", "Multimedia Publicity Management",
		"Multimedia Sales And Marketing",
		"Multimedia Security Development",
		"Multimedia Security Management",
		"Multimedia Software Development",
		"Multimedia Strategic Planning",
		"Multimedia Systems Management", "Music", "Nano Biotechnology",
		"Nanotechnology", "Nano-Technology", "Nautical Sciences",
		"Nautical Studies", "Network Management Systems",
		"Network Programming", "Network Programming Lab",
		"Neural Networks", "Neural Networks And Fuzzy Logic",
		"Neural Networks And Fuzzy Logic Systems",
		"Non Conventional Sources Of Energy", "Norwegian",
		"Novel Drug Delivery Systems And Regulatory Affairs",
		"Object Oriented Analysis And Design",
		"Object Oriented Programming",
		"Object Oriented Programming Lab", "Oceanography",
		"Operating System",
		"Operating Systems And Compiler Design Lab",
		"Operations Research", "Optical Communications",
		"Optimization Of Chemical Processes",
		"Optimization Techniques",
		"Opto – Electronic And Laser Instrumentation",
		"Ordnance Survey", "Organic Chemistry",
		"Organizational Behavior", "Organization Communication",
		"Oria", "Ornithology", "Paranormal Studies", "Parapsychology",
		"Pattern Recognition", "Pavement Analysis And Design",
		"PC Based Instrumentation",
		"Pc Based Instrumentation And Control",
		"Performance Management", "Personal Fitness Coaching",
		"Petroleum And Petro Chemical Technology",
		"Pharmaceutical Analysis I", "Pharmaceutical Analysis – II",
		"Pharmaceutical Biochemistry", "Pharmaceutical Biotechnology",
		"Pharmaceutical Jurisprudence", "Pharmaceutical Microbiology",
		"Pharmaceutical Organic Chemistry I",
		"Pharmaceutical Organic Chemistry – II",
		"Pharmaceutical Technology –I", "Pharmaceutical Technology II",
		"Pharmaceutical Unit Operations –I",
		"Pharmaceutical Unit Operations II", "Pharmacognosy – I",
		"Pharmacognosy –II", "Pharmacognosy III", "Pharmacology I",
		"Pharmacology II", "Pharmacology III",
		"Pharmacy Administration", "Pharm Inorganic Chemistry",
		"Philosophy", "Photography", "Physical Pharmacy – I",
		"Physical Pharmacy – II", "Physics",
		"Physiological Systems Modeling",
		"Phytochemicals And Herbal Medicine", "Plant Biotechnology",
		"Plant Tissue Culture Lab", "Polish", "Political History",
		"Political Philosophy", "Political Science",
		"Political Studies", "Politics", "Polymer Technology",
		"Portuguese", "Power Electronics",
		"Power Electronics And Simulation Lab",
		"Power Plant Engineering", "Power Plant Instrumentation",
		"Power Plant Instrumentation And Control",
		"Power Semiconductor Drives", "Power System Analysis",
		"Power System Operation And Control", "Power Systems I",
		"Power Systems II", "Prestressed Concrete",
		"Prime Movers And Mechanical Components",
		"Principles Of Communications",
		"Principles Of Entrepreneurship",
		"Principles Of Programming Languages",
		"Probability And Statistical Applications",
		"Probability And Statistics",
		"Probability Theory And Stochastic Processes",
		"Process Control", "Process Control Instrumentation",
		"Process Control Lab", "Process Dynamics And Control",
		"Process Dynamics And Control Lab", "Process Heat Transfer",
		"Process Heat Transfer Lab", "Process Instrumentation",
		"Process Modeling And Simulation",
		"Product Design And Assembly Automation",
		"Production And Operations Management",
		"Production Drawing Practice And Instrumentation Lab",
		"Production Planning And Control", "Production Technology",
		"Production Technology Lab", "Programmable Logic Controllers",
		"Project Management", "Propellant Technology",
		"Property Development", "Property Management", "Psycology",
		"Public Administration", "Publicity Management",
		"Public Relations", "Pulse And Digital Circuits",
		"Pulse And Digital Circuits Lab",
		"Pulse Circuits And Ic Applications Lab",
		"Quantitative Analysis For Business Decision",
		"Quantitative Engineering Physiology", "Radar Systems",
		"Real Estate Development", "Real Estate Management",
		"Realty Development", "Realty Management",
		"Realty Project Management",
		"Refrigeration And Air Conditioning",
		"Rehabilitation Engineering",
		"Reliability Engineering And Application To Power Systems",
		"Religion", "Religious Studies",
		"Remedial Mathematics/ Remedial Biology",
		"Remote Sensing And Gis Applications",
		"Research And Development", "Research Methodology",
		"Residential Property Management", "Resource Management",
		"Robotics", "Robotics And Automation", "Rockets And Missiles",
		"Russian", "Sales And Distribution Management",
		"Sales And Marketing", "Sanskrit", "Satellite Communications",
		"Security Analysis And Portfolio Management",
		"Sensors And Signal Conditioning", "Serbo-Croat",
		"Services Marketing", "Signals And Systems", "Simulation Lab",
		"Social Sciences", "Social Studies", "Sociology",
		"Software Compilation", "Software Creation", "Software Design",
		"Software Engineering", "Software Management",
		"Software Programming", "Software Project Management",
		"Software Research And Development",
		"Software Testing Methodologies",
		"Soil Dynamics And Machine Foundations", "Space Mechanics",
		"Spanish", "Special Event Management", "Sports Management",
		"Statistical Methods And Computer Applications",
		"Strategic Investment And Financing Decisions",
		"Strategic Management", "Strategic Sciences",
		"Strategic Studies", "Strength Of Materials I",
		"Strength Of Materials II", "Strength Of Materials Lab",
		"Structural Analysis And Detailed Design",
		"Structural Analysis And Detailed Design Lab",
		"Structural Analysis I", "Structural Analysis II",
		"Structural Biology", "Surveying", "Surveying Lab I",
		"Surveying Lab II", "Swedish", "Switch Gear And Protection",
		"Switching Theory And Logic Design",
		"System Modeling And Simulation", "Systems Audit", "Tamil",
		"Technical Science", "Technical Studies",
		"Technology Development", "Technology Engineering",
		"Technology Management",
		"Technology Of Pharmaceuticals And Fine Chemicals",
		"Telecommunication Switching Systems", "Telemedicine",
		"Telemetry And Telecontrol", "Television Engineering",
		"Telugu", "Thai", "Theology", "Therapy",
		"Thermal Engineering I", "Thermal Engineering II",
		"Thermal Engineering Lab", "Thermodynamics",
		"Thermodynamics For Biotechnologists",
		"Tourism And Hotel Management",
		"Tourism And Special Event Management", "Tourism Management",
		"Traffic Engineering", "Transducers",
		"Transducers And Instrumentation Lab", "Transducers Lab",
		"Transportation Engineering",
		"Transportation Phenomena In Living Systems",
		"Transport Phenomena", "Transport Phenomena In Bio Processes",
		"Tribology", "Turkish", "Unconventional Machining Processes",
		"Unix And Shell Programming", "Unix And Shell Programming Lab",
		"Unix Programming", "Urdu", "Utilization Of Electrical Energy",
		"Vehicle Body Engg And Safety", "Vehicle Dynamics",
		"Vehicle Transport Management 	",
		"Vibrations And Structural Dynamics", "Vietnamese",
		"Virtual Instrumentation", "Virtual Reality", "Vlsi Design",
		"Water Resources Engineering I",
		"Water Resources Engineering–II",
		"Water Resources System Planning And Management",
		"Water Shed Management", "Web Technologies",
		"Web Technologies Lab", "Welsh",
		"Wireless Communications And Networks", "Women's Studies",
		"Zoology" };
	
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
