import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class Main {
    public static void main(String[] args) {

        //色定義
        Color bgColor      = new Color(245, 245, 247); // 画面背景（薄いグレー）
        Color panelColor   = new Color(255, 255, 255); // テキストエリア背景（白）
        Color bottomColor  = new Color(238, 238, 240); // 下部パネル背景
        Color textColor    = new Color(40,  40,  40 ); // 文字色（ほぼ黒）
        Color mutedColor   = new Color(160, 160, 165); // 文字数ラベルの色
        Color btnBg        = new Color(90,  140, 255); // ボタン背景（青）
        Color btnText      = Color.WHITE;               // ボタン文字

        //JFrameの設定
        JFrame frame = new JFrame("メモ帳");
        frame.setSize(600, 460);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(bgColor);
        frame.setLayout(new BorderLayout(12, 12));

        //JTextAreaの設定
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Yu Gothic", Font.PLAIN, 15));
        textArea.setForeground(textColor);
        textArea.setBackground(panelColor);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setCaretColor(new Color(90, 140, 255));
        textArea.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14)); // 内側の余白

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 224), 1));
        scrollPane.setBackground(panelColor);

        // パネル
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(bottomColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14)); // 上下左右の余白

        // 文字数ラベル
        JLabel countLabel = new JLabel("0 文字");
        countLabel.setFont(new Font("Yu Gothic", Font.PLAIN, 13));
        countLabel.setForeground(mutedColor);

        // クリアボタン（角丸風）
        JButton clearBtn = new JButton("クリア") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 角丸
                g2.dispose();
                super.paintComponent(g);
            }
        };
        clearBtn.setFont(new Font("Yu Gothic", Font.BOLD, 13));
        clearBtn.setForeground(btnText);
        clearBtn.setBackground(btnBg);
        clearBtn.setFocusPainted(false);
        clearBtn.setBorderPainted(false);
        clearBtn.setContentAreaFilled(false); // 自前で描画するため無効化
        clearBtn.setOpaque(false);
        clearBtn.setPreferredSize(new Dimension(90, 34));
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // カーソルを手の形に

        bottomPanel.add(countLabel, BorderLayout.WEST);
        bottomPanel.add(clearBtn,   BorderLayout.EAST);

        // 文字数の読み取り
        clearBtn.addActionListener(e -> {
            textArea.setText("");
            countLabel.setText("0 文字");
        });

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { update(); }
            public void removeUpdate(DocumentEvent e)  { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
            void update() {
                countLabel.setText(textArea.getText().length() + " 文字");
            }
        });

        // 表示
        frame.add(scrollPane,   BorderLayout.CENTER);
        frame.add(bottomPanel,  BorderLayout.SOUTH);


        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e){
                try {
                    java.io.FileWriter fw=new java.io.FileWriter("memo.txt");
                    fw.write(textArea.getText());
                    fw.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        try {
            java.io.BufferedReader br=new java.io.BufferedReader(new java.io.FileReader("memo.txt"));
            StringBuilder sb=new StringBuilder();
            String line;
            while((line = br.readLine())!=null){
                sb.append(line).append("\n");

            }
            br.close();
            textArea.setText(sb.toString());

        } catch (Exception ex) {
        }

        frame.setVisible(true);
    }
}