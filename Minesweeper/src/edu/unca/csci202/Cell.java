package edu.unca.csci202;
public class Cell {
	private String display;

	/**
	 *  creates a cell with input display
	 * @param display String display to be shown on board to player
	 */
	public Cell(String display ) {
		this.display=display;
	}
	/**
	 * sets the display of a cell to be shown on the board
	 * @param display
	 */
	public void setDisplay(String display) {
		this.display=display;
	}
	/**
	 * gets the display of a cell on the game board
	 * @return returns the display of a cell
	 */
	public String getDisplay() {
		return display;
	}
	/**
	 * checks if there is a mine on the selected cell
	 * @return returns true or false if there is a mine on the selected cell
	 */
	public boolean mineCheck() {
		return display.equals("M");
	}
}
