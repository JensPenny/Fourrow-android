package penny.fourrow.logic;

import java.util.EventListener;

public interface GameFinishedListener extends EventListener{
	
	public void gamefinishedEventHappened(GameFinishedEvent event);
}
