#!/bin/bash

num_1=1;
num_2=1;

num_sum=1;

while(( $num_1<=100))
do
        echo "==="
        while(( $num_2<=100 ))
        do
                echo $num_sum
                echo -n "$num_sum " >> file
                        let num_2++
                        let num_sum++

        done

        echo '' >> file

        num_2=1

        let num_1++

done
