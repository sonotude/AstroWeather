package Location;

import AstroWeather.Main;
import Common.AstroPanel;
import Common.Resources;
import Home.MainPanel;
import NewAPI.API;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

@SuppressWarnings("serial")
public class LocationPanel extends AstroPanel {

	private ActionListener goButtonAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				removeAll();
				add(loading, BorderLayout.CENTER);
				validate();
				repaint();
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							String city = locationField.getText();
                            parent.setForecast(new API().getForecastFromString(city));
                            parent.changePanel(new MainPanel(parent, false, 1));

                            File f = new File("previousSearches.txt");
                            f.createNewFile();
                            Scanner s = new Scanner(f);
							PrintWriter pw = new PrintWriter("temp.txt");
							pw.println(city);
							int i = 0;
							while(s.hasNextLine() && i < 7) {
								String str = s.nextLine();
								pw.println(str);
								i++;
							}
							s.close();
							pw.close();
							Path from = new File("temp.txt").toPath();
							Path to = new File("previousSearches.txt").toPath();

							Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
						} catch (Exception e) {
                            System.err.println(e + e.getMessage());
                            e.printStackTrace();
							parent.changePanel(new LocationPanel(parent, "Place does not exist"));
						}
					}
				});
				t.start();
				
			} catch (Exception ex) {
				
			}
		}
	};
	
	private ActionListener prevButtonAction = new ActionListener() {

		@Override
		public void actionPerformed(final ActionEvent e) {
			try {
				removeAll();
				add(loading, BorderLayout.CENTER);
				validate();
				repaint();
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							String city = ((JButton)e.getSource()).getText();
                            parent.setForecast(new API().getForecastFromString(city));
							parent.changePanel(new MainPanel(parent, false, 1));
						} catch (Exception e) {
							parent.changePanel(new LocationPanel(parent));
						}
					}
				});
				t.start();
				
			} catch (Exception ex) {
				
			}
		}
	};

	private JTextField locationField = new JTextField();
	private JPanel loading, topPanel;

	public LocationPanel(Main parent) {
		super(parent);
        commonInit();
		
	}

    public LocationPanel(Main parent, String error) {
        super(parent);
        commonInit();
        JLabel errLabel = new JLabel(error);
        errLabel.setForeground(Color.red);
        errLabel.setHorizontalTextPosition(JLabel.CENTER);
        topPanel.add(errLabel, BorderLayout.SOUTH);
    }
    void commonInit() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 5, 5, 5));

        locationField.setOpaque(false);
        locationField.setFont(Resources.titleFont);
        locationField.setForeground(Color.gray);
        locationField.setBorder(BorderFactory.createCompoundBorder(locationField.getBorder(), new EmptyBorder(5, 3, 3, 3)));
        locationField.setText("Enter Location...");
        locationField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                locationField.setText("");
                locationField.setForeground(Resources.titleColor);
                locationField.removeMouseListener(this);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JButton goBtn = new JButton("Go");
        goBtn.setOpaque(false);
        goBtn.setFont(Resources.titleFont);
        goBtn.setForeground(Resources.titleColor);
        goBtn.setContentAreaFilled(false);
        goBtn.addActionListener(goButtonAction);

        topPanel = new JPanel(new BorderLayout());
        topPanel.add(locationField, BorderLayout.CENTER);

        JPanel previouslyPanel = new JPanel(new GridLayout(8, 1));
        previouslyPanel.setOpaque(false);
        try {
            Scanner s = new Scanner(new File("previousSearches.txt"));
            int i = 0;
            while(s.hasNextLine()&& i < 8) {
                JButton btn = new PreviousLocationButton(s.nextLine());
                btn.addActionListener(prevButtonAction);
                previouslyPanel.add(btn);
            }
            s.close();
        } catch (Exception e) {}

        topPanel.add(goBtn, BorderLayout.EAST);
        topPanel.setOpaque(false);
        add(topPanel, BorderLayout.NORTH);
        add(previouslyPanel, BorderLayout.CENTER);

        loading = new JPanel(new BorderLayout());
        loading.setOpaque(false);
        try {
            loading.add(new JLabel(new ImageIcon(ClassLoader.getSystemResource("loader.gif"))), BorderLayout.CENTER);
        } catch (Exception e) {}
    }

}
