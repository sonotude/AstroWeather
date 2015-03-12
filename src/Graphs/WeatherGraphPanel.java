package Graphs;

import API.Forecast;
import API.Forecasts;
import AstroWeather.Main;
import Common.AstroPanel;
import Home.MainPanel;

import org.jfree.data.xy.XYSeries;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

@SuppressWarnings("serial")

	 
public abstract class WeatherGraphPanel extends AstroPanel {
	
	private ActionListener backListener = new ActionListener() {

		  @Override
		  public void actionPerformed(ActionEvent e) {
		   parent.changePanel(new MainPanel(parent, orientation, forecasts));
		  }
	};


	public WeatherGraphPanel(Main parent, boolean orientation, Forecasts forecasts, String graphTitle) {
		super(parent, orientation, forecasts);
		JButton back = new JButton("Back");
		back.addActionListener(backListener);
		add(back, BorderLayout.NORTH);
		setLayout(new BorderLayout());
		add(new WeatherGraph(graphTitle, collectDataByHour(forecasts.getHourlyForecasts(), graphTitle)), BorderLayout.CENTER);
	}

	@Override
	public void changeOrientation(boolean orientation) {
		// Nothing special needs to happen for the temperature graph,
		// it will resize automatically
	}

	private XYSeries collectDataByHour(List<Forecast> hourlyForecasts, String graphTitle) {
		XYSeries dataByHour = new XYSeries(graphTitle);
		for (Forecast forecast : hourlyForecasts) {
			dataByHour.add(forecast.date.getTime(), extractValue(forecast));
		}
		return dataByHour;
	}

	protected abstract double extractValue(Forecast forecast);
}
