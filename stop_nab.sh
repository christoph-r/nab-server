#!/bin/bash
ps -ef | grep 'NabServer' | grep -v grep | awk '{print $2}' | xargs kill -9 $1
