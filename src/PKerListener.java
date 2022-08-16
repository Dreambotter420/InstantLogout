
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.input.mouse.MouseSettings;
import org.dreambot.api.script.ScriptManager;

public class PKerListener implements Runnable{
    @Override
    public void run() {
    	MethodProvider.log("PKerListener Thread starting!");
    	Main.threadAlive = true;
    	ScriptManager manager = ScriptManager.getScriptManager();
        manager.pause();
        try {
            while(manager.isPaused())
            {
            	if(Main.shouldPanic())
            	{
            		manager.resume();
                    Main.threadAlive = false;
            		return;
            	}
            	Thread.sleep(Sleep.calculate(69,69));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        MethodProvider.log("PKerListener Thread stopping!");
        Main.threadAlive = false;
    }
}