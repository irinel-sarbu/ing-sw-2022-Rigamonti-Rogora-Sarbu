FROM python:3.8-bullseye

WORKDIR /bot
COPY . /bot
RUN pip install --trusted-host pypi.python.org -r requirements.txt

EXPOSE 5000

CMD ["python3", "main.py"]