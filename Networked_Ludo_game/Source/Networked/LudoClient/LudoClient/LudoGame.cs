using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using PlayerTokenClassLibrary;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;

/*
 * Gagondeep Srai
 * W1374553
 * Networking games design and implementation (EICG602)
 * Coursework 2
 */

namespace LudoClient
{
    public class LudoGame : Game
    {
        public Player.PlayerColor PlayerID;

        public LudoGame()
        {
            InitBoard();
        }

        public void moveToHome(int dieRoll)
        {
            List<Token> tokens = this.getAllTokensInHomeColumn();
            // pos + die roll == 5 

            foreach (Token t in tokens) // all tokens in current player home row
            {
                if (t.position + dieRoll == 5) // if its in the home row then add it 
                {
                    t.isHome = true;
                    t.position = -1;
                    this.incrementScore();

                    if (t.color == Player.PlayerColor.Red)
                    {
                        this.playerList[0].tokens[t.id].isHome = true;
                        this.playerList[0].tokens[t.id].position = -1;
                    }
                    if (t.color == Player.PlayerColor.Yellow)
                    {
                        this.playerList[1].tokens[t.id].isHome = true;
                        this.playerList[1].tokens[t.id].position = -1;
                    }
                    if (t.color == Player.PlayerColor.Blue)
                    {
                        this.playerList[2].tokens[t.id].isHome = true;
                        this.playerList[2].tokens[t.id].position = -1;
                    }
                    if (t.color == Player.PlayerColor.Green)
                    {
                        this.playerList[3].tokens[t.id].isHome = true;
                        this.playerList[3].tokens[t.id].position = -1;
                    }
                }
            }
        }

        public bool moveTokenInHome(int squarePos, int dieRoll)
        {
            List<Token> tokensInHomeCol = this.getAllTokensInHomeColumn(); // gets all the tokens in the home row/column for the current player
            // calc using die roll if you should unlock buttons or home or none !!!!
            foreach (Token t in tokensInHomeCol)
            {
                if (t.position == squarePos - dieRoll)
                {
                    t.position = squarePos;
                    return true;
                }    
            }

            return false;
        }

        public List<int> getAllTokensInPlay()
        {
            List<int> tokenPositions = new List<int>();

            for (int x = 0; x < LENGTH; x++)
            {
                if (board[x] == null) continue;
                if (board[x].color == getColorFromCurrentState())
                {
                    tokenPositions.Add(x); // list as not all tokens may be in play
                }
            }

            return tokenPositions;
        }

        public List<Token> getAllTokensInHomeColumn()
        {
            List<Token> tokens = new List<Token>();
            Player.PlayerColor color = getColorFromCurrentState();

            if (color == Player.PlayerColor.Red)
            {
                foreach (Token t in playerList[0].tokens)
                {
                    if (t.position != -1) // if its in the home row then add it 
                        tokens.Add(t);
                }
            }
            else if (color == Player.PlayerColor.Yellow)
            {
                foreach (Token t in playerList[1].tokens)
                {
                    if (t.position != -1)
                        tokens.Add(t);
                }
            }
            else if (color == Player.PlayerColor.Blue)
            {
                foreach (Token t in playerList[2].tokens)
                {
                    if (t.position != -1)
                        tokens.Add(t);
                }
            }
            else if (color == Player.PlayerColor.Green)
            {
                foreach (Token t in playerList[3].tokens)
                {
                    if (t.position != -1)
                        tokens.Add(t);
                }
            }

            return tokens;
        }

        private Player.PlayerColor getColorFromCurrentState()
        {
            if (this.currentState == GameStates.Player1Turn)
            {
                return Player.PlayerColor.Red;
            }
            else if (this.currentState == GameStates.Player2Turn)
            {
                return Player.PlayerColor.Yellow;
            }
            else if (this.currentState == GameStates.Player3Turn)
            {
                return Player.PlayerColor.Blue;
            }
            else
            {
                return Player.PlayerColor.Green;
            }
        }

        // increases score on goal
        private void incrementScore()
        {
            if (this.currentState == GameStates.Player1Turn)
            {
                this.playerList[0].score++;
            }
            else if (this.currentState == GameStates.Player2Turn)
            {
                this.playerList[1].score++;
            }
            else if (this.currentState == GameStates.Player3Turn)
            {
                this.playerList[2].score++;
            }
            else
            {
                this.playerList[3].score++;
            }
        }

        public bool ReceiveServerInput(Socket server)
        {
            try
            {
                byte[] buffer = new byte[4096];
                int length = server.Receive(buffer);
                MemoryStream memory = new MemoryStream(buffer, 0, length);
                BinaryReader reader = new BinaryReader(memory);
                DeSerializeData(memory);
                currentState = (GameStates)reader.ReadInt32();
                PlayerID = (Player.PlayerColor)reader.ReadInt32();

                return true;
            }
            catch (Exception e)
            {
                Console.WriteLine("error " + e.Message);
                return false;
            }

        }

        private void DeSerializeData(Stream stream)
        {
            try
            {
                BinaryFormatter formatter = new BinaryFormatter();

                this.board = (Token[])formatter.Deserialize(stream);
                this.playerList = (Player[])formatter.Deserialize(stream);
            }
            catch (Exception e)
            {
                Console.WriteLine("Deserialization error: " + e);
            }
            
        }

        public bool IsMyGo()
        {
            return (PlayerID == Player.PlayerColor.Red && currentState == GameStates.Player1Turn)
                | (PlayerID == Player.PlayerColor.Yellow && currentState == GameStates.Player2Turn)
                | (PlayerID == Player.PlayerColor.Blue && currentState == GameStates.Player3Turn)
                | (PlayerID == Player.PlayerColor.Green && currentState == GameStates.Player4Turn);
        }

    }
}
