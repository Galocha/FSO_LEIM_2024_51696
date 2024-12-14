
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Semaphore;
import java.awt.event.ActionEvent;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textNomeDoRobot;
	private JRadioButton rdbtnOnOff;
	private JLabel labelRobot;
	private JRadioButton rdbtnDebug;
	private JCheckBox chckbxVaguear;
	private JCheckBox chckbxEvitar;
	private JCheckBox chckbxFugir;
	private JLabel labelConsola;
	public static JTextPane textPaneConsola;
	public static JScrollPane scrollPaneConsola;
	private JButton btnStop;
	private JButton setaDireita;
	private JButton setaAtras;
	private JTextField textFieldDist;
	private JTextField textFieldRaio;
	private JLabel lblDist;
	private JLabel lblRaiocm;
	private JLabel lblAngulo;
	private JTextField textFieldAngulo;
	private JButton setaEsquerda;
	private JButton setaFrente;
	private Semaphore semaforo;
	
	private BD bd;
	

	/**
	 * Launch the application.
	 */
	private void initGraphics() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent arg0) {
							if(bd.isOnOff()) bd.getRobot().CloseEV3();
							bd.setTerminar(true);
						}
					});
					
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					setBounds(100, 100, 547, 356);
					contentPane = new JPanel();
					contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
					setContentPane(contentPane);
					contentPane.setLayout(null);
					
					setVisible(true);
					
					labelRobot = new JLabel("Lego EV Controller");
					labelRobot.setFont(new Font("Tahoma", Font.PLAIN, 17));
					labelRobot.setBounds(20, 10, 143, 29);
					contentPane.add(labelRobot);
					
					textNomeDoRobot = new JTextField();
					textNomeDoRobot.setText(bd.getNomeRobot());
					textNomeDoRobot.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							bd.setNomeRobot(textNomeDoRobot.getText());
							textPaneConsola.setText("O nome do robot foi alterado para " + bd.getNomeRobot());
						}
					});
					textNomeDoRobot.setText("nome do robot");
					textNomeDoRobot.setBounds(427, 18, 96, 19);
					contentPane.add(textNomeDoRobot);
					textNomeDoRobot.setColumns(10);
					
					rdbtnOnOff = new JRadioButton("On/Off");
					rdbtnOnOff.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							if(bd.isOnOff()) {
								bd.setOnOff(false);
								bd.getRobot().CloseEV3();
							} else {
								bd.setOnOff(bd.getRobot().OpenEV3(bd.getNomeRobot()));	
							}
							escreverConsola("O robot " + bd.getNomeRobot() + " foi " + (bd.isOnOff() ? " aberto." : " fechado."));
							rdbtnOnOff.setSelected(bd.isOnOff());
							btnStop.setEnabled(bd.isOnOff());;
							setaFrente.setEnabled(bd.isOnOff());
							setaAtras.setEnabled(bd.isOnOff());
							setaDireita.setEnabled(bd.isOnOff());
							setaEsquerda.setEnabled(bd.isOnOff());
						}
					});
					rdbtnOnOff.setBounds(338, 17, 83, 21);
					contentPane.add(rdbtnOnOff);
					
					rdbtnDebug = new JRadioButton("Debug");
					rdbtnDebug.setBounds(456, 73, 67, 21);
					contentPane.add(rdbtnDebug);
					
					chckbxVaguear = new JCheckBox("Vaguear");
					chckbxVaguear.setBounds(456, 96, 67, 21);
					chckbxVaguear.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent e) {
				            if (chckbxVaguear.isSelected()) {
				                bd.setVaguearOn(true);
				                escreverConsola("Tarefa de Vaguear ativa.");
				            } else {
				                bd.setVaguearOn(false);
				                try {
                                    semaforo.acquire();
                                    bd.getRobot().Parar(true);
                                    escreverConsola("Tarefa de Vaguear desativada.");
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                semaforo.release();
				            }
					    }
					});
					contentPane.add(chckbxVaguear);
					
					chckbxEvitar = new JCheckBox("Evitar");
					chckbxEvitar.setBounds(456, 119, 67, 21);
					chckbxEvitar.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                    		if (chckbxEvitar.isSelected()) {
                                bd.setEvitarOn(true);
                                escreverConsola("Tarefa de Evitar ativa.");
                            } else {
                                bd.setEvitarOn(false);
                                try {
                                    semaforo.acquire();
                                    bd.getRobot().Parar(true);
                                    escreverConsola("Tarefa de Evitar desativada.");
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                semaforo.release();
                            }
                        }
                    });
					contentPane.add(chckbxEvitar);
					
					chckbxFugir = new JCheckBox("Fugir");
					chckbxFugir.setBounds(456, 142, 67, 21);
					chckbxFugir.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (chckbxFugir.isSelected()) {
                                bd.setFugirOn(true);
                                escreverConsola("Tarefa de Fugir ativa.");
                            } else {
                            	bd.setFugirOn(false);
                                try {
                                    semaforo.acquire();
                                    bd.getRobot().Parar(true);
                                    escreverConsola("Tarefa de Fugir desativada.");
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                semaforo.release();
                            }
                        }
                    });
					contentPane.add(chckbxFugir);
					
					labelConsola = new JLabel("Consola");
					labelConsola.setBounds(20, 239, 67, 13);
					contentPane.add(labelConsola);
					
					btnStop = new JButton("🛑");
					btnStop.setBackground(new Color(250, 128, 114));
					btnStop.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							bd.getRobot().Parar(true);
							escreverConsola("O robot parou.");
						}
					});
					btnStop.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
					btnStop.setBounds(231, 102, 69, 35);
					contentPane.add(btnStop);
					
					setaDireita = new JButton("↷");
					setaDireita.setBackground(new Color(152, 251, 152));
					setaDireita.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							bd.getRobot().CurvarDireita(bd.getRaio(), bd.getAngulo());
							bd.getRobot().Parar(false);
							escreverConsola("Robot a curvar para a Direita com raio " + bd.getRaio() + " e ângulo " + bd.getAngulo());
						}
					});
					setaDireita.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
					setaDireita.setBounds(310, 100, 53, 35);
					contentPane.add(setaDireita);
					
					setaAtras = new JButton("↧");
					setaAtras.setBackground(new Color(152, 251, 152));
					setaAtras.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							bd.getRobot().Reta(0 - bd.getDistancia());
							bd.getRobot().Parar(false);
							escreverConsola("O robot andou " + bd.getDistancia() + " cm para trás.");
						}
					});
					setaAtras.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
					setaAtras.setBounds(230, 144, 69, 35);
					contentPane.add(setaAtras);
					
					textFieldDist = new JTextField();
					textFieldDist.setText("25");
					textFieldDist.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							bd.setDistancia(Integer.parseInt(textFieldDist.getText()));
							escreverConsola("A distância foi alterada para " + bd.getDistancia());
						}
					});
					textFieldDist.setColumns(10);
					textFieldDist.setBounds(100, 205, 63, 19);
					contentPane.add(textFieldDist);
					
					textFieldRaio = new JTextField();
					textFieldRaio.setText("" + bd.getRaio());
					textFieldRaio.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							bd.setRaio(Integer.parseInt(textFieldRaio.getText()));
							escreverConsola("O raio foi alterado para " + bd.getRaio());
						}
					});
					textFieldRaio.setColumns(10);
					textFieldRaio.setBounds(271, 205, 63, 19);
					contentPane.add(textFieldRaio);
					
					lblDist = new JLabel("Distância (cm):");
					lblDist.setFont(new Font("Tahoma", Font.PLAIN, 10));
					lblDist.setBounds(20, 200, 76, 29);
					contentPane.add(lblDist);
					
					lblRaiocm = new JLabel("Raio (cm):");
					lblRaiocm.setFont(new Font("Tahoma", Font.PLAIN, 10));
					lblRaiocm.setBounds(200, 200, 63, 29);
					contentPane.add(lblRaiocm);
					
					lblAngulo = new JLabel("Ângulo (graus):");
					lblAngulo.setFont(new Font("Tahoma", Font.PLAIN, 10));
					lblAngulo.setBounds(374, 200, 83, 29);
					contentPane.add(lblAngulo);
					
					textFieldAngulo = new JTextField();
					textFieldAngulo.setText("" + bd.getAngulo());
					textFieldAngulo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							bd.setAngulo(Integer.parseInt(textFieldAngulo.getText()));
							escreverConsola("Ângulo alterado para " + bd.getAngulo());
						}
					});
					textFieldAngulo.setColumns(10);
					textFieldAngulo.setBounds(460, 205, 63, 19);
					contentPane.add(textFieldAngulo);
					
					setaEsquerda = new JButton("↶");
					setaEsquerda.setBackground(new Color(152, 251, 152));
					setaEsquerda.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							bd.getRobot().CurvarEsquerda(bd.getRaio(), bd.getAngulo());
							bd.getRobot().Parar(false);
							escreverConsola("Robot a curvar para a esquerda com raio " + bd.getRaio() + " e ângulo " + bd.getAngulo());
						}
					});
					setaEsquerda.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
					setaEsquerda.setBounds(171, 104, 53, 36);
					contentPane.add(setaEsquerda);
					
					textPaneConsola = new JTextPane();
					scrollPaneConsola = new JScrollPane(textPaneConsola);
					scrollPaneConsola.setBounds(20, 262, 503, 88);
					contentPane.add(scrollPaneConsola);
					
					setaFrente = new JButton("↥");
					setaFrente.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
					setaFrente.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							bd.getRobot().Reta(bd.getDistancia());
							bd.getRobot().Parar(false);
							escreverConsola("O robot andou " + bd.getDistancia() + " cm para a frente.");
						}
					});
					setaFrente.setBackground(new Color(152, 251, 152));
					setaFrente.setBounds(231, 62, 69, 35);
					contentPane.add(setaFrente);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	
	public GUI(Semaphore semaforo) {
		bd = new BD();
		initGraphics();
		this.semaforo = semaforo;
	}

	public BD getBd() {
		return bd;
	}
	
	public static void escreverConsola(String m) {
		textPaneConsola.setText(textPaneConsola.getText() + "\n" + m);
		textPaneConsola.setCaretPosition(textPaneConsola.getDocument().getLength());
	}
}
