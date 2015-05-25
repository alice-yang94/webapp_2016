import java.applet.Applet;


public class RunningMonster extends Applet implements Runnable{

	public void start() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
