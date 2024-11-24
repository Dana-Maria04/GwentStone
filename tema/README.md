#### Copyright © 2024 Caruntu Dana-Maria 321CAa

### Overview:
This project simulates a turn-based card game combining elements from
HearthStone and Gwent. Two players compete using custom decks of cards,
taking turns to play cards, attack, or use special abilities. The game
ends when one player's hero is defeated.

### Project Structure:
#### Packages:

The **gameutils** package contains core game classes, while related
classes are grouped into sub-packages like **cardsinfo**:

- **heroes package**: Implements the heroes (e.g., General Kocioraw,
  Empress Thorina) using a Factory Design Pattern and Singleton for
  unique hero instances.

- **minions package**: Implements the minions (e.g. Disciple, The
  Ripper). The Factory Pattern is used for special minions, while
  others are handled by the **Minion** class.

#### Classes:
- **StartGame**: Handles game initialization and game loop logic,
  executing commands from **ActionsInput**.

- **CommandHandler**: Processes commands and writes game output to JSON.
  Handles methods like `getPlayerDeck`, `placeCard`, and `useHeroAbility`.

- **GameStats**: Tracks game statistics, including player turns,
  round count, and turn cycle, which are essential for multiple games.

- **GameConstants**: Defines constants like `MAX_HEALTH`, `START_MANA`,
  `NUM_ROWS`, and `NUM_CARDS`.

- **Hand**: Represents a player's hand, holding cards available to play.
  It has methods like `addCard`, `removeCard`, and `getHand`. An
  `ArrayList` is used to allow easy shifting of elements when cards
  are added or removed.

- **Table**: Manages minions on the board using an `ArrayList` of
  `LinkedLists` for easy card placement/removal. This structure
  allows automatic shifting of elements when minions are added or
  removed. Implements methods like `verifyTankOnRow` to ensure tanks
  are attacked first.

- **Player**: Represents a player, containing mana, deck, hero, and
  wins. Includes methods like `updateMana` (limits mana to 10 max)
  and `incWinCnt`.

- **Minion**: Represents a minion card, with fields for health,
  attack status, and frozen status. Includes `verifyIfTank` to
  check for tank type and an `ability` method for special minions.

  Special minions include:
    - **Disciple**: Increases minion health.
    - **Miraj**: Swaps its health with another minion.
    - **The Cursed One**: Swaps attack and health of another minion.
    - **The Ripper**: Decreases attack of other minions.

- **Cards**: Represents a general card, using `CardInput` to store
  attributes, inherited by **Minion** and **Hero** classes.

- **Hero**: Represents a hero, extending **Cards**. It tracks whether
  the hero has used its ability, and includes an overridden `ability`
  method for special heroes.

  Heroes include:
    - **General Kocioraw**: Increases minion attack.
    - **Empress Thorina**: Destroys the card with the highest health.
    - **King Mudface**: Increases minion health.
    - **Lord Royce**: Freezes opponent minions.

### OOP Concepts Used:
- Inheritance
- Polymorphism
- Encapsulation
- Interfaces
- Design Patterns: Factory, Singleton
- Wrapper Classes (e.g., Integer)
- Composition
- Aggregation
- Packages
- Exception Handling
- Method Overriding
- Final Classes/Methods/Variables

### Resources Used:
- [Refactoring Guru](https://refactoring.guru/)
- [OCW - POO-CA-CD](https://ocw.cs.pub.ro/courses/poo-ca-cd)
