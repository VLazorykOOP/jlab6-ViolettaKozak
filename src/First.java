import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class First {
    public static void main() {
        RotatingLine frame = new RotatingLine();
    }
}
class RotatingLine extends Frame {

    public RotatingLine() {
        Frame f = new Frame();
        f.add(new MyCanvas());
        f.setSize(650, 650);
        f.setVisible(true);
 // запускаємо таймер
    }
}

class MyCanvas extends Canvas{
    final private Point2D point;      // точка на відрізку
    final private Line2D line1;
    final private Line2D line2;// відрізок
    private double angle = 0;
    public MyCanvas(){
        setBackground(Color.white);
        setSize(650, 650);


        Point2D p1 = new Point2D.Double(200, 240);
        Point2D p2 = new Point2D.Double(400, 240);

        // ініціалізуємо відрізок
        line1 = new Line2D.Double(p1, p2);
        line2 = new Line2D.Double(p1, p2);

        // початкова точка - середина відрізка
        point = new Point2D.Double((p1.getX() + p2.getX()) / 2,
                (p1.getY() + p2.getY()) / 2);

        // створюємо таймер з частотою 30 кадрів на секунду
        Timer timer = new Timer(33, e -> {
            // обчислюємо нові координати точки на відрізку
            double newX = line1.getX1() + (line1.getX2() - line1.getX1()) / 2
                    + Math.cos(Math.toRadians(angle)) * (line1.getY2() - line1.getY1()) / 2;
            double newY = line1.getY1() + (line1.getY2() - line1.getY1()) / 2
                    + Math.sin(Math.toRadians(angle)) * (line1.getX2() - line1.getX1()) / 2;
            double x1 = newX-100+Math.cos(Math.toRadians(angle))*(line1.getY2() - line1.getY1());
            double x2 = newX+100-Math.cos(Math.toRadians(angle))*(line1.getY2() - line1.getY1());
            double y1 = newY+Math.sin(Math.toRadians(angle))*(line1.getX2() - line1.getX1());
            double y2 = newY-Math.sin(Math.toRadians(angle))*(line1.getX2() - line1.getX1()) ;

            // оновлюємо координати точки
            point.setLocation(newX, newY);
            line2.setLine(x1,y1,x2,y2);

            angle++;

            // перерисовуємо вікно
            repaint();
        });

        timer.start();
    }
    public void paint (Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // малюємо відрізок
        g2.draw(line2);

        // малюємо точку на відрізку
        Ellipse2D.Double circle = new Ellipse2D.Double(point.getX() - 5, point.getY() - 5, 10, 10);
        g2.fill(circle);
    }
}

