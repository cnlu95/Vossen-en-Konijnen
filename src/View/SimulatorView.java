package View;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import Runner.ThreadRunner;
import Logic.Counter;
import Logic.Field;
import Logic.FieldStats;
import Main.Main;
import Main.Simulator;
import Model.Bear;
import Model.Fox;
import Model.Rabbit;
import Model.AllesEter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author Caroline
 * @version 1.0
 */
public class SimulatorView extends JFrame
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;
    
    private JFrame frame;
    private JTabbedPane tabbedpane;
    private JPanel jpanel;
    private static Simulator simulator;
	private ThreadRunner threadRunner;
	private boolean isReset;
	
	//views
	private PieView pieChart;
	private Histogram histogram;
	private HistoryView historyView;

	private JFrame settingsFrame;
	private JFrame PieFrame;
	
    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final String VERSION_NUMBER = "0.1 Alpha";
    private JLabel stepLabel, population;
    private FieldView fieldView;
    
    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;   

    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width)
    {
    	frame = new JFrame("Vossen en konijnen");
        stats = new FieldStats();
        colors = new LinkedHashMap<Class, Color>();      
        threadRunner = new ThreadRunner();       
        
        //Making the left menu with buttons
        JPanel leftMenu = new JPanel();
        leftMenu.setLayout(new GridLayout(0,1));
        
        //Making the one step button
        JButton oneStep = new JButton("1 step");
        oneStep.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		threadRunner.startRun(1);
        	}
        });
        
        //Making the hundred steps button
        JButton hundredSteps = new JButton("100 steps");
        hundredSteps.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		threadRunner.startRun(100);
        	}
        });      
        
        JButton start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				threadRunner.startRun(0);
				Component c = (Component)e.getSource(); 
			    c.getToolkit().beep();
			}
		});

		JButton stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				threadRunner.stop();
				Component c = (Component)e.getSource(); 
			    c.getToolkit().beep();
			}
		});
        
        //Making the reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Main.getSimulator().reset();
        	}
        });
        
        //make views
        makeFieldView(height, width);
        makePieChart(height, width);
		makeHistogram(height, width);
		makeHistoryView(height, width);
        makeMainFrame();
        
        //Adding it to the menu
        leftMenu.add(oneStep);
        leftMenu.add(hundredSteps);
        leftMenu.add(start);
        leftMenu.add(stop);
        leftMenu.add(resetButton);
        
        //Flow for spacing
        JPanel flow = new JPanel();
        flow.add(leftMenu);
              
        
        //Making the menu attached to the frame
        makeGUIMenu(frame);
        
        //Making the labels for the steps performed and population of the rabbits and foxes.
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        
        JLabel version_number = new JLabel(VERSION_NUMBER);
        
        //Making the view with the animals
        JPanel fieldViewLayout = new JPanel(new BorderLayout());
        fieldView = new FieldView(height, width);
        fieldViewLayout.add(stepLabel, BorderLayout.NORTH);
        fieldViewLayout.add(fieldView, BorderLayout.CENTER);
        fieldViewLayout.add(population, BorderLayout.SOUTH);

        //Adding everything to the container
        Container contents = frame.getContentPane();
        contents.add(fieldViewLayout, BorderLayout.CENTER);
        contents.add(flow, BorderLayout.WEST);
        contents.add(version_number, BorderLayout.SOUTH);
        frame.pack();
        
        //Making sure for a centered position on the screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
        
        //Showing the results
        frame.setVisible(true);
        
    }
    
    /**
     * Method to attach a menu to a frame.
     * @param frame Frame which the menu will be attached to.
     */
    private void makeGUIMenu(JFrame frame)
    {
    	//The main menu bar.
    	JMenuBar menu = new JMenuBar(); 
    	frame.setJMenuBar(menu);
    	
    	//Menu number 1
    	JMenu menu1 = new JMenu("File");
    	JMenuItem menuItem = new JMenuItem("Quit");
    	menuItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			System.exit(0);
    		}
    	});
    	JMenuItem menuItem4 = new JMenuItem("Settings");
    	menuItem4.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (settingsFrame == null){
					makeSettings();
				}
				settingsFrame.setVisible(true);
    		}
    	});
    	menu1.add(menuItem4);
    	menu1.add(menuItem);
    	menu.add(menu1);
    	
    	//Menu number 2.
    	JMenu menu2 = new JMenu("Views");
    	JMenuItem menu2Item1 = new JMenuItem("CirkelDiagram");
    	menu2Item1.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			if (PieFrame == null){
				}
				PieFrame.setVisible(true);
    		}
    	});
    		
    	JMenuItem menu2Item2 = new JMenuItem("Histogram");
    	JMenuItem menu2Item3 = new JMenuItem("HistoryView");
    	menu2.add(menu2Item1);
    	menu2.add(menu2Item2);
    	menu2.add(menu2Item3);
    	menu.add(menu2);
    	
    	//Making the help menu
    	JMenu helpMenu = new JMenu("Help");
    	JMenuItem helpItem = new JMenuItem("Help me!");
    	helpItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) { //Popup help menu message.
    			makePopupMessage("Geen hulp beschikbaar, sorry.");
    		}
    	});
    	helpMenu.add(helpItem);
    	menu.add(helpMenu);
    	
    }
    
 	/**
	 * Maak main frame aan en al zijn componenten Main frame is de onderste
	 * laag, daarna komt tweede laag view panel (rechterkant) en toolbar panel
	 * (linkerkant). Aan de toolbar panel worden knoppen toegevoegd en aan de
	 * view panel worden meerdere views toegevoegd(piecharts etc.)
	 */
	public void makeMainFrame() {
		// maak main frame (onderste laag) aan, layout en border van main frame.
		JPanel mainFrame = new JPanel();
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setBorder(new EmptyBorder(10, 10, 10, 10));

		// maak view panel (tweede laag) aan, layout en border van view panel.
		JPanel viewPanel = new JPanel();
		viewPanel.setLayout(new GridLayout(0, 3));
		viewPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		// histoGram panel
		JPanel diagram = new JPanel();
		diagram.setLayout(new BorderLayout());
		diagram.add(histogram, BorderLayout.CENTER);			
		
		// pieChart panel
		JPanel chart = new JPanel();
		chart.setLayout(new BorderLayout());
		chart.add(pieChart, BorderLayout.CENTER);

		// maak field panel (bovenste laag) aan, en border van field panel.
		JPanel field = new JPanel();
		field.setLayout(new BorderLayout());
		field.add(stepLabel, BorderLayout.NORTH);
		field.add(fieldView, BorderLayout.CENTER);
		field.add(population, BorderLayout.SOUTH);
		
		// textArea panel
		JTextArea textArea = new JTextArea(20, 20);
		historyView.setTextArea(textArea);		
		// scroll panel voor de textArea
		JScrollPane scrollPane = new JScrollPane(textArea);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textArea.setEditable(false);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		
		
		//	alles toevoegen aan de frame
		this.add(mainFrame);
		mainFrame.add(viewPanel, BorderLayout.CENTER); 
		
		viewPanel.add(field);
		viewPanel.add(scrollPane);
		viewPanel.add(chart);
		viewPanel.add(diagram);
		
		// Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(1280, 720));
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
    
	/**
	 * maak een pop up settings window, wordt opgeroepen wanneer menu item
	 * settings wordt gebruikt. Settings wordt gebruikt om gegevens van een
	 * actoren te kunnen wijzigen
	 */
	private void makeSettings() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//	maak settings frame aan
		settingsFrame = new JFrame();
		settingsFrame.setTitle("Settings");
		
		//	maak settings frame's main tab aan, size, layout en border van
		//	settings panel
		JTabbedPane mainTab = new JTabbedPane();
		mainTab.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainTab.setPreferredSize(new Dimension(100, 100));

		//	maak general tab aan, layout en border
		JPanel generalTab = new JPanel();
		generalTab.setLayout(new GridLayout(8, 1));
		generalTab.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		//	voeg labels, tekstvelden toe aan general tab
		generalTab.add(new JLabel("Animation Speed"));
		final JTextField animationSpeed = new JTextField();
		generalTab.add(animationSpeed);
		generalTab.add(new JLabel("Rabbit creation probability"));
		final JTextField rabbitCreationProbability = new JTextField();
		generalTab.add(rabbitCreationProbability);
		generalTab.add(new JLabel("Fox creation probability"));
		final JTextField foxCreationProbability = new JTextField();
		generalTab.add(foxCreationProbability);
		generalTab.add(new JLabel("Bear creation probability"));
		final JTextField bearCreationProbability = new JTextField();
		generalTab.add(bearCreationProbability);
		generalTab.add(new JLabel("Hunter creation probability"));
		final JTextField hunterCreationProbability = new JTextField();
		generalTab.add(hunterCreationProbability);
		generalTab.add(new JLabel("AllesEter creation probability"));
		final JTextField alleseterCreationProbability = new JTextField();
		generalTab.add(alleseterCreationProbability);
		
		// change setting button
		JButton change = new JButton("change setting");
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Simulator.setRabbitCreationProbability(stringToDouble(rabbitCreationProbability));
				Simulator.setFoxCreationProbability(stringToDouble(foxCreationProbability));	
				Simulator.setBearCreationProbability(stringToDouble(bearCreationProbability));
				Simulator.setHunterCreationProbability(stringToDouble(hunterCreationProbability));
				Simulator.setAllesEterCreationProbability(stringToDouble(hunterCreationProbability));
			}			
		});
		generalTab.add(change);
		
		//	set default button
		JButton setDefault = new JButton("default");
		setDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Simulator.setDefault();
			}
		});
		generalTab.add(setDefault);
		
		// maak rabbits tab aan, layout en border
		JPanel rabbitTab = new JPanel();
		rabbitTab.setLayout(new GridLayout(8, 0));
		rabbitTab.setBorder(new EmptyBorder(10, 10, 10, 10));

		// voeg labels, tekstvelden en ActionListener toe aan rabbit tab
		rabbitTab.add(new JLabel("Breeding age"));
		final JTextField rabbitBreedingAge = new JTextField();
		rabbitTab.add(rabbitBreedingAge);
		rabbitTab.add(new JLabel("Max age"));
		final JTextField rabbitMaxAge = new JTextField();
		rabbitTab.add(rabbitMaxAge);
		rabbitTab.add(new JLabel("Breeding probability"));
		final JTextField rabbitBreedingProbability = new JTextField();
		rabbitTab.add(rabbitBreedingProbability);
		rabbitTab.add(new JLabel("Max litter size"));
		final JTextField rabbitMaxLitterSize = new JTextField();
		rabbitTab.add(rabbitMaxLitterSize);
		
		// change setting button
		JButton changeRabbit = new JButton("change setting");
		changeRabbit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Rabbit.setBreedingAge(stringToInt(rabbitBreedingAge));
				Rabbit.setMaxAge(stringToInt(rabbitMaxAge));		
				Rabbit.setBreedingProbability(stringToDouble(rabbitBreedingProbability));
				Rabbit.setMaxLitterSize(stringToInt(rabbitMaxLitterSize));		
			}			
		});
		rabbitTab.add(changeRabbit);
		
		//	set default button
		JButton setDefaultRabbit = new JButton("default");
		setDefaultRabbit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Rabbit.setDefault();
			}
		});
		rabbitTab.add(setDefaultRabbit);

		// maak foxes tab aan, layout en border
		JPanel foxTab = new JPanel();
		foxTab.setLayout(new GridLayout(8, 0));
		foxTab.setBorder(new EmptyBorder(10, 10, 10, 10));

		// voeg labels, tekstvelden en ActionListener toe aan fox tab
		foxTab.add(new JLabel("Breeding age"));
		final JTextField foxBreedingAge = new JTextField();
		foxTab.add(foxBreedingAge);
		foxTab.add(new JLabel("Max age"));
		final JTextField foxMaxAge = new JTextField();
		foxTab.add(foxMaxAge);
		foxTab.add(new JLabel("Breeding probability"));
		final JTextField foxBreedingProbability = new JTextField();
		foxTab.add(foxBreedingProbability);
		foxTab.add(new JLabel("Max litter size"));
		final JTextField foxMaxLitterSize = new JTextField();
		foxTab.add(foxMaxLitterSize);
		
		// change setting button
		JButton changeFox = new JButton("change setting");
		changeFox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Fox.setBreedingAge(stringToInt(foxBreedingAge));
				Fox.setMaxAge(stringToInt(foxMaxAge));		
				Fox.setBreedingProbability(stringToDouble(foxBreedingProbability));
				Fox.setMaxLitterSize(stringToInt(foxMaxLitterSize));		
			}			
		});
		foxTab.add(changeFox);
		
		//	set default button
		JButton setDefaultFox = new JButton("default");
		setDefaultFox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Fox.setDefault();
			}
		});
		foxTab.add(setDefaultFox);	
		
		
		// maak bears tab aan, layout en border
		JPanel bearTab = new JPanel();
		bearTab.setLayout(new GridLayout(8, 0));
		bearTab.setBorder(new EmptyBorder(10, 10, 10, 10));

		// voeg labels, tekstvelden en ActionListener toe aan bear tab
		bearTab.add(new JLabel("Breeding age"));
		final JTextField bearBreedingAge = new JTextField();
		bearTab.add(bearBreedingAge);
		bearTab.add(new JLabel("Max age"));
		final JTextField bearMaxAge = new JTextField();
		bearTab.add(bearMaxAge);
		bearTab.add(new JLabel("Breeding probability"));
		final JTextField bearBreedingProbability = new JTextField();
		bearTab.add(bearBreedingProbability);
		bearTab.add(new JLabel("Max litter size"));
		final JTextField bearMaxLitterSize = new JTextField();
		bearTab.add(bearMaxLitterSize);
		
		// change setting button
		JButton changeBear = new JButton("change setting");
		changeBear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bear.setBreedingAge(stringToInt(bearBreedingAge));
				Bear.setMaxAge(stringToInt(bearMaxAge));		
				Bear.setBreedingProbability(stringToDouble(bearBreedingProbability));
				Bear.setMaxLitterSize(stringToInt(bearMaxLitterSize));		
			}			
		});
		bearTab.add(changeBear);
		
		//	set default button
		JButton setDefaultBear = new JButton("default");
		setDefaultBear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bear.setDefault();
			}
		});
		bearTab.add(setDefaultBear);
		
		// maak alleseter tab aan, layout en border
				JPanel alleseterTab = new JPanel();
				alleseterTab.setLayout(new GridLayout(8, 0));
				alleseterTab.setBorder(new EmptyBorder(10, 10, 10, 10));

				// voeg labels, tekstvelden en ActionListener toe aan rabbit tab
				alleseterTab.add(new JLabel("Breeding age"));
				final JTextField alleseterBreedingAge = new JTextField();
				alleseterTab.add(alleseterBreedingAge);
				alleseterTab.add(new JLabel("Max age"));
				final JTextField alleseterMaxAge = new JTextField();
				alleseterTab.add(alleseterMaxAge);
				alleseterTab.add(new JLabel("Breeding probability"));
				final JTextField alleseterBreedingProbability = new JTextField();
				alleseterTab.add(alleseterBreedingProbability);
				alleseterTab.add(new JLabel("Max litter size"));
				final JTextField alleseterMaxLitterSize = new JTextField();
				alleseterTab.add(alleseterMaxLitterSize);
				
				// change setting button
				JButton changeAlleseter = new JButton("change setting");
				changeAlleseter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AllesEter.setBreedingAge(stringToInt(alleseterBreedingAge));
						AllesEter.setMaxAge(stringToInt(alleseterMaxAge));		
						AllesEter.setBreedingProbability(stringToDouble(alleseterBreedingProbability));
						AllesEter.setMaxLitterSize(stringToInt(alleseterMaxLitterSize));		
					}			
				});
				alleseterTab.add(changeAlleseter);
				
				//	set default button
				JButton setDefaultAllesEter = new JButton("default");
				setDefaultAllesEter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AllesEter.setDefault();
					}
				});
				alleseterTab.add(setDefaultAllesEter);
			
		// alle tabs toevoegen aan maintab
		// main tab toevoegen aan de settings frame
		mainTab.addTab("General", generalTab);
		mainTab.addTab("Rabbit", rabbitTab);
		mainTab.addTab("Fox", foxTab);
		mainTab.addTab("Bear", bearTab);
		mainTab.addTab("AllesEter", alleseterTab);		
		
		
		settingsFrame.add(mainTab);

		settingsFrame.setSize(new Dimension(640, 240));

		settingsFrame.setResizable(false);
		settingsFrame.setLocationRelativeTo(null); // center de settingsFrame
		settingsFrame.setVisible(true);
	}
    
    
    
    
    
    /**
     * Method to make the settings menu for all the animals.
     */
    public void makeSettingsMenu()
    {
    	
    }
    
    /**
     * Method to make a popup message
     * @param message message to be shown on screen.
     */
    private void makePopupMessage(String message)
    {
    	JOptionPane.showMessageDialog(frame, 
                message,"",
                 JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color)
    {
        colors.put(animalClass, color);
    }
    
	/**
	 * Getter voor boolean isReset
	 * @return isReset bepaald of step 0 is voor de historyview
	 */
	public boolean getIsReset() {
		return isReset;
	}
    
	/**
	 * convert text from JTextField to int
	 * @param number
	 */
	private int stringToInt(JTextField text) {
		int number = 0;
		if (!text.getText().equals("")) {
			String string = text.getText();
			for (int s = 0; s < string.length(); s++) {
				if (string.charAt(s) == '0' || string.charAt(s) == '1'
						|| string.charAt(s) == '2' || string.charAt(s) == '3'
						|| string.charAt(s) == '4' || string.charAt(s) == '5'
						|| string.charAt(s) == '6' || string.charAt(s) == '7'
						|| string.charAt(s) == '8' || string.charAt(s) == '9') {
					number++;
				}
			}
			if (number == string.length()) {
				number = Integer.parseInt(text.getText());
			} else {
				historyView.getTextArea().append(
						"Alleen hele getallen zijn toegestaan" + "\r\n");
				return number = -1;
			}
		}
		else{
				return number = -1;
		}
		return number;
	}

	/**
	 * convert text from JTextField to double
	 * @param number
	 */
	private double stringToDouble(JTextField text) {
		double number = 0;
		int komma = 0;
		if (!text.getText().equals("")) 
		{
			String string = text.getText();
			
			for (int s = 0; s < string.length(); s++) {
				if (string.charAt(s) == '0' || string.charAt(s) == '1'|| string.charAt(s) == '2' ||
					string.charAt(s) == '3' || string.charAt(s) == '4'|| string.charAt(s) == '5' ||
					string.charAt(s) == '6' || string.charAt(s) == '7'|| string.charAt(s) == '8' ||
					string.charAt(s) == '9' || string.charAt(s) == '.')
				{
					number++;
				}
				
				
				if (string.charAt(s) == '.')
				{
					komma++;
				}
			}
			
			if (number == string.length() && komma <= 1) {
				number = Double.parseDouble(text.getText());
			} else {
				historyView.getTextArea().append(
						"Alleen cijfers zijn toegestaan en . getallen" + "\r\n");
				return number = -1;
			}
		}
		else{
				return number = -1;
		}
		return number;
	}


    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass)
    {
        Color col = colors.get(animalClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Field field)
    {
        if(!frame.isVisible()) {
            frame.setVisible(true);
        }
            
        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();
        
        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if(animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                }
                else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }
    
    /**
     * Method to set the simulator so it won't have to be instanced.
     * @param simulator1 the simulator to be set.
     */
    public static void setSimulator(Simulator simulator1)
    {
    	simulator = simulator1;
    }
  
       	/**
    	 * maak pieChart aan
    	 * @param height
    	 * @param width
    	 */
    	private void makePieChart(int height, int width) {
    		PieFrame = new JFrame();
    		//JPanel PiePanel = new JPanel();
    		//PiePanel.setLayout(new GridLayout(0, 1));
    		PieFrame.setTitle("PieChart");
    		pieChart = new PieView();
    		pieChart.setSize(height * 2, width * 2);
    		pieChart.stats(getPopulationDetails());
    		pieChart.repaint();
    		//PiePanel.add(pieChart);
    	}
    	
    	/**
    	 * maak historygram aan
    	 * @param height
    	 * @param width
    	 */
    	private void makeHistogram(int height, int width) {
    		histogram = new Histogram();
    		histogram.setSize(height * 2, width * 2);
    		histogram.stats(getPopulationDetails());
    		histogram.repaint();
    	}

    	/**
    	 * maak historyView aan
    	 * @param height
    	 * @param width
    	 */
    	private void makeHistoryView(int height, int width) {
    		historyView = new HistoryView(height, width);
    		historyView.setSize(height, width);
    		historyView.stats(getPopulationDetails());
    		historyView.history(getIsReset());
    	}
        
    	/**
    	 * maak fieldView aan
    	 * @param height
    	 * @param width
    	 */
    	public void makeFieldView(int height, int width) {
    		fieldView = new FieldView(height, width);
    		stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
    		population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
    	}

    	/**
    	 * retourneert de counter voor ieder kleur 
    	 * @return colorStats HashMap die kleur bij houdt en de hoeveelheid
    	 */
    	@SuppressWarnings("rawtypes")
    	public HashMap<Color, Counter> getPopulationDetails() {
    		HashMap<Class, Counter> classStats = stats.getPopulation();
    		HashMap<Color, Counter> colorStats = new HashMap<Color, Counter>();

    		for (Class c : classStats.keySet()) {
    			colorStats.put(getColor(c), classStats.get(c));
    		}
    		return colorStats;
    	}   	  	   	
    }


