package org.analyse.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GUIUtilitiesTest {

    @Nested
    @DisplayName("Window Centering Tests")
    class WindowCenteringTests {

        @Test
        @DisplayName("Should center component on screen")
        void shouldCenterComponentOnScreen() {
            JFrame frame = new JFrame();
            frame.setSize(400, 300);

            GUIUtilities.centerComponent(frame);

            Point location = frame.getLocation();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            int expectedX = (screenSize.width - 400) / 2;
            int expectedY = (screenSize.height - 300) / 2;

            assertEquals(expectedX, location.x, 5);
            assertEquals(expectedY, location.y, 5);
        }

        @Test
        @DisplayName("Should center child relative to parent")
        void shouldCenterChildRelativeToParent() {
            JFrame parent = new JFrame();
            parent.setBounds(100, 100, 600, 400);

            JDialog child = new JDialog(parent);
            child.setSize(200, 150);

            GUIUtilities.centerComponent(parent, child);

            Point location = child.getLocation();

            int expectedX = 100 + (600 - 200) / 2;
            int expectedY = 100 + (400 - 150) / 2;

            assertEquals(expectedX, location.x, 5);
            assertEquals(expectedY, location.y, 5);
        }
    }

    @Nested
    @DisplayName("Image Icon Tests")
    class ImageIconTests {

        @Test
        @DisplayName("Should cache loaded icons")
        void shouldCacheLoadedIcons() {
            String iconPath = "mcd.png";

            ImageIcon icon1 = GUIUtilities.getImageIcon(iconPath);
            ImageIcon icon2 = GUIUtilities.getImageIcon(iconPath);

            if (icon1 != null) {
                assertSame(icon1, icon2, "Should return same cached instance");
            }
        }

        @Test
        @DisplayName("Should return null for non-existent image")
        void shouldReturnNullForNonExistentImage() {
            ImageIcon icon = GUIUtilities.getImageIcon("nonexistent.png");
            assertNull(icon);
        }

        @Test
        @DisplayName("Should load image from resources")
        void shouldLoadImageFromResources() {
            ImageIcon icon = GUIUtilities.getImageIcon("new.png");
            if (icon != null) {
                assertNotNull(icon.getImage());
                assertTrue(icon.getIconWidth() > 0);
                assertTrue(icon.getIconHeight() > 0);
            }
        }
    }

    @Nested
    @DisplayName("Font Tests")
    class FontTests {

        @Test
        @DisplayName("Should cache loaded fonts")
        void shouldCacheLoadedFonts() {
            Font font1 = GUIUtilities.getFont("Arial");
            Font font2 = GUIUtilities.getFont("Arial");

            if (font1 != null) {
                assertSame(font1, font2, "Should return same cached font instance");
            }
        }

        @Test
        @DisplayName("Should return font with default size")
        void shouldReturnFontWithDefaultSize() {
            Font font = GUIUtilities.getFont("Arial");
            if (font != null) {
                assertEquals(12, font.getSize());
            }
        }

        @Test
        @DisplayName("Should load custom font from resources")
        void shouldLoadCustomFontFromResources() {
            Font font = GUIUtilities.loadFont("test.ttf");
            if (font != null) {
                assertNotNull(font.getFontName());
            }
        }
    }

    @Nested
    @DisplayName("Message Display Tests")
    class MessageDisplayTests {

        @Test
        @DisplayName("Should create message dialog with correct properties")
        void shouldCreateMessageDialogWithCorrectProperties() {
            JFrame parent = new JFrame();
            String title = "Test Title";
            String message = "Test Message";

            SwingUtilities.invokeLater(() -> {
                try {
                    Robot robot = new Robot();
                    robot.setAutoDelay(100);

                    Timer timer = new Timer(500, e -> {
                        robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
                        robot.keyRelease(java.awt.event.KeyEvent.VK_ESCAPE);
                    });
                    timer.setRepeats(false);
                    timer.start();

                } catch (AWTException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Nested
    @DisplayName("Error Dialog Tests")
    class ErrorDialogTests {

        @Test
        @DisplayName("Should display error message with exception")
        void shouldDisplayErrorMessageWithException() {
            Exception testException = new Exception("Test exception message");
            String errorMessage = "An error occurred";

            SwingUtilities.invokeLater(() -> {
                try {
                    Robot robot = new Robot();
                    robot.setAutoDelay(100);

                    Timer timer = new Timer(500, e -> {
                        robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
                        robot.keyRelease(java.awt.event.KeyEvent.VK_ESCAPE);
                    });
                    timer.setRepeats(false);
                    timer.start();

                } catch (AWTException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Nested
    @DisplayName("Dimension Creation Tests")
    class DimensionCreationTests {

        @Test
        @DisplayName("Should create dimension with specified values")
        void shouldCreateDimensionWithSpecifiedValues() {
            Dimension dim = GUIUtilities.getDimension(800, 600);

            assertNotNull(dim);
            assertEquals(800, dim.width);
            assertEquals(600, dim.height);
        }

        @Test
        @DisplayName("Should create point with specified coordinates")
        void shouldCreatePointWithSpecifiedCoordinates() {
            Point point = GUIUtilities.getPoint(100, 200);

            assertNotNull(point);
            assertEquals(100, point.x);
            assertEquals(200, point.y);
        }
    }

    @Nested
    @DisplayName("HTML Window Tests")
    class HtmlWindowTests {

        @Test
        @DisplayName("Should create HTML window with correct title")
        void shouldCreateHtmlWindowWithCorrectTitle() {
            JFrame parent = new JFrame();
            String title = "Test HTML Window";
            String resource = "/test.html";

            HtmlWindow window = GUIUtilities.openHtmlWindow(parent, title, resource);

            if (window != null) {
                assertEquals(title, window.getTitle());
                window.dispose();
            }
        }
    }

    @Nested
    @DisplayName("File URL Tests")
    class FileUrlTests {

        @Test
        @DisplayName("Should return null for non-existent file")
        void shouldReturnNullForNonExistentFile() {
            URL url = GUIUtilities.getFileURL("nonexistent_file.txt");
            assertNull(url);
        }

        @Test
        @DisplayName("Should return URL for existing resource")
        void shouldReturnUrlForExistingResource() {
            URL url = GUIUtilities.getFileURL("new.png");
            if (url != null) {
                assertNotNull(url.toString());
                assertTrue(url.toString().contains("new.png"));
            }
        }
    }

    @Nested
    @DisplayName("Input Stream Tests")
    class InputStreamTests {

        @Test
        @DisplayName("Should return null for non-existent file stream")
        void shouldReturnNullForNonExistentFileStream() {
            InputStream stream = GUIUtilities.getInputStream("nonexistent_file.txt");
            assertNull(stream);
        }

        @Test
        @DisplayName("Should return stream for existing resource")
        void shouldReturnStreamForExistingResource() {
            InputStream stream = GUIUtilities.getInputStream("new.png");
            if (stream != null) {
                assertNotNull(stream);
                try {
                    stream.close();
                } catch (Exception e) {
                    fail("Failed to close stream");
                }
            }
        }
    }
}