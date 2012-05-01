package penny.fourrow.logic;

public class Player {

	public String name;
	public int playerColorResource;
	public int score;
	
	public Player(String name, int resource) {
		this.name = name;
		this.playerColorResource = resource;
		this.score = 0;
	}
	
	public void addScore(int score)
	{
		this.score += score;
	}
	
	public void resetPlayer()
	{
		this.score = 0;
	}
}
