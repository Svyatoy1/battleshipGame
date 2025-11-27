package org.battleship.model;

public enum CellState {
    UNKNOWN,  // клітинка ще не стріляна
    HAS_SHIP,  // тут стоїть корабель (для рендера)
    MISS,     // промах
    HIT,      // влучили по кораблю
    SUNK      // частина потопленого корабля
}
