package market;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;

public class SimulationGUI extends JFrame {
    private Store store;
    private javax.swing.Timer timer;
    private int step;
    private JLabel steps;
    private Map<String, JLabel> productLabel = new HashMap<>();
    private Map<String, JPanel> queuePanel = new HashMap<>();
    private Random random = new Random();
    private Map<ProductType, Color> colors= new HashMap<>();

    public SimulationGUI(){
        colors.put(ProductType.FRUITS, new Color(220, 140, 130));
        colors.put(ProductType.BAKERY, new Color(140, 190, 140));
        colors.put(ProductType.MEAT, new Color(190, 100, 90));
        colors.put(ProductType.VEGETABLES, new Color(210, 185, 150));

        setTitle("Симуляция магазина");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 243, 236));

        store = new Store();
        makeStore();
        makeTopPanel();
        makeDepartmentPanel();

        timer = new javax.swing.Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startSteps();
            }
        });
        timer.start();
    }

    private void startSteps(){
        step++;
        store.doStep();
        steps.setText("ШАГ " + step);

        int count = random.nextInt(10); //рандомное кол-во покупателей

        for (int i = 0; i < count; i++) {
            Customer customer = new Customer(random);
            store.addCustomer(customer);
        }

        productStep();
        queueStep();
    }

    private JComponent createCircle(Customer customer){
        Color color = getColors(customer.getDesiredType());
        return new JComponent(){
            {
                setPreferredSize(new Dimension(35, 35));
            }
            protected void paintComponent(Graphics g){
                super.paintComponents(g);
                g.setColor(color);
                g.fillOval(3, 3, getWidth() - 6, getHeight() - 6);
                g.setColor(Color.BLACK);
                g.drawOval(3, 3, getWidth() - 6, getHeight() - 6);
            }
        };
    }

    private void productStep(){
        for (Department department : store.getDepartments().values()){
            for (Product product : department.getProducts().values()){
                String name = department.getType().name() + "_" + product.getName();
                JLabel label = productLabel.get(name);

                String text = product.getName() + " * " + product.getAmount();
                label.setText(text);

            }
        }
    }

    private void queueStep(){
        for (JPanel panel : queuePanel.values()){
            panel.removeAll();
        }

        for (Department department : store.getDepartments().values()){
            List<Cashier> cashiers = department.getCashiers();
            for (int i = 0; i < cashiers.size(); i++){
                Cashier cashier = cashiers.get(i);
                String name = department.getType().name() + "_cashier_" + i;
                JPanel queueP = queuePanel.get(name);

                for (Customer customer : cashier.getQueue()){
                    queueP.add(createCircle(customer));
                }
            }
        }
        for (JPanel panel : queuePanel.values()) {
            panel.revalidate();
            panel.repaint();
        }

    }

    private void makeTopPanel(){
        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setBackground(new Color(180, 160, 140));
        top.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("Магазин");
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        top.add(title, BorderLayout.WEST);

        steps = new JLabel("ШАГ 0", SwingConstants.CENTER);
        steps.setFont(new Font("Monospaced", Font.BOLD, 28));
        top.add(steps, BorderLayout.CENTER);

        JButton button = new JButton("СТОП");
        button.setFont(new Font("Monospaced", Font.BOLD, 28));
        button.setBackground(new Color(245, 233, 223));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5,15));
        top.add(button, BorderLayout.EAST);

        button.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                button.setText("▶ СТАРТ");
                steps.setText("ПАУЗА на шаге " + step);
                // Запускаем цепочку диалогов статистики
                showStatisticsDialog();
            } else {
                timer.start();
                button.setText("⏸ ПАУЗА");
                steps.setText("ШАГ " + step);
            }
        });

        add(top, BorderLayout.NORTH);
    }

    private void makeDepartmentPanel(){
        JPanel depPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        depPanel.setBackground(new Color(245, 233, 223));
        depPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,10));

        for (Department department : store.getDepartments().values()){
            depPanel.add(makeDepartmentBlock(department));
        }
        add(depPanel, BorderLayout.CENTER);
    }

    private JPanel makeDepartmentBlock(Department department){
        JPanel block = new JPanel(new BorderLayout());
        block.setBorder(BorderFactory.createLineBorder(new Color(180, 160, 140), 3));
        block.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel(department.getType().getProductType(), SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        title.setOpaque(true);
        title.setBackground(new Color(180, 160, 140));
        block.add(title, BorderLayout.NORTH);

        JPanel shelf = new JPanel();
        shelf.setLayout(new BoxLayout(shelf, BoxLayout.Y_AXIS));
        shelf.setBackground(new Color(245, 233, 223));
        shelf.setName("shelf_" + department.getType().name());
        shelf.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        for (Product product : department.getProducts().values()){
            JLabel thing = new JLabel(product.getName() + " * " + product.getAmount());
            thing.setFont(new Font("Monospaced", Font.BOLD, 28));
            thing.setAlignmentX(Component.LEFT_ALIGNMENT);
            thing.setForeground(new Color(180, 160, 140));

            String name = department.getType().name() + "_" + product.getName();
            productLabel.put(name, thing);

            shelf.add(thing);
            shelf.add(Box.createVerticalStrut(5));
        }
        block.add(shelf, BorderLayout.CENTER);

        JPanel cashiers = new JPanel(new GridLayout(department.getCashiers().size(), 1, 5, 5));
        cashiers.setBackground(new Color(245, 233, 223));
        cashiers.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        int i = 1;
        for (Cashier cashier : department.getCashiers()){
            JPanel cashierPanel = new JPanel();
            cashierPanel.setLayout(new BoxLayout(cashierPanel, BoxLayout.Y_AXIS));
            cashierPanel.setBackground(new Color(245, 233, 223));
            cashierPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 160, 140), 3));

            JLabel numb = new JLabel("КАССА "+ i);
            numb.setFont(new Font("Monospaced", Font.BOLD, 28));
            numb.setAlignmentX(Component.CENTER_ALIGNMENT);
            numb.setBackground(new Color(180, 160, 140));
            cashierPanel.add(numb);

            JPanel queue = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            queue.setBackground(new Color(245, 233, 223));
            queue.setPreferredSize(new Dimension(300, 50));
            queue.setName("queue_" + department.getType().name() + "_" + (i - 1));
            cashierPanel.add(queue);

            String name = department.getType().name() + "_cashier_" + (i - 1);
            queuePanel.put(name, queue);

            cashierPanel.add(queue);
            cashiers.add(cashierPanel);
            i++;
        }

        block.add(cashiers, BorderLayout.SOUTH);
        return block;
    }

    private Color getColors(ProductType type){
        return colors.get(type);
    }

    private void showStatisticsDialog() {
        String name = JOptionPane.showInputDialog(this, "Введите название товара для статистики по продажам:");
        if (name == null) return;
        name = name.toLowerCase();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        String type = JOptionPane.showInputDialog(this, "Введите название вида товара для статистики по продажам:");
        if (type == null) return;
        type = type.toLowerCase();
        type = type.substring(0, 1).toUpperCase() + type.substring(1);

        String stepStr = JOptionPane.showInputDialog(this, "Введите количество шагов для статистики скоропортящихся товаров:");
        if (stepStr == null) return;
        int step = 5;
        try { step = Integer.parseInt(stepStr.trim()); } catch (Exception e) { step = 5; }

        StringBuilder stat = new StringBuilder();
        stat.append("   \nСТАТИСТИКА\n");

        stat.append("ТОВАР '").append(name).append("'\n");
        Product product = store.findProduct(name);
        if (product != null){
            stat.append("   Продано товара: ").append(product.getSoldCount()).append(" шт.\n");
            stat.append("   Продано товара на сумму: ").append(product.getSoldIncome()).append("руб.\n");
            stat.append("   Товара осталось в количестве ").append(product.getAmount()).append(" шт.\n");
            stat.append("   Товара осталось на сумму ").append(product.getAmount() * product.getPrice()).append(" руб.\n");
        } else {
            stat.append("   Товар не найден.\n");
        }

        Map<String, ProductType> dict = new HashMap<>();
        dict.put("Фрукты", ProductType.FRUITS);
        dict.put("Овощи", ProductType.VEGETABLES);
        dict.put("Мясо", ProductType.MEAT);
        dict.put("Выпечка", ProductType.BAKERY);

        stat.append("   \nВИД ТОВАРА '").append(type).append("'\n");
        ProductType type1 = dict.get(type);
        if (type1 != null){
            Department department = store.getDepartmentByType(type1);
            int soldCount = 0, amount = 0;
            double soldIncome = 0.0, amountIncome = 0.0;

            for (Product p : department.getProducts().values()){
                soldCount += p.getSoldCount();
                soldIncome += p.getSoldIncome();
                amount += p.getAmount();
                amountIncome += p.getAmount() * p.getPrice();
            }
            stat.append("   Продано товаров: ").append(soldCount).append(" шт.\n");
            stat.append("   Продано товаров на сумму: ").append(soldIncome).append("руб.\n");
            stat.append("   Товаров осталось в количестве ").append(amount).append(" шт.\n");
            stat.append("   Товаров осталось на сумму ").append(amountIncome).append(" руб.\n");
        } else {
            stat.append("   Такого вида товара нет\n");
        }

        boolean found = false;
        stat.append("   \nСКОРОПОРТЯЩИЕСЯ ТОВАРЫ\n");
        for (Department dept : store.getDepartments().values()){
            for (Product p : dept.getProducts().values()){
                if (p instanceof ProductPerishable perish){
                    if (perish.getCountStepsExpire() <= step && perish.getAmount() > 0){
                        stat.append("Товар '").append(perish.getName()).append("': количество - ")
                                .append(perish.getAmount()).append(" шт., на сумму - ")
                                .append(perish.getAmount() * perish.getPrice()).append(" руб.\n");
                        found = true;
                    }
                }
            }
        }
        if (!found) stat.append("   Нет таких товаров.\n");

        boolean foundSeason = false;
        stat.append("   \nСЕЗОННЫЕ ТОВАРЫ\n");
        for (Department dept : store.getDepartments().values()){
            for (Product p : dept.getProducts().values()){
                if (p.getIsSeason() && Math.abs(p.getPrice() - p.getPriceBase()) > 0.01){
                    stat.append("   Товар '").append(p.getName()).append("' продаётся по нестандартной цене: ")
                            .append(p.getPrice()).append(" руб.\n");
                    foundSeason = true;
                }
            }
        }
        if (!foundSeason) stat.append("   Таких товаров нет.\n");

        stat.append("   \nСРЕДНИЕ  ЗНАЧЕНИЯ\n");
        int allSteps = store.getTime();
        int allServiced = 0;
        double allIncome = 0.0;
        for (Department dept : store.getDepartments().values()){
            for (Cashier c : dept.getCashiers()){
                allServiced += c.getServicedCount();
                allIncome += c.getIncome();
            }
        }
        double avgServ = allSteps > 0 ? (double)allServiced / allSteps : 0;
        double avgInc = allSteps > 0 ? allIncome / allSteps : 0;
        stat.append("   Среднее количество покупателей за шаг: ").append(avgServ).append(" чел.\n");
        stat.append("   Средняя прибыль за шаг: ").append(avgInc).append(" руб.\n");

        stat.append("   \nНЕОБСЛУЖАННЫЕ КЛИЕНТЫ\n");
        stat.append("   Всего необслужанных покупателей: ").append(store.getAllNotWaiting()).append(" чел.\n");

        stat.append("   \nПРИБЫЛЬ МАГАЗИНА\n");
        double totalStore = 0;
        int numCashier = 1;
        for (Department dept : store.getDepartments().values()){
            stat.append("   Отдел '").append(dept.getType().getProductType()).append("': \n");
            double deptInc = 0;
            for (Cashier c : dept.getCashiers()){
                double cInc = c.getIncome();
                stat.append("   Прибыль кассы ").append(numCashier++).append(": ").append(cInc).append(" руб.\n");
                deptInc += cInc;
            }
            stat.append("   Общая прибыль в отделе: ").append(deptInc).append(" руб.\n\n");
            totalStore += deptInc;
        }
        stat.append("   ОБЩАЯ ПРИБЫЛЬ МАГАЗИНА: ").append(totalStore).append(" руб.");

        JTextArea textArea = new JTextArea(stat.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 450));

        JOptionPane.showMessageDialog(this, scroll, "СТАТИСТИКА", JOptionPane.INFORMATION_MESSAGE);
    }

    private void makeStore(){
        Department depFr = new Department(ProductType.FRUITS);
        Department depVg = new Department(ProductType.VEGETABLES);
        Department depBk = new Department(ProductType.BAKERY);
        Department depMt = new Department(ProductType.MEAT);

        depFr.addProduct(new Product("Яблоко", ProductType.FRUITS, 50.0, 0.5, 40, 7, true));
        depFr.addProduct(new Product("Банан", ProductType.FRUITS, 60.0, 0.5, 35, 4, true));

        depVg.addProduct(new Product("Помидор", ProductType.VEGETABLES, 75.5, 0.0, 25, 5, false));
        depVg.addProduct(new Product("Лук", ProductType.VEGETABLES, 30.5, 0.0, 23, 8, false));
        depVg.addProduct(new ProductPerishable("Помидор", ProductType.VEGETABLES, 75.5, 0.2, 33, 5, false, 10));

        depBk.addProduct(new Product("Хлеб", ProductType.BAKERY, 40.5, 0.4, 32, 5, false));

        depMt.addProduct(new ProductPerishable("Курица", ProductType.MEAT, 400.5, 0.6, 44, 6, true, 10));
        depMt.addProduct(new ProductPerishable("Говядина", ProductType.MEAT, 600.5, 0.8, 38, 6, true, 10));

        depFr.addCashier(new Cashier());
        depFr.addCashier(new Cashier());
        depMt.addCashier(new Cashier());
        depMt.addCashier(new Cashier());
        depBk.addCashier(new Cashier());
        depBk.addCashier(new Cashier());
        depVg.addCashier(new Cashier());
        depVg.addCashier(new Cashier());

        store.addDepartment(depFr, ProductType.FRUITS);
        store.addDepartment(depVg, ProductType.VEGETABLES);
        store.addDepartment(depBk, ProductType.BAKERY);
        store.addDepartment(depMt, ProductType.MEAT);
    }


}