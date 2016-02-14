package coffeeMakerQuest;

public class Inventory {
	
	public static final String COFFEE = "coffee";
	public static final String CREAM = "cream";
	public static final String SUGAR = "sugar";
	
	private static final String[] items = {COFFEE, CREAM, SUGAR};
	private static final String[] items_inventory_quotes = {"a cup of delicious coffee", "some fresh cream", "some tasty sugar"};
	private static final String[] items_discovery_quotes = {"some caffeinated coffee", "some creamy cream", "some sweet sugar"};
	
	//binary
	private static final String[] items_drink_quotes = {
			"You drink the air, as you have no coffee, sugar, or cream.\n"              //0
	+ "The air is invigorating, but not invigorating enough. You cannot study.",
			"Without the cream, you get an ulcer and cannot study.",                    //1
			"You drink the cream, but without caffeine, you cannot study.",             //2
			"Without sugar, the coffee is too bitter. You cannot study",			    //3
			"You eat the sugar, but without caffeine, you cannot study.",               //4
			"Without the cream, you get an ulcer and cannot study.",                    //5
			"You drink the sweetened cream, but without caffeine, you cannot study."};  //6
	
	
	private boolean[] inventory = new boolean[3];
	
	public void defeat_information(){
		//binary_to_decimal
		int code = 0;
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i]){
				code += Math.pow(2, i);
			}
		}
		System.out.print(items_drink_quotes[code]+"\n");
	}
	
	public void receive(String item){
		for (int i = 0; i < items.length; i++) {
			if (item.equals(items[i])){
				inventory[i] = true;
				System.out.print("You found " + items_discovery_quotes[i] + "!\n\n");
			}
		}
	}
	
	public void show(){
		for (int i = 0; i < inventory.length; i++) {
			boolean item = this.inventory[i];
			
			if (item){
				System.out.print("You have " + items_inventory_quotes[i] + ".\n");
			}
			else{
				System.out.print(("You have no " + items[i] + "!\n").toUpperCase());
			}
		}
		System.out.print("\n");
		
	}
	
	public boolean check(){
		for (int i = 0; i < inventory.length; i++) {
			if (!this.inventory[i]){
				return false;
			}
		}
		return true;
	}
	
}
