package world;

/**
 * CreateWorld will be called only if there is not a world already saved in the
 * current directory specified in World. It will create rooms and exits for
 * rooms, and make sure that the exits are directed towards another valid room.
 * It also creates the MOB's along with any gear that the world has.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class CreateWorld {

	private World world = World.getInstance();

	/**
	 * This is the main method for creating the world. It will call one method
	 * that will create all objects that the world has.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void createWorld() {

		// defaultWorld();
		buildRooms();
		// world.saveWorld();

	}

	/**
	 * Test method, this will not be used.
	 */
	public void defaultWorld() {
		Room zero = world.createRoom("Home Room");
		world.addToWorld(zero);

		Room central = world.createRoom("Central Chamber");
		central.setDescription("This is the Central Room.");
		world.addToWorld(central);

		Room north = world.createRoom("North Wing");
		north.setDescription("This is the North Wing");
		world.addToWorld(north);

		Room east = world.createRoom("East Wing");
		east.setDescription("This is the East Wing");
		world.addToWorld(east);

		Room south = world.createRoom("South Wing");
		south.setDescription("This is the South Wing");
		world.addToWorld(south);

		Room west = world.createRoom("West Wing");
		north.setDescription("This is the West Wing");
		world.addToWorld(west);

		central.setExitDestination(Direction.NORTH, north);
		central.setExitDestination(Direction.EAST, east);
		central.setExitDestination(Direction.SOUTH, south);
		central.setExitDestination(Direction.WEST, west);

		south.setExitDestination(Direction.NORTH, central);
		west.setExitDestination(Direction.EAST, central);
		north.setExitDestination(Direction.SOUTH, central);
		east.setExitDestination(Direction.WEST, central);

		Weapon laser = new Weapon("STING", "A laser sword from days of old.");
		world.addToWorld(laser);

		laser.setLocation(north);
		north.add(laser);

		Room environment = world.createRoom("Environment Room");
		zero.add(environment);
		environment.add(central);
		environment.add(north);
		environment.add(east);
		environment.add(south);
		environment.add(west);
		world.addToWorld(environment);

		System.out.println("Central Room");
		System.out.println(central.generateDescription());
		System.out.println("Env Room's Rooms");
		System.out.println(environment.listRooms());
		System.out.println();
	}

	/**
	 * This is the main build method for the CreateWorld class. It will be used
	 * when there isn't a already existing world. It creates all objects that
	 * the world will have, including rooms, MOB's, items, etc.
	 */
	public void buildRooms() {

		// Camp
		Room zero = world.createRoom("Home Room");

		Room landingPad = world.createRoom("Base Camp: Landing Pad");
		landingPad
				.setDescription("Ships fly in low before disgorging their "
						+ "small contingent of equipment, supplies, and more rarely, "
						+ "people. The landing pads are surrounded with constantly "
						+ "swirling dust, driving most new arrivals northward into the "
						+ "shelter of the walled camp amidst the featureless wastes.");

		Room campSquare = world.createRoom("Base Camp: Camp Square");
		landingPad.setExitDestination(Direction.NORTH, campSquare);
		campSquare.setExitDestination(Direction.SOUTH, landingPad);
		campSquare
				.setDescription("The camp bustles with motion. Here "
						+ "inside the walls the storm of wind and dust outside is "
						+ "subdued, but not entirely abated. If one wanted to get out "
						+ "of the blowing grit, they'd likely need to head inside the "
						+ "sturdy pre-fabricated buildings that fill the camp. Few "
						+ "people cross the square unless traveling between the Landing "
						+ "Pads to the south, the barracks to the west, the armory to "
						+ "the east, or the camp's gates to the north.");

		Room campGate = world.createRoom("Base Camp: Camp Gate");
		campSquare.setExitDestination(Direction.NORTH, campGate);
		campGate.setExitDestination(Direction.SOUTH, landingPad);
		campGate
				.setDescription("Guards and emplaced guns keep the camp safe "
						+ "from outside threats, but once you walk north into the wastes, "
						+ "you're on your own. the core of the mercenary camp lies deeper "
						+ "behind the walls, south.");

		Room baseCampEnvironment = world
				.createRoom("You shouldn't be seeing this!");
		baseCampEnvironment.add(landingPad);
		baseCampEnvironment.add(campSquare);
		baseCampEnvironment.add(campGate);

		// end camp creation.

		// Add bunkers
		Room landingPadBunker = world
				.createRoom("Base Camp: Landing Pad Bunker");

		landingPadBunker
				.setDescription("A ladder in the middle of the room. The walls are made of steel, "
						+ "and there are storage crates all around you.");

		landingPad.setExitDestination(Direction.DOWN, landingPadBunker);
		landingPadBunker.setExitDestination(Direction.UP, landingPad);

		Room campSquareBunker = world
				.createRoom("Base Camp: Camp Square Bunker");
		campSquareBunker
				.setDescription("A ladder in the middle of the room. Four officer desks in each corner of the room. "
						+ "A visual display of the surrounding waste lands glows from a monitor hanging on the west wall. "
						+ "All the lifeforms and vehicles in the area can be seen there. The walls are made of steel.");

		Room campSquareBunkerEast = world
				.createRoom("Base Camp: Camp Square Bunker East");
		campSquareBunkerEast.setDescription("Storage crates all around.");
		campSquareBunker.setExitDestination(Direction.EAST,
				campSquareBunkerEast);
		campSquareBunkerEast.setExitDestination(Direction.WEST,
				campSquareBunker);

		campSquare.setExitDestination(Direction.DOWN, campSquareBunker);
		campSquareBunker.setExitDestination(Direction.UP, campSquare);
		campSquareBunker.setExitDestination(Direction.SOUTH, landingPadBunker);
		landingPadBunker.setExitDestination(Direction.NORTH, campSquareBunker);

		Room campGateBunker = world.createRoom("Base Camp: Camp Gate Bunker");
		campGateBunker
				.setDescription("A ladder in the middle of the room. The walls are made of steel, "
						+ "and there are storage crates all around you.");

		campSquareBunker.setExitDestination(Direction.NORTH, campGateBunker);
		campGate.setExitDestination(Direction.DOWN, campGateBunker);
		campGateBunker.setExitDestination(Direction.UP, campGate);
		campGateBunker.setExitDestination(Direction.SOUTH, campSquareBunker);

		baseCampEnvironment.add(landingPadBunker);
		baseCampEnvironment.add(campSquareBunker);
		baseCampEnvironment.add(campSquareBunkerEast);
		baseCampEnvironment.add(campGateBunker);

		// end of Base Camp

		Room nearWaste = world.createRoom("Wastelands: The Near Wastes");
		campGate.setExitDestination(Direction.NORTH, nearWaste);
		nearWaste.setExitDestination(Direction.SOUTH, campGate);
		nearWaste
				.setDescription("The tamest of the wastelands, this area is high "
						+ "in traffic from the mercenary camp, and any larger fauna tend to "
						+ "be dealt with swiftly. Only the occasional giant scorpion can be "
						+ "found crawling around, attacking people for sustenance - And all "
						+ "the juicy water they carry.");

		Room wasteland = world.createRoom("Wastelands: The Wasteland");
		nearWaste.setExitDestination(Direction.NORTH, wasteland);
		wasteland.setExitDestination(Direction.SOUTH, nearWaste);
		wasteland
				.setDescription("A vast expanse of dirt and dust, and little else. These "
						+ "rolling hills are blasted by sand - but, plenty more blowing in "
						+ "seems to keep the general amount of grit constant. The distant "
						+ "camp to the south is a mere shadow, and a deep canyon falls away "
						+ "to the west. The wastes grow deeper north, and a faint shape looms "
						+ "in the haze to the east.");

		Room crashSite = world.createRoom("Wastelands: Crash Site");
		wasteland.setExitDestination(Direction.EAST, crashSite);
		crashSite.setExitDestination(Direction.WEST, wasteland);
		crashSite
				.setDescription("A massive cargo-hauling vessel has crashed into the dunes here. "
						+ "It has a large central spine which connects the forward bridge and rear "
						+ "engines pods together, both of which tower a half dozen stories tall. "
						+ "Hanging from the spine are enormous cargo containers the size of small "
						+ "buildings. one pod has a gaping crack in the side.");

		Room deepWaste = world.createRoom("Wastelands: The Deep Wastes");
		wasteland.setExitDestination(Direction.NORTH, deepWaste);
		deepWaste.setExitDestination(Direction.SOUTH, wasteland);
		deepWaste
				.setDescription("The terrible blowing winds that howl across the wastes are all "
						+ "the worse here. Giant rock formations jut skyward, eerily symmetrical, "
						+ "but their shapes do nothing to abate the winds - Possibly even amplifying "
						+ "it as dust blows between the pillars.");

		Room caveMouth = world.createRoom("Wastelands: Cave Mouth");
		nearWaste.setExitDestination(Direction.WEST, caveMouth);
		caveMouth.setExitDestination(Direction.EAST, nearWaste);
		caveMouth
				.setDescription("The land almost seems to fall away walking west from the near "
						+ "wastes, descending into a miles-wide bowl in the earth. The bowl seems to "
						+ "pour north into a deep canyon carved out by a long-forgotten river, but "
						+ "tucked away in the bowl's western ridge is a gaping cave mouth, pitch black "
						+ "within, that howls in the passing winds.");

		Room cave = world.createRoom("Wastelands: Cave");
		caveMouth.setExitDestination(Direction.WEST, cave);
		cave.setExitDestination(Direction.EAST, caveMouth);
		cave
				.setDescription("The land almost seems to fall away walking west from the near "
						+ "wastes, descending into a miles-wide bowl in the earth. The bowl seems to "
						+ "pour north into a deep canyon carved out by a long-forgotten river, but "
						+ "tucked away in the bowl's western ridge is a gaping cave mouth, pitch black "
						+ "within, that howls in the passing winds.");

		Room canyon = world.createRoom("Wastelands: Canyon");
		caveMouth.setExitDestination(Direction.NORTH, canyon);
		canyon.setExitDestination(Direction.SOUTH, caveMouth);
		canyon
				.setDescription("A deep canyon runs through the wasteland, travelling from a depression " +
						"in the south to a massive cavern in the north.");

		Room cavern = world.createRoom("Wastelands: Cavern");
		canyon.setExitDestination(Direction.NORTH, cavern);
		cavern.setExitDestination(Direction.SOUTH, canyon);
		cavern
				.setDescription("This cave surrounds a gigantic spire of rock, glinting semiprecious " +
						"stones jutting out, exposed by long-gone water flows. A cave mouth leads out " +
						"south at the base of the spire, while a curving ramp slwoly winds upwards " +
						"around the core.");

		Room lookOut = world.createRoom("Wastelands: Lookout");
		cavern.setExitDestination(Direction.UP, lookOut);
		lookOut.setExitDestination(Direction.DOWN, cavern);
		lookOut.setExitDestination(Direction.EAST, deepWaste);
		deepWaste.setExitDestination(Direction.WEST, lookOut);
		lookOut
				.setDescription("This high spire allows one to see out over the distant wastes, and " +
						"the massive canyon to the south. Far north, craggy mountains are visible " +
						"through the haze, and a strange, organic-lookign structure nestles among them. " +
						"Far to the east, some sort of crashed vessel can be seen in the sands.");

		Room wastelandEnvironment = world
				.createRoom("The wasteland environment room.");

		wastelandEnvironment.add(nearWaste);
		wastelandEnvironment.add(wasteland);
		wastelandEnvironment.add(crashSite);
		wastelandEnvironment.add(deepWaste);
		wastelandEnvironment.add(caveMouth);
		wastelandEnvironment.add(cave);
		wastelandEnvironment.add(canyon);
		wastelandEnvironment.add(cavern);
		wastelandEnvironment.add(lookOut);

		// end Wasteland building block
		// begin crashedShip building block.

		Room brokenCargoPod = world
				.createRoom("Crashed Ship: Broken Cargo Pod");
		crashSite.setExitDestination(Direction.EAST, brokenCargoPod);
		brokenCargoPod.setExitDestination(Direction.WEST, crashSite);
		brokenCargoPod
				.setDescription("Inside of the cargo container, the walls are stripped bare - "
						+ "Only fitting, given how easy it was to get in here. A seemingly long-broken "
						+ "freight elevator leads up to the central corridor in the vessel's spine,"
						+ " however the gantry around the broken elevator could be climbed.");

		Room shipsSpineNorth = world
				.createRoom("Crashed Ship: North Spinal Corridor");
		brokenCargoPod.setExitDestination(Direction.UP, shipsSpineNorth);
		shipsSpineNorth.setExitDestination(Direction.DOWN, brokenCargoPod);
		shipsSpineNorth
				.setDescription("Once space-worthy, the corridor is now anything but. Large gashes "
						+ "and tears in the cielings allow bands of light to drift into the corridors, "
						+ "filled with sand. On either side of the hall large freight elevators lead "
						+ "down into the cargo pods. The hall continues south towards the engines, and "
						+ "north eventually comes to the pried-open doors of the bridge-module.");

		Room shipsSpineSouth = world
				.createRoom("Crashed Ship: South Spinal Corridor");
		shipsSpineNorth.setExitDestination(Direction.SOUTH, shipsSpineSouth);
		shipsSpineSouth.setExitDestination(Direction.NORTH, shipsSpineNorth);
		shipsSpineNorth
				.setDescription("Once space-worthy, the corridor is now anything but. Large gashes "
						+ "and tears in the cielings allow bands of light to drift into the corridors, "
						+ "filled with sand. On either side of the hall large freight elevators lead "
						+ "down into the cargo pods. The hall continues south until it hits a sealed "
						+ "door to the engine pods, and continues north.");

		Room cargoPod = world.createRoom("Crashed Ship: Cargo Pod Nest");
		shipsSpineSouth.setExitDestination(Direction.DOWN, cargoPod);
		cargoPod.setExitDestination(Direction.UP, shipsSpineSouth);
		cargoPod
				.setDescription("Coming down the gantry, this cargo pod is a mess - But at least "
						+ "it seems sealed against the elements. Something else seems to have decided "
						+ "to take advantage of this, with scraps of hull and cargo containers with "
						+ "acid-seared edges forming some sort of nest in one corner. Just outside the"
						+ " strange formation, goods from the long-gone containers lay strewn about.");

		Room crashedShipEnvironment = world
				.createRoom("The Crashed Ship environment room.");

		crashedShipEnvironment.add(brokenCargoPod);
		crashedShipEnvironment.add(shipsSpineNorth);
		crashedShipEnvironment.add(shipsSpineSouth);
		crashedShipEnvironment.add(cargoPod);

		// Remaining: Bridge?

		// end CrashedShip

		// Begin Mountains

		Room footHills = world.createRoom("The Mountains: Foothills");
		deepWaste.setExitDestination(Direction.NORTH, footHills);
		footHills.setExitDestination(Direction.SOUTH, deepWaste);
		footHills
				.setDescription("These foothills mark the split between the sandy wastes and the "
						+ "craggy mountains of the north.");

		Room treacherousTrail = world
				.createRoom("The Mountains: Treacherous Trail");
		footHills.setExitDestination(Direction.NORTH, treacherousTrail);
		treacherousTrail.setExitDestination(Direction.SOUTH, footHills);
		treacherousTrail
				.setDescription("An unreliable path winds up between the crags of the hills. "
						+ "East, jagged mountain peaks rise up and make passage treacherous. A wide "
						+ "highland plains spreads out to the west.");
		// Begin maze

		String Warning = "[WARNING: Exits in this maze do not seem to connect logically! If at any time "
				+ "you become hopelessly lost, type 'Down' to return to this room.] \n \n";
		String baseDescription = "Twisty little canyons all alike lead off in many directions, winding "
				+ "over and under eachother to make your sense of direction unreliable. \n";

		//SW Room
		Room mazeCanyon = world
				.createRoom("The Mountains: Maze-Like Canyon (SW Corner)");
		treacherousTrail.setExitDestination(Direction.EAST, mazeCanyon);
		mazeCanyon.setExitDestination(Direction.WEST, treacherousTrail);
		mazeCanyon.setDescription(Warning + baseDescription
				+ "The only safe path out is to the West.");

		//W Room
		Room mazeCanyonWest = world
				.createRoom("The Mountains: Maze-Like Canyon (W Edge)");
		mazeCanyonWest.setExitDestination(Direction.DOWN, mazeCanyon);
		mazeCanyonWest
				.setDescription(Warning
						+ baseDescription
						+ "You can see a trail leading up the back of a mountain to the north, but aren't sure how to get there.");

		//NW Room
		Room mazeCanyonNorthWest = world
				.createRoom("The Mountains: Maze-Like Canyon (NW Corner)");
		mazeCanyonNorthWest.setExitDestination(Direction.DOWN, mazeCanyon);
		mazeCanyonNorthWest
				.setDescription(Warning
						+ baseDescription
						+ "A trail leads up the side of a mountain here. A long slope leads south, "
						+ "and actually seems to go that way. A simple trail to the east seems"
						+ "like it might not be decieving you as well.");

		//N Room
		Room mazeCanyonNorth = world
				.createRoom("The Mountains: Maze-Like Canyon (N Edge)");
		mazeCanyonNorth.setExitDestination(Direction.DOWN, mazeCanyon);
		mazeCanyonNorth.setDescription(Warning + baseDescription);

		//Center Room
		Room mazeCanyonCenter = world
				.createRoom("The Mountains: Maze-Like Canyon (Center)");
		mazeCanyonCenter.setExitDestination(Direction.DOWN, mazeCanyon);
		mazeCanyonCenter
				.setDescription(Warning
						+ baseDescription
						+ "This is possibly the most "
						+ "confusing nexus of trails and pathways you have ever seen. GPS gear tends to "
						+ "give up and explode, while maps burst into flame without warning.");

		//S Room
		Room mazeCanyonSouth = world
				.createRoom("The Mountains: Maze-Like Canyon (S Edge)");
		mazeCanyonSouth.setExitDestination(Direction.DOWN, mazeCanyon);
		mazeCanyonSouth.setDescription(Warning + baseDescription);

		mazeCanyonSouth
				.setDescription(Warning
						+ baseDescription);

		//SE Room
		Room mazeCanyonSouthEast = world
				.createRoom("The Mountains: Maze-Like Canyon (SE Corner)");
		mazeCanyonSouthEast.setExitDestination(Direction.DOWN, mazeCanyon);
		mazeCanyonSouthEast.setDescription(Warning + baseDescription);

		//E Room
		Room mazeCanyonEast = world
				.createRoom("The Mountains: Maze-Like Canyon (E Edge)");
		mazeCanyonEast.setExitDestination(Direction.DOWN, mazeCanyon);

		mazeCanyonEast.setDescription(Warning + baseDescription);

		//NE Room
		Room mazeCanyonNorthEast = world
				.createRoom("The Mountains: Maze-Like Canyon (NE Corner)");
		mazeCanyonNorthEast.setExitDestination(Direction.DOWN, mazeCanyon);
		mazeCanyonNorthEast.setDescription(Warning + baseDescription);
		
		//Exits from the SW Corner:
		mazeCanyon.setExitDestination(Direction.NORTH, mazeCanyonWest);
		mazeCanyon.setExitDestination(Direction.EAST, mazeCanyonCenter);
		
		//Exits from the W Edge:
		mazeCanyonWest.setExitDestination(Direction.WEST, mazeCanyonEast);
		mazeCanyonWest.setExitDestination(Direction.EAST, mazeCanyonSouth);
		
		//Exits from NW Corner:
		mazeCanyonNorthWest.setExitDestination(Direction.SOUTH, mazeCanyonWest);
		mazeCanyonNorthWest.setExitDestination(Direction.EAST, mazeCanyonNorth);
		
		//Exits from N Edge:
		mazeCanyonNorth.setExitDestination(Direction.NORTH, mazeCanyonNorthEast);
		mazeCanyonNorth.setExitDestination(Direction.SOUTH, mazeCanyonCenter);
		
		//Exits from Center:
		mazeCanyonCenter.setExitDestination(Direction.NORTH, mazeCanyonEast);
		mazeCanyonCenter.setExitDestination(Direction.EAST, mazeCanyonSouth);
		mazeCanyonCenter.setExitDestination(Direction.SOUTH, mazeCanyonWest);
		mazeCanyonCenter.setExitDestination(Direction.WEST, mazeCanyonNorth);
		
		//Exits from S Edge
		mazeCanyonSouth.setExitDestination(Direction.EAST, mazeCanyon);
		mazeCanyonSouth.setExitDestination(Direction.WEST, mazeCanyonSouthEast);
		mazeCanyonSouth.setExitDestination(Direction.SOUTH, mazeCanyonSouthEast);
		
		//Exits from SE Corner
		mazeCanyonSouthEast.setExitDestination(Direction.WEST, mazeCanyonSouth);
		mazeCanyonSouthEast.setExitDestination(Direction.NORTH, mazeCanyonEast);
		
		//Exits from E Edge
		mazeCanyonEast.setExitDestination(Direction.NORTH, mazeCanyonNorthEast);
		mazeCanyonEast.setExitDestination(Direction.WEST, mazeCanyonCenter);
		
		//Exits from NE Corner
		mazeCanyonNorthEast.setExitDestination(Direction.SOUTH, mazeCanyonNorth);
		mazeCanyonNorthEast.setExitDestination(Direction.NORTH, mazeCanyonSouthEast);
		mazeCanyonNorthEast.setExitDestination(Direction.EAST, mazeCanyonNorthWest);
		
		//Holy Crap. yes, that's the end of the maze.
		

		// End Mountains

		// Begin Hive

		Room hiveApproach = world.createRoom("Alien Hive: Approach");
		treacherousTrail.setExitDestination(Direction.WEST, hiveApproach);
		hiveApproach.setExitDestination(Direction.EAST, treacherousTrail);
		hiveApproach
				.setDescription("A squat, organic structure lurks in the distance. A low plain "
						+ "spreads out across the highlands between the crags of the foothills and "
						+ "the distant hive. Tree-sized, barbed spines jut up from the landscape and "
						+ "seem to pulse slowly.");

		Room hivePerimeter = world.createRoom("Alien Hive: Perimeter");
		hiveApproach.setExitDestination(Direction.WEST, hivePerimeter);
		hivePerimeter.setExitDestination(Direction.EAST, hiveApproach);
		hivePerimeter
				.setDescription("Orifices of various sizes on the south side of this strange, fleshy "
						+ "structure seem to pulse as if the structure itself were breathing. This close "
						+ "to the hive, twisted creatures which serve as guards are bound to notice unwary "
						+ "mercenaries. The open land to the east, dotted with alien spires, leads back "
						+ "towards the mountains. ");

		Room hiveCathedral = world.createRoom("Alien Hive: Cathedral");
		hivePerimeter.setExitDestination(Direction.NORTH, hiveCathedral);
		hiveCathedral.setExitDestination(Direction.SOUTH, hivePerimeter);
		hiveCathedral
				.setDescription("A vaulted cieling rises overhead, ventricles and valves shunting air "
						+ "through the hive's depths, the alls themselves pulsating like a living thing. "
						+ "Many little tunnels pass south out of the hive and a wide, dark passage leads "
						+ "into the rocky heart of the Hive.");

		Room hiveBroodChamber = world.createRoom("Alien Hive: Brood Chamber");
		hiveCathedral.setExitDestination(Direction.NORTH, hiveBroodChamber);
		hiveBroodChamber.setExitDestination(Direction.SOUTH, hiveCathedral);
		hiveBroodChamber
				.setDescription("Grotesque egg-like sacks of fleshy membranes are spread all over "
						+ "this moist, humid chamber. They are in various sizes, likely representing "
						+ "the wide variety of creatures. A smooth tunnel, ground covered in slime, "
						+ "descends to the west, while air whistles through a small gap to the east.");

		Room hiveQueensLair = world.createRoom("Alien Hive: Queen's Lair");
		hiveBroodChamber.setExitDestination(Direction.WEST, hiveQueensLair);
		hiveQueensLair.setExitDestination(Direction.EAST, hiveCathedral);
		hiveQueensLair
				.setDescription("Strange organelles and unusual organic formations coat the dripping "
						+ "walls as the passage descends into the earth itself. From within comes a deep,"
						+ " ominous chittering, and signs of an immense, scaly-hided monster fill this "
						+ "fortified chamber.");

		Room hiveTrail = world.createRoom("Alien Hive: Hidden Trail");
		hiveBroodChamber.setExitDestination(Direction.EAST, hiveTrail);
		hiveTrail.setExitDestination(Direction.WEST, hiveBroodChamber);
		hiveTrail.setExitDestination(Direction.DOWN, mazeCanyonNorthWest);
		hiveTrail
				.setDescription("The wind howls around you on this narrow pathway, which leads up through "
						+ "the canyons and rock formations of the mountains, towards a small craggy opening "
						+ "in an enormous organic structure. The crag leads west into the Hive, while the "
						+ "trail leads back down the mountain.");

		// Exit from the Mountains, which can't be called until now.
		mazeCanyonNorthWest.setExitDestination(Direction.UP, hiveTrail);

		// //
		Room hiveBroodUnderground = world
				.createRoom("Alien Hive: Brood Chamber Underground");
		hiveBroodUnderground.setExitDestination(Direction.UP, hiveBroodChamber);
		hiveBroodChamber.setExitDestination(Direction.DOWN,
				hiveBroodUnderground);
		hiveBroodUnderground
				.setDescription("Smaller creatures, possibly runts from the nursery above, scurry around in this deep tunnel, running from everything.");

		Room hiveBroodTunnelOne = world
				.createRoom("Alien Hive: Brood Tunnel One");
		hiveBroodTunnelOne.setExitDestination(Direction.SOUTH,
				hiveBroodUnderground);
		hiveBroodUnderground.setExitDestination(Direction.NORTH,
				hiveBroodTunnelOne);
		hiveBroodTunnelOne
				.setDescription("A dark tunnel, a faint glow coming form the North.");

		Room hiveBroodTunnelTwo = world
				.createRoom("Alien Hive: Brood Tunnel Two");
		hiveBroodTunnelTwo.setExitDestination(Direction.SOUTH,
				hiveBroodTunnelOne);
		hiveBroodTunnelOne.setExitDestination(Direction.NORTH,
				hiveBroodTunnelTwo);
		hiveBroodTunnelTwo
				.setDescription("A tunnel, a glow coming form the North.");

		Room hiveBroodTunnelThree = world
				.createRoom("Alien Hive: Brood Tunnel Three");
		hiveBroodTunnelThree.setExitDestination(Direction.SOUTH,
				hiveBroodTunnelTwo);
		hiveBroodTunnelTwo.setExitDestination(Direction.NORTH,
				hiveBroodTunnelThree);
		hiveBroodTunnelThree
				.setDescription("A tunnel, a bright glow coming form the West.");

		Room hiveBroodTunnelFour = world
				.createRoom("Alien Hive: Brood Tunnel Four");
		hiveBroodTunnelFour.setExitDestination(Direction.EAST,
				hiveBroodTunnelThree);
		hiveBroodTunnelThree.setExitDestination(Direction.WEST,
				hiveBroodTunnelFour);
		hiveBroodTunnelFour
				.setDescription("A tunnel, a blinding glow coming form the South.");

		Room hiveBroodTunnelCave = world
				.createRoom("Alien Hive: Brood Tunnel Cave");
		hiveBroodTunnelCave.setExitDestination(Direction.NORTH,
				hiveBroodTunnelFour);
		hiveBroodTunnelFour.setExitDestination(Direction.SOUTH,
				hiveBroodTunnelCave);
		hiveBroodTunnelCave
				.setDescription("A cave. The floor of the cave is illuminating a bliding glow. Your barely make out an item's shape in the middle of the floow.");

		// ///

		Room hiveEnvironmentRoom = world
				.createRoom("The Hive: The Treacherous Hive");
		hiveEnvironmentRoom.add(hiveApproach);
		hiveEnvironmentRoom.add(hivePerimeter);
		hiveEnvironmentRoom.add(hiveCathedral);
		hiveEnvironmentRoom.add(hiveBroodChamber);
		hiveEnvironmentRoom.add(hiveQueensLair);
		hiveEnvironmentRoom.add(hiveTrail);
		hiveEnvironmentRoom.add(hiveBroodUnderground);
		hiveEnvironmentRoom.add(hiveBroodTunnelOne);
		hiveEnvironmentRoom.add(hiveBroodTunnelTwo);
		hiveEnvironmentRoom.add(hiveBroodTunnelThree);
		hiveEnvironmentRoom.add(hiveBroodTunnelFour);
		hiveEnvironmentRoom.add(hiveBroodTunnelCave);

		// End of Hive

		// Add gear

		// Camp Gear

		world.addGearToWorld(new Weapon("Laser Pistol",
				"A clunky old laser pistol; reliable, but ugly.", 1, 3),
				campSquare);

		GearContainer captainsDesk = new GearContainer("Captain's Cabinet",
				"(The captain's gun cabinet.)", 5, false);
		world.addGearToWorld(captainsDesk, campSquareBunker);

		world
				.addGearToWorld(
						new Weapon(
								"Laser Rifle",
								"(A brand new laser rifle. It is a little heavy, but packs a punch)",
								1, 6), captainsDesk);

		world
				.addGearToWorld(
						new Weapon(
								"Plasma Gun",
								"(A brand new plasma gun. It is unwieldy, but also packs a whallop.)",
								2, 9), captainsDesk);

		world
				.addGearToWorld(
						new Weapon(
								"Vibro-Blade",
								"(A sword whose edge hums with slicing power, yearning for a Dreadnaught's gauntlets.)",
								3, 15), captainsDesk);

		world
				.addGearToWorld(
						new HealthOrb(
								"Life Orb",
								"(A health orb, adds 5 hit points when used. Regenerates every 10 seconds. Dropping it would be a bad idea.)",
								5), captainsDesk);
		world
				.addGearToWorld(
						new HealthOrb(
								"Long Life Orb",
								"(A health orb, adds 10 hit points when used. Regenerates every 10 seconds. Dropping it would be a bad idea.)",
								10), captainsDesk);

		// Gear in the Wild

		world
				.addGearToWorld(
						new Weapon(
								"Laser Rifle",
								"(A bruised and beaten laser rifle which ahs seen it's share of combat.)",
								1, 6), brokenCargoPod);

		world
				.addGearToWorld(
						new Weapon(
								"Laser Cannon",
								"(A brand new laser cannon with just a little dust on the lens.)",
								3, 12), brokenCargoPod);

		world.addGearToWorld(new Armor("Flak Vest",
				"A sturdy vest meant to protect the wearer's torso.", 2, 'M'),
				brokenCargoPod);

		world.addGearToWorld(new Armor("Regolith Suit",
				"Armor made of the planet's processed regolith (very strong).",
				2, 'H'), brokenCargoPod);

		world
				.addGearToWorld(
						new Armor(
								"Tuxedo T-Shirt and Jeans",
								"Tuxedo T-Shirt and Jeans (even your Mom won't think your funny).",
								1, 'L'), brokenCargoPod);

		// Easter-Egg Gear

		Gear orbOfSpontaneousReanimation = new HealthOrb("Reanimator",
				"An orb of spontaneous regeneration.", 99);
		world.addGearToWorld(orbOfSpontaneousReanimation, hiveBroodTunnelCave);

		world
				.addGearToWorld(
						new Weapon(
								"Plasma McCannon",
								"(An inductive weapon which directly proves your enemies to be a pile of ash, beyond "
										+ "contradiction.)", 5, 25),
						brokenCargoPod);

		world
				.addGearToWorld(
						new Armor(
								"Assault SNO Cuirass",
								"(Despite it's requirements to withstand orbital strikes, this armor appears to "
										+ "be a composite, made out of strange glowing rectangles connected by dotted lines "
										+ "in some technological pattern.)", 3,
								'H'), cave);

		world
				.addGearToWorld(
						new Weapon(
								"Malign Pattern",
								"(While initially appearing to be a sheet of paper with glittering silicon traces, "
										+ "A Dreadnaught with enough strength could bend this simple paper into an epic "
										+ "Origami Vibro-Blade.)", 5, 35),
						hiveQueensLair);

		// Add mobs.
		world.createMobile("Off-Duty Marine", "An off duty marine.",
				campSquare, new Greets());

		Mobile rover = world.createMobile("Rover", "A mechanical dog.",
				landingPad, new Mutters("Rover Woofs!"));
		((Mutters) world.getMobile("Rover").getStrategy()).setMobile(rover);
		world.addToWorld(rover);

		Mobile sandCat = world
				.createMobile(
						"Sand Cat",
						"Not exactly a feline, but this sleek, "
								+ "hard to catch creature is similar to a housecat in shape. "
								+ "Also, it has huge digging claws. Weird.",
						landingPad,
						new Mutters(
								"Sand Cat burrows into the ground, leaving a raised trail in the earth."));
		((Mutters) world.getMobile("Sand_Cat").getStrategy())
				.setMobile(sandCat);
		world.getMobile("Sand_Cat").setStat(10, Trait.AGILITY);
		world.getMobile("Sand_Cat").setStat(2, Trait.STRENGTH);
		world.addToWorld(sandCat);

		world
				.createMobile(
						"Giant Scorpion",
						"A giant scorpion the likes of which the world has never seen.",
						cave, new PassiveAgressive());
		((Mobile) world.getMobile("Giant_Scorpion"))
				.setStat(40, Trait.STRENGTH);
		((Mobile) world.getMobile("Giant_Scorpion")).setStat(3, Trait.AGILITY);

		world
				.createMobile(
						"Small Scorpion",
						"A small scorpion the likes of which you've likely seen before.",
						nearWaste, new PassiveAgressive());
		((Mobile) world.getMobile("Small_Scorpion")).setStat(2, Trait.STRENGTH);
		((Mobile) world.getMobile("Small_Scorpion")).setStat(7, Trait.AGILITY);
		((Mobile) world.getMobile("Small_Scorpion")).setStat(15,
				Trait.MAXHITPOINTS);

		world
				.createMobile(
						"Medium Scorpion",
						"A medium scorpion the likes of which you've likely seen before.",
						canyon, new PassiveAgressive());
		((Mobile) world.getMobile("Medium_Scorpion"))
				.setStat(3, Trait.STRENGTH);
		((Mobile) world.getMobile("Medium_Scorpion")).setStat(8, Trait.AGILITY);
		((Mobile) world.getMobile("Medium_Scorpion")).setStat(16,
				Trait.MAXHITPOINTS);

		world.createMobile("Lurker",
				"A monstrous spider-like creature, whose fangs drip acid.",
				cargoPod, new Agressive());
		((Mobile) world.getMobile("Lurker")).setStat(6, Trait.STRENGTH);
		((Mobile) world.getMobile("Lurker")).setStat(6, Trait.AGILITY);
		((Mobile) world.getMobile("Lurker")).setStat(25, Trait.MAXHITPOINTS);

		world
				.createMobile(
						"Hive Scout",
						"A strange creature, like an eight-legged dog with "
								+ "dozens of solid red eyes, and chitinous body-segmets "
								+ "that turn into a whiplike tail.",
						hiveApproach, new PassiveAgressive());
		((Mobile) world.getMobile("Hive_Scout")).setStat(4, Trait.STRENGTH);
		((Mobile) world.getMobile("Hive_Scout")).setStat(25, Trait.MAXHITPOINTS);
		((Mobile) world.getMobile("Hive_Scout")).setStat(8, Trait.AGILITY);
		((Mobile) world.getMobile("Hive_Scout")).setStat(3, Trait.TOUGHNESS);

		world
				.createMobile(
						"Hive Drone",
						"A strange creature, like an eight-legged dog with "
								+ "a white face, dozens of solid red eyes, and chitinous body-segmets "
								+ "that turn into a whiplike tail.",
						hiveApproach, new PassiveAgressive());
		((Mobile) world.getMobile("Hive_Drone")).setStat(4, Trait.STRENGTH);
		((Mobile) world.getMobile("Hive_Drone")).setStat(15, Trait.MAXHITPOINTS);
		((Mobile) world.getMobile("Hive_Drone")).setStat(8, Trait.AGILITY);
		((Mobile) world.getMobile("Hive_Drone")).setStat(3, Trait.TOUGHNESS);

		world
				.createMobile(
						"Hive Guardian",
						"A strange armored beast, like a six-legged rhino with long talons and angry teeth.",
						hivePerimeter, new Agressive());
		((Mobile) world.getMobile("Hive_Guardian")).setStat(9, Trait.STRENGTH);
		((Mobile) world.getMobile("Hive_Guardian")).setStat(6, Trait.AGILITY);
		((Mobile) world.getMobile("Hive_Guardian")).setStat(7, Trait.TOUGHNESS);
		((Mobile) world.getMobile("Hive_Guardian"))
				.setStat(40, Trait.MAXHITPOINTS);

		world
				.createMobile(
						"Hive Worker",
						"A strange insectile creature with the body of a gorilla and thick armored plates moves about ",
						hivePerimeter,
						new Mutters(
								"A Hive Worker uses it's mandibles to gnaw a thick chunk "
										+ "of organic material out of the wall, and take it "
										+ "deeper into the hive before returning."));
		((Mobile) world.getMobile("Hive_Worker")).setStat(9, Trait.STRENGTH);
		((Mobile) world.getMobile("Hive_Worker")).setStat(2, Trait.AGILITY);
		((Mobile) world.getMobile("Hive_Worker")).setStat(40, Trait.MAXHITPOINTS);
		((Mutters) world.getMobile("Hive_Worker").getStrategy())
				.setMobile(world.getMobile("Hive_Worker"));

		world
				.createMobile(
						"Alien Queen",
						"This enormous creature has a bulbous body and thousands of legs, and a vaguely face-like "
								+ "construct filled with fangs and glittering ruby eyes. and often leaves dripping eggs in "
								+ "it's wake as it meanders through it's deep chamber.",
						hiveQueensLair, new Agressive());
		((Mobile) world.getMobile("Hive_Guardian")).setStat(16, Trait.STRENGTH);
		((Mobile) world.getMobile("Hive_Guardian")).setStat(6, Trait.AGILITY);
		((Mobile) world.getMobile("Hive_Guardian")).setStat(8, Trait.TOUGHNESS);
		((Mobile) world.getMobile("Hive_Guardian")).setStat(200,
				Trait.MAXHITPOINTS);

	}
}
