package SeaBattle.controller;

import SeaBattle.model.Player;
import SeaBattle.model.Ship;

public interface IDateExchange {
    Ship[][] getShipPlayer(Player player);
}
