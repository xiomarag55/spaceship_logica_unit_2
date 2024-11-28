package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MapReduceHealthMetrics {

    //Function to read JSON from a file
    public static JSONArray readJsonFromFile(String filePath) throws IOException {
        FileReader reader = new FileReader(filePath);
        StringBuilder stringBuilder = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            stringBuilder.append((char) ch);
        }
        return new JSONArray(stringBuilder.toString());
    }

    // Map function: Extracting health metrics
    public static List<Map.Entry<String, Integer>> mapFunction(JSONObject person, int personId) {
        List<Map.Entry<String, Integer>> mappedValues = new ArrayList<>();

        //We extract health metrics
        JSONObject healthMetrics = person.getJSONObject("healthMetrics");
        int heartRate = healthMetrics.getInt("heartRate");
        int bloodPressure = healthMetrics.getInt("bloodPressure");
        int oxygenLevel = healthMetrics.getInt("oxygenLevel");
        int nivelSalud = person.getInt("nivelSalud");

        // Mapping of health metrics to key metrics
        mappedValues.add(new AbstractMap.SimpleEntry<>("heartRate", heartRate));
        mappedValues.add(new AbstractMap.SimpleEntry<>("bloodPressure", bloodPressure));
        mappedValues.add(new AbstractMap.SimpleEntry<>("oxygenLevel", oxygenLevel));
        mappedValues.add(new AbstractMap.SimpleEntry<>("nivelSalud", nivelSalud));

        return mappedValues;
    }

    //Combiner function: Combines mapped results
    public static Map<String, List<Integer>> combinerFunction(List<Map.Entry<String, Integer>> mappedData) {
        Map<String, List<Integer>> combined = new HashMap<>();
        for (Map.Entry<String, Integer> entry : mappedData) {
            combined.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(entry.getValue());
        }
        return combined;
    }

    // Reduce function: Calculates the average of each metric
    public static Map<String, Double> reduceFunction(Map<String, List<Integer>> combinedData) {
        Map<String, Double> summary = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : combinedData.entrySet()) {
            double avg = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0);
            summary.put(entry.getKey(), avg);
        }
        return summary;
    }

    //Worker class for mapping (execution of the Map task)
    static class MapWorker implements Runnable {
        private final BlockingQueue<Map.Entry<String, Integer>> mapQueue;
        private final BlockingQueue<Map<String, List<Integer>>> reduceQueue;
        private final AtomicInteger activeMapWorkers;

        public MapWorker(BlockingQueue<Map.Entry<String, Integer>> mapQueue, BlockingQueue<Map<String, List<Integer>>> reduceQueue, AtomicInteger activeMapWorkers) {
            this.mapQueue = mapQueue;
            this.reduceQueue = reduceQueue;
            this.activeMapWorkers = activeMapWorkers;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Map.Entry<String, Integer> task = mapQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task == null) {
                        if (activeMapWorkers.decrementAndGet() == 0) {
                            reduceQueue.put(new HashMap<>());
                        }
                        break;
                    }
                    List<Map.Entry<String, Integer>> mappedData = mapFunction(new JSONObject(task.getKey()), task.getValue());
                    Map<String, List<Integer>> combinedData = combinerFunction(mappedData);
                    reduceQueue.put(combinedData);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //Worker class for reduction (execution of the Reduce task)
    static class ReduceWorker implements Runnable {
        private final BlockingQueue<Map<String, List<Integer>>> reduceQueue;
        private final BlockingQueue<Map<String, Double>> resultQueue;

        public ReduceWorker(BlockingQueue<Map<String, List<Integer>>> reduceQueue, BlockingQueue<Map<String, Double>> resultQueue) {
            this.reduceQueue = reduceQueue;
            this.resultQueue = resultQueue;
        }

        @Override
        public void run() {
            try {
                Map<String, List<Integer>> combinedData = new HashMap<>();
                while (true) {
                    Map<String, List<Integer>> task = reduceQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task == null || task.isEmpty()) {
                        break;
                    }
                    for (Map.Entry<String, List<Integer>> entry : task.entrySet()) {
                        combinedData.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
                    }
                }
                Map<String, Double> reducedData = reduceFunction(combinedData);
                resultQueue.put(reducedData);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //  Master controller that manages the MapReduce process
    public static Map<String, Double> masterController(JSONArray logs, int numMapTasks, int numReduceTasks) throws InterruptedException {
        BlockingQueue<Map.Entry<String, Integer>> mapQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Map<String, List<Integer>>> reduceQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Map<String, Double>> resultQueue = new LinkedBlockingQueue<>();
        AtomicInteger activeMapWorkers = new AtomicInteger(numMapTasks);

        //Start Map workers
        ExecutorService mapExecutor = Executors.newFixedThreadPool(numMapTasks);
        for (int i = 0; i < numMapTasks; i++) {
            mapExecutor.submit(new MapWorker(mapQueue, reduceQueue, activeMapWorkers));
        }

        // Start Reduce workers
        ExecutorService reduceExecutor = Executors.newFixedThreadPool(numReduceTasks);
        for (int i = 0; i < numReduceTasks; i++) {
            reduceExecutor.submit(new ReduceWorker(reduceQueue, resultQueue));
        }

        //Assign mapping tasks
        for (int i = 0; i < logs.length(); i++) {
            JSONObject person = logs.getJSONObject(i);
            mapQueue.put(new AbstractMap.SimpleEntry<>(person.toString(), i));
        }

        //Mark the end of Map processing
        mapExecutor.shutdown();
        mapExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        //Waiting for the results of the reduction
        reduceExecutor.shutdown();
        reduceExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        return resultQueue.take();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String filePath = "src/main/java/org/example/utils/pasajerosSalud.json"; // Ruta del archivo JSON con los datos
        JSONArray jsonData = readJsonFromFile(filePath);
        Map<String, Double> result = masterController(jsonData, 2, 1);  // Número de workers Map y Reduce

        //Show result
        System.out.println("Resumen de métricas de salud: " + result);
    }
}
