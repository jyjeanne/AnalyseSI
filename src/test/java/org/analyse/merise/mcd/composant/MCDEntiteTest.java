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
import java.util.List;

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
    @DisplayName("Properties Tests")
    class PropertiesTests {

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
        }

        @Test
        @DisplayName("Should get information at index")
        void shouldGetInformationAtIndex() {
            String info = "PRIMARY_KEY:id:INTEGER";
            entite.addInformation(info);

            String retrieved = entite.getInformation(0);
            assertEquals(info, retrieved);
        }

        @Test
        @DisplayName("Should clear all informations")
        void shouldClearAllInformations() {
            entite.addInformation("info1");
            entite.addInformation("info2");

            entite.clearInformation();

            assertEquals(0, entite.sizeInformation());
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
        @DisplayName("Should update position")
        void shouldUpdatePosition() {
            int newX = 200;
            int newY = 300;

            entite.setX(newX);
            entite.setY(newY);

            assertEquals(newX, entite.getX());
            assertEquals(newY, entite.getY());
        }

        @Test
        @DisplayName("Should update size")
        void shouldUpdateSize() {
            int newWidth = 150;
            int newHeight = 100;

            entite.setWidth(newWidth);
            entite.setHeight(newHeight);

            assertEquals(newWidth, entite.getWidth());
            assertEquals(newHeight, entite.getHeight());
        }

        @Test
        @DisplayName("Should detect if point is inside entite")
        void shouldDetectIfPointIsInsideEntite() {
            entite.setX(100);
            entite.setY(100);
            entite.setWidth(100);
            entite.setHeight(50);

            assertTrue(entite.contient(150, 125));
            assertFalse(entite.contient(50, 50));
            assertFalse(entite.contient(250, 250));
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
        @DisplayName("Should return correct type")
        void shouldReturnCorrectType() {
            String type = entite.getType();

            assertNotNull(type);
            assertEquals("ENTITE", type);
        }
    }

    @Nested
    @DisplayName("Selection Tests")
    class SelectionTests {

        @BeforeEach
        void setUpEntite() {
            entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);
        }

        @Test
        @DisplayName("Should handle selection state")
        void shouldHandleSelectionState() {
            assertFalse(entite.isSelected());

            entite.setSelected(true);
            assertTrue(entite.isSelected());

            entite.setSelected(false);
            assertFalse(entite.isSelected());
        }
    }

    @Nested
    @DisplayName("Link Management Tests")
    class LinkManagementTests {

        @BeforeEach
        void setUpEntite() {
            entite = new MCDEntite(mockMCD, "TestEntity", 100, 100);
        }

        @Test
        @DisplayName("Should add and retrieve links")
        void shouldAddAndRetrieveLinks() {
            MCDLien mockLink = mock(MCDLien.class);
            entite.addLink(mockLink);

            List links = entite.getLinks();
            assertNotNull(links);
            assertTrue(links.contains(mockLink));
        }

        @Test
        @DisplayName("Should remove links")
        void shouldRemoveLinks() {
            MCDLien mockLink = mock(MCDLien.class);
            entite.addLink(mockLink);
            entite.removeLink(mockLink);

            List links = entite.getLinks();
            assertFalse(links.contains(mockLink));
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
    }

    @Nested
    @DisplayName("Paint Tests")
    class PaintTests {

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
}