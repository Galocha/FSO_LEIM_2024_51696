import java.util.Random;
import java.util.concurrent.Semaphore;

public class Vaguear extends Tarefa {

    private Random random;
    private long tempoPausa;
    
    private enum TipoMovimento {
        RETA,
        CURVA
    }

    private TipoMovimento tipoMovimento;
    
    public Semaphore vaguearNoPermits = new Semaphore(0);

    public Vaguear(BD bd, Semaphore semaforo) {
        super(bd, semaforo);
        this.random = new Random();
        this.tempoPausa = 0;
    }
    
    @Override
    public boolean isTarefaOn() {
        return bd.isVaguearOn();
    }
    
    public void bloquearVaguear() throws InterruptedException {
    	vaguearNoPermits.acquire();
    	estadoAtual = Estado.ESTADO_1;
    }
    
    public void desbloquearVaguear() {
        vaguearNoPermits.release();
        estadoAtual = Estado.ESTADO_2;
    }
    
    public void executar() {
			int distanciaAleatoria = 10 + random.nextInt(51); // Distância entre 10 cm e 60 cm
            int raioAleatorio = 5 + random.nextInt(21); // Raio entre 5 cm e 25 cm
            int anguloAleatorio = 15 + random.nextInt(76); // Ângulo entre 15° e 90°
            
            bd.setDistancia(distanciaAleatoria);
            bd.setRaio(raioAleatorio);
            bd.setAngulo(anguloAleatorio);
            
            int movimento = random.nextInt(3); // 0 = frente, 1 = direita, 2 = esquerda, 3 = Trás
            switch (movimento) {
            case 0:
            	tipoMovimento = TipoMovimento.RETA;
                bd.getRobot().Reta(bd.getDistancia());
                GUI.escreverConsola("O robot andou " + bd.getDistancia() + " cm para a frente.");
                break;
            case 1:
            	tipoMovimento = TipoMovimento.CURVA;
                bd.getRobot().CurvarDireita(bd.getRaio(), bd.getAngulo());
                GUI.escreverConsola("O robot curvou, para a direita, " + bd.getRaio() + " cm, com um ângulo de " + bd.getAngulo() + " graus.");
                break;
            case 2:
            	tipoMovimento = TipoMovimento.CURVA;
                bd.getRobot().CurvarEsquerda(bd.getRaio(), bd.getAngulo());
                GUI.escreverConsola("O robot curvou, para a esquerda, " + bd.getRaio() + " cm, com um ângulo de " + bd.getAngulo() + " graus.");
                break;
            }
    }
    
    public void pausa() {
            //bd.getRobot().Parar(false);
            switch (tipoMovimento) {
                case RETA:
                    int velocidadeReta = 30; // em cm/s, ajuste conforme a velocidade do robô
                    tempoPausa = (bd.getDistancia() * 1000) / velocidadeReta; // Convertendo para milissegundos
                    System.out.println("O robot demorou " + tempoPausa + " milisegundos");
                    break;

                case CURVA:
                    int raio = bd.getRaio();
                    int angulo = bd.getAngulo();
                    double comprimentoArco = (2 * Math.PI * raio) * (angulo / 360.0);
                    int velocidadeCurva = 30; // em cm/s, ajuste conforme a velocidade do robô
                    tempoPausa = (long)((comprimentoArco * 1000) / velocidadeCurva); // Convertendo para milissegundos
                    System.out.println("O robot demorou " + tempoPausa + " milisegundos");
                    break;
            }
    }
    
    public void maquinaEstados() {
    	switch (estadoAtual) {
		    case ESTADO_1: //Bloqueado
		        if (isTarefaOn()) {
		        	GUI.escreverConsola("O robô começou a Vaguear.");
		            estadoAtual = Estado.ESTADO_2; // Passar para o estado EXECUTAR
		        } else {
		        	GUI.escreverConsola("O robô parou de Vaguear.");
		        }
		        break;
		
		    case ESTADO_2: //Executar
		    	if (!isTarefaOn()) {
		            estadoAtual = Estado.ESTADO_1;
		    	} else {
		        	try {
		        		semaforo.acquire();
		        		executar();
		        	} catch (InterruptedException e) {
		        		e.printStackTrace();
		        	}
		        	semaforo.release();
			        estadoAtual = Estado.ESTADO_3; // Após executar o movimento, vai para o estado PAUSA
		        }
		        break;
		
		    case ESTADO_3: //Pausa
		        if (!isTarefaOn()) {
		            estadoAtual = Estado.ESTADO_1;
		        } else {
		        	pausa();
			        try {
			            Thread.sleep(tempoPausa); // Aguardar o tempo de pausa
			        } catch (InterruptedException e) {
			            e.printStackTrace();
			        }
			        estadoAtual = Estado.ESTADO_2; // Voltar ao estado EXECUTAR para novo movimento
		        }
		        break;
			default:
				break;
    	}
    }
}
