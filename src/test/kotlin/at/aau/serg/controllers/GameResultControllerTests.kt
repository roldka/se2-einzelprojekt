package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import kotlin.test.Test
import kotlin.test.assertEquals

// Tests für 100% Coverage in GameResultController :)
// generiert mit ChatGPT
class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock(GameResultService::class.java)
        controller = GameResultController(mockedService)
    }

    // Test für getGameResult(gameResultId)
    @Test
    fun test_getGameResult_returnsCorrectResult() {
        // erstelle Beispiel-GameResult
        val gameResult = GameResult(1, "player1", 100, 10.0)

        // lege Verhalten des gemockten Services fest
        // wenn getGameResult(1) aufgerufen wird, returne gameResult
        `when`(mockedService.getGameResult(1)).thenReturn(gameResult)

        // rufe Methode im Controller auf (dieser ruft gemockten Service auf, gibt also gameResult zurück)
        val res = controller.getGameResult(1)

        // überprüfe, ob getGameResult(1) aufgerufen wurde
        verify(mockedService).getGameResult(1)
        // überprüfe, ob Ergebnis der Controller-Methode gleich dem erwarteten gameResult ist
        assertEquals(gameResult, res)
    }

    // Test für getAllGameResults()
    @Test
    fun test_getAllGameResults_returnsList() {

        val results = listOf(
            GameResult(1, "p1", 100, 10.0),
            GameResult(2, "p2", 90, 12.0)
        )

        `when`(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(2, res.size)
    }

    // Test für addGameResult(gameResult)
    // Hier ohne Mocken, da kein Rückgabewert, sondern nur "Weiterleiten" zum Service
    @Test
    fun test_addGameResult_callsService() {

        val gameResult = GameResult(0, "player", 50, 20.0)

        controller.addGameResult(gameResult)

        verify(mockedService).addGameResult(gameResult)
    }

    // Test für deleteGameResult(gameResultId)
    // Hier ohne Mocken, da kein Rückgabewert, sondern nur "Weiterleiten" zum Service
    @Test
    fun test_deleteGameResult_callsService() {

        controller.deleteGameResult(5)

        verify(mockedService).deleteGameResult(5)
    }
}