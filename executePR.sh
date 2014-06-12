#!/bin/bash
dfactor=0.85
epsilon=0.000001
output=./R02725005.pagerank

while getopts d:e:o: option
do
    case "${option}" in
    	d) dfactor=${OPTARG};;
    	e) epsilon=${OPTARG};;
    	o) output=${OPTARG};;
	esac
done

input=${@: -1};

java WEBIR_HW2_R02725005 $dfactor $epsilon $output $input
