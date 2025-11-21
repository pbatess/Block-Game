# Block-Game
A Java-based puzzle game built on a quad-tree board structure, where each cell can be subdivided, rotated, reflected, or regenerated. Players manipulate a color-based grid to maximize their score based on different objectives. Includes recursive data structures, grid rendering, and complete gameplay logic.

# Demo
[Click here to view live demo](https://drive.google.com/file/d/1HX-LR-CJq77oFFZmUSq_Ydj0s4xjyk78/view?usp=drive_link)

# How to Play
**Objective**

Each Player assigned a goal, either:

- **Blob Goal:** Score based on the largest connected area of a target color. 
- **Perimeter Goal:** Score based on how much perimeter of the board is the target color.

Players alternate turns trying to improve their score, or sabotaging the other player.

**Players**

- Includes a single and two player mode.
- Players take turns in 2 player mode and each receive a different random goal type and target color.

**Actions**

- **Smash:** splits a block into four smaller blocks.
- **Rotate Clockwise**
- **Rotate Counterclockwise**
- **Reflect Horizontal**
- **Reflect Vertical**

**Setup**
- Maximum subdivision for smashing is chosen by user at the start.
- Players choose how many turns they each get.

# File Overview
- **Block.java:** Core quad-tree board implementation. Handles random generation, subdivision, block transformations (rotate, reflect, smash), board flattening, and locating selected blocks.
- **PerimeterGoal.java:** Scoring logic for the perimeter goal. Counts how many unit cells of the target color lie along the board’s outer edge, with corners counting double.
- **BlobGoal.java:** Scoring logic for the blob goal. Uses recursive search to find the size of the largest connected region of the target color.
- **BlockGame.java:** Main game driver and Swing UI. Manages rendering, player input, turn progression, score updates, and game setup.
- **BlockToDraw.java:** Lightweight drawing helper storing color, shape, and stroke settings for rendering each visible block.
- **GameColors.java:** Centralized color palette and highlight/frame definitions used throughout the game.
- **Goal.java:** Parent class for scoring strategies. Stores the target color and defines the interface for computing a score and generating a description.

# Tech Stack
- **Language:** Java, Swing Graphics
- **Environment:** IntelliJ IDEA
- **Core Concepts:** OOP, GUI, Quad-tree


