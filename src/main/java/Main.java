import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Task {
    private String name;
    private String description;
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
}

class KanbanBoard extends JFrame {
    private List<Task> todoList;
    private List<Task> inProgressList;
    private List<Task> verificationList;
    private List<Task> doneList;

    private DefaultListModel<String> todoListModel;
    private DefaultListModel<String> inProgressListModel;
    private DefaultListModel<String> verificationListModel;
    private DefaultListModel<String> doneListModel;

    private JList<String> todoListUI;
    private JList<String> inProgressListUI;
    private JList<String> verificationListUI;
    private JList<String> doneListUI;

    private JButton moveInProgressButton;
    private JButton moveVerificationButton;
    private JButton moveDoneButton;
    private JButton deleteButton;

    public KanbanBoard() {
        super("Kanban Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 4));

        JLabel todoLabel = new JLabel("TODO");
        JLabel inProgressLabel = new JLabel("In Progress");
        JLabel verificationLabel = new JLabel("Verification");
        JLabel doneLabel = new JLabel("Done");

        Font customFont = new Font("Arial", Font.BOLD, 16); // Np. Arial, pogrubiona, rozmiar 16
        todoLabel.setFont(customFont);
        inProgressLabel.setFont(customFont);
        verificationLabel.setFont(customFont);
        doneLabel.setFont(customFont);

        todoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        inProgressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        verificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        doneLabel.setHorizontalAlignment(SwingConstants.CENTER);

        todoList = new ArrayList<>();
        inProgressList = new ArrayList<>();
        verificationList = new ArrayList<>();
        doneList = new ArrayList<>();

        todoListModel = new DefaultListModel<>();
        inProgressListModel = new DefaultListModel<>();
        verificationListModel = new DefaultListModel<>();
        doneListModel = new DefaultListModel<>();

        todoListUI = new JList<>(todoListModel);
        inProgressListUI = new JList<>(inProgressListModel);
        verificationListUI = new JList<>(verificationListModel);
        doneListUI = new JList<>(doneListModel);
        todoListUI.setBackground(new Color(255, 180, 180));
        inProgressListUI.setBackground(new Color(255, 204, 153));
        verificationListUI.setBackground(new Color(255, 255, 153));
        doneListUI.setBackground(new Color(204, 255, 153));


        moveInProgressButton = new JButton("Move to In Progress");
        moveVerificationButton = new JButton("Move to Verification");
        moveDoneButton = new JButton("Move to Done");
        deleteButton = new JButton("Delete Task");
        moveVerificationButton.setBackground(Color.orange);
        moveDoneButton.setBackground(Color.yellow);
        moveInProgressButton.setBackground(Color.red);
        deleteButton.setBackground(Color.green);
        moveInProgressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveTaskToInProgress();
            }
        });

        moveVerificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveTaskToVerification();
            }
        });

        moveDoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveTaskToDone();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        JPanel todoPanel = createPanelWithColor(new Color(153, 204, 255));
        JPanel inProgressPanel = createPanelWithColor(new Color(255, 255, 153));
        JPanel verificationPanel = createPanelWithColor(new Color(255, 204, 153));
        JPanel donePanel = createPanelWithColor(new Color(204, 255, 153));

        // Dodaj etykiety do paneli
        todoPanel.add(todoLabel, BorderLayout.NORTH);
        inProgressPanel.add(inProgressLabel, BorderLayout.NORTH);
        verificationPanel.add(verificationLabel, BorderLayout.NORTH);
        donePanel.add(doneLabel, BorderLayout.NORTH);

        JScrollPane panel = new JScrollPane(todoListUI);
        panel.setOpaque(true);
        panel.setBackground(Color.blue);
        todoPanel.add(panel);
        inProgressPanel.add(new JScrollPane(inProgressListUI));
        verificationPanel.add(new JScrollPane(verificationListUI));
        donePanel.add(new JScrollPane(doneListUI));

        todoPanel.add(moveInProgressButton, BorderLayout.SOUTH);
        inProgressPanel.add(moveVerificationButton, BorderLayout.SOUTH);
        verificationPanel.add(moveDoneButton, BorderLayout.SOUTH);
        donePanel.add(deleteButton, BorderLayout.SOUTH);

        add(todoPanel);
        add(inProgressPanel);
        add(verificationPanel);
        add(donePanel);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);

    }

    private JPanel createPanelWithColor(Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBackground(color);
        panel.setForeground(color);

        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }

    public void addTaskToTodoList(Task task) {
        todoList.add(task);
        todoListModel.addElement(task.getName());
    }

    private void moveTaskToInProgress() {
        int selectedIndex = todoListUI.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = todoList.get(selectedIndex);
            todoList.remove(selectedIndex);
            todoListModel.remove(selectedIndex);

            inProgressList.add(task);
            inProgressListModel.addElement(task.getName());
        }
    }

    private void moveTaskToVerification() {
        int selectedIndex = inProgressListUI.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = inProgressList.get(selectedIndex);
            inProgressList.remove(selectedIndex);
            inProgressListModel.remove(selectedIndex);

            verificationList.add(task);
            verificationListModel.addElement(task.getName());
        }
    }

    private void moveTaskToDone() {
        int selectedIndex = verificationListUI.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = verificationList.get(selectedIndex);
            verificationList.remove(selectedIndex);
            verificationListModel.remove(selectedIndex);

            doneList.add(task);
            doneListModel.addElement(task.getName());
        }
    }

    private void deleteTask() {
        int selectedIndex = doneListUI.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = doneList.get(selectedIndex);
            doneList.remove(selectedIndex);
            doneListModel.remove(selectedIndex);

            // Task usunięte
        }
    }
}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Tworzenie tablicy Kanban
                KanbanBoard kanbanBoard = new KanbanBoard();

                // Tworzenie zadań
                Task task1 = new Task("Zadanie 1", "Opis zadania 1");
                Task task2 = new Task("Zadanie 2", "Opis zadania 2");
                Task task3 = new Task("Zadanie 3", "Opis zadania 3");

                // Dodawanie zadań do listy "TODO"
                kanbanBoard.addTaskToTodoList(task1);
                kanbanBoard.addTaskToTodoList(task2);
                kanbanBoard.addTaskToTodoList(task3);
            }
        });
    }
}