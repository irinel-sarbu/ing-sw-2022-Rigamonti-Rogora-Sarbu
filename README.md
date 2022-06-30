# Prova Finale di Ingegneria del Software - AA 2021-2022

![Eriantys](src/main/resources/Graphical_Assets/eriantys.jpg)

Implementazione del gioco da tavolo [Eriantys](https://www.craniocreations.it/prodotto/eriantys/).

---

## Librerie e plugins

| Libreria/Plugin | Descrizione                                                                                                    |
|-----------------|----------------------------------------------------------------------------------------------------------------|
| Maven           | Strumento di gestione di progetti software basati su Java e build automation                                   |
| JUnit           | Framework di unit testing per il linguaggio di programmazione Java                                             |
| JavaFx          | Famiglia di software applicativi, basati sulla piattaforma Java, per la creazione di rich Internet application |

---

## Funzionalità

| Funzionalità        | Stato               |
|---------------------|---------------------|
| Regole semplificate | :heavy_check_mark:  |
| Regole complete     | :heavy_check_mark:  |
| Socket              | :heavy_check_mark:  |
| Cli                 | :heavy_check_mark:  |
| Gui                 | :heavy_check_mark:  |

| Funzionalità avanzate          | Stato              |
|--------------------------------|--------------------|
| Carte personaggio              | :heavy_check_mark: |
| Partite a 4 giocatori          | :x:                |
| Partite multiple               | :heavy_check_mark: |
| Persistenza                    | :x:                |
| Resilienza alle disconnessioni | :x:                |

---

## Tests

Il seguente è il report ottenuto da Jacoco

| Elemento | Percentuale |
|----------|-------------|
| Model    | 95%         |
| Totale   | 86%         |

---

## OS Support

| OS      | x86_64             | Arm                |
|---------|--------------------|--------------------|
| Windows | :heavy_check_mark: | :x:                |
| Linux   | :heavy_check_mark: | :x:                |
| MacOS   | :x:                | :heavy_check_mark: |

---

## Esecuzione

Questo progetto richiede una versione di Java 17.

### Eriantys client

Le seguenti istruzioni descrivono come eseguire il client con interfaccia CLI o GUI.

### Cli

Per eseguire il Client CLI digitare da terminale uno dei seguenti comandi:

> La versione cli è supportata solamente su Linux e MacOs. Potrebbe dare problemi su Windows

```
java -jar eriantys-{OS}.jar --cli
```

```
java -jar eriantys-{OS}.jar -c
```

### Gui

Per eseguire il Client GUI digitare da terminale il seguente comando:

```
java -jar eriantys-{OS}.jar
```

### Eriantys Server

Per eseguire il Server digitare da terminale il seguente comando:

```
java -jar eriantys-{OS}.jar --server
```

```
java -jar eriantys-{OS}.jar -s
```

E' possibile usare una porta diversa da quella di default usando il seguente comando:

```
java -jar eriantys-{OS}.jar --server --port=CUSTOM_PORT
```

```
java -jar eriantys-{OS}.jar -s --port=CUSTOM_PORT
```

---

## Jar

Al seguente link sono disponibili gli eseguibili:

- [Latest release](https://github.com/irinel-sarbu/ing-sw-2022-Rigamonti-Rogora-Sarbu/releases/latest)

## Componenti del gruppo

- [_**Rigamonti Alberto**_](https://github.com/Alberto1Rigamonti)
- [_**Rogora Matteo**_](https://github.com/teo3300)
- [_**Sarbu Razvan Irinel**_](https://github.com/irinel-sarbu)
