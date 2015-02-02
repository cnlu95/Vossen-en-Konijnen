package View;
import Logic.Counter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

/**
 * De klasse abstractview.
 * 
 * @author Caroline
 * @version 1.0
 */

interface AbstractView
{
	/**
	 * stats wordt toegewezen
	 * @param stats populate stats
	 */
	public void stats(HashMap<Color, Counter> stats);
	
	/**
	 * bepaald de frame grote van de histogram
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height);
	
	/**
	 * maak de histogram
	 * @param g Graphic component
	 */
	public void paintComponent(Graphics g);
}