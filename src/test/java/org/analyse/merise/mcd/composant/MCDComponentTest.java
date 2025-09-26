package org.analyse.merise.mcd.composant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class MCDComponentTest {

    private MCDComponent mcdComponent;

    @BeforeEach
    void setUp() {
        mcdComponent = new MCDComponent();
    }

    @Nested
    @DisplayName("Component Initialization Tests")
    class ComponentInitializationTests {

        @Test
        @DisplayName("Should create MCD component")
        void shouldCreateMcdComponent() {
            assertNotNull(mcdComponent);
        }

        @Test
        @DisplayName("Should initialize with data dictionary")
        void shouldInitializeWithDataDictionary() {
            assertNotNull(mcdComponent.getData());
        }
    }

    @Nested
    @DisplayName("Entity Management Tests")
    class EntityManagementTests {

        @Test
        @DisplayName("Should add entity to MCD")
        void shouldAddEntityToMcd() {
            MCDEntite entite = new MCDEntite(mcdComponent, "TestEntity", 100, 100);

            mcdComponent.addElement(entite);

            assertTrue(mcdComponent.getSize() > 0);
        }

        @Test
        @DisplayName("Should remove entity from MCD")
        void shouldRemoveEntityFromMcd() {
            MCDEntite entite = new MCDEntite(mcdComponent, "TestEntity", 100, 100);
            mcdComponent.addElement(entite);

            mcdComponent.removeElement(entite);

            assertEquals(0, mcdComponent.getSize());
        }

        @Test
        @DisplayName("Should clear all elements")
        void shouldClearAllElements() {
            MCDEntite entite1 = new MCDEntite(mcdComponent, "Entity1", 100, 100);
            MCDEntite entite2 = new MCDEntite(mcdComponent, "Entity2", 200, 200);

            mcdComponent.addElement(entite1);
            mcdComponent.addElement(entite2);

            mcdComponent.clear();

            assertEquals(0, mcdComponent.getSize());
        }
    }

    @Nested
    @DisplayName("Association Management Tests")
    class AssociationManagementTests {

        @Test
        @DisplayName("Should add association to MCD")
        void shouldAddAssociationToMcd() {
            MCDAssociation association = new MCDAssociation(mcdComponent);

            mcdComponent.addElement(association);

            assertTrue(mcdComponent.getSize() > 0);
        }
    }

    @Nested
    @DisplayName("Component Properties Tests")
    class ComponentPropertiesTests {

        @Test
        @DisplayName("Should have correct component ID")
        void shouldHaveCorrectComponentId() {
            String id = mcdComponent.getID();
            assertNotNull(id);
            assertEquals(MCDComponent.MCD_ID, id);
        }

        @Test
        @DisplayName("Should have correct component title")
        void shouldHaveCorrectComponentTitle() {
            String title = mcdComponent.getTitle();
            assertNotNull(title);
            assertEquals(MCDComponent.MCD_TITLE, title);
        }
    }

    @Nested
    @DisplayName("Element Selection Tests")
    class ElementSelectionTests {

        @Test
        @DisplayName("Should select element at position")
        void shouldSelectElementAtPosition() {
            MCDEntite entite = new MCDEntite(mcdComponent, "TestEntity", 100, 100);
            entite.setWidth(100);
            entite.setHeight(50);
            mcdComponent.addElement(entite);

            Object selected = mcdComponent.getElement(150, 125);

            assertSame(entite, selected);
        }

        @Test
        @DisplayName("Should return null when no element at position")
        void shouldReturnNullWhenNoElementAtPosition() {
            Object selected = mcdComponent.getElement(500, 500);

            assertNull(selected);
        }
    }

    @Nested
    @DisplayName("Data Dictionary Tests")
    class DataDictionaryTests {

        @Test
        @DisplayName("Should have data dictionary")
        void shouldHaveDataDictionary() {
            assertNotNull(mcdComponent.getData());
        }

        @Test
        @DisplayName("Should access data dictionary properties")
        void shouldAccessDataDictionaryProperties() {
            Object data = mcdComponent.getData();
            assertNotNull(data);
        }
    }
}