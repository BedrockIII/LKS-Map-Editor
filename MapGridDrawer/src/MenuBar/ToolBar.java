package MenuBar;

import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import GUI.GridHandler;
public class ToolBar extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ToolBar()
	{
		
		JToggleButton PaintToolSelect = null;
		ImageIcon pressedGrid = null;
		try {
			PaintToolSelect = new JToggleButton(new ImageIcon(ClassLoader.getSystemResourceAsStream("Grid1.png").readAllBytes()));
			pressedGrid = new ImageIcon(ClassLoader.getSystemResourceAsStream("GridPressed1.png").readAllBytes());
			
		} catch (IOException e) 
		{
			System.out.println("Failed to locate Paint Button Images");
		} catch (NullPointerException e)
		{
			PaintToolSelect = new JToggleButton(new ImageIcon("Grid1.png"));
			pressedGrid = new ImageIcon("GridPressed1.png");
		}
		
		PaintToolSelect.setBorderPainted(false);
		PaintToolSelect.setContentAreaFilled(false);
		PaintToolSelect.setFocusPainted(false);
		PaintToolSelect.setPressedIcon(pressedGrid);
		PaintToolSelect.setSelectedIcon(pressedGrid);
		PaintToolSelect.addActionListener(e -> 
		{
			GridHandler.TOOLINUSE = GridHandler.SELECTMODE;
		});
        add(PaintToolSelect);
		
		
		JToggleButton SelectToolSelect= null;
		ImageIcon pressedSelect = null;
		try {
			SelectToolSelect = new JToggleButton(new ImageIcon(ClassLoader.getSystemResourceAsStream("Grid1.png").readAllBytes()));
			pressedSelect = new ImageIcon(ClassLoader.getSystemResourceAsStream("GridPressed1.png").readAllBytes());
			
		} catch (IOException e) 
		{
			System.out.println("Failed to locate Select Button Images");
		} catch (NullPointerException e)
		{
			SelectToolSelect = new JToggleButton(new ImageIcon("src/Grid1.png"));
			pressedSelect = new ImageIcon("src/GridPressed1.png");
		}
		SelectToolSelect.setBorderPainted(false);
		SelectToolSelect.setContentAreaFilled(false);
		SelectToolSelect.setFocusPainted(false);
		SelectToolSelect.setPressedIcon(pressedSelect);
		SelectToolSelect.setSelectedIcon(pressedSelect);
		SelectToolSelect.addActionListener(e -> 
		{
			GridHandler.TOOLINUSE = GridHandler.PAINTMODE;
		});
		add(SelectToolSelect);
		
		add(new ExportObjButton());
	}
}
