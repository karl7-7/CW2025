Tetris (JavaFX Maintenance and Extension)

This project is a refactored and extended version of a classic Tetris game built with JavaFX. The work focuses on transitioning the legacy codebase to a robust MVC architecture implementing modern gameplay mechanics, overhauling the user interface. 

Github

Repository Link: https://github.com/karl7-7/CW2025.git

Compilation Instructions:

This project uses Maven for build management and dependency handling.

Users can compile the application in several ways depending on preferred tools and environment. The most reliable method is using Maven as it automatically manages dependencies like JavaFX and JUnit. 

Using Maven command line (recommended):

*   Prerequisites:
    
    *   Java JDK 21 installed or newer
        
    *   Maven installed and configured in your system PATH
        
    *   To check run in terminal:
        
        *   Java –version
            
        *   mvn -version
            
    *   If nothing shows up, install the files.
        
*   Dependencies (managed via pom.xml):
    
    *   Org.openjfx:javafx-controls
        
    *   Org.openjfx:javafx-fxml
        
    *   Org.openjfx:javafx-media (added manually for music support)
        
    *   Org.junit.jupiter:junit-jupiter (added for unit testing)
        
*   Step-by-step guide on compilation:
    
    *   Open your terminal or command prompt
        
    *   Navigate to the project root directory (where pom.xml is located)
        
    *   Clean and compile the project (mvn clean compile)
        
    *   Run the application using the JavaFX Maven plugin (mvn javafx:run)
        

Using an IDE like IntelliJ, Eclipse or VScode:

IntelliJ:

1.  Select file > Open and choose the project folder containing pom.xml
    
2.  intelliJ should automatically detect it as a Maven project and download dependencies
    
3.  Open the maven sidebar tab on the right
    
4.  Navigate to Plugins > javafx > javafx:run double click it to start the game
    

VS code:

1.  Install the “Extension pack for Java” and “Maven for Java” extensions 
    
2.  Open the project folder
    
3.  In the Maven sidebar view expand Plugins > javafx
    
4.  Right click javafx:run and select Run
    

Eclipse:

1.  Go to File > Import > Maven > Existing Maven Projects
    
2.  Select the project directory and finish
    
3.  Right click the project > Run As > Maven Build
    
4.  In the “Goals” field, type javafx:run and click Run
    

Implemented and Working Properly:
=================================

The following features have been successfully implemented, tested and are functioning as expected:

1) Ghost brick mechanic:

SimpleBoard.java is used to calculate exactly which row the piece would land in if it dropped instantly. The getGhostY() method essentially simulates where the block would land. It creates a copy of the current board matrix taking in the current piece’s x and y coordinates, moves down one row at a time checking for a collision. ViewData.java transports the calculated ghostY value to the renderer without exposing the game logic. GameViewRenderer.java draws the ghost version of the block at the calculated position. The CSS file is what gave it its “ghost” look.

2) Instant drop mechanic:

SimpleBoard.java is used to find the lowest valid row the piece can exist without colliding. The getGhostY method creates a loop incrementing the piece’s y value checking for a collision. If there is a collision it returns its y-1 value. InputHandler.java was updated because I needed to map the space key to trigger the drop action. GuiController.java was updated so that when the controller receives the drop event, it updates the board and awards the player extra bonus points for playing faster.

3) Start menu

gameLayout.fxml is where we define the start menu as a VBox container. Because it is defined after the game board, javafx renders it on top. The start menu is a vertical box containing a title, label and buttons and is set visible by default when the app launches. It is styled in the CSS file. GuiController handles user interaction. 

4) Controls and “how to play” options

In the FXML file, I added a VBox container for both screens. I defined them near the bottom of the file in order for these elements to be drawn on top of earlier elements. I styled them in the CSS stylesheet. The GuiController was used to handle the switching logic so the user can navigate between menus without getting lost. This is why the variable previousMenu was added to allow users to click Back to backtrack. Validation was also added in the controller to ensure that if a key like the arrowkeys is pressed, it doesn’t accidentally move the blocks in the game behind the menu.

5) Pause function and menu

The pause menu is a VBox container defined later in the FXML file so it renders on top of the game board. It contains options to resume the game, start a new game, view controls and return to the main menu. I styled in the external CSS stylesheet. I defined the core logic in the pauseGame method in the GuiController. The logic works as follows: if the game is paused, stop the game loop, set isPause to true and then show the menu. If the game is paused, if the game is running, start the game loop, set isPause to false and hide the menu. P is mapped to trigger the method and when isPause is true, the InputHandler ignores all other keys freezing the player's controls.

6) Game over menu

This menu is a VBox overlay, identical in structure to the pause and start menus. It is placed near the bottom of the FXML file, ensuring it draws on top of the game board. It contains a “game over” label and buttons to retry or go to the main menu. This is also styled in the external CSS stylesheet. The game needs to know when it is over and this is found out in the GuiController. In the controller there is a method called createNewBrick(). This method attempts to spawn a block at the top and returns true if there is a collision which means game over. The controller is also where the visibility of the game over menu is set to either be visible or hidden whenever game over is true. 

7) Preview the next shape

In SimpleBoard.java we retrieve the shape of the upcoming brick from the generator and package it for the UI. The BrickGenerator keeps track of the next piece. So inside the method getViewData() we call brickGenerator.getNextBrick() to take the first rotation state of that brick’s shape matrix to display as the preview. ViewData is used to transport the next brick’s shape matrix to the renderer safely. GameViewRenderer.java clears the previous previews and then draws the next brick into a dedicated side panel. The FXML file is where we define where this preview should appear.

8) Level system

In GuiController the progression rules are defined where you gain a level for every 50 points with the max level capped at 20. The gravity starts at 400ms and gets faster every level. Levels initially start at level 1. Inside the FXML, is where we define a label to display the level and style it with CSS. The controller will keep track of the level and automatically update the label when the score changes. 

9) Display score

The score is defined as a label in the FXML file and styled with CSS. The controller connects the model (Score) to the view (Label) and automatically updates the score so that the display is up to date. 

10) Audio

SoundOrganiser handles the details of loading and playing audio files. It uses MediaPlayer, a standard javafx class for controlling media playback. The playBackgroundMusic method takes a filename that it searches for in the resources folder and we loop the music infinite times to make the background music continuous. The controller initialises the sound system when the application starts. I added the javafx-media dependency to the pom.xml file to prevent any crashes.

11) Visual styling

I chose to establish a deep space/galaxy feel to my tetris game. In my .root selector, which sets the stage for the entire application window, I used a radial-gradient to somewhat mimic a nebula effect, transitioning from a deep purple in the centre to black at the edges. The game board border has a neon glow to and buttons have a metallic look to it. When hovering over buttons a glow is applied which gives immediate visual feedback to the user. Ghost blocks are styled to allow users to see where the block will be placed if dropped right there and then. 

Implemented but not working properly
====================================

1) Mute sound

In my project, I added methods that were meant to allow the user to mute the background music. What happened instead is that the methods are a part of my code, but have no usage and so there is no option to mute music for the user. The reason that this came about is due to time restrictions. I decided I did not have enough time left and decided to leave it out. If I were to do this again I would ensure I start earlier, preparing for disruptions like the Nations Cup.

2) Down movement scoring system

When you press the down button, it moves the piece down but it also increases the score by 1. I believe that this is too much as it then means that if you span the down button you can collect more points than if you do an immediate drop. To fix this, I would not award any points if the piece is moved down by the user.

Features not implemented
========================

1) Hold Piece

This feature was initially attempted but was removed because I encountered issues where the held block would not render consistently in the side panel. To ensure a stable and polished final product under time constraint, I opted to remove the feature instead.

2) Sound effects

I would have liked to add sound effects to when a button is clicked, increasing user visual feedback, a sound when a block is placed and when the game over screen appears. This would have made the game more interactive, however I did not allow myself enough time to add these features. 

3) Bomb block or other experimental block types

The bomb block feature was an experimental idea I had where when placed, would clear a 3x3 grid of the game. Due to the complexity of the idea, combined with the time restriction, I opted to discard the feature.

4) High score

A system to track the top scores achieved. Ideally, this would be saved locally so scores remain after closing the game, and potentially extend to a leaderboard via a simple backend.

While a persistent high score system adds replay value, it requires file I/O operations (for local saving), which introduce complexities like error handling, file permissions, and data serialisation. Given the project's focus on architectural refactoring (MVC) and core gameplay mechanics (Ghost Piece, Hard Drop), the time investment for a robust persistence layer was deemed lower priority than stabilising the refactored codebase.

5) Alternative game modes

Modes beyond the standard "Marathon" style, such as "Sprint" (clear 40 lines as fast as possible) or "Ultra" (score as much as possible in 2 minutes) or “Gravity change” (linked to space theme, where gravity could change randomly). This would require different end-game conditions and scoring rules. 

Implementing multiple game modes would necessitate a more complex GameService or Board hierarchy to handle varying rulesets and win conditions. To ensure the MVC refactoring was clean and the base game was bug free, the scope was limited to the standard endless mode.

6) Tutorial

An interactive walkthrough that teaches the player controls (move, rotate, hard drop) and mechanics (clearing lines, scoring) step-by-step, rather than just a static "How to Play" screen.

Creating an interactive tutorial requires a scripted event system (e.g., "Wait for player to press Space") and a specialised UI state. This level of scripting and state management was outside the scope of the maintenance and extension goals, which focused on improving the existing game architecture.

7) Add profiles

The ability for multiple users to create named profiles, each with their own separate high scores, settings, and keybinds.

A profile system significantly increases the complexity of data management (creating, saving, loading, and switching user data). Without a database or a complex file structure, managing multiple user configurations robustly is difficult and was considered a "nice-to-have" feature rather than a core requirement.

8) Customisable key binds

A settings menu allowing players to remap actions (e.g., Move Left, Rotate) to any key of their choice, rather than hardcoded keys.

While InputHandler was refactored to support this theoretically, building the UI for key capture and validation (preventing conflicts) and saving these preferences would have consumed significant development time. The default controls (Arrows/WASD) cover the vast majority of use cases.

9) Interactive effects (e.g. if row or game board cleared, effect occurs)

Advanced visual feedback such as particle explosions when lines are cleared, screen shake on hard drops, or animated transitions for leveling up.

JavaFX's standard rendering pipeline is not optimized for high-performance particle systems compared to game engines. Implementing smooth, performant particle effects would require complex custom rendering logic or external libraries, which might have destabilised the application performance for purely aesthetic gain.

New Java Classes
================

The following classes were created for this project.

InputHandler

*   **Location:** com.comp2042.gameUI
    
*   **Purpose:** Intercepts raw keyboard inputs (e.g., KeyCode.SPACE, KeyCode.UP) and translates them into abstract game events (e.g., HARD\_DROP, ROTATE).
    
*   **Reason for Creation:** To prevent the GuiController from becoming cluttered with large switch statements for key detection. It centralises control mapping, making it easier to add new keybinds (like WASD support) or disable inputs (e.g. during Paused state) in one specific place.
    

GameViewRenderer

*   **Location:** com.comp2042.gameUI
    
*   **Purpose:** The dedicated View component in the MVC architecture. It is responsible for all JavaFX drawing operations, including rendering the main game board, the active falling piece, the ghost piece, and the "Next/Hold" side panels.
    
*   **Reason for Creation:** This is the most critical refactoring step. The original code mixed logical calculations (where the block is) with drawing instructions (draw a rectangle here). This class accepts a data only snapshot (ViewData) and handles all the specific JavaFX node updates (Rectangle, GridPane, Color). This separation allows the game logic to be tested without needing a running GUI and makes the visual style easier to modify.
    

SoundOrganiser

*   **Location:** com.comp2042.gameUI
    
*   **Purpose:** Handles the loading, playback, looping, and volume control of audio resources (specifically background music) using javafx.scene.media.
    
*   **Reason for Creation:** Audio management involves specific exception handling (e.g., missing files) and state management (play/stop/mute). Moving this complexity into a helper class keeps the GuiController clean and focused purely on game flow, rather than low-level media resource management.
    

TimelineManager

*   **Location**: com.comp2042.gameUI
    
*   **Purpose**: Manages the game’s timing loop using a JavaFX Timeline. It triggers periodic MoveEvent’s (gravity) that drives the game forward
    
*   **Reason for Creation**: To isolate the timing logic from the UI updates. By isolating it here, we can easily start, stop or adjust the game speed without risking side effects in the rendering or input handling code. 
    

NotificationManager

*   **Location:** com.comp2042.gameUI
    
*   **Purpose:** Manages temporary visual feedback elements, specifically the floating score pop-ups that appear when lines are cleared.
    
*   **Reason for Creation:** Visual feedback requires creating temporary UI nodes, animating them, and removing them from the scene graph. This is purely cosmetic logic. Separating it ensures that the core game logic remains unaware of these visual effects, adhering to the separation of concerns principle.
    

Modified Java Classes
=====================

The following classes were modified to allow the game to run smoother. Other classes might have also been modified but only received minor modifications.

GuiController

*   **Location:** com.comp2042.gameUI
    
*   **Purpose:** Acts as the Controller (Mediator). It initializes the application, routes user inputs to the logic layer, manages the game loop state (Pause/Resume), and handles High Score persistence.
    

**Reason for Modification:**

*   **MVC Implementation:** Stripped of all drawing and game rule logic. It now delegates drawing to GameViewRenderer and logic to SimpleBoard.
    
*   **UI Extension:** Modified to handle the new interactive menus (Start, Pause, Game Over) and side panels (Next, High Score).
    
*   **Persistence:** Added file I/O logic to save and load the High Score (highscore.dat).
    
*   **Audio Integration:** Connected to SoundOrganiser to handle music toggling.
    

SimpleBoard

**Location:** com.comp2042.logic.game

**Purpose:** Represents the Model. It contains the grid data (int\[\]\[\]), handles collision detection, and manages game rules (line clearing, spawning).

**Reason for Modification:**

*   **Refactoring:** Removed all UI dependencies (e.g., javafx.scene.paint.Color, Rectangle). The board now only deals with integers and logic, making it unit-testable.
    
*   **Ghost Piece:** Added getGhostY() to calculate where the piece will land.
    
*   **Hard Drop:** Added hardDrop() to allow instant locking of pieces.
    
*   **Hold Logic:** Added holdPiece() to manage swapping the current brick with a saved one.
    

ViewData

**Location:** com.comp2042.logic.game

**Purpose:** An immutable Data Transfer Object (DTO) that captures a snapshot of the game state (brick positions, shapes, ghost coordinates) to be sent to the Renderer.

**Reason for Modification:**

*   **Added ghostY:** To support the new Ghost Piece feature, the logic layer needs to calculate the safe landing row and pass it to the renderer.
    
*   **Added nextBrickData & heldBrickData:** To support the "Next Piece" and "Hold" UI panels, the view needs access to these shapes without directly accessing the BrickGenerator or internal Board state.
    

Unexpected Problems
===================

1) Audio dependency crashes

Issue: The application crashed immediately upon startup when trying to play music

Resolution: I discovered that the media module is not included by default. I had to manually add the org.openjfx:javafx-media dependency to the pom.xml

2) “How to play” screen sizing

Issue: The text in the “How to Play” was too large pushing the Back button off the bottom of the screen, making it impossible to close the menu

Resolution: I standardise the font sizes in the FXML (reducing the title and body text sizes) and condensed the spacing to ensure all UI elements remain visible within the default 600x510 window style

3) Ghost piece rendering 

Issue: Initially, the Ghost Piece would persist on the screen after the block moved, or draw over existing blocks.

Resolution: I updated the GameViewRenderer to clear and redraw the specific grid cells dynamically based on the updated ViewData on every tick, ensuring the ghost piece only renders in valid empty spaces.

