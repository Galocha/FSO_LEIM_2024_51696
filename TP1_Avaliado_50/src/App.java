
import java.util.concurrent.Semaphore;

public class App {
	
	private GUI gui;
	
	private Vaguear vaguear;
    
    private Evitar evitar;
    
    private Fugir fugir;
    
    private Semaphore acessoRobot = new Semaphore(1);
	
	public App() {
		gui = new GUI(acessoRobot);
		
		vaguear = new Vaguear(gui.getBd(), acessoRobot);
        vaguear.start();
        
        fugir = new Fugir(gui.getBd(), acessoRobot, vaguear);
        fugir.start();
        
        evitar = new Evitar(gui.getBd(), acessoRobot, vaguear, fugir);
        evitar.start();
        
	}
	
	public void run() {
		while(!gui.getBd().isTerminar()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		App app = new App();
		System.out.println("Aplicação começou");
		app.run();
		System.out.println("Aplicação terminou");
	}
}
