# Bot telegram git webhook

Project structure:
```
.
├── bot.env
├── docker-compose.yaml
├── Dockerfile
├── main.py
├── README.md
└── requirements.txt
```

## Deploy using docker-compose

Create a file named `bot.env` and insert the following information:
```bash
TELEGRAM_TOKEN=
GROUP_CHAT_ID=
WEBHOOK_URL=
```

I suggest you use [ngrok](https://ngrok.com/) if you want to use the bot locally.

To start an ngrok instance use:
```bash
ngrok http 80
```
Copy the http link and use it as the webhook_url.

\* Remember to also setup github webhook. Please use `"webhook_url"/git-webhook` as webhook url.

Then run docker-compose:
```bash
docker-compose up
```
