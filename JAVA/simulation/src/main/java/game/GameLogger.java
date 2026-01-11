package game;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor
public class GameLogger implements Serializable {
    private List<String> logs = new ArrayList<>();
    private transient JTextArea logArea;

    public void log(final String message) {
        logs.add(message);
        if (logArea != null) {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }
        System.out.println(message);
    }
}
