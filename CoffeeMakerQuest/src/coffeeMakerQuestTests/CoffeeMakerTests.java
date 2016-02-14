package coffeeMakerQuestTests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.mockito.Mockito;

import coffeeMakerQuest.InstructionManager;
import coffeeMakerQuest.Inventory;
import coffeeMakerQuest.Map;
import coffeeMakerQuest.Room;

public class CoffeeMakerTests {

	@Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
	
	private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private ByteArrayInputStream inContent;
	
	// The H input triggers the Help instruction
	// Information about the possible instructions should appear
	// -FUN-HELP
	@Test
	public void testHelpInput(){
		
		inContent = new ByteArrayInputStream("H".getBytes());
		System.setIn(inContent);
		System.setOut(new PrintStream(outContent));
	    
		InstructionManager instruction_manager = new InstructionManager();
		
		instruction_manager.input();
		
		instruction_manager.execute(Mockito.mock(Map.class), Mockito.mock(Inventory.class));
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
				   + "[N] to go North,\n"
				   + "[S] to go South,\n"
				   + "[L] to Look for items,\n"
				   + "[I] for Inventory,\n"
				   + "[H] for Help,\n"
				   + "[D] to Drink.\n"
				   + "\n", outContent.toString());
		
		System.setOut(System.out);
	}

	// The I input triggers the Inventory instruction
	// Information about the items in the inventory should appear
	// -FUN-INVENTORY
	@Test
	public void testInventoryInput(){
		inContent = new ByteArrayInputStream("I".getBytes());
		System.setIn(inContent);
		System.setOut(new PrintStream(outContent));
	    
		InstructionManager instruction_manager = new InstructionManager();
		Inventory inventory = new Inventory();
		
		instruction_manager.input();
		
		instruction_manager.execute(Mockito.mock(Map.class), inventory);
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
				   + "YOU HAVE NO COFFEE!\n"
				   + "YOU HAVE NO CREAM!\n"
				   + "YOU HAVE NO SUGAR!\n"
				   + "\n", outContent.toString());
		
		System.setOut(System.out);
	}

	// The D input triggers the Drink instruction
	// The check counts with an EMPTY inventory
	// Information about the items in the inventory should appear and the user should LOSE
	// -FUN-LOSE
	// http://stackoverflow.com/questions/12757559/code-coverage-in-java-with-eclemma-not-scanning-expecting-exception-methods
	@Test
	public void testDrinkInputWithEmptyInventory(){
		inContent = new ByteArrayInputStream("D".getBytes());
		System.setIn(inContent);
		System.setOut(new PrintStream(outContent));
		
		InstructionManager instruction_manager = new InstructionManager();
		Inventory inventory = new Inventory();
		
		instruction_manager.input();
		exit.expectSystemExit();
		instruction_manager.execute(Mockito.mock(Map.class), inventory);
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
				   + "YOU HAVE NO COFFEE!\n"
				   + "YOU HAVE NO CREAM!\n"
				   + "YOU HAVE NO SUGAR!\n"
				   + "\n"
			       + "You drink the air, as you have no coffee, sugar, or cream.\n"
		       	   + "The air is invigorating, but not invigorating enough. You cannot study.\n"
		       	   + "You lose!\n"
				   + "Exiting with error code 1\n", outContent.toString());
		
		System.setOut(System.out);
	}
	
	// The L input triggers the Look instruction
	// The check assume a room WITH a hidden item
	// Information about the hidden item should appear
	// -FUN-LOOK
	@Test
	public void testLookInputInARoomWithHiddenItem(){
		inContent = new ByteArrayInputStream("L".getBytes());
		System.setIn(inContent);
		System.setOut(new PrintStream(outContent));
		
		InstructionManager instruction_manager = new InstructionManager();
		Inventory inventory = new Inventory();
		
		Map mockMap = Mockito.mock(Map.class);
		Room mockRoom = Mockito.mock(Room.class);
		Mockito.when(mockMap.getCurrent_room()).thenReturn(mockRoom);
		Mockito.when(mockRoom.getHidden_item()).thenReturn(Inventory.CREAM);
		
		instruction_manager.input();

		instruction_manager.execute(mockMap, inventory);
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
			       + "There might be something here...\n"
		       	   + "You found some creamy cream!\n"
			       + "\n", outContent.toString());
		
		System.setOut(System.out);
	}
	
	// The N input triggers the go North instruction
	// It should change from Small room to Funny room
	// -FUN-ITERATION
	@Test
	public void testGoNorthInput(){
		inContent = new ByteArrayInputStream("N".getBytes());
		System.setIn(inContent);
		
		InstructionManager instruction_manager = new InstructionManager();
		Map map = new Map();
		Inventory mockInventory = Mockito.mock(Inventory.class);
		
		instruction_manager.input();

		instruction_manager.execute(map, mockInventory);
		
		Room small_room = new Room("Small room", "Quaint sofa", "Magenta door", null, Inventory.CREAM);
		Room funny_room = new Room("Funny room", "Sad record player", "Beige door", "Massive door", null);
		Room refinanced_room = new Room("Refinanced room", "Tight pizza", "Dead door", "Smart door", Inventory.COFFEE);
		
		funny_room.setSouth_room(small_room);
		funny_room.setNorth_room(refinanced_room);
	
		assertEquals(funny_room.toString(), map.getCurrent_room().toString());			
	}
	
	// The S input triggers the go South instruction
	// It should change from Funny room to Small room
	// -FUN-ITERATION
	@Test
	public void testGoSouthInput(){
		inContent = new ByteArrayInputStream("S".getBytes());
		System.setIn(inContent);
		
		InstructionManager instruction_manager = new InstructionManager();
		Map map = new Map();
		Inventory mockInventory = Mockito.mock(Inventory.class);
		
		Room small_room = new Room("Small room", "Quaint sofa", "Magenta door", null, Inventory.CREAM);
		Room funny_room = new Room("Funny room", "Sad record player", "Beige door", "Massive door", null);
		Room refinanced_room = new Room("Refinanced room", "Tight pizza", "Dead door", "Smart door", Inventory.COFFEE);
		
		small_room.setNorth_room(funny_room);
		funny_room.setSouth_room(small_room);
		funny_room.setNorth_room(refinanced_room);
	
		map.setCurrent_room(funny_room);
		
		instruction_manager.input();

		instruction_manager.execute(map, mockInventory);
		
		
		assertEquals(small_room.toString(), map.getCurrent_room().toString());			
	}
	
	// The L input triggers the Look instruction
	// The check assume a room WITHOUT a hidden item
	// Information about not finding something should appear
	// -FUN-LOOK
	@Test
	public void testLookInputInARoomWithoutHiddenItem(){
		inContent = new ByteArrayInputStream("L".getBytes());
		System.setIn(inContent);
		System.setOut(new PrintStream(outContent));
		
		InstructionManager instruction_manager = new InstructionManager();
		Inventory inventory = new Inventory();
		
		Map mockMap = Mockito.mock(Map.class);
		Room mockRoom = Mockito.mock(Room.class);
		Mockito.when(mockMap.getCurrent_room()).thenReturn(mockRoom);
		Mockito.when(mockRoom.getHidden_item()).thenReturn(null);
		
		instruction_manager.input();

		instruction_manager.execute(mockMap, inventory);
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
			       + "You don't see anything out of the ordinary.\n"
			       + "\n", outContent.toString());
		
		System.setOut(System.out);
	}
	
	// Test if the user wins
	// This checks process looking and drinking, but not moving
	// Information about the inventory should appear and the user should WIN
	// -FUN-WIN
	// http://stackoverflow.com/questions/12757559/code-coverage-in-java-with-eclemma-not-scanning-expecting-exception-methods
	@Test
	public void testWin(){
		inContent = new ByteArrayInputStream("L".getBytes());
		System.setIn(inContent);
		
		InstructionManager instruction_manager = new InstructionManager();
		Inventory inventory = new Inventory();
		
		Map mockMap = Mockito.mock(Map.class);
		Room mockRoom = Mockito.mock(Room.class);
		Mockito.when(mockMap.getCurrent_room()).thenReturn(mockRoom);
		Mockito.when(mockRoom.getHidden_item()).thenReturn(Inventory.CREAM);
		
		instruction_manager.input();

		instruction_manager.execute(mockMap, inventory);
		
		Mockito.when(mockRoom.getHidden_item()).thenReturn(Inventory.COFFEE);
		
		inContent = new ByteArrayInputStream("L".getBytes());
		System.setIn(inContent);
		
		instruction_manager.input();

		instruction_manager.execute(mockMap, inventory);
		
		Mockito.when(mockRoom.getHidden_item()).thenReturn(Inventory.SUGAR);
		
		inContent = new ByteArrayInputStream("L".getBytes());
		System.setIn(inContent);
		
		instruction_manager.input();

		instruction_manager.execute(mockMap, inventory);

		System.setOut(new PrintStream(outContent));
		inContent = new ByteArrayInputStream("D".getBytes());
		System.setIn(inContent);
		
		instruction_manager.input();
		exit.expectSystemExit();
		instruction_manager.execute(mockMap, inventory);
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
				   + "You have a cup of delicious coffee.\n"
				   + "You have some fresh cream.\n"
				   + "You have some tasty sugar.\n"
				   + "\n"
			       + "You drink the beverage and are ready to study!\n"
		       	   + "You win!\n"
				   + "Exiting with error code 0\n", outContent.toString());
		
		System.setOut(System.out);
	}
	
	// The D input triggers the Drink instruction
	// The check counts with an inventory WITH CREAM
	// Information about the items in the inventory should appear and the user should LOSE
	// -FUN-LOSE
	// http://stackoverflow.com/questions/12757559/code-coverage-in-java-with-eclemma-not-scanning-expecting-exception-methods
	@Test
	public void testDrinkInputWithCreamInInventory(){
		inContent = new ByteArrayInputStream("L".getBytes());
		System.setIn(inContent);
		System.setOut(new PrintStream(outContent));
		
		InstructionManager instruction_manager = new InstructionManager();
		Map map = new Map();
		Inventory inventory = new Inventory();
		
		instruction_manager.input();

		instruction_manager.execute(map, inventory);
		
		inContent = new ByteArrayInputStream("D".getBytes());
		System.setIn(inContent);

		instruction_manager.input();
		exit.expectSystemExit();
		instruction_manager.execute(map, inventory);
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
				   + "There might be something here...\n"
				   + "You found some creamy cream!\n"
				   + "\n"
				   + " INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
				   + "YOU HAVE NO COFFEE!\n"
				   + "You have some fresh cream.\n"
				   + "YOU HAVE NO SUGAR!\n"
				   + "\n"
				   + "You drink the cream, but without caffeine, you cannot study.\n"
				   + "You lose!\n"
				   + "Exiting with error code 1\n", outContent.toString());
		
		System.setOut(System.out);	
	}
	
	// The D input triggers the Drink instruction
	// The check counts with an FULL inventory
	// Information about the items in the inventory should appear and and the user should WIN
	// -FUN-WIN
	// http://stackoverflow.com/questions/12757559/code-coverage-in-java-with-eclemma-not-scanning-expecting-exception-methods
	@Test
	public void testDrinkInputWithFullInventory(){
		inContent = new ByteArrayInputStream("D".getBytes());
		System.setIn(inContent);
		
		InstructionManager instruction_manager = new InstructionManager();
		Inventory inventory = new Inventory();
		
		inventory.receive(Inventory.COFFEE);
		inventory.receive(Inventory.CREAM);
		inventory.receive(Inventory.SUGAR);

		System.setOut(new PrintStream(outContent));
		
		instruction_manager.input();
		exit.expectSystemExit();
		instruction_manager.execute(Mockito.mock(Map.class), inventory);
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
				   + "You have a cup of delicious coffee.\n"
				   + "You have some fresh cream.\n"
				   + "You have some tasty sugar.\n"
				   + "\n"
			       + "You drink the beverage and are ready to study!\n"
		       	   + "You win!\n"
				   + "Exiting with error code 0\n", outContent.toString());
		
		System.setOut(System.out);	
	}
	
	// Test valid lower case input
	// The instruction shall be executed normally
	// -FUN-INPUT-CAPS
	@Test
	public void testLowerCaseInput(){
		inContent = new ByteArrayInputStream("n".getBytes());
		System.setIn(inContent);
		
		InstructionManager instruction_manager = new InstructionManager();
		Map map = new Map();
		Inventory mockInventory = Mockito.mock(Inventory.class);
		
		instruction_manager.input();

		instruction_manager.execute(map, mockInventory);
		
		Room small_room = new Room("Small room", "Quaint sofa", "Magenta door", null, Inventory.CREAM);
		Room funny_room = new Room("Funny room", "Sad record player", "Beige door", "Massive door", null);
		Room refinanced_room = new Room("Refinanced room", "Tight pizza", "Dead door", "Smart door", Inventory.COFFEE);
		
		funny_room.setSouth_room(small_room);
		funny_room.setNorth_room(refinanced_room);
	
		assertEquals(funny_room.toString(), map.getCurrent_room().toString());	
	}
	
	// Test when the user is going North without a door leading to North
	// The user still remains in the same room and a warning message should appear
	// -FUN-MOVE
	@Test
	public void testGoNorthInputWhenItDoesNotHaveANorthDoor(){
		inContent = new ByteArrayInputStream("N".getBytes());
		System.setIn(inContent);
		System.setOut(new PrintStream(outContent));
		
		InstructionManager instruction_manager = new InstructionManager();
		Map map = new Map();
		Inventory mockInventory = Mockito.mock(Inventory.class);
		
		Room bloodthisty_room = new Room("Bloodthisty room", "Beautiful bag of money", "Purple door", "Sandy door", null);
		Room rough_room = new Room("Rough room", "Perfect air hockey table", null, "Minimalist door", Inventory.SUGAR);
		
		rough_room.setSouth_room(bloodthisty_room);
		map.setCurrent_room(rough_room);
		
		instruction_manager.input();

		instruction_manager.execute(map, mockInventory);
		
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
			       + "There is no door leading North.\n"
			       + "\n", outContent.toString());
		
		assertEquals(rough_room.toString(), map.getCurrent_room().toString());
		
		System.setOut(System.out);
	}
	
	// Test when the user is going South without a door leading to South
	// The user still remains in the same room and a warning message should appear
	// -FUN-MOVE
	@Test
	public void testGoSouthInputWhenItDoesNotHaveASouthDoor(){
		inContent = new ByteArrayInputStream("S".getBytes());
		System.setIn(inContent);
		System.setOut(new PrintStream(outContent));
		
		InstructionManager instruction_manager = new InstructionManager();
		Map map = new Map();
		Inventory mockInventory = Mockito.mock(Inventory.class);
		
		instruction_manager.input();

		instruction_manager.execute(map, mockInventory);
		
		Room small_room = new Room("Small room", "Quaint sofa", "Magenta door", null, Inventory.CREAM);
		Room funny_room = new Room("Funny room", "Sad record player", "Beige door", "Massive door", null);
		
		small_room.setNorth_room(funny_room);
	
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
			       + "There is no door leading South.\n"
			       + "\n", outContent.toString());
		assertEquals(small_room.toString(), map.getCurrent_room().toString());
		
		System.setOut(System.out);
	}
	
	// Test INVALID input
	// The program should answer "What?"]
	// -FUN-UNKNOWN-COMMAND
	@Test
	public void testWrongInput(){
		inContent = new ByteArrayInputStream("Q".getBytes());
		System.setIn(inContent);
		System.setOut(new PrintStream(outContent));
		
		InstructionManager instruction_manager = new InstructionManager();
		Map mockMap = Mockito.mock(Map.class);
		Inventory inventory = new Inventory();
		
		instruction_manager.input();

		instruction_manager.execute(mockMap, inventory);
		
		assertEquals(" INSTRUCTIONS (N,S,L,I,H,D) >\n"
				   + "\n"
			       + "What?\n"
			       + "\n", outContent.toString());
		
		System.setOut(System.out);
	}

}
