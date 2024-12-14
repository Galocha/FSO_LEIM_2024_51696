
import robot.*;

public class BD {
	private RobotLegoEV3 robot;
	private String nomeRobot;
	private int distancia;
	private int raio;
	private int angulo;
	private boolean onOff;
	private boolean terminar;
	private boolean isVaguearOn;
	private boolean isEvitarOn;
	private boolean isFugirOn;
	
	public BD() {
		robot = new RobotLegoEV3();
		nomeRobot = "EV2";
		distancia = 25;
		raio = 5;
		angulo = 40;
		onOff = false;
		terminar = false;
		isVaguearOn = false;
		isEvitarOn = false;
		isFugirOn = false;
	}

	public RobotLegoEV3 getRobot() {
		return robot;
	}

	public void setRobot(RobotLegoEV3 robot) {
		this.robot = robot;
	}

	public String getNomeRobot() {
		return nomeRobot;
	}

	public void setNomeRobot(String nomeRobot) {
		this.nomeRobot = nomeRobot;
	}

	public int getDistancia() {
		return distancia;
	}

	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	
	public int getRaio() {
		return raio;
	}

	public void setRaio(int raio) {
		this.raio = raio;
	}
	
	public int getAngulo() {
		return angulo;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public boolean isOnOff() {
		return onOff;
	}

	public void setOnOff(boolean onOff) {
		this.onOff = onOff;
	}

	public boolean isTerminar() {
		return terminar;
	}

	public void setTerminar(boolean terminar) {
		this.terminar = terminar;
	}
	
	public boolean isVaguearOn() {
		return isVaguearOn;
	}

	public void setVaguearOn(boolean isVaguearOn) {
		this.isVaguearOn = isVaguearOn;
	}

	public boolean isEvitarOn() {
		return isEvitarOn;
	}

	public void setEvitarOn(boolean isEvitarOn) {
		this.isEvitarOn = isEvitarOn;
	}
	
	public boolean isFugirOn() {
		return isFugirOn;
	}

	public void setFugirOn(boolean isFugirOn) {
		this.isFugirOn = isFugirOn;
	}
}

