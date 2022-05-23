#!/bin/bash

FrodoFile="algorithms/Frodo/python3/nist_kat.py"

Kyber512P="algorithms/Kyber/kyber512"
Kyber512_90sP="algorithms/Kyber/kyber512-90s"
Kyber768P="algorithms/Kyber/kyber768"
Kyber768_90sP="algorithms/Kyber/kyber768-90s"
Kyber1024P="algorithms/Kyber/kyber1024"
Kyber1024_90sP="algorithms/Kyber/kyber1024-90s"

NTRU701P="algorithms/NTRU/ntruhrss701"
NTRU4096821P="algorithms/NTRU/ntruhps4096821"
NTRU2048677P="algorithms/NTRU/ntruhps2048677"
NTRU2048509P="algorithms/NTRU/ntruhps2048509"

run(){
    echo "${1}/PQCgenKAT_kem"
    rm "${1}/PQCgenKAT_kem"
    make -C $1
    "${1}/PQCgenKAT_kem"

    mv *.req files/${2}.req
    mv *.rsp files/${2}.rsp
}

chooseKyber(){
    if [ $1 == "Kyber512" ]; then
        run $Kyber512P Kyber512
    elif [ $1 == "Kyber512_90s" ]; then
        run $Kyber512_90sP Kyber512-90s
    elif [ $1 == "Kyber768" ]; then
        run $Kyber768P Kyber768
    elif [ $1 == "Kyber768_90s" ]; then
        run $Kyber768_90sP Kyber768-90s
    elif [ $1 == "Kyber1024" ]; then
        run $Kyber1024P Kyber1024
    elif [ $1 == "Kyber1024_90s" ]; then
        run $Kyber1024_90sP Kyber1024-90s
    else
        echo "Not valid Kyber type"
    fi
}


chooseNTRU(){
    if [ $1 == "NTRU701" ]; then
        run $NTRU701P NTRU701
    elif [ $1 == "NTRU4096" ]; then
        run $NTRU4096821P NTRU4096
    elif [ $1 == "NTRU2048v1" ]; then
        run $NTRU2048677P NTRU2048v1
    elif [ $1 == "NTRU2048v2" ]; then
        run $NTRU2048509P NTRU2048v2
    else
        echo "Not valid NTRU type"
    fi
}

chooseFrodo(){
    if [ $1 == "FrodoKEM-640" ]; then
        python $FrodoFile $1 > files/FrodoKEM-640
    elif [ $1 == "FrodoKEM-976" ]; then
        python $FrodoFile $1 > files/FrodoKEM-976
    elif [ $1 == "FrodoKEM-1344" ]; then
        python $FrodoFile $1 > files/FrodoKEM-1344
    else
        echo "Not valid Frodo type"
    fi

}

if [[ $1 == *"Kyber"* ]]; then
    chooseKyber $1
elif [[ $1 == *"NTRU"* ]]; then
    chooseNTRU $1
elif [[ $1 == *"Frodo"* ]]; then
    chooseFrodo $1
elif [[ $1 == "AES" ]]; then
    openssl enc -aes-256-cbc -in pom.xml -out files/aes.enc -k PASS
else
   echo "Maybe try something else..."
fi