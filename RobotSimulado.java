package ig;
import robot.*;
import javax.bluetooth.*;

public class RobotSimulado {
	
	public static final int S_1 = 0;
	public static final int S_2 = 1;
	private int sensorToqueValue;
	private float sensorUSValue;
	private boolean sim;
	private RobotLegoEV3 robot;
	
	public RobotSimulado(boolean sim) {
	    this.sim = sim;
	    this.sensorToqueValue = 0;
	    this.sensorUSValue = 0;
	    if (!conectarBluetooth() || sim) {
            System.out.println("Modo de simulação ativado.");
            this.sim = true;  // Força o modo simulado caso Bluetooth não esteja disponível
        } else {
            System.out.println("Conexão Bluetooth estabelecida.");
        }
	}
    
    public boolean OpenEV3(String nome) {
        System.out.println("Simulando abertura do robô " + nome);
        if (robot != null) { 
        	return robot.OpenEV3(nome); 
        } else { 
        	System.err.println("Erro: Objeto robot não inicializado."); 
        	return false; 
        }
    }

    public void CloseEV3() {
        System.out.println("Simulando fechamento do robô");
        robot.CloseEV3();
    }

    public void Reta(int distancia) {
        if (sim) {
            System.out.println("Simulando movimento em linha reta por " + distancia + " cm");
        } else {
            robot.Reta(distancia);
        }
    }

    public void CurvarEsquerda(int raio, int angulo) {
        if (sim) {
            System.out.println("Simulando curva à esquerda com raio " + raio + " cm e ângulo " + angulo + " graus");
        } else {
            robot.CurvarEsquerda(raio, angulo);
        }
    }
    public void CurvarDireita(int raio, int angulo) {
    	if (sim) {
    		System.out.println("Simulando curva à direita com raio " + raio + " cm e ângulo " + angulo + " graus");
    	} else {
    		robot.CurvarDireita(raio, angulo);
    	}
    }

    public void Parar(boolean imediato) {
        if (sim) {
            System.out.println("Simulando paragem do robô" + (imediato ? " imediatamente." : "."));
        } else {
            robot.Parar(imediato);
        }
    }
    
    public void setSensorToqueValue(int value) {
        this.sensorToqueValue = value; // Atualiza o valor do sensor de toque
    }

    
    public int SensorToque(int sensor) {
        if (sim) {
            System.out.println("Simulando leitura do sensor de toque na porta " + sensor);
            return sensorToqueValue; // Retorna o valor controlado pela GUI
        } else {
            return robot.SensorToque(sensor);
        }
    }
    
    public void setSensorUSValue(float value) {
        this.sensorUSValue = value; // Atualiza o valor do sensor us
    }
    
    public float SensorUS(int sensor) {
    	if (sim) {
            System.out.println("Simulando leitura do sensor us na porta " + sensor);
            return sensorUSValue;
        } else {
        	return robot.SensorUS(sensor);
        }
    }
    
    public void verificarSensorUltrassonico(float distancia) {
        if (distancia <= 50) {
            System.out.println("Obstáculo detectado! Distância: " + distancia + " cm");
        } else {
            System.out.println("Sem obstáculos próximos. Distância: " + distancia + " cm");
        }
    }
    
    public void setSim(boolean sim) {
    	this.sim = sim;
    }
    
    public boolean getSim() {
    	return sim;
    }
    
    public boolean conectarBluetooth() {
        try {
            // Inicializa a biblioteca BlueCove
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            System.out.println("Adaptador Bluetooth encontrado: " + localDevice.getFriendlyName());
            return true;  // Bluetooth está ativo
        } catch (BluetoothStateException e) {
            System.out.println("Bluetooth não encontrado, ativando modo de simulação.");
            return false;  // Não foi possível encontrar o Bluetooth
        }
    }
}
