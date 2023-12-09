# Boggle Game

Alan Castillo, Crystal Sheppard

HW X - Boggle Game

-------------------------------------------
Description of how to use the interface:
-------------------------------------------

The opening menu displays the "start game" button to begin the game, a "rules" button (which displays the Boggle game rules), and the "exit" button to exit the game.

To play the virtual game, the player plays similarly to a physical boggle game by writing their words on paper to keep track of their words. The board is "shaken" upon the start of the game by displaying a board of randomized letters. The player creates their words by clicking on the letters of the board to create the word they want to submit, then clicking the "submit word" button. The game keeps track of words that have already been used and checks the validity of words as they are submitted and lets the user know if the player has already submitted a word or if a submitted word is invalid. A timer begins from the time the board is displayed, and the user can submit words for the duration of the standard 3-minute Boggle game time. During game play, the player also has the option to reset the game by clicking the "reset game" button, or to rotate the letters on the board by clicking the "rotate letters" button.

The game keeps track of the highest score achieved by any player and displays the final score at the end of the round upon completion of the game time.


-----------------------------------------------------------------------
Details about implementation, how to run the program, etc:
-----------------------------------------------------------------------

No special compilation needed.

------------------------------------------------------------------------------
Details about extras needed to compile the code when unpacked:
------------------------------------------------------------------------------

Included in the tar/jar file are the text files needed to use the standard word list dictionaries for word validation (i.e. EnglishBoggleWords.txt and SpanishBoggleWords.txt)

----------------
Extra Features:
----------------
- There is a Spanish-language game option that allows the user to play the Spanish-language version of Boggle (inclusive of the letter ñ) that utilizes the standard Scrabble Spanish word list.
- The "Rotate Letters" option allows a player to rotate the letters on the board to achieve a different visual perspective to enhance game-play and usability.
- The highest score of any game is tracked and displayed at the end of the game and displayed with the highest-scoring user's name upon re-running the program/re-opening the game.
- A count-down timer is displayed when the game begins for the user to keep track of the remaining game time.
