using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
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
    public class LudoGui : LudoGame
    {
        public bool quit;
        bool connected = false;
        public LudoForm form;
        public Thread gameThread;
        public Socket serverSocket;
        public IPEndPoint ipLocal;
        bool hasSent = false;

        public void init(LudoForm f)
        {
            serverSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            ipLocal = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 8221);
            
            while(!connected)
            {
                try
                {
                    serverSocket.Connect(ipLocal);
                    quit = false;
                    form = f;
                    connected = true;
                    ReceiveServerInput(serverSocket);
                    gameThread = new Thread(new ParameterizedThreadStart(GameLoop));
                    gameThread.Start(this);
                }
                catch (Exception e)
                {
                    
                    Console.WriteLine("init error: " + e.Message);
                }
            }
            
        }

        void GameLoop(Object o)
        {
            LudoGui client = (LudoGui)o;

            while (!quit)
            {
                client.form.update(client);
                if (!IsMyGo()) hasSent = true; // if its not your go then wait to receive else
                if (hasSent)
                {
                    client.ReceiveServerInput(client.serverSocket);
                    form.alreadyRendered = false;
                    hasSent = false;
                }
            }
        }

        public void SendToServer()
        { 
            try
            {
                MemoryStream memory = new MemoryStream();
                memory = (MemoryStream)SerializeData();
                serverSocket.Send(memory.GetBuffer());
                hasSent = true;
            }
            catch(Exception e)
            {
                Console.WriteLine("Send Error: " + e.Message);
            }
            
        }

        private Stream SerializeData()
        {
            Stream stream = new MemoryStream();
            BinaryFormatter formatter = new BinaryFormatter();

            formatter.Serialize(stream, this.board);
            formatter.Serialize(stream, this.playerList);

            return stream;
        }

        public void Shutdown()
        {
            gameThread.Abort();
            gameThread = null;
        }

    }
}
