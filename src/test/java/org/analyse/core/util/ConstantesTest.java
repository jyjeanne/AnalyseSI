package org.analyse.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ConstantesTest {

    @Test
    @DisplayName("Should have application name constant")
    void shouldHaveApplicationNameConstant() {
        assertNotNull(Constantes.NOM_APPLICATION);
        assertEquals("AnalyseSI", Constantes.NOM_APPLICATION);
    }

    @Test
    @DisplayName("Should have release version constant")
    void shouldHaveReleaseVersionConstant() {
        assertNotNull(Constantes.RELEASE);
        assertFalse(Constantes.RELEASE.isEmpty());
        assertTrue(Constantes.RELEASE.contains("v"));
    }

    @Test
    @DisplayName("Should have author and contact information")
    void shouldHaveAuthorAndContactInformation() {
        assertNotNull(Constantes.AUTHOR);
        assertEquals("Bruno Dabo", Constantes.AUTHOR);

        assertNotNull(Constantes.CONTACT_EMAIL);
        assertTrue(Constantes.CONTACT_EMAIL.contains("@"));

        assertNotNull(Constantes.COMPANY);
        assertEquals("LYWOON SOFTWARE", Constantes.COMPANY);
    }

    @Test
    @DisplayName("Should have case constants")
    void shouldHaveCaseConstants() {
        assertEquals(0, Constantes.UPPER);
        assertEquals(1, Constantes.LOWER);
        assertNotEquals(Constantes.LOWER, Constantes.UPPER);
    }

    @Test
    @DisplayName("Should have file extension constants")
    void shouldHaveFileExtensionConstants() {
        assertNotNull(Constantes.FILE_EXTENSION);
        assertEquals("asi", Constantes.FILE_EXTENSION);

        assertNotNull(Constantes.ASI_ENCODING);
        assertEquals("UTF-8", Constantes.ASI_ENCODING);
    }

    @Test
    @DisplayName("Should have PNG file name constants")
    void shouldHavePngFileNameConstants() {
        assertNotNull(Constantes.FILE_PNG_ANALYSESI);
        assertEquals("analysesi.png", Constantes.FILE_PNG_ANALYSESI);

        assertNotNull(Constantes.FILE_PNG_NEW);
        assertEquals("filenew.png", Constantes.FILE_PNG_NEW);

        assertNotNull(Constantes.FILE_PNG_OPEN);
        assertEquals("fileopen.png", Constantes.FILE_PNG_OPEN);

        assertNotNull(Constantes.FILE_PNG_SAVE);
        assertEquals("filesave.png", Constantes.FILE_PNG_SAVE);

        assertNotNull(Constantes.FILE_PNG_SAVEAS);
        assertEquals("filesaveas.png", Constantes.FILE_PNG_SAVEAS);
    }
}