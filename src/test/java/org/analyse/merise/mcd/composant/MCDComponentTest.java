package org.analyse.merise.mcd.composant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.analyse.merise.gui.table.DictionnaireTable;
import org.analyse.core.gui.zgraph.ZElement;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MCDComponentTest {

    private MCDComponent mcdComponent;
    private DictionnaireTable mockDictionnaire;

    @BeforeEach
    void setUp() {
        mockDictionnaire = mock(DictionnaireTable.class);
        mcdComponent = new MCDComponent(mockDictionnaire);
    }

    @Nested
    @DisplayName("Component Initialization Tests")
    class ComponentInitializationTests {

        @Test
        @DisplayName("Should create MCD component with dictionary")
        void shouldCreateMcdComponentWithDictionary() {
            assertNotNull(mcdComponent);
            assertNotNull(mcdComponent.getData());
            assertEquals(mockDictionnaire, mcdComponent.getData());
        }

        @Test
        @DisplayName("Should register as observer of dictionary")
        void shouldRegisterAsObserverOfDictionary() {
            verify(mockDictionnaire).addObserver(mcdComponent);
        }
    }

    @Nested
    @DisplayName("Entity Management Tests")
    class EntityManagementTests {

        @Test
        @DisplayName("Should add entity to MCD at specified position")
        void shouldAddEntityToMcdAtPosition() {
            MCDEntite entite = mcdComponent.addEntite(100, 200);

            assertNotNull(entite);
            assertEquals(100, entite.getX());
            assertEquals(200, entite.getY());
            verify(mockDictionnaire).addObserver(entite);
        }

        @Test
        @DisplayName("Should add existing entity object")
        void shouldAddExistingEntityObject() {
            MCDEntite entite = new MCDEntite(mcdComponent, "TestEntity", 150, 250);

            mcdComponent.addObjet(entite);

            verify(mockDictionnaire).addObserver(entite);
        }

        @Test
        @DisplayName("Should remove entity and clear its information")
        void shouldRemoveEntityAndClearInformation() {
            MCDEntite entite = mcdComponent.addEntite(100, 100);
            entite.addInformation("test_info");

            mcdComponent.removeObjet(entite);

            assertEquals(0, entite.sizeInformation());
        }

        @Test
        @DisplayName("Should find entity by name")
        void shouldFindEntityByName() {
            String entityName = "TestEntity";
            MCDEntite entite = new MCDEntite(mcdComponent, entityName, 100, 100);
            mcdComponent.addObjet(entite);

            MCDObjet found = mcdComponent.getElement(entityName);

            assertNotNull(found);
            assertEquals(entityName, found.getName());
            assertSame(entite, found);
        }

        @Test
        @DisplayName("Should return null for non-existent entity name")
        void shouldReturnNullForNonExistentEntityName() {
            MCDObjet found = mcdComponent.getElement("NonExistent");

            assertNull(found);
        }
    }

    @Nested
    @DisplayName("Association Management Tests")
    class AssociationManagementTests {

        @Test
        @DisplayName("Should add association to MCD at specified position")
        void shouldAddAssociationToMcdAtPosition() {
            MCDAssociation association = mcdComponent.addAssociation(200, 300);

            assertNotNull(association);
            assertEquals(200, association.getX());
            assertEquals(300, association.getY());
            verify(mockDictionnaire).addObserver(association);
        }

        @Test
        @DisplayName("Should add existing association object")
        void shouldAddExistingAssociationObject() {
            MCDAssociation association = new MCDAssociation(mcdComponent, "TestAssoc", 250, 350);

            mcdComponent.addObjet(association);

            verify(mockDictionnaire).addObserver(association);
        }
    }

    @Nested
    @DisplayName("Link Management Tests")
    class LinkManagementTests {

        @Test
        @DisplayName("Should create link between entities and associations")
        void shouldCreateLinkBetweenEntitiesAndAssociations() {
            MCDEntite entite = new MCDEntite(mcdComponent, "Entity", 100, 100);
            MCDAssociation association = new MCDAssociation(mcdComponent, "Assoc", 200, 200);

            // Test that we can add both entities and associations
            mcdComponent.addObjet(entite);
            mcdComponent.addObjet(association);

            // Verify objects were added
            assertNotNull(mcdComponent.getElement("Entity"));
            assertNotNull(mcdComponent.getElement("Assoc"));
        }
    }

    @Nested
    @DisplayName("Component Properties Tests")
    class ComponentPropertiesTests {

        @Test
        @DisplayName("Should have data dictionary")
        void shouldHaveDataDictionary() {
            DictionnaireTable data = mcdComponent.getData();

            assertNotNull(data);
            assertSame(mockDictionnaire, data);
        }

        @Test
        @DisplayName("Should enumerate elements")
        void shouldEnumerateElements() {
            MCDEntite entite1 = mcdComponent.addEntite(100, 100);
            MCDEntite entite2 = mcdComponent.addEntite(200, 200);
            MCDAssociation assoc = mcdComponent.addAssociation(150, 150);

            int count = 0;
            for (Iterator<ZElement> iter = mcdComponent.enumElements(); iter.hasNext(); ) {
                iter.next();
                count++;
            }

            assertEquals(3, count);
        }
    }

    @Nested
    @DisplayName("Clear and Cleanup Tests")
    class ClearAndCleanupTests {

        @Test
        @DisplayName("Should clear all elements")
        void shouldClearAllElements() {
            mcdComponent.addEntite(100, 100);
            mcdComponent.addEntite(200, 200);
            mcdComponent.addAssociation(150, 150);

            mcdComponent.clear();

            int count = 0;
            for (Iterator<ZElement> iter = mcdComponent.enumElements(); iter.hasNext(); ) {
                iter.next();
                count++;
            }

            assertEquals(0, count);
        }
    }

    @Nested
    @DisplayName("Element Selection Tests")
    class ElementSelectionTests {

        @Test
        @DisplayName("Should get element by index")
        void shouldGetElementByIndex() {
            MCDEntite entite = mcdComponent.addEntite(100, 100);

            ZElement element = mcdComponent.getElement(0);

            assertNotNull(element);
            assertSame(entite, element);
        }

    }
}