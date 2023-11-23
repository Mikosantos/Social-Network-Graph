
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

public class SocialGraph {

    // from - to
    private ArrayList<ArrayList<Integer>> network;

    public SocialGraph() {
        network = new ArrayList<>();
    }

    public boolean loadGraph(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            String[] tokens = line.split(" ");
            int n = Integer.parseInt(tokens[0]);    //Parse the number of nodes (n)

            network = new ArrayList<>(n);   //Create the network with n nodes
            for (int i = 0; i < n; i++) {
                network.add(new ArrayList<>()); // Initialize with an empty ArrayList
            }

            int e = Integer.parseInt(tokens[1]);

            for (int i = 0; i < e; i++) {
                line = reader.readLine();
                tokens = line.split(" ");
                int a = Integer.parseInt(tokens[0]);
                int b = Integer.parseInt(tokens[1]);
                network.get(a).add(b);
                network.get(b).add(a);
            }
            System.out.println("Graph loaded!");
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<Integer> bubbleSort(ArrayList<Integer> friends) {
        int n = friends.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (friends.get(j) > friends.get(j + 1)) {
                    // Swap
                    int temp = friends.get(j);
                    friends.set(j, friends.get(j + 1));
                    friends.set(j + 1, temp);
                }
            }
        }
        return friends;
    }

    public void displayFriendList(int id) {
        if (id >= 0 && id < network.size()) {
            ArrayList<Integer> friends = network.get(id);   // Get the list of friends for the person with a given id
            System.out.println("Person " + id + " has " + friends.size() + " friends!");
            System.out.print("List of friends: ");
            friends = bubbleSort(friends); // Sort the list of friends using bubble sort
            for (int friend : friends) {
                System.out.print(friend + " ");
            }
            System.out.println();
        } else {
            System.out.println("Person " + id + " does not exist!");
        }
    }
    public void displayConnections(int id1, int id2) {
        if (id1 < 0 || id1 >= network.size() || id2 < 0 || id2 >= network.size()) {
            System.out.println("One or both persons do not exist!");
            return; // pre-terminate the method
        }
        // Searching for the connection between id1 and id2 through findConnection method
        ArrayList<Integer> path = findConnection(id1, id2);
        if (path.isEmpty()) {   //If no connection, print a prompt that indicates absence of connection
            System.out.println("Cannot find a connection between " + id1 + " and " + id2);
        } else {    //Otherwise, print a prompt that indicates presence of connection
            System.out.println("There is a connection from " + id1 + " to " + id2 + "!");
            int size = path.size();
            // Print the friendship connection between two nodes, current and next
            for (int i = 0; i < size - 1; i++) {
                int current = path.get(i);
                int next = path.get(i + 1);
                System.out.println(current + " is friends with " + next);
            }
        }
    }

    public ArrayList<Integer> findConnection(int id1, int id2) {
        ArrayList<Integer> path = null; //Initialize variable for final path
        LinkedList<Integer> linkedList = new LinkedList<>(); // Instantiate linked list to perform BFS
        LinkedList<Integer> visited = new LinkedList<>();    // Instantiate linked list to track visited nodes
        HashMap<Integer, Integer> parent = new HashMap<>();  // Instantiate hash map to keep parent-children relationship during traversal

        linkedList.add(id1); //id1 as starting node
        visited.add(id1);   // id1 marked as visited node

        //Breadth-first Search
        while (!linkedList.isEmpty()) {
            int current = linkedList.poll(); // Get the current node

            if (current == id2) { // If the current node is equal to target node, id2, create a path.
                path = new ArrayList<>();


                // Trace back from the target node(id2) to the start node (id1)
                while (current != id1) {
                    path.add(current);  // Add current node to path
                    current = parent.get(current);  //Move to the parent node
                }
                // Add the starting node (id1) to the path
                path.add(id1);

                // Reversing the path using LinkedList
                LinkedList<Integer> reversedPath = new LinkedList<>();
                for (int i = path.size() - 1; i >= 0; i--) {    // Iterate through ArrayList in reverse order
                    reversedPath.add(path.get(i));  //Add each element to reversedPath
                }
                path = new ArrayList<>(reversedPath);   // Convert reversedPath back to Arraylist
            }
            // Explore neighbors of the current node and add them to the linked list for further traversal
            for (int neighbor : network.get(current)) {
                if (!visited.contains(neighbor)) {
                    linkedList.add(neighbor);
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }
        return path;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SocialGraph socialGraph = new SocialGraph();
        String filePath;
        boolean isValidFile;

        do {
            System.out.print("Input file path: ");
            filePath = scanner.nextLine();
            isValidFile = socialGraph.loadGraph(filePath);
            if (!isValidFile)
                System.out.println("Invalid file path. Please try again.");
        } while (!isValidFile);

        while (true) {
            System.out.println("\nMAIN MENU");
            System.out.println("[1] Get friend list");
            System.out.println("[2] Get connection");
            System.out.println("[3] Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter ID of person: ");
                    int id = scanner.nextInt();
                    socialGraph.displayFriendList(id);
                }
                case 2 -> {
                    System.out.print("Enter ID of first person: ");
                    int id1 = scanner.nextInt();
                    System.out.print("Enter ID of second person: ");
                    int id2 = scanner.nextInt();
                    socialGraph.displayConnections(id1, id2);
                }
                case 3 -> System.exit(0);
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
