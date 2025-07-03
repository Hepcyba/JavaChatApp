# 💬 Java Chat Application

A real-time, multi-user chat app built with Java Sockets, multithreading, and a Swing GUI.

---

## 🚀 Features

- 👥 Multi-client support with Java multithreading
- 🔐 GUI-based login (username + password)
- 💬 Public and private messaging (`@username`)
- 🟢 Live online user list display
- 🖥️ Clean and simple Java Swing interface
- 💾 Daily server-side chat logs: `chatlog-YYYY-MM-DD.txt`
- 🔌 Real-time socket communication (port `1234`)
- ❌ Graceful disconnect handling

---

## 🗂️ Project Structure
``JavaChatApp/
├── client/
│ └── Client.java
├── server/
│ ├── Server.java
│ └── ClientHandler.java
├── .gitignore
└── README.md``
## 🖼️ Screenshots

[chat1](https://github.com/user-attachments/assets/dae6276c-7bbf-4227-8a38-6a7e92a1dc56)
[chat2](https://github.com/user-attachments/assets/89c13335-234a-4c54-a4fd-56ca3d8dccd5)

🧰 Built With
Java 17+
Java Socket Programming
Java Swing GUI
Java Multithreading

## 🛠️ How to Run

### 🔁 Start the Server

```bash
cd server
javac Server.java ClientHandler.java
java Server

💬 Start a Client
cd client
javac Client.java
java Client
📸 Sample Conversation
✅ Server started. Waiting for clients on port 1234...
✅ User logged in: john
✅ User logged in: katy

katy: Hello everyone!
john: @katy Hi Katy!

👤 Developer
G. Hepcyba
B.Tech CSE | VNRVJIET '26
🔗 LinkedIn

