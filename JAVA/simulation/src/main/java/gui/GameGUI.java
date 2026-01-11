package gui;

import game.*;
import org.apache.commons.lang3.StringUtils;
import organisms.Organism;
import organisms.animals.*;
import organisms.plants.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameGUI extends JFrame {
    private World world;
    private JPanel boardPanel;
    private JTextArea logArea;
    private JTextArea statsArea;
    private JLabel infoLabel;
    private JPanel chartPanel;
    private int rows;
    private int cols;
    private final int HEX_SIZE = 20; // Radius of hex
    private final int SQUARE_SIZE = 30;

    private final List<Map<String, Integer>> history = new ArrayList<>();

    private final Map<String, Color> organismColors = new HashMap<>();

    public GameGUI(final World world) {
        this.world = world;
        this.rows = world.getHeight();
        this.cols = world.getWidth();

        setTitle("Virtual world simulation - Mateusz Bednarczyk XXXXXX");
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeColors();
        initializeMenu();
        initializeTopPanel();
        initializeLeftPanel();
        initializeRightPanel();
        initializeMapPanel();
        initializeInputHandling();

        setVisible(true);
        requestFocusInWindow();
    }

    private void initializeMapPanel() {
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawWorld(g);
            }
        };
        boardPanel.setBackground(Color.WHITE);
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
                GameGUI.this.requestFocusInWindow(); // Ensure focus for keyboard
            }
        });

        final JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.DARK_GRAY);
        centerWrapper.add(boardPanel);

        final JScrollPane boardScroll = new JScrollPane(centerWrapper);
        boardScroll.setBorder(null);
        add(boardScroll, BorderLayout.CENTER);

        updateInfoLabel();
        updateStats();

        setFocusable(true);
    }

    private void initializeInputHandling() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
                    GameGUI.this.world.makeTurn();
                    updateStats();
                    boardPanel.repaint();
                    return;
                }

                final Human human = world.getHuman();
                if (Objects.nonNull(human) && human.isAlive()) {
                    boolean moved = false;

                    if (world instanceof SquareWorld) {
                        if (key == KeyEvent.VK_UP) {
                            human.setNextMoveDirection(0);
                            moved = true;
                        } else if (key == KeyEvent.VK_DOWN) {
                            human.setNextMoveDirection(1);
                            moved = true;
                        } else if (key == KeyEvent.VK_LEFT) {
                            human.setNextMoveDirection(2);
                            moved = true;
                        } else if (key == KeyEvent.VK_RIGHT) {
                            human.setNextMoveDirection(3);
                            moved = true;
                        }
                    } else if (world instanceof HexWorld) {
                        if (key == KeyEvent.VK_Q) {
                            human.setNextMoveDirection(0);
                            moved = true;
                        } else if (key == KeyEvent.VK_W) {
                            human.setNextMoveDirection(1);
                            moved = true;
                        } else if (key == KeyEvent.VK_A) {
                            human.setNextMoveDirection(2);
                            moved = true;
                        } else if (key == KeyEvent.VK_D) {
                            human.setNextMoveDirection(3);
                            moved = true;
                        } else if (key == KeyEvent.VK_Z) {
                            human.setNextMoveDirection(4);
                            moved = true;
                        } else if (key == KeyEvent.VK_X) {
                            human.setNextMoveDirection(5);
                            moved = true;
                        }
                    }

                    if (moved) {
                        world.makeTurn();
                        updateStats();
                        boardPanel.repaint();
                    }
                }
            }
        });
    }

    private void initializeRightPanel() {
        final var rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(320, 0));

        final var statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(new TitledBorder("Statistics"));
        statsPanel.setPreferredSize(new Dimension(300, 250));

        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, 12));
        statsArea.setFocusable(false);

        final var statsScrollPane = new JScrollPane(statsArea);
        statsPanel.add(statsScrollPane, BorderLayout.CENTER);

        rightPanel.add(statsPanel, BorderLayout.NORTH);

        JPanel chartAndLegendPanel = new JPanel(new BorderLayout());

        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);
            }
        };
        chartPanel.setBorder(new TitledBorder("Population chart"));
        chartPanel.setBackground(Color.WHITE);
        chartAndLegendPanel.add(chartPanel, BorderLayout.CENTER);

        JPanel legendPanel = new JPanel(new GridLayout(0, 2, 5, 2));
        legendPanel.setBorder(new TitledBorder("Legend"));

        for (Map.Entry<String, Color> entry : organismColors.entrySet()) {
            final JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            final JPanel colorBox = new JPanel();
            colorBox.setPreferredSize(new Dimension(12, 12));
            colorBox.setBackground(entry.getValue());
            colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel nameLabel = new JLabel(entry.getKey());
            nameLabel.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, 10));

            itemPanel.add(colorBox);
            itemPanel.add(nameLabel);
            legendPanel.add(itemPanel);
        }
        final JPanel legendWrapper = new JPanel(new BorderLayout());
        legendWrapper.add(legendPanel, BorderLayout.NORTH);
        chartAndLegendPanel.add(legendWrapper, BorderLayout.SOUTH);
        rightPanel.add(chartAndLegendPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private void initializeLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 0));
        leftPanel.setBorder(new TitledBorder("Logs"));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, 12));
        logArea.setFocusable(false);

        final var scrollPane = new JScrollPane(logArea);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);
        this.world.getLogger().setLogArea(logArea);
    }

    private void initializeMenu() {
        final var menuBar = new JMenuBar();
        final var gameMenu = new JMenu("Options");
        final var newGameItem = new JMenuItem("New Game");
        final var newTurnItem = new JMenuItem("Next turn");
        final var saveItem = new JMenuItem("Save");
        final var loadItem = new JMenuItem("Load");
        final var quitItem = new JMenuItem("Exit without saving");

        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(newTurnItem);
        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        gameMenu.addSeparator();
        gameMenu.add(quitItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        newGameItem.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(GameInitializer::startNewGame);
        });

        newTurnItem.addActionListener(e -> {
            GameGUI.this.world.makeTurn();
            updateStats();
            boardPanel.repaint();
            GameGUI.this.requestFocusInWindow();
        });

        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save game state");
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.endsWith(".json")) {
                    filePath += ".json";
                }
                try {
                    GameGUI.this.world.saveWorldToFile(filePath);
                    logArea.append("Game saved to " + filePath + ".\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    logArea.append("Error occurred while saving the game: " + ex.getMessage() + "\n");
                }
            }
            GameGUI.this.requestFocusInWindow();
        });

        loadItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load game state");
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                try {
                    World loadedWorld = world.loadWorldFromFile(fileToLoad.getAbsolutePath());
                    setWorld(loadedWorld);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    logArea.append("Error occurred while loading the game: " + ex.getMessage() + "\n");
                }
            }
        });

        quitItem.addActionListener(e -> System.exit(0));
    }

    private void initializeTopPanel() {
        final var topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        infoLabel = new JLabel("Movement: ", SwingConstants.CENTER);
        infoLabel.setFont(new Font(Constants.FONT_NAME, Font.BOLD, 14));
        topPanel.add(infoLabel, BorderLayout.CENTER);

        final var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        initializeNextTurnButton(buttonPanel);
        initializeSpecialAbilityButton(buttonPanel);

        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initializeSpecialAbilityButton(final JPanel buttonPanel) {
        JButton abilityBtn = new JButton("Special ability (immortability)");
        abilityBtn.addActionListener(_ -> {
            if (GameGUI.this.world.getHuman() != null && GameGUI.this.world.getHuman().isAlive()) {
                GameGUI.this.world.getHuman().activateAbility();
                updateStats();
                boardPanel.repaint();
                GameGUI.this.requestFocusInWindow();
            }
        });
        buttonPanel.add(abilityBtn);
    }

    private void initializeNextTurnButton(final JPanel buttonPanel) {
        JButton nextTurnBtn = new JButton("Next turn");
        nextTurnBtn.addActionListener(_ -> {
            GameGUI.this.world.makeTurn();
            updateStats();
            boardPanel.repaint();
            GameGUI.this.requestFocusInWindow();
        });
        buttonPanel.add(nextTurnBtn);
    }

    private void initializeColors() {
        organismColors.put(Wolf.NAME, Wolf.COLOR);
        organismColors.put(Sheep.NAME, Sheep.COLOR);
        organismColors.put(Fox.NAME, Fox.COLOR);
        organismColors.put(Turtle.NAME, Turtle.COLOR);
        organismColors.put(Antelope.NAME, Antelope.COLOR);
        organismColors.put(CyberSheep.NAME, CyberSheep.COLOR);
        organismColors.put(Human.NAME, Human.COLOR);
        organismColors.put(Grass.NAME, Grass.COLOR);
        organismColors.put(Dandelion.NAME, Dandelion.COLOR);
        organismColors.put(Guarana.NAME, Guarana.COLOR);
        organismColors.put(Belladonna.NAME, Belladonna.COLOR);
        organismColors.put(Hogweed.NAME, Hogweed.COLOR);
    }

    private void setWorld(World newWorld) {
        this.world = newWorld;
        this.rows = world.getHeight();
        this.cols = world.getWidth();
        this.world.setLogger(new GameLogger());
        this.world.getLogger().setLogArea(logArea);

        logArea.setText("");
        logArea.append("Game loaded.\n");
        history.clear();
        updateInfoLabel();
        updateStats();

        boardPanel.repaint();
        boardPanel.revalidate();
        this.requestFocusInWindow();
    }

    private void updateInfoLabel() {
        if (this.world instanceof HexWorld) {
            infoLabel.setText("<html><center>Hexagonal mode<br>Controlling player with: Q(NW), W(NE), A(W), D(E), Z(SW), X(SE)<br>Space for next round without move</center></html>");
        } else {
            infoLabel.setText("<html><center>Square mode<br>Controlling player with: Arrows<br>Space for next round without move</center></html>");
        }
    }

    private void updateStats() {
        Map<String, Integer> counts = new HashMap<>();
        for (Organism o : world.getOrganisms()) {
            if (o.isAlive()) {
                String name = o.getName();
                counts.put(name, counts.getOrDefault(name, 0) + 1);
            }
        }

        history.add(new HashMap<>(counts));
        if (history.size() > 50) {
            history.removeFirst();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Turn: ").append(world.getTurnNumber()).append("\n");

        Human human = world.getHuman();
        if (human != null) {
            sb.append("Human: ").append(human.isAlive() ? "Alive" : "Dead").append("\n");
            if (human.isAlive()) {
                sb.append("Strength: ").append(human.getStrength()).append("\n");
                if (human.isAbilityActive()) {
                    sb.append("Ability: Active (lasts for ").append(human.getAbilityDuration()).append(" turns)\n");
                } else if (human.getAbilityCooldown() > 0) {
                    sb.append("Ability: Cooldown (resets in ").append(human.getAbilityCooldown()).append(" turns)\n");
                } else {
                    sb.append("Ability: Ready (Can activate)\n");
                }
            }
        } else {
            sb.append("Human: missing\n");
        }
        sb.append("\n--- Population ---\n");

        counts.forEach((name, count) -> sb.append(name).append(": ").append(count).append("\n"));

        statsArea.setText(sb.toString());
        chartPanel.repaint();
    }

    private void drawChart(Graphics g) {
        if (history.isEmpty()) return;

        final int w = chartPanel.getWidth();
        final int h = chartPanel.getHeight();
        final int padding = 20;

        int maxCount = 0;
        for (Map<String, Integer> map : history) {
            for (int count : map.values()) {
                if (count > maxCount) maxCount = count;
            }
        }
        if (maxCount == 0) maxCount = 1;

        final int stepX = (w - 2 * padding) / Math.max(1, history.size() - 1);
        final double scaleY = (double) (h - 2 * padding) / maxCount;

        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2.0f));

        java.util.Set<String> allNames = new java.util.HashSet<>();
        for (Map<String, Integer> map : history) {
            allNames.addAll(map.keySet());
        }

        for (String name : allNames) {
            g2d.setColor(organismColors.getOrDefault(name, Color.BLACK));

            for (int i = 0; i < history.size() - 1; i++) {
                int count1 = history.get(i).getOrDefault(name, 0);
                int count2 = history.get(i + 1).getOrDefault(name, 0);

                int x1 = padding + i * stepX;
                int y1 = h - padding - (int) (count1 * scaleY);
                int x2 = padding + (i + 1) * stepX;
                int y2 = h - padding - (int) (count2 * scaleY);

                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, h - padding, w - padding, h - padding);
        g2d.drawLine(padding, padding, padding, h - padding);
    }

    private void drawWorld(final Graphics g) {
        if (world instanceof SquareWorld) {
            drawSquareWorld(g);
        } else if (world instanceof HexWorld) {

            drawHexWorld(g);
        }
        boardPanel.revalidate();
    }

    private void drawSquareWorld(final Graphics g) {
        boardPanel.setPreferredSize(new Dimension(cols * SQUARE_SIZE + 1, rows * SQUARE_SIZE + 1));
        final Graphics2D g2d = (Graphics2D) g.create();

        try {
            g2d.setStroke(new BasicStroke(2.0f));
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.drawRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }
            }

            for (Organism o : world.getOrganisms()) {
                if (o.isAlive()) {
                    g2d.setColor(o.getColor());
                    g2d.fillRect(o.getX() * SQUARE_SIZE + 1, o.getY() * SQUARE_SIZE + 1, SQUARE_SIZE - 1, SQUARE_SIZE - 1);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(o.getX() * SQUARE_SIZE, o.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }
            }
        } finally {
            g2d.dispose();
        }
    }

    private void drawHexWorld(final Graphics g) {
        final double width = Math.sqrt(3) * HEX_SIZE;
        final double height = 2 * HEX_SIZE;
        final double horizontalSpacing = width;
        final double verticalSpacing = height * 0.75;
        int pWidth = (int) (cols * horizontalSpacing + width);
        int pHeight = (int) (rows * verticalSpacing + height);
        boardPanel.setPreferredSize(new Dimension(pWidth, pHeight));

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                double xPos = x * horizontalSpacing;
                double yPos = y * verticalSpacing;
                if (y % 2 != 0) {
                    xPos += width / 2.0;
                }
                drawHex(g, (int) xPos + HEX_SIZE, (int) yPos + HEX_SIZE, Color.WHITE, true);
            }
        }

        for (Organism o : world.getOrganisms()) {
            if (o.isAlive()) {
                double xPos = o.getX() * horizontalSpacing;
                double yPos = o.getY() * verticalSpacing;
                if (o.getY() % 2 != 0) {
                    xPos += width / 2.0;
                }
                drawHex(g, (int) xPos + HEX_SIZE, (int) yPos + HEX_SIZE, o.getColor(), false);
            }
        }
    }

    private void drawHex(Graphics g, int x, int y, Color color, boolean outlineOnly) {
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle_deg = 60 * i - 30;
            double angle_rad = Math.PI / 180 * angle_deg;
            hex.addPoint((int) (x + HEX_SIZE * Math.cos(angle_rad)),
                    (int) (y + HEX_SIZE * Math.sin(angle_rad)));
        }

        final Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setStroke(new BasicStroke(2.0f));
            if (outlineOnly) {
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawPolygon(hex);
            } else {
                g2d.setColor(color);
                g2d.fillPolygon(hex);
                g2d.setColor(Color.BLACK);
                g2d.drawPolygon(hex);
            }
        } finally {
            g2d.dispose();
        }
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        int gridX = -1;
        int gridY = -1;

        if (world instanceof SquareWorld) {
            gridX = mouseX / SQUARE_SIZE;
            gridY = mouseY / SQUARE_SIZE;
        } else if (world instanceof HexWorld) {
            final double width = Math.sqrt(3) * HEX_SIZE;
            final double height = 2 * HEX_SIZE;
            final double horizSpacing = width;
            final double vertSpacing = height * 0.75;

            double minDist = Double.MAX_VALUE;

            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    double xPos = x * horizSpacing;
                    double yPos = y * vertSpacing;
                    if (y % 2 != 0) {
                        xPos += width / 2.0;
                    }
                    double centerX = xPos + HEX_SIZE;
                    double centerY = yPos + HEX_SIZE;

                    double dist = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
                    if (dist < HEX_SIZE && dist < minDist) {
                        minDist = dist;
                        gridX = x;
                        gridY = y;
                    }
                }
            }
        }

        if (gridX >= 0 && gridX < cols && gridY >= 0 && gridY < rows) {
            if (!world.isOccupied(gridX, gridY)) {
                showAddOrganismDialog(gridX, gridY);
            }
        }
    }

    private void showAddOrganismDialog(final int x, final int y) {
        final String[] options = {Wolf.NAME, Sheep.NAME, Fox.NAME, Turtle.NAME, Antelope.NAME, CyberSheep.NAME, Grass.NAME, Dandelion.NAME, Guarana.NAME, Hogweed.NAME};
        final String choice = (String) JOptionPane.showInputDialog(this, "Choose organism: ", "Add organism", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (StringUtils.isBlank(choice)) return;

        try {
            Class<? extends Organism> chosenOrganism = null;
            switch (choice) {
                case Wolf.NAME:
                    chosenOrganism = Wolf.class;
                    break;
                case Sheep.NAME:
                    chosenOrganism = Sheep.class;
                    break;
                case Fox.NAME:
                    chosenOrganism = Fox.class;
                    break;
                case Turtle.NAME:
                    chosenOrganism = Turtle.class;
                    break;
                case Antelope.NAME:
                    chosenOrganism = Antelope.class;
                    break;
                case CyberSheep.NAME:
                    chosenOrganism = CyberSheep.class;
                    break;
                case Human.NAME:
                    chosenOrganism = Human.class;
                    break;
                case Grass.NAME:
                    chosenOrganism = Grass.class;
                    break;
                case Dandelion.NAME:
                    chosenOrganism = Dandelion.class;
                    break;
                case Guarana.NAME:
                    chosenOrganism = Guarana.class;
                    break;
                case Belladonna.NAME:
                    chosenOrganism = Belladonna.class;
                    break;
                case Hogweed.NAME:
                    chosenOrganism = Hogweed.class;
                    break;
                default:
                    break;
            }

            if (chosenOrganism != null) {
                Organism newOrg = chosenOrganism.getDeclaredConstructor(World.class, int.class, int.class).newInstance(world, x, y);
                if (newOrg != null) {
                    world.addOrganismImmediately(newOrg);
                    updateStats();
                    boardPanel.repaint();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
