import requests
from flask import Flask, request, json

from telegram import *
from telegram.ext import *

import logging
import os

global bot
global dispatcher

app = Flask(__name__)

logging.basicConfig(level=logging.INFO,
                    format="%(asctime)s %(levelname)s - %(message)s")

logger = logging.getLogger(__name__)


WEBHOOK_URL = os.environ['WEBHOOK_URL']
TELEGRAM_TOKEN = os.environ['TELEGRAM_TOKEN']
GROUP_CHAT_ID = os.environ['GROUP_CHAT_ID']

logger.debug("WEBHOOK_URL: " + WEBHOOK_URL)
logger.debug("QNSS_TOKEN: " + TELEGRAM_TOKEN)
logger.debug("GROUP_CHAT_ID: " + GROUP_CHAT_ID)


def listToString(l):
    string = ""
    for el in l:
        string += "\- `" + el + "`\n"

    return string


def setTelegramWebHook(token, webhook):
    url = "https://api.telegram.org/bot{}/setWebhook?url={}/telegram-webhook".format(
        token, webhook)
    res = requests.get(url)
    logger.info(res.text)


@app.route("/info", methods=['GET'])
def info():
    url = "https://api.telegram.org/bot{}/getWebhookInfo".format(
        TELEGRAM_TOKEN)
    res = requests.get(url)
    return json.loads(res.text)


@app.route("/telegram-webhook", methods=['GET', 'POST'])
def telegramWebHook():
    post_data = request.get_json()
    logger.debug("New Telegram message:\n" + json.dumps(post_data, indent=1))

    update = Update.de_json(post_data, bot)
    dispatcher.process_update(update)

    return "Data received", 200


@app.route("/git-webhook", methods=['GET', 'POST'])
def gitWebHook():
    post_data = request.get_json()
    logger.debug("New Git message:\n" + json.dumps(post_data, indent=1))

    res = ""
    for commit in post_data["commits"]:

        committer = commit["author"]["name"]
        message = "`" + commit["message"].replace(".", "\.") + "`"
        modified = commit["modified"]
        removed = commit["removed"]
        url = commit["url"]

        res += "🆙 *New [COMMIT](" + url + ") by " + committer + "*\n"
        res += message + "\n"

        if len(modified) > 0:
            res += "✅ Modified:\n" + listToString(modified) + "\n"

        if len(removed) > 0:
            res += "❌ Removed:\n" + listToString(removed) + "\n"

        logger.info("New commit...")
        logger.debug("Commit info:\n" + res)

        msg_sent_res = bot.send_message(
            GROUP_CHAT_ID, res, parse_mode=ParseMode.MARKDOWN_V2)

        # bot.unpinAllChatMessages(msg_sent.chat_id)
        # bot.pinChatMessage(msg_sent.chat_id, msg_sent.message_id)

    return "Data received", 200


if __name__ == "__main__":
    setTelegramWebHook(TELEGRAM_TOKEN, WEBHOOK_URL)

    bot = Bot(TELEGRAM_TOKEN)

    dispatcher = Dispatcher(bot, None, workers=0)

    app.run(host="0.0.0.0", port="5000")
