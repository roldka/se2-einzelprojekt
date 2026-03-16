package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

// Tests für 100% Coverage in LeaderboardController :)
class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    // [ALT] Ergebnis (sortiert nach id): first < second < third
    // [NEU] Ergebnis (sortiert nach timeInSeconds): second < third < first
    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeInSecondsSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)
        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        // res = Leaderboard-Reihenfolge
        val res: List<GameResult> = controller.getLeaderboard()

        // Änderung: Reihenfolge der Spieler (first, second, third) gemäß neuer Sortierung angepasst
        // Erwartete Reihenfolge: second (10s) < third (15s) < first (20s)
        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(second, res[0])
        assertEquals(third, res[1])
        assertEquals(first, res[2])
    }

    // "normales" Verhalten von getLeaderboard mit rank testen (+- 3 Positionen)
    @Test
    fun test_getLeaderboard_rank5_returnsPlayersAroundRank() {

        val players = (1..9).map { GameResult(it.toLong(),"p$it",100-it,10.0) }

        whenever(mockedService.getGameResults()).thenReturn(players)

        val res = controller.getLeaderboard(5)

        assertEquals(7, res.size)
        assertEquals("p2", res[0].playerName)
        assertEquals("p8", res[6].playerName)
    }

    // Verhalten von getLeaderboard mit rank = 1 testen (+ 3 Positionen)
    @Test
    fun test_getLeaderboard_rank1_returnsBeginning() {

        val players = (1..9).map { GameResult(it.toLong(),"p$it",100-it,10.0) }

        whenever(mockedService.getGameResults()).thenReturn(players)

        val res = controller.getLeaderboard(1)

        assertEquals(4, res.size)
        assertEquals("p1", res[0].playerName)
    }

    // Verhalten von getLeaderboard mit rank = 9 (= letzter) testen (- 3 Positionen)
    @Test
    fun test_getLeaderboard_rank9_returnsEnd() {

        val players = (1..9).map { GameResult(it.toLong(),"p$it",100-it,10.0) }

        whenever(mockedService.getGameResults()).thenReturn(players)

        val res = controller.getLeaderboard(9)

        assertEquals(4, res.size)
        assertEquals("p6", res[0].playerName)
        assertEquals("p9", res[3].playerName)
    }

    // Verhalten von getLeaderboard mit ungültigen rank (negativ / zu klein)
    @Test
    fun test_getLeaderboard_rankTooSmall_throwsException() {

        val players = (1..5).map { GameResult(it.toLong(),"p$it",100-it,10.0) }

        whenever(mockedService.getGameResults()).thenReturn(players)

        assertThrows<ResponseStatusException> {
            controller.getLeaderboard(0)
        }
    }

    // Verhalten von getLeaderboard mit ungültigen rank (zu groß)
    @Test
    fun test_getLeaderboard_rankTooLarge_throwsException() {

        val players = (1..5).map { GameResult(it.toLong(),"p$it",100-it,10.0) }

        whenever(mockedService.getGameResults()).thenReturn(players)

        assertThrows<ResponseStatusException> {
            controller.getLeaderboard(10)
        }
    }
}