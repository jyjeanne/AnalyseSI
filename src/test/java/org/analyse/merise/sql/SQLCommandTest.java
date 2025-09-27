package org.analyse.merise.sql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.List;
import java.util.Observer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SQLCommandTest {

    private SQLCommand sqlCommand;

    @BeforeEach
    void setUp() {
        sqlCommand = new SQLCommand();
    }

    @Nested
    @DisplayName("Connection State Tests")
    class ConnectionStateTests {

        @Test
        @DisplayName("Should initialize with disconnected state")
        void shouldInitializeWithDisconnectedState() {
            assertEquals(SQLCommand.DECONNECTED, sqlCommand.getState());
        }
    }

    @Nested
    @DisplayName("SQL Keywords Tests")
    class SQLKeywordsTests {

        @Test
        @DisplayName("Should contain standard SQL keywords")
        void shouldContainStandardSQLKeywords() {
            List<String> keywords = sqlCommand.getKeywords();

            assertNotNull(keywords);
            assertTrue(keywords.size() > 0);
            assertTrue(keywords.contains("CREATE"));
            assertTrue(keywords.contains("ALTER"));
            assertTrue(keywords.contains("SELECT"));
            assertTrue(keywords.contains("INSERT"));
            assertTrue(keywords.contains("TABLE"));
            assertTrue(keywords.contains("VIEW"));
        }

        @Test
        @DisplayName("Should contain SQL constraint keywords")
        void shouldContainSQLConstraintKeywords() {
            List<String> keywords = sqlCommand.getKeywords();

            assertTrue(keywords.contains("PRIMARY"));
            assertTrue(keywords.contains("FOREIGN"));
            assertTrue(keywords.contains("KEY"));
            assertTrue(keywords.contains("REFERENCES"));
            assertTrue(keywords.contains("NOT"));
            assertTrue(keywords.contains("NULL"));
        }
    }

    @Nested
    @DisplayName("SQL Types Tests")
    class SQLTypesTests {

        @Test
        @DisplayName("Should contain standard SQL types")
        void shouldContainStandardSQLTypes() {
            List<String> types = sqlCommand.getTypes();

            assertNotNull(types);
            assertTrue(types.size() > 0);
            assertTrue(types.contains("VARCHAR"));
            assertTrue(types.contains("INTEGER"));
            assertTrue(types.contains("DECIMAL"));
            assertTrue(types.contains("DATE"));
            assertTrue(types.contains("TIME"));
            assertTrue(types.contains("TIMESTAMP"));
        }

    }

    @Nested
    @DisplayName("Request Management Tests")
    class RequestManagementTests {

        @Test
        @DisplayName("Should add request and get all as string")
        void shouldAddRequestAndGetAllAsString() {
            String request1 = "CREATE TABLE test (id INTEGER PRIMARY KEY);";
            String request2 = "INSERT INTO test VALUES (1);";

            sqlCommand.addRequest(request1);
            sqlCommand.addRequest(request2);

            String allRequests = sqlCommand.getRequests();
            assertTrue(allRequests.contains(request1));
            assertTrue(allRequests.contains(request2));
        }

        @Test
        @DisplayName("Should clear all requests")
        void shouldClearAllRequests() {
            sqlCommand.addRequest("CREATE TABLE test (id INTEGER);");
            sqlCommand.addRequest("INSERT INTO test VALUES (1);");

            sqlCommand.clear();

            String allRequests = sqlCommand.getRequests();
            assertEquals("", allRequests);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should get error information")
        void shouldGetErrorInformation() {
            assertNull(sqlCommand.getError());
            assertEquals(0, sqlCommand.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Observer Pattern Tests")
    class ObserverPatternTests {

        @Test
        @DisplayName("Should add observers")
        void shouldAddObservers() {
            Observer mockObserver = mock(Observer.class);

            assertDoesNotThrow(() -> sqlCommand.addObserver(mockObserver));
        }
    }

    @Nested
    @DisplayName("SQL Syntax Tests")
    class SQLSyntaxTests {

        @Test
        @DisplayName("Should support different SQL syntaxes")
        void shouldSupportDifferentSQLSyntaxes() {
            assertNotNull(SQLCommand.SQLsyntax.MySQL);
            assertNotNull(SQLCommand.SQLsyntax.PostgreSQL);
            assertNotNull(SQLCommand.SQLsyntax.OracleDB);
        }
    }
}