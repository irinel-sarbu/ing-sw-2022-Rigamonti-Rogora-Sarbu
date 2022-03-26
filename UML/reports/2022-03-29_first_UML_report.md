# Initial UML Report

Group GC6 (Rigamonti-Rogora-Sarbu)

---

## Design choices and additional notes regarding intial UML of the Model:

- The Game class is used as a broker for the controller to call more complex methods on the model; simpler methods can
  still be called by obtaining `Game`'s elements.
- Getter and setter for the instances of objects referred by game are oitted to maintain UML readability.
- Character effects are handled in the controller, the `Character` class is used to reoport to the controller if a
  character has been used and act accordingly.
- Each `SchoolBoard` instance is accessible through it's reference inside `Player`, `Students` are placed inside Lists
  when in the entrance or a Stack when in the `DiningRoom`.
- All pieces in the play ar identified by an unique ID (an `int` or a `String`).
- In expert mode there are both:
    - A common stash of coins as an object of `CoinSupply`.
    - A personal stash of coins for each player, accessible through their `SchoolBoard`.
- Each IslandGroups is an aggregation of IslandTiles, when constructed an `IslandGroup` contains a single `IslandTile`;
  The `IslandGroup.join(IslandGroup)` methods remove all tiles from a group and add them to the other only if the tower
  colors of the two groups matches.

---