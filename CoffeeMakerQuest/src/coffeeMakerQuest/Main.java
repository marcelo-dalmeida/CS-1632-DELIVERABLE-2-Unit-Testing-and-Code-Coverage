package coffeeMakerQuest;


public class Main {

	public static final String GAME_VERSION = "Coffee Maker Quest 1.01\n";
	
	public static void main(String[] args) {
		
		Map map = new Map();
		Inventory inventory = new Inventory();
		InstructionManager instruction_manager = new InstructionManager();
		
		System.out.println(GAME_VERSION);
		System.out.println(map.getCurrent_room());
		
		while(true){
			while (!instruction_manager.input()){
				System.out.println(map.getCurrent_room());
			};
			
			instruction_manager.execute(map, inventory);
				System.out.println(map.getCurrent_room());
		}	
	}

}
