package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {
    // REST-Endpoint für Leaderboards (sortiert GameResults nach Score)
    @GetMapping
    // sortiere nach score (absteigend) und tiebreaker: timeInSeconds (aufsteigend) "schneller ist besser"
    // änderung: .sortedWith(compareBy({ -it.score }, { it.id })) -> .sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))
    fun getLeaderboard(): List<GameResult> =
        gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

}