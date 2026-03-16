package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {
    // REST-Endpoint für Leaderboards (sortiert GameResults nach Score)
    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int? = null): List<GameResult> { // rank: Int? -> ? bedeutet, dass rank null sein kann
        // sortiere nach score (absteigend) und tiebreaker: timeInSeconds (aufsteigend) "schneller ist besser"
        // Änderung: .sortedWith(compareBy({ -it.score }, { it.id })) -> .sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))
        val leaderboard = gameResultService.getGameResults()
            .sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

        // für Aufrufe ohne Parameter "rank": einfach Leaderboard "normal" zurückgeben
        if (rank == null) {
            return leaderboard
        }

        // ungültiger rank (negativ oder zu groß)
        if (rank < 1 || rank > leaderboard.size) {
            // HTTP-Error 400: Bad Request
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

        // ab hier gibt es einen gültigen rank
        val index = rank - 1  // Index anpassen wegen Nullindizierung
        val start = maxOf(0, index - 3)  // entweder beim 0. (ersten) Spieler oder beim index-3. Spieler starten
        val end = minOf(leaderboard.size, index + 4)  // entweder beim leaderboard.size. (letzten) Spieler oder beim index+4. Spieler enden
        return leaderboard.subList(start, end)  // Teilliste vom Leaderboard (+-3 um rank herum) zurückgeben
    }




}