# Setup guide

### 1. Firebase (for notifications)
Follow the article to setup firebase
https://medium.com/@jishnusaha89/firebase-cloud-messaging-push-notifications-with-angular-1fb7f173bfba   
Then save the firebase server key as "FIREBASE_SERVER_KEY" your environment variables
Note: if switching between multiple clients/browsers, please update the notification token (found in the settings page)  

### 2. JWT Signing key
Create a >512 bit key in your environment variable as "JWT_SIGNKEY"  

### 3. LTA Datamall key
Go to Singapore's Land Transport Authority website to obtain a Datamall key and save as "LTA_DATAMALL_API" in environment variables  

### 4. Telegram bot token
Go to Botfather in Telegram to obtain a bot token and save as "TELEGRAM_BOT_TOKEN" in environment variables  
(Note: for bus stop search to work, the table bus_stop should be populated. Please log in as admin to populate the database)  

### 5. MySQL
Create a MySQL database and in the environment variables, save the username as "DB_USER", password as "DB_PASS", url as "DB_URL"  

