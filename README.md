# Tetris (JavaFX Refactoring & Extension)
This project is a refactored and extended version of a classic Tetris game built with JavaFX. The work focuses on transitioning the legacy codebase to a robust **MVC (Model-View-Controller)** architecture, implementing modern gameplay mechanics, and overhauling the user interface.

## 🔗 GitHub
* **Repository Link:** [https://github.com/karl7-7/CW2025.git]

---

## 🛠️ Compilation Instructions

This project uses **Maven** for build management and dependency handling.

Users can compile the application in several ways depending on their preferred tools and environment. The most reliable method is using Maven, as it automatically manages dependencies (like JavaFX and JUnit).

Using Maven command line (This is the standard, perform-independent method recommended for this project)
**1. Prerequisites:**
* **Java JDK 21** (or compatible newer version) installed.
* **Maven** installed and configured in your system PATH.

**2. Dependencies (Managed via `pom.xml`):**
* `org.openjfx:javafx-controls`
* `org.openjfx:javafx-fxml`
* `org.openjfx:javafx-media` (Added manually for background music support)
* `org.junit.jupiter:junit-jupiter` (Added for unit testing)

**3. Step-by-Step Compilation & Run:**
1.  Open your terminal or command prompt.
2.  Navigate to the project root directory (where `pom.xml` is located).
3.  Clean and compile the project:
    ```bash
    mvn clean compile
    ```
4.  Run the application using the JavaFX Maven plugin:
    ```bash
    mvn javafx:run
    ```
Using an IDE such as IntelliJ IDEA, Eclipse or VScode
**IntelliJ IDEA:**
1. Select File > Open and choose the project folder containing pom.xml
2. IntelliJ should automatically detect it as a Maven project and download dependecies
3. Open the Maven sidebar tab on the right
4. Nativage to Plugins > javafx > javafx:run and double click it to start the game

**VS code** 
1. Install the "Extension pack for Java" and "Maven for Java" extensions
2. Open the project folder
3. In the Maven sidebar view (button left ususally), expand Plugins > javafx
4. Right-click javafx:run and select Run

**Eclipse**
1. Go to File > Import > Maven > Existing Maven Projects
2. Select the project directory and finish
3. Right click the project > Run As > Maven Build
4. In the "Goals" field, type javafx:run and click Run


---

## ✅ Implemented and Working Properly

The following features have been successfully implemented, tested, and are functioning as expected:

* **MVC Architecture Refactoring:** The application has been successfully decoupled. `GuiController` handles flow, `GameViewRenderer` handles drawing, and `SimpleBoard` handles logic, improving maintainability.
* **Space Theme UI:** A complete visual overhaul featuring a purplish-black theme, neon-styled blocks, and "Cyberpunk" styled menus using CSS (`window_style.css`).
* **Ghost Piece:** A semi-transparent projection appears at the bottom of the column, indicating exactly where the current piece will land.
* **Hard Drop:** Pressing **SPACE** instantly drops the piece to the Ghost Piece position and locks it, speeding up gameplay.
* **Next Piece Preview:** The upcoming block is successfully passed from the logic layer and displayed in a dedicated panel on the right side of the window.
* **Audio System:** Background music loops during gameplay. 
* **Interactive Menus:** Start, Pause, Game Over, and "How to Play" screens are implemented as VBox overlays, allowing seamless navigation without restarting the app. Controls is added to help users understand the keybinds for the game.
* **Scoring & Leveling:** Implemented a quadratic scoring formula (`50 * lines²`) and a level progression system that increases game speed every 50 points up to level 20.

---

## ⚠️ Implemented but Not Working Properly

* **None.** All features currently present in the code (Ghost Piece, Music, High Score persistence, Next Piece) were debugged and are functioning correctly.

---

## ❌ Features Not Implemented

* **Hold Piece:** This feature was initially attempted but was removed.
    * *Reason:* I encountered issues where the held block would not render consistently in the side panel during the MVC refactoring. To ensure a stable and polished final product within the deadline, I opted to prioritize the **Persistent High Score** feature instead, which utilized the same screen space.
* **Complex Wall Kicks (SRS):**
    * *Reason:* The rotation system relies on basic collision detection. Implementing the full "Super Rotation System" (SRS) guidelines was deemed out of scope for this coursework, which focused primarily on architectural maintenance (MVC).

---

## 🆕 New Java Classes

| Class | Location | Purpose |
| :--- | :--- | :--- |
| **`InputHandler`** | `com.comp2042.gameUI` | Decouples input processing from the main Controller. It intercepts `KeyEvent`s and maps specific keys (Space, P, N) to abstract game events. |
| **`GameViewRenderer`** | `com.comp2042.gameUI` | Encapsulates all JavaFX drawing operations. It manages the `GridPane` updates for the main board, next piece, and ghost piece, removing low-level view logic from the Controller. |
| **`SoundOrganiser`** | `com.comp2042.gameUI` | Manages the `MediaPlayer` for background music. It handles loading the resource, looping the audio, and providing the mute toggling logic. |

---

## 📝 Modified Java Classes

| Class | Location | Changes & Rationale |
| :--- | :--- | :--- |
| **`GuiController`** | `com.comp2042.gameUI` | **Change:** Stripped of direct drawing logic (`Rectangle` creation) and game rule calculations. Added logic for High Score persistence (File I/O) and Menu navigation.<br>**Rationale:** To adhere to the Single Responsibility Principle and act purely as a mediator in the MVC pattern. |
| **`SimpleBoard`** | `com.comp2042.logic.game` | **Change:** Removed all references to JavaFX components (`Color`, `Rectangle`). Added `getGhostY()` calculation and `hardDrop()` logic.<br>**Rationale:** To make the Model purely logical and testable without UI dependencies. |
| **`ViewData`** | `com.comp2042.logic.game` | **Change:** Added fields for `ghostY` and `nextBrickData`.<br>**Rationale:** To act as a comprehensive Data Transfer Object (DTO) that passes all necessary state to the Renderer in one immutable packet. |

---

## ❗ Unexpected Problems

1.  **Audio Dependency Crashes:**
    * *Issue:* The application crashed immediately upon startup when trying to play music (`java.lang.NoClassDefFoundError`).
    * *Resolution:* I discovered that JavaFX 11+ does not include the media module by default. I had to manually add the `org.openjfx:javafx-media` dependency to the `pom.xml` file.

2.  **"How to Play" Screen Sizing:**
    * *Issue:* The text in the "How to Play" overlay was too large, pushing the "Back" button off the bottom of the screen, making it impossible to close the menu.
    * *Resolution:* I standardised the font sizes in the FXML (reducing title size and body text) and condensed the spacing to ensure all UI elements remain visible within the default 600x510 window size.

3.  **Ghost Piece Rendering Artifacts:**
    * *Issue:* Initially, the Ghost Piece would persist on the screen after the block moved, or draw over existing blocks.
    * *Resolution:* I updated the `GameViewRenderer` to clear and redraw the specific grid cells dynamically based on the updated `ViewData` on every tick, ensuring the ghost piece only renders in valid empty spaces.

    * *Issue:* In the early stages of development, as the blocks stacked on the game board, around half way up the board, the game would suddenly end and a gameover screen would suddenly appear
    * *Resolution:*

