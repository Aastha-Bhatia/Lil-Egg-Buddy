import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;

public class EggTimer {
    public static void main(String[] args) {
        new EggTimer().createHomePage();
    }

    JFrame frame;
    JPanel homePanel, timerPanel;
    Timer countdownTimer;
    Clip backgroundMusic;
    Clip alarmClip;
    boolean isMuted = false;
    JButton muteButton;
    Timer alarmTimer;
    boolean isMusicPlaying = false;

    public void createHomePage() {
        if (frame == null) {
            frame = new JFrame("Lil Egg Buddy :)");
            frame.setResizable(false);
            frame.setSize(400, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
        } else {
            frame.getContentPane().removeAll();
        }

        if (!isMusicPlaying) {
            playLoopMusic("/resources/calm.wav");
        }

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(245, 245, 220));

        ImageIcon eggIcon = loadImageIcon("/resources/egg.gif");
        JLabel eggLabel = new JLabel(eggIcon);
        eggLabel.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(eggLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Lil Egg Buddy ->");
        startButton.setPreferredSize(new Dimension(400, 45));
        startButton.setBackground(new Color(245, 245, 220));
        startButton.setFocusPainted(false);
        startButton.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        startButton.addActionListener(e -> {
            playSound("/resources/click.wav");
            showTimerSelectionPage();
        });

        muteButton = new JButton(isMuted ? "Unmute" : "Mute");
        muteButton.setFocusPainted(false);
        muteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        muteButton.setBackground(new Color(245, 245, 220));
        muteButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        muteButton.addActionListener(e -> toggleMute());

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(245, 245, 220));
        buttonPanel.add(startButton, BorderLayout.CENTER);
        buttonPanel.add(muteButton, BorderLayout.EAST);

        background.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(background);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private ImageIcon loadImageIcon(String path) {
        try {
            return new ImageIcon(getClass().getResource(path));
        } catch (Exception e) {
            System.out.println("Error loading image: " + path);
            return new ImageIcon();
        }
    }

    public void showTimerSelectionPage() {
        frame.getContentPane().removeAll();
        
        timerPanel = new JPanel(new BorderLayout());
        timerPanel.setBackground(new Color(245, 245, 220));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 220));
        
        JButton back = new JButton("<-");
        back.setFocusPainted(false);
        back.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        back.setBackground(new Color(245, 245, 220));
        back.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        back.addActionListener(e -> {
            playSound("/resources/click.wav");
            createHomePage();
        });
        
        JLabel title = new JLabel("Choose your egg type...", SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        
        muteButton = new JButton(isMuted ? "Unmute" : "Mute");
        muteButton.setFocusPainted(false);
        muteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        muteButton.setBackground(new Color(245, 245, 220));
        muteButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        muteButton.addActionListener(e -> toggleMute());
        
        topPanel.add(back, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(muteButton, BorderLayout.EAST);
        
        timerPanel.add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        gridPanel.setBackground(new Color(245, 245, 220));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton egg6 = createImageButton("/resources/images/soft_boiled.jpg");
        JButton egg9 = createImageButton("/resources/images/firm_boiled.jpg");
        JButton egg12 = createImageButton("/resources/images/medium_boiled.jpg");
        JButton egg15 = createImageButton("/resources/images/hard_boiled.jpg");

        egg6.addActionListener(e -> {
            playSound("/resources/click.wav");
            showBoilingPage(6);
        });
        egg9.addActionListener(e -> {
            playSound("/resources/click.wav");
            showBoilingPage(9);
        });
        egg12.addActionListener(e -> {
            playSound("/resources/click.wav");
            showBoilingPage(12);
        });
        egg15.addActionListener(e -> {
            playSound("/resources/click.wav");
            showBoilingPage(15);
        });

        gridPanel.add(egg6);
        gridPanel.add(egg9);
        gridPanel.add(egg12);
        gridPanel.add(egg15);

        timerPanel.add(gridPanel, BorderLayout.CENTER);

        JButton customButton = new JButton("Add Custom Time");
        customButton.setPreferredSize(new Dimension(400, 45));
        customButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        customButton.setBackground(new Color(245, 245, 220));
        customButton.setFocusPainted(false);
        customButton.addActionListener(e -> {
            playSound("/resources/click.wav");
            String input = JOptionPane.showInputDialog(frame, "Enter time in minutes:");
            if (input != null && !input.isEmpty()) {
                try {
                    int mins = Integer.parseInt(input);
                    showBoilingPage(mins);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
                }
            }
        });

        timerPanel.add(customButton, BorderLayout.SOUTH);

        frame.setContentPane(timerPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void showBoilingPage(int minutes) {
        frame.getContentPane().removeAll();
        
        JPanel boilingPanel = new JPanel(new BorderLayout());
        boilingPanel.setBackground(new Color(245, 245, 220));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 220));
        
        JButton backButton = new JButton("<-");
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        backButton.setBackground(new Color(245, 245, 220));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.addActionListener(e -> {
            if (countdownTimer != null) countdownTimer.stop();
            playSound("/resources/click.wav");
            showTimerSelectionPage();
        });
        
        JLabel boilingLabel = new JLabel("Boiling your egg...", SwingConstants.CENTER);
        boilingLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        
        muteButton = new JButton(isMuted ? "Unmute" : "Mute");
        muteButton.setFocusPainted(false);
        muteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        muteButton.setBackground(new Color(245, 245, 220));
        muteButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        muteButton.addActionListener(e -> toggleMute());
        
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(boilingLabel, BorderLayout.CENTER);
        topPanel.add(muteButton, BorderLayout.EAST);
        
        boilingPanel.add(topPanel, BorderLayout.NORTH);

        ImageIcon gifIcon = loadImageIcon("/resources/boiling.gif");
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        boilingPanel.add(gifLabel, BorderLayout.CENTER);

        JLabel timerLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        boilingPanel.add(timerLabel, BorderLayout.SOUTH);

        frame.setContentPane(boilingPanel);
        frame.revalidate();
        frame.repaint();

        int totalSeconds = minutes * 60;
        countdownTimer = new Timer(1000, new TimerAction(totalSeconds, timerLabel));
        countdownTimer.start();
    }

    private JButton createImageButton(String imagePath) {
        try {
            ImageIcon originalIcon = loadImageIcon(imagePath);
            int width = 180;
            int height = 180;
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            
            JButton button = new JButton(scaledIcon);
            button.setPreferredSize(new Dimension(width, height));
            button.setBorder(BorderFactory.createLineBorder(new Color(210, 180, 140), 3));
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            
            return button;
        } catch (Exception e) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(180, 180));
            button.setBorder(BorderFactory.createLineBorder(new Color(210, 180, 140), 3));
            return button;
        }
    }

    public void showPopupAfterTimer() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 200));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel message = new JLabel("<html><center><font size='5' color='#8B4513'>Your egg is ready!</font></center></html>", SwingConstants.CENTER);
        
        JButton snoozeButton = new JButton("Snooze");
        snoozeButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        snoozeButton.setBackground(new Color(255, 218, 185));
        snoozeButton.setFocusPainted(false);
        snoozeButton.addActionListener(e -> {
            stopAlarm();
            String input = JOptionPane.showInputDialog(frame, "Enter snooze time in minutes:");
            if (input != null && !input.isEmpty()) {
                try {
                    int mins = Integer.parseInt(input);
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof JDialog) {
                            window.dispose();
                        }
                    }
                    showBoilingPage(mins);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
                }
            }
        });
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        okButton.setBackground(new Color(255, 218, 185));
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> {
            stopAlarm();
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog) {
                    window.dispose();
                }
            }
            createHomePage();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 255, 200));
        buttonPanel.add(snoozeButton);
        buttonPanel.add(okButton);
        
        panel.add(message, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JDialog dialog = new JDialog(frame, "Time's Up!", true);
        dialog.setContentPane(panel);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void startAlarm() {
        try {
            if (alarmClip != null) {
                alarmClip.stop();
                alarmClip.close();
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/resources/alarm.wav"));
            alarmClip = AudioSystem.getClip();
            alarmClip.open(audioIn);
            
            alarmTimer = new Timer(3000, e -> {
                if (alarmClip != null) {
                    alarmClip.setFramePosition(0);
                    alarmClip.start();
                }
            });
            alarmTimer.start();
            alarmClip.start();
        } catch (Exception e) {
            System.out.println("Error playing alarm sound: " + e.getMessage());
        }
    }

    private void stopAlarm() {
        if (alarmTimer != null) {
            alarmTimer.stop();
        }
        if (alarmClip != null) {
            alarmClip.stop();
            alarmClip.close();
        }
        stopMusic();
        playLoopMusic("/resources/calm.wav");
    }

    public void playSound(String soundFileName) {
        if (isMuted && !soundFileName.equals("/resources/alarm.wav")) return;
        
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(soundFileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    public void playLoopMusic(String soundFileName) {
        if (isMuted) return;
        
        try {
            if (backgroundMusic != null && backgroundMusic.isRunning()) {
                backgroundMusic.stop();
                backgroundMusic.close();
            }
            
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(soundFileName));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            isMusicPlaying = true;
        } catch (Exception e) {
            System.out.println("Error playing music: " + e.getMessage());
            isMusicPlaying = false;
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null) {
            if (backgroundMusic.isRunning()) {
                backgroundMusic.stop();
            }
            backgroundMusic.close();
            backgroundMusic = null;
            isMusicPlaying = false;
        }
    }

    private void toggleMute() {
        isMuted = !isMuted;
        muteButton.setText(isMuted ? "Unmute" : "Mute");
        
        if (isMuted) {
            stopMusic();
        } else {
            playLoopMusic("/resources/calm.wav");
        }
    }

    private class TimerAction implements ActionListener {
        private int timeLeft;
        private JLabel timerLabel;
        
        public TimerAction(int totalSeconds, JLabel timerLabel) {
            this.timeLeft = totalSeconds;
            this.timerLabel = timerLabel;
        }
        
        public void actionPerformed(ActionEvent e) {
            int mins = timeLeft / 60;
            int secs = timeLeft % 60;
            timerLabel.setText(String.format("Time left: %02d:%02d", mins, secs));
            timeLeft--;
            
            if (timeLeft < 0) {
                countdownTimer.stop();
                stopMusic();
                startAlarm();
                showPopupAfterTimer();
            }
        }
    }
}