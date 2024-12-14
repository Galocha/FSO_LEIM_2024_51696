
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Fugir extends Tarefa{
	
    private Random random;
    private int ultimaCurva;
    
    public Vaguear v;
    
    public Semaphore fugirNoPermits = new Semaphore(0);
    
    public Fugir(BD bd, Semaphore semaforo, Vaguear v) {
        super(bd, semaforo);
        this.random = new Random();
        this.ultimaCurva = -1;  // Inicializa com um valor inválido para ser definido na primeira curva
        this.v = v;
    }
    
    @Override
    public boolean isTarefaOn() {
        return bd.isFugirOn();
    }
    
    public void bloquearFugir() throws InterruptedException {
    	fugirNoPermits.acquire();
    	estadoAtual = Estado.ESTADO_1;
    }
    
    public void desbloquearFugir() {
		fugirNoPermits.release();
		estadoAtual = Estado.ESTADO_2;
    }
    
    public void curvaAleatoria(boolean espelhado) {
        if (espelhado) {
            if (ultimaCurva == 0) {
                bd.getRobot().CurvarDireita(20, 90);
                GUI.escreverConsola("O robô curvou à direita com ângulo de 90º e raio de 20cm.");
            } else {
                bd.getRobot().CurvarEsquerda(20, 90);
                GUI.escreverConsola("O robô curvou à esquerda com ângulo de 90º e raio de 20cm.");
            }
        } else {
            ultimaCurva = random.nextInt(2); // 0 - curva à esquerda | 1 - curva à direita
            if (ultimaCurva == 0) {
                bd.getRobot().CurvarEsquerda(20, 90);
                GUI.escreverConsola("O robô curvou à esquerda com ângulo de 90º e raio de 20cm.");
            } else {
                bd.getRobot().CurvarDireita(20, 90);
                GUI.escreverConsola("O robô curvou à direita com ângulo de 90º e raio de 20cm.");
            }
        }
    }
    
    public void maquinaEstados() {
        switch (estadoAtual) {
            case ESTADO_1: //BLOQUEADO
            	if (isTarefaOn()) {
		            estadoAtual = Estado.ESTADO_2; // Passar para o estado EXECUTAR
		        } else {
		        	GUI.escreverConsola("O robô parou de fugir.");
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
                	if (bd.getRobot().SensorUS(bd.getRobot().S_2) < 50.0){
                    	GUI.escreverConsola("Obstáculo detetado. Começar a fugir.");
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
                	GUI.escreverConsola("O comportamento Vaguear foi bloqueado por Fugir.");
        			estadoAtual = Estado.ESTADO_4;
        		} else {
        			estadoAtual = Estado.ESTADO_4;
        		}
            	break;

            case ESTADO_4: //FUGIR
            	if (!isTarefaOn()) {
            		v.desbloquearVaguear();
            		GUI.escreverConsola("O comportamento Vaguear foi desbloqueado.");
		            estadoAtual = Estado.ESTADO_1;
		        } else {
		        	try {
		        		curvaAleatoria(false);
		                bd.getRobot().Reta(20);
		                GUI.escreverConsola("O robô andou em frente 20cm.");
		                curvaAleatoria(true);
		                bd.getRobot().Parar(false);
		                Thread.sleep(200);
		        	} catch (InterruptedException e) {
		        		e.printStackTrace();
		        	}
		        	semaforo.release();
	                if (v.isTarefaOn()) {
	                	v.desbloquearVaguear();
    	                System.out.println("O comportamento Vaguear foi desbloqueado.");
    	                estadoAtual = Estado.ESTADO_2;
                	} else {
                		estadoAtual = Estado.ESTADO_2;
	                }
		        }
                break;
	     }
    }
}
