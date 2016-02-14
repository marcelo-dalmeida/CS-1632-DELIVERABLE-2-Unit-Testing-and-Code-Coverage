package coffeeMakerQuest;

import java.util.ArrayList;
import java.util.List;

public class Map {
	
	private Room current_room;
	
	public Map(){
		Room small_room = new Room("Small room", "Quaint sofa", "Magenta door", null, Inventory.CREAM);
		Room funny_room = new Room("Funny room", "Sad record player", "Beige door", "Massive door", null);
		Room refinanced_room = new Room("Refinanced room", "Tight pizza", "Dead door", "Smart door", Inventory.COFFEE);
		Room dumb_room = new Room("Dumb room", "Flat energy drink", "Vivacious door", "Slim door", null);
		Room bloodthisty_room = new Room("Bloodthisty room", "Beautiful bag of money", "Purple door", "Sandy door", null);
		Room rough_room = new Room("Rough room", "Perfect air hockey table", null, "Minimalist door", Inventory.SUGAR);
		
		List<Room> rooms = new ArrayList<Room>();
		
		rooms.add(small_room);
		rooms.add(funny_room);
		rooms.add(refinanced_room);
		rooms.add(dumb_room);
		rooms.add(bloodthisty_room);
		rooms.add(rough_room);
		
		for (int i = 0; i < rooms.size(); i++) {
			Room room = rooms.get(i);
			if (i != 0){
				room.setSouth_room(rooms.get(i-1));
			}
			if (i != rooms.size() - 1){
				room.setNorth_room(rooms.get(i+1));
			}
		}
		
		this.current_room = small_room;
	}
	
	public Room getCurrent_room() {
		return current_room;
	}

	public void setCurrent_room(Room current_room) {
		this.current_room = current_room;
	}
}
