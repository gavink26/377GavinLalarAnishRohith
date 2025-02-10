import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import org.jfree.data.xy.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

class PlayerGameStats {
    double points, rebounds, assists, steals, blocks, turnovers, fgMade, fgAttempted, threeMade, threeAttempted, ftMade, ftAttempted, plusMinus, gameScore;

    public PlayerGameStats(double points, double rebounds, double assists, double steals, double blocks, double turnovers,
                           double fgMade, double fgAttempted, double threeMade, double threeAttempted,
                           double ftMade, double ftAttempted, double plusMinus, double gameScore) {
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
        this.turnovers = turnovers;
        this.fgMade = fgMade;
        this.fgAttempted = fgAttempted;
        this.threeMade = threeMade;
        this.threeAttempted = threeAttempted;
        this.ftMade = ftMade;
        this.ftAttempted = ftAttempted;
        this.plusMinus = plusMinus;
        this.gameScore = gameScore;
    }
}

public class LeBronStats {
    public static void main(String[] args) {
        ArrayList<PlayerGameStats> lebronStats = readCSV("src/lebron_career.csv");

       
        Map<String, Double> averages = calculateAverages(lebronStats);
        printCareerAverages(averages);

     
        showBarChart(averages);
        showLineChart(lebronStats);
    }

    public static ArrayList<PlayerGameStats> readCSV(String filename) {
        ArrayList<PlayerGameStats> statsList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length < 26) continue; 

                try {
                    double fgMade = Double.parseDouble(values[7].trim());
                    double fgAttempted = Double.parseDouble(values[8].trim());
                    double threeMade = Double.parseDouble(values[10].trim());
                    double threeAttempted = Double.parseDouble(values[11].trim());
                    double ftMade = Double.parseDouble(values[13].trim());
                    double ftAttempted = Double.parseDouble(values[14].trim());
                    double rebounds = Double.parseDouble(values[18].trim());
                    double assists = Double.parseDouble(values[19].trim());
                    double steals = Double.parseDouble(values[20].trim());
                    double blocks = Double.parseDouble(values[21].trim());
                    double turnovers = Double.parseDouble(values[22].trim());
                    double points = Double.parseDouble(values[23].trim());
                    double gameScore = Double.parseDouble(values[24].trim());
                    double plusMinus = Double.parseDouble(values[25].trim());

                    statsList.add(new PlayerGameStats(points, rebounds, assists, steals, blocks, turnovers,
                                                      fgMade, fgAttempted, threeMade, threeAttempted,
                                                      ftMade, ftAttempted, plusMinus, gameScore));
                } catch (NumberFormatException e) {
                    System.out.println("skipping row number " + Arrays.toString(values));
                }
            }
        } catch (IOException e) {
            System.err.println("error reading " + filename);
        }
        return statsList;
    }

    public static Map<String, Double> calculateAverages(ArrayList<PlayerGameStats> stats) {
        double totalPoints = 0, totalRebounds = 0, totalAssists = 0, totalSteals = 0, totalBlocks = 0;
        double totalTurnovers = 0, totalFGMade = 0, totalFGAttempted = 0, totalThreeMade = 0, totalThreeAttempted = 0;
        double totalFTMade = 0, totalFTAttempted = 0, totalPlusMinus = 0, totalGameScore = 0;
        int games = stats.size();

        for (PlayerGameStats stat : stats) {
            totalPoints += stat.points;
            totalRebounds += stat.rebounds;
            totalAssists += stat.assists;
            totalSteals += stat.steals;
            totalBlocks += stat.blocks;
            totalTurnovers += stat.turnovers;
            totalFGMade += stat.fgMade;
            totalFGAttempted += stat.fgAttempted;
            totalThreeMade += stat.threeMade;
            totalThreeAttempted += stat.threeAttempted;
            totalFTMade += stat.ftMade;
            totalFTAttempted += stat.ftAttempted;
            totalPlusMinus += stat.plusMinus;
            totalGameScore += stat.gameScore;
        }

        Map<String, Double> averages = new HashMap<>();
        averages.put("Points per Game", totalPoints / games);
        averages.put("Rebounds per Game", totalRebounds / games);
        averages.put("Assists per Game", totalAssists / games);
        averages.put("Steals per Game", totalSteals / games);
        averages.put("Blocks per Game", totalBlocks / games);
        averages.put("Turnovers per Game", totalTurnovers / games);
        averages.put("Field Goal %", (totalFGMade / totalFGAttempted) * 100);
        averages.put("3PT %", (totalThreeMade / totalThreeAttempted) * 100);
        averages.put("Free Throw %", (totalFTMade / totalFTAttempted) * 100);
        averages.put("Plus/Minus", totalPlusMinus / games);
        averages.put("Game Score", totalGameScore / games);

        return averages;
    }

    public static void printCareerAverages(Map<String, Double> averages) {
        System.out.println("\n LeBron James Career Averages");
        for (Map.Entry<String, Double> entry : averages.entrySet()) {
            System.out.printf("%-20s: %.2f\n", entry.getKey(), entry.getValue());
        }
    }

    public static void showBarChart(Map<String, Double> averages) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : averages.entrySet()) {
            dataset.addValue(entry.getValue(), "LeBron James", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "LeBron James Career Averages",
                "Category",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        JFrame frame = new JFrame("Bar Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(barChart));
        frame.pack();
        frame.setVisible(true);
    }

    public static void showLineChart(ArrayList<PlayerGameStats> stats) {
        XYSeries series = new XYSeries("Field Goal % over Games");
        for (int i = 0; i < stats.size(); i++) {
            double fgPercent = (stats.get(i).fgMade / stats.get(i).fgAttempted) * 100;
            series.add(i + 1, fgPercent);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                "LeBron FG% Over Time",
                "Game Number",
                "FG%",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        JFrame frame = new JFrame("Line Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(lineChart));
        frame.pack();
        frame.setVisible(true);
    }
}
