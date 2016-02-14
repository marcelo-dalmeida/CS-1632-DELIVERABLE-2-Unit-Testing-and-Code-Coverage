package coffeeMakerQuest;

public class Room {

	private String name;
	private String furniture;
	private Room north_room;
	private String north_door;
	private Room south_room;
	private String south_door;
	private String hidden_item;
	
	public Room(String name, String furniture, String north_door, String south_door, String hidden_item){
		this.name = name;
		this.furniture = furniture;
		this.north_door = north_door;
		this.south_door = south_door;
		this.hidden_item = hidden_item;
	}

	public Room getNorth_room() {
		return north_room;
	}
	
	public void setNorth_room(Room north_room) {
		this.north_room = north_room;
	}
	
	public Room getSouth_room() {
		return south_room;
	}
	
	public void setSouth_room(Room south_room) {
		this.south_room = south_room;
	}
	
	public String getHidden_item() {
		return hidden_item;
	}

	@Override
	public String toString() {
		String description = "";
		
		description += "You see a " + name + ".\n";
		description += "It has a " + furniture + ".\n";
		if (north_room != null){
			description += "A " + north_door + " leads North.\n";
		}
		if (south_room != null){
			description += "A " + south_door + " leads South.\n";
		}
		
		
		return description;
	}
}
