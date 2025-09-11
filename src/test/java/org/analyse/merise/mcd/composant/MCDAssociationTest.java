package org.analyse.merise.mcd.composant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.analyse.merise.gui.table.DictionnaireTable;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MCDAssociationTest {

    private MCDAssociation association;

    @Mock
    private MCDComponent mockMcdComponent;

    @Mock
    private DictionnaireTable mockDictionnaireTable;

    @Mock
    private Graphics2D mockGraphics2D;

    @Mock
    private FontMetrics mockFontMetrics;

    @Mock
    private Font mockFont;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockMcdComponent.getData()).thenReturn(mockDictionnaireTable);
    }

    @Test
    void constructorWithMcdComponentOnlyCreatesAssociationWithDefaultName() {
        association = new MCDAssociation(mockMcdComponent);

        assertTrue(association.getName().startsWith("Association "));
        assertNotNull(association);
        assertEquals(mockMcdComponent, association.getMCD());
    }

    @Test
    void constructorWithCoordinatesCreatesAssociationAtSpecifiedPosition() {
        int x = 100;
        int y = 150;

        association = new MCDAssociation(mockMcdComponent, x, y);

        assertEquals(x, association.getX());
        assertEquals(y, association.getY());
        assertTrue(association.getName().startsWith("Association "));
    }


    @Test
    void updateSizeCalculatesCorrectDimensionsWithInformations() {
        association = new MCDAssociation(mockMcdComponent, "Test", 0, 0);
        association.addInformation("info1");
        association.addInformation("info2");

        when(mockMcdComponent.getFont()).thenReturn(mockFont);
        when(mockMcdComponent.getFontMetrics(mockFont)).thenReturn(mockFontMetrics);
        when(mockFontMetrics.stringWidth("Test")).thenReturn(30);
        when(mockFontMetrics.stringWidth("Info1Name")).thenReturn(80);
        when(mockFontMetrics.stringWidth("Info2Name")).thenReturn(70);
        when(mockFontMetrics.getMaxDescent()).thenReturn(5);
        when(mockDictionnaireTable.getValue("info1", DictionnaireTable.NAME)).thenReturn("Info1Name");
        when(mockDictionnaireTable.getValue("info2", DictionnaireTable.NAME)).thenReturn("Info2Name");

        association.updateSize();

        assertEquals(130, association.getWidth());
        assertEquals(80, association.getHeight());
    }

    @Test
    void paintRendersAssociationWithoutInformations() {
        association = new MCDAssociation(mockMcdComponent, "TestAssoc", 10, 20);
        when(mockMcdComponent.getFont()).thenReturn(mockFont);
        when(mockMcdComponent.getFontMetrics(mockFont)).thenReturn(mockFontMetrics);
        when(mockFontMetrics.stringWidth("TestAssoc")).thenReturn(60);
        when(mockFontMetrics.getMaxDescent()).thenReturn(5);
        when(mockGraphics2D.create()).thenReturn(mockGraphics2D);

        association.paint(mockGraphics2D);

        verify(mockGraphics2D).setFont(mockFont);
        verify(mockGraphics2D, atLeast(1)).setColor(any());
        verify(mockGraphics2D).drawString(eq("TestAssoc"), anyInt(), anyInt());
    }

    @Test
    void paintRendersAssociationWithInformations() {
        association = new MCDAssociation(mockMcdComponent, "TestAssoc", 10, 20);
        association.addInformation("info1");

        when(mockMcdComponent.getFont()).thenReturn(mockFont);
        when(mockMcdComponent.getFontMetrics(mockFont)).thenReturn(mockFontMetrics);
        when(mockFontMetrics.stringWidth(anyString())).thenReturn(60);
        when(mockFontMetrics.getMaxDescent()).thenReturn(5);
        when(mockDictionnaireTable.getValue("info1", DictionnaireTable.NAME)).thenReturn("Info1Name");

        association.paint(mockGraphics2D);

        verify(mockGraphics2D).drawString(eq("TestAssoc"), anyInt(), anyInt());
        verify(mockGraphics2D).drawString(eq("Info1Name"), anyInt(), anyInt());
    }

    @Test
    void getInformationsReturnsAllInformations() {
        association = new MCDAssociation(mockMcdComponent, "Test", 0, 0);
        association.addInformation("info1");
        association.addInformation("info2");

        List<String> informations = association.getInformations();

        assertEquals(2, informations.size());
        assertTrue(informations.contains("info1"));
        assertTrue(informations.contains("info2"));
    }

    @Test
    void setInformationsReplacesExistingInformations() {
        association = new MCDAssociation(mockMcdComponent, "Test", 0, 0);
        association.addInformation("oldInfo");

        List<String> newInformations = new ArrayList<>();
        newInformations.add("newInfo1");
        newInformations.add("newInfo2");

        association.setInformations(newInformations);

        List<String> result = association.getInformations();
        assertEquals(2, result.size());
        assertTrue(result.contains("newInfo1"));
        assertTrue(result.contains("newInfo2"));
        assertFalse(result.contains("oldInfo"));
    }

    @Test
    void toStringReturnsCorrectFormat() {
        association = new MCDAssociation(mockMcdComponent, "TestAssoc", 100, 200);

        String result = association.toString();

        assertTrue(result.startsWith("MCDAssociation : TestAssoc"));
    }

    @Test
    void multipleAssociationsHaveDifferentDefaultNames() {
        MCDAssociation association1 = new MCDAssociation(mockMcdComponent);
        MCDAssociation association2 = new MCDAssociation(mockMcdComponent);

        assertNotEquals(association1.getName(), association2.getName());
    }

    @Test
    void associationWithZeroCoordinatesIsPositionedCorrectly() {
        association = new MCDAssociation(mockMcdComponent, "Test", 0, 0);

        assertEquals(0, association.getX());
        assertEquals(0, association.getY());
    }

    @Test
    void associationWithNegativeCoordinatesIsPositionedCorrectly() {
        association = new MCDAssociation(mockMcdComponent, "Test", -50, -75);

        assertEquals(-50, association.getX());
        assertEquals(-75, association.getY());
    }

    @Test
    void updateSizeHandlesEmptyInformationName() {
        association = new MCDAssociation(mockMcdComponent, "Test", 0, 0);
        association.addInformation("info1");

        when(mockMcdComponent.getFont()).thenReturn(mockFont);
        when(mockMcdComponent.getFontMetrics(mockFont)).thenReturn(mockFontMetrics);
        when(mockFontMetrics.stringWidth("Test")).thenReturn(30);
        when(mockFontMetrics.stringWidth("")).thenReturn(0);
        when(mockFontMetrics.getMaxDescent()).thenReturn(5);
        when(mockDictionnaireTable.getValue("info1", DictionnaireTable.NAME)).thenReturn("");

        assertDoesNotThrow(() -> association.updateSize());
    }


}
