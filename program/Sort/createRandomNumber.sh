#!/bin/bash

num_random_limit=32767
num_random_end=100000
num_1=1
num_2=1
num_3=1
num_sum=0

while(( $num_1<=10))
do
    echo "==="
    while(( $num_2<=100 ))
    do

        while(( $num_sum<=100 ))
        do
            num_random=$RANDOM

            if [ $num_sum -gt $num_random_limit ] 
            then  num_random=`expr $num_random_end - $num_random`
                  echo "=======$num_random======="
            fi

            echo $num_random
            echo -n "$num_random " >> file

            let num_3++
            let num_sum++

            echo "counter: $num_sum"
        done

        

        num_3=1
        let num_2++

    done

    echo '' >> file

    num_2=1

    let num_1++

done

echo "number: $num_sum"

