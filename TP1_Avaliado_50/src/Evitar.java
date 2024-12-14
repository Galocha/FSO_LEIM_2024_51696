
import java.util.concurrent.Semaphore;

public class Evitar extends Tarefa{

	public Vaguear v;
	public Fugir f;
	
    public Evitar(BD bd, Semaphore semaforo, Vaguear v, Fugir f) {
        super(bd, semaforo);
        this.v = v;
        this.f = f;
    }
    
    @Override
    public boolean isTarefaOn() {
        return bd.isEvitarOn();
    }
    
    public void maquinaEstados() {
	        switch (estadoAtual) {
	            case ESTADO_1: //BLOQUEADO
	            	if (isTarefaOn()) {
			            estadoAtual = Estado.ESTADO_2; // Passar para o estado EXECUTAR
			        } else {
			        	GUI.escreverConsola("O robô parou de evitar.");
			        }
	                break;
	
	            case ESTADO_2: //DETECTAR
	            	if (!isTarefaOn()) {
	            		estadoAtual = Estado.ESTADO_1;
	            	} else {
	            		try {
							semaforo.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	            		if (bd.getRobot().SensorToque(bd.getRobot().S_1) == 1) {
	            			GUI.escreverConsola("Obstáculo detetado. Começar a evitar.");
		                    estadoAtual = Estado.ESTADO_3;
	            		} else {
	            			semaforo.release();
	            		}
	                } 
	                break;
	                
	            case ESTADO_3: //BLOQUEAR TAREFA(S)
	            	if (v.isTarefaOn()) {
                    	try {
							v.bloquearVaguear();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            			System.out.println("O comportamento Vaguear foi bloqueado por Evitar.");
            			estadoAtual = Estado.ESTADO_4;
                    } else if (f.isTarefaOn()) {
                    	try {
							f.bloquearFugir();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                    	System.out.println("O comportamento Fugir foi bloqueado por Evitar.");
                    	estadoAtual = Estado.ESTADO_4;
                    } else {
                    	estadoAtual = Estado.ESTADO_4;
                    }
	            	break;
	
	            case ESTADO_4: //MOVER
	            	if (!isTarefaOn()) {
	            		v.desbloquearVaguear();
	            		GUI.escreverConsola("O comportamento Vaguear foi desbloqueado.");
	            		f.desbloquearFugir();
	            		GUI.escreverConsola("O comportamento Fugir foi desbloqueado.");
			            estadoAtual = Estado.ESTADO_1;
			        } else {
			        	try {
			        		bd.getRobot().Parar(true);
			        		GUI.escreverConsola("O robô parou.");
			                bd.getRobot().Reta(-15);
			                GUI.escreverConsola("O robô andou para trás 15cm.");
			                bd.getRobot().Parar(false);
			                bd.getRobot().CurvarEsquerda(0, 90);
			                GUI.escreverConsola("O robô curvou à esquerda com um ângulo de 90º.");
			                bd.getRobot().Parar(false);
			                Thread.sleep(200);
			        	} catch (InterruptedException e) {
			        		e.printStackTrace();
			        	}
			        	semaforo.release();
			        	if (v.isTarefaOn()) {
			        		v.desbloquearVaguear();
			        		GUI.escreverConsola("O comportamento Vaguear foi desbloqueado.");
			        		estadoAtual = Estado.ESTADO_2;
			        	} else if (f.isTarefaOn()){
			        		f.desbloquearFugir();
		            		GUI.escreverConsola("O comportamento Fugir foi desbloqueado.");
			                estadoAtual = Estado.ESTADO_2;
			        	} else {
			        		estadoAtual = Estado.ESTADO_2;
			        	}
			        }
	                break;
	        }
    }
}