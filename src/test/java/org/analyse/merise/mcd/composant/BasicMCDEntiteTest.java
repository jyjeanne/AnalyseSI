package org.analyse.merise.mcd.composant;

import org.analyse.merise.gui.table.DictionnaireTable;

/**
 * Very basic test for MCDEntite that avoids GUI-related operations
 */
public class BasicMCDEntiteTest {

    public static void main(String[] args) {
        System.out.println("Running basic MCDEntite tests...");

        try {
            testBasicConstructor();
            testBasicInformationOperations();
            testNameOperations();

            System.out.println("All basic tests passed!");
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

    private static void testBasicInformationOperations() {
        System.out.print("Testing basic information operations... ");

        java.util.List<String> columns = new java.util.ArrayList<>();
        columns.add("Type");
        columns.add("Nom");
        columns.add("Taille");
        DictionnaireTable mockData = new DictionnaireTable(columns);
        MCDComponent mockMCD = new MCDComponent(mockData);
        MCDEntite entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);

        // Test initial state
        assert entite.sizeInformation() == 0 : "Should start with no informations";

        // Test adding single information (avoid multiple adds to prevent GUI operations)
        String info = "id:INTEGER";
        entite.addInformation(info);
        assert entite.sizeInformation() == 1 : "Should have 1 information";
        assert info.equals(entite.getCodeInformation(0)) : "Information should match";

        // Test clearing informations
        entite.clearInformations();
        assert entite.sizeInformation() == 0 : "Should have no informations after clear";

        System.out.println("PASSED");
    }

    private static void testNameOperations() {
        System.out.print("Testing name operations... ");

        java.util.List<String> columns = new java.util.ArrayList<>();
        columns.add("Type");
        columns.add("Nom");
        columns.add("Taille");
        DictionnaireTable mockData = new DictionnaireTable(columns);
        MCDComponent mockMCD = new MCDComponent(mockData);
        MCDEntite entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);

        // Test name operations
        assert "TestEntity".equals(entite.getName()) : "Initial name should be 'TestEntity'";
        entite.setName("UpdatedEntity");
        assert "UpdatedEntity".equals(entite.getName()) : "Updated name should be 'UpdatedEntity'";

        // Test position
        assert entite.getX() == 100 : "X should be 100";
        assert entite.getY() == 100 : "Y should be 100";

        // Test that MCD reference is maintained
        assert mockMCD.equals(entite.getMCD()) : "MCD reference should be maintained";

        System.out.println("PASSED");
    }
}