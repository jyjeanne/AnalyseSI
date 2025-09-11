package org.analyse.merise.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.analyse.core.gui.AnalyseFrame;
import org.analyse.core.gui.action.NavigationActionFactory;
import org.analyse.core.gui.action.BasicAction;
import org.analyse.core.modules.AnalysePanel;
import org.analyse.merise.gui.table.DictionnaireTable;
import org.analyse.merise.mcd.composant.MCDComponent;
import org.analyse.merise.mcd.composant.MLDComponent;
import org.analyse.merise.mcd.composant.MPDComponent;
import org.analyse.merise.sql.SQLCommand;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeriseModuleTest {

    private MeriseModule meriseModule;

    @Mock
    private AnalyseFrame mockAnalyseFrame;

    @Mock
    private NavigationActionFactory mockNavigationFactory;

    @Mock
    private BasicAction mockAction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        meriseModule = new MeriseModule();
        when(mockAnalyseFrame.getNavigationActionFactory()).thenReturn(mockNavigationFactory);
        when(mockNavigationFactory.buildNavigationAction(any(ImageIcon.class), anyString(), anyString(), any(AnalysePanel.class)))
                .thenReturn(mockAction);
    }

    @Test
    void moduleIdReturnsMerise() {
        String id = meriseModule.getID();

        assertEquals("MERISE", id);
    }

    @Test
    void moduleNameReturnsMerise() {
        String name = meriseModule.getName();

        assertEquals("Merise", name);
    }

    @Test
    void moduleAuthorReturnsCorrectAuthor() {
        String author = meriseModule.getAuthor();

        assertEquals("Dreux Loic", author);
    }






    @Test
    void dictionnaireTableIsNullBeforeInitialization() {
        assertNull(meriseModule.getDictionnaireTable());
    }

    @Test
    void allComponentsAreNullBeforeInitialization() {
        assertNull(meriseModule.getMCDComponent());
        assertNull(meriseModule.getMPDComponent());
        assertNull(meriseModule.getMLDComponent());
        assertNull(meriseModule.getSQLCommand());
    }

    @Test
    void allPanelsAreNullBeforeInitialization() {
        assertNull(meriseModule.getDictionnairePanel());
        assertNull(meriseModule.getMCDPanel());
        assertNull(meriseModule.getMPDPanel());
        assertNull(meriseModule.getSQLPanel());
    }


    @Test
    void clearMethodThrowsExceptionWhenComponentsAreNull() {
        assertThrows(NullPointerException.class, () -> meriseModule.clear());
    }


    @Test
    void initGuiWithNullAnalyseFrameThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            meriseModule.initGUI(null);
        });
    }
}
