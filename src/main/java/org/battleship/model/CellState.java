package org.battleship.model;

public enum CellState {
    UNKNOWN,  // клітинка ще не стріляна
    MISS,     // промах
    HIT,      // влучили по кораблю
    SUNK      // частина потопленого корабля
}
