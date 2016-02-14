package coffeeMakerQuest;

import java.util.Scanner;

public class InstructionManager {
	
	public static final String INPUT_N = "N";
	public static final String INPUT_S = "S";
	public static final String INPUT_L = "L";
	public static final String INPUT_I = "I";
	public static final String INPUT_H = "H";
	public static final String INPUT_D = "D";
	
	private String input;
	

	
	private void goNorth(Map map){
		Room current_room = map.getCurrent_room();
		if (current_room.getNorth_room() != null){
			map.setCurrent_room(current_room.getNorth_room());
		}
		else{
			System.out.print("There is no door leading North.\n\n");
		}
	}
	
	private void goSouth(Map map){
		Room current_room = map.getCurrent_room();
		if (current_room.getSouth_room() != null){
			map.setCurrent_room(current_room.getSouth_room());
		}
		else{
			System.out.print("There is no door leading South.\n\n");
		}
	}
	
	private void lookForItems(Map map, Inventory inventory){
		Room current_room = map.getCurrent_room();
		String hidden_item = current_room.getHidden_item();
		if (hidden_item != null){
			System.out.print("There might be something here...\n");
			inventory.receive(hidden_item);
		}
		else
		{
			System.out.print("You don't see anything out of the ordinary.\n\n");
		}
	}
	
	private void lookInventory(Inventory inventory){
		inventory.show();
	}
	
	private void help(){
		System.out.print("[N] to go North,\n"
				 + "[S] to go South,\n"
				 + "[L] to Look for items,\n"
				 + "[I] for Inventory,\n"
				 + "[H] for Help,\n"
				 + "[D] to Drink.\n\n");
	}
	
	private void drink(Inventory inventory){
		inventory.show();
		if(inventory.check()){
			System.out.print("You drink the beverage and are ready to study!\n");
			System.out.print("You win!\n");
			System.out.print("Exiting with error code 0\n");
			System.exit(0);
		}
		else{
			inventory.defeat_information();
			System.out.print("You lose!\n");
			System.out.print("Exiting with error code 1\n");
			System.exit(1);
		}
	}
	
	
	public void execute(Map map, Inventory inventory){
		if (this.input != null){
				switch(this.input){
				case INPUT_N:
					goNorth(map);
					break;
				case INPUT_S:
					goSouth(map);
					break;
				case INPUT_L:
					lookForItems(map, inventory);
					break;
				case INPUT_I:
					lookInventory(inventory);
					break;
				case INPUT_H:
					help();
					break;
				case INPUT_D:
					drink(inventory);
					break;
				default:
			}
			this.input = null;
		}
	}
	
	public boolean input(){
		System.out.print(" INSTRUCTIONS (N,S,L,I,H,D) >\n\n");

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		input = input.toUpperCase();
		
		switch(input){
			case INPUT_N:
			case INPUT_S:
			case INPUT_L:
			case INPUT_I:
			case INPUT_H:
			case INPUT_D:
				this.input = input;
				return true;
			default:
				System.out.print("What?\n");
				System.out.print("\n");
				this.input = null;
				return false;
		}
	}
}
