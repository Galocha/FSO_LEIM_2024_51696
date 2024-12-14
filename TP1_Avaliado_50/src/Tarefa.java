
import java.util.concurrent.Semaphore;

public abstract class Tarefa extends Thread{

	protected BD bd;
	public Semaphore semaforo;
	
    protected enum Estado {
        ESTADO_1,
        ESTADO_2,
        ESTADO_3,
        ESTADO_4
    }

    protected Estado estadoAtual;

    public Tarefa(BD bd, Semaphore semaforo) {
        this.bd = bd;
        this.semaforo = semaforo;
        this.estadoAtual = Estado.ESTADO_1;
    }
    
    public abstract boolean isTarefaOn();

    public abstract void maquinaEstados();

    @Override
    public void run() {
        while (!bd.isTerminar()) {
            if (isTarefaOn()) {
                maquinaEstados();
                System.out.println("Estado atual de " + this.getClass().getSimpleName() + ": " + estadoAtual);
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
           }
        }
    }
}