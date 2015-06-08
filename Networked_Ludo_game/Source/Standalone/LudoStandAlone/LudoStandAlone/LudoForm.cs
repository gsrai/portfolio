using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

/*
 * Gagondeep Srai
 * W1374553
 * Networking games design and implementation (EICG602)
 * Coursework 2
 */

namespace LudoStandAlone
{
    public partial class LudoForm : Form
    {
        LudoGui ludoClient;
        Button[] path = new Button[52];
        Button[] redHome = new Button[5];
        Button[] yellowHome = new Button[5];
        Button[] blueHome = new Button[5];
        Button[] greenHome = new Button[5];
        Button[] redTokenYard = new Button[4];
        Button[] yellowTokenYard = new Button[4];
        Button[] blueTokenYard = new Button[4];
        Button[] greenTokenYard = new Button[4];

        List<Button> playable = new List<Button>();

        public int dieResult = 0;
        public bool hasDieBeenRolled = false;
        public bool alreadyRendered = false;
        public bool hasAlreadyGottenPlayable = false;

        public int tokenPosition; // the token position if near the home row
        public bool twoPlayer = false;
        public bool threePlayer = false;
        public bool end = false;

        public LudoForm()
        {
            InitializeComponent();

            Application.ApplicationExit += delegate { Quit(); };

            ludoClient = new LudoGui();
            ludoClient.init(this);
            this.initGui();
        }

        private void initGui()
        {
            // add the path in order starting from the red starting square.
            path[0] = this.redStartSquare;
            path[1] = this.trackSquare1;
            path[2] = this.trackSquare2;
            path[3] = this.trackSquare3;
            path[4] = this.trackSquare4;
            path[5] = this.trackSquare5;
            path[6] = this.trackSquare6;
            path[7] = this.trackSquare7;
            path[8] = this.trackSquare8;
            path[9] = this.trackSquare9;
            path[10] = this.trackSquare10;
            path[11] = this.trackSquare11;
            path[12] = this.trackSquare12;

            path[13] = this.yellowStartSquare;
            path[14] = this.trackSquare13;
            path[15] = this.trackSquare14;
            path[16] = this.trackSquare15;
            path[17] = this.trackSquare16;
            path[18] = this.trackSquare17;
            path[19] = this.trackSquare18;
            path[20] = this.trackSquare19;
            path[21] = this.trackSquare20;
            path[22] = this.trackSquare21;
            path[23] = this.trackSquare22;
            path[24] = this.trackSquare23;
            path[25] = this.trackSquare24;

            path[26] = this.blueStartSquare;
            path[27] = this.trackSquare25;
            path[28] = this.trackSquare26;
            path[29] = this.trackSquare27;
            path[30] = this.trackSquare28;
            path[31] = this.trackSquare29;
            path[32] = this.trackSquare30;
            path[33] = this.trackSquare31;
            path[34] = this.trackSquare32;
            path[35] = this.trackSquare33;
            path[36] = this.trackSquare34;
            path[37] = this.trackSquare35;
            path[38] = this.trackSquare36;

            path[39] = this.greenStartSquare;
            path[40] = this.trackSquare37;
            path[41] = this.trackSquare38;
            path[42] = this.trackSquare39;
            path[43] = this.trackSquare40;
            path[44] = this.trackSquare41;
            path[45] = this.trackSquare42;
            path[46] = this.trackSquare43;
            path[47] = this.trackSquare44;
            path[48] = this.trackSquare45;
            path[49] = this.trackSquare46;
            path[50] = this.trackSquare47;
            path[51] = this.trackSquare48;

            // red home column
            redHome[0] = this.redHomeColumn1;
            redHome[1] = this.redHomeColumn2;
            redHome[2] = this.redHomeColumn3;
            redHome[3] = this.redHomeColumn4;
            redHome[4] = this.redHomeColumn5;

            // yellow home column
            yellowHome[0] = this.yellowHomeColumn1;
            yellowHome[1] = this.yellowHomeColumn2;
            yellowHome[2] = this.yellowHomeColumn3;
            yellowHome[3] = this.yellowHomeColumn4;
            yellowHome[4] = this.yellowHomeColumn5;

            // blue home column
            blueHome[0] = this.blueHomeColumn1;
            blueHome[1] = this.blueHomeColumn2;
            blueHome[2] = this.blueHomeColumn3;
            blueHome[3] = this.blueHomeColumn4;
            blueHome[4] = this.blueHomeColumn5;

            // green home column
            greenHome[0] = this.greenHomeColumn1;
            greenHome[1] = this.greenHomeColumn2;
            greenHome[2] = this.greenHomeColumn3;
            greenHome[3] = this.greenHomeColumn4;
            greenHome[4] = this.greenHomeColumn5;

            // red yard
            redTokenYard[0] = this.redToken1;
            redTokenYard[1] = this.redToken2;
            redTokenYard[2] = this.redToken3;
            redTokenYard[3] = this.redToken4;

            // yellow yard
            yellowTokenYard[0] = this.yellowToken1;
            yellowTokenYard[1] = this.yellowToken2;
            yellowTokenYard[2] = this.yellowToken3;
            yellowTokenYard[3] = this.yellowToken4;

            // blue yard
            blueTokenYard[0] = this.blueToken1;
            blueTokenYard[1] = this.blueToken2;
            blueTokenYard[2] = this.blueToken3;
            blueTokenYard[3] = this.blueToken4;

            // green yard
            greenTokenYard[0] = this.greenToken1;
            greenTokenYard[1] = this.greenToken2;
            greenTokenYard[2] = this.greenToken3;
            greenTokenYard[3] = this.greenToken4;

            // initially we want all the buttons un enabled
            for (int i = 0; i < 52; i++)
            {
                path[i].Enabled = false;
            }

            for (int i = 0; i < 5; i++)
            {
                redHome[i].Enabled = false;
                yellowHome[i].Enabled = false;
                blueHome[i].Enabled = false;
                greenHome[i].Enabled = false;
            }

            for (int i = 0; i < 4; i++)
            {
                redTokenYard[i].Enabled = false;
                yellowTokenYard[i].Enabled = false;
                blueTokenYard[i].Enabled = false;
                greenTokenYard[i].Enabled = false;
            }

            this.homeSquare.Enabled = false;
        }



        private void buttonRollDice_Click(object sender, EventArgs e)
        {
            Random r = new Random();
            
            for (int i = 0; i < 10; i++)
            {
                // using the for loop to increase the randomness of the dice roll
                
                dieResult = r.Next(6) + 1;
            }

            this.labelDiceRoll.Text = dieResult.ToString();
            this.hasDieBeenRolled = true;
        }

        private delegate void UpdateDelegate(LudoGui game);

        public void update(LudoGui game)
        {
            if (buttonRollDice.InvokeRequired)
            {
                try
                {
                    Invoke(new UpdateDelegate(update), new object[] { game });
                }
                catch (System.Exception)
                {
                    Console.WriteLine("error");
                }
            }
            else
            {

                this.setScore(Player.PlayerColor.Red, ludoClient.playerList[0].score);
                this.setScore(Player.PlayerColor.Yellow, ludoClient.playerList[1].score);
                this.setScore(Player.PlayerColor.Blue, ludoClient.playerList[2].score);
                this.setScore(Player.PlayerColor.Green, ludoClient.playerList[3].score);

                if (hasDieBeenRolled)
                {
                    this.buttonRollDice.Enabled = false;
                    this.labelStatus.Text = "Waiting for current Player action.";
                }
                else
                {
                    if (!end)
                    {
                        this.buttonRollDice.Enabled = true;
                        this.labelStatus.Text = "Waiting for current Player to roll.";
                    }
                }

                if (!alreadyRendered)
                {
                    Render(game);
                }
                
                switch (game.currentState)
                {
                    case Game.GameStates.StartGame:
                        game.currentState = Game.GameStates.Player1Turn;
                        break;
                    case Game.GameStates.Player1Turn:
                        this.labelTurn.Text = "Red Turn";
                        this.labelColor.Text = "Red";

                        if (hasDieBeenRolled)
                        {
                            if (dieResult == 6)
                            {
                                // allow user to click on yard token
                                if (!game.startSquareBusy())
                                {
                                    stateOfYard(game, true);
                                }
                                
                                // and allow other pieces on board to move 6 spaces
                                if (!hasAlreadyGottenPlayable)
                                {
                                    getPlayable(game);
                                }
                                // now wait for user io

                                if (playable.Count == 0 && game.playerList[0].isYardEmpty()) // if you cant play anything then skip turn
                                {
                                    input();
                                }
                            }

                            if (dieResult < 6)
                            {
                                // move peice on board and end turn, else end turn if no piece on board.
                                // check for collision here by passing die result, if collision then move to 1 block before else take the enemy.
                                if (game.playerList[0].hasTokenOnBoard())
                                {
                                    // find the position of all tokens, unlock the buttons that are dieresult away
                                    if (!hasAlreadyGottenPlayable)
                                    {
                                        getPlayable(game);
                                    }
                                }
                                else
                                {
                                    game.currentState = Game.GameStates.Player2Turn;
                                    hasDieBeenRolled = false;
                                }
                            }
                        }

                        break;
                    case Game.GameStates.Player2Turn:
                        this.labelTurn.Text = "Yellow Turn";
                        this.labelColor.Text = "Yellow";

                        if (hasDieBeenRolled)
                        {
                            if (dieResult == 6)
                            {
                                // allow user to click on yard token
                                if (!game.startSquareBusy())
                                {
                                    stateOfYard(game, true);
                                }
                                // and allow other pieces on board to move 6 spaces
                                if (!hasAlreadyGottenPlayable)
                                {
                                    getPlayable(game);
                                }
                                // now wait for user io
                                if (playable.Count == 0 && game.playerList[1].isYardEmpty()) // if you cant play anything then skip turn
                                {
                                    input();
                                }
                            }

                            if (dieResult < 6)
                            {
                                // move peice on board and end turn, else end turn if no piece on board.
                                if (game.playerList[1].hasTokenOnBoard())
                                {
                                    // find the position of all tokens, unlock the buttons that are dieresult away
                                    if (!hasAlreadyGottenPlayable)
                                    {
                                        getPlayable(game);
                                    }
                                }
                                else
                                {
                                    if (twoPlayer)
                                    {
                                        // if two player has been selected then change state to player1
                                        ludoClient.currentState = Game.GameStates.Player1Turn;
                                    }
                                    else
                                    {
                                        ludoClient.currentState = Game.GameStates.Player3Turn;
                                    }
                                    hasDieBeenRolled = false;
                                }
                            }
                        }

                        break;
                    case Game.GameStates.Player3Turn:
                        this.labelTurn.Text = "Blue Turn";
                        this.labelColor.Text = "Blue";

                        if (hasDieBeenRolled)
                        {
                            if (dieResult == 6)
                            {
                                // allow user to click on yard token
                                if (!game.startSquareBusy())
                                {
                                    stateOfYard(game, true);
                                }
                                // and allow other pieces on board to move 6 spaces
                                if (!hasAlreadyGottenPlayable)
                                {
                                    getPlayable(game);
                                }
                                // now wait for user io

                                if (playable.Count == 0 && game.playerList[2].isYardEmpty()) // if you cant play anything then skip turn
                                {
                                    input();
                                }
                            }

                            if (dieResult < 6)
                            {
                                // move peice on board and end turn, else end turn if no piece on board.
                                if (game.playerList[2].hasTokenOnBoard())
                                {
                                    // find the position of all tokens, unlock the buttons that are dieresult away
                                    if (!hasAlreadyGottenPlayable)
                                    {
                                        getPlayable(game);
                                    }
                                }
                                else
                                {
                                    if (threePlayer)
                                    {
                                        // if three player has been selected then change state to player1
                                        ludoClient.currentState = Game.GameStates.Player1Turn;
                                    }
                                    else
                                    {
                                        ludoClient.currentState = Game.GameStates.Player4Turn;
                                    } 
                                    hasDieBeenRolled = false;
                                }
                            }
                        }

                        break;
                    case Game.GameStates.Player4Turn:
                        this.labelTurn.Text = "Green Turn";
                        this.labelColor.Text = "Green";

                        if (hasDieBeenRolled)
                        {
                            if (dieResult == 6)
                            {
                                // allow user to click on yard token
                                if (!game.startSquareBusy())
                                {
                                    stateOfYard(game, true);
                                }
                                // and allow other pieces on board to move 6 spaces
                                if (!hasAlreadyGottenPlayable)
                                {
                                    getPlayable(game);
                                }
                                // now wait for user io
                                if (playable.Count == 0 && game.playerList[3].isYardEmpty()) // if you cant play anything then skip turn
                                {
                                    input();
                                }
                            }

                            if (dieResult < 6)
                            {
                                // move peice on board and end turn, else end turn if no piece on board.
                                if (game.playerList[3].hasTokenOnBoard())
                                {
                                    // find the position of all tokens, unlock the buttons that are dieresult away
                                    if (!hasAlreadyGottenPlayable)
                                    {
                                        getPlayable(game);
                                    }
                                }
                                else
                                {
                                    game.currentState = Game.GameStates.Player1Turn;
                                    hasDieBeenRolled = false;
                                }
                            }
                        }

                        break;
                    case Game.GameStates.Player1Win:
                        this.labelTurn.Text = "Red Wins";
                        this.labelStatus.Text = "End of Game, Congratz!";
                        this.buttonRollDice.Enabled = false;
                        this.end = true;
                        break;
                    case Game.GameStates.Player2Win:
                        this.labelTurn.Text = "Yellow Wins";
                        this.labelStatus.Text = "End of Game, Congratz!";
                        this.buttonRollDice.Enabled = false;
                        this.end = true;
                        break;
                    case Game.GameStates.Player3Win:
                        this.labelTurn.Text = "Blue Wins";
                        this.labelStatus.Text = "End of Game, Congratz!";
                        this.buttonRollDice.Enabled = false;
                        this.end = true;
                        break;
                    case Game.GameStates.Player4Win:
                        this.labelTurn.Text = "Green Wins";
                        this.labelStatus.Text = "End of Game, Congratz!";
                        this.buttonRollDice.Enabled = false;
                        this.end = true;
                        break;
                    case Game.GameStates.PlayAgain:
                        game.currentState = Game.GameStates.StartGame;
                        break;
                }
            }
        }

        private void Render(LudoGui game)
        {
            alreadyRendered = true;
            ClearScreen();
            // draw the game from the model!!!

            // draw the board:
            for (int x = 0; x < 52; x++)
            {
                if (game.board[x] != null)
                {
                    if (game.board[x].color == Player.PlayerColor.Red)
                        path[x].BackgroundImage = Properties.Resources.redToken; 

                    if (game.board[x].color == Player.PlayerColor.Yellow)
                        path[x].BackgroundImage = Properties.Resources.yellowToken; 
                    
                    if (game.board[x].color == Player.PlayerColor.Blue)
                        path[x].BackgroundImage = Properties.Resources.blueToken; 
                    
                    if (game.board[x].color == Player.PlayerColor.Green)
                        path[x].BackgroundImage = Properties.Resources.greenToken; 
                }
            }
               
            // draw home rows
            for (int i = 0; i < 4; i++)
            {
                if (game.playerList[0].tokens[i].position != -1)
                    redHome[game.playerList[0].tokens[i].position].BackgroundImage = Properties.Resources.redToken;

                if (game.playerList[1].tokens[i].position != -1)
                    yellowHome[game.playerList[1].tokens[i].position].BackgroundImage = Properties.Resources.yellowToken;

                if (game.playerList[2].tokens[i].position != -1)
                    blueHome[game.playerList[2].tokens[i].position].BackgroundImage = Properties.Resources.blueToken;

                if (game.playerList[3].tokens[i].position != -1)
                    greenHome[game.playerList[3].tokens[i].position].BackgroundImage = Properties.Resources.greenToken;
            }

            // draw yards
            for (int i = 0; i < 4; i++)
            {
                if (game.playerList[0].tokens[i].isInYard)
                    redTokenYard[i].BackgroundImage = Properties.Resources.redToken;

                if (game.playerList[1].tokens[i].isInYard)
                    yellowTokenYard[i].BackgroundImage = Properties.Resources.yellowToken;
                
                if (game.playerList[2].tokens[i].isInYard)
                    blueTokenYard[i].BackgroundImage = Properties.Resources.blueToken;
               
                if (game.playerList[3].tokens[i].isInYard)
                    greenTokenYard[i].BackgroundImage = Properties.Resources.greenToken;
            }
            
        }

        private void ClearScreen()
        {
            // remove background image of every button
            // draw the board:
            for (int x = 0; x < 52; x++)
            {
                path[x].BackgroundImage = null;
            }

            // draw home rows
            for (int i = 0; i < 5; i++)
            {
                redHome[i].BackgroundImage = null;
                yellowHome[i].BackgroundImage = null;
                blueHome[i].BackgroundImage = null;
                greenHome[i].BackgroundImage = null;
            }

            // draw yards
            for (int i = 0; i < 4; i++)
            {
                redTokenYard[i].BackgroundImage = null;
                yellowTokenYard[i].BackgroundImage = null;
                blueTokenYard[i].BackgroundImage = null;
                greenTokenYard[i].BackgroundImage = null;
            }
        }

        private void stateOfYard(LudoGui game, bool state)
        {
            switch (game.currentState)
            {
                case Game.GameStates.Player1Turn:
                    for (int i = 0; i < 4; i++)
                    {
                        if (game.playerList[0].tokens[i].isInYard)
                        {
                            redTokenYard[i].Enabled = state;
                        }
                    }

                    break;
                case Game.GameStates.Player2Turn:
                    for (int i = 0; i < 4; i++)
                    {
                        if (game.playerList[1].tokens[i].isInYard)
                        {
                            yellowTokenYard[i].Enabled = state;
                        }
                    }

                    break;
                case Game.GameStates.Player3Turn:
                    for (int i = 0; i < 4; i++)
                    {
                        if (game.playerList[2].tokens[i].isInYard)
                        {
                            blueTokenYard[i].Enabled = state;
                        }
                    }

                    break;
                case Game.GameStates.Player4Turn:
                    for (int i = 0; i < 4; i++)
                    {
                        if (game.playerList[3].tokens[i].isInYard)
                        {
                            greenTokenYard[i].Enabled = state;
                        }
                    }

                    break;

            }
        }

        private void Quit()
        {
            ludoClient.Shutdown();
            ludoClient = null;
        }

        private void endOfTurn()
        {
            // here you change the next player based on 2player, 3player 
            // when you place a piece, you turn is over. call this whenever you click on a path or home row button.
            if (dieResult < 6)
            {
                switch (ludoClient.currentState)
                {
                    case Game.GameStates.Player1Turn:
                        ludoClient.currentState = Game.GameStates.Player2Turn;
                        break;
                    case Game.GameStates.Player2Turn:
                        if (twoPlayer) 
                        {
                            // if two player has been selected then change state to player1
                            ludoClient.currentState = Game.GameStates.Player1Turn;
                        }
                        else
                        {
                            ludoClient.currentState = Game.GameStates.Player3Turn;
                        }
                        break;
                    case Game.GameStates.Player3Turn:
                        if (threePlayer) 
                        {
                            // if three player has been selected then change state to player1
                            ludoClient.currentState = Game.GameStates.Player1Turn;
                        }
                        else
                        {
                            ludoClient.currentState = Game.GameStates.Player4Turn;
                        }
                        break;
                    case Game.GameStates.Player4Turn:
                        ludoClient.currentState = Game.GameStates.Player1Turn;
                        break;
                }
            }
        }

        private void getPlayable(LudoGame game)
        {
            hasAlreadyGottenPlayable = true;
            List<int> tokensInPlay;

            tokensInPlay = game.getAllTokensInPlay();
            foreach (int tokenPos in tokensInPlay)
            {
                // here we move to the home column game.board[tokenPos]
                if (ludoClient.canMoveToHomeColumn(tokenPos, dieResult, game.board[tokenPos].color))
                {
                    getPlayableHomeRow(game.board[tokenPos].color, tokenPos + dieResult);
                    this.tokenPosition = tokenPos;
                }
                else
                {
                    path[getFixDieRoll(tokenPos + dieResult)].Enabled = true;
                    playable.Add(path[getFixDieRoll(tokenPos + dieResult)]);
                }
            }
                
            // get all the tokens on the board that are the same color as the turn color (aka playable
            // put them in the list
            // enable buttons diceroll ahead
            // whichever one is clicked, callse set playable false, changes the board(moves token) and sets diceroll and render and calls non sixroll

            // what about the tokens not in play aka on the board, the tokens that are in their respective home rows/columns
            // get the player color from current state, then check the players token array for positions != -1, these are in the home row.
            // then unlock these home row buttons, however if the roll is greater than the length but not the roll needed to get home, then skip

            List<Token> tokensInHomeCol = game.getAllTokensInHomeColumn(); // gets all the tokens in the home row/column for the current player
            // calc using die roll if you should unlock buttons or home or none !!!!
            foreach (Token t in tokensInHomeCol)
            {
                int newPos = t.position + dieResult;

                if (newPos > 5)
                {
                    this.labelStatus.Text = "roll to high to move home.";
                    // do not unlock or move
                    if (playable.Count < 1 && dieResult != 6) // if the player has no tokens in play and die roll is not 6
                        input();
                } 
                else if (newPos == 5)
                {
                    // unlock home.
                    this.homeSquare.Enabled = true;
                    playable.Add(this.homeSquare); // they rolled exactly to the home square
                }
                else if (newPos < 5) {
                    // unlock homecolumn square of index newPos
                    Player.PlayerColor color = t.color;

                    if (color == Player.PlayerColor.Red)
                    {
                        redHome[newPos].Enabled = true;
                        playable.Add(redHome[newPos]);
                    }
                    else if (color == Player.PlayerColor.Yellow)
                    {
                        yellowHome[newPos].Enabled = true;
                        playable.Add(yellowHome[newPos]);
                    }
                    else if (color == Player.PlayerColor.Blue)
                    {
                        blueHome[newPos].Enabled = true;
                        playable.Add(blueHome[newPos]);
                    }
                    else if (color == Player.PlayerColor.Green)
                    {
                        greenHome[newPos].Enabled = true;
                        playable.Add(greenHome[newPos]);
                    }
                }
            }
        }

        private void resetPlayable()
        {
            // gets called by all buttons on the path.
            foreach (Button b in playable)
            {
                b.Enabled = false;
            }
            playable = new List<Button>();
            hasAlreadyGottenPlayable = false;
        }

        private void getPlayableHomeRow(Player.PlayerColor color, int newPos)
        {
            if (color == Player.PlayerColor.Red)
            {
                int homePos = newPos - 51;
                if (homePos >= 5)
                {
                    this.homeSquare.Enabled = true;
                    playable.Add(this.homeSquare); // they rolled exactly to the home square
                }
                else
                {
                    redHome[homePos].Enabled = true;
                    playable.Add(redHome[homePos]);
                }
            }
            else if (color == Player.PlayerColor.Yellow)
            {
                int homePos = newPos - 12;
                if (homePos >= 5)
                {
                    this.homeSquare.Enabled = true;
                    playable.Add(this.homeSquare);
                }
                else
                {
                    yellowHome[homePos].Enabled = true;
                    playable.Add(yellowHome[homePos]);
                }
            }
            else if (color == Player.PlayerColor.Blue)
            {
                int homePos = newPos - 25;
                if (homePos >= 5)
                {
                    this.homeSquare.Enabled = true;
                    playable.Add(this.homeSquare);
                }
                else
                {
                    blueHome[homePos].Enabled = true;
                    playable.Add(blueHome[homePos]);
                }
            }
            else if (color == Player.PlayerColor.Green)
            {
                int homePos = newPos - 38;
                if (homePos >= 5)
                {
                    this.homeSquare.Enabled = true;
                    playable.Add(this.homeSquare);
                }
                else
                {
                    greenHome[homePos].Enabled = true;
                    playable.Add(greenHome[homePos]);
                }
            }
        }

        private int getFixDieRoll(int overflowVal)
        {
            if (overflowVal >= 52)
            {
                return overflowVal - 52; // overflowVal will be out of bounds, we want it to roll over so we take away our bounds to get the roll overed index
            }

            return overflowVal;
        }

        private void input()
        {
            // whenever the user does something you want to reset these variables
            stateOfYard(ludoClient, false);
            resetPlayable();
            hasDieBeenRolled = false; // user has taken an action
            alreadyRendered = false; // user has taken an action so redraw
            endOfTurn(); // this will only work if not a double roll.
            this.labelDiceRoll.Text = "0";
        }

        public void setScore(Player.PlayerColor color, int score)
        {
            if (color == Player.PlayerColor.Red)
            {
                this.labelRedScore.Text = score.ToString();
            }
            else if (color == Player.PlayerColor.Yellow)
            {
                this.labelYellowScore.Text = score.ToString();
            }
            else if (color == Player.PlayerColor.Blue)
            {
                this.labelBlueScore.Text = score.ToString();
            }
            else if (color == Player.PlayerColor.Green)
            {
                this.labelGreenScore.Text = score.ToString();
            }
        }


        private void redToken1_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[0].tokens[0].color, 0);
            input();
        }

        private void redToken2_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[0].tokens[1].color, 1);
            input();
        }

        private void redToken3_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[0].tokens[2].color,2);
            input();
        }

        private void redToken4_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[0].tokens[3].color, 3);
            input();
        }

        private void yellowToken1_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[1].tokens[0].color, 0);
            input();
        }

        private void yellowToken2_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[1].tokens[1].color, 1);
            input();
        }

        private void yellowToken3_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[1].tokens[2].color, 2);
            input();
        }

        private void yellowToken4_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[1].tokens[3].color, 3);
            input();
        }

        private void blueToken1_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[2].tokens[0].color, 0);
            input();
        }

        private void blueToken2_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[2].tokens[1].color, 1);
            input();
        }

        private void blueToken3_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[2].tokens[2].color, 2);
            input();
        }

        private void blueToken4_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[2].tokens[3].color, 3);
            input();
        }

        private void greenToken1_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[3].tokens[0].color, 0);
            input();
        }

        private void greenToken2_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[3].tokens[1].color, 1);
            input();
        }

        private void greenToken3_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[3].tokens[2].color, 2);
            input();
        }

        private void greenToken4_Click(object sender, EventArgs e)
        {
            stateOfYard(ludoClient, false);
            ludoClient.moveToBoard(ludoClient.playerList[3].tokens[3].color, 3);
            input();
        }

        private void redStartSquare_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(0 - dieResult, 0);
            input();
        }

        private void trackSquare1_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(1 - dieResult, 1);
            input();
        }

        private void trackSquare2_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(2 - dieResult, 2);
            input();
        }

        private void trackSquare3_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(3 - dieResult, 3);
            input();
        }

        private void trackSquare4_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(4 - dieResult, 4);
            input();
        }

        private void trackSquare5_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(5 - dieResult, 5);
            input();
        }

        private void trackSquare6_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(6 - dieResult, 6);
            input();
        }

        private void trackSquare7_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(7 - dieResult, 7);
            input();
        }

        private void trackSquare8_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(8 - dieResult, 8);
            input();
        }

        private void trackSquare9_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(9 - dieResult, 9);
            input();
        }

        private void trackSquare10_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(10 - dieResult, 10);
            input();
        }

        private void trackSquare11_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(11 - dieResult, 11);
            input();
        }

        private void trackSquare12_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(12 - dieResult, 12);
            input();
        }

        private void yellowStartSquare_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(13 - dieResult, 13);
            input();
        }

        private void trackSquare13_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(14 - dieResult, 14);
            input();
        }

        private void trackSquare14_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(15 - dieResult, 15);
            input();
        }

        private void trackSquare15_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(16 - dieResult, 16);
            input();
        }

        private void trackSquare16_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(17 - dieResult, 17);
            input();
        }

        private void trackSquare17_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(18 - dieResult, 18);
            input();
        }

        private void trackSquare18_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(19 - dieResult, 19);
            input();
        }

        private void trackSquare19_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(20 - dieResult, 20);
            input();
        }

        private void trackSquare20_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(21 - dieResult, 21);
            input();
        }

        private void trackSquare21_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(22 - dieResult, 22);
            input();
        }

        private void trackSquare22_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(23 - dieResult, 23);
            input();
        }

        private void trackSquare23_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(24 - dieResult, 24);
            input();
        }

        private void trackSquare24_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(25 - dieResult, 25);
            input();
        }

        private void blueStartSquare_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(26 - dieResult, 26);
            input();
        }

        private void trackSquare25_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(27 - dieResult, 27);
            input();
        }

        private void trackSquare26_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(28 - dieResult, 28);
            input();
        }

        private void trackSquare27_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(29 - dieResult, 29);
            input();
        }

        private void trackSquare28_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(30 - dieResult, 30);
            input();
        }

        private void trackSquare29_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(31 - dieResult, 31);
            input();
        }

        private void trackSquare30_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(32 - dieResult, 32);
            input();
        }

        private void trackSquare31_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(33 - dieResult, 33);
            input();
        }

        private void trackSquare32_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(34 - dieResult, 34);
            input();
        }

        private void trackSquare33_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(35 - dieResult, 35);
            input();
        }

        private void trackSquare34_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(36 - dieResult, 36);
            input();
        }

        private void trackSquare35_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(37 - dieResult, 37);
            input();
        }

        private void trackSquare36_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(38 - dieResult, 38);
            input();
        }

        private void greenStartSquare_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(39 - dieResult, 39);
            input();
        }

        private void trackSquare37_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(40 - dieResult, 40);
            input();
        }

        private void trackSquare38_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(41 - dieResult, 41);
            input();
        }

        private void trackSquare39_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(42 - dieResult, 42);
            input();
        }

        private void trackSquare40_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(43 - dieResult, 43);
            input();
        }

        private void trackSquare41_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(44 - dieResult, 44);
            input();
        }

        private void trackSquare42_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(45 - dieResult, 45);
            input();
        }

        private void trackSquare43_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(46 - dieResult, 46);
            input();
        }

        private void trackSquare44_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(47 - dieResult, 47);
            input();
        }

        private void trackSquare45_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(48 - dieResult, 48);
            input();
        }

        private void trackSquare46_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(49 - dieResult, 49);
            input();
        }

        private void trackSquare47_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(50 - dieResult, 50);
            input();
        }

        private void trackSquare48_Click(object sender, EventArgs e)
        {
            ludoClient.movePiece(51 - dieResult, 51);
            input();
        }

        private void homeSquare_Click(object sender, EventArgs e)
        {
            // change the score check for win else end turn
            if (dieResult == 6)
            {
                // move token to home row
                ludoClient.moveToHomeColumn(this.tokenPosition, 1, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
                dieResult--;
            }

            ludoClient.moveToHome(dieResult);
            input();
        }

        private void redHomeColumn1_Click(object sender, EventArgs e)
        {
            // check if a token is in the home row, and it has the same pos as this button - dieroll, then change its position
            if (!ludoClient.moveTokenInHome(0, dieResult))
            {
                // moves to the right place by calculating via tokenPos and die result. the correct square is unlocked when we find playable
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            }
            input();
        }

        private void redHomeColumn2_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(1, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void redHomeColumn3_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(2, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void redHomeColumn4_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(3, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void redHomeColumn5_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(4, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void yellowHomeColumn1_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(0, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void yellowHomeColumn2_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(1, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void yellowHomeColumn3_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(2, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void yellowHomeColumn4_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(3, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void yellowHomeColumn5_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(4, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void blueHomeColumn1_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(0, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void blueHomeColumn2_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(1, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void blueHomeColumn3_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(2, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void blueHomeColumn4_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(3, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void blueHomeColumn5_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(4, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void greenHomeColumn1_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(0, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void greenHomeColumn2_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(1, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void greenHomeColumn3_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(2, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void greenHomeColumn4_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(3, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void greenHomeColumn5_Click(object sender, EventArgs e)
        {
            if (!ludoClient.moveTokenInHome(4, dieResult))
                ludoClient.moveToHomeColumn(this.tokenPosition, this.dieResult, ludoClient.board[this.tokenPosition].color, ludoClient.board[this.tokenPosition]);
            input();
        }

        private void button2P_Click(object sender, EventArgs e)
        {
            this.button2P.Enabled = false;
            this.button3P.Enabled = false;
            this.twoPlayer = true;
        }

        private void button3P_Click(object sender, EventArgs e)
        {
            this.button2P.Enabled = false;
            this.button3P.Enabled = false;
            this.threePlayer = true;
        }

    }
}
