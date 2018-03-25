#!/usr/bin/env bash
spark-submit \
    --class fr.soat.batch.ParisTrees \
    --master "spark://master:7077" \
	--num-executors 2 \
	--executor-cores 1 \
	--executor-memory 1G \
    /tmp/upload/spark-hubtalk-epitech-assembly-1.0.jar "true"