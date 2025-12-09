package org.ynov.world;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Fenêtre Swing pour visualiser la planète.
 * - Affiche la grille width x height
 * - Dessine les OBSTACLE en gris
 * - Dessine les ROVER en rouge
 *
 * Hypothèses :
 *  - TypeElement possède au moins OBSTACLE et ROVER
 *  - Element a getType() et getPosition() (Vector2 avec x, y)
 *  - Planet a findElements(TypeElement), getObstacles(), GetElement(Vector2)
 *  - Le tableau interne elements[][] correspond à [width][height]
 */
public class PlanetFrame extends JFrame {

    private final Planet planet;
    private final PlanetPanel panel;

    public PlanetFrame(Planet planet) {
        super("Planet Viewer - " + getPlanetNameSafe(planet));
        this.planet = planet;
        this.panel = new PlanetPanel(planet);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel);
        setMinimumSize(new Dimension(480, 480));
        pack();
        setLocationRelativeTo(null);
    }

    public void update() {
        panel.repaint(); // Redessine la grille et les éléments
    }


    private static String getPlanetNameSafe(Planet p) {
        try {
            Field f = Planet.class.getDeclaredField("name");
            f.setAccessible(true);
            Object v = f.get(p);
            return v == null ? "Planet" : v.toString();
        } catch (Exception e) {
            return "Planet";
        }
    }

    private static class PlanetPanel extends JPanel {
        private final Planet planet;

        PlanetPanel(Planet planet) {
            this.planet = planet;
            setBackground(Color.white);
            setPreferredSize(new Dimension(planet.getSize().x, planet.getSize().y));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getPlanetWidth(planet);
            int height = getPlanetHeight(planet);

            if (width <= 0 || height <= 0) {
                g2.setColor(Color.RED);
                g2.drawString("No Data.", 20, 20);
                g2.dispose();
                return;
            }

            int cell = Math.max(1, Math.min(getWidth() / width, getHeight() / height));
            int gridW = cell * width;
            int gridH = cell * height;
            int offsetX = (getWidth() - gridW) / 2;
            int offsetY = (getHeight() - gridH) / 2;

            g2.setColor(new Color(245, 245, 245));
            g2.fillRect(offsetX, offsetY, gridW, gridH);

            g2.setColor(new Color(210, 210, 210));
            for (int x = 0; x <= width; x++) {
                int xx = offsetX + x * cell;
                g2.drawLine(xx, offsetY, xx, offsetY + gridH);
            }
            for (int y = 0; y <= height; y++) {
                int yy = offsetY + y * cell;
                g2.drawLine(offsetX, yy, offsetX + gridW, yy);
            }

            List<Element> obstacles = planet.getObstacles();
            g2.setColor(new Color(80, 80, 80));
            for (Element e : obstacles) {
                Vector2 p = e.getPosition();
                int px = offsetX + p.x * cell;
                int py = offsetY + p.y * cell;
                g2.fillRect(px + 1, py + 1, cell - 1, cell - 1);
            }

            List<Element> rovers = planet.findElements(TypeElement.ROVER);
            g2.setColor(new Color(200, 30, 30));
            for (Element r : rovers) {
                Vector2 p = r.getPosition();
                int cx = offsetX + p.x * cell;
                int cy = offsetY + p.y * cell;

                int margin = Math.max(2, cell / 6);
                int size = cell - 2 * margin;
                g2.fillOval(cx + margin, cy + margin, size, size);

                g2.setColor(Color.BLACK);
                g2.drawOval(cx + margin, cy + margin, size, size);
                g2.setColor(new Color(50, 100, 100));
            }

            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Grille: " + width + " x " + height, offsetX, offsetY - 8 < 12 ? 12 : offsetY - 8);
            g2.dispose();
        }

        private static int getPlanetWidth(Planet planet) {
           // Element[][] arr = getElementsArray(planet);
            //return (arr != null) ? arr.length : 0;
            return planet.getSize().x;
        }

        private static int getPlanetHeight(Planet planet) {
//            Element[][] arr = getElementsArray(planet);
//            return (arr != null && arr.length > 0 && arr[0] != null) ? arr[0].length : 0;
            return planet.getSize().y;
        }

        @SuppressWarnings("unchecked")
        private static Element[][] getElementsArray(Planet planet) {
            try {
                Field f = Planet.class.getDeclaredField("elements");
                f.setAccessible(true);
                return (Element[][]) f.get(planet);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void view() {
        SwingUtilities.invokeLater(() -> {
            Planet p = Planet.getPlanet();
            List<Element> rovers = p.findElements(TypeElement.ROVER);
            if (rovers.isEmpty()) {
                p.addElement(new Element(TypeElement.ROVER, new Vector2(0, 0)));
            }

            new PlanetFrame(p).setVisible(true);
        });
    }
}
