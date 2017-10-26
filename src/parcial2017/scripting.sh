#!/bin/bash

if [ ! -f $1 ]; then
  echo "Error en archivo de puntajes."
  exit
fi

if [ ! -f $2 ]; then
  echo "Error en archivo de resultados."
  exit
fi

OLDIFS=IFS
IFS='
'
for linea_puntaje in $(cat $1); do
  pais_act=$(echo $linea_puntaje | cut -f1)
  puntaje_act=$(echo $linea_puntaje | cut -f2)
  
  linea_res=$(cat $2 | grep $pais_act)
  
  if [ ! -z $linea_res ]; then
    if [[ $pais_act == $(echo $linea_res | cut -f1) ]]; then
      goles_act=$(echo $linea_res | cut -f3)
      goles_otro=$(echo $linea_res | cut -f4)
    else
      goles_act=$(echo $linea_res | cut -f4)
      goles_otro=$(echo $linea_res | cut -f3)
    fi
    if (( $goles_act > $goles_otro )); then
      let puntaje_act=$puntaje_act+3
    elif (( $goles_act == $goles_otro )); then
      let puntaje_act=$puntaje_act+1
    fi
  fi

  echo $pais_act	$puntaje_act
done
IFS=OLDIFS

