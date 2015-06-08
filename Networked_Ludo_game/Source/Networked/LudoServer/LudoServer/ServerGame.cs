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

namespace LudoServer
{
    class ServerGame : Game
    {
        public ServerGame()
        {

        }

        public void InitGame()
        {
            InitBoard();
            currentState = GameStates.Player1Turn;
        }

        private Stream SerializeData()
        {
            Stream stream = new MemoryStream();
            BinaryFormatter formatter = new BinaryFormatter();
            stream.Position = 0;
            formatter.Serialize(stream, this.board);
            formatter.Serialize(stream, this.playerList);

            return stream;
        }

        private void DeSerializeData(Stream stream)
        {
            try
            {
                BinaryFormatter formatter = new BinaryFormatter();
                             
                this.board = (Token[])formatter.Deserialize(stream);
                this.playerList = (Player[])formatter.Deserialize(stream);
            }
            catch (IOException e)
            {
                Console.WriteLine("Deserialization error: " + e.Message);
            }
        }

        public bool Send(Socket player, Player.PlayerColor playerID)
        {
            try
            {
                MemoryStream memory = new MemoryStream();
                memory = (MemoryStream)SerializeData();
                BinaryWriter writer = new BinaryWriter(memory);
                writer.Write((int)currentState); // send the current state (who's turn) 
                writer.Write((int)playerID); // 
                player.Send(memory.GetBuffer()); // sends the current state, the player and the state of the board 
                return true;
            }
            catch (SocketException e)
            {
                Console.WriteLine("socket send error: " + e.Message);
                return false;
            }
        }

        public bool Receive(Socket player)
        {
            try
            {
                byte[] buffer = new byte[4096];
                int length = player.Receive(buffer);

                MemoryStream memory = new MemoryStream(buffer, 0, length);
                BinaryReader reader = new BinaryReader(memory);
                DeSerializeData(memory);

                if (hasWon()) // check if game is over
                {
                    if (GetActivePlayer() == Player.PlayerColor.Red)
                    {
                        currentState = GameStates.Player1Win;
                    }
                    else if (GetActivePlayer() == Player.PlayerColor.Yellow)
                    {
                        currentState = GameStates.Player2Win;
                    }
                    else if (GetActivePlayer() == Player.PlayerColor.Blue)
                    {
                        currentState = GameStates.Player3Win;
                    }
                    else
                    {
                        currentState = GameStates.Player4Win;
                    }
                }
                else
                {
                    if (currentState == GameStates.Player1Turn)
                    {
                        currentState = GameStates.Player2Turn;
                    }
                    else if (currentState == GameStates.Player2Turn)
                    {
                        currentState = GameStates.Player3Turn;
                    }
                    else if (currentState == GameStates.Player3Turn)
                    {
                        currentState = GameStates.Player4Turn;
                    }
                    else
                    {
                        currentState = GameStates.Player1Turn;
                    }
                    
                }
                

                return true;
            }
            catch (SocketException e)
            {
                Console.WriteLine("socket receive error: " + e.Message);
                return false;
            }

        }

        public Player.PlayerColor GetActivePlayer() // returns active player depending on the game state
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

        public bool hasWon()
        {
            // get current player, has he now scored 4 or more? if so then change state to that player won
            if (this.currentState == GameStates.Player1Turn)
            {
                if (this.playerList[0].score >= 4)
                {
                    return true;
                }
            }
            else if (this.currentState == GameStates.Player2Turn)
            {
                if (this.playerList[1].score >= 4)
                {
                    return true;
                }
            }
            else if (this.currentState == GameStates.Player3Turn)
            {
                if (this.playerList[2].score >= 4)
                {
                    return true;
                }
            }
            else
            {
                if (this.playerList[3].score >= 4)
                {
                    return true;
                }
                
            }
            return false;
        }

    }
}
