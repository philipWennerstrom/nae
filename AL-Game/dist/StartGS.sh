#!/bin/bash

case $1 in
noloop)
  [ -d log/ ] || mkdir log/
  [ -f log/console.log ] && mv log/console.log "log/backup/`date +%Y-%m-%d_%H-%M-%S`_console.log"
  java -Xms2048m -Xmx8192m -ea -Xbootclasspath/p:libs/jsr166-1.7.0.jar -javaagent:./libs/al-commons-1.3.jar -cp ./libs/*:AL-Game.jar com.aionemu.gameserver.GameServer > log/console.log 2>&1
  echo $! > gameserver.pid
  echo "Server started!"
  ;;
*)
  ./StartGS_loop.sh &
  ;;
esac