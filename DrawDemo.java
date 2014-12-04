import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

class Point
{
   private double x;
   private double y;
   
   public Point (double x1, double y1)
   {
      x = x1;
      y = y1;
   }
   
   public double getX()
   {
      return x;
   }
   
   public double getY()
   {
      return y;
   }
}

class LineSegment
{

   public static int count = 0;
   private Point p1;
   private Point p2;
   
   public LineSegment(Point point1, Point point2)
   {
      p1 = point1;
      p2 = point2;
      
      count +=1;
   }
   
   public  Point getPointOne()
   {
      return p1;
   }
   
   public  Point getPointTwo()
   {
      return p2;
   }
   
   public static int getcount()
   {
      return count;
   }
   

} 


public class DrawDemo
{
   public static void main (String[] args)
   {
      TextTestFrame frame = new TextTestFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}


class TextTestFrame extends JFrame
{
   private JLabel label;
   public TextTestFrame()
   {
      setTitle("Demo");
      
      DocumentListener listener = new ClockFieldListener();
      ColorAction cAction = new ColorAction ( Color.CYAN );
      GridAction gridAction = new GridAction();
      DemoAction demoAction = new DemoAction();
      ResetDemoAction resetdemoAction = new ResetDemoAction();
      
      // add a panel with text fields
      
      JPanel panel = new JPanel();
      
      panel.add(new JLabel("Points:" ));
      hourField = new JTextField("0", 3);
      panel.add(hourField);
      hourField.getDocument().addDocumentListener(listener);
      
      
      JButton drawGrid = new JButton("Grid");
      JButton drawDemo = new JButton("Step");
      JButton resetDemo = new JButton("Reset Step");
      JButton paint = new JButton("Paint");
      
      
      panel.add(drawDemo, BorderLayout.SOUTH);
      panel.add(resetDemo, BorderLayout.SOUTH);
      panel.add(drawGrid, BorderLayout.SOUTH);
      panel.add(paint, BorderLayout.SOUTH);
   
      
      
      paint.addActionListener( cAction );
      drawGrid.addActionListener(gridAction);
      drawDemo.addActionListener(demoAction);
      resetDemo.addActionListener(resetdemoAction);
      
      add(panel, BorderLayout.SOUTH);
      
      clock = new ClockPanel();
      add(clock, BorderLayout.CENTER);
      pack();
      
   
   }
   
   // Set the clock to the values stored in the text fields

   public void setClock()
   {
      try
      {
         
         int hours = Integer.parseInt(hourField.getText().trim());
         clock.setTime(hours);
      }
       
      catch (NumberFormatException e){System.out.println(e);}
   }
   
   public static final int DEFAULT_WIDTH = 500;
   public static final int DEFAULT_HEIGHT = 700;
   
   private JTextField hourField;
   private JTextField minuteField;
   private ClockPanel clock =  new ClockPanel();
   
   private class ClockFieldListener implements DocumentListener
   {
      public void insertUpdate(DocumentEvent event) {setClock();}
      public void removeUpdate(DocumentEvent event) {setClock();}
      public void changedUpdate(DocumentEvent event) {}
   }
  
   private class ColorAction implements ActionListener
   {
      private Color _backgroundColor;
      
      public ColorAction(Color c)
      {
         _backgroundColor = c;
      }
      
      public void actionPerformed( ActionEvent evt)
      {
         clock.Paint( _backgroundColor );
      }
   }
   
   private class GridAction implements ActionListener
   {
      public GridAction()
      {
      
      }
      
      public void actionPerformed (ActionEvent evt)
      {
         clock.drawGrid();
      }
   }
   
   private class DemoAction implements ActionListener
   {
      public DemoAction()
      {
      
      }
      
      public void actionPerformed ( ActionEvent evt )
      {
         clock.drawDemo();
      }
   }
   
   private class ResetDemoAction implements ActionListener
   {
      public ResetDemoAction()
      {
      
      }
      
      public void actionPerformed ( ActionEvent evt )
      {
         clock.resetPoints();
      }
   }
   
}

// A panel that draws a clock

class ClockPanel extends JPanel
{
   public ClockPanel()
   {
      setPreferredSize(new Dimension(2 * RADIUS + 1, 2 * RADIUS + 1));
      setBackground(Color.WHITE);
   }
   public void Paint ( Color c)
   {
      paint = !paint;
      if (paint)
         setBackground(c);
      else
         setBackground(Color.WHITE);
   }
   
   public void drawGrid()
   {
      gridon = !gridon;
      repaint();
   }
   public void drawDemo() 
   {
   
      ++points;
      
      
      if (points >= 250)
      {
         overload = true; 
      }
      else
      {
         overload = false;
         getPoints(points);
      }
   
            
      repaint();
   }
   private void drawGrid(Graphics g)
   {
      if (gridon)
      {
         int w, h = w = RADIUS*2;
         int w1 = w/2, w2 = w/2, wt = h/2, h1 = h/2, h2 = h/2, ht = h/2;
          
         
         g.drawLine(w1, h, w1, 0);
         g.drawLine(0, h/2, w, h/2);
        
         while ( wt >= 50)
         {
            wt = wt/2; 
         } 
      
         while (ht >= 50)
         {
            ht = ht/2;
         }
      
         while ( w2 >= 50)
         {
            w1 += wt;
            w2 -= wt;
         
            g.drawLine(w1, h, w1, 0);
            g.drawLine(w2, h, w2, 0);
         
         } 
       
         while (h2 >= 50)
         {  
            h1 += ht;
            h2 -= ht;
         
            g.drawLine(0, h1, w, h1);
            g.drawLine(0, h2, w, h2);
         } 
      
      }
   }
  
   
   public void paintComponent (Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      
      Dimension size = super.getSize();
   
      double xRatio = (double)size.width / (2* RADIUS);
      double yRatio = (double)size.height / (2 * RADIUS);
      
      
      if (overload)
      {
         Ellipse2D circle = new Ellipse2D.Double(0, 0, 2* RADIUS * xRatio , 2* RADIUS * yRatio);
         g2.fill(circle);
         
      }
      else
      {
         drawLines(g2, xRatio, yRatio);
      }
      
      
      drawGrid(g);
      
   
      
   }

   public void setTime(int h)
   {
      if (h >= 250)
      {
         overload = true; 
      }
      else
      {
         overload = false;
         getPoints(h);
      }
      repaint();
      
      
      
   }
   
   public void getPoints(int npoints)
   {  
      Point[] point = new Point[npoints];
      double arc = 2 * Math.PI / npoints;
      double narc = 0;
      int i;
      
      for (i = 0; i < npoints; ++ i)
      {
         narc += arc;
         point[i] = new Point(Math.cos(narc)*RADIUS
             + RADIUS, Math.sin(narc)*RADIUS + RADIUS);
      }
          
      getSegments( point, npoints);    
   }
   
   public void getSegments( Point[] point, int npoints)
   {
      int count = 0;
      LineSegment.count = 0;
      int maxsegs = npoints * npoints;
      segs = new LineSegment[maxsegs];
      for ( int j = 0; j < npoints; ++j)
      {
         for (int i = 0; i < npoints; ++i)
         {
            if (i != j)
            {
               count = LineSegment.count;
               segs[count] = new LineSegment(point[j], point[i]);
            }
         }
      }
      
      
   }
      
   public void drawLines(Graphics2D g2, double xRatio, double yRatio)
   {  
      double x1, y1, x2, y2;
      
   
         
      if (LineSegment.count >= 2) 
      {
         for (int j = 0; j < LineSegment.count; ++ j)
         {
            x1 = segs[j].getPointOne().getX() * xRatio;
            y1 = segs[j].getPointOne().getY() * yRatio;
            
            x2 = segs[j].getPointTwo().getX() * xRatio;
            y2 = segs[j].getPointTwo().getY() * yRatio;
            
            Line2D line = new Line2D.Double(x1, y1, x2, y2);
         
            g2.draw(line);
         }
      }
   
   }
   
   public void resetPoints()
   {
      points = 1;
      getPoints(points);
      repaint();
   }
   
   private static LineSegment[] segs;
   private double minutes = 0;
   private int RADIUS = 300;
   private double MINUTE_HAND_LENGTH = 0.8 * RADIUS;
   private double HOUR_HAND_LENGTH = 0.6 * RADIUS;
   private static boolean gridon = false;
   private static int DemoPoints = 50;
   private static int points = 1;
   private static boolean paint = false;
   private static boolean overload = false;
}

