<p align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="Java" width="132">
</p>


<p align="center">
  <strong>Plugin Paper na nagrody discord.</strong>
</p>

<p align="center">
  <img alt="release" src="https://img.shields.io/badge/release-1.0-2f81f7?style=flat-square">
  <img alt="paper" src="https://img.shields.io/badge/Paper-1.20.6%2B-43b581?style=flat-square">
  <img alt="java" src="https://img.shields.io/badge/Java-17%2B-f89820?style=flat-square">
</p>


<h1 align="center">FEPNagrody</h1>

## Instalacja:
 1. Pobierz plik FepNagrody.jar
 2.  Wrzuć go do folderu plugins na twoim serwerze
 3. Wystartuj serwer
 4. Wrzuć do configu token twojego bota i channelid 
 5. Zrestartuj serwer

## Config:

<details>
  <summary>config.yml</summary>

  <pre><code>bot-token: exampletoken # NIE UDOSTEPNIAJ NIKOMU!!!
discord-channel-id: exampleid

# Komendy której mają się wykonać na przyznanie nagrody
commands:
  - give %player% diamond 5

# Wiadomość dla każdego gracza na serwerze
broadcast-message: "&aGracz %player% odebrał nagrodę discord ty też możesz to zrobić wchodząc na: discord.gg/twojlink"

# Wygląd Embeda po próbie odebrania nagrody ponownie
embed-recieved:
  title: "Otrzymales juz Nagrodę!"
  color: "#E31E1E"
  description:
    - "Otrzymałeś już nagrodę!"
    - "Nie możesz odebrać jej kilka razy"
  thumbnail: "https://cdn.discordapp.com/attachments/1358719974629445733/1495199181927612567/content.png"

# Wygląd Embeda po nie znalezieniu gracza na serwerze
embed-notOnline:
  title: "Tego Gracze nie ma na serwerze!"
  color: "#E31E1E"
  description:
    - "Ten gracz jest offline/lub nie ma go na serwerze!"
    - "Prosze upewnij się że dobrze wpisałeś nick!"
  thumbnail: "https://cdn.discordapp.com/attachments/1358719974629445733/1495199181927612567/content.png"

# Wygląd Embeda po pomyślnym otrzymaniu nagrody
embed-Success:
  title: "Odebrales Nagrodę!"
  color: "#0DD939"
  description:
    - "Właśnie odebrałeś nagrodę za discord!"
    - "Za chwilę powinieneś otrzymać nagrodę w Minecraft!"
  thumbnail: "https://cdn.discordapp.com/attachments/1358719974629445733/1495199611936051310/content.png"</code></pre>
</details>

## Bot Token
**Token twojego bota discord możesz znaleść:**
 - https://discord.com/developers/applications

## ChannelID
**ChannelID możesz znaleść na discordzie kliknij na kanał**
**PPM i kliknij kopiuj ID Kanału**


## Licencja

Ten projekt jest udostępniany na licencji GNU GPL v3.0.

<p align="center">Copyright (C) 2026 Fepbox</p>
