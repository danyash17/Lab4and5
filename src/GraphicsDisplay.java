

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphicsDisplay extends JPanel {

	private ArrayList<Double[]> graphicsData;
	private ArrayList<Double[]> originalData;
	private int selectedMarker = -1;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	
	private double scaleX;
	private double scaleY;
	
	private double[][] viewport = new double[2][2];
	private ArrayList<double[][]> undoHistory = new ArrayList(5);
	private boolean showAxis = true;
	private boolean showMarkers = true;
	private boolean clockRotate = false;
	private boolean antiClockRotate = false;

	private Font axisFont;
	private Font labelsFont;
	
	private BasicStroke axisStroke;
	//private BasicStroke graphicsStroke;
	private BasicStroke markerStroke;
	private BasicStroke gridStroke;
	private BasicStroke selectionStroke;
	private static DecimalFormat formatter=(DecimalFormat)NumberFormat.getInstance();
	
	private boolean scaleMode = false;
	private boolean changeMode = false;
	private double[] originalPoint = new double[2];
	private Rectangle2D.Double selectionRect = new Rectangle2D.Double();
	
	public GraphicsDisplay ()	{
		setBackground(Color.WHITE);
		/*graphicsStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, 
				new float [] {4,1,1,1,2,1,1,1,4}, 0.0f);*/
		axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
		markerStroke = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 5.0f, null, 0.0f);
		selectionStroke = new BasicStroke(1.0F, 0, 0, 10.0F, new float[] { 10, 10 }, 0.0F);		
		gridStroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 5.0f, new float [] {5,5}, 2.0f);
		axisFont = new Font("Serif", Font.BOLD, 36);
		labelsFont = new java.awt.Font("Serif",0,10);
		addMouseMotionListener(new MouseMotionHandler());
		addMouseListener(new MouseHandler());
	}
	
	public void showGraphics(ArrayList<Double[]> graphicsData)	{
		this.graphicsData = graphicsData;
		

	    this.originalData = new ArrayList(graphicsData.size());
	    for (Double[] point : graphicsData) {
	      Double[] newPoint = new Double[2];
	      newPoint[0] = new Double(point[0].doubleValue());
	      newPoint[1] = new Double(point[1].doubleValue());
	      this.originalData.add(newPoint);
	    }
	    this.minX = ((Double[])graphicsData.get(0))[0].doubleValue();
	    this.maxX = ((Double[])graphicsData.get(graphicsData.size() - 1))[0].doubleValue();
	    this.minY = ((Double[])graphicsData.get(0))[1].doubleValue();
	    this.maxY = this.minY;
		
	    for (int i = 1; i < graphicsData.size(); i++) {
	        if (((Double[])graphicsData.get(i))[1].doubleValue() < this.minY) {
	          this.minY = ((Double[])graphicsData.get(i))[1].doubleValue();
	        }
	        if (((Double[])graphicsData.get(i))[1].doubleValue() > this.maxY) {
	          this.maxY = ((Double[])graphicsData.get(i))[1].doubleValue();
	        }
	    }
		
		zoomToRegion(minX, maxY, maxX, minY);
		
		}
	
	public void zoomToRegion(double x1,double y1,double x2,double y2)	{
		this.viewport[0][0]=x1;
		this.viewport[0][1]=y1;
		this.viewport[1][0]=x2;
		this.viewport[1][1]=y2;
		this.repaint();
	}
	public void setShowAxis(boolean showAxis) {
		this.showAxis = showAxis;
		repaint();
	}

	public void setShowMarkers(boolean showMarkers) {
		this.showMarkers = showMarkers;
		repaint();
	}
	
}