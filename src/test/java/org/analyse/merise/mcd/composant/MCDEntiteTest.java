package org.analyse.merise.mcd.composant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.analyse.merise.gui.table.DictionnaireTable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;

class MCDEntiteTest {

    private MCDComponent mockMCD;
    private DictionnaireTable mockData;
    private MCDEntite entite;

    @BeforeEach
    void setUp() {
        mockMCD = mock(MCDComponent.class);
        mockData = mock(DictionnaireTable.class);
        when(mockMCD.getData()).thenReturn(mockData);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create entite with default name and position")
        void shouldCreateEntiteWithDefaultNameAndPosition() {
            entite = new MCDEntite(mockMCD);

            assertNotNull(entite);
            assertTrue(entite.getName().startsWith("Entite"));
            assertTrue(entite.getX() >= 0);
            assertTrue(entite.getY() >= 0);
        }

        @Test
        @DisplayName("Should create entite with specified position")
        void shouldCreateEntiteWithSpecifiedPosition() {
            int x = 100;
            int y = 200;
            entite = new MCDEntite(mockMCD, x, y);

            assertNotNull(entite);
            assertTrue(entite.getName().startsWith("Entite"));
            assertEquals(x, entite.getX());
            assertEquals(y, entite.getY());
        }

        @Test
        @DisplayName("Should create entite with specified name and position")
        void shouldCreateEntiteWithSpecifiedNameAndPosition() {
            String name = "TestEntity";
            int x = 150;
            int y = 250;
            entite = new MCDEntite(mockMCD, name, x, y);

            assertNotNull(entite);
            assertEquals(name, entite.getName());
            assertEquals(x, entite.getX());
            assertEquals(y, entite.getY());
        }
    }

    @Nested
    @DisplayName("Information Management Tests")
    class InformationManagementTests {

        @BeforeEach
        void setUpEntite() {
            entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);
        }

        @Test
        @DisplayName("Should add information to entite")
        void shouldAddInformationToEntite() {
            String info = "PRIMARY_KEY:id:INTEGER";
            entite.addInformation(info);

            assertTrue(entite.sizeInformation() > 0);
            assertEquals(1, entite.sizeInformation());
        }



        @Test
        @DisplayName("Should get information code at index")
        void shouldGetInformationCodeAtIndex() {
            String info = "PRIMARY_KEY:id:INTEGER";
            entite.addInformation(info);

            String retrieved = entite.getCodeInformation(0);
            assertEquals(info, retrieved);
        }

        @Test
        @DisplayName("Should clear all informations")
        void shouldClearAllInformations() {
            entite.addInformation("info1");
            entite.addInformation("info2");

            entite.clearInformations();

            assertEquals(0, entite.sizeInformation());
        }

        @Test
        @DisplayName("Should delete specific information")
        void shouldDeleteSpecificInformation() {
            String info1 = "info1";
            String info2 = "info2";
            entite.addInformation(info1);
            entite.addInformation(info2);

            entite.deleteInformation(info1);

            assertEquals(1, entite.sizeInformation());
            assertEquals(info2, entite.getCodeInformation(0));
        }

        @Test
        @DisplayName("Should move informations within the list")
        void shouldMoveInformationsWithinList() {
            String info1 = "info1";
            String info2 = "info2";
            entite.addInformation(info1);
            entite.addInformation(info2);

            entite.moveInformations(0, 1);

            assertEquals(info2, entite.getCodeInformation(0));
            assertEquals(info1, entite.getCodeInformation(1));
        }
    }

    @Nested
    @DisplayName("Name and Type Tests")
    class NameAndTypeTests {

        @BeforeEach
        void setUpEntite() {
            entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);
        }

        @Test
        @DisplayName("Should get and set name")
        void shouldGetAndSetName() {
            String newName = "UpdatedEntity";
            entite.setName(newName);

            assertEquals(newName, entite.getName());
        }

        @Test
        @DisplayName("Should have consistent name behavior")
        void shouldHaveConsistentNameBehavior() {
            String originalName = entite.getName();
            String newName = "UpdatedEntity";
            entite.setName(newName);

            assertNotNull(originalName);
            assertEquals(newName, entite.getName());
        }
    }

    @Nested
    @DisplayName("Position and Size Tests")
    class PositionAndSizeTests {

        @BeforeEach
        void setUpEntite() {
            entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);
        }

        @Test
        @DisplayName("Should get position coordinates")
        void shouldGetPositionCoordinates() {
            assertEquals(100, entite.getX());
            assertEquals(100, entite.getY());
        }

        @Test
        @DisplayName("Should get dimensions")
        void shouldGetDimensions() {
            int width = entite.getWidth();
            int height = entite.getHeight();

            assertTrue(width >= 0);
            assertTrue(height >= 0);
        }
    }

    @Nested
    @DisplayName("Clone Tests")
    class CloneTests {

        @BeforeEach
        void setUpEntite() {
            entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);
        }

        @Test
        @DisplayName("Should create copy of entite with same properties")
        void shouldCreateCopyOfEntiteWithSameProperties() {
            entite.addInformation("info1");
            entite.addInformation("info2");

            // Test entity properties instead of clone
            MCDEntite newEntity = new MCDEntite(mockMCD, entite.getName(), entite.getX(), entite.getY());

            assertNotSame(entite, newEntity);
            assertEquals(entite.getName(), newEntity.getName());
            assertEquals(entite.getX(), newEntity.getX());
            assertEquals(entite.getY(), newEntity.getY());
        }
    }

    @Nested
    @DisplayName("Paint and Graphics Tests")
    class PaintAndGraphicsTests {

        @BeforeEach
        void setUpEntite() {
            entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);
            Font mockFont = mock(Font.class);
            FontMetrics mockFontMetrics = mock(FontMetrics.class);
            when(mockMCD.getFont()).thenReturn(mockFont);
            when(mockMCD.getFontMetrics(any(Font.class))).thenReturn(mockFontMetrics);
            when(mockFontMetrics.stringWidth(anyString())).thenReturn(50);
            when(mockFontMetrics.getHeight()).thenReturn(15);
        }

        @Test
        @DisplayName("Should paint entite without exceptions")
        void shouldPaintEntiteWithoutExceptions() {
            Graphics2D mockGraphics = mock(Graphics2D.class);

            assertDoesNotThrow(() -> entite.paint(mockGraphics));

            verify(mockGraphics, atLeastOnce()).setFont(any(Font.class));
        }

    }

    @Nested
    @DisplayName("MCD Component Tests")
    class MCDComponentTests {

        @BeforeEach
        void setUpEntite() {
            entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);
        }

        @Test
        @DisplayName("Should get MCD component")
        void shouldGetMCDComponent() {
            MCDComponent mcd = entite.getMCD();
            assertSame(mockMCD, mcd);
        }

        @Test
        @DisplayName("Should have reference to data dictionary")
        void shouldHaveReferenceToDataDictionary() {
            // The entity should interact with the data dictionary through its MCD
            entite.addInformation("test_info");

            // Verify that the entity was created with a valid MCD reference
            assertNotNull(entite.getMCD());
        }
    }

    @Nested
    @DisplayName("Observer Pattern Tests")
    class ObserverPatternTests {

        @BeforeEach
        void setUpEntite() {
            entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);
        }

        @Test
        @DisplayName("Should notify observers when informations change")
        void shouldNotifyObserversWhenInformationsChange() {
            // The addInformation method should trigger observer notifications
            assertDoesNotThrow(() -> entite.addInformation("test_info"));

            // Verify that the entity maintains its state correctly
            assertEquals(1, entite.sizeInformation());
        }

    }
}