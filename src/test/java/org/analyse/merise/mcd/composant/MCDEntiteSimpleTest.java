package org.analyse.merise.mcd.composant;

import org.analyse.merise.gui.table.DictionnaireTable;

/**
 * Simple test class for MCDEntite without external dependencies
 */
public class MCDEntiteSimpleTest {

    public static void main(String[] args) {
        System.out.println("Running MCDEntite simple tests...");

        try {
            testBasicConstructor();
            testNameAndPosition();
            System.out.println("All tests passed!");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testBasicConstructor() {
        System.out.print("Testing basic constructor... ");

        // Create mock MCD component
        java.util.List<String> columns = new java.util.ArrayList<>();
        columns.add("Type");
        columns.add("Nom");
        columns.add("Taille");
        DictionnaireTable mockData = new DictionnaireTable(columns);
        MCDComponent mockMCD = new MCDComponent(mockData);

        // Test default constructor
        MCDEntite entite = new MCDEntite(mockMCD);
        assert entite != null : "Entity should not be null";
        assert entite.getName().startsWith("Entite") : "Entity name should start with 'Entite'";
        assert entite.getX() >= 0 : "X coordinate should be non-negative";
        assert entite.getY() >= 0 : "Y coordinate should be non-negative";

        // Test constructor with position
        MCDEntite entite2 = new MCDEntite(mockMCD, 100, 200);
        assert entite2.getX() == 100 : "X coordinate should be 100";
        assert entite2.getY() == 200 : "Y coordinate should be 200";

        // Test constructor with name and position
        MCDEntite entite3 = new MCDEntite(mockMCD, "TestEntity", 150, 250);
        assert "TestEntity".equals(entite3.getName()) : "Entity name should be 'TestEntity'";
        assert entite3.getX() == 150 : "X coordinate should be 150";
        assert entite3.getY() == 250 : "Y coordinate should be 250";

        System.out.println("PASSED");
    }



    private static void testNameAndPosition() {
        System.out.print("Testing name and position... ");

        java.util.List<String> columns3 = new java.util.ArrayList<>();
        columns3.add("Type");
        columns3.add("Nom");
        columns3.add("Taille");
        DictionnaireTable mockData = new DictionnaireTable(columns3);
        MCDComponent mockMCD = new MCDComponent(mockData);
        MCDEntite entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);

        // Test name operations
        assert "TestEntity".equals(entite.getName()) : "Initial name should be 'TestEntity'";
        entite.setName("UpdatedEntity");
        assert "UpdatedEntity".equals(entite.getName()) : "Updated name should be 'UpdatedEntity'";

        // Test position
        assert entite.getX() == 100 : "X should be 100";
        assert entite.getY() == 100 : "Y should be 100";

        // Test dimensions
        int width = entite.getWidth();
        int height = entite.getHeight();
        assert width >= 0 : "Width should be non-negative";
        assert height >= 0 : "Height should be non-negative";

        System.out.println("PASSED");
    }
}