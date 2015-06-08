using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PlayerTokenClassLibrary;
using System.Net;
using System.Net.Sockets;
using System.Threading;

/*
 * Gagondeep Srai
 * W1374553
 * Networking games design and implementation (EICG602)
 * Coursework 2
 */

namespace LudoServer
{
    class Program
    {
        static void Main(string[] args)
        {

            Socket serverSocket;

            Socket player1;
            Socket player2;
            Socket player3;
            Socket player4;

            ServerGame myGame;

            while (true)
            {
                myGame = new ServerGame();  // instance of the game
                serverSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp); // standard socket
                try
                {
                    IPEndPoint ipLocal = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 8221); // server ip
                    serverSocket.Bind(ipLocal); // assign the socket to that ip and port
                    serverSocket.Listen(4);  // listen on that ip and port
                    Console.WriteLine("Waiting for clients ...");


                    player1 = serverSocket.Accept(); // accept a connection, call that player 1
                    Console.WriteLine("Player 1 joined");

                    player2 = serverSocket.Accept();  // accept a connection, call that player 2
                    Console.WriteLine("Player 2 joined");

                    player3 = serverSocket.Accept(); // accept a connection, call that player 3
                    Console.WriteLine("Player 3 joined");

                    player4 = serverSocket.Accept();  // accept a connection, call that player 4
                    Console.WriteLine("Player 4 joined");

                    myGame.InitGame(); // initialize the game on the server

                    while (true)
                    {
                        if (!myGame.Send(player1, Player.PlayerColor.Red)) Console.WriteLine("player 1 forfeited the game");
                        if (!myGame.Send(player2, Player.PlayerColor.Yellow)) Console.WriteLine("player 2 forfeited the game");
                        if (!myGame.Send(player3, Player.PlayerColor.Blue)) Console.WriteLine("player 3 forfeited the game");
                        if (!myGame.Send(player4, Player.PlayerColor.Green)) Console.WriteLine("player 4 forfeited the game");

                        Console.WriteLine("Sent Data");

                        if (myGame.GetActivePlayer() == Player.PlayerColor.Red)
                        {
                            Console.WriteLine("Red waiting to Recieve");
                            if (!myGame.Receive(player1)) Console.WriteLine("player 1 forfeited the game");
                        }
                        else if (myGame.GetActivePlayer() == Player.PlayerColor.Yellow)
                        {
                            Console.WriteLine("Yellow waiting to Recieve");
                            if (!myGame.Receive(player2)) Console.WriteLine("player 2 forfeited the game");
                        }
                        else if (myGame.GetActivePlayer() == Player.PlayerColor.Blue)
                        {
                            Console.WriteLine("Blue waiting to Recieve");
                            if (!myGame.Receive(player3)) Console.WriteLine("player 3 forfeited the game");
                        }
                        else
                        {
                            Console.WriteLine("Green waiting to Recieve");
                            if (!myGame.Receive(player4)) Console.WriteLine("player 4 forfeited the game");
                        }
                        Console.WriteLine("Recieved Data");
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine("all clients disconnected, game abandoned " + e.Message);
                }
                serverSocket.Close();
            }

        }
    }
}
