package at.aau.serg.models

// Datenmodell: Repräsentiert Spielergebnis für einen Spieler
data class GameResult(var id: Long, var playerName: String, var score: Int, var timeInSeconds: Double)